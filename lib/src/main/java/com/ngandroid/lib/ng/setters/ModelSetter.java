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

package com.ngandroid.lib.ng.setters;

import com.ngandroid.lib.ng.MethodInvoker;

/**
 * Created by davityle on 1/24/15.
 */
public class ModelSetter implements Setter {

    private final String mFieldName;
    private final MethodInvoker mMethodInvoker;

    public ModelSetter(String mFieldName, MethodInvoker mMethodInvoker) {
        this.mFieldName = mFieldName;
        this.mMethodInvoker = mMethodInvoker;
    }

    public void set(Object ... parameters) throws Throwable {
        mMethodInvoker.invoke("set" + mFieldName, parameters);
    }
}
