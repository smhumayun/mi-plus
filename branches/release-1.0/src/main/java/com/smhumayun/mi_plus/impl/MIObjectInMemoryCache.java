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
package com.smhumayun.mi_plus.impl;

import com.smhumayun.mi_plus.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * HashMap based In-Memory cache implementation of {@link com.smhumayun.mi_plus.MIObjectCache}
 *
 * @author smhumayun
 * @since 1.0
 */
public final class MIObjectInMemoryCache implements MIObjectCache {

    /**
     * Internal cache to hold singleton MI Container Proxy objects against an MI Container class
     */
    private final HashMap<Class, Object> containerProxySingletonCache = new HashMap<Class, Object>();

    /**
     * Internal cache to hold singleton Composed objects against an MI Container class
     */
    private final HashMap<Class, LinkedHashMap<Class, Object>> composedSingletonCache = new HashMap<Class, LinkedHashMap<Class, Object>>();

    /**
     * Internal cache to hold validated classes
     */
    private final HashSet<Class> validatedClassesCache = new HashSet<Class>();

    /**
     * Get cached MI Container Proxy object against given <code>miContainerClass</code>
     *
     * @param miContainerClass MI Container class
     * @return cached MI Container Proxy object against given <code>miContainerClass</code>
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    public Object getCachedMiContainerProxy(Class miContainerClass) throws MIException {
        return containerProxySingletonCache.get(miContainerClass);
    }

    /**
     * Cache given <code>miContainerProxy</code> object against given <code>miContainerClass</code>
     *
     * @param miContainerClass MI Container class
     * @param miContainerProxyObj      MI Container Proxy object to cache
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    public void cacheMiContainerProxy(Class miContainerClass, Object miContainerProxyObj) throws MIException {
        containerProxySingletonCache.put(miContainerClass, miContainerProxyObj);
    }

    /**
     * Get cached Composed Objects against given <code>miContainerClass</code> - access underlying cache in a
     * synchronized, multi thread-safe way
     *
     * @param miContainerClass MI Container class
     * @return cached Composed Objects against given <code>miContainerClass</code>
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    public LinkedHashMap<Class, Object> getCachedComposedObjects(Class miContainerClass) throws MIException {
        return composedSingletonCache.get(miContainerClass);
    }

    /**
     * Cache given <code>composedObjects</code> against given <code>miContainerClass</code> - access underlying cache
     * in a synchronized, multi thread-safe way
     *
     * @param miContainerClass MI Container class
     * @param composedObjects  composed objects to cache
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    public void cacheComposedObjects(Class miContainerClass, LinkedHashMap<Class, Object> composedObjects) throws MIException {
        composedSingletonCache.put(miContainerClass, composedObjects);
    }

    /**
     * Returns true of the <code>classToValidate</code> has already been validated
     *
     * @param classToValidate class to
     * @return true if validates, false otherwise
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    @Override
    public boolean isValidated(Class classToValidate) throws MIException {
        return validatedClassesCache.contains(classToValidate);
    }

    /**
     * Cache this <code>validatedClass</code> and mark it as validated
     *
     * @param validatedClass validated class to cache
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    @Override
    public void validated(Class validatedClass) throws MIException {
        validatedClassesCache.add(validatedClass);
    }

}
