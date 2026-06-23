package com.chen1335.immersiveMechanical.util;

import org.apache.commons.lang3.mutable.MutableObject;

import java.util.function.Supplier;

public class MutableObjectSupplier<T> extends MutableObject<T> implements Supplier<T> {
    public MutableObjectSupplier(T object){
        setValue(object);
    }
    @Override
    public T get() {
        return getValue();
    }
}
