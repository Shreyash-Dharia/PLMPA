package modules;
import java.util.*;

/** A simulate season feature where the user can simulate the 2024/2025 season.
 * It uses the 2024/2025 fixtures and the current available data for each team and makes predictions to who will win.
 * The stats from the predictions are then update on the league table.
 * The league table is sorted from 1st place to last and printed.
 */
public class SimulateSeason extends BaseFeatures {

    // List of teams participating in the league
    private List<String> teams;

    // Array to store the attack and defense strengths for each team
    private double[][] strengths;


    /** Initializes the list of teams.
     */
    public SimulateSeason() {
        teams = new ArrayList<>();
    }


    /** Gets the team names for the simultion
     * @return the list of teams in the league
     */
    public List<String> getTeams() {
        return teams;
    }

    /** Gets the teams strengths which contains the attack and defense strength
     * @return a 2D array of team strengths, where each row contains the attack and defense strengths of a team
     */
    public double[][] getStrengths() {
        return strengths;
    }


    /** Extracts the list of teams from the given league data
     * @param leagueData a list of rows, where each row represents team data from the league table
     */
    private void extractTeams(List<String[]> leagueData) {
        // Iterates through each row in column 2 of the leagueData
        for (String[] row : leagueData) {

            // Checks if the team is not already on the list and adds the team name
            if (!teams.contains(row[2])) {
                teams.add(row[2]);
            }
        }
    }


    /** Calculates the league-wide average for a given statistic
     * @param leagueData a list of rows, where each row represents team data from the league table
     * @param column the column index for the statistic to average
     * @return the league-wide average for the specified statistic
     */
    private double calculateLeagueAverage(List<String[]> leagueData, int column) {
        double total = 0 ;
        double totalMatches = 0;

        // Iterates through each row of the league data
        for (String[] row : leagueData) {

            // Checks if the row has the specified column to avoid index out of bounds errors
            if (row.length > column) {
                try { 

                    // Adds the statistics to the variables
                    total += Double.parseDouble(row[column]);
                    totalMatches += Double.parseDouble(row[3]);

                // Skips if values cannot be parsed as numbers
                } catch (NumberFormatException e) {}
            }
        }

        return total / totalMatches;
    }


    /** Calculates and stores the attack and defense strengths for a specific team
     * @param leagueData a list of rows, where each row represents team data from the league table
     * @param team the name of the team
     * @param leagueAvgGF the league-wide average goals scored per match
     * @param leagueAvgGA the league-wide average goals conceded per match
     * @return an array that stores the attack strength and defense strength of the team
     */
    private double[] calculateSingularTeamStrength(List<String[]> leagueData, String team, double leagueAvgGF, double leagueAvgGA) {
        double teamGF = 0;
        double teamGA = 0;
        double teamxGD90 = 0;

        // Iterates through each row of the league data
        for (String[] row : leagueData) {

            // Checks if the current row belongs to the specified team
            if (row[2].equals(team)) {
                try {

                    // Adds the columns values to the correct variable
                    teamGF += Double.parseDouble(row[7]);
                    teamGA += Double.parseDouble(row[8]);
                    teamxGD90 += Double.parseDouble(row[15]);

                // Skips if values cannot be parsed as numbers
                } catch (NumberFormatException e) {}
            }
        }

        // Calculates the teams average GF and GA
        double teamAvgGF = teamGF / 38;
        double teamAvgGA = teamGA / 38;

        // Calculates the attack strength and defense strength of each team
        double attackStrength = (teamAvgGF / leagueAvgGF) + (teamxGD90 * -0.82);
        double defenseStrength = (leagueAvgGA / teamAvgGA) + teamxGD90;

        return new double[]{attackStrength, defenseStrength};
    }


    /** Calculates the attack and defense strengths for each team based on league data
     * @param leagueData a list of rows, where each row represents team data from the league table
     */
    private void calculateTeamStrengths(List<String[]> leagueData) {
        strengths = new double[teams.size()][2];

        // Calculates the league-wide average GF and GA
        double leagueAvgGF = calculateLeagueAverage(leagueData, 7);
        double leagueAvgGA = calculateLeagueAverage(leagueData, 8);

        // Iterates through each team and calculates its strengths using the provided values
        for (int i = 0; i < teams.size(); i++) {
            strengths[i] = calculateSingularTeamStrength(leagueData, teams.get(i), leagueAvgGF, leagueAvgGA);
        }
    }


