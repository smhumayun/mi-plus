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

import com.smhumayun.mi_plus.impl.MIMethodResolverImpl;
import com.smhumayun.mi_plus.impl.MIObjectInMemoryCache;
import eu.infomas.annotation.AnnotationDetector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for creating objects that supports MI
 *
 * @author smhumayun
 * @since 1.0
 */
public final class MIFactory {

    /**
     * Cache of container and composed objects
     *  - Defaults to {@link com.smhumayun.mi_plus.impl.MIObjectInMemoryCache}
     */
    private MIObjectCache miObjectCache;

    /**
     * Method resolver
     *  - Defaults to {@link com.smhumayun.mi_plus.impl.MIMethodResolverImpl}
     */
    private MIMethodResolver miMethodResolver;

    /**
     * Logger {@link java.util.logging.Logger}
     */
    private Logger logger = Logger.getLogger(MIFactory.class.getName());

    /**
     * Lock Object for MI Container Cache
     */
    private final Object miContainerProxyCacheLockObj = new Object();

    /**
     * Lock Object for Composed Objects Cache
     */
    private final Object composedObjectsCacheLockObj = new Object();

    /**
     * Constructor which initializes this factory instance with a default {@link com.smhumayun.mi_plus.impl.MIObjectInMemoryCache} and a default
     * {@link com.smhumayun.mi_plus.impl.MIMethodResolverImpl}
     *
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory() throws MIException {
        this(new MIObjectInMemoryCache(), new MIMethodResolverImpl(), null);
    }

    /**
     * Constructor which initializes this factory instance with a default {@link MIObjectInMemoryCache} and a default
     * {@link MIMethodResolverImpl}; and scans given <code>packagesToScan</code> to find and process classes annotated
     * with {@link MISupport} annotation
     *
     * @param packagesToScan list of packages to scan for {@link MISupport} annotation
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory(List<String> packagesToScan) throws MIException {
        this(new MIObjectInMemoryCache(), new MIMethodResolverImpl(), packagesToScan);
    }

    /**
     * Constructor which initializes this factory instance with given <code>miObjectCache</code> and a default
     * {@link MIMethodResolverImpl}
     *
     * @param miObjectCache object cache implementation
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory(MIObjectCache miObjectCache) throws MIException {
        this(miObjectCache, new MIMethodResolverImpl(), null);
    }

    /**
     * Constructor which initializes this factory instance with given <code>miObjectCache</code> and a default
     * {@link MIMethodResolverImpl}; and scans given <code>packagesToScan</code> to find and process classes annotated
     * with {@link MISupport} annotation
     *
     * @param miObjectCache object cache implementation
     * @param packagesToScan list of packages to scan for {@link MISupport} annotation
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory(MIObjectCache miObjectCache, List<String> packagesToScan) throws MIException {
        this(miObjectCache, new MIMethodResolverImpl(), packagesToScan);
    }

    /**
     * Constructor which initializes this factory instance with given <code>miMethodResolver</code> and a default
     * {@link MIObjectInMemoryCache}
     *
     * @param miMethodResolver method resolver implementation
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory(MIMethodResolver miMethodResolver) throws MIException {
        this(new MIObjectInMemoryCache(), miMethodResolver, null);
    }

    /**
     * Constructor which initializes this factory instance with given <code>miMethodResolver</code> and a default
     * {@link MIObjectInMemoryCache}; and scans given <code>packagesToScan</code> to find and process classes annotated
     * with {@link MISupport} annotation
     *
     * @param miMethodResolver method resolver implementation
     * @param packagesToScan list of packages to scan for {@link MISupport} annotation
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory(MIMethodResolver miMethodResolver, List<String> packagesToScan) throws MIException {
        this(new MIObjectInMemoryCache(), miMethodResolver, packagesToScan);
    }

    /**
     * Constructor which initializes this factory instance with given <code>miObjectCache</code> and
     * <code>miMethodResolver</code> objects
     *
     * @param miObjectCache object cache implementation
     * @param miMethodResolver method resolver implementation
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("UnusedDeclaration")
    public MIFactory(MIObjectCache miObjectCache, MIMethodResolver miMethodResolver) throws MIException {
        this(miObjectCache, miMethodResolver, null);
    }

    /**
     * Constructor which initializes this factory instance with given <code>miObjectCache</code> and
     * , <code>miMethodResolver</code> objects; and scans given <code>packagesToScan</code>
     * to find and process classes annotated with {@link MISupport} annotation
     *
     * @param miObjectCache object cache implementation
     * @param miMethodResolver method resolver implementation
     * @param packagesToScan list of packages to scan for {@link MISupport} annotation
     * @throws MIException {@link MIException}
     */
    public MIFactory(MIObjectCache miObjectCache, MIMethodResolver miMethodResolver, List<String> packagesToScan)
            throws MIException {
        setMiObjectCache(miObjectCache);
        setMiMethodResolver(miMethodResolver);
        logger.info(MIFactory.class.getSimpleName() + " of type " + this.getClass().getName() + " created!");
        scanPackagesAndProcessAnnotatedClasses(packagesToScan);
    }

