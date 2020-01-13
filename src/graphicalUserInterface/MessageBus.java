package graphicalUserInterface;

import graphicalUserInterface.controllers.ApplicationWindowController;

public class MessageBus {
    static ApplicationWindowController window;

    public MessageBus(ApplicationWindowController a) {
        window = a;
    }

    public void write(String line) {
        window.write(line);
    }

    public void write(String line, String mode) {
        window.write(line, mode);
    }
}
