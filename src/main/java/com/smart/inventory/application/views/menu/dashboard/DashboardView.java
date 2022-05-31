package com.smart.inventory.application.views.menu.dashboard;

import com.smart.inventory.application.data.entities.Employer;
import com.smart.inventory.application.data.entities.TodoItem;
import com.smart.inventory.application.data.services.todo.TodoService;
import com.smart.inventory.application.util.Utilities;
import com.smart.inventory.application.views.widgets.MaterialCardBackground;
import com.storedobject.chart.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
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

    Dialog dialog = new Dialog();

    Button save = new Button("Save");

    private final String flexWrap = "flex-wrap";
    private final String wrap = "wrap";

    TextField textField = new TextField();


    MultiSelectListBox<TodoItem> listBox = new MultiSelectListBox<>();

    Utilities utilities = new Utilities();

    ComboBox<Employer> employerComboBox = new ComboBox<>("Assign to :");


    public DashboardView(@Nonnull TodoService service) {
        this.service = service;
        setSizeFull();
        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        materialCardBackground = new MaterialCardBackground();

        listBox.setWidthFull();
        listBox.addClassNames("db-note-content");

        materialCardBackground.add(configureTodo());

        setPadding(true);

        setHorizontalComponentAlignment(Alignment.CENTER);

        add(materialCardBackground, dialog);
        add(configureChart());

        updateList();


    }


    private Component configureChart(){

        HorizontalLayout layout = new HorizontalLayout();

        // Creating a chart display area
        SOChart soChart = new SOChart();
        soChart.setSize("900px", "500px");

        // Let us define some inline data
        CategoryData labels =
                new CategoryData("April Fool's Day", "Marriage Day", "Election Day", "Any Other Day");
        Data data = new Data(5, 20, 100, 2);

        // Axes
        XAxis xAxis;
        YAxis yAxis;

        // Bar chart
        BarChart bc1 = new BarChart(labels, data); // First bar chart
        xAxis = new XAxis(labels);
        xAxis.getLabel(true).setRotation(45);
        yAxis = new YAxis(data);
        RectangularCoordinate coordinate = new RectangularCoordinate(xAxis, yAxis);
        bc1.plotOn(coordinate); // Bar chart needs to be plotted on a coordinate system
        coordinate.getPosition(true).setRight(Size.percentage(60)); // Leave space on the right side

        BarChart bc2 = new BarChart(data, labels); // Second bar chart
        xAxis = new XAxis(data);
        yAxis = new YAxis(labels);
        coordinate = new RectangularCoordinate(xAxis, yAxis);
        bc2.plotOn(coordinate); // Bar chart needs to be plotted on a coordinate system
        coordinate.getPosition(true).setLeft(Size.percentage(60)); // Leave space on the left side

        // Just to demonstrate it, we are creating a "Download" and a "Zoom" toolbox button
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());

        // Switching off the default legend
        soChart.disableDefaultLegend();

        // Let's add some titles
        Title title = new Title("Probability of Getting Fooled");
        title.setSubtext("Truth is always simple but mostly hidden - Syam");

        // Add the chart components to the chart display area
        soChart.add(bc1, bc2, toolbox, title);

        // Add to the view
        layout.add(soChart);

        return layout;
    }

    private void updateList() {
        if (utilities.owner != null) {
            listBox.setRenderer(new ComponentRenderer<>(todoItem -> {
                HorizontalLayout row = new HorizontalLayout();
                Span employee = new Span(todoItem.getEmployer().getFirstName());
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
        if (utilities.employer != null) {
            listBox.setRenderer(new ComponentRenderer<>(todoItem -> {
                HorizontalLayout row = new HorizontalLayout();
                Span title = new Span(todoItem.getTitle());
                VerticalLayout column = new VerticalLayout(title);
                column.setPadding(false);
                column.setSpacing(false);
                row.add(column);
                row.getStyle().set("line-height", "var(--lumo-line-height-m)");
                return row;
            }));
        }

        listBox.setItems(service.findAllTodoItem(utilities));

    }

    @Nonnull
    private VerticalLayout createDialogLayout(@Nonnull Dialog dialog) {
        VerticalLayout layout = new VerticalLayout();

        layout.addClassName("db-dialog");

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

        employerComboBox.setItems(employerList);
        employerComboBox.setItemLabelGenerator(Employer::getEmail);
        employerComboBox.setPlaceholder("Select employee");
        employerComboBox.setWidthFull();
        employerComboBox.addThemeVariants(ComboBoxVariant.LUMO_ALIGN_LEFT);


        if (employerList.isEmpty()) {
            textField.setEnabled(false);
            employerComboBox.setPlaceholder("No employees");
            save.setEnabled(false);
        }

        save.addClickListener(buttonClickEvent -> addTodo(employerComboBox.getValue()));
        var hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(Alignment.END);
        hl.setJustifyContentMode(JustifyContentMode.END);
        hl.setWidthFull();
        hl.add(save);

        layout.add(header, employerComboBox, textField, hl);

        return layout;
    }

    private void addTodo(Employer employer) {
        service.addTodo(textField.getValue(), employer, utilities);
        updateList();
        dialog.close();
    }

    @Nonnull
    private Component configureTodo() {

        String titleNote = utilities.employer != null ? "Note" : "Master Note";

        H4 title = new H4(titleNote);

        Button plusButton;


        if (utilities.owner != null) {
            plusButton = new Button(new Icon(VaadinIcon.PLUS));
            plusButton.addClickListener(event -> dialog.open());
        } else {
            plusButton = new Button(new Icon(VaadinIcon.ELLIPSIS_DOTS_V));
        }

        plusButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);


        HorizontalLayout hr = new HorizontalLayout();
        hr.setWidthFull();
        hr.getStyle().set(flexWrap, wrap);
        hr.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        hr.setJustifyContentMode(JustifyContentMode.BETWEEN);
        hr.add(title, plusButton);

        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("db-note");
        layout.setPadding(true);


        layout.add(hr, listBox);

        return layout;
    }

}
