import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVFileReader {
    private static CSVFileReader instance;
    private ArrayList<String> documents = new ArrayList<>();

    public ArrayList<String> getDocuments() {
        return documents;
    }


    public static CSVFileReader getInstance() {
        if (instance == null) {
            instance = new CSVFileReader();
        }
        return instance;
    }

    public void readCSVFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String doc;
            int indexOfDoc = 1;
            while ((doc = br.readLine()) != null) {
                //Each line is a document
                doc = doc.split("\",\"")[1];
                doc = doc.substring(0, doc.length() - 1);
                documents.add(doc);
                indexOfDoc++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}