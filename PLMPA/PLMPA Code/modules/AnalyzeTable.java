package modules;
import java.util.*;

/** An analyze league table and team feature where users can analyze league stats from various seasons and league stats from different teams.
 * The feature takes in data from League Tables.csv and uses it to print out the stats.
 */
public class AnalyzeTable extends BaseFeatures {

    // Instance variables to track stats
    private String season;
    private String squad;

    private int rank;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;

    private double pointsPerMatch;
    private double xG;
    private double xGA;
    private double xGD;
    private double xGDPer90;


    /** Gets the season associated with the team
     * @return the year of the season
     */
    public String getSeason() {
        return season;
    }

    /** Gets the rank of the team in the league
     * @return the team's rank
     */
    public int getRank() {
        return rank;
    }

    /** Gets the number of matches played by the team
     * @return the number of matches played
     */
    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    /** Gets the number of wins achieved by the team
     * @return the number of wins
     */
    public int getWins() {
        return wins;
    }

    /** Gets the number of draws the team has had
     * @return the number of draws
     */
    public int getDraws() {
        return draws;
    }

    /** Gets the squad name of  the team
     * @return the squad name
     */
    public String getSquad() {
        return squad;
    }
    
    /** Get the number of losses the team has suffered
     * @return the number of losses
     */
    public int getLosses() {
        return losses;
    }

    /** Get the number of goals scored by the team
     * @return the number of goals scored by the team
     */
    public int getGoalsFor() {
        return goalsFor;
    }

    /** Gets the number of goals conceded by the team
     * @return the number of goals conceded
     */
    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    /** Gets the goal difference for the team
     * @return the goal difference
     */
    public int getGoalDifference() {
        return goalDifference;
    }

    /** Gets the total points achieved by the team
     * @return the team's points
     */
    public int getPoints() {
        return points;
    }

    /** Gets the points per match ratio for the team
     * @return the points per match ratio
     */
    public double getPointsPerMatch() {
        return pointsPerMatch;
    }

    /** Gets the expected goals for the team
     * @return the expected goals
     */
    public double getxG() {
        return xG;
    }

    /** Gets the expected goals against for the team
     * @return the expected goals against
     */
    public double getxGA() {
        return xGA;
    }

    /** Gets the expected goal difference for the team.
     * @return the expected goal difference
     */
    public double getxGD() {
        return xGD;
    }

    /** Gets the expected goal difference per 90 minutes for the team.
     * @return the expected goal difference per 90 minutes
     */
    public double getxGDPer90() {
        return xGDPer90;
    }


    /** Sets the season associated with the team
     * @param season the season to set 
     */
    public void setSeason(String season) {
        this.season = season;
    }

    /** Sets the squad details for the team
     * @param squad the squad to set
     */
    public void setSquad(String squad) {
        this.squad = squad;
    }

    /** Sets the rank of the team in the league
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /** Sets the number of matches played by the team
     * @param matchesPlayed the number of matches played to set
     */
    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    /** Sets the number of wins achieved by the team
     * @param wins the number of wins to set
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /** Sets the number of draws the team has had
     * @param draws the number of draws to set
     */
    public void setDraws(int draws) {
        this.draws = draws;
    }

    /** Sets the number of losses suffered by the team
     * @param losses the number of losses to set
     */
    public void setLosses(int losses) {
        this.losses = losses;
    }

    /** Sets the number of goals scored by the team.
     * @param goalsFor the number of goals to set
     */
    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    /** Sets the number of goals conceded by the team
     * @param goalsAgainst the number of goals conceded to set
     */
    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    /** Sets the goal difference for the team
     * @param goalDifference the goal difference to set
     */
    public void setGoalDifference(int goalDifference) {
        this.goalDifference = goalDifference;
    }

    /** Sets the total points accumulated by the team
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /** Sets the points per match ratio for the team
     * @param pointsPerMatch the points per match to set
     */
    public void setPointsPerMatch(double pointsPerMatch) {
        this.pointsPerMatch = pointsPerMatch;
    }

    /** Sets the expected goals for the team.
     * @param xG the expected goals to set
     */
    public void setxG(double xG) {
        this.xG = xG;
    }

    /** Sets the expected goals against for the team
     * @param xGA the expected goals against to set
     */
    public void setxGA(double xGA) {
        this.xGA = xGA;
    }

    /** Sets the expected goal difference for the team
     * @param xGD the expected goal difference to set
     */
    public void setxGD(double xGD) {
        this.xGD = xGD;
    }

    /** Sets the expected goal difference per 90 minutes for the team
     * @param xGDPer90 the expected goal difference per 90 minutes to set
     */
    public void setxGDPer90(double xGDPer90) {
        this.xGDPer90 = xGDPer90;
    }


