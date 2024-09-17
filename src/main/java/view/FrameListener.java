package view;

import view.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameListener extends WindowAdapter {
    private final View view;

    public FrameListener(View view) {
        this.view = view;
    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        view.exit();
    }
}
