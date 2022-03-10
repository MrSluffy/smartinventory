package com.smart.inventory.application.views.menu.dashboard;

import com.smart.inventory.application.data.entities.Employer;
import com.smart.inventory.application.data.entities.TodoItem;
import com.smart.inventory.application.data.services.todo.TodoService;
import com.smart.inventory.application.util.Utilities;
import com.smart.inventory.application.views.widgets.MaterialCardBackground;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;

import javax.annotation.Nonnull;
import java.util.List;

@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

    private final TodoService service;

    MaterialCardBackground materialCardBackground;

    Span assignee = new Span();
    ContextMenu menu = new ContextMenu();

    Dialog dialog = new Dialog();

    Button save = new Button("Save");

    private final String flexWrap = "flex-wrap";
    private final String wrap = "wrap";

    TextField textField = new TextField();


    MultiSelectListBox<TodoItem> listBox = new MultiSelectListBox<>();

    private Employer employer;

    Utilities utilities = new Utilities();


    public DashboardView(@Nonnull TodoService service) {
        this.service = service;
        setSizeFull();
        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        materialCardBackground = new MaterialCardBackground();

        materialCardBackground.add(configureTodo());

        menu.setTarget(assignee);
        listBox.setWidthFull();


        setPadding(true);

        setHorizontalComponentAlignment(Alignment.CENTER);
        add(materialCardBackground, dialog);

        updateList();


    }

    private void updateList() {
        listBox.setItems(service.findAllTodoItem(utilities));
        listBox.setRenderer(new ComponentRenderer<>(todoItem -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(FlexComponent.Alignment.CENTER);
            Span employee = new Span(todoItem.getEmployer().getEmail());
            Span title = new Span(todoItem.getTitle());
            title.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");
            VerticalLayout column = new VerticalLayout(employee, title);
            column.setPadding(false);
            column.setSpacing(false);
            row.add(column);
            row.getStyle().set("line-height", "var(--lumo-line-height-m)");
            return row;
        }));
    }

    @Nonnull
    private VerticalLayout createDialogLayout(@Nonnull Dialog dialog) {
        VerticalLayout layout = new VerticalLayout();

        var headline = new H3("Add new note");

        Button close = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        close.addClickListener(event -> dialog.close());

        HorizontalLayout header = new HorizontalLayout(headline, close);
        header.getStyle().set(flexWrap, wrap);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setWidthFull();
        header.addClassNames("border-b", "border-contrast-10");
        header.getElement().getClassList().add("draggable");

        layout.setWidthFull();

        dialog.setModal(false);
        dialog.setDraggable(true);
        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        dialog.setMaxHeight("80%");
        dialog.setWidth("26%");

        textField.setWidthFull();
        textField.setLabel("Title");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        List<Employer> employerList = service.findAllEmployer(utilities);

        for (Employer employer : employerList) {
            MenuItem menuItem = menu.addItem(employer.getEmail(), event -> {
                setAssignee(employer);
            });
            menuItem.setCheckable(true);
            this.employer = employer;
        }

        if (!employerList.isEmpty()){
            setAssignee(employerList.get(0));
        }
        else{
            assignee.setText("You don't have any employer to assign");
            textField.setEnabled(false);
            save.setEnabled(false);
        }

        save.addClickListener(buttonClickEvent -> addTodo(employer));
        var hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.setJustifyContentMode(JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(save);

        var hlfirstRow = new HorizontalLayout();
        hlfirstRow.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hlfirstRow.setWidthFull();
        hlfirstRow.add(new Label("Assign to: "), assignee);

        layout.add(header, hlfirstRow, textField, hl);

        return layout;
    }

    private void addTodo(Employer employer) {
        service.addTodo(textField.getValue(), employer, utilities);
        updateList();
        dialog.close();
    }

    private void setAssignee(Employer employer) {
        // Update checked state of menu items
        menu.getItems().forEach(
                item -> item.setChecked(item.getText().equals(employer.getEmail()))
        );


        if (employer != null) {
            assignee.setText(employer.getEmail());
        } else {
            // Todo : extract this and determine if user/client is using mobile or desktop
            assignee.setText("Tap and hold | Right Click");
        }
    }

    @Nonnull
    private Component configureTodo() {

        H4 title = new H4("Note");

        Button plusButton = new Button(new Icon(VaadinIcon.PLUS));

        plusButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);

        plusButton.addClickListener(event -> dialog.open());

        HorizontalLayout hr = new HorizontalLayout();
        hr.setWidthFull();
        hr.getStyle().set(flexWrap, wrap);
        hr.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hr.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hr.add(title, plusButton);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);


        layout.add(hr, listBox);

        return layout;
    }

}
