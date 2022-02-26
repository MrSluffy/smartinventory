package com.smart.inventory.application.views.menu.sold;

import com.smart.inventory.application.data.entity.SoldItem;
import com.smart.inventory.application.data.services.solditem.SoldItemService;
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

@PageTitle("Sold Item")
public class SoldItemView extends VerticalLayout {
    Grid<SoldItem> grid = new Grid<>(SoldItem.class, false);
    private final SoldItemService service;

    FilterText filterText = new FilterText();

    PlusButton plusButton = new PlusButton();

    DeleteButton delete = new DeleteButton();

    Utilities utilities = new Utilities();

    Dialog dialog = new Dialog();

    Anchor anchor;

    private final SoldItemForm form;


    public SoldItemView(SoldItemService service){
        this.service = service;
        addClassName("gen-view");
        setSizeFull();
        form = new SoldItemForm(service.findAllItems(utilities.company.getId()));
        configureGrid();
        configureForm();
        configureDialog();

        add(getToolbar(), getContent(), getFooter());

        updateList();

        closeEditor();

    }

    private void closeEditor() {
        form.setVisible(false);
        plusButton.setVisible(true);
        delete.setVisible(false);
        removeClassName("editing");
        grid.asMultiSelect().clear();
    }

    private void updateList() {
        grid.setItems(service.findAllSoldItems(utilities.company.getId()));
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
        plusButton.addClickListener(click -> addNewSoldItem());

        delete.addClickListener(deleteEvnt -> {
            int selectedSize = grid.asMultiSelect().getValue().size();
            if (!grid.asMultiSelect().isEmpty()) {
                dialog.open();
            }
        });
        return new HorizontalLayout(plusButton, delete);
    }

    private void addNewSoldItem() {
        form.setSoldItem(new SoldItem());
        form.add.setVisible(true);
        form.save.setVisible(false);
        form.setVisible(true);
        plusButton.setVisible(false);
        delete.setVisible(true);
        addClassName("editing");
    }


    @Nonnull
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    @Nonnull
    private Component getToolbar() {
        filterText.setPlaceholder("Search item by item name...");
        filterText.addValueChangeListener(this::onNameFilterTextChange);
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        plusButton.setVisible(false);
        delete.setVisible(true);
        return toolbar;
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<SoldItem> dataProvider = (ListDataProvider<SoldItem>) grid.getDataProvider();
        dataProvider.setFilter(soldItem -> soldItem.getItem().getItemName(), s -> Utilities.caseInsensitiveContainsFilter(s, event.getValue()));
    }


    private void configureDialog() {
        CustomDialog customDialog = new CustomDialog(dialog, "item");
        customDialog.confirm(clickEvent -> {
            SoldItemView.this.deleteSoldItem(
                    new SoldItemView.SoldItemViewEvent.DeleteEvent(SoldItemView.this,
                            form.getSoldItem()));
            dialog.close();
        });
        dialog.add(customDialog);
    }

    private void configureForm() {
        form.setWidth("25em");
        form.addListener(SoldItemForm.SoldItemFormEvent.SaveEvent.class, this::saveSoldItem);
        form.addListener(SoldItemForm.SoldItemFormEvent.AddEvent.class, this::addNewItem);
        addListener(SoldItemView.SoldItemViewEvent.DeleteEvent.class, this::deleteSoldItem);
        form.addListener(SoldItemForm.SoldItemFormEvent.CloseEvent.class, closeEvent -> closeEditor());
    }

    private void deleteSoldItem(SoldItemView.SoldItemViewEvent.DeleteEvent deleteEvent) {
        service.deleteSelectedSoldItem(new ArrayList<>(grid.getSelectedItems()));
        updateList();
        Notification.show("Successfully deleted ",
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        closeEditor();
    }

    private void addNewItem(SoldItemForm.SoldItemFormEvent.AddEvent addEvent) {
        service.addNewSoldItem(form.itemList.getValue(), form.description.getValue(), form.quantity.getValue(), utilities);
        closeEditor();
        updateList();

    }

    private void saveSoldItem(SoldItemForm.SoldItemFormEvent.SaveEvent saveEvent) {
        SoldItem soldItem = saveEvent.getSoldItem();
        service.updateSoldItem(
                soldItem.getId(),
                form.description.getValue(),
                form.itemList.getValue(),
                form.quantity.getValue(),
                utilities);
        updateList();
        closeEditor();

    }

    private void configureGrid() {
        grid.addClassName("configure-itemgrid");
        grid.setSizeFull();
        grid.setRowsDraggable(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumns("itemName","description", "quantity", "purchaseAmount", "dateAndTime");

        anchor = new Anchor(new StreamResource("smartinventory-" + "sold-item-" +
                LocalDateTime.now().toLocalDate().toString() + ".xlsx",
                Exporter.exportAsExcel(grid)), "Download As Excel");
        grid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            if (selection.getFirstSelectedItem().isPresent()) {
                editSoldItem(selection.getFirstSelectedItem().get());
            }
            boolean isSingleSelection = size == 1;
            form.setVisible(isSingleSelection);
            plusButton.setVisible(size == 0);
            delete.setVisible(size != 0);
        });
    }

    private void editSoldItem(@Nonnull SoldItem soldItem) {
        if(soldItem.getItem() != null){
            form.setSoldItem(soldItem);
            form.setVisible(true);
            form.save.setVisible(true);
            form.add.setVisible(false);
            plusButton.setVisible(false);
            delete.setVisible(true);
            addClassName("editing");
        }
        delete.setVisible(false);
    }


    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


    public static abstract class SoldItemViewEvent extends ComponentEvent<SoldItemView> {


        private final SoldItem soldItem;

        public SoldItemViewEvent(SoldItemView source, SoldItem soldItem) {
            super(source, false);
            this.soldItem = soldItem;
        }

        public SoldItem getCustomer() {
            return soldItem;
        }


        public static class DeleteEvent extends SoldItemViewEvent {
            DeleteEvent(SoldItemView source, SoldItem soldItem) {
                super(source, soldItem);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
