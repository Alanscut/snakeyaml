/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue11;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YamlMapTest extends TestCase {
    public void testYaml() throws IOException {
        Yaml yaml = new Yaml(new Loader(new ExtendedConstructor()), new Dumper(
                new ExtendedRepresenter(), new DumperOptions()));
        String output = yaml.dump(new Custom(123));
        // System.out.println(output);
        Custom o = (Custom) yaml.load(output);
        assertEquals("123", o.getStr());
    }

    @SuppressWarnings("unchecked")
    public void testYamlMap() throws IOException {
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("customTag", new Custom(123));

        Yaml yaml = new Yaml(new Loader(new ExtendedConstructor()), new Dumper(
                new ExtendedRepresenter(), new DumperOptions()));
        String output = yaml.dump(data);
        // System.out.println(output);
        Object o = yaml.load(output);

        assertTrue(o instanceof Map);
        Map<String, Object> m = (Map<String, Object>) o;
        assertTrue(m.get("customTag") instanceof Custom);
    }

    @SuppressWarnings("unchecked")
    public void testYamlMapBean() throws IOException {
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("knownClass", new Wrapper("test", new Custom(456)));

        Yaml yaml = new Yaml(new Loader(new ExtendedConstructor()), new Dumper(
                new ExtendedRepresenter(), new DumperOptions()));
        String output = yaml.dump(data);
        // System.out.println(output);
        Object o = yaml.load(output);

        assertTrue(o instanceof Map);
        Map<String, Object> m = (Map<String, Object>) o;
        assertEquals(Wrapper.class, m.get("knownClass").getClass());
    }

    public static class Wrapper {
        private String a;
        private Custom b;

        public Wrapper(String s, Custom bb) {
            a = s;
            b = bb;
        }

        public Wrapper() {
        }

        public String getA() {
            return a;
        }

        public void setA(String s) {
            a = s;
        }

        public Custom getB() {
            return b;
        }

        public void setB(Custom bb) {
            b = bb;
        }
    }

    public static class Custom {
        final private String str;

        public Custom(Integer i) {
            str = i.toString();
        }

        public Custom(Custom c) {
            str = c.str;
        }

        public String toString() {
            return str;
        }

        public String getStr() {
            return str;
        }
    }

    public static class ExtendedRepresenter extends Representer {
        public ExtendedRepresenter() {
            this.representers.put(Custom.class, new RepresentCustom());
        }

        private class RepresentCustom implements Represent {
            public Node representData(Object data) {
                return representScalar("!Custom", ((Custom) data).toString());
            }
        }
    }

    public static class ExtendedConstructor extends Constructor {
        public ExtendedConstructor() {
            this.yamlConstructors.put("!Custom", new ConstructCustom());
        }

        private class ConstructCustom extends AbstractConstruct {
            public Object construct(Node node) {
                String str = (String) constructScalar((ScalarNode) node);
                return new Custom(Integer.parseInt(str));
            }

        }
    }
}