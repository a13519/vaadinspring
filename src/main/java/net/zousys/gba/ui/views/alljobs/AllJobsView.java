package net.zousys.gba.ui.views.alljobs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import net.zousys.gba.function.batch.dto.JobDTO;
import net.zousys.gba.function.batch.service.BatchService;
import net.zousys.gba.ui.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@PageTitle("All Jobs")
@Menu(icon = "line-awesome/svg/angle-double-down-solid.svg", order = 0)
@Route(value = "all-jobs", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class AllJobsView extends Composite<VerticalLayout> {

    @Autowired()
    private BatchService batchService;
    private int page = 0;
    private final int size = 16;
    private Grid stripedGrid;
    private Sort sort = Sort.by("id").descending();
    private H4 h6;
    /**
     *
     */
    public AllJobsView() {
        Tabs tabs = new Tabs();
        Button refreshButton = new Button("Refresh", e -> navigatePage(-0));
        Button prevButton = new Button("Previous", e -> navigatePage(-1));
        h6 = new H4("Page 0");
        Button nextButton = new Button("Next", e -> navigatePage(1));
        HorizontalLayout paginationLayout = new HorizontalLayout(refreshButton, prevButton, h6, nextButton);
        paginationLayout.setWidthFull();  // Set layout to full width
        paginationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);  // Align to the right

        stripedGrid = new Grid(JobDTO.class, false);
        stripedGrid.addColumn("name");
        stripedGrid.addColumn("id");
        stripedGrid.addColumn("parameters");
        stripedGrid.addColumn("started").setSortable(true);
        stripedGrid.addColumn("ended");
        stripedGrid.addColumn("status");

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabs.setWidth("100%");

        setTabsSampleData(tabs);

        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.setPageSize(size);
//        stripedGr
        stripedGrid.getStyle().set("flex-grow", "0");

        setJobGridData(stripedGrid);

        getContent().add(tabs);
        getContent().add(stripedGrid, paginationLayout);
//        loadPage(page);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("History"));
        tabs.add(new Tab("Run A Job"));
    }

    private void setJobGridData(Grid grid) {

        // Set up the data provider for pagination
        DataProvider<JobDTO, Void> dataProvider = DataProvider.fromCallbacks(
                // Fetch data based on page size and offset
                query -> {
                    int offset = query.getOffset();  // Starting point (row number)
                    int limit = query.getLimit();    // Number of items to fetch (page size)
                    h6.setText("Page "+offset/limit);
                    page = offset/limit;
                    return batchService.listAll(PageRequest.of(offset/limit, limit, sort)).stream();
                },
                // Count the total number of entities (for grid sizing)
                query -> batchService.countEntities()
        );

        grid.setDataProvider(dataProvider);
    }

    private void loadPage(int pageNumber) {
        List<JobDTO> entities = batchService.listAll(PageRequest.of(pageNumber, size, sort));
        stripedGrid.setItems(entities);
    }

    private void navigatePage(int direction) {
        page += direction;
        page = Math.max(0, page);
        h6.setText("Page "+page);
        loadPage(page);
    }

}
