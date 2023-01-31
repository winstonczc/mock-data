package idea.verlif.mock.data.creator.data;

import idea.verlif.mock.data.MockDataCreator;
import idea.verlif.mock.data.creator.DataCreator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 字典型数据构建器
 *
 * @author Verlif
 */
public class DictDataCreator<T> implements DataCreator<T> {

    private final T[] arrays;

    private final Random random;

    public DictDataCreator(T[] arrays) {
        this.arrays = arrays;
        this.random = new Random();
    }

    @Override
    public T mock(Class<?> cla, Field field, MockDataCreator.Creator creator) {
        if (arrays.length == 0) {
            return null;
        }
        return arrays[random.nextInt(arrays.length)];
    }

    @Override
    public List<Class<?>> types() {
        List<Class<?>> list = new ArrayList<>();
        Class<?> cla = arrays.getClass().getComponentType();
        list.add(cla);
        return list;
    }
}
