package org.yaml.snakeyaml.issues.issue307;

import junit.framework.TestCase;
import lombok.Data;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class OrderTest extends TestCase {

    public void test_order() {
        Representer representer = new Representer();
        representer.setPropertyUtils(new OrderUtil());
        Yaml yaml = new Yaml(representer);
        String s = Util.getLocalResource("issues/issue307.yaml");
        OrderBean orderBean = yaml.loadAs(s, OrderBean.class);
        String dump = yaml.dump(orderBean);
        String str = "!!org.yaml.snakeyaml.issues.issue307.OrderTest$OrderBean\n" +
                "name: tian\n" +
                "type: {z: 256, y: 255, x: 254}\n" +
                "age: 22\n" +
                "line: twelve\n" +
                "text: omit\n";
        assertEquals(str,dump);
    }

    public void test_extend_order() {
        Representer representer = new Representer();
        representer.setPropertyUtils(new OrderUtil());
        Yaml yaml = new Yaml(representer);
        String s = Util.getLocalResource("issues/issue307.yaml");
        OrderBean2 orderBean2 = yaml.loadAs(s, OrderBean2.class);
        orderBean2.setPojoName("pojo");
        String dump = yaml.dump(orderBean2);
        String str = "!!org.yaml.snakeyaml.issues.issue307.OrderTest$OrderBean2\n" +
                "pojoName: pojo\n" +
                "name: tian\n" +
                "type: {z: 256, y: 255, x: 254}\n" +
                "age: 22\n" +
                "line: twelve\n" +
                "text: omit\n";
        assertEquals(str,dump);
    }

    public static class OrderUtil extends PropertyUtils {
        @Override
        protected Set<Property> createPropertySet(final Class<?> type, BeanAccess bAccess) {
            Set<Property> properties = new TreeSet<>(new Comparator<Property>() {
                @Override
                public int compare(Property prop1, Property prop2) {
                    Integer order1 = getValue(prop1.getName(), type);
                    Integer order2 = getValue(prop2.getName(), type);
                    if (order1 == null) {
                        order1 = 0;
                    }
                    if (order2 == null) {
                        order2 = 0;
                    }
                    if (order1 < order2) {
                        return -1;
                    }
                    if (order1 > order2) {
                        return 1;
                    }
                    return prop1.getName().compareTo(prop2.getName());
                }
            });
            properties.addAll(super.createPropertySet(type, bAccess));
            return properties;
        }

        public Integer getValue(String name, Class<?> type) {
            PropertyUtils propertyUtils = new PropertyUtils();
            propertyUtils.setBeanAccess(BeanAccess.FIELD);
            Property property = propertyUtils.getProperty(type, name);
            Annotation annotation = property.getAnnotation(OrderAnnotation.class);
            Integer invoke = null;
            if (annotation != null) {
                try {
                    Method method = annotation.annotationType().getDeclaredMethod("order");
                    invoke = (Integer) method.invoke(annotation, null);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return invoke;
        }
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OrderAnnotation {
        int order() default 0;
    }

    @Data
    static class OrderBean {

        public String name;

        @OrderAnnotation(order = 1)
        public Blocks type;

        @OrderAnnotation(order = 2)
        public Integer age;

        @OrderAnnotation(order = 3)
        public String line;

        @OrderAnnotation(order = 4)
        public String text;

    }

    @Data
    public static class Blocks {
        @OrderAnnotation(order = 3)
        public int x;

        @OrderAnnotation(order = 2)
        public int y;

        @OrderAnnotation(order = 1)
        public int z;

    }

    @Data
    static class OrderBean2 extends OrderBean {
        @OrderAnnotation(order = -1)
        public String pojoName;
    }
}
