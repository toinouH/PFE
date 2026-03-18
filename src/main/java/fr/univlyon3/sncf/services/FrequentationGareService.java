package fr.univlyon3.sncf.services;

import java.util.List;

import fr.univlyon3.sncf.transverse.LocalisationService;
import org.springframework.stereotype.Service;

import fr.univlyon3.sncf.models.FichierCave;
import fr.univlyon3.sncf.models.FrequentationGare;
import fr.univlyon3.sncf.models.Gare;
import fr.univlyon3.sncf.repositories.FrequentationGareRepository;
import fr.univlyon3.sncf.repositories.GareRepository;
import fr.univlyon3.sncf.transverse.Localisation;
import fr.univlyon3.sncf.transverse.XMLReader;

@Service
public class FrequentationGareService {

    private final FrequentationGareRepository frequentationGareRepository;
    private final GareRepository gareRepository;
    private final LocalisationService localisation;

    public FrequentationGareService(
            FrequentationGareRepository frequentationGareRepository,
            GareRepository gareRepository,
            LocalisationService localisation

    ) {
        this.frequentationGareRepository = frequentationGareRepository;
        this.gareRepository = gareRepository;
        this.localisation = localisation;
    }

    public void alimenterDepuisStops(FichierCave fichierCave, List<XMLReader.StopData> stops) {


        for (XMLReader.StopData stop : stops) {
            List<Gare> garesProches = localisation.getGaresDansUnRayonDe500Km(
                    stop.latitude(),
                    stop.longitude()
            );

            if (garesProches == null || garesProches.isEmpty()) {
                continue;
            }

            Gare gareTrouvee = garesProches.get(0);

            Gare gareEnBase = gareRepository.findByShortLabel(gareTrouvee.getShortLabel())
                    .orElse(null);

            if (gareEnBase == null) {
                System.out.println("Gare absente de la base : " + gareTrouvee.getShortLabel());
                continue;
            }

            boolean existe = frequentationGareRepository
                    .existsByFichierCaveIdFichierAndGareIdAndDateMesureAndMonteesAdultsAndDescentesAdults(
                            fichierCave.getIdFichier(),
                            gareEnBase.getId(),
                            fichierCave.getDateCourse(),
                            stop.totalIn(),
                            stop.totalOut()
                    );

            if (existe) {
                System.out.println("Fréquentation déjà présente pour : " + gareEnBase.getShortLabel());
                continue;
            }

            FrequentationGare frequentation = new FrequentationGare();
            frequentation.setDateMesure(fichierCave.getDateCourse());
            frequentation.setMonteesAdults(stop.totalIn());
            frequentation.setDescentesAdults(stop.totalOut());
            frequentation.setMonteesBikes(0);
            frequentation.setDescentesBikes(0);
            frequentation.setMonteesWheelchairs(0);
            frequentation.setDescentesWheelchairs(0);
            frequentation.setGare(gareEnBase);
            frequentation.setFichierCave(fichierCave);

            frequentationGareRepository.save(frequentation);
        }
    }
}