    /** Simulates the entire season based on the fixture data
     * @param fixturesData a list of rows, where each row represents match data from the fixtures data
     * @return a 2D array representing the final league table
     */
    private int[][] simulateSeason(List<String[]> fixturesData) {
        int[][] table = initializeLeagueTable(teams.size());

        // Iterates through each match in the fixture data 
        for (String[] match : fixturesData) {
            if (match.length < 7) {
                continue;
            }

            // Gets the index values of the teams in the list
            int homeIndex = teams.indexOf(match[1]);
            int awayIndex = teams.indexOf(match[6]);

            // Skips matches where either team is not found in the teams list
            if (homeIndex == -1 || awayIndex == -1) {
                continue;
            }

            // Simulates the match
            simulateMatch(table, homeIndex, awayIndex);
        }

        // Sort the league table based on points, goal difference and goals scored
        sortLeagueTable(table);
        return table;
    }


    /** Initializes the league table 
     * @param teamCount the number of teams in the league
     * @return a 2D array representing the initial league table
     */
    private int[][] initializeLeagueTable(int teamCount) {
        int[][] table = new int[teamCount][10];

        // Assigns each team's index value to the first column
        for (int i = 0; i < teamCount; i++) {
            table[i][0] = i;
        }
        return table;
    }


    /** Processes a match between two teams and updates the league table based on the result
     * @param table the league table to update
     * @param homeIndex the index of the home team in the league table
     * @param awayIndex the index of the away team in the league table
     */
    private void simulateMatch(int[][] table, int homeIndex, int awayIndex) {

        // Calculates the xG for the home and away teams
        double homeXG = strengths[homeIndex][0] * strengths[awayIndex][1];
        double awayXG = strengths[awayIndex][0] * strengths[homeIndex][1];

        // Generates the actual goals scored by the home and away teams
        int homeGoals = generateNegativeBinomial(homeXG);
        int awayGoals = generateNegativeBinomial(awayXG);


        // Updates the league table with the match results
        updateLeagueTable(table, homeIndex, awayIndex, homeGoals, awayGoals);
    }


    /** Updates the league table based on the results of a match
     * @param table the league table to update
     * @param homeIndex the index of the home team
     * @param awayIndex the index of the away team
     * @param homeGoals the number of goals scored by the home team
     * @param awayGoals the number of goals scored by the away team
     */
    private void updateLeagueTable(int[][] table, int homeIndex, int awayIndex, int homeGoals, int awayGoals) {

        // Increases the matches played by 1 for both teams
        table[homeIndex][1]++;
        table[awayIndex][1]++;

        // Determine the outcome of the match and update wins, draws, losses, and points accordingly
        if (homeGoals > awayGoals) {
            table[homeIndex][2]++; // Increases home team win
            table[awayIndex][4]++; // Increases away team loss
            table[homeIndex][5] += 3; // Increases home teams pounts by 3

        } else if (homeGoals < awayGoals) {
            table[awayIndex][2]++; // Increases away team wins
            table[homeIndex][4]++; // Increases home team loss
            table[awayIndex][5] += 3; // Increases away teams pounts by 3

        } else {

            // Increases draws by 1
            table[homeIndex][3]++;
            table[awayIndex][3]++;

            // Increases points by 1
            table[homeIndex][5]++;
            table[awayIndex][5]++;
        }

        // Calculates and updates GF for both teams
        table[homeIndex][6] += homeGoals;
        table[awayIndex][6] += awayGoals;

        // Calculates and updates GA for both teams
        table[homeIndex][7] += awayGoals;
        table[awayIndex][7] += homeGoals;

        // Calculates and updates the GD for both teams
        table[homeIndex][8] = table[homeIndex][6] - table[homeIndex][7];
        table[awayIndex][8] = table[awayIndex][6] - table[awayIndex][7];
    }


    /** Sorts the league table based on points, goal difference and goals scored using merge sort
     * @param table the league table to sort
     */
    private void sortLeagueTable(int[][] table) {
        mergeSort(table, 0, table.length - 1);
    }


    /** Implements the merge sort algorithm to sort the league table.
     * @param table the league table to sort
     * @param left the starting index of the array to sort
     * @param right the ending index of the array to sort
     */
    private void mergeSort(int[][] table, int left, int right) {
        if (left < right) {

            // Finds the middle point of the array
            int mid = left + (right - left) / 2;

            // Sorts both halves
            mergeSort(table, left, mid);
            mergeSort(table, mid + 1, right);

            // Merges the sorted halves
            merge(table, left, mid, right);
        }
    }


