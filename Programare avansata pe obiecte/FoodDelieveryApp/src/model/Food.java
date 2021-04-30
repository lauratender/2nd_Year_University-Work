package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Food extends Product{
    private List<String> ingredients;
    int weightInGrams;
    public Food(String name, String desc, Restaurant r, int pr, int w, List <String> ingredients){
        super(name, desc, r, pr);
        weightInGrams = w;
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public void print(){
        super.print();
        System.out.print(weightInGrams + "g\nIngredients:");
        for (String ing:ingredients)
            System.out.print(ing + " ");
        System.out.print("\n");
    }
    /*
    // CRUD TO BE DONE IN SERVICE
    public void addIngredient(String ingredient){
        ingredients.add(ingredient);
    }*/

}

