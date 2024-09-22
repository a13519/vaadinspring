package net.zousys.gba.ui.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class LogPanel extends Dialog{

    private String log;
    private File logFile;
    private TextArea logArea;
    private BufferedReader br;
    private Timer timer = new Timer();
    private UI currentUI;
    private Icon active;

    /**
     *
     */
    public LogPanel(String log) {
        super();
        currentUI = UI.getCurrent();

        this.log = "/var/log/system.log";
        logFile = new File(this.log);

        setHeaderTitle("Log");
        setWidth("950px");
        setResizable(true);
        setDraggable(true);
        addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        VerticalLayout dialogLayout = createDialogLayout();
        add(dialogLayout);

        Button cancelButton = new Button("Close", e -> {
            timer.cancel();
            close();
        });
        Button clearButton = new Button("Clear", e -> {
            logArea.clear();
        });

        active = VaadinIcon.EXIT.create();
        active.setColor("grey");
        HorizontalLayout paginationLayout = new HorizontalLayout(clearButton, cancelButton);
        paginationLayout.setWidthFull();  // Set layout to full width
        paginationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);  // Align to the right
        HorizontalLayout rowLayout = new HorizontalLayout(active, paginationLayout);
        rowLayout.setWidthFull();
        rowLayout.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);

        getFooter().add(rowLayout);
    }

    public void show() {
        // Start tailing the log file
        open();
        startLogTail();

    }

    /**
     * @return
     */
    private VerticalLayout createDialogLayout() {
        VerticalLayout r = new VerticalLayout();
        if (logFile.exists()) {
            try {
                br = new BufferedReader(new FileReader(logFile));
                logArea = new TextArea();
                logArea.setWidthFull();
                logArea.setHeight("500px");
                logArea.setReadOnly(true);  // Read-only, so the user cannot edit it
                logArea.getStyle().set("color", "#ffffff");
                logArea.getStyle().set("background-color", "#010101");
                logArea.getStyle().set("font-family", "monospace");
                logArea.getStyle().set("line-height","1.5");
                logArea.getStyle().set("margin-block-start", "2.6em !important");
                logArea.getStyle().set("margin-block-end", "0.5em !important");
                logArea.getStyle().set("font-size", "14px");
                add(logArea);
            } catch (IOException e) {
                e.printStackTrace();
                // todo
            }
        } else {
            add(new Text("Log is not exist."));
        }
        return r;
    }

    /**
     *
     */
    private void startLogTail() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentUI.access(() -> {
                    try {
                        active.setColor("white");
                        String newLine = null;
                        while ((newLine = br.readLine()) != null) {
                            if (!newLine.isEmpty()) {
                                logArea.setReadOnly(false);
                                logArea.setValue(logArea.getValue() + newLine);  // Append new log lines
                                logArea.scrollToEnd();
                                logArea.setReadOnly(true);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    active.setColor("grey");
                    currentUI.push();  // Push updates to the client
                });
            }
        }, 0, 3000);  // Check for new log lines every second
    }


}

