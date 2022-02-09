package com.smart.inventory.application.views.list.dashboard;

import com.smart.inventory.application.data.service.SmartInventoryService;
import com.smart.inventory.application.views.MainLayout;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {
    private final SmartInventoryService service;

    public DashboardView(SmartInventoryService service) {
        this.service = service;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(getCompaniesChart());
    }

    private Chart getCompaniesChart() {
        Chart chart = new Chart(ChartType.getDefault());
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
