package fr.univlyon3.sncf.repositories;

import fr.univlyon3.sncf.models.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository("gestionnaireRepository")
public interface GestionnaireRepository extends JpaRepository<Gestionnaire, Integer> {
    Optional<Gestionnaire> findByLogin(String login);
}