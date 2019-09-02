package com.noosh.csvapi.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Service
public class CsvService {

    @PostConstruct
    public void init() {
        File file = new File("I:\\cloud\\drop-csv\\csv-api\\csv-files\\Reinsurance.csv");

        String[] headers = {"UniqueName", "Setid", "Name",
                "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};

        parseExcelCsv(file, headers, 1);
    }

    public void parseExcelCsv(File file, String[] headers, int skipLineCount) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            // TODO: return a file not found message
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
            for (CSVRecord record : records) {
                Map<String, String> line = new HashMap<>(headers.length);
                for (String header: headers) {
                    line.put(header, record.get(header));
                }
                System.out.println(line);
                // TODO: map will take too much bandwidth, use headers + same sort order data list []
            }
        }

    }
}
