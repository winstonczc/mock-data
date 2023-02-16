package idea.verlif.mock.data;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.mock.data.domain.Person;
import idea.verlif.mock.data.example.PropertiesDataPool;
import org.junit.Test;

import java.io.IOException;

public class SimpleTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        PropertiesDataPool dataPool = new PropertiesDataPool();
        //        dataPool.load("src/test/resources/data-pool.properties");
        dataPool.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("data-pool.properties"));
        MockDataCreator mdcreator = new MockDataCreator();
        mdcreator.getConfig()
                .forceNew(true)
                .autoCascade(true)
                .creatingDepth(2)
                .setFieldDataPool(dataPool);
        System.out.println(JSONObject.toJSONString(mdcreator.mock(Person.class)));

        MockDataCreator.Creator creator = mdcreator.new Creator(mdcreator.getConfig());
        creator.getDataCreator(Person.class);
    }

}
