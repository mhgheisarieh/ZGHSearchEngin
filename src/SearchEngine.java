import java.util.Scanner;

class SearchEngine {
    private DocumentHolder documentHolder;
    private Processor processor;
    private Scanner scanner;
    private Printable printer;

    SearchEngine(DocumentHolder documentHolder, Processor processor, Scanner scanner, Printable printer) {
        this.documentHolder = documentHolder;
        this.processor = processor;
        this.scanner = scanner;
        this.printer = printer;
    }

    void query() {
        while (true) {
            String query = scanner.nextLine();
            this.printer.printResults(documentHolder.getDocuments(), processor.processQuery(query));
        }
    }
}
