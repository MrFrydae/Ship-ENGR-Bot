package edu.ship.engr.discordbot.utils.csv;

import com.google.common.collect.Lists;
import edu.ship.engr.discordbot.utils.Exceptions;
import edu.ship.engr.discordbot.utils.Log;
import edu.ship.engr.discordbot.utils.OptionsManager;
import lombok.Getter;
import org.javatuples.Tuple;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CSVHandler {
    @Getter private String fileName;
    @Getter private CSVParser parser;
    @Getter private List<String> headers;
    @Getter private List<CSVRecord> records;

    /**
     * Loads all of the information for a file with this name.
     *
     * @param fileName the file to search for
     */
    public CSVHandler(@NotNull String fileName) {
        this.fileName = fileName.replace(".csv", "") + ".csv";
        if (OptionsManager.getSingleton().isTestMode() || OptionsManager.getSingleton().isDevMode()) {
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

    /**
     * Adds a line to the file.
     *
     * @param tuple the values to add
     * @throws Exceptions.CSVException if anything bad happens
     */
    public void addEntry(Tuple tuple) throws Exceptions.CSVException {
        try {
            FileWriter fileWriter = new FileWriter(getFileName(), true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(Arrays.stream(tuple.toArray()).map(String::valueOf).collect(Collectors.joining(",")));
            printWriter.close();
        } catch (IOException e) {
            throw new Exceptions.CSVException();
        }
    }

    @Override
    public String toString() {
        return "{fileName=" + fileName + ",records=" + records + "}";
    }
}
