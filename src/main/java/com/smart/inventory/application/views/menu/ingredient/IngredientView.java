package com.smart.inventory.application.views.menu.ingredient;

import com.smart.inventory.application.data.entities.ingredients.Ingredients;
import com.smart.inventory.application.data.services.ingredient.IngredientsService;
import com.smart.inventory.application.util.Utilities;
import com.smart.inventory.application.views.widgets.CustomDialog;
import com.smart.inventory.application.views.widgets.DeleteButton;
import com.smart.inventory.application.views.widgets.FilterText;
import com.smart.inventory.application.views.widgets.PlusButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.vaadin.haijian.Exporter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;

@PageTitle("Costing")
public class IngredientView extends VerticalLayout {

    Grid<Ingredients> grid = new Grid<>(Ingredients.class, false);

    FilterText filterText = new FilterText();

    PlusButton plusButton = new PlusButton();

    DeleteButton delete = new DeleteButton();

    Dialog dialog = new Dialog();

    Anchor anchor;

    private final IngredientsForm ingredientsForm;

    private final IngredientsService service;

    Utilities utilities = new Utilities();


    public IngredientView(IngredientsService service) {
        this.service = service;
        addClassName("gen-view");
        setSizeFull();

        ingredientsForm = new IngredientsForm(service.findAllUnit());

        configureGrid();
        configureForm();
        configureDialog();

        add(getToolbar(), getContent(), getFooter());
        updateList();

        closeEditor();

    }

    private void configureDialog() {
        CustomDialog customDialog = new CustomDialog(dialog, "product");
        customDialog.confirm(clickEvent -> {
            this.deleteIngredient(new CostingViewEvent.DeleteEvent(this, ingredientsForm.getCosting()));
            dialog.close();
        });
        dialog.add(customDialog);
    }

    @Nonnull
    private Component getFooter() {
        HorizontalLayout footer = new HorizontalLayout(anchor, getFab());
        footer.setWidthFull();
        footer.setClassName("footer");
        footer.getStyle().set("flex-wrap", "wrap");
        footer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return footer;
    }

    private Component getFab() {
        plusButton.addClickListener(click -> addNewIngredient());

        delete.addClickListener(deleteEvnt -> {
            int selectedSize = grid.asMultiSelect().getValue().size();
            if (!grid.asMultiSelect().isEmpty()) {
                dialog.open();
            }
        });
        return new HorizontalLayout(plusButton, delete);
    }

    private void updateList() {
        if(!service.findAllIngredients().isEmpty()){
            grid.setItems(service.findAllIngredients(utilities.company.getId()));
        }
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Ingredients> dataProvider = (ListDataProvider<Ingredients>) grid.getDataProvider();
        dataProvider.setFilter(Ingredients::getProductName, s -> Utilities.caseInsensitiveContainsFilter(s, event.getValue()));
    }

    @Nonnull
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, ingredientsForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, ingredientsForm);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        ingredientsForm.setWidth("25em");
        ingredientsForm.addListener(IngredientsForm.CostingFormEvent.SaveEvent.class, this::saveIngredient);
        ingredientsForm.addListener(IngredientsForm.CostingFormEvent.AddEvent.class, this::addNewIngredient);
        addListener(CostingViewEvent.DeleteEvent.class, this::deleteIngredient);
        ingredientsForm.addListener(IngredientsForm.CostingFormEvent.CloseEvent.class, closeEvent -> closeEditor());
    }

    @Nonnull
    private Component getToolbar() {
        filterText.setPlaceholder("Search item by ingredients...");

        filterText.addValueChangeListener(this::onNameFilterTextChange);
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        plusButton.setVisible(false);
        delete.setVisible(true);
        return toolbar;
    }

    private void configureGrid() {

        grid.addClassName("configure-itemgrid");
        grid.setSizeFull();
        grid.setRowsDraggable(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns(
                "productName",
                "productQuantity",
                "productPrice",
                "totalCost");
        grid.addColumn(ingredients -> ingredients.getQuantityUnit().getUnitName()).setHeader("Unit/Measurement");
        anchor = new Anchor(new StreamResource("smartinventory-" + getCurrentPageTitle() +
                LocalDateTime.now().toLocalDate().toString() + ".xlsx",
                Exporter.exportAsExcel(grid)), "Download As Excel");
        grid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            if (selection.getFirstSelectedItem().isPresent()) {
                editCosting(selection.getFirstSelectedItem().get());
            }
            boolean isSingleSelection = size == 1;
            ingredientsForm.setVisible(isSingleSelection);
            plusButton.setVisible(size == 0);
            delete.setVisible(size != 0);
        });
    }

    private void addNewIngredient() {
        ingredientsForm.setCosting(new Ingredients());
        ingredientsForm.add.setVisible(true);
        ingredientsForm.save.setVisible(false);
        ingredientsForm.setVisible(true);
        plusButton.setVisible(false);
        delete.setVisible(true);
        addClassName("editing");
    }

    private void addNewIngredient(IngredientsForm.CostingFormEvent.AddEvent addEvent) {
        service.addIngredient(addEvent.getCosting(), ingredientsForm.unit.getValue(), utilities);
        updateList();
        closeEditor();
    }


    private void editCosting(Ingredients ingredients) {
        if (ingredients != null) {
            ingredientsForm.setCosting(ingredients);
            ingredientsForm.save.setVisible(true);
            ingredientsForm.add.setVisible(false);
            ingredientsForm.setVisible(true);
            plusButton.setVisible(false);
            delete.setVisible(true);
            addClassName("editing");
        }
        delete.setVisible(false);
    }


    private void closeEditor() {
        ingredientsForm.setVisible(false);
        plusButton.setVisible(true);
        delete.setVisible(false);
        removeClassName("editing");
        grid.asMultiSelect().clear();
    }


    private void deleteIngredient(CostingViewEvent.DeleteEvent deleteEvent) {
        service.deleteIngredientSelected(new ArrayList<>(grid.getSelectedItems()), utilities);
        grid.getDataProvider().refreshAll();
        updateList();
        Notification.show("Successfully deleted ",
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        closeEditor();
    }

    private void saveIngredient(IngredientsForm.CostingFormEvent.SaveEvent saveEvent) {
        Ingredients ingredients = saveEvent.getCosting();
        service.updateIngredient(ingredients.getId(),
                ingredientsForm.productName.getValue(),
                ingredientsForm.productQuantity.getValue(),
                ingredientsForm.productPrice.getValue(),
                ingredientsForm.unit.getValue(), utilities);
        updateList();
        closeEditor();
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    public static abstract class CostingViewEvent extends ComponentEvent<IngredientView>{

        private final Ingredients ingredients;

        public CostingViewEvent(IngredientView source, Ingredients ingredients) {
            super(source, false);
            this.ingredients = ingredients;

        }

        public Ingredients getCosting() {
            return ingredients;
        }

        public static class DeleteEvent extends CostingViewEvent {
            DeleteEvent(IngredientView source, Ingredients ingredients) {
                super(source, ingredients);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
