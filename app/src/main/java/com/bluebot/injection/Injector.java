package com.bluebot.injection;

import android.app.Activity;
import android.content.Context;

import com.bluebot.MainActivity;
import com.bluebot.droid.ide.BlueBotIDEActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Clifton Craig on 4/12/17.
 * Copyright GE 4/12/17
 */

public class Injector {
    private static final SharedComponents share = new InjectorSharedComponents();

    private static final Map<Class,Class>moduleMap = new HashMap<Class,Class>(){{
        put(BlueBotIDEActivity.class, BlueBotIDEActivityModule.class);
        put(MainActivity.class, MainActivityModule.class);
    }};
    public static void injectEach(Activity activity) {
        if(share.doesNotHave(Context.class)) {
            share.put(Context.class, activity.getApplicationContext());
        }
        if(moduleMap.containsKey(activity.getClass())) {
            InjectionModule module = inflate(moduleMap.get(activity.getClass()));
            module.setSharedComponents(share);
            module.inject(activity);
        }
    }

    private static InjectionModule inflate(Class aClass) {
        Object instance = null;
        try {
            instance = aClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Cannot instantiate " + aClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access " + aClass, e);
        }
        return (InjectionModule) instance;
    }

}
