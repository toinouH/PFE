package fr.univlyon3.sncf.transverse;

public enum Regions {

    AQU("AQUITAINE"),
    ARA("AUVERGNE RHONE-ALPES"),
    BFC("BOURGOGNE FRANCHE-COMTE"),
    BRE("BRETAGNE"),
    CVL("CENTRE VAL-DE-LOIRE"),
    COR("CORSE"),
    GRE("GRAND-EST"),
    HDF("HAUTS-DE-FRANCE"),
    IDF("ILE-DE-FRANCE"),
    LRP("LANGUEDOC-ROUSSILLON MIDI-PYRENNEES"),
    NOR("NORMANDIE"),
    PAK("PACA"),
    PDL("PAYS-DE-LA-LOIRE"),;

    private final String libelle;

    Regions(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
