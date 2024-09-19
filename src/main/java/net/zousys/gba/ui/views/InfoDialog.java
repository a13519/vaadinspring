package net.zousys.gba.ui.views;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;

/**
 *
 */
public class InfoDialog {
    public static void openInfo(String name, String desc) {
        Span status = new Span();
        status.setVisible(false);

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(name);
        dialog.setText(new Html(desc));

        dialog.setConfirmText("Close");
        dialog.open();
    }

    public static void openConfirmation(String title, String desc,
    String cancel, String ok, String discard, ComponentEventListener<ConfirmDialog.ConfirmEvent> listener){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(title);
        dialog.setText(desc);

        if (cancel!=null) {
            dialog.setCancelable(true);
            dialog.setCancelText(cancel);
        }
        if (discard!=null) {
            dialog.setRejectable(true);
            dialog.setRejectText(discard);
        }
        if (ok!=null) {
            dialog.setConfirmText(ok);
            dialog.addConfirmListener(listener);
        }
        dialog.open();
    }
}
