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
package com.smhumayun.mi_plus.util;

/**
 * Utility class contain various utility methods used across api's implementation.
 *
 * @author smhumayun
 * @since 1.0
 */
public class Utils {

    /**
     * Utility method to get array of classes against given set of <code>objects</code>
     *
     * @param objects array of objects
     * @return array of classes against given set of <code>objects</code>
     */
    public Class[] getClasses (Object[] objects)
    {
        Class[] classes = null;
        if(objects != null)
        {
            classes = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                classes[i] = object.getClass();
            }
        }
        return classes;
    }

}
