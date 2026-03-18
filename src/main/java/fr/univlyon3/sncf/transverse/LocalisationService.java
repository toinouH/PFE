package fr.univlyon3.sncf.transverse;

import fr.univlyon3.sncf.models.Gare;

import java.util.List;

public interface LocalisationService {
    double calculerDistanceKm(double latitude1, double longitude1, double latitude2, double longitude2);
    List<Gare> getGaresDansUnRayonDe500Km(double latitude, double longitude);
}
