package com.noosh.csvapi.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Service
public class CsvFileService {

    private final Path rootLocation;
    private final CsvDataService csvDataService;

    @Autowired
    public CsvFileService(StorageProperties properties, CsvDataService csvDataService) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.csvDataService = csvDataService;
    }


    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }


    // TODO: we may not need headers anymore, use headersSize is enough
    // TODO: we may use multi thead to insert data into database
    public void parseExcelCsvAndInsertData(MultipartFile file, String csvName, String[] headers, int csvShardIndex) {
        BufferedReader in = null;
        try {
            // TODO: store and process later
            in = new BufferedReader(new InputStreamReader(file.getInputStream()));
        } catch (FileNotFoundException e) {
            // TODO: return a file not found message
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // parse
        Iterable<CSVRecord> records = null;
        try {
            if (in != null) {
                records = CSVFormat.EXCEL
                        .withHeader(headers)
                        .parse(in);
            }
        } catch (IOException e) {
            // TODO: return a file not found message
            e.printStackTrace();
        }

        if (records != null) {
            // TODO: batchSize
            // different DB has different optimal size,
            // like mysql https://dev.mysql.com/doc/refman/5.6/en/server-system-variables.html#sysvar_max_allowed_packet
            int batchSize = 1000; // TODO: set as 1000 or by different DB's optimal size, how to calculate it, write a script to test it

            int lineCount = 0;
            List<Object[]> batchLine = new ArrayList<>(batchSize);

            int lineDataLength = headers.length + 1; // +1 for generated id
            int batchTimes = 0;
            for (CSVRecord record : records) {
                List<String> line = new ArrayList<>(lineDataLength);
                // get id
                line.add(CsvIdGenerator.get(csvShardIndex, lineCount));
                if (record.size() != headers.length) {
                    for (int i = 0; i < record.size(); i++) {
                        line.add(record.get(i));
                    }

                    // append null to make the line complete for batch insert
//                    for (int i = 0 ; i < headers.length - record.size() ; i++) {
//                        line.add(null);
//                    }
                } else {
                    for (int i = 0; i < headers.length; i++) {
                        line.add(record.get(i));
                    }
                }


                // batch update with batchSize
                if (lineCount != 0 && (lineCount % batchSize) == 0) {
                    // batch update
                    csvDataService.insertIntoDataTableWithId(csvName, lineDataLength, batchLine);
                    // reset count and batchLine
                    batchLine = new ArrayList<>(batchSize);

                    batchTimes++;
                }

                // this record should be add to batchLine too
                batchLine.add(line.toArray(new Object[lineDataLength]));
                lineCount++;
            }

            // process rest of data
            if ((lineCount - (batchSize * batchTimes)) > 0) {
                csvDataService.insertIntoDataTableWithId(csvName, lineDataLength, batchLine);
            }

        } else {
            // TODO: no data message
            System.out.println("No Data Message");
        }

    }

}


