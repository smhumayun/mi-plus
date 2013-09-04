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
package com.smhumayun.mi_plus.testdata;

interface InterfaceForStaticClassInsideTopLevelClass { @SuppressWarnings("UnusedDeclaration")
                                                       void m1 (); }

interface InterfaceForNonStaticClassInsideTopLevelClass { @SuppressWarnings("UnusedDeclaration")
                                                          void m2 (); }

public class TopLevelClass {

    public static class StaticClassInsideTopLevelClass implements InterfaceForStaticClassInsideTopLevelClass {
        public void m1() {
            System.out.println("TopLevelClass$StaticClassInsideTopLevelClass.m1");
        }
    }

    public class NonStaticClassInsideTopLevelClass implements InterfaceForNonStaticClassInsideTopLevelClass {
        public void m2() {
            System.out.println("TopLevelClass$NonStaticClassInsideTopLevelClass.m2");
        }
    }

}
