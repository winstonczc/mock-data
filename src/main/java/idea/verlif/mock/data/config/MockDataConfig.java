package idea.verlif.mock.data.config;

import idea.verlif.mock.data.domain.SFunction;
import idea.verlif.mock.data.domain.counter.StringCounter;
import idea.verlif.mock.data.util.NamingUtil;
import idea.verlif.mock.data.util.ReflectUtil;

/**
 * @author Verlif
 */
public class MockDataConfig extends CommonConfig {

    private static final int ARRAY_SIZE = 5;
    private static final int DEFAULT_DEPTH = 2;

    /**
     * 属性填充的循环次数
     */
    private StringCounter maxDepthCounter;

    /**
     * 构建数组时的填充长度
     */
    private SizeCreator arraySizeCreator;

    /**
     * 自动级联构建
     */
    private boolean autoCascade;

    /**
     * 强制生成新对象。<br>
     * 例如基础属性在声明时就存在默认对象，此时在forceNew为false的情况下就会被忽略。
     */
    private boolean forceNew = false;

    public MockDataConfig copy() {
        MockDataConfig config = new MockDataConfig();
        config.maxDepthCounter = this.maxDepthCounter;
        config.arraySizeCreator = this.arraySizeCreator;
        config.fieldCreatorMap.putAll(this.fieldCreatorMap);
        config.interfaceCreatorMap.putAll(this.interfaceCreatorMap);
        config.instanceCreatorMap.putAll(this.instanceCreatorMap);
        config.autoCascade = this.autoCascade;
        config.cascadeCreateSet.addAll(this.cascadeCreateSet);
        config.cascadeCreatePattern.addAll(this.cascadeCreatePattern);
        config.fieldFilters.addAll(this.fieldFilters);
        config.classFilters.addAll(this.classFilters);
        config.forceNew = this.forceNew;
        config.fieldDataPool = this.fieldDataPool;

        return config;
    }

    public int getMaxCreatingDepth(String key) {
        if (maxDepthCounter == null) {
            return DEFAULT_DEPTH;
        }
        return maxDepthCounter.getCount(key);
    }

    public MockDataConfig creatingDepth(int defaultDepth) {
        if (maxDepthCounter == null) {
            maxDepthCounter = new StringCounter(defaultDepth);
        } else {
            maxDepthCounter.setDefaultCount(defaultDepth);
        }
        return this;
    }

    /**
     * 设定构建深度
     *
     * @param function 属性表达式
     * @param depth    属性的构建深度
     */
    public <T> MockDataConfig creatingDepth(SFunction<T, ?> function, int depth) {
        setKeyMaxDepth(NamingUtil.getKeyName(ReflectUtil.getFieldFromLambda(function)), depth);
        return this;
    }

    /**
     * 设定构建深度
     *
     * @param cla   目标类
     * @param depth 目标类的构建深度
     */
    public MockDataConfig creatingDepth(Class<?> cla, int depth) {
        setKeyMaxDepth(NamingUtil.getKeyName(cla), depth);
        return this;
    }

    private void setKeyMaxDepth(String key, int depth) {
        if (maxDepthCounter == null) {
            maxDepthCounter = new StringCounter(DEFAULT_DEPTH);
        }
        maxDepthCounter.setCount(key, depth);
    }

    public int getArraySize(Class<?> cla) {
        if (arraySizeCreator == null) {
            return ARRAY_SIZE;
        }
        return arraySizeCreator.getSize(cla);
    }

    public MockDataConfig arraySize(int arraySize) {
        this.arraySizeCreator = new StaticSizeCreator(arraySize);
        return this;
    }

    public MockDataConfig arraySize(SizeCreator sizeCreator) {
        this.arraySizeCreator = sizeCreator;
        return this;
    }

    public boolean isForceNew() {
        return forceNew;
    }

    public MockDataConfig forceNew(boolean forceNew) {
        this.forceNew = forceNew;
        return this;
    }

    /**
     * 设置自动级联构造标识
     *
     * @param autoCascade 是否自动级联构造
     */
    public MockDataConfig autoCascade(boolean autoCascade) {
        this.autoCascade = autoCascade;
        return this;
    }

    /**
     * 查询属性是否级联构造
     *
     * @param key 目标key
     * @return 目标key是否级联构造
     */
    @Override
    public boolean isCascadeCreate(String key) {
        return autoCascade || super.isCascadeCreate(key);
    }

    /**
     * 固定大小创建器
     */
    private static final class StaticSizeCreator implements SizeCreator {

        private final int size;

        public StaticSizeCreator(int size) {
            this.size = size;
        }

        @Override
        public int getSize(Class<?> cla) {
            return size;
        }
    }

}
