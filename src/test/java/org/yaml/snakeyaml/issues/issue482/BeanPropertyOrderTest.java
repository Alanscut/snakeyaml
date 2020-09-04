/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.issues.issue482;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

public class BeanPropertyOrderTest extends TestCase {

    public void test_bean_order() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setKeepBeanPropertyOrder(true);
        Yaml yaml = new Yaml(dumperOptions);
        String str = "!!org.yaml.snakeyaml.issues.issue482.OrderBean\n" +
                "userName: Bruce\n" +
                "passWord: '123456'\n" +
                "email: www.github.com\n" +
                "address: {z: '255', y: '256', x: '254'}\n";
        OrderBean orderBean = yaml.loadAs(str, OrderBean.class);
        assertEquals(str, yaml.dump(orderBean));
    }

    public void test_private_public_bean_order() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setKeepBeanPropertyOrder(true);
        Yaml yaml = new Yaml(dumperOptions);
        String str = "!!org.yaml.snakeyaml.issues.issue482.OrderBean2\n" +
                "userName: Bruce\n" +
                "passWord: '123456'\n" +
                "email: www.github.com\n" +
                "address: {z: '255', y: '256', x: '254'}\n";
        OrderBean2 orderBean2 = yaml.loadAs(str, OrderBean2.class);
        assertEquals(str, yaml.dump(orderBean2));
    }

    public void test_BeanAccessFIELD_bean_order() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setKeepBeanPropertyOrder(true);
        propertyUtils.setBeanAccess(BeanAccess.FIELD);
        Representer representer = new Representer();
        representer.setPropertyUtils(propertyUtils);
        Yaml yaml = new Yaml(representer);
        String str = "!!org.yaml.snakeyaml.issues.issue482.OrderBean3\n" +
                "userName: Bruce\n" +
                "passWord: '123456'\n" +
                "email: www.github.com\n" +
                "address: {z: '255', y: '256', x: '254'}\n";
        OrderBean3 orderBean3 = yaml.loadAs(str, OrderBean3.class);
        assertEquals(str, yaml.dump(orderBean3));
    }

    public void test_extend_bean_order() {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setKeepBeanPropertyOrder(true);
        Yaml yaml = new Yaml(dumperOptions);
        String str = "!!org.yaml.snakeyaml.issues.issue482.OrderBean4\n" +
                "name: lucy\n" +
                "account: 654\n" +
                "userName: Bruce\n" +
                "passWord: '123456'\n" +
                "email: www.github.com\n" +
                "address: {z: '255', y: '256', x: '254'}\n";
        OrderBean4 orderBean4 = yaml.loadAs(str, OrderBean4.class);
        assertEquals(str, yaml.dump(orderBean4));
    }
}
