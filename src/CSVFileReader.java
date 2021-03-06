import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class CSVFileReader {


    ArrayList<String> readCSVFile(String fileName) {
        ArrayList<String> documents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String doc;
            while ((doc = br.readLine()) != null) {
                doc = doc.split("\",\"")[1];
                doc = doc.substring(0, doc.length() - 1);
                documents.add(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }
}