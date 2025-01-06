package com.ssl.noaa_species.model;

import java.util.Map;

public class Species {
    private String name;
    private String scientificName;
    private String alsoKnownAs;
    private String imageUrl;
    private Map<String, String> additionalDetails;
    private Map<String, String> scientificClassification;

    public Species(String name, String scientificName, String alsoKnownAs, String imageUrl, 
                Map<String, String> additionalDetails, Map<String, String> scientificClassification) {
        this.name = name;
        this.scientificName = scientificName;
        this.alsoKnownAs = alsoKnownAs;
        this.imageUrl = imageUrl;
        this.additionalDetails = additionalDetails;
        this.scientificClassification = scientificClassification;
    }

    public String getName() {
        return name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Map<String, String> getAdditionalDetails() {
        return additionalDetails;
    }

    public Map<String, String> getScientificClassification() {
        return scientificClassification;
    }
}
