package com.bluebot.droid.ide;

import java.util.Map;

/**
 * Created by Clifton Craig on 4/9/17.
 * Copyright GE 4/9/17
 */

public interface ResponseHandler {
    void responseForRequest(String requestType, Map<String, String> response);
}
