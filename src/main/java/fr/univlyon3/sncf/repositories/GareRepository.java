package fr.univlyon3.sncf.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import fr.univlyon3.sncf.models.Gare;

@Repository("gareRepository")
public interface GareRepository extends JpaRepository<Gare, Integer> {
    Optional<Gare> findByShortLabel(String shortLabel);
    boolean existsByShortLabel(String shortLabel);
}