package fr.univlyon3.sncf.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "fichier_cave")
public class FichierCave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFichier;

    @Column(nullable = false)
    private String nomFichier;

    @Column(nullable = false)
    private LocalDateTime dateReception;

    @Column(nullable = false)
    private LocalDate dateCourse;

    @Column(nullable = false)
    private String vehicule;

    @Column(nullable = false)
    private String statutEnrichissement;

    @Column(nullable = false)
    private float tauxEnrichissement;

    @Column(nullable = false)
    private String cheminFichierOriginal;

    @Column(nullable = false)
    private String cheminFichierEnrichi;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "fichierCave")
    private List<FrequentationGare> frequentations;

    public FichierCave() {
    }

    public Integer getIdFichier() {
        return idFichier;
    }

    public void setIdFichier(Integer idFichier) {
        this.idFichier = idFichier;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public LocalDateTime getDateReception() {
        return dateReception;
    }

    public void setDateReception(LocalDateTime dateReception) {
        this.dateReception = dateReception;
    }

    public LocalDate getDateCourse() {
        return dateCourse;
    }

    public void setDateCourse(LocalDate dateCourse) {
        this.dateCourse = dateCourse;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public String getStatutEnrichissement() {
        return statutEnrichissement;
    }

    public void setStatutEnrichissement(String statutEnrichissement) {
        this.statutEnrichissement = statutEnrichissement;
    }

    public float getTauxEnrichissement() {
        return tauxEnrichissement;
    }

    public void setTauxEnrichissement(float tauxEnrichissement) {
        this.tauxEnrichissement = tauxEnrichissement;
    }

    public String getCheminFichierOriginal() {
        return cheminFichierOriginal;
    }

    public void setCheminFichierOriginal(String cheminFichierOriginal) {
        this.cheminFichierOriginal = cheminFichierOriginal;
    }

    public String getCheminFichierEnrichi() {
        return cheminFichierEnrichi;
    }

    public void setCheminFichierEnrichi(String cheminFichierEnrichi) {
        this.cheminFichierEnrichi = cheminFichierEnrichi;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public List<FrequentationGare> getFrequentations() {
        return frequentations;
    }

    public void setFrequentations(List<FrequentationGare> frequentations) {
        this.frequentations = frequentations;
    }

    public FichierCave(Integer idFichier, String nomFichier, LocalDateTime dateReception, LocalDate dateCourse,
            String vehicule, String statutEnrichissement, float tauxEnrichissement, String cheminFichierOriginal,
            String cheminFichierEnrichi, Region region, List<FrequentationGare> frequentations) {
        this.idFichier = idFichier;
        this.nomFichier = nomFichier;
        this.dateReception = dateReception;
        this.dateCourse = dateCourse;
        this.vehicule = vehicule;
        this.statutEnrichissement = statutEnrichissement;
        this.tauxEnrichissement = tauxEnrichissement;
        this.cheminFichierOriginal = cheminFichierOriginal;
        this.cheminFichierEnrichi = cheminFichierEnrichi;
        this.region = region;
        this.frequentations = frequentations;
    }
}