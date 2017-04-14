package com.bluebot.injection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clifton Craig on 4/13/17.
 * Copyright GE 4/13/17
 */
class InjectorSharedComponents implements SharedComponents {
    private Map<Class, Object> internalShare = new HashMap<>();

    @Override
    public boolean doesNotHave(Class aClass) {
        return ! internalShare.containsKey(aClass);
    }

    @Override
    public <T> T get(Class<T> aClass) {
        return (T) internalShare.get(aClass);
    }

    @Override
    public <T> void put(Class<T> aClass, T instance) {
        internalShare.put(aClass, instance);
    }
}
