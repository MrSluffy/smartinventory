package com.smart.inventory.application.views.menu.recent;

import com.smart.inventory.application.data.entities.Activity;
import com.smart.inventory.application.data.services.activity.ActivityService;
import com.smart.inventory.application.util.Utilities;
import com.smart.inventory.application.views.widgets.CustomDialog;
import com.smart.inventory.application.views.widgets.DeleteButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@PageTitle("Recent History")
public class RecentActivityView extends VerticalLayout {

    Grid<Activity> activityGrid = new Grid<>(Activity.class, false);

    Utilities utilities = new Utilities();
    Dialog dialog = new Dialog();

    DeleteButton delete = new DeleteButton();
    private final ActivityService service;

    public RecentActivityView(ActivityService service){
        this.service = service;
        addClassName("gen-view");
        setSizeFull();

        configureGrid();

        configureDialog();

        add(getContent(), getFooter());
        updateList();

    }

    private void configureGrid() {
        activityGrid.addClassName("configure-itemgrid");
        activityGrid.setSizeFull();
        activityGrid.setRowsDraggable(true);
        activityGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        activityGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);

        activityGrid.addColumn(creatActivityRenderer()).setHeader("Activity History");
        activityGrid.addColumn(Activity::getDate).setHeader("Date");

        activityGrid.addSelectionListener(selection -> {
            int size = selection.getAllSelectedItems().size();
            delete.setVisible(size != 0);
        });
    }

    private Renderer<Activity> creatActivityRenderer() {
        return LitRenderer.<Activity>of("<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                + "    <span> ${item.activityTitle} </span>"
                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                + "      ${item.activitySubTitle}" + "    </span>"
                + "  </vaadin-vertical-layout>"
                + "</vaadin-horizontal-layout>")
                .withProperty("activityTitle", Activity::getActivityTitle)
                .withProperty("activitySubTitle", Activity::getActivitySubTitle);
    }

    private void configureDialog() {
        CustomDialog customDialog = new CustomDialog(dialog, "activity");
        customDialog.confirm(clickEvent -> {
            this.deleteAll(
                    new RecentActivityViewEvent.DeleteEvent(this,
                            new Activity()));
            dialog.close();
        });
        dialog.add(customDialog);
    }

    @Nonnull
    private HorizontalLayout getFooter() {
        HorizontalLayout footer = new HorizontalLayout(getFab());
        footer.setWidthFull();
        footer.setClassName("footer");
        footer.getStyle().set("flex-wrap", "wrap");
        footer.setJustifyContentMode(JustifyContentMode.END);
        return footer;
    }

    private void updateList() {
        activityGrid.setItems(service.findAllActivity(utilities.company.getId()));
        delete.setVisible(!activityGrid.asMultiSelect().isEmpty());
    }

    @Nonnull
    private Component getFab() {

        delete.addClickListener(deleteEvnt -> {
            if (!activityGrid.asMultiSelect().isEmpty()) {
                dialog.open();
            }
        });
        return new HorizontalLayout(delete);
    }

    @Nonnull
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(activityGrid);
        content.setFlexGrow(1, activityGrid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private void deleteAll(RecentActivityViewEvent.DeleteEvent deleteEvent) {
        service.delete(new ArrayList<>(activityGrid.getSelectedItems()), utilities);
        activityGrid.getDataProvider().refreshAll();
        updateList();
        Notification.show("Successfully deleted ",
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }


    public static abstract class RecentActivityViewEvent extends ComponentEvent<RecentActivityView> {


        private final Activity activity;

        public RecentActivityViewEvent(RecentActivityView source, Activity activity) {
            super(source, false);

            this.activity = activity;
        }

        public Activity getActivity() {
            return activity;
        }

        public static class DeleteEvent extends RecentActivityViewEvent {
            DeleteEvent(RecentActivityView source, Activity activity) {
                super(source, activity);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}