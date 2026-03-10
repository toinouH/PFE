package fr.univlyon3.sncf.transverse;

import fr.univlyon3.sncf.models.Gare;

import java.util.ArrayList;
import java.util.List;

/// Exemple de sortie aléatoire
/// ```xml
/// ...
/// </Stop>
/// <NearestStations>
///     <nearestStation latitude="45.778152" longitude="3.100512" name1="Station A" order="1" />
///     <nearestStation latitude="45.808152" longitude="3.100512" name1="Station A" order="2" />
///     <nearestStation latitude="45.778152" longitude="-0.100512" name1="Station A" order="3" />
/// </NearestStations>
/// <Stop>
/// ...
/// ```
public class NearestStations {

    public String generateNearestStationsXML(double latitude, double longitude) {
        List<NearestStation> nearestStations = toGare(latitude, longitude);

        if (nearestStations.isEmpty()) {
            return "";  // On n'enrichie pas le fichier si il n'y a pas de gare
        }

        StringBuilder xml = new StringBuilder();
        xml.append("    <NearestStations>\n");
        for (NearestStation station : nearestStations) {
            xml.append(String.format("        <nearestStation latitude=\"%.6f\" longitude=\"%.6f\" name1=\"%s\" order=\"%d\" />\n",
                    station.latitude(),
                    station.longitude(),
                    station.name1(),
                    station.order()));
        }
        xml.append("    </NearestStations>");

        return xml.toString();
    }

    private List<NearestStation> toGare(double latitude, double longitude) {
        Localisation localisation = new Localisation();
        List<Gare> gares = localisation.getGaresDansUnRayonDe500Km(latitude, longitude);
        List<NearestStation> nearestStations = new ArrayList<>();
        for (int i = 1; i < gares.size(); i++) {
            nearestStations.add(new NearestStation(gares.get(i).latitude(), gares.get(i).longitude(), gares.get(i).shortLabel(), i));
        }
        return nearestStations;
    }

    private record NearestStation(double latitude, double longitude, String name1, int order) {}

}
