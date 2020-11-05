package cn.hutool.extra.expression;

import cn.hutool.core.lang.Console;
import cn.hutool.extra.expression.engine.mvel.MvelExpressionEngine;
import cn.hutool.extra.expression.engine.mvel.MvelType;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.mvel2.MVEL;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *
 * @author independenter
 * @since
 */
public class MvelExpressionTest {

    /**
     * 从属性方法推断最后一个类型参数
     */
    @Test
    public void testInferLastTypeParametersFromPropertyMethod() {
        MvelExpressionEngine mvelExpressionEngine = new MvelExpressionEngine(MvelType.WRAP_COMPILE);
        mvelExpressionEngine.setContext((context) -> {
            context.setStrongTyping(true);
            context.addInput("a", A.class);
        },new AWrapper());
        String result = mvelExpressionEngine.getString("a.getFooMap()[\"key\"].someMethod()");
        Console.log(result);
    }

    /**
     * 测试对象转字符串 字符串转对象
     */
    @Test
    public void testObjectToString(){
        MvelExpressionEngine mvelExpressionEngine = new MvelExpressionEngine();

        Object data1 = getData();
        String str = mvelExpressionEngine.toMevlString(data1);
        Console.log(str);
        Object data2 = mvelExpressionEngine.get(str);
        Assert.assertEquals(data1,data2);
    }

    @Test
    public void testHashCode() {
        MvelExpressionEngine mvelExpressionEngine = new MvelExpressionEngine();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("var0", "var0");
        mvelExpressionEngine.set(map);
        List list = (List) mvelExpressionEngine.get("[ 'key1', var0 ]");
        Console.log(list.hashCode());
    }

    @Test
    public void quickSort(){
        Map<String,Object> vars = new HashMap<>();

        Set<String> things = new HashSet<>();
        things.add("soap");
        things.add("siphon");
        things.add("power");

        vars.put("name","Mike");
        vars.put("things",things);

        MvelExpressionEngine mvelExpressionEngine = new MvelExpressionEngine(vars);
        mvelExpressionEngine.setType(MvelType.RUNTIME);
        String result = mvelExpressionEngine.getString("My List of things @foreach{item:things}@{item.toUpperCase()}@end{', '}");
        Console.log(result);
    }

    @Test
    public void testReturn(){
        MvelExpressionEngine engine = new MvelExpressionEngine();
        Boolean result = engine.getBoolean("a=10;b=(a=2)+10;a==2");
        Console.log(result);
    }

    private Object getData() {
        Pet pet = new Pet();
        pet.setName("rover");
        pet.setAge(7);
        List list = new ArrayList();
        list.add("a");
        list.add(12);
        list.add(new SomeNumers(10.02f,
                22.02,
                5,
                100l,
                new BigDecimal(23.0234d,
                        MathContext.DECIMAL128),
                new BigInteger("1001")));
        list.add(new Date());
        //list.add( 'b' ); // generates ok but breaks round trip equals
        list.add(new Cheese("cheddar",
                6));

        pet.setList(list);
        pet.setArray(new int[]{1, 2, 3});

        Map map = new HashMap();
        //map.put( new Date(), new Cheese( "stilton", 11) ); // TODO why doesn't this work
        map.put("key1",
                13);
        map.put("key3",
                "value3");
        map.put("key2",
                15);
        map.put("key4",
                new Cheese("stilton",
                        11));
        Calendar cal = Calendar.getInstance();
//        cal.setTime( new Date() );
//        map.put( "key5",
//                 cal ); // TODO why doesn't this work.
        //map.put( "key4", new String[] { "a", "b" } ); // TODO why doesn't this work

        Person person = new Person();
        person.setName("mark");
        person.setAge(33);
        person.setPet(pet);
        person.setSomeDate(new Date());
        person.setMap(map);
        cal = Calendar.getInstance();
        cal.setTime(new Date());
        person.setCal(cal);

        return person;
    }

    @Data
    public static class SomeNumers {
        private float aFloat;
        private double aDouble;
        private int aInt;
        private long aLong;
        private BigDecimal aBigDecimal;
        private BigInteger aBigInteger;

        public SomeNumers() {

        }

