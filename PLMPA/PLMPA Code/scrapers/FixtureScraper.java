package scrapers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/** A web scraper for Premier League scores and fixtures from 2019/2020 to the current season, 2024/2025.
 * It first connects to each link, parses the HTML code for the fixtures and extracts it to a CSV file.
 * The CSV files holds all the data necessary for the different features.
 */
public class FixtureScraper {

    /** Scrapes the provided link and stores scores and fixtures data for multiple seasons in a CSV file
     */
    public void scrapeFixtures() {

        // The list of the URLs used for the data scraping
        List<String> urls = Arrays.asList(
                "https://fbref.com/en/comps/9/schedule/Premier-League-Scores-and-Fixtures",
                "https://fbref.com/en/comps/9/2023-2024/schedule/2023-2024-Premier-League-Scores-and-Fixtures",
                "https://fbref.com/en/comps/9/2022-2023/schedule/2022-2023-Premier-League-Scores-and-Fixtures",
                "https://fbref.com/en/comps/9/2021-2022/schedule/2021-2022-Premier-League-Scores-and-Fixtures",
                "https://fbref.com/en/comps/9/2020-2021/schedule/2020-2021-Premier-League-Scores-and-Fixtures",
                "https://fbref.com/en/comps/9/2019-2020/schedule/2019-2020-Premier-League-Scores-and-Fixtures");

        // The of the HTML ID needed to scrape the exact data needed
        List<String> tableIDs = Arrays.asList(
                "sched_2024-2025_9_1",
                "sched_2023-2024_9_1",
                "sched_2022-2023_9_1",
                "sched_2021-2022_9_1",
                "sched_2020-2021_9_1",
                "sched_2019-2020_9_1");

        // The name of the file where the data will be stored
        String outputFileName = "data/Fixtures.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            writer.println("Season,Home Team,Home xG,Home Score,Away Score,Away xG,Away Team");

            // Goes through the list of URls and Table IDs
            for (int index = 0; index < urls.size(); index++) {
                String url = urls.get(index);
                String tableID = tableIDs.get(index);

                // Initializes the request count and time started
                int requestCount = 0;
                long startTime = System.currentTimeMillis();

                // If the amount of server requests is 10 or greater then it calculates the time remaining (The website only allows 10 requests/min)
                if (requestCount >= 10) {
                    long elapsedTime = System.currentTimeMillis() - startTime;

                    // Pausing the program until the minute is complete
                    if (elapsedTime <= 60000) {
                        Thread.sleep(60000 - elapsedTime);
                    }

                    // Resets the counter and timer
                    requestCount = 0;
                    startTime = System.currentTimeMillis();
                }

                // Connects to the URL and increases the request count by 1
                Document doc = Jsoup.connect(url).get();
                requestCount++;

                // Gets the season by splitting the URL
                String season;
                if (index == 0) {
                    season = "2024 2025"; // Explicitly sets for the first URL
                } else {
                    season = url.split("/")[6].replace("-", " ");
                }

                // The table gets selected from where the data is being scraped from
                Element table = doc.select("table.stats_table#" + tableID).first();

                if (table != null) {
                    
                    // Selects all the rows from the HTML tags within <tbody> in <tr>
                    Elements rows = table.select("tbody > tr");
                    for (Element row: rows) {
                        StringBuilder rowData = new StringBuilder();
                        rowData.append(season).append(",");

                        // Selects all the columns from the HTML tags <td>
                        Elements columns = row.select("td");

                        // Gets the values from the 3rd column in the table
                        String homeTeam = replaceText(columns.get(3).text());
                        rowData.append(homeTeam).append(",");

                        // Gets the values from the 4th column in the table
                        String homeXG = replaceText(columns.get(4).text());
                        rowData.append(homeXG).append(",");

                        // Gets the values from the 5th column in the table
                        String score = replaceText(columns.get(5).text());
                        String homeScore = "N/A";
                        String awayScore = "N/A";

                        // The score is split for each respective team
                        if (score.length() > 1) {
                            homeScore = score.substring(0, 1);
                            awayScore = score.substring(1);
                        }

                        rowData.append(homeScore).append(",");
                        rowData.append(awayScore).append(",");

                        // Gets the values from the 6th column in the table
                        String awayXG = replaceText(columns.get(6).text());
                        rowData.append(awayXG).append(",");

                        // Gets the values from the 7th column in the table
                        String awayTeam = replaceText(columns.get(7).text());
                        rowData.append(awayTeam);

                        // All the appended data in the rowData is added to the CSV file
                        writer.println(rowData.toString());
                    }
                } else {
                    System.out.println("Table not found on page: " + url);
                }
            }
            System.out.println("Fixture data scraped and successfully saved to " + outputFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /** Replaces all non-ASCII character to ensure the program can read and work with the data properly
     * @param text Gets the string that needs characters replaced
     * @return string with replaced, readable characters
     */
    private String replaceText(String text) {
        return text.replaceAll("[^\\x00-\\x7F]", "").trim();
    }
}