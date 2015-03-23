
/*
 * Copyright 2015 Tyler Davis
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.ngandroid.lib.binder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.ngandroid.lib.R;
import com.ngandroid.lib.exceptions.NgException;

import java.lang.reflect.Field;

/**
 * Created by davityle on 1/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BindingInflaterFactory implements LayoutInflater.Factory2 {

    private final LayoutInflater.Factory2 mFactory2;
    private SparseArray<TypedArray> mAttrArray;


    private BindingInflaterFactory(LayoutInflater.Factory2 factory2, SparseArray<TypedArray> attrArray) {
        this.mFactory2 = factory2;
        this.mAttrArray = attrArray;
    }

    @Override
    public View onCreateView(String s, Context context, AttributeSet attributeSet) {
        parseAttributes(context, attributeSet);
        if(mFactory2 == null){
            return null;
        }
        return mFactory2.onCreateView(s, context, attributeSet);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
        parseAttributes(context, attributeSet);
        if(mFactory2 == null){
            return null;
        }
        return mFactory2.onCreateView(view,s,context, attributeSet);
    }

    private void parseAttributes(Context context, AttributeSet attributeSet){
        String idValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "id");
        if(idValue != null) {
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.ngAndroid);
            if(array.getIndexCount() > 0) {
                int id = Integer.parseInt(idValue.replace("@", ""));
                mAttrArray.put(id, array);
            }
        }
    }

    private static void setSettable(LayoutInflater inflater){
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(inflater, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new NgException("Unable to access mFactorySet in LayoutInflater. Please submit issue on at github.com/davityle/ngAndroid/issues");
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    static void setFactory(LayoutInflater inflater,final  SparseArray<TypedArray> attrArray){
        final LayoutInflater.Factory2 factory2 = inflater.getFactory2();
        if(factory2 instanceof BindingInflaterFactory){
            BindingInflaterFactory inflaterFactory = (BindingInflaterFactory) factory2;
            inflaterFactory.mAttrArray = attrArray;
        }else {
            setSettable(inflater);
            inflater.setFactory2(new BindingInflaterFactory(factory2, attrArray));
        }
    }
}