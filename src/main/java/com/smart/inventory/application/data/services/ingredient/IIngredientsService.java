package com.smart.inventory.application.data.services.ingredient;

import com.smart.inventory.application.data.entity.ingredients.Ingredients;

import javax.annotation.Nonnull;
import java.util.List;

public interface IIngredientsService {

    void deleteIngredientSelected(List<Ingredients> ingredients);

    void saveIngredient(@Nonnull Ingredients ingredients);

    List<Ingredients> findAllIngredients(String filterText);
}
