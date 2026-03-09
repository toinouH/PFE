package fr.univlyon3.sncf.models;

public record Gare(String label,
            String shortLabel,
            String mediumLabel,
            String longLabel,
            double latitude,
            double longitude) {}

