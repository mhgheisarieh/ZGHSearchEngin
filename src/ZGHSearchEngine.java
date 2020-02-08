import java.util.*;

public class ZGHSearchEngine {

    public static final String FILE_NAME = "English.csv";

    public static void main(String[] args) {
        DocumentHolder documentHolder = new DocumentHolder(new CSVFileReader().readCSVFile(FILE_NAME));
        PreProcessor preProcessor = new PreProcessor(documentHolder.getDocuments());
        Processor processor = new Processor(preProcessor);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String query = scanner.nextLine();
            processor.processQuery(query);
            processor.printResults(documentHolder.getDocuments());
        }

    }
}
