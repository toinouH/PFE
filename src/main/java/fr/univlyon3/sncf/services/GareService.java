package fr.univlyon3.sncf.services;

import fr.univlyon3.sncf.models.Gare;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class GareService {

    private static final String SOURCE_FICHIER_JSON = "input/Référentiel_stations_transverses.json";

    public List<Gare> getAllGares() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SOURCE_FICHIER_JSON)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("JSON file not found: " + SOURCE_FICHIER_JSON);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<List<Gare>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error while reading stations JSON file", e);
        }
    }
}
