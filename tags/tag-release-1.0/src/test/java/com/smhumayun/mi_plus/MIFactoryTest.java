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

import com.smhumayun.mi_plus.testdata.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Test cases against {@link MIFactory}
 *
 * @author smhumayun
 * @since 1.0
 */
@SuppressWarnings("UnusedDeclaration")
@Test(groups = {"MIFactory Basic Tests"})
public class MIFactoryTest {

    MIFactory miFactory;

    @SuppressWarnings("UnusedDeclaration")
    @BeforeClass
    public void beforeClass () {
        System.out.println("\n===================================[ " + this.getClass().getSimpleName() + " ]===================================");
    }

    @SuppressWarnings("UnusedDeclaration")
    @BeforeMethod
    public void beforeMethod (Method method) throws MIException {
        System.out.println("\n-----------------------------------[ " + method.getName() + " ]-----------------------------------");
        miFactory = new MIFactory();
        Assert.assertNotNull(miFactory, "NULL factory returned");
    }

    @Test(groups = {"negative tests"}, expectedExceptions = {MIException.class})
    public void newInstance_missingAnnotation () throws MIException {
        class MITest {}
        miFactory.newInstance(MITest.class);
    }

    @Test(groups = {"negative tests"}, expectedExceptions = {MIException.class})
    public void newInstance_missingParentClasses () throws MIException {
        @MISupport(parentClasses = {})
        class MITest {}
        miFactory.newInstance(MITest.class);
    }

    @Test(groups = {"negative tests"}, expectedExceptions = {MIException.class})
    public void newInstance_parentInterface () throws MIException {
        @MISupport(parentClasses = {MIFactory.class})
        class MITest {}
        miFactory.newInstance(MITest.class);
    }

    @Test(groups = {"negative tests"}, expectedExceptions = {MIException.class})
    public void newInstance_parentAbstract () throws MIException {
        abstract class AbstractParent {}
        @MISupport(parentClasses = {AbstractParent.class})
        class MITest {}
        miFactory.newInstance(MITest.class);
    }

    @Test(groups = {"positive tests"})
    public void newInstance_parentStaticNestedClass () throws MIException {
        miFactory.newInstance(IChild4.class);
    }

    @Test(groups = {"negative tests"}, expectedExceptions = {MIException.class})
    public void newInstance_parentNonStaticNestedClass () throws MIException {
        miFactory.newInstance(IChild5.class);
    }

    @Test(groups = {"negative tests"}, expectedExceptions = {MIException.class})
    public void newInstance_missingNoArgConstructor () throws MIException {
        class Parent { private Parent() {} }
        @MISupport(parentClasses = {Parent.class})
        class MITest {}
        miFactory.newInstance(MITest.class);
    }

    @Test(groups = {"positive tests"})
    public void newInstance_callParentMethods () throws MIException {
        IChild child = (IChild) miFactory.newInstance(IChild.class);
        Assert.assertNotNull(child, "object returned by factory is NULL");
        child.parentOneMethod();
        child.parentTwoMethod();
        Assert.assertTrue(child.sameMethodSignature().equals(ParentOne.class.getSimpleName()));
    }

    @Test(groups = {"positive tests"})
    public void newInstance_callDuplicateParentMethods () throws MIException {
        IChild child = (IChild) miFactory.newInstance(IChild.class);
        Assert.assertNotNull(child, "object returned by factory is NULL");
        Assert.assertTrue(child.sameMethodSignature().equals(ParentOne.class.getSimpleName()));
    }

    @Test(groups = {"positive tests"})
    public void testScanPackagesAndProcessAnnotatedClasses () {
        MIFactory miFactory = new MIFactory(Arrays.asList("com.smhumayun.mi_plus.testdata"));
        Assert.assertNotNull(miFactory, "NULL factory returned");
        Assert.assertNotNull(miFactory.getMiObjectCache().getCachedMiContainerProxy(IChild.class)
                , IChild.class.getName() + "'s instance not found in Container Proxy Cache");
        Assert.assertNull(miFactory.getMiObjectCache().getCachedMiContainerProxy(IChild2.class)
                , IChild2.class.getName() + "'s instance found in Container Proxy Cache");
        Assert.assertNotNull(miFactory.getMiObjectCache().getCachedComposedObjects(IChild2.class)
                , IChild2.class.getName() + "'s instance not found in Composed Object Cache");
        Assert.assertNull(miFactory.getMiObjectCache().getCachedMiContainerProxy(IChild3.class)
                , IChild3.class.getName() + "'s instance found in Container Proxy Cache");
        Assert.assertNull(miFactory.getMiObjectCache().getCachedComposedObjects(IChild3.class)
                , IChild3.class.getName() + "'s instance found in Composed Object Cache");
        Assert.assertNotNull(miFactory.getMiObjectCache().getCachedMiContainerProxy(IChild4.class)
                , IChild4.class.getName() + "'s instance not found in Container Proxy Cache");
    }

}
