package com.noosh.csvapi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@Service
public class MockCsvFileService {


    public void generateBigCsv(int csvLineSize, String fileName) {
        File file = new File(fileName);
        if(file.exists()) {
            file.delete();
        }

        try (FileWriter fileWriter = new FileWriter(fileName, true);
             CSVPrinter printer = CSVFormat.EXCEL.withHeader(Headers.class).print(fileWriter);) {

            for (int i = 0; i < csvLineSize; i++) {
                printer.printRecord(
                        "ID" + i,
                        "CustomerNo" + i,
                        "Name" + i,
                        "Address" + i,
                        "PhoneNumber" + i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
