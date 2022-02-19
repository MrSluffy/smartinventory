package com.smart.inventory.application.data.services.ingredient;

import com.smart.inventory.application.data.entity.ingredients.Ingredients;
import com.smart.inventory.application.data.entity.ingredients.QuantityUnit;
import com.smart.inventory.application.data.repository.IIngredientsRepository;
import com.smart.inventory.application.data.repository.IQuantityUnitRepository;
import com.smart.inventory.application.exeptions.NotFoundExeption;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class IngredientsService implements IIngredientsService {

    private final IIngredientsRepository ingredientsRepository;

    private final IQuantityUnitRepository quantityUnitRepository;

    @Autowired
    public IngredientsService(IIngredientsRepository ingredientsRepository,
                              IQuantityUnitRepository quantityUnitRepository) {
        this.ingredientsRepository = ingredientsRepository;
        this.quantityUnitRepository = quantityUnitRepository;
    }

    @Override
    public void deleteIngredientSelected(List<Ingredients> ingredients) {
        ingredientsRepository.deleteAll(ingredients);
    }

    @Override
    public void addIngredient(@Nonnull Ingredients ingredients, QuantityUnit unit, Utilities utilities) {
        utils(unit, ingredients, utilities);
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

    public List<QuantityUnit> findAllUnit(){
        return quantityUnitRepository.findAll();
    }

    public Ingredients getIngredientById(Integer id){
        return ingredientsRepository.findById(id).orElseThrow(NotFoundExeption::new);
    }

    @Transactional
    @Override
    public void updateIngredient(Integer id,
                                 String productName,
                                 int productQuantity,
                                 Double productPrice,
                                 QuantityUnit unit,
                                 Utilities utilities) {
        Ingredients ingredients = getIngredientById(id);
        ingredients.setProductName(productName);
        ingredients.setProductQuantity(productQuantity);
        ingredients.setProductPrice(productPrice);
        utils(unit, ingredients, utilities);
        ingredients.setTotalCost(productQuantity);

        ingredientsRepository.save(ingredients);
    }

    public List<Ingredients> findAllIngredients() {
        return ingredientsRepository.findAll();
    }

    private void utils(QuantityUnit unit,
                       Ingredients ingredients,
                       Utilities utilities) {
        if(utilities.employer != null){
            ingredients.getAddedByEmployer().add(utilities.employer);
        }
        if(utilities.owner != null){
            ingredients.getAddedByOwner().add(utilities.owner);
        }
        ingredients.getCompany().add(utilities.company);
        ingredients.setIngredientCompany(utilities.company);
        ingredients.setQuantityUnit(unit);
    }
}
