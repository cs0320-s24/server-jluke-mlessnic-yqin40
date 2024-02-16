package edu.brown.cs.student.main.CSV.Census;

import java.util.List;

public class Census {
    private String City;
    private String MedianHouseholdIncome;
    private String MedianFamilyIncome;
    private String PerCapitalIncome;

    // Getter for City
    public String getCity() {
        return City;
    }

    // Setter for City
    public void setCity(String city) {
        this.City = city;
    }

    // Getter for MedianHouseholdIncome
    public String getMedianHouseholdIncome() {
        return MedianHouseholdIncome;
    }

    // Setter for MedianHouseholdIncome
    public void setMedianHouseholdIncome(String medianHouseholdIncome) {
        this.MedianHouseholdIncome = medianHouseholdIncome;
    }

    // Getter for MedianFamilyIncome
    public String getMedianFamilyIncome() {
        return MedianFamilyIncome;
    }

    // Setter for MedianFamilyIncome
    public void setMedianFamilyIncome(String medianFamilyIncome) {
        this.MedianFamilyIncome = medianFamilyIncome;
    }

    // Getter for PerCapitaIncome
    public String getPerCapitalIncome() {
        return PerCapitalIncome;
    }

    // Setter for PerCapitaIncome
    public void setPerCapitalIncome(String perCapitalIncome) {
        this.PerCapitalIncome = perCapitalIncome;
    }


    public List<String> turnIntoRawStrings() {
        return List.of(this.City, this.MedianHouseholdIncome, this.MedianFamilyIncome, this.PerCapitalIncome);
    }
}

