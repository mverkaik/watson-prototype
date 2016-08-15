import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class JSoupTest {



    @Test
    public void testJSoup() throws IOException {

        /*
        Example HTML

        <tr>
             <td class="date"> Sun 11/06/2016</td>
             <td class="location"><h2><a href="/race/B3BA0FB6-FA14-482E-8A35-9A630E0FE321/world-run-day-2016-celebration--flushing">World Run Day 2016 Celebration - Flushing</a></h2>Flushing, NY</td>
             <td class="distances">1K, 5K, 8K, 10K, 15K, Half Marathon, Marathon</td>
             <td class="sponsor"><a href="http://www.tantracking.com/click.track?CID=254301&amp;AFID=196612&amp;nonencodedurl=http%3A%2F%2Fwww.active.com%2Fflushing-ny%2Frunning%2Fdistance-running-races%2Fworld-run-day-celebration-flushing-2016?cmp=39-34-196612" target="_blank"><img src="/sites/runnersworld.com/modules/custom/rw_race_finder/images/btn_black.png" width="126" height="28"></a></td>
        </tr>
         */

        Pattern LOCATION_PATTERN = Pattern.compile("</h2>([^<]*)");
        Matcher matcher;

        Document doc = Jsoup.connect("http://www.runnersworld.com/race-finder/results?miles=25&zip=10030&type=halfmarathon&date_range[start]=2016-08-06&date_range[end]=2017-08-06").get();
        Elements races = doc.select("#race-finder-search-results > tbody > tr");


        for (Element race : races) {

            String date =race.select("td.date").text();
            System.out.println("date: " + date);

            Elements raceAnchorEl = race.select("td.location h2 a");

            String name = raceAnchorEl.text();
            System.out.println("name: " + name);

            String link = raceAnchorEl.attr("href");
            System.out.println("link: " + link);

            String locationHTML = race.select("td.location").html();
            matcher = LOCATION_PATTERN.matcher(locationHTML);
            if (matcher.find()) {
                System.out.println("location: " + matcher.group(1));
            }

            String distances = race.select("td.distances").text();
            System.out.println("distances: " + distances);
        }



    }


    @Test
    public void testValueExtraction() {

        Pattern pattern = Pattern.compile("</h2>([^<]*)");
        Matcher matcher = pattern.matcher("<td class=\"location\"><h2><a href=\"/race/99313EB2-0D12-41FA-8014-DF8EC7247DE6/the-2016-end-of-summer-half-marathon\">The 2016 End of Summer Half Marathon</a></h2>Rockaway Park, NY</td>");
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }


    @Test
    public void raceDetailsTest() throws IOException {

        String toReturn = "";
        String url = "http://www.runnersworld.com/race/1965347/newport-liberty-halfmarathon";
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("div.race-finder > h1.font18");
        Element firstElement = elements.first();
        if (firstElement != null) toReturn = firstElement.toString();

        elements = doc.select("#race-info");
        firstElement = elements.first();
        if (firstElement != null) toReturn = toReturn + firstElement;

        System.out.println(toReturn);

    }

}
