package org.yaml.snakeyaml.issues.issue401;

import junit.framework.TestCase;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertySubstitute;
import org.yaml.snakeyaml.representer.Representer;

import java.lang.reflect.Method;
import java.util.Set;

public class SubstitudePropertyTypeTest extends TestCase {

    class MyThing {

        String prop;

        public String getProp() {
            return prop;
        }

        public void setProp(String prop) {
            this.prop = prop;
        }
    }

    public void test() {

        TypeDescription td = new TypeDescription(MyThing.class);
        Constructor c = new Constructor();
        Representer r = new Representer();

        c.addTypeDescription(td);

        td.substituteProperty(new PropertySubstitute("prop", Integer.class, "getProp", "setProp"));

        Set<Property> props = td.getProperties();
        if (props.size() == 1) {
            System.out.println("OK");
        } else {
            System.out.println("FAIL " + props.size());
        }
    }

    public void test2() throws NoSuchMethodException {
        Class clazz = MyThing.class;
        Method getProp = clazz.getDeclaredMethod("getProp");
        System.out.println(getProp);
        Method setProp = clazz.getDeclaredMethod("setProp", String.class);
        System.out.println(setProp);
    }
}
