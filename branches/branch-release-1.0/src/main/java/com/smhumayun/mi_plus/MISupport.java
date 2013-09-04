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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project MI+'s custom annotation to enable MI support
 *
 * @author smhumayun
 * @since 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MISupport {

    /**
     * Parent classes to extend from
     *  - Only concrete classes are allowed; no interfaces or abstract classes
     *  - Nested classes are not allowed (why? http://codeoftheday.blogspot.com/2013/07/no-arguments-default-constructor-and.html)
     *  - Order of the classes matters, depending upon the selected {@link MIMethodResolver}
     *
     * @return parent classes to extend from
     */
    Class[] parentClasses();

    /**
     * Scope for container and composed objects
     *  - Default is {@link MIObjectScope#SINGLETON_CONTAINER_SINGLETON_COMPOSED}
     *
     * @return scope for container and composed objects
     * @see {@link MIObjectScope}
     */
    MIObjectScope scope() default MIObjectScope.SINGLETON_CONTAINER_SINGLETON_COMPOSED;

}
