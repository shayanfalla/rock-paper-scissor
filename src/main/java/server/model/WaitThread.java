/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import static java.lang.Thread.sleep;
import server.controller.Controller;

public class WaitThread extends Thread {

    Controller controller;
    private volatile boolean running = true;

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
