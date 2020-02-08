import java.util.ArrayList;

public class Printer implements Printable {

    public void printResults(ArrayList<String> documents, ArrayList<Result> results) {
        results.forEach(result -> System.out.println(result.getScore() + "     " + documents.get(result.getIndex())));
    }

}