    /** Loads and parses the league table data from a CSV file
     * @param filePath the path to the CSV file containing league data
     * @param seasonFilter the season data to load and parse
     * @return an ArrayList of AnalyzeTable objects containing the league data
     */
    public ArrayList<AnalyzeTable> loadLeagueTable(String filePath, String seasonFilter) {
        ArrayList<AnalyzeTable> rows = new ArrayList<>();

        // Reads the data from the CSV file
        ArrayList<String[]> data = readCSV(filePath, true, seasonFilter);
        
        // Iterates over each row of data to create AnalyzeTable objects
        for (String[] row : data) {
            try {

                // Extracts stats
                String season = row[0];
                String[] stats = new String[row.length - 1];
                for (int i = 1; i < row.length; i++) {
                    stats[i - 1] = row[i];
                }               

                // Adds the created AnalyzeTable object to the rows list
                rows.add(createStats(season, stats));

            } catch (Exception e) {}
        }
        return rows;
    }


    /** Creates an AnalyzeTable object from raw league data.
     * @param season the season for which stats are being created
     * @param data an array of stats for the team in that season
     * @return an AnalyzeTable object containing the parsed stats
     */
    private AnalyzeTable createStats(String season, String[] data) {
        AnalyzeTable stats = new AnalyzeTable();
        
        // Set the season and various stats values in the AnalyzeTable object
        stats.setSeason(season);
        stats.setRank(Integer.parseInt(data[0]));
        stats.setSquad(data[1]);
        stats.setMatchesPlayed(Integer.parseInt(data[2]));
        stats.setWins(Integer.parseInt(data[3]));
        stats.setDraws(Integer.parseInt(data[4]));
        stats.setLosses(Integer.parseInt(data[5]));
        stats.setGoalsFor(Integer.parseInt(data[6]));
        stats.setGoalsAgainst(Integer.parseInt(data[7]));
        stats.setGoalDifference(Integer.parseInt(data[8]));
        stats.setPoints(Integer.parseInt(data[9]));
        stats.setPointsPerMatch(Double.parseDouble(data[10]));
        stats.setxG(Double.parseDouble(data[11]));
        stats.setxGA(Double.parseDouble(data[12]));
        stats.setxGD(Double.parseDouble(data[13]));
        stats.setxGDPer90(Double.parseDouble(data[14]));
        
        return stats;
    }


    /** Formats the team stats
     * @param stats the AnalyzeTable object containing the team stats to be formatted
     * @return a formatted string of the team stats
     */
    private String formatStats(AnalyzeTable stats) {

        // Format the stats with padding and alignment for each column
        return String.format(
                "%-5d %-20s %-3d %-3d %-3d %-3d %-3d %-3d %-3d %-3d %-7.2f %-7.2f %-7.2f %-7.2f %-7.2f",
                stats.getRank(), stats.getSquad(), stats.getMatchesPlayed(), stats.getWins(), stats.getDraws(), stats.getLosses(),
                stats.getGoalsFor(), stats.getGoalsAgainst(), stats.getGoalDifference(), stats.getPoints(), stats.getPointsPerMatch(),
                stats.getxG(), stats.getxGA(), stats.getxGD(), stats.getxGDPer90());
    }


    /** Asks the user for the season and prints that season table
     * @param leagueTable the list of AnalyzeTable objects containing league data
     * @param input the Scanner object to read user input
     */
    private void handleViewSeason(ArrayList<AnalyzeTable> leagueTable, Scanner input) {

        while (true) {

            // Asks the user for a season
            String season = getValidSeason(input, "Enter the season (Eg. 2024 2025): ");

            // Prints the league table header for the entered season
            System.out.printf("\n--- League Table for %s ---\n", season);
            System.out.println("Rk    Squad                MP  W   D   L   GF  GA  GD  Pts Pts/MP  xG      xGA     xGD     xGD/90");

            // Iterates through each row in the league table and print stats for the matching season
            for (AnalyzeTable row : leagueTable) {
                if (row.getSeason().equals(season)) {
                    System.out.println(formatStats(row)); 
                }
            }
            break; 
        }
    }


    /** Asks the user for the teams and seasons and prints out the stats for them
     * @param leagueTable the list of AnalyzeTable objects containing league data
     * @param input the Scanner object to read user input
     */
    private void handleCompareTeams(ArrayList<AnalyzeTable> leagueTable, Scanner input) {
        
        while (true) {

            // Asks user for team names and season data
            String team1 = getValidTeam(input, "Enter Team 1 (Eg. Liverpool): ");
            String season1 = getValidSeason(input, "Enter Team 1 Season (Eg. 2023 2024): ");
            String team2 = getValidTeam(input, "Enter Team 2 (Eg. Manchester Utd): ");
            String season2 = getValidSeason(input, "Enter Team 2 Season (Eg. 2023 2024): ");

            // Checks if stats for both teams in their respective seasons are found
            boolean team1Found = printStatsForSeason(leagueTable, team1, season1);
            boolean team2Found = printStatsForSeason(leagueTable, team2, season2);

            // If stats for either team are found, print the additional stats for other seasons
            if (team1Found || team2Found) {
                printOtherSeasonsStats(leagueTable, team1, season1);
                printOtherSeasonsStats(leagueTable, team2, season2);
                break;
            }
        }
    }
    