    /** Merges two sorted subarrays into a single sorted array
     * @param table the league table to merge
     * @param leftIndex the starting index of the first subarray
     * @param midIndex the ending index of the first subarray
     * @param rightIndex the ending index of the second subarray
     */
    private void merge(int[][] table, int leftIndex, int midIndex, int rightIndex) {
        
        // Gets the size of the two halves
        int leftHalfSize  = midIndex - leftIndex + 1;
        int rightHalfSize  = rightIndex - midIndex;

        // Creates arrays to temporary store the left and right halves
        int[][] leftArray = new int[leftHalfSize][10];
        int[][] rightArray = new int[rightHalfSize][10];

        // Copys the information of the left and right half into the array
        for (int i = 0; i < leftHalfSize; i++) {
            leftArray[i] = table[leftIndex + i];
        }
        for (int j = 0; j < rightHalfSize; j++) {
            rightArray[j] = table[midIndex + 1 + j];
        }

        // Index for left half
        int i = 0;

        // Index for right half
        int j = 0;

        // Index for the merged table
        int k = leftIndex;

        while (i < leftHalfSize && j < rightHalfSize) {

            // If the points of the left team are greater than the right team, it places the left team in the merged table
            // Increases the left index by 1
            if (leftArray[i][5] > rightArray[j][5]) {
                table[k] = leftArray[i];
                i++;

            // If the points on the left team smaller than the right team, it places the right team in the merged table
            // Increases the right index by 1
            } else if (leftArray[i][5] < rightArray[j][5]) {
                table[k] = rightArray[j];
                j++;

            // If the points are equal, it compares using GD
            } else {

                // If the GD of the left team are greater than the right team, it places the left team in the merged table
                // Increases the left index by 1
                if (leftArray[i][8] > rightArray[j][8]) {
                    table[k] = leftArray[i];
                    i++;

                // If the GD on the left team smaller than the right team, it places the right team in the merged table
                // Increases the right index by 1
                } else if (leftArray[i][8] < rightArray[j][8]) {
                    table[k] = rightArray[j];
                    j++;

                // If the GD are equal, it compares using GF
                } else {

                    // If the GF on the left team smaller than the right team, it places the right team in the merged table
                    // Increases the left index by 1
                    if (leftArray[i][6] > rightArray[j][6]) {
                        table[k] = leftArray[i];
                        i++;

                    // If the GF on the left team smaller than the right team, it places the right team in the merged table
                    // Increases the right index by 1
                    } else {
                        table[k] = rightArray[j];
                        j++;
                    }
                }
            }
            k++;
        }

        // Any remaining elements in the left array are placed into the merged table
        while (i < leftHalfSize) {
            table[k] = leftArray[i];
            i++;
            k++;
        }

        // Any remaining elements in the right array are placed into the merged table
        while (j < rightHalfSize) {
            table[k] = rightArray[j];
            j++;
            k++;
        }
    }


    /** Generates a random number of goals scored based on the negative binomial distribution 
     * @param mean the expected goals for the match
     * @return the number of goals scored
     */
    private int generateNegativeBinomial(double mean) {
        
        // Number of trials
        double r = 2;

        // Probability of a goal being scored in a single trial
        double p = 1 / (1 + mean);
        int goals = 0;

        // Runs until r reaches 0
        while (r > 0) {
            if (Math.random() < p) {
                goals++;
            }
            r--;
        }

        return goals;
    }


    /** Prints the final league table after the season simulation
     * @param table the final league table to print
     */
    private void printFinalTable(int[][] table) {
        
        // Prints header for the league table
        System.out.println("\n--- League Table Simulation for 2024 2025 ---");
        System.out.printf("%5s %20s %5s %5s %5s %5s %5s %5s %5s %5s\n", "Rank", "Team", "MP", "W", "D", "L", "Pts", "GF", "GA", "GD");

        // Iterates through each row in the table and prints the stats 
        for (int i = 0; i < table.length; i++) {
            int index = table[i][0];
            System.out.printf("%5d %20s %5d %5d %5d %5d %5d %5d %5d %5d\n",
                    i + 1, teams.get(index), table[i][1], table[i][2], table[i][3], table[i][4], table[i][5],
                    table[i][6], table[i][7], table[i][8]);
        }
    }


    /** Executes the Simulate Season feature
     */
    public void executeFeature() {

        // Reads league and fixtures data from CSV files
        List<String[]> leagueData = readCSV("data/League Tables.csv", true, "2024 2025");
        List<String[]> fixturesData = readCSV("data/Fixtures.csv", true, "2024 2025");

         // Extract team names and other data from the league table and calculate team strengths based on that data
        extractTeams(leagueData);
        calculateTeamStrengths(leagueData);

        // Simulates the seasons and prints the final league table
        int[][] finalTable = simulateSeason(fixturesData);
        printFinalTable(finalTable);
    }
}
