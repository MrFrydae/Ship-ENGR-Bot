package edu.ship.engr.discordbot.utils.csv;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.Exceptions;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.OptionsManager;
import edu.ship.engr.discordbot.utils.StringUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class CSVHandler {
    private String fileName;
    private CSVParser parser;
    private List<String> headers = Lists.newArrayList();
    private List<CSVRecord> records = Lists.newArrayList();

    public CSVHandler(@NotNull String fileName) {
        this.fileName = fileName.replace(".csv", "") + ".csv";
        if (OptionsManager.getSingleton().isTestMode()) {
            this.fileName = "stage/" + this.fileName;
        }
        this.parser = getCSV(getFileName());

        try {
            this.records = Objects.requireNonNull(parser).getRecords();
            this.headers = Lists.newArrayList(parser.getHeaderMap().keySet());
        } catch (IOException e) {
            Log.exception("Error creating CSVHandler object for file: " + getFileName(), new Exceptions.CSVException());
        }
    }

    private CSVParser getCSV(@NotNull String fileName) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            return new CSVParser(reader, CSVFormat.EXCEL.withHeader());
        } catch (IOException e) {
            Log.exception("Error loading csv file: " + fileName, new Exceptions.CSVException());
            return null;
        }
    }

    public void updateByColumn(String column, String oldValue, String newValue) {
        List<CSVRecord> foundRecords = getRecords(column, oldValue);
        try {
            for (CSVRecord record : foundRecords) {
                records.remove(record);
                update();
                Map<String, String> newRecord = record.toMap();
                newRecord.replace(column, newValue);
                addEntry(newRecord);
            }
        } catch (Exceptions.CSVException e) {
            Log.exception("Error updating " + getFileName(), e);
        }
    }

    public String getWhereColumnEquals(String find, String column, String equals) {
        for (CSVRecord record : getRecords()) {
            if (record.get(column).equals(equals)) {
                return record.get(find);
            }
        }
        return null;
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

    public List<CSVRecord> getRecords(String column, String value) {
        return getRecords()
                .stream()
                .filter(record -> record.get(column).equals(value))
                .collect(Collectors.toList());
    }

    public void update() {
        File csvSaveFile = new File(getFileName());
        File backupFile = new File(csvSaveFile.getParentFile(), csvSaveFile.getName() + ".bak");
        File tmpFile = new File(csvSaveFile.getParentFile(), csvSaveFile.getName() + ".tmp");

        try {
            FileWriter fileWriter = new FileWriter(tmpFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(StringUtil.join(getHeaders(), ","));
            getRecords().stream().map(record -> StringUtil.join(getValues(record), ",")).forEach(printWriter::println);
            printWriter.close();

            if (csvSaveFile.isFile()) {
                if (backupFile.isFile()) {
                    backupFile.delete();
                }
                csvSaveFile.renameTo(backupFile);
            }
            tmpFile.renameTo(csvSaveFile);
        } catch (IOException e) {
            Log.exception("Error updating csv file: " + getFileName(), new Exceptions.CSVException());
        }
    }

    private List<String> getValues(CSVRecord record) {
        return Lists.newArrayList(record.toMap().values());
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
    
}
