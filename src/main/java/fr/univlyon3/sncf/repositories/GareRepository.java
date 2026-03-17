package fr.univlyon3.sncf.repositories;

import fr.univlyon3.sncf.models.Gare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GareRepository extends JpaRepository<Gare, Integer> {
    Optional<Gare> findByShortLabel(String shortLabel);
}