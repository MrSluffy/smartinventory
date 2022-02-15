package com.smart.inventory.application.data.services.ingredient;

import com.smart.inventory.application.data.entity.ingredients.Ingredients;
import com.smart.inventory.application.data.entity.ingredients.QuantityUnit;

import javax.annotation.Nonnull;
import java.util.List;

public interface IIngredientsService {

    void deleteIngredientSelected(List<Ingredients> ingredients);

    void addIngredient(@Nonnull Ingredients ingredients, QuantityUnit unit);

    List<Ingredients> findAllIngredients(String filterText);

    void updateIngredient(Integer id, String productName, int productQuantity, Double productPrice, QuantityUnit unit);
}
