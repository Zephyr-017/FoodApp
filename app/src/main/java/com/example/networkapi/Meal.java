package com.example.networkapi;

public class Meal {
    private String strMeal, strMealThumb, idMeal, strArea;

    public Meal(String idMeal, String strMeal, String strMealThumb, String strArea) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
        this.strArea = strArea;
    }

    public String getStrMeal() { return strMeal; }
    public String getStrMealThumb() { return strMealThumb; }
    public String getIdMeal() { return idMeal; }
    public String getStrArea() { return strArea; }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }
}