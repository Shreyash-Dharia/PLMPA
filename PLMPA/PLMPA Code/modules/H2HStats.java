package modules;
import java.util.*;

/** A head-to-head stats feature where the user can get a stats for any two teams.
 * The feature will use historical data from fixtures.csv and provide the stats based on the data extracted.
 * The stats will then be printed for the user to see in a clean format.
 */
public class H2HStats extends BaseFeatures {

    // Instance variables to track stats
    private int gamesPlayed = 0;
    private int team1GoalsScored = 0;
    private int team2GoalsScored = 0;
    private int team1GoalsConceded = 0;
    private int team2GoalsConceded = 0;
    private int team1GamesWon = 0;
    private int team2GamesWon = 0;
    private int gamesDrew = 0;

    
    /** Gets the total number of games played between the two teams
     * @return the number of games played
     */
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    /** Gets the total goals scored by team 1
     * @return the number of goals scored by team 1
     */
    public int getTeam1GoalsScored() {
        return team1GoalsScored;
    }

    /** Gets the total goals scored by team 2
     * @return the number of goals scored by team 2
     */
    public int getTeam2GoalsScored() {
        return team2GoalsScored;
    }

    /** Gets the total goals conceded by team 1
     * @return the number of goals conceded by team 1
     */
    public int getTeam1GoalsConceded() {
        return team1GoalsConceded;
    }

    /** Returns the total goals conceded by team 2
     * @return the number of goals conceded by team 2
     */
    public int getTeam2GoalsConceded() {
        return team2GoalsConceded;
    }

    /** Returns the total number of games won by team 1
     * @return the number of games won by team 1
     */
    public int getTeam1GamesWon() {
        return team1GamesWon;
    }

    /** Returns the total number of games won by team 2
     * @return the number of games won by team 2
     */
    public int getTeam2GamesWon() {
        return team2GamesWon;
    }

    /** Returns the total number of games drawn between the two teams
     * @return the number of games drawn
     */
    public int getGamesDrew() {
        return gamesDrew;
    }

    
    /** Sets the number of games played between the two teams
     * @param gamesPlayed the number of games played
     */
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    /** Sets the total goals scored by team 1
     * @param team1GoalsScored the number of goals scored by team 1
     */
    public void setTeam1GoalsScored(int team1GoalsScored) {
        this.team1GoalsScored = team1GoalsScored;
    }

    /** Sets the total goals scored by team 2
     * @param team2GoalsScored the number of goals scored by team 2
     */
    public void setTeam2GoalsScored(int team2GoalsScored) {
        this.team2GoalsScored = team2GoalsScored;
    }

    /** Sets the total goals conceded by team 1
     * @param team1GoalsConceded the number of goals conceded by team 1
     */
    public void setTeam1GoalsConceded(int team1GoalsConceded) {
        this.team1GoalsConceded = team1GoalsConceded;
    }

    /** Sets the total goals conceded by team 2
     * @param team2GoalsConceded the number of goals conceded by team 2
     */
    public void setTeam2GoalsConceded(int team2GoalsConceded) {
        this.team2GoalsConceded = team2GoalsConceded;
    }

    /** Sets the total number of games won by team 1
     * @param team1GamesWon the number of games won by team 1
     */
    public void setTeam1GamesWon(int team1GamesWon) {
        this.team1GamesWon = team1GamesWon;
    }

    /** Sets the total number of games won by team 2
     * @param team2GamesWon the number of games won by team 2
     */
    public void setTeam2GamesWon(int team2GamesWon) {
        this.team2GamesWon = team2GamesWon;
    }

    /** Sets the total number of games drawn between the two teams
     * @param gamesDrew the number of games drawn
     */
    public void setGamesDrew(int gamesDrew) {
        this.gamesDrew = gamesDrew;
    }


    /** Updates the stats based on the parsed results 
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @param homeScore the score of the home team
     * @param awayScore the score of the away team
     * @param team1 the name of team 1
     * @param team2 the name of team 2
     */
    private void updateStats(String homeTeam, String awayTeam, int homeScore, int awayScore, String team1, String team2) {

        // Updates stats for team1 and team2 based on home/away match results
        // Increases the home/away score depending on the results
        if (homeTeam.equals(team1)) {
            team1GoalsScored += homeScore;
            team2GoalsScored += awayScore;

            team1GoalsConceded += awayScore;
            team2GoalsConceded += homeScore;

            // Increases the game won count depending on the results
            if (homeScore > awayScore) {
                team1GamesWon++;
            } else if (homeScore < awayScore) {
                team2GamesWon++;
            } else {
                gamesDrew++;
            }

        // Increases the home/away score depending on the results
        } else if (homeTeam.equals(team2)) {
            team2GoalsScored += homeScore;
            team1GoalsScored += awayScore;

            team2GoalsConceded += awayScore;
            team1GoalsConceded += homeScore;

            // Increases the game won count depending on the results
            if (homeScore > awayScore) {
                team2GamesWon++;
            } else if (homeScore < awayScore) {
                team1GamesWon++;
            } else {
                gamesDrew++;
            }
        }
    }


