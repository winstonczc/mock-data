package idea.verlif.mock.data;

import com.sun.tools.javac.util.List;
import idea.verlif.mock.data.domain.Person;
import idea.verlif.mock.data.domain.SBiFunction;
import idea.verlif.mock.data.domain.SFunction;
import idea.verlif.mock.data.util.ReflectUtil;
import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description
 * @Company 广州致景科技有限公司
 * @Author chenzhicong
 * @Date 2023/2/15 14:16
 * @Version 1.0.0
 */
public class LambdaTest {

    public static <T> idea.verlif.mock.data.util.SerializedLambda resolve(SFunction<T, ?> func) {
        idea.verlif.mock.data.util.SerializedLambda lambda = idea.verlif.mock.data.util.SerializedLambda.resolve(func);
        return lambda;
    }

    public static <T, U> idea.verlif.mock.data.util.SerializedLambda resolveBi(SBiFunction<T, U, ?> func) {
        idea.verlif.mock.data.util.SerializedLambda lambda = idea.verlif.mock.data.util.SerializedLambda.resolve(func);
        return lambda;
    }

    public static <T> SFunction<T, ?> func(SFunction<T, ?> func) {
        return func;
    }

    @Test
    public void test() {
        SerializedLambda sl = ReflectUtil.getSerializedLambda(Person::getAge);
        idea.verlif.mock.data.util.SerializedLambda sl1 = resolve(Person::getName);
        idea.verlif.mock.data.util.SerializedLambda sl2 = resolve(Person::getBirthday);

        idea.verlif.mock.data.util.SerializedLambda sl3 = idea.verlif.mock.data.util.SerializedLambda.resolve(new Person()::getPlus);
        idea.verlif.mock.data.util.SerializedLambda sl4 = resolve(Person::getAge);
        idea.verlif.mock.data.util.SerializedLambda sl5 = resolve(new Person()::getPlusAge);
        idea.verlif.mock.data.util.SerializedLambda sl6 = resolve(new Person()::getPlus);
        idea.verlif.mock.data.util.SerializedLambda sl7 = resolveBi(new Person()::getPlusAge2);
        idea.verlif.mock.data.util.SerializedLambda sl8 = resolveBi(new Person()::getPlus2);
        idea.verlif.mock.data.util.SerializedLambda sl9 = resolveBi(Person::getPlus);


        SFunction<Integer, ?> f1 = new Person()::getPlusAge;
        System.out.println(f1.apply(3));

        SFunction f2 = func(new Person()::getPlusAge);
        List<Integer> list = List.of(1, 2);
        list.stream().map(f2).collect(Collectors.toList());

        SFunction<Person, ?> f3 = Person::getAge;
        List<Person> list1 = List.of(new Person(), new Person());
        list1.stream().map(f3).collect(Collectors.toList());

        System.out.println();
    }
}
