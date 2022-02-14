package com.smart.inventory.application.views.list.dashboard;

import com.smart.inventory.application.data.services.item.ItemsService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;

import javax.annotation.Nonnull;

@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

    private final ItemsService service;

    public DashboardView(ItemsService service) {
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(configreContent());
    }

    private Component configreContent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.add(getSummary(), getItemsChart());
        return verticalLayout;
    }

    private Component getSummary() {
        Chart chart = new Chart(ChartType.LINE);
        DataSeries series = new DataSeries();
        service.findAllItem().forEach(item -> {
            series.add(new DataSeriesItem(item.getItemName(), item.getPrice()));
        });
        chart.getConfiguration().addSeries(series);
        return chart;
    }

    @Nonnull
    private Chart getItemsChart() {
        Chart chart = new Chart(ChartType.PIE);
        DataSeries dataSeries = new DataSeries();
        service.findAllItem().forEach(items ->
                dataSeries.add(new DataSeriesItem(items.getItemName(),
                        items.getPrice())));
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Item data");
        conf.setSubTitle("Summary of the items of the company");
        conf.setSeries(dataSeries);
        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setMarker(new Marker(false));
        conf.setPlotOptions(plotOptions);
        return chart;
    }
}
