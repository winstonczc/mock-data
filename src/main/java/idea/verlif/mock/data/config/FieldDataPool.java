package idea.verlif.mock.data.config;

import idea.verlif.mock.data.domain.SFunction;
import idea.verlif.mock.data.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 属性数据池
 */
public class FieldDataPool {

    private final Map<Class<?>, PatternValues<?>> patternValuesMap;

    public FieldDataPool() {
        this.patternValuesMap = new HashMap<>();
    }

    public void addPatternValues(Class<?> cl, PatternValues<?> pv) {
        if (patternValuesMap.containsKey(cl)) {
            PatternValues<?> oldPv = patternValuesMap.get(cl);
            oldPv.addPatternValues(pv);
        } else {
            patternValuesMap.put(cl, pv);
        }
    }

    public <T> T[] getValues(Class<?> cl) {
        return getValues(cl, "");
    }

    public <T> T[] getValues(Class<?> cl, String key) {
        PatternValues<?> patternValues = patternValuesMap.get(cl);
        if (patternValues != null) {
            return (T[]) patternValues.getValues(key);
        }
        return null;
    }

    public <T> PatternValues<T> type(Class<? extends T> cl, T... values) {
        return type(cl, null, values);
    }

    public <T> PatternValues<T> type(Class<? extends T> cl, String fieldName, T... values) {
        PatternValues<T> pv = (PatternValues<T>) patternValuesMap.get(cl);
        if (pv == null) {
            pv = new PatternValues<>();
            patternValuesMap.put(cl, pv);
        }
        if (values.length == 0) {
            return pv;
        } else if (fieldName == null) {
            return pv.values(values);
        } else {
            return pv.values(values, fieldName, Pattern.CASE_INSENSITIVE);
        }
    }

    public <C, T> PatternValues<T> like(SFunction<C, T> function, T... values) {
        Field field = ReflectUtil.getFieldFromLambda(function);
        return like((Class<T>) field.getType(), field.getName(), values);
    }

    public <T> PatternValues<T> like(Class<? extends T> cl, String fieldName, T... values) {
        PatternValues<T> pv = type(cl);
        pv.values(values, ".*" + fieldName + ".*", Pattern.CASE_INSENSITIVE);
        return pv;
    }

    public <T> PatternValues<T> type(Class<? extends T> cl, Function<String, T[]> valueFun) {
        return type(cl, null, valueFun);
    }

    public <T> PatternValues<T> type(Class<? extends T> cl, String fieldName, Function<String, T[]> valueFun) {
        PatternValues<T> pv = (PatternValues<T>) patternValuesMap.get(cl);
        if (pv == null) {
            pv = new PatternValues<>();
            patternValuesMap.put(cl, pv);
        }
        if (valueFun == null) {
            return pv;
        } else if (fieldName == null) {
            return pv.value(valueFun);
        } else {
            return pv.value(valueFun, fieldName, Pattern.CASE_INSENSITIVE);
        }
    }

    public <C, T> PatternValues<T> like(SFunction<C, T> function, Function<String, T[]> valueFun) {
        Field field = ReflectUtil.getFieldFromLambda(function);
        return like((Class<T>) field.getType(), field.getName(), valueFun);
    }

    public <T> PatternValues<T> like(Class<? extends T> cl, String fieldName, Function<String, T[]> valueFun) {
        PatternValues<T> pv = type(cl);
        pv.value(valueFun, ".*" + fieldName + ".*", Pattern.CASE_INSENSITIVE);
        return pv;
    }

    public final class PatternValues<T> {

        private final ArrayList<Pattern> patterns;

        private final ArrayList<Object> values;

        public PatternValues() {
            this.patterns = new ArrayList<>();
            this.values = new ArrayList<>();
        }

        public PatternValues<T> values(T... values) {
            return values(values, ".*");
        }

        public PatternValues<T> values(T[] values, String... regexes) {
            for (String regex : regexes) {
                this.patterns.add(Pattern.compile(regex));
                this.values.add(values);
            }
            return this;
        }

        public PatternValues<T> values(T[] values, String regex, int flags) {
            this.patterns.add(Pattern.compile(regex, flags));
            this.values.add(values);
            return this;
        }

        public PatternValues<T> value(Function<String, T[]> valueFun) {
            return value(valueFun, ".*");
        }

        public PatternValues<T> value(Function<String, T[]> valueFun, String... regexes) {
            for (String regex : regexes) {
                this.patterns.add(Pattern.compile(regex));
                this.values.add(valueFun);
            }
            return this;
        }

        public PatternValues<T> value(Function<String, T[]> valueFun, String regex, int flags) {
            this.patterns.add(Pattern.compile(regex, flags));
            this.values.add(valueFun);
            return this;
        }

        public synchronized void addPatternValues(PatternValues<?> pv) {
            for (Object value : pv.values) {
                this.values.add((T[]) value);
            }
            for (Pattern pattern : pv.patterns) {
                this.patterns.add(pattern);
            }
        }

        public FieldDataPool next() {
            return FieldDataPool.this;
        }

        public boolean isMatched(String str) {
            if (patterns.size() == 0) {
                return true;
            }
            for (Pattern pattern : patterns) {
                if (pattern.matcher(str).matches()) {
                    return true;
                }
            }
            return false;
        }

        public T[] getValues(String str) {
            for (int i = patterns.size() - 1; i >= 0; i--) {
                Pattern pattern = patterns.get(i);
                if (pattern.matcher(str).matches()) {
                    T[] valArr = null;
                    Object valObj = values.get(i);
                    if (Function.class.isAssignableFrom(valObj.getClass())) {
                        valArr = (T[]) ((Function) valObj).apply(str);
                    } else {
                        valArr = (T[]) valObj;
                    }
                    return valArr;
                }
            }
            return null;
        }
    }
}
