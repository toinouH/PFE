package fr.univlyon3.sncf.transverse;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Component("xmlReader")
public class XMLReader {

    private static final Logger LOGGER = LogManager.getLogger(XMLReader.class);

    @Value("${path.fichierReferentielGares}")
    private String cheminFichier;

    /// Cette méthode permet plus de flexibilité en passant directement le chemin du répertoire des fichiers XML
    /// grâce au @Value (bien pratique)
    public List<StopData> lireStops() {
        return lireStops(cheminFichier);
    }

    /// Permet de lire les données des gares à partir d'un fichier XML
    /// On passe en paramètre le chemin du fichier XML
    /// @Param cheminFichier : chemin du fichier XML à lire
    public List<StopData> lireStops(String cheminFichier) {
        List<StopData> stops = new ArrayList<>();

        try (InputStream inputStream = Files.newInputStream(Paths.get(cheminFichier))) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Fichier XML introuvable : " + cheminFichier);
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            document.getDocumentElement().normalize();

            NodeList stopNodes = document.getElementsByTagName("Stop");

            for (int i = 0; i < stopNodes.getLength(); i++) {
                Element stopElement = (Element) stopNodes.item(i);

                String stopId = stopElement.getAttribute("stopId");
                String arrivalTime = stopElement.getAttribute("arrivalTime");
                String departureTime = stopElement.getAttribute("departureTime");

                Element gpsElement = (Element) stopElement.getElementsByTagName("GPS").item(0);
                double latitude = Double.parseDouble(gpsElement.getAttribute("latitude"));
                double longitude = Double.parseDouble(gpsElement.getAttribute("longitude"));

                int adultsIn = 0;
                int adultsOut = 0;
                int bikesIn = 0;
                int bikesOut = 0;
                int wheelchairsIn = 0;
                int wheelchairsOut = 0;

                NodeList countNodes = stopElement.getElementsByTagName("Count");
                for (int j = 0; j < countNodes.getLength(); j++) {
                    Element countElement = (Element) countNodes.item(j);
                    String type = countElement.getAttribute("type");
                    int in = Integer.parseInt(countElement.getAttribute("in"));
                    int out = Integer.parseInt(countElement.getAttribute("out"));

                    switch (type) {
                        case "adults" -> {
                            adultsIn += in;
                            adultsOut += out;
                        }
                        case "bikes" -> {
                            bikesIn += in;
                            bikesOut += out;
                        }
                        case "wheelchairs" -> {
                            wheelchairsIn += in;
                            wheelchairsOut += out;
                        }
                        default -> LOGGER.error("Type inconnu : {}", type);

                    }
                }

                stops.add(new StopData(stopId, arrivalTime, departureTime, latitude, longitude,
                        adultsIn, adultsOut, bikesIn, bikesOut, wheelchairsIn, wheelchairsOut));
            }

            return stops;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier XML.", e);
        }
    }

    /// Parse les noms de fichier et cherche le schéma _TRIGRAMME_ (c'est le troisième élément)
    private static final Pattern REGION_PATTERN = Pattern.compile(".*_([A-Z]{3})_.*");

    public Regions extraireRegionDepuisNomFichier(String nomFichier) {
        Matcher matcher = REGION_PATTERN.matcher(nomFichier);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Impossible d'extraire le code région depuis le nom de fichier : " + nomFichier
            );
        }

        String codeRegion = matcher.group(1);

        try {
            return Regions.valueOf(codeRegion);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Code région inconnu dans le nom de fichier : " + codeRegion,
                    e
            );
        }
    }

    public record StopData(
            String stopId,
            String arrivalTime,
            String departureTime,
            double latitude,
            double longitude,
            int adultsIn,
            int adultsOut,
            int bikesIn,
            int bikesOut,
            int wheelchairsIn,
            int wheelchairsOut) {}
}