package net.zousys.gba;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Push
@PWA(name = "VaadinAir App", shortName = "App", offlinePath = "offline.html")
@Theme(value = "my-app", variant = Lumo.DARK)
@Viewport("width=device-width, initial-scale=1.0")
@Meta(name = "description", content = "This is my awesome Vaadin application")
@Meta(name = "keywords", content = "vaadin, java, web app")
public class AppShell implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        // Set favicon
//        settings.addFavIcon("icon", "icons/favicon.ico", "32x32");

        // Add other configurations here if needed
    }
}