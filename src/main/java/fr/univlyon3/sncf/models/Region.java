package fr.univlyon3.sncf.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String trigramme;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "region")
    private List<FichierCave> fichiers;

    public Region() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrigramme() {
        return trigramme;
    }

    public void setTrigramme(String trigramme) {
        this.trigramme = trigramme;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FichierCave> getFichiers() {
        return fichiers;
    }

    public void setFichiers(List<FichierCave> fichiers) {
        this.fichiers = fichiers;
    }
}