    /**
     * This method will scan given <code>packagesToScan</code> to find and process classes annotated with
     * {@link MISupport} annotation
     *
     * @param packagesToScan list of packages to scan for {@link MISupport} annotation
     * @throws MIException {@link MIException}
     */
    private void scanPackagesAndProcessAnnotatedClasses (List<String> packagesToScan) throws MIException
    {
        try
        {
            if(packagesToScan != null && packagesToScan.size() > 0)
            {
                AnnotationDetector annotationDetector = new AnnotationDetector(new AnnotationDetector.TypeReporter() {

                    public void reportTypeAnnotation(Class<? extends Annotation> aClass, String className) {
                        try {
                            newInstance(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            throw new MIException(e.toString(), e);
                        }
                    }

                    public Class<? extends Annotation>[] annotations() {
                        //noinspection unchecked
                        return new Class[]{MISupport.class};
                    }

                });

                //noinspection ToArrayCallWithZeroLengthArrayArgument
                annotationDetector.detect(packagesToScan.toArray(new String[]{}));
            }
        }
        catch (Throwable t)
        {
            if(t instanceof MIException)
                throw (MIException) t;
            throw new MIException(t);
        }
    }

    /**
     * Factory method to create new instances of MI Container classes that are annotated with
     * {@link com.smhumayun.mi_plus.MISupport} annotation
     *
     * @param clazz class to instantiate - MI Container class annotated with {@link MISupport} annotation
     * @return an object that functionally supports MI
     * @throws MIException {@link MIException}
     */
    @SuppressWarnings("ConstantConditions")
    public Object newInstance(Class clazz) throws MIException {
        try
        {
            logger.info("clazz = " + clazz.getName());
            //get annotation
            MISupport MISupport = getAnnotation(clazz);
            logger.fine("scope = " + MISupport.scope().name());
            //if singleton container
            if(MISupport.scope().equals(MIObjectScope.SINGLETON_CONTAINER_SINGLETON_COMPOSED))
            {
                //acquire lock for MI Container Proxy cache
                synchronized (miContainerProxyCacheLockObj)
                {
                    //get MI Container Proxy from cache
                    Object miContainerProxy = getMiObjectCache().getCachedMiContainerProxy(clazz);
                    //if MI Container Proxy does not exists against this clazz
                    if(miContainerProxy == null)
                    {
                        logger.fine("MI Container Proxy object not available in cache");
                        //create new MI Container Proxy object containing new set of composed objects
                        MIContainerSupport miContainerSupport = createContainerSupport(MISupport, clazz, null);
                        //create a MI Container Proxy object and associate MI Container Support object as the invocation handler
                        miContainerProxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, miContainerSupport);
                        logger.finer("MI Container Proxy object created");
                        //cache MI Container Proxy
                        getMiObjectCache().cacheMiContainerProxy(clazz, miContainerProxy);
                        logger.finer("MI Container Proxy object saved in cache");
                    }
                    else
                        logger.fine("MI Container Proxy object found in cache");
                    return miContainerProxy;
                }
            }
            else //if prototype container
            {
                MIContainerSupport miContainerSupport;
                //if singleton composed objects
                if(MISupport.scope().equals(MIObjectScope.PROTOTYPE_CONTAINER_SINGLETON_COMPOSED))
                {
                    logger.finer("get Composed objects from cache");
                    //acquire lock of composed objects cache
                    synchronized (composedObjectsCacheLockObj)
                    {
                        LinkedHashMap<Class, Object> composedObjects = getMiObjectCache().getCachedComposedObjects(clazz);
                        //create new container object containing composed objects - create composed objects if
                        // - Singleton Composed Objects are not available in cache - once created cache them as well
                        miContainerSupport = createContainerSupport(MISupport, clazz, composedObjects);
                    }
                }
                else
                    //create new container object containing composed objects and create composed objects
                    miContainerSupport = createContainerSupport(MISupport, clazz, null);
                //create a proxy object and associate MI container object as the invocation handler
                Object miContainerProxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, miContainerSupport);
                logger.finer("Proxy of container object created");
                return miContainerProxy;
            }
        }
        catch(Exception e) {
            if(e instanceof MIException)
                throw (MIException) e;
            logger.log(Level.SEVERE, e.toString(), e);
            throw new MIException(e.toString(), e);
        }
    }

    /**
     * This method is responsible to:
     *  - validate MI Container class - the one which is annotated with {@link MISupport} annotation
     *  - create a new {@link MIContainerSupport} object using
     *  - if <code>composedObjects</code> are provided compose them inside newly created MI Container Support object
     *  - else, create new set of composed objects
     *  - cache newly created composed objects if {@link MIObjectScope#PROTOTYPE_CONTAINER_SINGLETON_COMPOSED}
     *  - finally return the newly created and configured MI Container Support object to the caller
     *
     * @param miSupport {@link MISupport} annotation
     * @param clazz MI Container class which has the {@link MISupport} annotation
     * @param composedObjects objects to be composed inside newly created container
     * @return newly created and configured {@link MIContainerSupport} object
     * @throws MIException {@link MIException}
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private MIContainerSupport createContainerSupport(MISupport miSupport, Class clazz, LinkedHashMap<Class, Object> composedObjects)
            throws MIException, IllegalAccessException, InstantiationException {
        //validate and extract annotation and parent classes
        validateMIClass(clazz);
        //create new MI Container Support object
        MIContainerSupport miContainerSupport = new MIContainerSupport(clazz, getMiMethodResolver());
        logger.finer("new MI Container Support object created");
        //if composed objects are not provided, create, compose and cache them
        if(composedObjects == null) {
            //iterate through available inheritance classes
            for(Class clazz1 : miSupport.parentClasses()) {
                //create new instances against each of the inherited classes and compose them inside
                //MI Container Support object
                miContainerSupport.compose(clazz1.newInstance());
                logger.finer("new Composed object created - " + clazz1.getName());
            }
            //check if composed objects needs to be cached
            if(miSupport.scope().equals(MIObjectScope.PROTOTYPE_CONTAINER_SINGLETON_COMPOSED))
            {
                //cache composed objects
                getMiObjectCache().cacheComposedObjects(clazz, miContainerSupport.getComposedObjects());
                logger.finer("newly created Composed object saved in cache");
            }
        }
        else //if composed objects are provided
            //compose them inside container
            miContainerSupport.setComposedObjects(composedObjects);
        return miContainerSupport;
    }

    /**
     * Return {@link MISupport} annotation if present on given <code>clazz</code> else throw following exception
     *
     * @param clazz class to extract annotation from
     * @return extracted {@link MISupport} annotation
     * @throws MIException if {@link MISupport} is not present on the <code>clazz</code>
     */
    private MISupport getAnnotation (Class clazz) throws MIException {
        //check for MISupport annotation
        if(clazz.isAnnotationPresent(MISupport.class))
            //if found return
            //noinspection unchecked
            return (MISupport) clazz.getAnnotation(MISupport.class);
        //else throw exception if annotation is missing
        throw new MIException("@MISupport annotation not found on class " + clazz.getName());
    }

    /**
     * Validate given MI Container class
     *
     * @param clazz MI Container class to validate
     * @throws MIException {@link MIException}
     */
    private void validateMIClass (Class clazz) throws MIException
    {
        logger.fine("validating class " + clazz.getName());
        //check cache, if it is already validated
        if(getMiObjectCache().isValidated(clazz))
        {
            logger.fine("class already validated : " + clazz.getName());
            return;
        }
        @SuppressWarnings("unchecked")
        MISupport MISupport = getAnnotation(clazz);
        //get parent classes information from annotation
        Class[] parentClasses = MISupport.parentClasses();
        //check if one or more inheritance classes are available
        if(parentClasses != null && parentClasses.length > 0) {
            logger.fine("parentClasses.length = " + parentClasses.length);
            //iterate through available inheritance classes
            for(Class parentClass : parentClasses) {
                logger.fine("parentClass - " + parentClass.getName());
                //check if it its an interface
                if(parentClass.isInterface())
                    //if it is, throw exception
                    throw new MIException("Found interface " + parentClass.getName()
                            + " - interfaces as parent not allowed, " +
                            "because a class can not extend an interface, " +
                            "it can only implement an interface");
                //check if its an abstract class
                if(Modifier.isAbstract(parentClass.getModifiers()))
                    //if it is throw exception
                    throw new MIException("Found abstract class " + parentClass.getName()
                            + " - abstract classes as parent are not allowed, " +
                            "because they can not be instantiated.");
                //find no-arg constructor
                boolean foundNoArgConstructor = false;
                Constructor<?>[] constructors = parentClass.getDeclaredConstructors();
                for (Constructor<?> constructor : constructors) {
                    if(constructor.getParameterTypes().length == 0
                            && constructor.getModifiers() == Modifier.PUBLIC) {
                        foundNoArgConstructor = true;
                        break;
                    }
                }
                //if not found
                if(!foundNoArgConstructor)
                {
                    displayClassInfo(parentClass);
                    //throw exception
                    throw new MIException("Class " + parentClass.getName()
                            + " does not contains required no-arg constructor. Possible reason: " +
                            "http://codeoftheday.blogspot.com/2013/07/no-arguments-default-constructor-and.html");
                }
            }
            //add validated class to cache
            getMiObjectCache().validated(clazz);
            return; // all is well
        }
        //throw exception if classes are missing
        throw new MIException("@MISupport annotation on class " + clazz.getName()
                + " must contain one or more concrete parent classes");
    }

    /**
     * Method to log given class info
     *
     * @param clazz class whose info is to be logged
     */
    private void displayClassInfo (Class clazz) {
        StringBuilder sb = new StringBuilder("\n").append(clazz.getName()).append(" [ ")
                .append(clazz.isMemberClass() ? "Member Class " : "")
                .append(clazz.isLocalClass() ? "Local Class " : "")
                .append(clazz.isAnonymousClass() ? "Anonymous Class " : "")
                .append(clazz.isSynthetic() ? "Synthetic Class " : "")
                .append("]")
                .append(getDeclaringClass(clazz))
                .append(clazz.getEnclosingClass() != null ? "\n\tEnclosed in class - " + clazz.getEnclosingClass().getSimpleName() : "")
                .append(clazz.getEnclosingConstructor() != null ? "\n\tEnclosed in constructor - " + clazz.getEnclosingConstructor().toGenericString() : "")
                .append(clazz.getEnclosingMethod() != null ? "\n\tEnclosed in method - " + clazz.getEnclosingMethod().toGenericString() : "")
                .append("\n\tConstructors:")
                ;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            sb.append("\n\t\t").append(constructor.toGenericString());
        }
        logger.info(sb.toString());
    }

    /**
     * The sole purpose of this method is to swallow following exception:
     *
     * java.lang.IncompatibleClassChangeError: com.smhumayun.mi_plus.MIFactoryTest and
     * com.smhumayun.mi_plus.MIFactoryTest$1Parent disagree on InnerClasses attribute
     * at java.lang.Class.getDeclaringClass(Native Method)
     *
     * Need to investigate it further as to why this was thrown and how it can be prevented. The exception was
     * thrown when we run impl case newInstance_missingNoArgConstructor(). It is quite confusing that the exception was
     * thrown in a method that is meant for displaying class meta only. The actual class and method to blame is
     * {@link Class#getDeclaringClass()}
     *
     * @param clazz class
     * @return declaring class's simple name
     */
    private String getDeclaringClass (Class clazz)
    {
        try
        {
            return clazz.getDeclaringClass() != null ? "\n\tDeclared by class - " + clazz.getDeclaringClass().getSimpleName() : "";
        }
        catch (IncompatibleClassChangeError e)
        {
            /*SWALLOW*/
            return "\n\tDeclared by class - [ " + e.toString() + "] ";
        }
    }

    /**
     * Get method resolver
     *
     * @return method resolver
     * @see {@link com.smhumayun.mi_plus.MIMethodResolver}
     */
    public MIMethodResolver getMiMethodResolver() {
        return miMethodResolver;
    }

    /**
     * Set method resolver
     *
     * @param miMethodResolver method resolver
     * @see {@link com.smhumayun.mi_plus.MIMethodResolver}
     * @throws MIException {@link MIException}
     */
    public void setMiMethodResolver(MIMethodResolver miMethodResolver) throws MIException {
        if(miMethodResolver == null)
            throw new MIException("miMethodResolver is NULL");
        this.miMethodResolver = miMethodResolver;
    }

    /**
     * Get {@link MIObjectCache}
     *
     * @return {@link MIObjectCache}
     */
    public MIObjectCache getMiObjectCache() {
        return miObjectCache;
    }

    /**
     * Set {@link MIObjectCache}
     *
     * @param miObjectCache {@link MIObjectCache}
     */
    public void setMiObjectCache(MIObjectCache miObjectCache) throws MIException {
        if(miObjectCache == null)
            throw new MIException("miObjectCache is NULL");
        this.miObjectCache = miObjectCache;
    }

}
