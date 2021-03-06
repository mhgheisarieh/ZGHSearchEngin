import java.util.Scanner;

public class ZGHSearchEngine {

    private static final String FILE_NAME = "English.csv";

    public static void main(String[] args) {
        DocumentHolder documentHolder = new DocumentHolder(new CSVFileReader().readCSVFile(FILE_NAME));
        PreProcessedData.getInstance().setDetailsOfWordHashMap(PreProcessor.preProcess(documentHolder.getDocuments()));
        Printable printer = new Printer();
        Scanner scanner = new Scanner(System.in);
        SearchEngine searchEngine = new SearchEngine(documentHolder, scanner, printer);
        searchEngine.query();
    }
}
