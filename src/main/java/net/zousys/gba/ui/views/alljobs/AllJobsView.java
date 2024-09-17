package net.zousys.gba.ui.views.alljobs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import net.zousys.gba.ui.MainLayout;
@PermitAll
@PageTitle("All Jobs")
@Menu(icon = "line-awesome/svg/angle-double-down-solid.svg", order = 0)
@Route(value = "all-jobs", layout = MainLayout.class)
public class AllJobsView extends Composite<VerticalLayout> {

    public AllJobsView() {
        Tabs tabs = new Tabs();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabs.setWidth("100%");
        setTabsSampleData(tabs);
        getContent().add(tabs);
    }

    private void setTabsSampleData(Tabs tabs) {
        tabs.add(new Tab("Dashboard"));
        tabs.add(new Tab("Payment"));
        tabs.add(new Tab("Shipping"));
    }
}
