package fr.univlyon3.sncf.transverse;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.charset.StandardCharsets;

@Component("xmlWritter")
public class XMLWritter {

    @Resource(name = "xmlReader")
    private XMLReader reader;

    public void enrichirXML(String inputXmlPath, String outputXmlPath) throws IOException {
        List<XMLReader.StopData> stops = reader.lireStops(inputXmlPath);
        NearestStations nearestStationsGenerator = new NearestStations();

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
        for (int i = 0; i < stops.size() - 1; i++) {
            XMLReader.StopData currentStop = stops.get(i);
            String stopTag = "</Stop>";
            int stopEndIndex = content.indexOf(stopTag, lastPos) + stopTag.length();

            enrichedContent.append(content, lastPos, stopEndIndex);

            String xmlToInsert = nearestStationsGenerator.generateNearestStationsXML(
                    currentStop.latitude(), currentStop.longitude()
            );
            if (!xmlToInsert.isEmpty()) {
                enrichedContent.append("\n").append(xmlToInsert);
            }

            lastPos = stopEndIndex;
        }

        enrichedContent.append(content.substring(lastPos));

        Path outputPath = construireCheminSortie(inputXmlPath, outputXmlPath);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, enrichedContent.toString());
    }

    private Path construireCheminSortie(String inputXmlPath, String outputXmlPath) {
        Path dossierSortie = Paths.get(outputXmlPath);
        String nomFichierEntree = Paths.get(inputXmlPath).getFileName().toString();
        Regions region = reader.extraireRegionDepuisNomFichier(nomFichierEntree);
        String nomFichierSortie = nomFichierEntree.replace(".xml", "_enriched.xml");

        return dossierSortie.resolve(region.name()).resolve(nomFichierSortie);
    }
}