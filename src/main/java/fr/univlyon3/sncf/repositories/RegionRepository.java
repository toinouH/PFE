package fr.univlyon3.sncf.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.univlyon3.sncf.models.Region;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    Optional<Region> findByTrigramme(String trigramme);
}