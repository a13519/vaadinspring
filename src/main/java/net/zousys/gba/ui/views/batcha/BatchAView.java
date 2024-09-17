package net.zousys.gba.ui.views.batcha;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import net.zousys.gba.ui.MainLayout;
@PermitAll
@PageTitle("Batch A")
@Menu(icon = "line-awesome/svg/play-circle-solid.svg", order = 1)
@Route(value = "batcha", layout = MainLayout.class)
public class BatchAView extends Composite<VerticalLayout> {

    public BatchAView() {
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
