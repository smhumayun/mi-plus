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
import com.smhumayun.mi_plus.util.Pair;
import com.smhumayun.mi_plus.util.Utils;
import org.apache.commons.beanutils.MethodUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

/**
 * {@link com.smhumayun.mi_plus.MIMethodResolver} implementation which resolves method based on the order of
 * {@link com.smhumayun.mi_plus.MISupport#parentClasses()} and method resolution strategy of Apache
 * Commons {@link MethodUtils#getMatchingAccessibleMethod(Class, String, Class[])}
 *
 * @author smhumayun
 * @since 1.0
 */
public final class MIMethodResolverImpl implements MIMethodResolver {

    /**
     * Logger {@link Logger}
     */
    private Logger logger = Logger.getLogger(MIMethodResolverImpl.class.getName());

    /**
     * Utils
     */
    private Utils utils = new Utils();

    /**
     * Resolve method based on following strategy:
     * - Iterate over composed objects (order will be the same as defined in {@link com.smhumayun.mi_plus.MISupport}
     * - For each composed object, check if there's a matching 'accessible' method based on the algorithm defined by
     * {@link MethodUtils#getAccessibleMethod(Class, String, Class[])} i.e. it finds an accessible method that matches
     * the given name and has compatible parameters. Compatible parameters mean that every method parameter is
     * assignable from the given parameters. In other words, it finds a method with the given name that will take the
     * parameters given.
     * - If a match is found, break the iteration and exit from the loop;
     * - Return the corresponding composed object and the matched method
     *
     * @param miContainerClass      MI Container class
     * @param composedObjects       map of composed objects
     * @param methodCall            method call made on Proxy
     * @param methodCallArgs        method call arguments
     * @return resolved target method and object
     * @throws com.smhumayun.mi_plus.MIException {@link com.smhumayun.mi_plus.MIException}
     */
    public Pair<Object, Method> resolve (Class miContainerClass, LinkedHashMap<Class, Object> composedObjects, Method methodCall
            , Object[] methodCallArgs) throws MIException {
        logger.info("resolve " + methodCall.toGenericString());
        Class[] methodCallArgsClasses = utils.getClasses(methodCallArgs);
        logger.fine("methodCallArgs classes = " + Arrays.toString(methodCallArgsClasses));
        Object targetObject = null;
        Method targetMethod = null;
        for (Class composedObjectClass : composedObjects.keySet()) {
            try
            {
                targetMethod = MethodUtils.getMatchingAccessibleMethod(composedObjectClass, methodCall.getName()
                        , methodCallArgsClasses);
                if(targetMethod != null)
                {
                    logger.info("matching method found! " + targetMethod.toGenericString());
                    targetObject = composedObjects.get(composedObjectClass);
                    break;
                }
            } catch (Exception e) { /* SWALLOW */ }
        }
        //if not found
        if(targetMethod == null)
            //throw exception
            throw new UnsupportedOperationException("method '" + methodCall.toGenericString() + "' not found");
        //else return the target object and method to caller
        return new Pair<Object, Method>(targetObject, targetMethod);
    }

}
