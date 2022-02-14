package com.smart.inventory.application.data.services.ingredient;

import com.smart.inventory.application.data.entity.ingredients.Ingredients;
import com.smart.inventory.application.data.repository.IIngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class IngredientsService implements IIngredientsService {

    private final IIngredientsRepository ingredientsRepository;

    @Autowired
    public IngredientsService(IIngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    @Override
    public void deleteIngredientSelected(List<Ingredients> ingredients) {
        ingredientsRepository.deleteAll(ingredients);
    }

    @Override
    public void saveIngredient(@Nonnull Ingredients ingredients) {
        ingredientsRepository.save(ingredients);
    }

    @Override
    public List<Ingredients> findAllIngredients(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return ingredientsRepository.findAll();
        } else {
            return ingredientsRepository.search(filterText);
        }
    }
}
