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
import com.smhumayun.mi_plus.util.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Provides core support for emulation of multiple inheritance behavior using Object Composition and Method Delegation
 *
 * @author smhumayun
 * @since 1.0
 */
final class MIContainerSupport implements InvocationHandler {

    /**
     * MI Container class - the class which has the {@link com.smhumayun.mi_plus.MISupport} annotation
     */
    private Class miContainerClass;

    /**
     * Holds composed objects
     */
    private LinkedHashMap<Class, Object> composedObjects = new LinkedHashMap<Class, Object>();

    /**
     * Method resolver
     * @see {@link com.smhumayun.mi_plus.MIMethodResolver}
     */
    private MIMethodResolver miMethodResolver;

    /**
     * LOGGER {@link java.util.logging.Logger}
     */
    private Logger logger = Logger.getLogger(MIContainerSupport.class.getName());

    /**
     * Utils
     */
    private Utils utils = new Utils();

    /**
     * Seed for hashCode
     */
    private int seed;

    /**
     * Random number generator for seed
     */
    private static final Random RANDOM = new Random(31);

    /**
     * Constructor will initialize this object with given <code>miContainerClass</code>
     * , <code>miMethodResolver</code> and <code>logger</code> objects
     *
     * @param miContainerClass MI Container class which has the {@link com.smhumayun.mi_plus.MISupport} annotation
     * @param miMethodResolver method resolver
     * @throws com.smhumayun.mi_plus.MIException {@link com.smhumayun.mi_plus.MIException}
     */
    MIContainerSupport(Class miContainerClass, MIMethodResolver miMethodResolver) throws MIException {
        setMiContainerClass(miContainerClass);
        setMiMethodResolver(miMethodResolver);
        this.seed = RANDOM.nextInt() + RANDOM.nextInt();
    }

    /**
     * Compose <code>objectToCompose</code>
     * NOTE: <code>objectToCompose</code> will be validated via {@link #validateAndCompose(Object)}
     *
     * @param objectToCompose object to compose
     * @throws MIException {@link MIException}
     */
    void compose (Object objectToCompose) throws MIException {
        //validate and thereafter compose
        validateAndCompose(objectToCompose);
    }

    /**
     * Validate <code>objectToCompose</code>
     *
     * @param objectToCompose object to validate for composition
     * @throws MIException {@link MIException}
     */
    private void validateAndCompose (Object objectToCompose) throws MIException {
        //check if the provided object to compose is not null
        if(objectToCompose == null)
            //if null throw exception
            throw new MIException("Composed object must not be null");
        //check if the same type of object has already been composed
        if(composedObjects.containsKey(objectToCompose.getClass()))
            //throw exception
            throw new MIException("Already inherited from " + objectToCompose.getClass().getName());
        logger.fine("Composing object of type " + objectToCompose.getClass().getName());
        //add the object to the list of composed objects. Why list? because order of composition matters
        //noinspection unchecked
        composedObjects.put(objectToCompose.getClass(), objectToCompose);
    }

    /**
     * Please refer to {@link InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}
     *
     * @param    proxy the proxy instance that the method was invoked on
     * @param    method the <code>Method</code> instance corresponding to
     * the interface method invoked on the proxy instance.
     * @param    args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or <code>null</code> if interface method takes no arguments.
     * @return the value to return from the method invocation on the
     * proxy instance.
     * @see    {@link java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])}
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("invoke - method: " + method.toGenericString() + " - args[]: " + Arrays.toString(utils.getClasses(args)));
        if(method.getName().equals("equals") && args != null && args.length == 1)
        {
            logger.finer("proxy.getClass().getName() = " + proxy.getClass().getName());
            logger.finer("args[0].getClass().getName() = " + args[0].getClass().getName());
            return proxy.getClass().getName().equals(args[0].getClass().getName()) && this.equals(Proxy.getInvocationHandler(args[0]));
        }
        if(method.getName().equals("hashCode") && (args == null || args.length == 0))
            return this.hashCode();
        //resolve target method using selected strategy
        Pair<Object, Method> target = getMiMethodResolver()
                .resolve(miContainerClass, composedObjects, method, args);
        //if target object found with the best method match
        if(target.getLeft() != null && target.getRight() != null)
            //invoke method on that target object and return the result to the caller
            return target.getRight().invoke(target.getLeft(), args);
        //in any other case, throw exception
        throw new UnsupportedOperationException("Method not found - " + method.toGenericString());
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj.getClass() != getClass())
            return false;
        MIContainerSupport other = (MIContainerSupport) obj;
        return this.seed == other.seed
                && this.getMiContainerClass().equals(other.getMiContainerClass())
                && this.getComposedObjects().equals(other.getComposedObjects());
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     *
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable
     */
    @Override
    public int hashCode() {
        System.out.println("this.seed = " + this.seed);
        int result = 3 * this.seed;
        System.out.println("getMiContainerClass().hashCode() = " + getMiContainerClass().hashCode());
        result = result * 5 + getMiContainerClass().hashCode();
        System.out.println("getComposedObjects().hashCode() = " + getComposedObjects().hashCode());
        result = result * 7 + getComposedObjects().hashCode();
        System.out.println("result = " + result);
        return result;
    }

    /**
     * Get list of composed objects
     *
     * @return list of composed objects
     */
    LinkedHashMap<Class, Object> getComposedObjects() {
        return composedObjects;
    }

    /**
     * Set list of composed objects
     *
     * @param composedObjects list of composed objects
     * @throws MIException {@link MIException}
     */
    void setComposedObjects(LinkedHashMap<Class, Object> composedObjects) throws MIException {
        if(composedObjects == null)
            throw new MIException("Composed objects should not be null");
        for(Object composedObject : composedObjects.values())
            validateAndCompose(composedObject);
    }

    /**
     * Get method resolver
     *
     * @return method resolver
     * @see {@link MIMethodResolver}
     */
    MIMethodResolver getMiMethodResolver() {
        return miMethodResolver;
    }

    /**
     * Set method resolver
     *
     * @param miMethodResolver method resolver
     * @see {@link MIMethodResolver}
     * @throws MIException {@link MIException}
     */
    void setMiMethodResolver(MIMethodResolver miMethodResolver) throws MIException {
        if(miMethodResolver == null)
            throw new MIException("miMethodResolver is NULL");
        this.miMethodResolver = miMethodResolver;
    }

    /**
     * Get MI Container class - the class which has the {@link com.smhumayun.mi_plus.MISupport} annotation
     *
     * @return MI Container class - the class which has the {@link com.smhumayun.mi_plus.MISupport} annotation
     */
    Class getMiContainerClass() {
        return miContainerClass;
    }

    /**
     * Set MI Container class - the class which has the {@link com.smhumayun.mi_plus.MISupport} annotation
     *
     * @param miContainerClass MI Container class - the class which has the {@link com.smhumayun.mi_plus.MISupport} annotation
     */
    void setMiContainerClass(Class miContainerClass) throws MIException {
        if(miContainerClass == null)
            throw new MIException("miContainerClass is NULL");
        this.miContainerClass = miContainerClass;
    }

}
