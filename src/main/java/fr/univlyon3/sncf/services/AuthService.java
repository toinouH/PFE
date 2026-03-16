package fr.univlyon3.sncf.services;

import fr.univlyon3.sncf.models.*;
import fr.univlyon3.sncf.repositories.GestionnaireRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final GestionnaireRepository gestionnaireRepository;

    public AuthService(GestionnaireRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public Gestionnaire authenticate(String login, String motPasse) {
        Optional<Gestionnaire> optionalGestionnaire = gestionnaireRepository.findByLogin(login);

        if (optionalGestionnaire.isPresent()) {
            Gestionnaire gestionnaire = optionalGestionnaire.get();

            if (gestionnaire.getMotPasse().equals(motPasse)) {
                return gestionnaire;
            }
        }

        return null;
    }

    public void register(Gestionnaire gestionnaire) {
         Optional<Gestionnaire> existingGestionnaire = gestionnaireRepository.findByLogin(gestionnaire.getLogin());
        if (existingGestionnaire.isPresent()) {
            throw new IllegalArgumentException("Login already exists");
        }
        gestionnaireRepository.save(gestionnaire);
    }

    
}