package fr.univlyon3.sncf.repositories;

import fr.univlyon3.sncf.models.FichierCave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FichierCaveRepository extends JpaRepository<FichierCave, Integer> {
    boolean existsByNomFichier(String nomFichier);
}