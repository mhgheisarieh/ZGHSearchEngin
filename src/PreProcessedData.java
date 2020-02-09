import java.util.HashMap;

public class PreProcessedData {
    private static PreProcessedData ourInstance = new PreProcessedData();
    private HashMap<String, DetailsOfWord> detailsOfWordHashMap;

    public static PreProcessedData getInstance() {
        return ourInstance;
    }

    private PreProcessedData() {
    }

    public HashMap<String, DetailsOfWord> getDetailsOfWordHashMap() {
        return detailsOfWordHashMap;
    }

    public void setDetailsOfWordHashMap(HashMap<String, DetailsOfWord> detailsOfWordHashMap) {
        this.detailsOfWordHashMap = detailsOfWordHashMap;
    }
}
