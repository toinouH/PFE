package fr.univlyon3.sncf.services;

import fr.univlyon3.sncf.transverse.XMLReader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureCaveService {

    public List<XMLReader.StopData> lireStopsDepuisFichier(String cheminFichier) {
        XMLReader reader = new XMLReader();
        return reader.lireStops(cheminFichier);
    }
}