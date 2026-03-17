package fr.univlyon3.sncf.services;


import fr.univlyon3.sncf.models.Gare;
import fr.univlyon3.sncf.repositories.GareRepository;
import tools.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import tools.jackson.core.type.TypeReference;

@Service("referentielGareService")
public class ReferentielGareService {

    @Value("${path.fichierReferentielGares}")
    private String dossierReferentiel;

    private final GareRepository gareRepository;
    private final ObjectMapper objectMapper;

    public ReferentielGareService(GareRepository gareRepository, ObjectMapper objectMapper) {
        this.gareRepository = gareRepository;
        this.objectMapper = objectMapper;
    }

    public ImportResult sauvegarderEtImporterJson(MultipartFile fichierJson) throws Exception {
        if (fichierJson == null || fichierJson.isEmpty()) {
            throw new IllegalArgumentException("Le fichier JSON est vide.");
        }

        String nomOriginal = fichierJson.getOriginalFilename();
        if (nomOriginal == null || !nomOriginal.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Le fichier doit être au format JSON.");
        }

        Path dossier = Path.of(dossierReferentiel);
        Files.createDirectories(dossier);

        // on écrase toujours le fichier physique précédent
        Path cheminJson = dossier.resolve("referentiel_gares.json");
        Files.copy(fichierJson.getInputStream(), cheminJson, StandardCopyOption.REPLACE_EXISTING);

        return importerDepuisJson(cheminJson);
    }

    public ImportResult importerDepuisJson(Path cheminJson) throws Exception {
        int total = 0;
        int ajoutees = 0;
        int ignorees = 0;

        try (InputStream inputStream = Files.newInputStream(cheminJson)) {
            List<GareJsonDto> garesJson = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<GareJsonDto>>() {}
            );

            total = garesJson.size();

            for (GareJsonDto dto : garesJson) {
                if (dto.shortLabel() == null || dto.shortLabel().isBlank()) {
                    ignorees++;
                    continue;
                }

                if (gareRepository.existsByShortLabel(dto.shortLabel())) {
                    ignorees++;
                    continue;
                }

                Gare gare = new Gare();
                gare.setLabel(dto.label());
                gare.setShortLabel(dto.shortLabel());
                gare.setMediumLabel(dto.mediumLabel());
                gare.setLongLabel(dto.longLabel());
                gare.setLatitude(dto.latitude());
                gare.setLongitude(dto.longitude());

                gareRepository.save(gare);
                ajoutees++;
            }
        }

        return new ImportResult(total, ajoutees, ignorees);
    }

    public record GareJsonDto(
            String label,
            String shortLabel,
            String mediumLabel,
            String longLabel,
            double latitude,
            double longitude
    ) {}

    public record ImportResult(
            int total,
            int ajoutees,
            int ignorees
    ) {}
}