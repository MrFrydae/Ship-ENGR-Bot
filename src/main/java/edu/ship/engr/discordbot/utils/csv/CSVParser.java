package edu.ship.engr.discordbot.utils.csv;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.Patterns;
import edu.ship.engr.discordbot.utils.StringUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CSVParser {
    private List<CSVRecord> records = Lists.newArrayList();
    private List<String> headers = Lists.newArrayList();

    public CSVParser(String fileName) throws IOException {
        Scanner scanner = new Scanner(Paths.get(fileName));

        if (scanner.hasNextLine()) {
            this.headers = Arrays.asList(scanner.nextLine().split(","));
        }

        while (scanner.hasNextLine()) {
            CSVRecord record = new CSVRecord(headers, Arrays.asList(Patterns.COMMA.split(scanner.nextLine())));
            this.records.add(record);
        }
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
