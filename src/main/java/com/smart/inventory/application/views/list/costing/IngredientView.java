package com.smart.inventory.application.views.list.costing;

import com.smart.inventory.application.data.entity.costing.Ingredients;
import com.smart.inventory.application.data.service.SmartInventoryService;
import com.smart.inventory.application.views.MainLayout;
import com.smart.inventory.application.views.widgets.DeleteButton;
import com.smart.inventory.application.views.widgets.FilterText;
import com.smart.inventory.application.views.widgets.PlusButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.vaadin.haijian.Exporter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Route(value = "costing", layout = MainLayout.class)
@PageTitle("Costing")
public class IngredientView extends VerticalLayout {

    Grid<Ingredients> grid = new Grid<>(Ingredients.class, false);

    FilterText filterText = new FilterText();

    PlusButton plusButton = new PlusButton();

    DeleteButton delete = new DeleteButton();

    ConfirmDialog dialog = new ConfirmDialog();

    Anchor anchor;

    private final IngredientsForm ingredientsForm;

    private final SmartInventoryService service;


    public IngredientView(SmartInventoryService service) {
        this.service = service;
        addClassName("costing-view");
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
        dialog.setText("Are you sure you want to permanently delete this ingredient?");
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.setCancelable(true);
        dialog.addConfirmListener(confirmEvent ->
                this.deleteIngredient(new CostingViewEvent.DeleteEvent(this, ingredientsForm.getCosting())));
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
        plusButton.addClickListener(click -> addNewCosting());

        delete.addClickListener(deleteEvnt -> {
            int selectedSize = grid.asMultiSelect().getValue().size();
            if (!grid.asMultiSelect().isEmpty()) {
                dialog.open();
                dialog.setHeader(
                        "Delete " +
                                selectedSize +
                                " selected ingredients?");
            }
        });
        return new HorizontalLayout(plusButton, delete);
    }

    private void updateList() {
        grid.getDataProvider().addDataProviderListener(
                dataChangeEvent ->
                        dataChangeEvent.getSource().refreshAll());
        grid.setItems(service.findAllCosting(filterText.getValue()));
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
        addListener(CostingViewEvent.DeleteEvent.class, this::deleteIngredient);
        ingredientsForm.addListener(IngredientsForm.CostingFormEvent.CloseEvent.class, closeEvent -> closeEditor());
    }

    @Nonnull
    private Component getToolbar() {
        filterText.setPlaceholder("Search item by ingredients...");

        filterText.addValueChangeListener(e -> updateList());
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
                "id",
                "productName",
                "productQuantity",
                "productPrice",
                "totalCost");
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

    private void editCosting(Ingredients ingredients) {
        if (ingredients != null) {
            ingredientsForm.setCosting(ingredients);
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

    private void addNewCosting() {
        editCosting(new Ingredients());
    }

    private void deleteIngredient(CostingViewEvent.DeleteEvent deleteEvent) {
        service.deleteIngredientSelected(new ArrayList<>(grid.getSelectedItems()));
        grid.getDataProvider().refreshAll();
        updateList();
        Notification.show("Successfully deleted ",
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        closeEditor();
    }

    private void saveIngredient(IngredientsForm.CostingFormEvent.SaveEvent saveEvent) {
        service.saveIngredient(saveEvent.getCosting());
        updateList();
        closeEditor();
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    public static abstract class CostingViewEvent extends ComponentEvent<IngredientView> {

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
