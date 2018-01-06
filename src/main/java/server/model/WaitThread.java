package server.model;

import static java.lang.Thread.sleep;
import server.controller.Controller;

public class WaitThread extends Thread {

    Controller controller;
    private boolean running = true;

    public WaitThread(Controller controller) {
        this.controller = controller;
    }

    public void terminate() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            if (controller.getNrofplayers() > 0) {
                controller.broadmsg("Waiting for " + (3 - controller.getNrofplayers()) + " players");
            }
            try {
                sleep(3000);
            } catch (InterruptedException ex) {
            }
        }
    }
}
