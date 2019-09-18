package com.noosh.csvapi.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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

    /**
     * read the last line by skipLineCount and split it by comma as headers
     *
     * @param file          uploaded file
     * @param skipLineCount start from 0
     * @return headers
     */
    public String[] getCsvHeaders(MultipartFile file, int skipLineCount) {
        BufferedReader in = null;
        try {
            // TODO: store and process later
            in = new BufferedReader(new InputStreamReader(file.getInputStream()));
        } catch (FileNotFoundException e) {
            // TODO: return a file not found message
        } catch (IOException e) {
            e.printStackTrace();
        }

        // skip lines
        try {
            if (in != null) {
                for (int i = 0; i <= skipLineCount; i++) {
                    String lineData = in.readLine();
                    if (i == skipLineCount) {
                        return lineData.split(",");
                    }
                }
            }
        } catch (IOException e) {
            // TODO: return no content message
        }

        return null;
    }

    public void parseExcelCsv(MultipartFile file, String csvName, String[] headers, int skipLineCount) {
        BufferedReader in = null;
        try {
            // TODO: store and process later
            in = new BufferedReader(new InputStreamReader(file.getInputStream()));
        } catch (FileNotFoundException e) {
            // TODO: return a file not found message
        } catch (IOException e) {
            e.printStackTrace();
        }

        // skip lines
        try {
            if (in != null) {
                for (int i = 0; i < skipLineCount; i++) {
                    in.readLine();
                }
            }
        } catch (IOException e) {
            // TODO: return no content message
        }


        // parse
        Iterable<CSVRecord> records = null;
        try {
            if (in != null) {
                records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
            }
        } catch (IOException e) {
            // TODO: return a file not found message
        }

        if (records != null) {
            // TODO: batchSize
            // different DB has different optimal size,
            // like mysql https://dev.mysql.com/doc/refman/5.6/en/server-system-variables.html#sysvar_max_allowed_packet
            int batchSize = 10; // TODO: set as 1000 or by different DB's optimal size, how to calculate it, write a script to test it

            int lineCount = 0;
            List<Object[]> batchLine = new ArrayList<>(batchSize);

            for (CSVRecord record : records) {
                List<String> line = new ArrayList<>(headers.length);
                for (String header : headers) {
                    line.add(record.get(header));
                }
                lineCount++;

                // batch update with batchSize
                if (lineCount == batchSize) {
                    // batch update
                    csvDataService.insertIntoDataTable(csvName, headers.length, batchLine);

                    // reset count and batchLine
                    lineCount = 0;
                    batchLine = new ArrayList<>(batchSize);

                    // this record should be add to batchLine too
                    lineCount++;
                    batchLine.add(line.toArray());
                } else if (lineCount < batchSize) {
                    batchLine.add(line.toArray());
                }
            }

            // process rest of data
            if (lineCount > 0) {
                csvDataService.insertIntoDataTable(csvName, headers.length, batchLine);
            }

        } else {
            // TODO: no data message
        }

    }

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
                records = CSVFormat.EXCEL.withHeader(headers).parse(in);
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
                for (String header : headers) {
                    line.add(record.get(header));
                }

                // batch update with batchSize
                if ((lineCount % batchSize) == 0) {
                    // batch update
                    csvDataService.insertIntoDataTableWithId(csvName, lineDataLength, batchLine);
                    // reset count and batchLine
                    batchLine = new ArrayList<>(batchSize);

                    batchTimes++;
                }

                // this record should be add to batchLine too
                batchLine.add(line.toArray());
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


