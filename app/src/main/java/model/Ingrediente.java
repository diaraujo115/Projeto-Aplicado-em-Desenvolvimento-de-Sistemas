package model;

public class Ingrediente {
    private Integer id;
    private String name;
    private String type;
    private String calories;
    private String saturatedFat;
    private String sodium;
    private String protein;
    private String iron;
    private String potassium;

    public Ingrediente(Integer id, String name, String type, String calories, String saturatedFat, String sodium, String protein, String iron, String potassium) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.calories = calories;
        this.saturatedFat = saturatedFat;
        this.sodium = sodium;
        this.protein = protein;
        this.iron = iron;
        this.potassium = potassium;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getSaturatedFat() {
        return saturatedFat;
    }

    public void setSaturatedFat(String saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    public String getSodium() {
        return sodium;
    }

    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getIron() {
        return iron;
    }

    public void setIron(String iron) {
        this.iron = iron;
    }

    public String getPotassium() {
        return potassium;
    }

    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }


}
