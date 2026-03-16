/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.univlyon3.sncf.models;

/**
 *
 * @author Faquira
 */
import jakarta.persistence.*;

@Entity
public class Gestionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String motPasse;

    public Gestionnaire() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public Gestionnaire(String login, String nom, String prenom, String motPasse) {
        this.login = login;
        this.nom = nom;
        this.prenom = prenom;
        this.motPasse = motPasse;
    }
}