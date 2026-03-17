package fr.univlyon3.sncf.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class NomFichierCaveService {

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
}