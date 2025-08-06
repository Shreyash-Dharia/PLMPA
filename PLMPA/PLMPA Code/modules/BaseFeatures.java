package modules;
import java.io.*;
import java.util.*;

/** An abstract class used by various classes in this program.
 * This abstract class includes common methods used by the classes to simplify code.
 * This file includes the ability to CSV files, ask for team inputs, parse inputs and verify if inputs are correct or not.
 */
public abstract class BaseFeatures {

    /** Reads data from a CSV file and allows the option to skip header and filter data
     * @param filePath the path of the CSV file to read
     * @param skipHeader true to skip the header row of the CSV file, otherwise false
     * @param seasonFilter the season to filter by
     * @return a list of rows where each row is an array of strings
     */
    protected ArrayList<String[]> readCSV(String filePath, boolean skipHeader, String seasonFilter) {
        ArrayList<String[]> data = new ArrayList<>();

        // Accesses the file from the folder
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = skipHeader;

            // Skips the first line if it's the header if specified
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }

                // Splits the row into columns by the commas
                String[] row = line.split(","); 
                if (seasonFilter == null || (row.length > 0 && row[0].equals(seasonFilter))) {

                    // Add row to data if it matches the filter
                    data.add(row);
                }
            }
        // Prints an error message if anything goes wrong
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage()); 
        }
        return data;
    }


    /** Asks the user for two valid team names for fixtures
     * @param input the Scanner object to read user input
     * @return a string containing both team names separated by a comma
     */
    protected String getTeamInput(Scanner input) {
        String team1, team2;

        // Iterates to get valid input for Team 1
        while (true) {
            System.out.print("Enter Team 1 (Eg. Liverpool): ");
            team1 = input.nextLine().trim();

            // If the name is valid then the loop breaks
            if (isValidTeamName(team1)) {
                break;
            }
            System.out.println("Invalid team name. Please try again. \n");
        }

        // Iterates to get valid input for Team 2, ensuring it's different from Team 1
        while (true) {
            System.out.print("Enter Team 2 (Eg. Everton): ");
            team2 = input.nextLine().trim();

            // If the name is valid and doesn't match the other teams name then the loop breaks
            if (isValidTeamName(team2) && !team2.equalsIgnoreCase(team1)) {
                break;
            }

            // Prints an error message if the team names match or an incorrect name is typed
            if (team2.equalsIgnoreCase(team1)) {
                System.out.println("Team 2 cannot be the same team as Team 1. Please try again. \n");
            } else {
                System.out.println("Invalid team name. Please try again. \n");
            }
        }

        return team1 + "," + team2;
    }


    /** Parses a string containing two team names separated by a comma
     * @param teamInput the comma-separated string of team names
     * @return an array containing the two team names
     */
    protected String[] parseTeams(String teamInput) {
        String[] teams = teamInput.split(",");
        return new String[] { teams[0].trim(), teams[1].trim() };
    }


    /** Checks if the provided fixture data is valid
     * @param fixture the array of data representing a fixture
     * @return true if the fixture is valid, otherwise false
     */
    protected boolean isFixtureValid(String[] fixture) {
        return fixture.length >= 7 && !fixture[1].isEmpty() && !fixture[6].isEmpty();
    }


    /** Checks if the provided team name is valid by comparing it against a list of valid teams
     * @param teamName the name of the team to check
     * @return true if the team name is valid, otherwise false
     */
    protected boolean isValidTeamName(String teamName) {
        List<String> validTeams = Arrays.asList(
                "Arsenal", "Aston Villa", "Bournemouth", "Brentford", "Brighton", "Burnley", "Chelsea",
                "Crystal Palace", "Everton", "Fulham", "Leeds United", "Leicester City", "Liverpool",
                "Luton Town", "Manchester City", "Manchester Utd", "Newcastle Utd", "Nott'ham Forest",
                "Norwich City", "Sheffield United", "Southampton", "Tottenham", "Watford", "West Brom",
                "West Ham", "Wolves");

        return validTeams.contains(teamName);
    }


    /** Checks if the provided season is valid by comparing it against a list of valid seasons
     * @param season the season to check
     * @return true if the season is valid, otherwise false
     */
    protected boolean isValidSeason(String season) {
        List<String> validSeason = Arrays.asList("2024 2025", "2023 2024", "2022 2023", "2021 2022", "2020 2021",
                "2019 2020");

        return validSeason.contains(season);
    }
    

    /** Abstract method to execute a specific feature, to be implemented by subclasses.
     */
    public abstract void executeFeature();
}
