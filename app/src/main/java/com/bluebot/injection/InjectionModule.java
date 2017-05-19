package com.bluebot.injection;

/**
 * Created by Clifton Craig on 4/12/17.
 * Copyright GE 4/12/17
 */

interface InjectionModule {
    void inject(Object object);

    void setSharedComponents(SharedComponents sharedComponents);
}
