package com.skillw.asahi.internal.util;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import taboolib.common.env.RuntimeDependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RuntimeDependency(value = "com.esotericsoftware:reflectasm:1.11.9", relocate = {"!com.esotericsoftware.reflectasm", "com.skillw.asahi.reflectasm"})
public class AsahiClassBean {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final ConcurrentHashMap<String, AsahiClassBean> cache = new ConcurrentHashMap<>();
    private final Class<?> clazz;
    private final MethodAccess methodAccess;
    private final FieldAccess fieldAccess;
    private final HashMap<String, Integer> getter = new HashMap<>();
    private final HashMap<String, Integer> singleParam = new HashMap<>();
    private final List<String> info = new ArrayList<>();

    private AsahiClassBean(Class<?> clazz) {
        this.clazz = clazz;
        this.info.add(clazz.getName() + " Bean Info:");
        this.methodAccess = MethodAccess.get(clazz);
        this.fieldAccess = FieldAccess.get(clazz);
        for (int index = 0; index < this.methodAccess.getParameterTypes().length; index++) {
            Class<?>[] types = this.methodAccess.getParameterTypes()[index];
            String name = this.methodAccess.getMethodNames()[index];
            if (types.length == 0) {
                int finalIndex = index;
                this.ifBeanReplaced(name, GET_PREFIX, (bean) -> this.getter.put(bean, finalIndex));
                this.ifBean(name, IS_PREFIX, (bean) -> this.getter.put(bean, finalIndex));
            }
            if (types.length == 1) {
                int finalIndex = index;
                if (this.ifBeanReplaced(name, SET_PREFIX, (bean) -> this.singleParam.put(bean, finalIndex))) {
                    continue;
                }
                this.singleParam.put(name, index);
            }
        }
    }

    public static AsahiClassBean of(Class<?> clazz) {
        String name = clazz.getName();
        if (cache.containsKey(name)) {
            return cache.get(name);
        } else {
            AsahiClassBean bean = new AsahiClassBean(clazz);
            cache.put(name, bean);
            return bean;
        }
    }

    public static List<String> info() {
        List<String> info = new ArrayList<>();
        cache.values().forEach((it) -> info.addAll(it.info));
        return info;
    }

    private boolean ifBeanReplaced(String name, String prefix, Consumer<String> consumer) {
        if (!name.startsWith(prefix)) {
            return false;
        }
        String bean = name.substring(prefix.length());
        bean = Character.toLowerCase(bean.charAt(0)) + bean.substring(1);
        this.info.add(prefix + "  :  " + name + "  ->  " + bean);
        consumer.accept(bean);
        return true;
    }

    private boolean ifBean(String name, String prefix, Consumer<String> consumer) {
        if (!name.startsWith(prefix)) {
            return false;
        }
        consumer.accept(name);
        return true;
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public Object get(Object object, String name) {
        if (this.getter.containsKey(name)) {
            return this.methodAccess.invoke(object, this.getter.get(name));
        }
        return this.fieldAccess.get(object, name);
    }

    public void set(Object object, String name, Object value) {
        if (this.singleParam.containsKey(name)) {
            this.methodAccess.invoke(object, this.singleParam.get(name), value);
            return;
        }
        this.fieldAccess.set(object, name, value);
    }

    public Object invoke(Object object, String methodName, Class<?>[] paramTypes, Object... args) {
        return this.methodAccess.invoke(object, methodName, paramTypes, args);
    }

    public Object invoke(Object object, String methodName, Object... args) {
        return this.methodAccess.invoke(object, methodName, args);
    }

    public Object invokeSingle(Object object, String methodName, Object param1) {
        if (this.singleParam.containsKey(methodName)) {
            return this.methodAccess.invoke(object, this.singleParam.get(methodName), param1);
        }
        return this.invoke(object, methodName, param1);
    }

    public List<String> getInfo() {
        return this.info;
    }

}
