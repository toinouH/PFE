package fr.univlyon3.sncf;

import fr.univlyon3.sncf.transverse.NearestStations;
import fr.univlyon3.sncf.transverse.Regions;
import fr.univlyon3.sncf.transverse.XMLReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NearestStationsTest {
    
    private final XMLReader xmlReader = new XMLReader();
    
    @Test
    void reconnaissanceDesRegionsDansLesFichiers() {
        String nomFichier = "FichierCAVE_AQU_X12345_29062022.xml";
        Regions regionObtenue = xmlReader.extraireRegionDepuisNomFichier(nomFichier);
        assertEquals(Regions.AQU, regionObtenue);
        assertEquals("AQUITAINE", regionObtenue.getLibelle());

        String nomFichier2 = "FichierCAVE_ARA_X12345_29062022.xml";
        Regions regionObtenue2 = xmlReader.extraireRegionDepuisNomFichier(nomFichier2);
        assertEquals(Regions.ARA, regionObtenue2);
        assertEquals("AUVERGNE RHONE-ALPES", regionObtenue2.getLibelle());

        String nomFichier3 = "FichierCAVE_BRE_X12345_29062022.xml";
        Regions regionObtenue3 = xmlReader.extraireRegionDepuisNomFichier(nomFichier3);
        assertEquals(Regions.BRE, regionObtenue3);
        assertEquals("BRETAGNE", regionObtenue3.getLibelle());

        String nomFichier4 = "FichierCAVE_CVL_X12345_29062022.xml";
        Regions regionObtenue4 = xmlReader.extraireRegionDepuisNomFichier(nomFichier4);
        assertEquals(Regions.CVL, regionObtenue4);
        assertEquals("CENTRE VAL-DE-LOIRE", regionObtenue4.getLibelle());

        String nomFichier5 = "FichierCAVE_COR_X12345_29062022.xml";
        Regions regionObtenue5 = xmlReader.extraireRegionDepuisNomFichier(nomFichier5);
        assertEquals(Regions.COR, regionObtenue5);
        assertEquals("CORSE", regionObtenue5.getLibelle());

        String nomFichier6 = "FichierCAVE_GRE_X12345_29062022.xml";
        Regions regionObtenue6 = xmlReader.extraireRegionDepuisNomFichier(nomFichier6);
        assertEquals(Regions.GRE, regionObtenue6);
        assertEquals("GRAND-EST", regionObtenue6.getLibelle());

        String nomFichier7 = "FichierCAVE_HDF_X12345_29062022.xml";
        Regions regionObtenue7 = xmlReader.extraireRegionDepuisNomFichier(nomFichier7);
        assertEquals(Regions.HDF, regionObtenue7);
        assertEquals("HAUTS-DE-FRANCE", regionObtenue7.getLibelle());

        String nomFichier8 = "FichierCAVE_IDF_X12345_29062022.xml";
        Regions regionObtenue8 = xmlReader.extraireRegionDepuisNomFichier(nomFichier8);
        assertEquals(Regions.IDF, regionObtenue8);
        assertEquals("ILE-DE-FRANCE", regionObtenue8.getLibelle());

        String nomFichier9 = "FichierCAVE_LRP_X12345_29062022.xml";
        Regions regionObtenue9 = xmlReader.extraireRegionDepuisNomFichier(nomFichier9);
        assertEquals(Regions.LRP, regionObtenue9);
        assertEquals("LANGUEDOC-ROUSSILLON MIDI-PYRENNEES", regionObtenue9.getLibelle());

        String nomFichier10 = "FichierCAVE_NOR_X12345_29062022.xml";
        Regions regionObtenue10 = xmlReader.extraireRegionDepuisNomFichier(nomFichier10);
        assertEquals(Regions.NOR, regionObtenue10);
        assertEquals("NORMANDIE", regionObtenue10.getLibelle());

        String nomFichier11 = "FichierCAVE_PAK_X12345_29062022.xml";
        Regions regionObtenue11 = xmlReader.extraireRegionDepuisNomFichier(nomFichier11);
        assertEquals(Regions.PAK, regionObtenue11);
        assertEquals("PACA", regionObtenue11.getLibelle());

        String nomFichier12 = "FichierCAVE_PDL_X12345_29062022.xml";
        Regions regionObtenue12 = xmlReader.extraireRegionDepuisNomFichier(nomFichier12);
        assertEquals(Regions.PDL, regionObtenue12);
        assertEquals("PAYS-DE-LA-LOIRE", regionObtenue12.getLibelle());
    }
    
    @Test
    void genererBlocNearestStationsXML() {
        String nomFichier = "input/FichierCAVE_AQU_X12345_29062022.xml";
        XMLReader xmlReader = new XMLReader();
        
        // Lire les arrêts du fichier XML
        var stops = xmlReader.lireStops(nomFichier);
        System.out.println(stops);
        
        // Vérifier qu'il y a des arrêts
        assertFalse(stops.isEmpty(), "Le fichier XML doit contenir des arrêts");
        
        // Créer une instance de NearestStations
        NearestStations nearestStations = new NearestStations();
        
        // Générer le bloc XML pour chaque arrêt
        for (var stop : stops) {
            String xmlGenere = nearestStations.generateNearestStationsXML(stop.latitude(), stop.longitude());
            System.out.println(xmlGenere);
            
            // Vérifier que le XML généré contient les éléments attendus
            if (!xmlGenere.isEmpty()) {
                assertTrue(xmlGenere.contains("<NearestStations>"), 
                    "Le XML généré doit contenir l'élément d'ouverture <NearestStations>");
                assertTrue(xmlGenere.contains("</NearestStations>"), 
                    "Le XML généré doit contenir l'élément de fermeture </NearestStations>");
                assertTrue(xmlGenere.contains("<nearestStation"), 
                    "Le XML généré doit contenir des éléments <nearestStation>");
                assertTrue(xmlGenere.contains("latitude="), 
                    "Les stations doivent contenir l'attribut latitude");
                assertTrue(xmlGenere.contains("longitude="), 
                    "Les stations doivent contenir l'attribut longitude");
                assertTrue(xmlGenere.contains("name1="), 
                    "Les stations doivent contenir l'attribut name1");
                assertTrue(xmlGenere.contains("order="), 
                    "Les stations doivent contenir l'attribut order");
            }
        }
    }
}
