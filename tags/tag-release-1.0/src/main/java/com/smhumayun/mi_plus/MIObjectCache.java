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

import java.util.LinkedHashMap;

/**
 * MI Object Cache used by {@link MIFactory}
 *
 * @author smhumayun
 * @since 1.0
 */
public interface MIObjectCache {

    /**
     * Get cached MI Container Proxy object against given <code>miContainerClass</code>
     *
     * @param miContainerClass MI Container class - the class which has the {@link MISupport} annotation
     * @return cached MI Container Proxy object against given <code>miContainerClass</code>
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    Object getCachedMiContainerProxy(Class miContainerClass) throws MIException;

    /**
     * Cache given <code>miContainerProxy</code> object against given <code>miContainerClass</code>
     *
     * @param miContainerClass MI Container class - the class which has the {@link MISupport} annotation
     * @param miContainerProxy      MI Container Proxy object to cache
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    void cacheMiContainerProxy(Class miContainerClass, Object miContainerProxy) throws MIException;

    /**
     * Get cached Composed Objects against given <code>miContainerClass</code>
     *
     * @param miContainerClass MI Container class - the class which has the {@link MISupport} annotation
     * @return cached Composed Objects against given <code>miContainerClass</code>
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    LinkedHashMap<Class, Object> getCachedComposedObjects (Class miContainerClass) throws MIException;

    /**
     * Cache given <code>composedObjects</code> against given <code>miContainerClass</code>
     *
     * @param miContainerClass MI Container class - the class which has the {@link MISupport} annotation
     * @param composedObjects  composed objects to cache
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    void cacheComposedObjects (Class miContainerClass, LinkedHashMap<Class, Object> composedObjects) throws MIException;

    /**
     * Returns true of the <code>classToValidate</code> has already been validated
     *
     * @param classToValidate class to
     * @return true if validates, false otherwise
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    boolean isValidated (Class classToValidate) throws MIException;

    /**
     * Cache this <code>validatedClass</code> and mark it as validated
     *
     * @param validatedClass validated class to cache
     * @throws com.smhumayun.mi_plus.MIException
     *          {@link com.smhumayun.mi_plus.MIException}
     */
    void validated (Class validatedClass) throws MIException;
}
