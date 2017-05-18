package com.bluebot.ui;

import com.bluebot.runtime.runner.BlueCodeRunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Clifton Craig on 4/11/17.
 * Copyright GE 4/11/17
 */

public class CodeRunnerRequestProcessor implements RequestProcessor, Runnable {
    private final Queue<Object> requestData = new LinkedList<>();
    private final BlueCodeRunner blueCodeRunner;
    private boolean runIndefinitely;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private ResponseHandler responseHandler;

    public CodeRunnerRequestProcessor(BlueCodeRunner blueCodeRunner) {
        this.blueCodeRunner = blueCodeRunner;
    }

    @Override
    public void bind(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
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
            try { blueCodeRunner.run(String.valueOf(data)); }
            catch (final IllegalArgumentException e) {
                final HashMap<String, Object> response = new HashMap<String, Object>() {{
                    put(UIResponseType.STATUS, UIResponseType.ERROR);
                    put(UIResponseType.DETAIL, e);
                }};
                respondWith(response);
                continue;
            }
            respondWith(new HashMap<String, Object>() {{
                put(UIResponseType.STATUS, UIResponseType.SUCCESS);
            }});
        }
    }

    private void respondWith(HashMap<String, Object> responseData) {
        if (responseHandler!=null) { responseHandler.responseForRequest(UIRequestTypes.EXECUTE_CODE, responseData); }
    }

    public void setRunIndefinitely(boolean runIndefinitely) {
        this.runIndefinitely = runIndefinitely;
        if (runIndefinitely) {
            executor.execute(this);
        }
    }
}
