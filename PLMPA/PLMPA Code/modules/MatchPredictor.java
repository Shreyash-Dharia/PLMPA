package modules;
import java.util.*;

/** A match predictor feature where the user can get a predictor for any two teams.
 * The feature will use historical data from fixtures.csv and make a prediction based on that data.
 * The predication will then be printed for the user to see.
 */
public class MatchPredictor extends BaseFeatures {

    // Instance variables to track stats
    private double team1TotalXG = 0;
    private double team2TotalXG = 0;

    private int team1TotalGoals = 0;
    private int team2TotalGoals = 0;

    private int team1Matches = 0;
    private int team2Matches = 0;


    /** Gets the total expected goals for team 1
     * @return the total expected goals for team 1
     */
    public double getTeam1TotalXG() {
        return team1TotalXG;
    }

    /** Gets the total expected goals for team 2
     * @return the total expected goals for team 2
     */
    public double getTeam2TotalXG() {
        return team2TotalXG;
    }

    /** Gets the total number of goals scored by team 1
     * @return the total goals for team 1
     */
    public int getTeam1TotalGoals() {
        return team1TotalGoals;
    }

    /** Gets the total number of goals scored by team 2 
     * @return the total goals for team 2
     */
    public int getTeam2TotalGoals() {
        return team2TotalGoals;
    }

    /** Gets the total number of matches played by team 1
     * @return the total matches played by team 1
     */
    public int getTeam1Matches() {
        return team1Matches;
    }

    /** Gets the total number of matches played by team 2
     * @return the total matches played by team 2
     */
    public int getTeam2Matches() {
        return team2Matches;
    }

    
    /** Sets the total expected goals for team 1
     * @param team1TotalXG the total expected goals for team 1
     */
    public void setTeam1TotalXG(double team1TotalXG) {
        this.team1TotalXG = team1TotalXG;
    }

    /** Sets the total expected goals for team 2
     * @param team2TotalXG the total expected goals for team 2
     */
    public void setTeam2TotalXG(double team2TotalXG) {
        this.team2TotalXG = team2TotalXG;
    }

    /** Sets the total number of goals scored by team 1
     * @param team1TotalGoals the total goals for team 1
     */
    public void setTeam1TotalGoals(int team1TotalGoals) {
        this.team1TotalGoals = team1TotalGoals;
    }

    /** Sets the total number of goals scored by team 2
     * @param team2TotalGoals the total goals for team 2
     */
    public void setTeam2TotalGoals(int team2TotalGoals) {
        this.team2TotalGoals = team2TotalGoals;
    }

    /** Sets the total number of matches played by team 1
     * @param team1Matches the total matches played by team 1
     */
    public void setTeam1Matches(int team1Matches) {
        this.team1Matches = team1Matches;
    }

    /** Sets the total number of matches played by team 2
     * @param team2Matches the total matches played by team 2
     */
    public void setTeam2Matches(int team2Matches) {
        this.team2Matches = team2Matches;
    }


    /** Updates the stats for both teams based on the match data
     * @param homeTeam the name of the home team
     * @param awayTeam the name of the away team
     * @param homeScore the score of the home team
     * @param awayScore the score of the away team
     * @param homeXG the expected goals for the home team
     * @param awayXG the expected goals for the away team
     * @param team1 the name of team 1
     * @param team2 the name of team 2
     */
    private void updateStats(String homeTeam, String awayTeam, int homeScore, int awayScore, double homeXG, double awayXG, String team1, String team2) {

        // Increases the team 1's stats
        if (homeTeam.equals(team1)) {
            team1TotalGoals += homeScore;
            team1TotalXG += homeXG;
            team1Matches++;
        } else if (awayTeam.equals(team1)) {
            team1TotalGoals += awayScore;
            team1TotalXG += awayXG;
            team1Matches++;
        }

        // Increases the team 2's stats
        if (homeTeam.equals(team2)) {
            team2TotalGoals += homeScore;
            team2TotalXG += homeXG;
            team2Matches++;
        } else if (awayTeam.equals(team2)) {
            team2TotalGoals += awayScore;
            team2TotalXG += awayXG;
            team2Matches++;
        }
    }


