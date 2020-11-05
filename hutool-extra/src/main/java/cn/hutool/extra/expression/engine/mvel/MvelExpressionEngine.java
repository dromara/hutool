package cn.hutool.extra.expression.engine.mvel;

import cn.hutool.extra.expression.ExpressionEngine;
import cn.hutool.extra.expression.ExpressionException;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.util.StringAppender;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p> 编译版本通过本类使用
 *
 * @author independenter
 * @since
 */
public class MvelExpressionEngine implements ExpressionEngine {

    public MvelType type;

    private Map<String,Object> mvelMap;

    private ParserContext context;

    private Object wrapper;

    public MvelExpressionEngine(){
        this(MvelType.SIMPLE);
    }

    public MvelExpressionEngine(Map<String, Object> map){
        this(MvelType.SIMPLE);
        set(map);
    }

    /**
     * Mvel强大的编译功能
     * @param type
     */
    public MvelExpressionEngine(MvelType type){
        this.type = type;
        if(type == MvelType.WRAP_COMPILE){
            context = new ParserContext();
        }else{
            mvelMap = new HashMap<>();
        }
    }

    public void setType(MvelType type) {
        this.type = type;
    }

    /**
     * @param halder 对于context进行特殊处理,做类的导入之类
     * @param wrapper
     */
    public void setContext(MvelVoidHalder halder,Object wrapper){
        if(type == MvelType.WRAP_COMPILE){
            halder.execute(context);
            if(wrapper == null) {
                throw new ExpressionException("包装类不能为null");
            }
            this.wrapper = wrapper;
        }else{
            halder.execute(context);
        }
    }

    @Override
    public Object get(final String expr) {
        if(type == MvelType.WRAP_COMPILE){
            final CompiledExpression compiledExpression = new ExpressionCompiler(expr, context)
                    .compile();
            return MVEL.executeExpression(compiledExpression, wrapper);
        }else if(type == MvelType.RUNTIME){
            return TemplateRuntime.eval(expr, mvelMap);
        }else{
            final Serializable ser = MVEL.compileExpression(expr);
            return MVEL.executeExpression(ser,context,mvelMap);
        }
    }

    @Override
    public void set(final Map<String, Object> map) {
        map.forEach(mvelMap::put);
    }

    @Override
    public void set(String key, Object value) {
        mvelMap.put(key,value);
    }

    @FunctionalInterface
    public interface MvelVoidHalder{
        void execute(ParserContext context);
    }

    public String toMevlString(Object data){
        Marshaller marshaller = new Marshaller();
        return marshaller.marshallToString(data);
    }

    public static enum Type {
        PRIMITIVE, CHAR, STRING, DATE, CALENDAR, BIG_INTEGER, BIG_DECIMAL, ARRAY, MAP, COLLECTION, OBJECT;
    }

    public static class ObjectConverter {
        private Class type;
        private ObjectConverterEntry[] fields;

        public ObjectConverter(Class type,
                               ObjectConverterEntry[] fields) {
            this.type = type;
            this.fields = fields;
        }

        public Class getType() {
            return this.type;
        }

        public ObjectConverterEntry[] getFields() {
            return fields;
        }
    }

    public static class ObjectConverterEntry {
        private String name;
        private Type type;
        private Method method;

        public ObjectConverterEntry(String name,
                                    Method method,
                                    Type type) {
            this.name = name;
            this.type = type;
            this.method = method;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }

        public Method getMethod() {
            return this.method;
        }

    }

    public static class MarshallerContext {
        private Marshaller marshaller;
        private StringAppender appender = new StringAppender();

        public MarshallerContext(Marshaller marshaller) {
            this.marshaller = marshaller;
            this.appender = new StringAppender();
        }

        public void marshall(Object object) {
            marshaller.marshall(object,
                    this);
        }

        public StringAppender getAppender() {
            return appender;
        }
    }

    public static interface CustomMarshaller {
        public void marshall(Object object,
                             MarshallerContext ctx);
    }

