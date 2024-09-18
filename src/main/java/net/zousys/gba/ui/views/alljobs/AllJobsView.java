package net.zousys.gba.ui.views.alljobs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.SortOrderProvider;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.PermitAll;
import net.zousys.gba.function.batch.dto.JobDTO;
import net.zousys.gba.function.batch.service.BatchService;
import net.zousys.gba.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Function;
import java.util.stream.Stream;

@PageTitle("All Jobs")
@Menu(icon = "line-awesome/svg/angle-double-down-solid.svg", order = 0)
@Route(value = "all-jobs", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class AllJobsView extends Composite<VerticalLayout> {

    public AllJobsView() {
        Tabs tabs = new Tabs();
        Grid stripedGrid = new Grid(JobDTO.class, false);
        stripedGrid.addColumn("name");
        stripedGrid.addColumn("id");
        stripedGrid.addColumn("parameters");
        stripedGrid.addColumn("started").setSortable(true);
        stripedGrid.addColumn("finished");
        stripedGrid.addColumn("status");

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabs.setWidth("100%");
        setTabsSampleData(tabs);

        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");

        setGridSampleData(stripedGrid);

        getContent().add(tabs);
        getContent().add(stripedGrid);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("History"));
        tabs.add(new Tab("Payment"));
        tabs.add(new Tab("Shipping"));
    }

    private void setGridSampleData(Grid grid) {
        org.springframework.data.domain.Sort sort =
                org.springframework.data.domain.Sort.by("id").descending();

        Pageable pageable = PageRequest.of(0, 16, sort);

        grid.setItems(query -> batchService.listAll(
                PageRequest.of(query.getPage(), query.getPageSize(), sort))
                .stream());

    }

    @Autowired()
    private BatchService batchService;
}
