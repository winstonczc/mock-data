package idea.verlif.mock.data.config;

import idea.verlif.mock.data.creator.DataCreator;
import idea.verlif.mock.data.creator.InstanceCreator;
import idea.verlif.mock.data.domain.MockField;
import idea.verlif.mock.data.domain.SFunction;
import idea.verlif.mock.data.util.NamingUtil;
import idea.verlif.mock.data.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Verlif
 */
public class MockDataConfig {

    /**
     * 属性填充的循环次数
     */
    private int creatingDepth = 3;

    /**
     * 构建数组时的填充长度
     */
    private int arraySize = 5;

    /**
     * 属性创造器表
     */
    private final Map<String, DataCreator<?>> fieldCreatorMap;

    /**
     * 实例构造器表
     */
    private final Map<String, InstanceCreator<?>> instanceCreatorMap;

    /**
     * 级联构造列表
     */
    private final Set<String> cascadeCreateSet;

    /**
     * 构造时忽略的属性
     */
    private final Set<String> ignoredFiledSet;

    /**
     * 允许构建private关键字
     */
    private int modifiers = Modifier.PRIVATE;

    /**
     * 强制生成新对象
     */
    private boolean forceNew = false;

    public MockDataConfig() {
        fieldCreatorMap = new HashMap<>();
        instanceCreatorMap = new HashMap<>();
        cascadeCreateSet = new HashSet<>();
        ignoredFiledSet = new HashSet<>();
    }

    public int getCreatingDepth() {
        return creatingDepth;
    }

    public void setCreatingDepth(int circleCount) {
        this.creatingDepth = circleCount;
    }

    public boolean isAllowPrivate() {
        return Modifier.isPrivate(modifiers);
    }

    public void setAllowPrivate(boolean allowPrivate) {
        this.modifiers = allowPrivate ? this.modifiers & Modifier.PRIVATE : this.modifiers ^ Modifier.PRIVATE;
    }

    public boolean isAllowPublic() {
        return Modifier.isPublic(modifiers);
    }

    public void setAllowPublic(boolean allowPublic) {
        this.modifiers = allowPublic ? this.modifiers | Modifier.PUBLIC : this.modifiers ^ Modifier.PUBLIC;
    }

    public boolean isAllowProtect() {
        return Modifier.isProtected(modifiers);
    }

    public void setAllowProtect(boolean allowProtect) {
        this.modifiers = allowProtect ? this.modifiers | Modifier.PROTECTED : this.modifiers ^ Modifier.PROTECTED;
    }

    public boolean isAllowStatic() {
        return Modifier.isStatic(modifiers);
    }

    public void setAllowStatic(boolean allowStatic) {
        this.modifiers = allowStatic ? this.modifiers | Modifier.STATIC : this.modifiers ^ Modifier.STATIC;
    }

    public void setAllowedModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * 添加允许的属性修饰符
     *
     * @param modifiers 属性修饰符
     */
    public void addAllowedModifiers(int... modifiers) {
        for (int modifier : modifiers) {
            this.modifiers |= modifier;
        }
    }

    /**
     * 移除允许的属性修饰符
     *
     * @param modifiers 属性修饰符
     */
    public void removeAllowedModifiers(int... modifiers) {
        for (int modifier : modifiers) {
            this.modifiers -= modifier;
        }
    }

