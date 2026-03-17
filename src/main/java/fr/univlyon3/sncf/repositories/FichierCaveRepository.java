package fr.univlyon3.sncf.repositories;

import fr.univlyon3.sncf.models.FichierCave;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("fichierCaveRepository")
public interface FichierCaveRepository extends CrudRepository<FichierCave, Integer> {
}
