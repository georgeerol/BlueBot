package com.bluebot.droid.ide.runtime;

/**
 * Created by Clifton Craig on 4/9/17.
 * Copyright GE 4/9/17
 */

public interface RequestProcessor {
    void processRequest(String requestType, Object data);
}
