package fr.univlyon3.sncf.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "frequentation_gare")
public class FrequentationGare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFrequentation;

    @Column(nullable = false)
    private LocalDate dateMesure;

    @Column(nullable = false)
    private int monteesAdults;

    @Column(nullable = false)
    private int descentesAdults;

    @Column(nullable = false)
    private int monteesBikes;

    @Column(nullable = false)
    private int descentesBikes;

    @Column(nullable = false)
    private int monteesWheelchairs;

    @Column(nullable = false)
    private int descentesWheelchairs;

    @ManyToOne
    @JoinColumn(name = "gare_id", nullable = false)
    private Gare gare;

    @ManyToOne
    @JoinColumn(name = "fichier_cave_id", nullable = false)
    private FichierCave fichierCave;

    public FrequentationGare() {
    }

    public Integer getIdFrequentation() {
        return idFrequentation;
    }

    public void setIdFrequentation(Integer idFrequentation) {
        this.idFrequentation = idFrequentation;
    }

    public LocalDate getDateMesure() {
        return dateMesure;
    }

    public void setDateMesure(LocalDate dateMesure) {
        this.dateMesure = dateMesure;
    }

    public int getMonteesAdults() {
        return monteesAdults;
    }

    public void setMonteesAdults(int monteesAdults) {
        this.monteesAdults = monteesAdults;
    }

    public int getDescentesAdults() {
        return descentesAdults;
    }

    public void setDescentesAdults(int descentesAdults) {
        this.descentesAdults = descentesAdults;
    }

    public int getMonteesBikes() {
        return monteesBikes;
    }

    public void setMonteesBikes(int monteesBikes) {
        this.monteesBikes = monteesBikes;
    }

    public int getDescentesBikes() {
        return descentesBikes;
    }

    public void setDescentesBikes(int descentesBikes) {
        this.descentesBikes = descentesBikes;
    }

    public int getMonteesWheelchairs() {
        return monteesWheelchairs;
    }

    public void setMonteesWheelchairs(int monteesWheelchairs) {
        this.monteesWheelchairs = monteesWheelchairs;
    }

    public int getDescentesWheelchairs() {
        return descentesWheelchairs;
    }

    public void setDescentesWheelchairs(int descentesWheelchairs) {
        this.descentesWheelchairs = descentesWheelchairs;
    }

    public Gare getGare() {
        return gare;
    }

    public void setGare(Gare gare) {
        this.gare = gare;
    }

    public FichierCave getFichierCave() {
        return fichierCave;
    }

    public void setFichierCave(FichierCave fichierCave) {
        this.fichierCave = fichierCave;
    }
}