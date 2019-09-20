package com.noosh.csvapi;

import com.noosh.csvapi.service.CsvFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CsvApiApplicationTests {

    @Autowired
    private MockCsvFileService mockCsvFileService;

    @Test
    public void createMockCsv_local_small() {
        int CSV_LINE_SIZE = 50_000;
        String fileName = "D:\\dev\\awesome\\test.csv";
        mockCsvFileService.generateBigCsv(CSV_LINE_SIZE, fileName);
    }

    @Test
    public void createMockCsv_local_big() {
        int CSV_LINE_SIZE = 1_000_000;
        String fileName = "D:\\dev\\awesome\\test.csv";
        mockCsvFileService.generateBigCsv(CSV_LINE_SIZE, fileName);
    }

    @Test
    public void createMockCsv_remote_small() {
        int CSV_LINE_SIZE = 50_000;
        String fileName = "I:\\cloud\\test.csv";
        mockCsvFileService.generateBigCsv(CSV_LINE_SIZE, fileName);
    }

    @Test
    public void createMockCsv_remote_big() {
        int CSV_LINE_SIZE = 1_000_000;
        String fileName = "I:\\cloud\\test.csv";
        mockCsvFileService.generateBigCsv(CSV_LINE_SIZE, fileName);
    }

    @Test
    public void testDivide() {
        System.out.println(0 % 10);
        System.out.println(1 % 10);
        System.out.println(10 % 10);
        System.out.println(11 % 10);
        System.out.println(20 % 10);
    }

}
