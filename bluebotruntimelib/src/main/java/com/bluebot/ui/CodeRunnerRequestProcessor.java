package com.bluebot.ui;

import com.bluebot.runtime.runner.BlueCodeRunner;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Clifton Craig on 4/11/17.
 * Copyright GE 4/11/17
 */

class CodeRunnerRequestProcessor implements RequestProcessor, Runnable {
    private Queue requestData = new LinkedList();
    private BlueCodeRunner blueCodeRunner;
    private boolean runIndefinitely;
    private Thread workerThread;

    public CodeRunnerRequestProcessor(BlueCodeRunner blueCodeRunner) {
        this.blueCodeRunner = blueCodeRunner;
    }

    @Override
    public void bind(ResponseHandler responseHandler) {

    }

    @Override
    public void processRequest(String requestType, Object data) {
        synchronized (requestData) {
            this.requestData.add(data);
        }
    }

    @Override
    public void run() {
        processRequestDataQueue();
        while (runIndefinitely) {
            processRequestDataQueue();
            try { Thread.sleep(100); }
            catch (InterruptedException e) { return; }
        }
    }

    private void processRequestDataQueue() {
        while(! requestData.isEmpty()) {
            final Object data;
            synchronized (requestData) { data = requestData.remove(); }
            blueCodeRunner.run(String.valueOf(data));
        }
    }

    public void setRunIndefinitely(boolean runIndefinitely) {
        this.runIndefinitely = runIndefinitely;
        workerThread = new Thread(this);
        workerThread.setDaemon(true);
        workerThread.start();
    }
}
