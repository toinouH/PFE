package fr.univlyon3.sncf.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.univlyon3.sncf.models.FichierCave;
import fr.univlyon3.sncf.models.FrequentationGare;
import fr.univlyon3.sncf.models.Gare;
import fr.univlyon3.sncf.models.Region;
import fr.univlyon3.sncf.repositories.GareRepository;
import fr.univlyon3.sncf.repositories.FichierCaveRepository;
import fr.univlyon3.sncf.repositories.FrequentationGareRepository;
import fr.univlyon3.sncf.repositories.RegionRepository;
import fr.univlyon3.sncf.services.GareService;
import fr.univlyon3.sncf.transverse.XMLReader;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RegionRepository regionRepository,
            FichierCaveRepository fichierCaveRepository,
            GareRepository gareRepository,
            FrequentationGareRepository frequentationGareRepository,
            GareService gareService
    ) {
        return args -> {
            createRegionIfNotExists(regionRepository, "AQU", "Aquitaine", "aquitaine@sncf.fr");
            createRegionIfNotExists(regionRepository, "ARA", "Auvergne Rhône-Alpes", "ara@sncf.fr");
            createRegionIfNotExists(regionRepository, "BFC", "Bourgogne Franche-Comté", "bfc@sncf.fr");
            createRegionIfNotExists(regionRepository, "BRE", "Bretagne", "bre@sncf.fr");
            createGareIfNotExists(gareRepository,"Bordeaux Saint-Jean","Bordeaux","Bordeaux St-Jean","Gare de Bordeaux Saint-Jean",44.8253,-0.5560);
            
            String cheminXml = "input/FichierCAVE_AQU_X12345_29062022.xml";

            List<XMLReader.StopData> stops = gareService.lireStopsDepuisFichier(cheminXml);
            System.out.println("Nombre de stops lus : " + stops.size());
            if (!stops.isEmpty()) {
                        Gare gare = gareRepository.findByShortLabel("Bordeaux")
                .orElseThrow(() -> new RuntimeException("Gare Bordeaux introuvable"));

                frequentationGareRepository.deleteAll();

                FichierCave fichier = fichierCaveRepository.findAll().get(0);

                for (XMLReader.StopData stop : stops) {
                    Gare gareTrouvee = gareService.trouverPremiereGare(
                            stop.latitude(),
                            stop.longitude()
                    );

                    if (gareTrouvee != null) {
                        FrequentationGare frequentation = new FrequentationGare();
                        frequentation.setDateMesure(fichier.getDateCourse());
                        frequentation.setMonteesAdults(stop.totalIn());
                        frequentation.setDescentesAdults(stop.totalOut());
                        frequentation.setMonteesBikes(0);
                        frequentation.setDescentesBikes(0);
                        frequentation.setMonteesWheelchairs(0);
                        frequentation.setDescentesWheelchairs(0);
                        frequentation.setGare(gareTrouvee);
                        frequentation.setFichierCave(fichier);

                        frequentationGareRepository.save(frequentation);
                    }
                }
                System.out.println("Fréquentations créées pour tous les stops : " + stops.size());
            }
        
    

            if (fichierCaveRepository.count() == 0) {
                String nomFichier = "FichierCAVE_AQU_X12345_29062022.xml";
                
                String trigramme = gareService.extraireTrigramme(nomFichier);
                String vehicule = gareService.extraireVehicule(nomFichier);
                LocalDate dateCourse = gareService.extraireDateCourse(nomFichier);

                Region region = regionRepository.findByTrigramme(trigramme)
                        .orElseThrow(() -> new RuntimeException("Région " + trigramme + " introuvable"));

                FichierCave fichier = new FichierCave();
                fichier.setNomFichier(nomFichier);
                fichier.setDateReception(LocalDateTime.now());
                fichier.setDateCourse(dateCourse);
                fichier.setVehicule(vehicule);
                fichier.setStatutEnrichissement("ENRICHI");
                fichier.setTauxEnrichissement(95.0);
                fichier.setCheminFichierOriginal("src/main/resources/input/" + nomFichier);
                fichier.setCheminFichierEnrichi("output/" + nomFichier);
                fichier.setRegion(region);

                fichierCaveRepository.save(fichier);
                System.out.println("FichierCave créé.");
            }

            if (frequentationGareRepository.count() == 0) {
                Gare gare = gareRepository.findByShortLabel("Bordeaux")
                        .orElseThrow(() -> new RuntimeException("Gare Bordeaux introuvable"));

                FichierCave fichier = fichierCaveRepository.findAll().get(0);

                FrequentationGare frequentation = new FrequentationGare();
                frequentation.setDateMesure(LocalDate.of(2022, 6, 29));
                frequentation.setMonteesAdults(120);
                frequentation.setDescentesAdults(80);
                frequentation.setMonteesBikes(5);
                frequentation.setDescentesBikes(2);
                frequentation.setMonteesWheelchairs(1);
                frequentation.setDescentesWheelchairs(1);
                frequentation.setGare(gare);
                frequentation.setFichierCave(fichier);

                frequentationGareRepository.save(frequentation);
                System.out.println("FrequentationGare créée.");
            }
        };
    }

    private void createRegionIfNotExists(
            RegionRepository regionRepository,
            String trigramme,
            String nom,
            String email
    ) {
        if (regionRepository.findByTrigramme(trigramme).isEmpty()) {
            Region region = new Region();
            region.setTrigramme(trigramme);
            region.setNom(nom);
            region.setEmail(email);
            regionRepository.save(region);
            System.out.println("Région " + trigramme + " créée.");
        }
    }

    private void createGareIfNotExists(
        GareRepository gareRepository,
        String label,
        String shortLabel,
        String mediumLabel,
        String longLabel,
        double latitude,
        double longitude
    ) {
        if (gareRepository.findByShortLabel(shortLabel).isEmpty()) {
            Gare gare = new Gare();
            gare.setLabel(label);
            gare.setShortLabel(shortLabel);
            gare.setMediumLabel(mediumLabel);
            gare.setLongLabel(longLabel);
            gare.setLatitude(latitude);
            gare.setLongitude(longitude);
            gareRepository.save(gare);
            System.out.println("Gare " + shortLabel + " créée.");
        }
    }
    
}