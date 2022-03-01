package com.smart.inventory.application.data.services.ingredient;

import com.smart.inventory.application.data.entities.ingredients.Ingredients;
import com.smart.inventory.application.data.entities.ingredients.QuantityUnit;
import com.smart.inventory.application.data.repository.IActivityRepository;
import com.smart.inventory.application.data.repository.IIngredientsRepository;
import com.smart.inventory.application.data.repository.IQuantityUnitRepository;
import com.smart.inventory.application.exceptions.NotFoundException;
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

    private final IActivityRepository activityRepository;

    String title;

    @Autowired
    public IngredientsService(IIngredientsRepository ingredientsRepository,
                              IQuantityUnitRepository quantityUnitRepository,
                              IActivityRepository activityRepository) {
        this.ingredientsRepository = ingredientsRepository;
        this.quantityUnitRepository = quantityUnitRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public void deleteIngredientSelected(List<Ingredients> ingredients, @Nonnull Utilities utilities) {
        ingredientsRepository.deleteAll(ingredients);

        utilities.configureActivity(
                title + " delete product costing: " + ingredients,
                "",
                activityRepository
        );
    }

    @Override
    public void addIngredient(@Nonnull Ingredients ingredients, QuantityUnit unit, Utilities utilities) {
        utils(unit, ingredients, utilities);
        ingredientsRepository.save(ingredients);

        utilities.configureActivity(
                title + " added product costing: " + ingredients.getProductName(),
                "",
                activityRepository
        );
    }

    @Override
    public List<Ingredients> findAllIngredients(Integer id) {
        return ingredientsRepository.search(id);
    }

    public List<QuantityUnit> findAllUnit(){
        return quantityUnitRepository.findAll();
    }

    public Ingredients getIngredientById(Integer id){
        return ingredientsRepository.findById(id).orElseThrow(NotFoundException::new);
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

        utilities.configureActivity(
                title + " update product costing: " + productName,
                "",
                activityRepository
                );

    }

    public List<Ingredients> findAllIngredients() {
        return ingredientsRepository.findAll();
    }

    private void utils(QuantityUnit unit,
                       Ingredients ingredients,
                       @Nonnull Utilities utilities) {
        if(utilities.employer != null){
            ingredients.getAddedByEmployer().add(utilities.employer);
            title = utilities.employer.getEmail();
        }
        if(utilities.owner != null){
            ingredients.getAddedByOwner().add(utilities.owner);
            title = utilities.owner.getEmail();

        }
        ingredients.getCompany().add(utilities.company);
        ingredients.setIngredientCompany(utilities.company);
        ingredients.setQuantityUnit(unit);
    }
}
