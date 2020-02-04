import java.util.*;

public class ZGHSearchEngine {

    public static final String FILE_NAME = "English.csv";

    public static void main(String[] args) {
        CSVFileReader.getInstance().readCSVFile(FILE_NAME);
        PreProcessor.getInstance().preProcessDocs(CSVFileReader.getInstance().getDocuments());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String query = scanner.nextLine();
            Processor.getInstance().restartProcessor();
            Processor.getInstance().processQuery(query);
            Processor.getInstance().printResults();
        }
    }
}
