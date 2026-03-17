package fr.univlyon3.sncf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.univlyon3.sncf.models.FrequentationGare;

@Repository("frequentationGareRepository")
public interface FrequentationGareRepository extends JpaRepository<FrequentationGare, Integer> {

    boolean existsByFichierCaveIdFichierAndGareIdAndDateMesureAndMonteesAdultsAndDescentesAdults(
            Integer idFichier,
            Integer gareId,
            java.time.LocalDate dateMesure,
            int monteesAdults,
            int descentesAdults
    );
}