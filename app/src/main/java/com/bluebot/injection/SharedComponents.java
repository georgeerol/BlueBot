package com.bluebot.injection;

/**
 * Created by Clifton Craig on 4/13/17.
 * Copyright GE 4/13/17
 */

interface SharedComponents {
    boolean has(Class aClass);

    <T> void put(Class<T> aClass, T instance);

    <T> T get(Class<T> aClass);
}
