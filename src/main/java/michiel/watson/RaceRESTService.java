package michiel.watson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ibm.watson.developer_cloud.util.MediaType.APPLICATION_JSON;
import static com.ibm.watson.developer_cloud.util.MediaType.TEXT_HTML;

/**
 * REST service to find races
 */
@Path("/races")
public class RaceRESTService {

    /** URL template to query Runner's World db */
    private static final String URL_TEMPLATE = "http://www.runnersworld.com/race-finder/results" +
            "?miles=25&zip={ZIP}&type={TYPE}&date_range[start]={START}&date_range[end]={END}";

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    /** REGEX to find race location in HTML */
    private static final Pattern LOCATION_PATTERN = Pattern.compile("</h2>([^<]*)");

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get start date as formatted string (yyy-MM-dd)
     *
     * @return
     */
    private String getStartDate() {

        return dateFormat.format(new Date());
    }

    /**
     * Get end date (90 days from current date) as formatted string (yyy-MM-dd)
     *
     * @return
     */
    private String getEndDate() {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 90);
        return dateFormat.format(now.getTime());
    }

    /**
     * Construct a search URL
     *
     * @param type
     * @param zip
     * @return
     */
    private String getSearchURL(String type, Integer zip) {
        String url = URL_TEMPLATE;
        url = url.replace("{TYPE}", type);
        url = url.replace("{ZIP}", zip.toString());
        url = url.replace("{START}", getStartDate());
        url = url.replace("{END}", getEndDate());
        System.out.println("url = " + url);
        return url;
    }


    /**
     * Find races within some parameters
     *
     * @param type
     * @param zip
     * @param miles
     * @param startDate
     * @param endDate
     * @return
     * @throws IOException
     */
    @GET
    @Produces(APPLICATION_JSON)
    public List<Race> findRaces(@QueryParam("type") String type,
                          @QueryParam("zip") Integer zip,
                          @QueryParam("miles") Integer miles,
                          @QueryParam("startDate") String startDate,
                          @QueryParam("endDate") String endDate) throws IOException {

        List<Race> races = new ArrayList<Race>();
        Race race;

        Matcher matcher;
        StringBuilder sb = new StringBuilder();

        String searchURL = getSearchURL(type, zip);
        Document doc = Jsoup.connect(searchURL).userAgent(USER_AGENT).get();
        Elements raceEls = doc.select("#race-finder-search-results > tbody > tr");

        for (Element raceEl : raceEls) {

            race = new Race();

            race.setDate(raceEl.select("td.date").text());

            Elements raceAnchorEl = raceEl.select("td.location h2 a");

            race.setName(raceAnchorEl.text());
            race.setLink(raceAnchorEl.attr("href"));

            String locationHTML = raceEl.select("td.location").html();
            matcher = LOCATION_PATTERN.matcher(locationHTML);
            if (matcher.find()) {
                race.setLocation(matcher.group(1));
            }

            race.setDistances(raceEl.select("td.distances").text());

            races.add(race);
        }

        return races;
    }


    /**
     * Get race info
     *
     * @param id The race identifier
     * @return HTML fragment of race info
     * @throws IOException
     */
    @GET
    @Path("/race")
    @Produces(TEXT_HTML)
    public String getRaceInfo(@QueryParam("id") String id) throws IOException {

        String toReturn = "";
        String url = "http://www.runnersworld.com/race/" + id;

        System.out.println("Getting race info from: " + url);

        Document doc = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .get();

        Elements elements = doc.select("div.race-finder > h1.font18");
        Element firstElement = elements.first();
        if (firstElement != null) toReturn = firstElement.toString();

        elements = doc.select("#race-info");
        firstElement = elements.first();
        if (firstElement != null) toReturn = toReturn + "\n" + firstElement;

        return toReturn;
    }

}