    /** Parses fixture data to update stats for the specified teams 
     * @param fixtures the list of fixture data
     * @param team1 the name of team 1
     * @param team2 the name of team 2
     * @param pastResults a list to store past match results for display
     */
    private void parseData(ArrayList<String[]> fixtures, String team1, String team2, ArrayList<String> pastResults) {

        // Skips any invalid fixtures
        for (String[] fixture : fixtures) {
            if (!isFixtureValid(fixture)) {
                continue;
            }

            try {

                // Parses the data and stores them in variables
                String date = fixture[0].trim();
                String homeTeam = fixture[1].trim();
                String awayTeam = fixture[6].trim();
                int homeScore = Integer.parseInt(fixture[3].trim());
                int awayScore = Integer.parseInt(fixture[4].trim());

                // Updates the stats of the team and gets added to the past results arraylist 
                if ((homeTeam.equals(team1) && awayTeam.equals(team2)) || (homeTeam.equals(team2) && awayTeam.equals(team1))) {
                    gamesPlayed++;
                    updateStats(homeTeam, awayTeam, homeScore, awayScore, team1, team2);
                    pastResults.add(String.format("[%s] %s %d - %d %s", date, homeTeam, homeScore, awayScore, awayTeam));
                }
            
            // Skips if values cannot be parsed as numbers
            } catch (NumberFormatException e) {}
        }
    }


    /** Determinees and displays the head-to-heads stats for the input teams
     * @param fixtures the list of fixture data
     * @param teamInput the input string containing the two teams' names
     */
    private void determineH2HStats(ArrayList<String[]> fixtures, String teamInput) {

        // Splits input into team names
        String[] teams = parseTeams(teamInput); 
        String team1 = teams[0];
        String team2 = teams[1];

        // Parses fixtures and updates stats
        ArrayList<String> pastResults = new ArrayList<>();
        parseData(fixtures, team1, team2, pastResults);

        // Prints summary if games exist
        if (gamesPlayed > 0) {
            printSummary(team1, team2, pastResults); 
        } else {
            System.out.println("\nNo fixtures found between " + team1 + " and " + team2 + ".");
        }
    }


    /** Prints the summary of head-to-head stats
     * @param team1 the name of team 1
     * @param team2 the name of team 2
     * @param pastResults the list of past results to display
     */
    private void printSummary(String team1, String team2, ArrayList<String> pastResults) {

        // Prints the header and the number of games won, lost, drawed and played
        System.out.println("\nHead-to-Head Statistics: " + team1 + " vs " + team2);
        System.out.println("\nGames Played: " + gamesPlayed);
        System.out.println(team1 + " won: " + team1GamesWon + " | " + "Draws: " + gamesDrew + " | " + team2 + " won: " + team2GamesWon + "\n");

        // Prints each teams goals scored, conceded and differences
        System.out.println(team1 + " Goals Scored: " + team1GoalsScored + "      " + team2 + " Goals Scored: " + team2GoalsScored);
        System.out.println(team1 + " Goals Conceded: " + team1GoalsConceded + "     " + team2 + " Goals Conceded: " + team2GoalsConceded);
        System.out.println(team1 + " Goal Difference: " + (team1GoalsScored - team1GoalsConceded) + "    " + team2 + " Goal Difference: " + (team2GoalsScored - team2GoalsConceded) + "\n");

        // Prints the past fixtures of both teams
        System.out.println("Past Fixture Results:");
        for (String result : pastResults) {
            System.out.println(result);
        }
    }

    
    /**Executes the head-to-head statistics feature
     */
    public void executeFeature() {

        // Reads the data from the CSV file
        ArrayList<String[]> fixtures = readCSV("data/Fixtures.csv", true, null);

        // Prints the header
        System.out.println("\n--- Head-to-Head Statistics ---");
       
        // Asks the user for their input
        Scanner input = new Scanner(System.in);
        String teamInput = getTeamInput(input);
        
        // Calculates and prints the head-to-head stats
        determineH2HStats(fixtures, teamInput);
    }
}
