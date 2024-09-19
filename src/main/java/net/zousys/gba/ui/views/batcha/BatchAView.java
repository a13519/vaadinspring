package net.zousys.gba.ui.views.batcha;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import net.zousys.gba.function.batch.dto.JobDTO;
import net.zousys.gba.function.batch.service.BatchService;
import net.zousys.gba.ui.MainLayout;
import net.zousys.gba.ui.views.InfoDialog;
import net.zousys.gba.ui.views.alljobs.AllJobsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

@PermitAll
@PageTitle("Batch A")
@Menu(icon = "line-awesome/svg/play-circle-solid.svg", order = 1)
@Route(value = "batcha", layout = MainLayout.class)
public class BatchAView extends Composite<VerticalLayout> {
    public static final String JOBNAME="BatchJob_A";
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
    public BatchAView() {
        Tabs tabs = new Tabs();
        Button refreshButton = new Button("Refresh", e -> navigatePage(-0));
        Button prevButton = new Button("Previous", e -> navigatePage(-1));
        h6 = new Text("Page 0");
        Div textContainer = new Div(h6);
        textContainer.setWidth("150px");
        Button runButton = new Button("Run", e -> openRunDialog());
        Button nextButton = new Button("Next", e -> navigatePage(1));
        HorizontalLayout paginationLayout = new HorizontalLayout(runButton, refreshButton, prevButton, nextButton);
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
        startAutoRefresh();
//        loadPage(page);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("History"));
//        tabs.add(new Tab("Run A Job"));
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
                    return batchService.listByName(JOBNAME, PageRequest.of(offset/limit, limit, sort)).stream();
                },
                // Count the total number of entities (for grid sizing)
                query -> batchService.countEntities(JOBNAME)
        );

        grid.setDataProvider(dataProvider);
    }

    private void loadPage(int pageNumber) {
        List<JobDTO> entities = batchService.listByName(JOBNAME, PageRequest.of(pageNumber, size, sort));
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

    private void openRunDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("BatchJob_A");
        dialog.getHeader().add(new Button(new Icon("lumo", "cross"), (e) -> dialog.close()));

        VerticalLayout dialogLayout = new VerticalLayout();
        dialog.add(dialogLayout);

        Select<String> envs = new Select<>();
        envs.setLabel("Environment");
        envs.setItems("Env 1", "Env 2", "Env 3", "Env 4");
        envs.setValue("choose env");

        Select<String> directory = new Select<>();
        directory.setLabel("Directory");
        directory.setItems("LDAP", "DB", "Redis");
        directory.setValue("choose directory");

        DateTimePicker dateTimePicker = new DateTimePicker();
        dateTimePicker.setLabel("Schedule date and time");
        dateTimePicker.setStep(Duration.ofMinutes(30));
        dateTimePicker.setValue(LocalDateTime.now());
        dateTimePicker.setHelperText("Now? Leave it as is");
        dateTimePicker.setDatePlaceholder("Date");
        dateTimePicker.setTimePlaceholder("Time");

        Button cancel = new Button("Cancel");
        cancel.addClickListener(a-> dialog.close());

        Button runButton = new Button("Run");
        runButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(cancel, runButton);
        buttonLayout.setWidthFull();
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        dialogLayout.add(envs);
        dialogLayout.add(directory);
        dialogLayout.add(dateTimePicker);
        dialogLayout.add(buttonLayout);

        runButton.addClickListener(a->{
            run(envs.getValue(), directory.getValue(), dateTimePicker.getValue());
            dialog.close();
            navigatePage(-0);
        });
        dialog.open();
    }

    /**
     *
     */
    private void startAutoRefresh() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Use UI.access() to update the UI from a background thread
                if (UI.getCurrent()!=null) {
                    UI.getCurrent().access(() -> {
                        loadPage(page);
                        UI.getCurrent().push();  // Push updates to the client
                    });
                }
            }
        }, 0, 10000);  // Update every 5 seconds
    }

    /**
     *
     * @param env
     * @param dir
     * @param localDateTime
     */
    private void run(String env, String dir, LocalDateTime localDateTime) {
//        ProgressBar progressBar = new ProgressBar();
//        progressBar.setIndeterminate(true);

        int r = batchService.runJobA(env);
        if (r==0) {
            InfoDialog.openInfo("Info", "<div>The job is triggered</div>");
        } else if (r==1) {
            InfoDialog.openInfo("Info", "<div>The previous job has not finished</div>");
        }
    }
}
