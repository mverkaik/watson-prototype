import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ObjectMapperTest {

    @Test
    public void objectMapperTest() throws IOException {

        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("foo", "bar");
        testMap.put("hello", "world");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(testMap));
        mapper.writeValueAsString(testMap);
    }
}
