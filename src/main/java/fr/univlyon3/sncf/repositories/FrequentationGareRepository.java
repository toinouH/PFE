package fr.univlyon3.sncf.repositories;

import fr.univlyon3.sncf.models.FrequentationGare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FrequentationGareRepository extends JpaRepository<FrequentationGare, Integer> {

    boolean existsByDateMesureAndGareIdAndFichierCaveIdFichier(
            LocalDate dateMesure,
            Integer gareId,
            Integer idFichier
    );
}