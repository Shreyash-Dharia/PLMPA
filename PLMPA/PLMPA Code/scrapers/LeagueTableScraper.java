package scrapers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/** A web scraper for Premier League tables from 2019/2020 to the current season, 2024/2025.
 * It first connects to each link, parses the HTML code for the table and extracts it to a CSV file.
 * The CSV files holds all the data necessary for the different features.
 */
public class LeagueTableScraper {

    /** Scrapes the provided link and stores the league table data for multiple seasons in a CSV file
     */
    public void scrapeLeagueTables() {
        // The list of the URLs used for the data scraping
        List<String> urls = Arrays.asList(
                "https://fbref.com/en/comps/9/Premier-League-Stats",
                "https://fbref.com/en/comps/9/2023-2024/2023-2024-Premier-League-Stats",
                "https://fbref.com/en/comps/9/2022-2023/2022-2023-Premier-League-Stats",
                "https://fbref.com/en/comps/9/2021-2022/2021-2022-Premier-League-Stats",
                "https://fbref.com/en/comps/9/2020-2021/2020-2021-Premier-League-Stats",
                "https://fbref.com/en/comps/9/2019-2020/2019-2020-Premier-League-Stats");

        // The of the HTML ID needed to scrape the exact data needed
        List<String> tableIDs = Arrays.asList(
                "results2024-202591_overall",
                "results2023-202491_overall",
                "results2022-202391_overall",
                "results2021-202291_overall",
                "results2020-202191_overall",
                "results2019-202091_overall");

        // The name of the file where the data will be stored
        String outputFileName = "data/League Tables.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {
            writer.println("Season,Rk,Squad,MP,W,D,L,GF,GA,GD,Pts,Pts/MP,xG,xGA,xGD,xGD/90");

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

                // Gets the season by checking if it's the first URL, otherwise extract season as usual
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
                    for (Element row : rows) {
                        StringBuilder rowData = new StringBuilder();
                        rowData.append(season).append(",");

                        // Selects all the columns from the HTML tags within <th> and <td>
                        Elements columns = row.select("th, td");

                        // Loops through the first 15 columns and extract each rows data from each column
                        for (int columnNum = 0; columnNum < 15; columnNum++) {
                            Element column = columns.get(columnNum);
                            rowData.append(column.text().replace(",", ""));

                            // Adds a comma beside all the data value and skips the last value
                            if (columnNum < 14) {
                                rowData.append(",");
                            }
                        }

                        // All the appended data in the rowData is added to the CSV file
                        writer.println(rowData.toString());
                    }
                } else {
                    System.out.println("Table not found on page: " + url);
                }
            }
            System.out.println("League table data scraped and successfully saved to " + outputFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}