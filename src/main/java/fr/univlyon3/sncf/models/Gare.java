package fr.univlyon3.sncf.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(
    name = "gare",
    indexes = {
        @Index(name = "idx_gare_short_label", columnList = "shortLabel")
    }
)
public class Gare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false, unique = true)
    private String shortLabel;

    @Column(nullable = false)
    private String mediumLabel;

    @Column(nullable = false)
    private String longLabel;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @OneToMany(mappedBy = "gare")
    private List<FrequentationGare> frequentations;

    public Gare() {
        // This is needed by JPA
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public String getMediumLabel() {
        return mediumLabel;
    }

    public void setMediumLabel(String mediumLabel) {
        this.mediumLabel = mediumLabel;
    }

    public String getLongLabel() {
        return longLabel;
    }

    public void setLongLabel(String longLabel) {
        this.longLabel = longLabel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<FrequentationGare> getFrequentations() {
        return frequentations;
    }

    public void setFrequentations(List<FrequentationGare> frequentations) {
        this.frequentations = frequentations;
    }
}