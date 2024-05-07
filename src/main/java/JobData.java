
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        Collections.sort(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        loadData();

        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the job's data by key/value, using
     * inclusion of the search term.
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);
            String dValue = aValue.toLowerCase();
            String value3 = value.toLowerCase();
            if (dValue.contains(value3)||value3.contains(dValue)) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */


    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        loadData();
        ArrayList<HashMap<String, String>> joblist = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            for(String searchTerm : row.keySet()){

                String bValue = row.get(searchTerm);

                String cValue = bValue.toLowerCase();
                String value2 = value.toLowerCase();
                if (!joblist.contains(row)) {
                    if(value2.contains(cValue) ||cValue.contains(value2)){
                        joblist.add(row);
                    }
                }

            }

        }


        return joblist;
    }
    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        if (isDataLoaded) {
            return;
        }

        try {

            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            int numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

}