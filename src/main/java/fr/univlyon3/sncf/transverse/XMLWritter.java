package fr.univlyon3.sncf.transverse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import fr.univlyon3.sncf.models.FichierCave;
import fr.univlyon3.sncf.models.Region;
import fr.univlyon3.sncf.repositories.FichierCaveRepository;
import fr.univlyon3.sncf.repositories.RegionRepository;
import fr.univlyon3.sncf.services.FrequentationGareService;
import jakarta.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component("xmlWritter")
public class XMLWritter {

    private static final Logger LOGGER = LogManager.getLogger(XMLWritter.class);

    @Resource(name = "xmlReader")
    private XMLReader reader;
    @Resource(name = "fichierCaveRepository")
    private FichierCaveRepository fichierCaveRepository;
    @Resource(name = "regionRepository")
    private RegionRepository regionRepository;
    @Resource(name = "nearestStations")
    private NearestStations nearestStationsGenerator;
    @Resource(name = "frequentationGareService")
    private FrequentationGareService frequentationGareService;

    private int counter;
    private int counterenrichi;
    private float tauxEnrichissement;

    public void enrichirXML(String inputXmlPath, String outputXmlPath) throws IOException {
        List<XMLReader.StopData> stops = reader.lireStops(inputXmlPath);

        String content;
        try (InputStream inputStream = Files.newInputStream(Paths.get(inputXmlPath))) {
            if (inputStream == null) {
                throw new IOException("Fichier XML introuvable : " + inputXmlPath);
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            content = result.toString(StandardCharsets.UTF_8.name());
        }

        StringBuilder enrichedContent = new StringBuilder();
        int lastPos = 0;
        counter = 0;                // A chaque fichier il faut reinitialiser le compteur à 0
        counterenrichi = 0;
        tauxEnrichissement = 0.0f;
        for (int i = 0; i < stops.size() -1 ; i++) {
            XMLReader.StopData currentStop = stops.get(i);
            String stopTag = "</Stop>";
            int stopEndIndex = content.indexOf(stopTag, lastPos) + stopTag.length();

            enrichedContent.append(content, lastPos, stopEndIndex);

            String xmlToInsert = nearestStationsGenerator.generateNearestStationsXML(
                    currentStop.latitude(), currentStop.longitude()
            );
            counter++;
            if (!xmlToInsert.isEmpty()) {
                enrichedContent.append("\n").append(xmlToInsert);
                counterenrichi++;
            }

            lastPos = stopEndIndex;
        }
        calculerTauxEnrichissement(counterenrichi, counter);

        enrichedContent.append(content.substring(lastPos));

        Path outputPath = construireCheminSortie(inputXmlPath, outputXmlPath);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, enrichedContent.toString());

        enregistrerEnBase(inputXmlPath, outputPath.toString(),stops);
    }


    private void calculerTauxEnrichissement(int counterenrichi, int counter) {
        this.tauxEnrichissement = (float) counterenrichi / counter * 100;
    }

    private void enregistrerEnBase(String inputXmlPath, String outputXmlPath, List<XMLReader.StopData> stops) {
        String nomFichier = Paths.get(inputXmlPath).getFileName().toString();
        Regions regionEnum = reader.extraireRegionDepuisNomFichier(nomFichier);

        Optional<Region> regionOpt = regionRepository.findByTrigramme(regionEnum.name());
        if (regionOpt.isEmpty()) {
            LOGGER.error("Région introuvable pour le fichier : " + nomFichier);
            return;
        }

        FichierCave fichierCave;

        Optional<FichierCave> fichierExistant = fichierCaveRepository.findByNomFichier(nomFichier);

        if (fichierExistant.isPresent()) {
            fichierCave = fichierExistant.get();
            LOGGER.warn("Fichier déjà présent en base : " + fichierCave.getNomFichier());
        } else {
            fichierCave = new FichierCave();
            fichierCave.setNomFichier(nomFichier);
            fichierCave.setDateReception(LocalDateTime.now());
            fichierCave.setCheminFichierEnrichi(outputXmlPath);
            fichierCave.setRegion(regionOpt.get());

            // On pourrait mettre ça dans un enum
            if (getTauxEnrichissement() == 0.0f) {
                fichierCave.setStatutEnrichissement("NON ENRICHI");
            } else if (getTauxEnrichissement() < 100.0f) {
                fichierCave.setStatutEnrichissement("PARTIELLEMENT ENRICHI");
            } else {    // Pas besoin de check pour 100% c'est déjà le cas si l'on arrive ici
                fichierCave.setStatutEnrichissement("ENRICHI");
            }

            fichierCave.setTauxEnrichissement(getTauxEnrichissement());

            // Extraction des métadonnées du nom de fichier : FichierCAVE_AQU_X12345_29062022.xml
            Pattern pattern = Pattern.compile("FichierCAVE_[A-Z]{3}_([^_]+)_(\\d{8})\\.xml");
            Matcher matcher = pattern.matcher(nomFichier);
            if (matcher.matches()) {
                fichierCave.setVehicule(matcher.group(1));
                String dateStr = matcher.group(2);
                LocalDate dateCourse = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("ddMMyyyy"));
                fichierCave.setDateCourse(dateCourse);
            } else {
                fichierCave.setVehicule("INCONNU");
                fichierCave.setDateCourse(LocalDate.now());
            }

            fichierCave = fichierCaveRepository.save(fichierCave);
            LOGGER.info("FichierCave enregistré : " + fichierCave.getNomFichier());
        }

        frequentationGareService.alimenterDepuisStops(fichierCave, stops);
        LOGGER.info("Fréquentations enregistrées pour : " + fichierCave.getNomFichier());
    }

    private Path construireCheminSortie(String inputXmlPath, String outputXmlPath) {
        Path dossierSortie = Paths.get(outputXmlPath);
        String nomFichierEntree = Paths.get(inputXmlPath).getFileName().toString();
        Regions region = reader.extraireRegionDepuisNomFichier(nomFichierEntree);
        String nomFichierSortie = nomFichierEntree.replace(".xml", "_enriched.xml");

        return dossierSortie.resolve(region.name()).resolve(nomFichierSortie);
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public float getTauxEnrichissement() {
        return tauxEnrichissement;
    }

    public void setTauxEnrichissement(float tauxEnrichissement) {
        this.tauxEnrichissement = tauxEnrichissement;
    }

    public int getCounterenrichi() {
        return counterenrichi;
    }

    public void setCounterenrichi(int counterenrichi) {
        this.counterenrichi = counterenrichi;
    }

}