package com.bluebot.ui;

/**
 * Created by Clifton Craig on 4/9/17.
 * Copyright GE 4/9/17
 */

public interface RequestProcessor {
    void bind(ResponseHandler responseHandler);

    void processRequest(String requestType, Object data);
}
