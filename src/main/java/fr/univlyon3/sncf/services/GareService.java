package fr.univlyon3.sncf.services;

import fr.univlyon3.sncf.models.Gare;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import fr.univlyon3.sncf.transverse.Localisation;
import fr.univlyon3.sncf.transverse.XMLReader;


import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("gareService")
public class GareService {

    @Value("${path.fichierReferentielGares}")
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

    //Lecture Gares depuis un fichier XML
    public List<XMLReader.StopData> lireStopsDepuisFichier(String cheminFichier) {
        XMLReader reader = new XMLReader();
        return reader.lireStops(cheminFichier);
    }

    //NomFichier 
    public String extraireTrigramme(String nomFichier) {
        String[] parties = nomFichier.replace(".xml", "").split("_");
        return parties[1];
    }

    public String extraireVehicule(String nomFichier) {
        String[] parties = nomFichier.replace(".xml", "").split("_");
        return parties[2];
    }

    public LocalDate extraireDateCourse(String nomFichier) {
        String[] parties = nomFichier.replace(".xml", "").split("_");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return LocalDate.parse(parties[3], formatter);
    }

    public Gare trouverPremiereGare(double latitude, double longitude) {
        Localisation localisation = new Localisation();
        List<Gare> gares = localisation.getGaresDansUnRayonDe500Km(latitude, longitude);

        if (gares == null || gares.isEmpty()) {
            return null;
        }

        return gares.get(0);
    }
}
