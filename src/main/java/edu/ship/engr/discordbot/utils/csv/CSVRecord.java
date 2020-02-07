package edu.ship.engr.discordbot.utils.csv;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class CSVRecord {
    private Map<String, String> values = Maps.newHashMap();

    /**
     * Fills a map with the information that is provided.
     *
     * @param headers A list of the headers
     * @param values A list of the values
     */
    public CSVRecord(List<String> headers, List<String> values) {
        for (int i = 0; i < headers.size(); i++) {
            String value = values.size() > i ? values.get(i) : "";
            this.values.put(headers.get(i), value);
        }
    }

    /**
     * Gets the value at the provided header key.
     *
     * @param key The header to find
     * @return the value in the requested column
     */
    public String get(String key) {
        return values.get(key);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
