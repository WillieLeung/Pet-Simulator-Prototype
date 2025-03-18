/**
 * This class represents reading and writing to both JSON and CSV files
 *
 * @author Abdul Hamdan, Shahob Zekria
 * @version 1.0
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WriteToFile {

    public void writeToCSV() {
    }

    public void writeToJSON() {
    }


    /**
     * Function reads from CSV file and returns a list of rows,
     *
     * @return dataList
     */

    public List<String[]> readFromCSV(String csv_file) throws IOException {
        List<String[]> dataList = new ArrayList<>();
        String line;
        // Define the delimiter used to separate the values in the CSV (comma in this case)
        String delimiter = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csv_file))) {
            br.readLine();
            // Read each  line of the CSV file until end of file
            while ((line = br.readLine()) != null) {
            // Split the line into an array of strings using the delimiter
                String[] data = line.split(delimiter);
                dataList.add(data);   // Add the split data (which is equivalent to one row) to the dataList
                for (String value : data) {
                    System.out.print(value + "\t"); // testing output
                }

            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle any file reading errors
        }

        return dataList;
    }

    public String[] readFromJSON() {
        return null;
    }


    public static void main(String[] args) {
        
    }
}
