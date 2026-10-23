package com.example.smartpantry.model;

import java.util.*;

public final class Models {
    private Models() {}
    public static class PantryItem { public long id; public String name, unit, expiry; public double quantity; public PantryItem() {} public PantryItem(long id,String name,double quantity,String unit,String expiry){this.id=id;this.name=name;this.quantity=quantity;this.unit=unit;this.expiry=expiry;} }
    public static class RecipeIngredient { public String name, unit; public double quantity; public RecipeIngredient(String name,double quantity,String unit){this.name=name;this.quantity=quantity;this.unit=unit;} }
    public static class Recipe { public int id; public String name, method; public List<RecipeIngredient> ingredients = new ArrayList<>(); public Recipe(int id,String name,String method){this.id=id;this.name=name;this.method=method;} }
}
