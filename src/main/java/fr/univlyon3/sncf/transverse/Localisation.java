package fr.univlyon3.sncf.transverse;

import fr.univlyon3.sncf.models.Gare;
import org.springframework.beans.factory.annotation.Value;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Localisation {

    private static final double RAYON_TERRE_KM = 6371.0D;

    @Value("${distance.maxkm}")
    private double DISTANCE_MAX_KM = 500.0D;    // La valeur par défaut est de 500.00km

    // Dans un monde idéal on peut paramétrer la localisation du fichier plus que ça
    // Si la clef existe dans la configuration elle passe par dessus la valeur par défaut
    @Value("${path.fichierReferentielGares}")
    private String SOURCE_FICHIER_JSON = "input/Référentiel_stations_transverses.json";

    /// Cette méthode retourne la liste des gares situées dans un rayon de 500km autour d'un point donné
    /// Les gares sont ordonnées dans l'ordre croissant de la plus proche à la plus éloignée
    public List<Gare> getGaresDansUnRayonDe500Km(double latitude, double longitude) {
        List<GareDistance> garesDansLeRayon = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SOURCE_FICHIER_JSON)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Fichier source JSON introuvable.");
            }

            // On fait le mapping avec le fichier JSON
            ObjectMapper objectMapper = new ObjectMapper();
            List<Gare> gares = objectMapper.readValue(inputStream, new TypeReference<List<Gare>>() {});

            // On calcule la distance entre le point et chacune des gares
            // Si le nombre de gare est trop grand on pourrait le faire un parallèle avec une l'API de Concurrence Structurée en preview
            for (Gare gare : gares) {
                double distance = calculerDistanceKm(latitude, longitude, gare.latitude(), gare.longitude());

                if (distance <= DISTANCE_MAX_KM) {
                    // On ajoute la gare et sa distance au résultat
                    garesDansLeRayon.add(new GareDistance(gare, distance));
                }
            }

            // On trie les gares par distance croissante
            garesDansLeRayon.sort(Comparator.comparingDouble(GareDistance::distance));

            // On reconvertit la liste des GareDistance en liste de Gares
            // Dans un certain monde on pourrait retourner la liste directement afin de limiter les opérations
            List<Gare> resultat = new ArrayList<>();
            for (GareDistance gareDistance : garesDansLeRayon) {
                resultat.add(gareDistance.gare());
            }

            return resultat;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier JSON.", e);
        }
    }

    /// La formule mathématique est un peu folle j'admets mais c'est la formule de Haversine pour calculer la
    /// distance entre deux points sur une sphère (ici la terre).
    private double calculerDistanceKm(double latitude1, double longitude1, double latitude2, double longitude2) {
        double lat1Rad = Math.toRadians(latitude1);
        double lon1Rad = Math.toRadians(longitude1);
        double lat2Rad = Math.toRadians(latitude2);
        double lon2Rad = Math.toRadians(longitude2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAYON_TERRE_KM * c;
    }

    /// Mapping d'une gare et de la distance qui la sépare avec un point
    private record GareDistance(Gare gare, double distance) {}

}
