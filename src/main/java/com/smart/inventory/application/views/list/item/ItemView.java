package com.smart.inventory.application.views.list.item;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.service.SmartInventoryService;
import com.smart.inventory.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import org.vaadin.haijian.Exporter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Route(value="item", layout = MainLayout.class)
@PageTitle("Item Stock")
public class ItemView extends VerticalLayout {

    //TODO MAKE IT NICE :0

    Grid<Item> itemGrid = new Grid<>(Item.class, false);
    TextField filterText = new TextField();

    Button plusButton, delete;

    ConfirmDialog dialog = new ConfirmDialog();

    Anchor anchor;

    ItemForm itemForm = new ItemForm();
    private final SmartInventoryService service;

    public ItemView(SmartInventoryService service) {
        this.service = service;
        addClassName("item-view");
        setSizeFull();

        getFab();

        configureGrid();
        configureForm();
        configureDialog();

        add(getToolbar(), getContent(), getFooter());
        updateList();

        closeEditor();
    }

    private void configureDialog() {
        dialog.setText("Are you sure you want to permanently delete this item?");
        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.setCancelable(true);
        dialog.addConfirmListener(confirmEvent -> deleteItem(new ItemViewEvent.DeleteEvent(this, itemForm.getItem())));
    }

    @Nonnull
    private HorizontalLayout getFooter() {
        HorizontalLayout footer = new HorizontalLayout(plusButton, delete, anchor);
        footer.setWidthFull();
        footer.setClassName("footer");
        footer.getStyle().set("flex-wrap", "wrap");
        footer.setJustifyContentMode(JustifyContentMode.END);
        return footer;
    }

    private void getFab() {
        plusButton = new Button(new Icon(VaadinIcon.PLUS));
        plusButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON);
        plusButton.setClassName("fab-plus");
        plusButton.setMaxHeight(8f, Unit.EX);
        plusButton.setWidth(8f, Unit.EX);
        plusButton.getStyle().set("margin-inline-end", "auto");
        plusButton.getElement().setAttribute("aria-label", "Add item");
        plusButton.setAutofocus(true);
        plusButton.addClickListener(click -> addItem());

        delete = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        delete.setClassName("fab-del");
        delete.setMaxHeight(8f, Unit.EX);
        delete.setWidth(8f, Unit.EX);
        delete.getStyle().set("margin-inline-end", "auto");
        delete.getElement().setAttribute("aria-label", "Add item");
        delete.setAutofocus(true);
    }

    private void updateList() {
        itemGrid.getDataProvider().addDataProviderListener(
                dataChangeEvent ->
                        dataChangeEvent.getSource().refreshAll());
        itemGrid.setItems(service.findAllItem(filterText.getValue()));
    }

    @Nonnull
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(itemGrid, itemForm);
        content.setFlexGrow(2, itemGrid);
        content.setFlexGrow(1, itemForm);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        itemForm.setWidth("25em");
        itemForm.addListener(ItemForm.ItemFormEvent.SaveEvent.class, this::saveItem);
        addListener(ItemViewEvent.DeleteEvent.class, this::deleteItem);
        itemForm.addListener(ItemForm.ItemFormEvent.CloseEvent.class, closeEvent -> closeEditor());
    }

    @Nonnull
    private Component getToolbar() {
        filterText.setPlaceholder("Search item by name...");
        filterText.setWidth(20f, Unit.EM);
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        plusButton.setVisible(false);
        delete.setVisible(true);
        return toolbar;
    }

    private void configureGrid() {
        itemGrid.addClassName("configure-itemgrid");
        itemGrid.setSizeFull();
        itemGrid.setRowsDraggable(true);
        itemGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        itemGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        itemGrid.setColumns(
                "id",
                "itemName",
                "quantity",
                "price",
                "totalPrice",
                "dateAndTime");
        itemGrid.getColumns()
                .get(0).setHeader("Item ID")
                .setFrozen(true)
                .setFlexGrow(0);
        itemGrid.getColumns().get(1)
                .setHeader("Item Name")
                .setFrozen(true)
                .setFlexGrow(0);
        itemGrid.getColumns().get(2)
                .setHeader("Quantity");
        itemGrid.getColumns().get(3)
                .setHeader("Price");
        itemGrid.getColumns().get(4)
                .setHeader("Total Price");
        itemGrid.getColumns().get(5)
                .setHeader("Last Updated ")
                .setAutoWidth(false);
        itemGrid.getColumns().forEach(itemColumn -> {
            itemColumn.setResizable(true);
            itemColumn.setAutoWidth(true);
        });

        anchor = new Anchor(new StreamResource("my-excel.xlsx", Exporter.exportAsExcel(itemGrid)), "Download As Excel");

        delete.addClickListener(deleteEvnt -> {
            int selectedSize = itemGrid.asMultiSelect().getValue().size();
            if (!itemGrid.asMultiSelect().isEmpty()) {
                dialog.open();
                dialog.setHeader(
                        "Delete " +
                                selectedSize +
                                " selected items?");
            }
        });
        itemGrid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            if (selection.getFirstSelectedItem().isPresent()) {
                editItem(selection.getFirstSelectedItem().get());
            }
            boolean isSingleSelection = size == 1;
            itemForm.setVisible(isSingleSelection);
            plusButton.setVisible(size == 0);
            delete.setVisible(size != 0);
        });
    }


    //TODO
    private String createTotalFooterText(List<Item> item) {
        double totalPrice = 0;
        for (Item items : item) {
            totalPrice += items.getTotalPrice();
        }
        return String.format("%s total amount", totalPrice);
    }


    private void editItem(Item item) {
        if (item != null) {
            itemForm.setItem(item);
            itemForm.setVisible(true);
            plusButton.setVisible(false);
            delete.setVisible(true);
            addClassName("editing");
        }
        delete.setVisible(false);
    }

    private void closeEditor() {
        itemForm.setVisible(false);
        plusButton.setVisible(true);
        delete.setVisible(false);
        removeClassName("editing");
        itemGrid.asMultiSelect().clear();
    }

    private void addItem() {
        editItem(new Item());
    }

    private void deleteItem(ItemViewEvent event) {
        service.deleteItemSelected(new ArrayList<>(itemGrid.getSelectedItems()));
        itemGrid.getDataProvider().refreshAll();
        updateList();
        Notification.show("Successfully deleted ",
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        closeEditor();
    }

    private void saveItem(ItemForm.ItemFormEvent.SaveEvent event) {
        event.getItem().setDateAndTime(LocalDateTime.now().toLocalTime().toString().substring(0, 5) + "-" + LocalDateTime.now().toLocalDate().toString());
        service.saveItem(event.getItem());
        itemForm.totalPrice.setPlaceholder(String.valueOf(event.getItem().getTotalPrice()));
        updateList();
        closeEditor();
    }

    public static abstract class ItemViewEvent extends ComponentEvent<ItemView> {

        private final Item item;

        public ItemViewEvent(ItemView source, Item item) {
            super(source, false);
            this.item = item;

        }

        public Item getItem() {
            return item;
        }

        public static class DeleteEvent extends ItemViewEvent {
            DeleteEvent(ItemView source, Item item) {
                super(source, item);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
