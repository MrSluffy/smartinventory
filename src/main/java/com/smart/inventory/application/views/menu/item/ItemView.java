package com.smart.inventory.application.views.menu.item;

import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.services.item.ItemsService;
import com.smart.inventory.application.views.widgets.DeleteButton;
import com.smart.inventory.application.views.widgets.FilterText;
import com.smart.inventory.application.views.widgets.PlusButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import org.vaadin.haijian.Exporter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Item Stock")
public class ItemView extends VerticalLayout {

    //TODO MAKE IT NICE :0

    Grid<Item> itemGrid = new Grid<>(Item.class, false);
    FilterText filterText = new FilterText();

    PlusButton plusButton = new PlusButton();

    DeleteButton delete = new DeleteButton();

    ConfirmDialog dialog = new ConfirmDialog();

    Anchor anchor;

    private final ItemForm itemForm;
    private final ItemsService service;

    private final Company company = VaadinSession.getCurrent().getAttribute(Company.class);

    public ItemView(ItemsService service) {
        this.service = service;
        addClassName("gen-view");
        setSizeFull();

        itemForm = new ItemForm();

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
        dialog.addConfirmListener(confirmEvent ->
                ItemView.this.deleteItem(
                        new ItemViewEvent.DeleteEvent(ItemView.this,
                                itemForm.getItem())));
    }

    @Nonnull
    private HorizontalLayout getFooter() {
        HorizontalLayout footer = new HorizontalLayout(anchor, getFab());
        footer.setWidthFull();
        footer.setClassName("footer");
        footer.getStyle().set("flex-wrap", "wrap");
        footer.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return footer;
    }

    @Nonnull
    private Component getFab() {
        plusButton.addClickListener(click -> addItem());

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
        return new HorizontalLayout(plusButton, delete);
    }

    private void updateList() {
        itemGrid.setItems(service.findAllItem(company.getId().toString()));
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Item> dataProvider = (ListDataProvider<Item>) itemGrid.getDataProvider();
        dataProvider.setFilter(Item::getItemName, s -> caseInsensitiveContainsFilter(s, event.getValue()));
    }

    private Boolean caseInsensitiveContainsFilter(String value, String filter) {
        return value.toLowerCase().contains(filter.toLowerCase());
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
        filterText.addValueChangeListener(this::onNameFilterTextChange);
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
                "itemName",
                "quantity",
                "price",
                "totalPrice",
                "dateAndTime");
        itemGrid.getColumns().get(0)
                .setHeader("Item Name")
                .setFrozen(true)
                .setFlexGrow(0);
        itemGrid.getColumns().get(1)
                .setHeader("Quantity");
        itemGrid.getColumns().get(2)
                .setHeader("Price");
        itemGrid.getColumns().get(3)
                .setHeader("Total Price");
        itemGrid.getColumns().get(4)
                .setHeader("Last Updated ")
                .setAutoWidth(false);
        itemGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));

        anchor = new Anchor(new StreamResource("smartinventory-" + getCurrentPageTitle()+
                LocalDateTime.now().toLocalDate().toString() +".xlsx",
                Exporter.exportAsExcel(itemGrid)), "Download As Excel");
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

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


    //TODO
    private String createTotalFooterText(@Nonnull List<Item> item) {
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

    private void saveItem(@Nonnull ItemForm.ItemFormEvent.SaveEvent event) {
        event.getItem().setDateAndTime(LocalDateTime.now().toLocalTime().toString().substring(0, 5) + "-" + LocalDateTime.now().toLocalDate().toString());
        service.saveItem(event.getItem());
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
