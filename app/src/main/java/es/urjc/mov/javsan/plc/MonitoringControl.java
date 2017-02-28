package es.urjc.mov.javsan.plc;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;


/*
 * This Class control de refresh to monitorize, the exit from monitorize tags..
 * and the close connection...
 */

public class MonitoringControl {
    private static final long INTERVAL = 1000; // wait time ms...

    private boolean exit;
    private InputStream datain;

    MonitoringControl() {
        exit = false;
        datain = null;
    }

    public synchronized boolean isExit() throws InterruptedException {
            wait(INTERVAL);
            return exit;
    }

    public synchronized void exit() {
        exit = true;
        notify();
    }

    public synchronized void setInput(InputStream in) {
        datain = in;
    }

    public synchronized InputStream getInput() {
        return datain;
    }

    public synchronized void close() {
        try {
            if (datain != null) {
                datain.close();
            }
        } catch (IOException e) {
            // Error closing socket...
        }
    }
}
