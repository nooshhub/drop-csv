package com.noosh.csvapi.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Service
public class CsvService {

    @PostConstruct
    public void init() {
        parseExcelCsv(1);
    }

    public void parseExcelCsv(int skipLineCount) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader("I:\\cloud\\drop-csv\\csv-api\\csv-files\\Reinsurance.csv"));


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


        Iterable<CSVRecord> records = null;
        try {
            if (in != null) {
                String[] headers = {"UniqueName", "Setid", "Name",
                        "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};
                records = CSVFormat.EXCEL.withFirstRecordAsHeader().withHeader(headers).parse(in);
            }
        } catch (IOException e) {
            // TODO: return a file not found message
        }

        if (records != null) {
            for (CSVRecord record : records) {
                String lastName = record.get("UniqueName");
                String firstName = record.get("Setid");
                System.out.println(lastName + "  " + firstName);
            }
        }

    }
}
