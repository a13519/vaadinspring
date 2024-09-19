package net.zousys.gba.ui.views.alljobs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
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
import java.util.stream.Stream;

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
    private Grid<JobDTO> stripedGrid;
    private Sort sort = Sort.by("id").descending();
    private Text h6;
    /**
     *
     */
    public AllJobsView() {
        Tabs tabs = new Tabs();
        Button refreshButton = new Button("Refresh", e -> navigatePage(-0));
        Button prevButton = new Button("Previous", e -> navigatePage(-1));
        h6 = new Text("Page 0");
        Div textContainer = new Div(h6);
        textContainer.setWidth("150px");
        Button nextButton = new Button("Next", e -> navigatePage(1));
        HorizontalLayout paginationLayout = new HorizontalLayout(refreshButton, prevButton, nextButton);
        paginationLayout.setWidthFull();  // Set layout to full width
        paginationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);  // Align to the right

        HorizontalLayout rowLayout = new HorizontalLayout(textContainer, paginationLayout);
        rowLayout.setWidthFull();
        // Align one element to the left and another to the right
        rowLayout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);

        stripedGrid = new Grid(JobDTO.class, false);
        stripedGrid.addColumn("id").setHeader("Exe Id").setSortable(true);
        stripedGrid.addColumn("name");
        stripedGrid.addColumn("jobId").setHeader("Job Id").setSortable(true);
        stripedGrid.addColumn("parameters");
        stripedGrid.addColumn("started").setSortable(true);
        stripedGrid.addColumn("ended").setSortable(true);
        stripedGrid.addColumn("status").setSortable(true);
//        stripedGrid.addColumn(createToggleDetailsRenderer(stripedGrid));
        stripedGrid.addColumn(new ComponentRenderer<>(item -> {
            Button button = new Button("Show Details");
            button.addClickListener(event -> openPopup(item));  // Open popup on button click
            return button;
        })).setHeader("Actions");


        stripedGrid.setDetailsVisibleOnClick(false);
        stripedGrid.setItemDetailsRenderer(createPersonDetailsRenderer());

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabs.setWidth("100%");

        setTabsSampleData(tabs);

        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.setHeight("500px");
        stripedGrid.setPageSize(size);
//        stripedGr
        stripedGrid.getStyle().set("flex-grow", "0");

        setJobGridData(stripedGrid);

        getContent().add(tabs);
        getContent().add(stripedGrid, rowLayout);
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

    private static Renderer<JobDTO> createToggleDetailsRenderer(
            Grid<JobDTO> grid) {
        return LitRenderer.<JobDTO> of(
                        "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Toggle details</vaadin-button>")
                .withFunction("handleClick",
                        item -> grid.setDetailsVisible(item,
                                !grid.isDetailsVisible(item)));
    }

    private static ComponentRenderer<JobDetailsFormLayout, JobDTO> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(JobDetailsFormLayout::new,
                JobDetailsFormLayout::setJob);
    }


    private static class JobDetailsFormLayout extends FormLayout {
        private final TextField exeid = new TextField("Exe Id");
        private final TextField name = new TextField("Name");
        private final TextField parameter = new TextField("Paras");
        private final TextField started = new TextField("Start Time");
        private final TextField ended = new TextField("End Time");
        private final TextField status = new TextField("Status");

        public JobDetailsFormLayout() {
            Stream.of(exeid, name, parameter, started, ended, status).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });

            setResponsiveSteps(new ResponsiveStep("0", 3));
            setColspan(parameter, 3);
            setColspan(started, 3);
            setColspan(ended, 3);
        }

        public void setJob(JobDTO job) {
            exeid.setValue(""+job.getId());
            name.setValue(job.getName());
            parameter.setValue(job.getParameters());
            started.setValue(job.getStarted().toString());
            ended.setValue(job.getEnded().toString());
            status.setValue(job.getStatus());
        }
    }

    private void openPopup(JobDTO item) {
        Dialog dialog = new Dialog();

        // Add content to the dialog
        VerticalLayout layout = new VerticalLayout();
        layout.add(new Span("Exe Id: " + item.getId()));
        layout.add(new Span("Name: "+item.getName()));
        layout.add(new Span("Parameters: "+item.getParameters()));
        layout.add(new Span("Started: "+(item.getStarted()==null?"":item.getStarted().toString())));
        layout.add(new Span("Ended: "+(item.getEnded()==null?"":item.getEnded().toString())));
        layout.add(new Span("Status: "+item.getStatus()));

        // Add a Close button
        Button closeButton = new Button("Close", e -> dialog.close());
        layout.add(closeButton);

        // Add the layout to the dialog and open it
        dialog.add(layout);
        dialog.open();
    }

}
