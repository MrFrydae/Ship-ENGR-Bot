package edu.ship.engr.discordbot.utils.csv;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.Exceptions;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.OptionsManager;
import edu.ship.engr.discordbot.utils.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CSVHandler {
    private String fileName;
    private CSVParser parser;
    private List<String> headers;
    private List<CSVRecord> records;

    public CSVHandler(@NotNull String fileName) {
        this.fileName = fileName.replace(".csv", "") + ".csv";
        if (OptionsManager.getSingleton().isTestMode()) {
            this.fileName = "stage/" + this.fileName;
        }

        this.parser = getCSV(getFileName());
        this.records = Objects.requireNonNull(parser).getRecords();
        this.headers = Lists.newArrayList(Objects.requireNonNull(parser).getHeaders());
    }

    private CSVParser getCSV(@NotNull String fileName) {
        try {
            return new CSVParser(fileName);
        } catch (IOException e) {
            Log.exception("Error loading csv file: " + fileName, new Exceptions.CSVException());
            return null;
        }
    }

    public void addEntry(Map<String, String> entry) throws Exceptions.CSVException {
        try {
            FileWriter fileWriter = new FileWriter(getFileName(), true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(StringUtil.join(entry.values(), ","));
            printWriter.close();
        } catch (IOException e) {
            throw new Exceptions.CSVException();
        }
    }

    public CSVParser getParser() {
        return parser;
    }

    public String getFileName() {
        return fileName;
    }

    public List<CSVRecord> getRecords() {
        return records;
    }

    public List<String> getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        return "{fileName=" + fileName + ",records=" + records + "}";
    }
}
