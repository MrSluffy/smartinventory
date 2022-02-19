package com.smart.inventory.application.views.menu.account;

import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.services.employer.EmployerService;
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

@PageTitle("My Employer")
public class AccountView extends VerticalLayout {

    Grid<Employer> grid = new Grid<>(Employer.class, false);

    FilterText filterText = new FilterText();

    PlusButton plusButton = new PlusButton();

    DeleteButton delete = new DeleteButton();

    Dialog dialog = new Dialog();

    Utilities utilities = new Utilities();

    Anchor anchor;

    private final AccountForm form;

    private final EmployerService service;

    public AccountView(EmployerService service){
        this.service = service;
        addClassName("gen-view");
        setSizeFull();

        form = new AccountForm(service.findAllPosition());

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
        grid.setItems(service.findAllEmployer(utilities.company.getName()));
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
        plusButton.addClickListener(click -> addNewEmployer());

        delete.addClickListener(deleteEvnt -> {
            if (!grid.asMultiSelect().isEmpty()) {
                dialog.open();
            }
        });
        return new HorizontalLayout(plusButton, delete);
    }

    private void addNewEmployer() {
        Employer employer = new Employer();
        form.setEmployerNew(employer);
        form.setVisible(true);
        plusButton.setVisible(false);
        delete.setVisible(true);
        addClassName("editing");
        delete.setVisible(false);
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
        filterText.setPlaceholder("Search employer by email...");
        filterText.addValueChangeListener(this::onNameFilterTextChange);
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        plusButton.setVisible(false);
        delete.setVisible(true);
        return toolbar;
    }

    private void onNameFilterTextChange(HasValue.ValueChangeEvent<String> event) {
        ListDataProvider<Employer> dataProvider = (ListDataProvider<Employer>) grid.getDataProvider();
        dataProvider.setFilter(Employer::getEmail, s -> Utilities.caseInsensitiveContainsFilter(s, event.getValue()));
    }

    private void configureDialog() {
        CustomDialog customDialog = new CustomDialog(dialog, "employer");
        customDialog.confirm(clickEvent -> {
            this.deleteEmployer(new AccountViewEvent.DeleteEvent(this, form.getEmployer()));
            dialog.close();
        });
        dialog.add(customDialog);
    }

    private void deleteEmployer(AccountViewEvent.DeleteEvent deleteEvent) {
        service.deleteEmployerSelected(new ArrayList<>(grid.getSelectedItems()));
        grid.getDataProvider().refreshAll();
        updateList();
        Notification.show("Successfully deleted ",
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        closeEditor();
    }

    private void addEmployer(AccountForm.AccountFormEvent.AddEvent addEvent){
        service.addNewEmployer(
                form.email.getValue(),
                form.firstName.getValue(),
                form.lastName.getValue(),
                form.passwordField.getValue(),
                form.position.getValue(), utilities);
        closeEditor();
        updateList();
    }

    private void configureForm() {
        form.setWidth("25em");
        form.addListener(AccountForm.AccountFormEvent.AddEvent.class, this::addEmployer);
        addListener(AccountViewEvent.DeleteEvent.class, this::deleteEmployer);
        form.addListener(AccountForm.AccountFormEvent.CloseEvent.class, closeEvent -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassName("configure-itemgrid");
        grid.setSizeFull();
        grid.setRowsDraggable(true);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_ROW_STRIPES);

        grid.setColumns(
                "email",
                "firstName",
                "lastName");
        grid.addColumn(employer -> employer.getPosition().getPostionName()).setHeader("Position");
        anchor = new Anchor(new StreamResource("smartinventory-" + getCurrentPageTitle() +
                LocalDateTime.now().toLocalDate().toString() + ".xlsx",
                Exporter.exportAsExcel(grid)), "Download As Excel");
        grid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            form.setVisible(false);
            plusButton.setVisible(size == 0);
            delete.setVisible(size != 0);
        });
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }


    public static abstract class AccountViewEvent extends ComponentEvent<AccountView> {

        private final Employer employer;

        public AccountViewEvent(AccountView source, Employer employer) {
            super(source, false);
            this.employer = employer;
        }

       public Employer getEmployer(){
            return employer;
       }

        public static class DeleteEvent extends AccountViewEvent {
            DeleteEvent(AccountView source, Employer employer) {
                super(source, employer);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
