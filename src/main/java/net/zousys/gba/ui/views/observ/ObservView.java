package net.zousys.gba.ui.views.observ;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import net.zousys.gba.function.observable.dto.ObservDTO;
import net.zousys.gba.function.observable.service.ObservService;
import net.zousys.gba.ui.MainLayout;
import net.zousys.gba.ui.views.LogPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@PageTitle("Observables View")
@Menu(icon = "line-awesome/svg/play-circle-solid.svg", order = 1)
@Route(value = "observables", layout = MainLayout.class)
public class ObservView extends Composite<VerticalLayout> {
    public static final String JOBNAME="BatchJob_A";
    @Autowired()
    private ObservService observService;
    private int page = 0;
    private final int size = 16;
    private Grid<ObservDTO> stripedGrid;
    private Sort sort = Sort.by("id").descending();
    private Text h6;
    private HorizontalLayout rowLayout;
    /**
     *
     */
    public ObservView() {
        Tabs tabs = new Tabs();
        Button refreshButton = new Button("Refresh", e -> navigatePage(-0));
        Button prevButton = new Button("Previous", e -> navigatePage(-1));
        h6 = new Text("Page 0");
        Div texth6 = new Div(h6);
        texth6.setWidth("150px");
        Button nextButton = new Button("Next", e -> navigatePage(1));
        HorizontalLayout paginationLayout = new HorizontalLayout(refreshButton, prevButton, nextButton);
        paginationLayout.setWidthFull();  // Set layout to full width
        paginationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);  // Align to the right

        rowLayout = new HorizontalLayout(texth6, paginationLayout);
        rowLayout.setWidthFull();
        rowLayout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);

        stripedGrid = new Grid(ObservDTO.class, false);
        stripedGrid.addColumn("id").setHeader("Exe Id").setSortable(true).setWidth("6rem");
        stripedGrid.addColumn("name").setWidth("9rem");
        stripedGrid.addColumn("nativeId").setHeader("Native Id").setSortable(true).setWidth("6rem");
        stripedGrid.addColumn("parameters").setWidth("18rem");
        stripedGrid.addColumn("label").setWidth("6rem");
        stripedGrid.addColumn("branch").setWidth("8rem");
//        stripedGrid.addColumn("started").setSortable(true);
        stripedGrid.addColumn(new LocalDateTimeRenderer<>(
                        ObservDTO::getStarted, () -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)))
                .setHeader("Started").setWidth("12rem");
        stripedGrid.addColumn(new LocalDateTimeRenderer<>(
                        ObservDTO::getEnded, () -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT,                         FormatStyle.MEDIUM)))
                .setHeader("Ended").setWidth("12rem");
        stripedGrid.addColumn("status").setSortable(true).setWidth("8rem");

        stripedGrid.addColumn(new ComponentRenderer<>(item -> {
            Button button = new Button(VaadinIcon.GLASSES.create());
            button.addClickListener(event -> {
                LogPanel lp = new LogPanel(item.getLog());
                lp.show();
            });  // Ope
            return button;
        })).setHeader("Log");

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

        setGridData(stripedGrid);

        getContent().add(tabs);
        getContent().add(stripedGrid, rowLayout);

//        loadPage(page);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("History"));
//        tabs.add(new Tab("Run A Job"));
    }

    private void setGridData(Grid grid) {

        // Set up the data provider for pagination
        DataProvider<ObservDTO, Void> dataProvider = DataProvider.fromCallbacks(
                // Fetch data based on page size and offset
                query -> {
                    int offset = query.getOffset();  // Starting point (row number)
                    int limit = query.getLimit();    // Number of items to fetch (page size)
                    h6.setText("Page "+offset/limit);
                    page = offset/limit;
                    return observService.retrieveObservs(JOBNAME).stream();
                },
                // Count the total number of entities (for grid sizing)
                query -> observService.countEntities(JOBNAME)
        );

        grid.setDataProvider(dataProvider);
    }

    private void loadPage(int pageNumber) {
        List<ObservDTO> entities = observService.retrieveObservs(JOBNAME);
        stripedGrid.setItems(entities);
    }

    private void navigatePage(int direction) {
        page += direction;
        page = Math.max(0, page);
        h6.setText("Page "+page);
        loadPage(page);
    }

    private static Renderer<ObservDTO> createToggleDetailsRenderer(
            Grid<ObservDTO> grid) {
        return LitRenderer.<ObservDTO> of(
                        "<vaadin-button theme=\"tertiary\" @click=\"${handleClick}\">Toggle details</vaadin-button>")
                .withFunction("handleClick",
                        item -> grid.setDetailsVisible(item,
                                !grid.isDetailsVisible(item)));
    }

    private static ComponentRenderer<JobDetailsFormLayout, ObservDTO> createPersonDetailsRenderer() {
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

        public void setJob(ObservDTO job) {
            exeid.setValue(""+job.getId());
            name.setValue(job.getName());
            parameter.setValue(job.getParameters());
            started.setValue(job.getStarted().toString());
            ended.setValue(job.getEnded().toString());
            status.setValue(job.getStatus());
        }
    }

    private void openPopup(ObservDTO item) {
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
//        rowLayout.add(new Span("Waiting for updates"));
        // Start the data feed thread
        FeederThread thread = new FeederThread(attachEvent.getUI(), this);
        thread.start();
    }


    private static class FeederThread extends Thread {
        private final UI ui;
        private final ObservView view;

        private int count = 0;

        public FeederThread(UI ui, ObservView view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            try {

                // Update the data for a while
                while (count < 10) {
//                    System.out.println("RRRRR");
                    // Sleep to emulate background work
                    Thread.sleep(30000);
                    String message = "This is update " + count++;

                    ui.access(() -> view.loadPage(view.page));
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
