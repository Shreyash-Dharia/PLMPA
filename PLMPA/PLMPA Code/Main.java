/** References
 * baeldung. (2023, October 30). Baeldung (S. Nagendra, Ed.). Baeldung. https://www.baeldung.com/java-printwriter-filewriter-difference
 * FileWriter Class in Java. (2020, December 14). GeeksforGeeks. https://www.geeksforgeeks.org/filewriter-class-in-java/
 * Java ArrayList Reference. (n.d.). Www.w3schools.com. Retrieved January 12, 2025, from https://www.w3schools.com/java/java_ref_arraylist.asp
 * Java String format() Method. (n.d.). Www.w3schools.com. Retrieved January 14, 2025, from https://www.w3schools.com/java/ref_string_format.asp
 * Java String Reference. (n.d.). Www.w3schools.com. Retrieved January 12, 2025, from https://www.w3schools.com/java/java_ref_string.asp
 * Java.lang.System.currentTimeMillis() Method - Tutorialspoint. (2019). Tutorialspoint.com. https://www.tutorialspoint.com/java/lang/system_currenttimemillis.htm
 * jsoup Java HTML Parser, with the best of HTML5 DOM methods and CSS selectors. (n.d.). Jsoup.org. Retrieved January 9, 2025, from https://jsoup.org/
 * Negative binomial distribution. (2020, December 18). Wikipedia. https://en.wikipedia.org/wiki/Negative_binomial_distribution
 * Pankaj. (2022, August 3). Java List - List in Java | DigitalOcean. Www.digitalocean.com. https://www.digitalocean.com/community/tutorials/java-list
 * Programiz. (2019). Merge Sort Algorithm. Programiz.com. https://www.programiz.com/dsa/merge-sortredpunter1. (2017). R
 * Reddit. Reddit.com. https://www.reddit.com/r/SoccerBetting/comments/69g2j3/how_to_use_negative_binomial_to_predict_score/
 */

/** 
 * @since January 10th, 2024
 * @author Shreyash Dharia
 * 
 * A simple program that allows a user to use various tools to predict and analyze various seasons and teams in the English Premier League.
 * The user can predict matches, view head-to-head statistics, simulate a full season, and analyze league tables and teams for various seasons.
 * The program uses web scraping and data extract and manipulation with simple math models to provide outputs. It first scrapes the links provided 
 * to it and then stores them in CSV files. Each feature extracts the necessary information it needs from these files to make predictions and 
 * provide data from analysis.
 */

import java.util.Scanner;
import modules.*;
import scrapers.*;

public class Main {
    public static void main(String[] args) {
        // Creates an instance of each scraper and an input for the user
        LeagueTableScraper tableData = new LeagueTableScraper();
        FixtureScraper matchData = new FixtureScraper();
        Scanner input = new Scanner(System.in);

        // Scrapes for the league tables and fixtures
        tableData.scrapeLeagueTables();
        matchData.scrapeFixtures();


        // Prints the welcome and how to use message
        System.out.println("\nWelcome to the Premier League Match Predictor & Analyzer (PLMPA) \nProgram developed by Shreyash Dharia for ICS4U1. \n");
        System.out.println("This program is simple to use. The numbers on the left corresponds to the tool. Type in that number to access the tool.");
        System.out.println("You will then be prompted with text to type in the necessary information.");
        System.out.println("Once entered, the program will output what you accessed for and you repeat the process all again. \n");
        System.out.println("Note: ");
        System.out.println("1. The data used for this program is from the 2019/2020 to the 2024/2025 season.");
        System.out.println("2. All names should be written in Pascal Case (Eg. Luton Town) and seasons should be written with a space (Eg. 2024 2025).");

        // Main program loop
        while (true) {
            // Displays the menu options
            System.out.println("\n--- PL Predictor & Analysis Tools ---");
            System.out.println("   (1) Predict Match Outcome");
            System.out.println("   (2) Head-to-Head Statistics");
            System.out.println("   (3) Simulate Full Season");
            System.out.println("   (4) Analyze League Table/Teams \n");
            System.out.println("   (0) End \n");
            System.out.print("Enter your choice: ");

            // Gets the user's choice
            String choice = input.nextLine();

             // Handles the user's choice
             // Depending on the choice that feature is executed
            switch (choice) {
                case "1":
                    MatchPredictor predictor = new MatchPredictor();
                    predictor.executeFeature();
                    break;
                case "2":
                    H2HStats h2hStats = new H2HStats();
                    h2hStats.executeFeature();
                    break;
                case "3":
                    SimulateSeason simulateSeason = new SimulateSeason();
                    simulateSeason.executeFeature();
                    break;
                case "4":
                    AnalyzeTable analyzeTable = new AnalyzeTable();
                    analyzeTable.executeFeature();
                    break;
                case "0":
                    System.out.println("Ending program.");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

