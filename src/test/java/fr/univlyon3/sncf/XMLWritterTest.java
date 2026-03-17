package fr.univlyon3.sncf;

import fr.univlyon3.sncf.repositories.FichierCaveRepository;
import fr.univlyon3.sncf.repositories.RegionRepository;
import fr.univlyon3.sncf.transverse.Regions;
import fr.univlyon3.sncf.transverse.XMLReader;
import fr.univlyon3.sncf.transverse.XMLWritter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class XMLWritterTest {

    private XMLReader reader;
    private FichierCaveRepository fichierCaveRepository;
    private RegionRepository regionRepository;
    private XMLWritter writter;

    @BeforeEach
    void setUp() {
        reader = mock(XMLReader.class);
        fichierCaveRepository = mock(FichierCaveRepository.class);
        regionRepository = mock(RegionRepository.class);
        writter = mock(XMLWritter.class);
        /*writter = new XMLWritter(reader, fichierCaveRepository, regionRepository);*/
    }

    @Test
    void doitEcrireLeFichierEnrichiDansLeDossierDeSortieAssortiDuSousDossierRegion() throws IOException {
        String inputPath = "src/test/resources/input/FichierCAVE_AQU_X12345_29062022.xml";
        String outputPath = "target/test-output/";
        String expectedResult = "target/AQU/FichierCAVE_AQU_X12345_29062022_enriched.xml";

        // Mock reader behavior
        XMLReader readerForData = new XMLReader();
        List<XMLReader.StopData> actualStops = readerForData.lireStops(inputPath);
        when(reader.lireStops(anyString())).thenReturn(actualStops);
        when(reader.extraireRegionDepuisNomFichier(anyString())).thenReturn(Regions.AQU);

        // Mock repository behavior
        when(regionRepository.findByTrigramme("AQU")).thenReturn(Optional.empty());

        writter.enrichirXML(inputPath, outputPath);

        Path actualOutputFile = Paths.get(outputPath, "AQU", "FichierCAVE_AQU_X12345_29062022_enriched.xml");

        assertTrue(Files.exists(actualOutputFile), "Le fichier de sortie doit être créé dans le sous-dossier de région");
        assertTrue(Files.isRegularFile(actualOutputFile), "Le chemin de sortie doit pointer vers un fichier");

        String actualContent = Files.readString(actualOutputFile).replace("\r\n", "\n").replaceAll("(?m)[ \t]+$", "");

        String expectedContent;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(expectedResult)) {
            if (is == null) {
                throw new IOException("Fichier de référence introuvable : " + expectedResult);
            }
            expectedContent = new String(is.readAllBytes(), StandardCharsets.UTF_8).replace("\r\n", "\n").replaceAll("(?m)[ \t]+$", "");
        }

        assertEquals(expectedContent, actualContent, "Le fichier généré ne correspond pas au fichier de référence");
    }
}