    public static class EpocDateMarshaller
            implements
            CustomMarshaller {

        public void marshall(Object object,
                             MarshallerContext ctx) {
            ctx.getAppender().append("new java.util.Date(" + ((Date) object).getTime() + ")");
        }
    }

    public static class EpocDefaultCalendarMarshaller
            implements
            CustomMarshaller {
        private CustomMarshaller dateMarshaller;

        public EpocDefaultCalendarMarshaller() {
            this(new EpocDateMarshaller());
        }

        public EpocDefaultCalendarMarshaller(CustomMarshaller dateMarshaller) {
            this.dateMarshaller = dateMarshaller;
        }

        public void marshall(Object object,
                             MarshallerContext ctx) {

            ctx.getAppender().append("with ( java.util.Calendar.getInstance() ) { time = ");
            this.dateMarshaller.marshall(((Calendar) object).getTime(),
                    ctx);
            ctx.getAppender().append("} ");
        }
    }

    public static class Marshaller {
        private Map<Class, ObjectConverter> converters;
        private CustomMarshaller dateMarshaller;
        private CustomMarshaller calendarMarshaller;

        public Marshaller() {
            this(new HashMap<Type, CustomMarshaller>());
        }

        public Marshaller(Map<Type, CustomMarshaller> custom) {
            this.converters = new HashMap<Class, ObjectConverter>();
            this.dateMarshaller = custom.get(Type.DATE);
            if (this.dateMarshaller == null) {
                this.dateMarshaller = new EpocDateMarshaller();
            }

            this.calendarMarshaller = custom.get(Type.CALENDAR);
            if (this.calendarMarshaller == null) {
                this.calendarMarshaller = new EpocDefaultCalendarMarshaller();
            }
        }

        public void marshall(Object object,
                             MarshallerContext ctx) {
            marshall(object,
                    getType(object.getClass()),
                    ctx);
        }

        public void marshall(Object object,
                             Type type,
                             MarshallerContext ctx) {
            if (object == null) {
                ctx.getAppender().append("null");
                return;
            }

            if (type != Type.OBJECT) {
                marshallValue(object,
                        type,
                        ctx);
            }
            else {
                Class cls = object.getClass();
                ObjectConverter converter = this.converters.get(cls);
                if (converter == null) {
                    converter = generateConverter(cls);
                    this.converters.put(cls,
                            converter);
                }

                try {
                    int i = 0;
                    ctx.getAppender().append("new " + cls.getName() + "().{ ");
                    for (ObjectConverterEntry entry : converter.getFields()) {
                        if (i++ != 0) {
                            ctx.getAppender().append(", ");
                        }
                        ctx.getAppender().append(entry.getName());
                        ctx.getAppender().append(" = ");

                        marshallValue(entry.getMethod().invoke(object,
                                null),
                                entry.getType(),
                                ctx);
                    }
                }
                catch (Exception e) {
                    throw new IllegalStateException("Unable to marshall object " + object,
                            e);
                }
                ctx.getAppender().append(" }");
            }
        }

        private void marshallValue(Object object,
                                   Type type,
                                   MarshallerContext ctx) {
            if (object == null) {
                ctx.getAppender().append("null");
                return;
            }

            switch (type) {
                case PRIMITIVE: {
                    ctx.getAppender().append(object);
                    break;
                }
                case CHAR: {
                    ctx.getAppender().append("'");
                    ctx.getAppender().append(object);
                    ctx.getAppender().append("'");
                    break;
                }
                case STRING: {
                    ctx.getAppender().append("'");
                    ctx.getAppender().append(object);
                    ctx.getAppender().append("'");
                    break;
                }
                case DATE: {
                    dateMarshaller.marshall(object,
                            ctx);
                    break;
                }
                case CALENDAR: {
                    calendarMarshaller.marshall(object,
                            ctx);
                    break;
                }
                case BIG_INTEGER: {
                    ctx.getAppender().append(object);
                    break;
                }
                case BIG_DECIMAL: {
                    ctx.getAppender().append(object);
                    break;
                }
                case ARRAY: {
                    marshallArray(object,
                            ctx);
                    break;
                }
                case MAP: {
                    marshallMap((Map) object,
                            ctx);
                    break;
                }
                case COLLECTION: {
                    marshallCollection((Collection) object,
                            ctx);
                    break;
                }
                case OBJECT: {
                    marshall(object,
                            type,
                            ctx);
                    break;
                }
            }
        }

