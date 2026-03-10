package fr.univlyon3.sncf.transverse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class XMLWritter {

    public void enrichirXML(String inputXmlPath, String outputXmlPath) throws IOException {
        XMLReader reader = new XMLReader();
        List<XMLReader.StopData> stops = reader.lireStops(inputXmlPath);
        NearestStations nearestStationsGenerator = new NearestStations();

        String content;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputXmlPath)) {
            if (inputStream == null) {
                throw new IOException("Fichier XML introuvable : " + inputXmlPath);
            }
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            content = result.toString(java.nio.charset.StandardCharsets.UTF_8.name());
        }
        
        StringBuilder enrichedContent = new StringBuilder();
        int lastPos = 0;
        
        for (int i = 0; i < stops.size() - 1; i++) {
            XMLReader.StopData currentStop = stops.get(i);
            String stopTag = "</Stop>";
            int stopEndIndex = content.indexOf(stopTag, lastPos) + stopTag.length();
            
            enrichedContent.append(content, lastPos, stopEndIndex);
            
            String xmlToInsert = nearestStationsGenerator.generateNearestStationsXML(currentStop.latitude(), currentStop.longitude());
            if (!xmlToInsert.isEmpty()) {
                enrichedContent.append("\n").append(xmlToInsert);
            }
            
            lastPos = stopEndIndex;
        }
        
        enrichedContent.append(content.substring(lastPos));
        
        Path outputPath = Paths.get(outputXmlPath);
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, enrichedContent.toString());
    }
}
