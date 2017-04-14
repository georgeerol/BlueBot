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
    private static final SharedComponents share = new SharedComponents(){
        private Map<Class,Object> internalShare = new HashMap<>();
        @Override
        public boolean has(Class aClass) {
            return internalShare.containsKey(aClass);
        }

        @Override
        public <T> T get(Class<T> aClass) {
            return (T) internalShare.get(aClass);
        }

        @Override
        public <T> void put(Class<T> aClass, T instance) {
            internalShare.put(aClass, instance);
        }
    };

    static Map<Class,Class>moduleMap = new HashMap<Class,Class>(){{
        put(BlueBotIDEActivity.class, BlueBotIDEActivityModule.class);
        put(MainActivity.class, MainActivityModule.class);
    }};
    public static void injectEach(Activity activity) {
        if(! share.has(Context.class)) {
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
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (InjectionModule) instance;
    }
}
