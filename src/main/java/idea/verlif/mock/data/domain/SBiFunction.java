package idea.verlif.mock.data.domain;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 序列化的接口方法
 *
 * @author Verlif
 */
@FunctionalInterface
public interface SBiFunction<T, U, R> extends BiFunction<T, U, R>, Serializable {
}
