import java.util.Scanner;

class SearchEngine {
    private DocumentHolder documentHolder;
    private Scanner scanner;
    private Printable printer;

    SearchEngine(DocumentHolder documentHolder, Scanner scanner, Printable printer) {
        this.documentHolder = documentHolder;
        this.scanner = scanner;
        this.printer = printer;
    }

    void query() {
        while (true) {
            String query = scanner.nextLine();
            this.printer.printResults(documentHolder.getDocuments(), new Processor().processQuery(query));
        }
    }
}
