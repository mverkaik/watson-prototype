import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyDataNews;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentsResult;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Alchemy Data News tests
 */
public class AlchemyDataNewsTest {

    AlchemyDataNews service = new AlchemyDataNews("0cd8968f720ce65ab96b8b3807172d2ba71b6f83");
    Map<String, Object> params = new HashMap<String, Object>();

    @Test
    public void alchemyDataNewsTest() {

        String[] fields =
                new String[] {"enriched.url.title", "enriched.url.url", "enriched.url.author",
                        "enriched.url.publicationDate", "enriched.url.enrichedTitle.entities",
                        "enriched.url.enrichedTitle.docSentiment"};

    params.put(AlchemyDataNews.RETURN, StringUtils.join(fields, ","));

    params.put(AlchemyDataNews.START, "1440720000");
    params.put(AlchemyDataNews.END, "1441407600");
    params.put(AlchemyDataNews.COUNT, 7);

    DocumentsResult result = service.getNewsDocuments(params).execute();

    System.out.println(result);
    }

}
