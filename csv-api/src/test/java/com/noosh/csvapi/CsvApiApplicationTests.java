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
	private CsvFileService csvFileService;

	@Test
	public void contextLoads() {
		csvFileService.generateBigCsv();
	}

}
