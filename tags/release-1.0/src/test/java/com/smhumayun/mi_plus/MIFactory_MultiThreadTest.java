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

import com.smhumayun.mi_plus.testdata.IChild;
import com.smhumayun.mi_plus.testdata.IChild2;
import com.smhumayun.mi_plus.testdata.IChild3;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Multi Threaded Test cases against {@link com.smhumayun.mi_plus.MIFactory}
 *
 * @author smhumayun
 * @since 1.0
 */
@SuppressWarnings("UnusedDeclaration")
@Test(dependsOnGroups = {"MIFactory Basic Tests"})
public class MIFactory_MultiThreadTest {
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
    }

    @SuppressWarnings("UnusedDeclaration")
    @BeforeGroups(value = {"newInstance_SINGLETON_CONTAINER_SINGLETON_COMPOSED"
            , "newInstance_PROTOTYPE_CONTAINER_SINGLETON_COMPOSED"
            , "newInstance_PROTOTYPE_CONTAINER_PROTOTYPE_COMPOSED"})
    public void beforeGroup () throws MIException {
        miFactory = new MIFactory();
        Assert.assertNotNull(miFactory, "NULL factory returned");
    }

    @Test(enabled = true, groups = {"positive tests", "newInstance_SINGLETON_CONTAINER_SINGLETON_COMPOSED"}, threadPoolSize = 10, invocationCount = 50)
    public void newInstance_SINGLETON_CONTAINER_SINGLETON_COMPOSED () throws MIException {
        IChild child1 = (IChild) miFactory.newInstance(IChild.class);
        Assert.assertNotNull(child1, "object returned by factory is NULL");
        child1.parentOneMethod();
        child1.parentTwoMethod();
        IChild child2 = (IChild) miFactory.newInstance(IChild.class);
        Assert.assertNotNull(child2, "object returned by factory is NULL");
        child2.parentOneMethod();
        child2.parentTwoMethod();
        Assert.assertEquals(child1.hashCode(), child2.hashCode(), "hashCode() should be same");
        Assert.assertTrue(child1.equals(child2), "equals() should be true");
        Assert.assertEquals(child1, child2, "should be equal");
    }

    @Test(enabled = true, groups = {"positive tests", "newInstance_PROTOTYPE_CONTAINER_SINGLETON_COMPOSED"}, threadPoolSize = 10, invocationCount = 50)
    public void newInstance_PROTOTYPE_CONTAINER_SINGLETON_COMPOSED () throws MIException {
        IChild2 child1 = (IChild2) miFactory.newInstance(IChild2.class);
        Assert.assertNotNull(child1, "object returned by factory is NULL");
        child1.parentOneMethod();
        child1.parentTwoMethod();
        IChild2 child2 = (IChild2) miFactory.newInstance(IChild2.class);
        Assert.assertNotNull(child2, "object returned by factory is NULL");
        child2.parentOneMethod();
        child2.parentTwoMethod();
        Assert.assertNotEquals(child1.hashCode(), child2.hashCode(), "hashCode() should not be same");
        Assert.assertFalse(child1.equals(child2), "equals() should be false");
        Assert.assertNotEquals(child1, child2, "should not be equal");
        MIContainerSupport miContainerSupport1 = (MIContainerSupport) Proxy.getInvocationHandler(child1);
        MIContainerSupport miContainerSupport2 = (MIContainerSupport) Proxy.getInvocationHandler(child2);
        Assert.assertNotEquals(miContainerSupport1.hashCode(), miContainerSupport2.hashCode(), "MI Containers hashCode should not be same");
        Assert.assertFalse(miContainerSupport1.equals(miContainerSupport2), "equals() for MI Containers should be false");
        Assert.assertNotEquals(miContainerSupport1, miContainerSupport2, "MI Containers should not be equal");
        Assert.assertEquals(miContainerSupport1.getMiContainerClass().hashCode(), miContainerSupport2.getMiContainerClass().hashCode()
                , "MI Container classes hashCode should be same");
        Assert.assertTrue(miContainerSupport1.getMiContainerClass().equals(miContainerSupport2.getMiContainerClass())
                , "equals() for MI Container classes should be true");
        Assert.assertEquals(miContainerSupport1.getMiContainerClass(), miContainerSupport2.getMiContainerClass()
                , "MI Container classes should be equal");
        Assert.assertEquals(miContainerSupport1.getComposedObjects().hashCode(), miContainerSupport2.getComposedObjects().hashCode()
                , "Composed Objects hashCode should be same");
        Assert.assertTrue(miContainerSupport1.getComposedObjects().equals(miContainerSupport2.getComposedObjects())
                , "equals() for Composed Objects should be true");
        Assert.assertEquals(miContainerSupport1.getComposedObjects(), miContainerSupport2.getComposedObjects()
                , "Composed Objects should be equal");
    }

    @Test(enabled = true, groups = {"positive tests", "newInstance_PROTOTYPE_CONTAINER_PROTOTYPE_COMPOSED"}, threadPoolSize = 10, invocationCount = 50)
    public void newInstance_PROTOTYPE_CONTAINER_PROTOTYPE_COMPOSED () throws MIException {
        IChild3 child1 = (IChild3) miFactory.newInstance(IChild3.class);
        Assert.assertNotNull(child1, "object returned by factory is NULL");
        child1.parentOneMethod();
        child1.parentTwoMethod();
        IChild3 child2 = (IChild3) miFactory.newInstance(IChild3.class);
        Assert.assertNotNull(child2, "object returned by factory is NULL");
        child2.parentOneMethod();
        child2.parentTwoMethod();
        Assert.assertNotEquals(child1.hashCode(), child2.hashCode(), "hashCode() should not be same");
        Assert.assertFalse(child1.equals(child2), "equals() should be false");
        Assert.assertNotEquals(child1, child2, "should not be equal");
        MIContainerSupport miContainerSupport1 = (MIContainerSupport) Proxy.getInvocationHandler(child1);
        MIContainerSupport miContainerSupport2 = (MIContainerSupport) Proxy.getInvocationHandler(child2);
        Assert.assertNotEquals(miContainerSupport1.hashCode(), miContainerSupport2.hashCode(), "MI Containers hashCode should not be same");
        Assert.assertFalse(miContainerSupport1.equals(miContainerSupport2), "equals() for MI Containers should be false");
        Assert.assertNotEquals(miContainerSupport1, miContainerSupport2, "MI Containers should not be equal");
        Assert.assertEquals(miContainerSupport1.getMiContainerClass().hashCode(), miContainerSupport2.getMiContainerClass().hashCode()
                , "MI Container classes hashCode should be same");
        Assert.assertTrue(miContainerSupport1.getMiContainerClass().equals(miContainerSupport2.getMiContainerClass())
                , "equals() for MI Container classes should be true");
        Assert.assertEquals(miContainerSupport1.getMiContainerClass(), miContainerSupport2.getMiContainerClass()
                , "MI Container classes should be equal");
        Assert.assertNotEquals(miContainerSupport1.getComposedObjects().hashCode(), miContainerSupport2.getComposedObjects().hashCode()
                , "Composed Objects hashCode should not be same");
        Assert.assertFalse(miContainerSupport1.getComposedObjects().equals(miContainerSupport2.getComposedObjects())
                , "equals() for Composed Objects should be false");
        Assert.assertNotEquals(miContainerSupport1.getComposedObjects(), miContainerSupport2.getComposedObjects()
                , "Composed Objects should not be equal");
    }
}
