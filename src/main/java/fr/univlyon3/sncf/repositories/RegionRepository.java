package fr.univlyon3.sncf.repositories;

import fr.univlyon3.sncf.models.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("regionRepository")
public interface RegionRepository extends CrudRepository<Region, Integer> {
    Optional<Region> findByTrigramme(String trigramme);
}