    /** Asks the user for a valid team name
     * @param input the Scanner object to read user input
     * @param prompt the prompt to display to the user
     * @return the valid team name entered by the user
     */
    private String getValidTeam(Scanner input, String prompt) {

        while (true) {

            // Displays the prompt and asks the user for team name
            System.out.print(prompt);
            String team = input.nextLine().trim(); 

            // If the team name is invalid, display an error and asks again
            if (!isValidTeamName(team)) {
                System.out.println("Invalid team name. Please try again. \n");
            } else {
                return team; 
            }
        }
    }
    

    /** Asks the user for a valid season
     * @param input the Scanner object to read user input
     * @param prompt the prompt to display to the user
     * @return the valid season entered by the user
     */
    private String getValidSeason(Scanner input, String prompt) {

        while (true) {

            // Displays the prompt and asks the user for team name
            System.out.print(prompt);
            String season = input.nextLine().trim(); 

            // If the season is invalid, display an error and asks again
            if (!isValidSeason(season)) {
                System.out.println("Invalid season. Please try again. \n");
            } else {
                return season;
            }
        }
    }


    /** Prints the league table stats for a specific team in a given season
     * @param leagueTable the list of AnalyzeTable objects containing league data
     * @param team the name of the team whose stats are to be printed
     * @param season the season for which the stats are to be printed
     * @return true if data is found for the specified team and season, otherwise false
     */
    private boolean printStatsForSeason(ArrayList<AnalyzeTable> leagueTable, String team, String season) {

        // Prints the header for the stats table of the specified team and season
        System.out.printf("\n--- Stats for %s in %s ---\n", team, season);
        System.out.println("Rk    Squad                MP  W   D   L   GF  GA  GD  Pts Pts/MP  xG      xGA     xGD     xGD/90");
        boolean found = false;

        // Iterates over each row in the league table and check if it matches the team and season
        for (AnalyzeTable row : leagueTable) {
            if (row.getSeason().equals(season) && row.getSquad().equalsIgnoreCase(team)) {
                System.out.println(formatStats(row));
                found = true;
            }
        }

        // If no data is found, print an error message
        if (!found) {
            System.out.println("No data found for " + team + " in " + season);
        }

        return found;
    }


    /** Prints the league table stats for a specific team of all other seasons
     * @param leagueTable the list of AnalyzeTable objects containing league data
     * @param team the name of the team whose stats are to be printed
     * @param excludeSeason the season to exclude from the stats display
     */
    private void printOtherSeasonsStats(ArrayList<AnalyzeTable> leagueTable, String team, String excludeSeason) {

        // Prints the header for the stats of the team in other seasons
        System.out.printf("\n\n--- Stats for %s in other seasons ---\n", team);
        System.out.println("Season      Rk    Squad                MP  W   D   L   GF  GA  GD  Pts Pts/MP  xG      xGA     xGD     xGD/90");
        boolean found = false;

        // Iterates over each row and check if it matches the team but not the excluded season
        for (AnalyzeTable row : leagueTable) {
            if (row.getSquad().equalsIgnoreCase(team) && !row.getSeason().equals(excludeSeason)) {
                System.out.printf("[%s] %s\n", row.getSeason(), formatStats(row));
                found = true; 
            }
        }

        // If no data is found for other seasons, print an error message
        if (!found) {
            System.out.println("No further data was found for " + team);
        }
    }


    /** Executes the feature to allows users to view season tables and compare teams
     */
    public void executeFeature() {

        // Load the league table data from the specified file
        ArrayList<AnalyzeTable> leagueTable = loadLeagueTable("data/League Tables.csv", null);

        // Create a new scanner for user input
        Scanner input = new Scanner(System.in);

        // Displays an interactive menu
        // Asks the users for their feature choice executes that feature accordingly
        while (true) {
            System.out.println("\n--- League Table/Team Analysis ---");
            System.out.println("   (1) View a season's league table");
            System.out.println("   (2) Compare two teams league stats across seasons \n");
            System.out.println("   (0) Exit to feature list \n");
            System.out.print("Enter your choice: ");

            String choice = input.nextLine();

            switch (choice) {
                case "1":
                    handleViewSeason(leagueTable, input); 
                    break;
                case "2":
                    handleCompareTeams(leagueTable, input); 
                    break;
                case "0":
                    System.out.println("Exiting tool.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}