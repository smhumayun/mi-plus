/**
 * Project MI+
 *
 * Copyright (c) 2013, Syed Muhammad Humayun - smhumayun@gmail.com
 *
 * This project includes software developed by Syed Muhammad Humayun - smhumayun@gmail.com
 * http://www.smhumayun.com
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.smhumayun.mi_plus;

import com.smhumayun.mi_plus.util.Pair;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * Method Resolver to be used for MI Method Resolution
 *
 * @author smhumayun
 * @since 1.0
 */
public interface MIMethodResolver {

    /**
     * Resolve method invoked method to target method and object
     *
     * @param miContainerClass      MI Container class - the class which has the {@link MISupport} annotation
     * @param composedObjects       map of composed objects
     * @param methodCall            method call made on Proxy
     * @param methodCallArgs        method call arguments
     * @return resolved target method and object
     * @throws MIException {@link MIException}
     */
    Pair<Object, Method> resolve (Class miContainerClass, LinkedHashMap<Class, Object> composedObjects, Method methodCall
            , Object[] methodCallArgs) throws MIException;

}
