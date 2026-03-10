package fr.univlyon3.sncf.transverse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLReader {

    public List<StopData> lireStops(String cheminFichier) {
        List<StopData> stops = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(cheminFichier)) {
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

                int totalIn = 0;
                int totalOut = 0;

                NodeList countNodes = stopElement.getElementsByTagName("Count");
                for (int j = 0; j < countNodes.getLength(); j++) {
                    Element countElement = (Element) countNodes.item(j);
                    totalIn += Integer.parseInt(countElement.getAttribute("in"));
                    totalOut += Integer.parseInt(countElement.getAttribute("out"));
                }

                stops.add(new StopData(stopId, arrivalTime, departureTime, latitude, longitude, totalIn, totalOut));
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
            int totalIn,
            int totalOut) {}
}
