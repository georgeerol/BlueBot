package com.bluebot.injection;

import android.app.Activity;

import com.bluebot.droid.ide.BlueBotIDEActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clifton Craig on 4/12/17.
 * Copyright GE 4/12/17
 */

public class Injector {
    static Map<Class,Class>moduleMap = new HashMap<Class,Class>(){{
        put(BlueBotIDEActivity.class, BlueBotIDEActivityModule.class);
    }};
    public static void injectEach(Activity activity) {
        if(moduleMap.containsKey(activity.getClass())) {
            InjectionModule module = inflate(moduleMap.get(activity.getClass()));
            module.inject(activity);
        }
    }

    private static InjectionModule inflate(Class aClass) {
        Object instance = null;
        try {
            instance = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (InjectionModule) instance;
    }
}
