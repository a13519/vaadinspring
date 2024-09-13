package net.zousys.gba.security;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.auth.NavigationAccessControl;
import net.zousys.gba.views.login.LoginView;

public class NavigationAccessCheckerInitializer implements VaadinServiceInitListener {

    private NavigationAccessControl accessControl;

    public NavigationAccessCheckerInitializer() {
        accessControl = new NavigationAccessControl();
        accessControl.setLoginView(LoginView.class);
    }

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            uiInitEvent.getUI().addBeforeEnterListener(accessControl);
        });
    }
}