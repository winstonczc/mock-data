package idea.verlif.mock.data.creator;

/**
 * 实例构造器
 *
 * @author Verlif
 */
public interface InstanceCreator<T> {

    Class<? extends T> matched();

    T newInstance();
}
