package edu.ship.engr.discordbot.utils.csv;


import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.StringUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class CSVParser {
    private List<CSVRecord> records = Lists.newArrayList();
    private List<String> headers = Lists.newArrayList();

    public CSVParser(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), StandardCharsets.UTF_8));

        if (reader.ready()) {
            this.headers = Arrays.asList(Patterns.COMMA.split(reader.readLine().replace("\ufeff", "")));
        }

        while (reader.ready()) {
            CSVRecord record = new CSVRecord(headers, Arrays.asList(Patterns.COMMA.split(reader.readLine().replace("\ufeff", ""))));
            this.records.add(record);
        }

        reader.close();
    }

    public List<CSVRecord> getRecords() {
        return records;
    }

    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "{" + StringUtil.join((String[]) records.toArray(), ",") + "}";
    }
}
