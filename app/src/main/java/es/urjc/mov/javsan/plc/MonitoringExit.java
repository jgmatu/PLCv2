package es.urjc.mov.javsan.plc;

/**
 * Created by javi on 28/02/17.
 */

public class MonitoringExit implements Runnable {
    MonitoringControl control;

    MonitoringExit(MonitoringControl c) {
        control = c;
    }

    @Override
    public void run() {
        exit();
    }

    private void exit() {
        control.close();
        control.exit();
    }
}