        private ObjectConverter generateConverter(Class cls) {
            BeanInfo beanInfo = null;

            try {
                beanInfo = Introspector.getBeanInfo(cls);
            }
            catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }

            PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
            List<ObjectConverterEntry> list = new ArrayList<ObjectConverterEntry>();

            for (int i = 0, length = props.length; i < length; i++) {
                PropertyDescriptor prop = props[i];
                if ("class".equals(prop.getName())) {
                    continue;
                }

                list.add(new ObjectConverterEntry(prop.getName(),
                        prop.getReadMethod(),
                        getType(prop.getPropertyType())));
            }

            return new ObjectConverter(cls,
                    list.toArray(new ObjectConverterEntry[list.size()]));
        }

        private Type getType(Class cls) {
            Type type = null;
            if (cls.isPrimitive() || Number.class.isAssignableFrom(cls)) {
                type = Type.PRIMITIVE;
            }
            else if (Character.class.isAssignableFrom(cls)) {
                type = Type.CHAR;
            }
            else if (String.class.isAssignableFrom(cls)) {
                type = Type.STRING;
            }
            else if (Date.class.isAssignableFrom(cls)) {
                type = Type.DATE;
            }
            else if (Calendar.class.isAssignableFrom(cls)) {
                type = Type.CALENDAR;
            }
            else if (BigInteger.class.isAssignableFrom(cls)) {
                type = Type.BIG_INTEGER;
            }
            else if (BigDecimal.class.isAssignableFrom(cls)) {
                type = Type.BIG_DECIMAL;
            }
            else if (cls.isArray()) {
                type = Type.ARRAY;
            }
            else if (Map.class.isAssignableFrom(cls)) {
                type = Type.MAP;
            }
            else if (Collection.class.isAssignableFrom(cls)) {
                type = Type.COLLECTION;
            }
            else {
                type = Type.OBJECT;
            }
            return type;
        }

        private void marshallMap(Map map,
                                 MarshallerContext ctx) {
            ctx.getAppender().append(" [ ");
            int i = 0;
            for (Iterator<Map.Entry> it = map.entrySet().iterator(); it.hasNext(); i++) {
                if (i != 0) {
                    ctx.getAppender().append(", ");
                }
                Map.Entry entry = it.next();
                marshall(entry.getKey(),
                        ctx);
                ctx.getAppender().append(':');
                marshall(entry.getValue(),
                        ctx);

            }
            ctx.getAppender().append(" ] ");
        }

        private void marshallCollection(Collection collection,
                                        MarshallerContext ctx) {
            ctx.getAppender().append(" [ ");
            int i = 0;
            for (Iterator it = collection.iterator(); it.hasNext(); i++) {
                if (i != 0) {
                    ctx.getAppender().append(", ");
                }
                marshall(it.next(),
                        ctx);
            }
            ctx.getAppender().append(" ] ");
        }

        private void marshallArray(Object array,
                                   MarshallerContext ctx) {
            ctx.getAppender().append(" { ");

            for (int i = 0, length = Array.getLength(array); i < length; i++) {
                if (i != 0) {
                    ctx.getAppender().append(", ");
                }
                marshall(Array.get(array,
                        i),
                        ctx);
            }
            ctx.getAppender().append(" } ");
        }

        public String marshallToString(Object object) {
            MarshallerContext ctx = new MarshallerContext(this);
            marshall(object,
                    ctx);
            return ctx.getAppender().toString();
        }
    }
}