        public SomeNumers(float float1,
                          double double1,
                          int int1,
                          long long1,
                          BigDecimal bigDecimal,
                          BigInteger bigInteger) {
            super();
            aFloat = float1;
            aDouble = double1;
            aInt = int1;
            aLong = long1;
            aBigDecimal = bigDecimal;
            aBigInteger = bigInteger;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((aBigDecimal == null) ? 0 : aBigDecimal.hashCode());
            result = prime * result + ((aBigInteger == null) ? 0 : aBigInteger.hashCode());
            long temp;
            temp = Double.doubleToLongBits(aDouble);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            result = prime * result + Float.floatToIntBits(aFloat);
            result = prime * result + aInt;
            result = prime * result + (int) (aLong ^ (aLong >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            SomeNumers other = (SomeNumers) obj;
            if (aBigDecimal == null) {
                if (other.aBigDecimal != null) return false;
            }
            else if (!aBigDecimal.equals(other.aBigDecimal)) return false;
            if (aBigInteger == null) {
                if (other.aBigInteger != null) return false;
            }
            else if (!aBigInteger.equals(other.aBigInteger)) return false;
            if (Double.doubleToLongBits(aDouble) != Double.doubleToLongBits(other.aDouble)) return false;
            if (Float.floatToIntBits(aFloat) != Float.floatToIntBits(other.aFloat)) return false;
            if (aInt != other.aInt) return false;
            if (aLong != other.aLong) return false;
            return true;
        }

    }

    @Data
    public static class Person {
        private String name;
        private int age;
        private Date someDate;

        private Pet pet;

        private Object nullTest;

        private Map map;

        private Calendar cal;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + age;
            result = prime * result + ((cal == null) ? 0 : cal.hashCode());
            result = prime * result + ((map == null) ? 0 : map.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((nullTest == null) ? 0 : nullTest.hashCode());
            result = prime * result + ((pet == null) ? 0 : pet.hashCode());
            result = prime * result + ((someDate == null) ? 0 : someDate.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Person other = (Person) obj;
            if (age != other.age) return false;
            if (cal == null) {
                if (other.cal != null) return false;
            }
            else if (!cal.equals(other.cal)) return false;
            if (map == null) {
                if (other.map != null) return false;
            }
            else if (!map.equals(other.map)) return false;
            if (name == null) {
                if (other.name != null) return false;
            }
            else if (!name.equals(other.name)) return false;
            if (nullTest == null) {
                if (other.nullTest != null) return false;
            }
            else if (!nullTest.equals(other.nullTest)) return false;
            if (pet == null) {
                if (other.pet != null) return false;
            }
            else if (!pet.equals(other.pet)) return false;
            if (someDate == null) {
                if (other.someDate != null) return false;
            }
            else if (!someDate.equals(other.someDate)) return false;
            return true;
        }

    }

    @Data
    public static class Pet {
        private String name;
        private Integer age;

        private List list;
        private int[] array;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((age == null) ? 0 : age.hashCode());
            result = prime * result + Arrays.hashCode(array);
            result = prime * result + ((list == null) ? 0 : list.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Pet other = (Pet) obj;
            if (age == null) {
                if (other.age != null) return false;
            }
            else if (!age.equals(other.age)) return false;
            if (!Arrays.equals(array,
                    other.array)) return false;
            if (list == null) {
                if (other.list != null) return false;
            }
            else if (!list.equals(other.list)) return false;
            if (name == null) {
                if (other.name != null) return false;
            }
            else if (!name.equals(other.name)) return false;
            return true;
        }

    }

    @Data
    public static class Cheese {
        private String type;
        private int age;
        private boolean edible;

        public Cheese() {

        }

        public Cheese(String type,
                      int age) {
            this.type = type;
            this.age = age;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + age;
            result = prime * result + (edible ? 1231 : 1237);
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Cheese other = (Cheese) obj;
            if (age != other.age) return false;
            if (edible != other.edible) return false;
            if (type == null) {
                if (other.type != null) return false;
            }
            else if (!type.equals(other.type)) return false;
            return true;
        }

    }

    private static final List<String> STRINGS = Arrays.asList("hi", "there", "dude");

    /**
     * A'wrapper class
     */
    public static class AWrapper {
        public A getA() {
            return new A();
        }
    }

    /**
     * A Class
     */
    public static class A {
        private boolean show;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public List<String> getStrings() {
            return STRINGS;
        }

        public List<String> values() {
            return STRINGS;
        }

        public Map<String, Foo> getFooMap() {
            Map<String, Foo> map = new HashMap<String, Foo>();
            map.put("key", new Foo() {
                public String someMethod() {
                    return "bar";
                }
            });

            return map;
        }

        public Map<String, Foo> getBarMap() {
            Map<String, Foo> map = new HashMap<String, Foo>();
            map.put("key", new FooImpl());
            return map;
        }
    }

    public static interface Foo {

        public String someMethod();
    }

    public static class FooImpl implements Foo {

        public String someMethod() {
            return "bar";
        }
    }
}
