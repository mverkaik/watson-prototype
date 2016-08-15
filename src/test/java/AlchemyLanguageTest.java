import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.CombinedResults;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Concepts;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Alchemy Language tests
 */
public class AlchemyLanguageTest {

    AlchemyLanguage alchemyLanguage = new AlchemyLanguage("0cd8968f720ce65ab96b8b3807172d2ba71b6f83");
    Map<String, Object> params = new HashMap<String, Object>();

    @Test
    public void testSentimentAnalysis() {

        params.put(AlchemyLanguage.TEXT, "IBM Watson won the Jeopardy television show hosted by Alex Trebek");
        DocumentSentiment sentiment = alchemyLanguage.getSentiment(params).execute();
        System.out.println(sentiment);
    }

    @Test
    public void testSentimentAnalysisFromUrl() {

        params.put(AlchemyLanguage.URL,
                "http://www.nycrunningmama.com/2014/10/13/2014-staten-island-half-marathon-race-recap/");
        DocumentSentiment sentiment = alchemyLanguage.getSentiment(params).execute();
        System.out.println(sentiment);

    }

    @Test
    public void testEntityExtraction() {

        params.put(AlchemyLanguage.URL,
                "https://en.wikipedia.org/wiki/Amsterdam");
        Concepts concepts = alchemyLanguage.getConcepts(params).execute();
        System.out.println(concepts);
    }

    @Test
    public void testCombinedResults() {

        params.put(AlchemyLanguage.URL,
                "http://findmymarathon.com/race-detail.php?zname=Yonkers%20Marathon&zdistance=16");
        CombinedResults combinedResults = alchemyLanguage.getCombinedResults(params).execute();
        System.out.println(combinedResults);
    }

}
