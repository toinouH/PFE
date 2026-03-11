package fr.univlyon3.sncf;

import fr.univlyon3.sncf.models.Gare;
import fr.univlyon3.sncf.transverse.Localisation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalisationTest {

    private final Localisation localisation = new Localisation();

    @Test
    void returnsExpectedNearbyStationsAroundLyonPartDieu() {
        List<Gare> result = localisation.getGaresDansUnRayonDe500Km(45.76059597, 4.85940903);

        assertEquals(6, result.size());
        assertTrue(result.stream().anyMatch(gare -> gare.label().equals("Lyon Part Dieu")));
        assertTrue(result.stream().anyMatch(gare -> gare.label().equals("Paris Est")));
        assertTrue(result.stream().anyMatch(gare -> gare.label().equals("Marseille Saint-Charles")));
        assertFalse(result.stream().anyMatch(gare -> gare.label().equals("Lille Europe")));
    }

    @Test
    void returnsStationItselfWhenCoordinatesMatchExactly() {
        List<Gare> result = localisation.getGaresDansUnRayonDe500Km(43.302666, 5.380407);

        assertTrue(result.stream().anyMatch(gare -> gare.label().equals("Marseille Saint-Charles")));
    }

    @Test
    void returnsNoStationWhenCoordinatesAreTooFarFromFrance() {
        List<Gare> result = localisation.getGaresDansUnRayonDe500Km(0.0, 0.0);

        assertTrue(result.isEmpty());
    }

}
