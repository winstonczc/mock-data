package idea.verlif.mock.data;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.mock.data.config.FieldDataPool;
import idea.verlif.mock.data.domain.Person;
import idea.verlif.mock.data.example.PropertiesDataPool;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class SimpleTest {

    @Test
    public void test() throws IOException, Exception {
        PropertiesDataPool dataPool = new PropertiesDataPool();
        dataPool.load("src/test/resources/data-pool.properties");

        dataPool.type(LocalDateTime.class, f -> new LocalDateTime[]{LocalDateTime.now()});

        MockDataCreator creator = new MockDataCreator();
        creator.getConfig().forceNew(true).autoCascade(true).fieldDataPool(dataPool);

        Person p = new Person();
        p.setCreatedTime(null);
        for (int i = 0; i < 5; i++) {
            System.out.println(JSONObject.toJSONString(creator.mock(Person.class)));
            Thread.sleep(1000L);
        }
    }

}