    public boolean isAllowedModifier(int mod) {
        return (mod & modifiers) != 0;
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public boolean isForceNew() {
        return forceNew;
    }

    public void setForceNew(boolean forceNew) {
        this.forceNew = forceNew;
    }

    public <T> DataCreator<T> getDataCreator(MockField field) {
        return getDataCreator(field.getKey());
    }

    public <T> DataCreator<T> getDataCreator(String key) {
        return (DataCreator<T>) fieldCreatorMap.get(key);
    }

    /**
     * 添加或替换属性数据创造器
     *
     * @param function 属性获取表达式
     * @param creator  数据创造器
     */
    public <T> void addFieldCreator(SFunction<T, ?> function, DataCreator<?> creator) {
        Field field = ReflectUtil.getFieldFromLambda(function, true);
        MockField mf = new MockField(field);
        addFieldCreator(mf.getKey(), creator);
    }

    /**
     * 添加或替换属性数据创造器
     *
     * @param key     属性key值
     * @param creator 数据创造器
     */
    public <T> void addFieldCreator(String key, DataCreator<?> creator) {
        fieldCreatorMap.put(
                key,
                creator);
    }

    /**
     * 添加或替换属性数据创造器
     *
     * @param cla     目标类
     * @param creator 数据创造器
     */
    public <T> void addFieldCreator(Class<?> cla, DataCreator<?> creator) {
        fieldCreatorMap.put(
                NamingUtil.getKeyName(cla),
                creator);
    }

    /**
     * 是否拥有独立属性构造器
     *
     * @param key 属性key
     */
    public boolean hasFiledCreator(String key) {
        return fieldCreatorMap.containsKey(key);
    }

    /**
     * 添加实例构造器
     *
     * @param creator 实例构造器
     */
    public void addInstanceCreator(InstanceCreator<?> creator) {
        Class<?> cla = creator.matched();
        if (cla != null) {
            instanceCreatorMap.put(NamingUtil.getKeyName(cla), creator);
        }
    }

    /**
     * 获取实例构造器
     *
     * @param cla 实例类
     * @param <T> 实例类
     * @return 实例类对应实例构造器
     */
    public <T> InstanceCreator<T> getInstanceCreator(Class<T> cla) {
        return (InstanceCreator<T>) instanceCreatorMap.get(NamingUtil.getKeyName(cla));
    }

    /**
     * 添加级联构造的类
     *
     * @param cla 需要级联构造的类
     */
    public void addCascadeCreateKey(Class<?> cla) {
        addCascadeCreateKey(NamingUtil.getKeyName(cla));
    }

    /**
     * 添加级联构造的属性
     *
     * @param function 需要级联构造的属性
     */
    public void addCascadeCreateKey(SFunction<?, ?> function) {
        addCascadeCreateKey(NamingUtil.getKeyName(ReflectUtil.getFieldFromLambda(function, true)));
    }

    /**
     * 添加级联构造的key
     *
     * @param key 需要级联构造的key
     */
    public void addCascadeCreateKey(String key) {
        cascadeCreateSet.add(key);
    }

    /**
     * 移除级联构造的类
     *
     * @param cla 需要级联构造的类
     */
    public void removeCascadeCreateKey(Class<?> cla) {
        removeCascadeCreateKey(NamingUtil.getKeyName(cla));
    }

    /**
     * 移除级联构造的属性
     *
     * @param function 需要级联构造的属性
     */
    public void removeCascadeCreateKey(SFunction<?, ?> function) {
        removeCascadeCreateKey(NamingUtil.getKeyName(ReflectUtil.getFieldFromLambda(function, true)));
    }

    /**
     * 移除级联构造的key
     *
     * @param key 需要级联构造的key
     */
    public void removeCascadeCreateKey(String key) {
        cascadeCreateSet.remove(key);
    }

    /**
     * 查询属性是否级联构造
     *
     * @param key 目标key
     * @return 目标key是否级联构造
     */
    public boolean isCascadeCreate(String key) {
        return cascadeCreateSet.contains(key);
    }

    /**
     * 增加忽略的属性
     */
    public <T> void addIgnoredField(SFunction<T, ?> function) {
        ignoredFiledSet.add(NamingUtil.getKeyName(ReflectUtil.getFieldFromLambda(function)));
    }

    /**
     * 增加忽略的属性
     */
    public void addIgnoredField(Class<?> cla) {
        ignoredFiledSet.add(NamingUtil.getKeyName(cla));
    }

    /**
     * 判断该属性是否是被忽略的
     *
     * @param key 属性key
     */
    public boolean isIgnoredFiled(String key) {
        return ignoredFiledSet.contains(key);
    }

    /**
     * 属性是否被允许构建
     *
     * @param field 属性对象
     */
    public boolean isAllowField(Field field) {
        return (field.getModifiers() | modifiers) == modifiers;
    }
}
