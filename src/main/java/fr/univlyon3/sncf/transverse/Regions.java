package fr.univlyon3.sncf.transverse;

public enum Regions {

    AQU("AQUITAINE", "gg"),
    ARA("AUVERGNE RHONE-ALPES", "gg"),
    BFC("BOURGOGNE FRANCHE-COMTE", "gg"),
    BRE("BRETAGNE", "gg"),
    CVL("CENTRE VAL-DE-LOIRE", "gg"),
    COR("CORSE", "gg"),
    GRE("GRAND-EST", "gg"),
    HDF("HAUTS-DE-FRANCE", "gg"),
    IDF("ILE-DE-FRANCE", "gg"),
    LRP("LANGUEDOC-ROUSSILLON MIDI-PYRENNEES", "gg"),
    NOR("NORMANDIE", "gg"),
    PAK("PACA", "gg"),
    PDL("PAYS-DE-LA-LOIRE", "gg"),;

    private final String libelle;
    private final String email;

    Regions(String libelle, String email) {
        this.libelle = libelle;
        this.email = email;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getEmail() {
        return email;
    }
}
