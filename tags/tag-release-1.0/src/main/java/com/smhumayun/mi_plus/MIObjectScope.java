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

/**
 * Defines scopes for MI Container and Composed objects
 *
 * @author smhumayun
 * @since 1.0
 */
public enum MIObjectScope {

    /**
     * Only one object of the MI Container will be created which will contain/share the same Composed objects
     */
    SINGLETON_CONTAINER_SINGLETON_COMPOSED

    ,
    /**
     * Every time a new MI Container object will be created which will contain/share the same Composed objects
     */
    PROTOTYPE_CONTAINER_SINGLETON_COMPOSED

    ,
    /**
     * Every time a new MI Container object will be created which will contain their own distinct Composed objects
     */
    PROTOTYPE_CONTAINER_PROTOTYPE_COMPOSED

}
