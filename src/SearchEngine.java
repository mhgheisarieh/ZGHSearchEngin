import java.util.ArrayList;
import java.util.Scanner;

public class SearchEngine {
    private DocumentHolder documentHolder;
    private Processor processor;
    private Scanner scanner;

    public SearchEngine(String FileName) {
        this.documentHolder = new DocumentHolder(new CSVFileReader().readCSVFile(FileName));
        PreProcessor preProcessor = new PreProcessor(documentHolder.getDocuments());
        this.processor = new Processor(preProcessor);
        this.scanner = new Scanner(System.in);
    }

    public SearchEngine(DocumentHolder documentHolder, Processor processor, Scanner scanner) {
        this.documentHolder = documentHolder;
        this.processor = processor;
        this.scanner = scanner;
    }

    public void query() {
        Printer printer = new Printer();
        while (true) {
            String query = scanner.nextLine();
            printer.printResults(documentHolder.getDocuments(), processor.processQuery(query));
        }
    }
}
