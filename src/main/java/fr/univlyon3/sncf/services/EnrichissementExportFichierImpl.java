package fr.univlyon3.sncf.services;

import fr.univlyon3.sncf.transverse.XMLWritter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service("enrichissementExportFichier")
public class EnrichissementExportFichierImpl implements EnrichissementExportFichier {

    private final Path dossierEntreesCAV;
    private final Path dossierCAVEnrichisTries;
    private final XMLWritter xmlWritter;

    public EnrichissementExportFichierImpl(
            @Value("${path.dossierEntreesCAV}") Path dossierEntreesCAV,
            @Value("${path.dossierCAVEnrichisTries}") Path dossierCAVEnrichisTries,
            XMLWritter xmlWritter) {
        this.dossierEntreesCAV = dossierEntreesCAV;
        this.dossierCAVEnrichisTries = dossierCAVEnrichisTries;
        this.xmlWritter = xmlWritter;
    }

    public void enrichirEtArchiverFichiers() throws IOException {
        if (Files.notExists(dossierEntreesCAV) || !Files.isDirectory(dossierEntreesCAV)) {
            throw new IOException("Le dossier d'entrée est introuvable : " + dossierEntreesCAV.toAbsolutePath());
        }

        List<Path> fichiersXml;
        try (var stream = Files.list(dossierEntreesCAV)) {
            fichiersXml = stream
                    .filter(Files::isRegularFile)
                    .filter(fichier -> fichier.getFileName().toString().endsWith(".xml"))
                    .toList();
        }

        for (Path fichier : fichiersXml) {
            xmlWritter.enrichirXML(
                    fichier.toAbsolutePath().toString(),
                    dossierCAVEnrichisTries.toAbsolutePath().toString()
            );
        }
    }
}