    /** Parses fixture data to update the stats for the specified teams
     * @param fixtures the list of fixture data
     * @param team1 the name of team 1
     * @param team2 the name of team 2
     */
    private void parseData(ArrayList<String[]> fixtures, String team1, String team2) {
        
        // Skips any invalid fixtures
        for (String[] fixture : fixtures) {
            if (!isFixtureValid(fixture)) {
                continue;
            }

            try {

                // Parses the data and stores them in variables
                String home = fixture[1].trim();
                String away = fixture[6].trim();
                int homeScore = Integer.parseInt(fixture[3].trim());
                int awayScore = Integer.parseInt(fixture[4].trim());
                double homeXG = Double.parseDouble(fixture[2].trim());
                double awayXG = Double.parseDouble(fixture[5].trim());

                // Updates the stats of the team
                if ((home.equals(team1) && away.equals(team2)) || (home.equals(team2) && away.equals(team1))) {
                    updateStats(home, away, homeScore, awayScore, homeXG, awayXG, team1, team2);
                }

            // Skips if values cannot be parsed as numbers
            } catch (NumberFormatException e) {}
        }
    }


    /** Calculates the predicted match outcome for the two teams based on their historical performance
     * @param fixtures the list of fixture data
     * @param teamInput the string input containing the names of the two teams
     */
    private void calculatePrediction(ArrayList<String[]> fixtures, String teamInput) {

        // Splits the input into team names
        String[] teams = parseTeams(teamInput); 
        String team1 = teams[0];
        String team2 = teams[1];

        // Parses data
        parseData(fixtures, team1, team2); 

        // Calculates each teams performance factor based on goals scored and XG
        double team1PF = (team1TotalGoals / team1TotalXG);
        double team2PF = (team2TotalGoals / team2TotalXG);

        // Calculates the predicted XG and goals for each team
        double team1PredictedXG = team1TotalXG / team1Matches;
        double team2PredictedXG = team2TotalXG / team2Matches;

        // Multiply predicted XG by performance factor to get predicted goals
        double team1PredictedGoals = team1PredictedXG * team1PF;
        double team2PredictedGoals = team2PredictedXG * team2PF;

         // Prints the predicted scoreline and winner
        printSummary(team1PredictedGoals, team2PredictedGoals, team1, team2);
    }


    /**  Prints the predicted scoreline and the predicted winner based on the calculated stats.
     * @param team1PredictedGoals the predicted goals for team 1
     * @param team2PredictedGoals the predicted goals for team 2
     * @param team1 the name of team 1
     * @param team2 the name of team 2
     */
    private void printSummary(double team1PredictedGoals, double team2PredictedGoals, String team1, String team2) {

        // Prints out the scorline and predicted winner
        System.out.println("\nPredicted Scoreline: " + Math.round(team1PredictedGoals) + " - " + Math.round(team2PredictedGoals));
        if (Math.round(team1PredictedGoals) > Math.round(team2PredictedGoals)) {
            System.out.println("Predicted Winner: " + team1);

        } else if (Math.round(team2PredictedGoals) > Math.round(team1PredictedGoals)) {
            System.out.println("Predicted Winner: " + team2);

        } else {
            System.out.println("Predicted Winner: Draw");
        }
    }

    
    /** Executes the match prediction feature
     */
    public void executeFeature() {
        
        // Reads the data from the CSV file
        ArrayList<String[]> fixtures = readCSV("data/Fixtures.csv", true, null); 

        // Prints the header
        System.out.println("\n--- Match Predictor ---");

        // Asks the user for their input
        Scanner input = new Scanner(System.in);
        String teamInput = getTeamInput(input); 

        // Calculates and prints the prediction
        calculatePrediction(fixtures, teamInput); 
    }
}
