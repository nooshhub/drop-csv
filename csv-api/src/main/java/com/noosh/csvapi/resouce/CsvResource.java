package com.noosh.csvapi.resouce;

import com.noosh.csvapi.dao.CsvHeader;
import com.noosh.csvapi.dao.CsvInfo;
import com.noosh.csvapi.service.CsvDataService;
import com.noosh.csvapi.service.CsvFileService;
import com.noosh.csvapi.service.CsvHeaderService;
import com.noosh.csvapi.service.CsvInfoService;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import com.noosh.csvapi.vo.CsvShardVo;
import com.noosh.csvapi.vo.CsvVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@CrossOrigin
@RestController
@RequestMapping("/csv")
public class CsvResource {

    @Autowired
    private CsvFileService csvFileService;
    @Autowired
    private CsvInfoService csvInfoService;
    @Autowired
    private CsvHeaderService csvHeaderService;
    @Autowired
    private CsvDataService csvDataService;


    @PostMapping(value = "/upload/init")
    public CsvInfo uploadCsvInit(
            @RequestBody CsvVo csvVo
    ) throws SQLException {

        String csvFileName = csvVo.getCsvFileName();
        // create csv info table if not exist
        csvInfoService.createCsvInfoTable();

        String uniqueCsvSearchName = csvFileName.substring(0, csvFileName.lastIndexOf(".")).toLowerCase();
        if(csvInfoService.isCsvExist(uniqueCsvSearchName)) {
            // TODO:
            // 1. we can update the table instead of throw exception, but the front-end should have a confirmation for updating
            // 2. we can also not delete and update, we can create new table, can rename the table name to switch it, and then drop the old table
            throw new RuntimeException("csv is exist");
        } else {
            // insert csv info if not exist
            CsvInfo csvInfo = csvInfoService.insertCsvInfo(csvFileName, uniqueCsvSearchName);

            // get headers
            String[] headers = csvVo.getCsvHeaders();

            // create csv header and data table
            csvHeaderService.createCsvHeaderTable(uniqueCsvSearchName, headers);
            csvDataService.createCsvDataTable(uniqueCsvSearchName, headers);

            return csvInfo;
        }

    }



    @PostMapping(value = "/upload/multi")
    public CsvShardVo uploadMultiCsv(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "csvId") Long csvId,
            @RequestParam(value = "csvShardIndex") Integer csvShardIndex
    ) throws SQLException {

        CsvInfo csvInfo = csvInfoService.findCsvInfo(csvId);
        if(csvInfo != null) {
            List<CsvHeader> csvHeaders = csvHeaderService.findHeaders(csvInfo.getSearchName());
            // get headers
            String[] headers = csvHeaders.stream()
                    .map(CsvHeader::getHeaderName)
                    .collect(Collectors.toList())
                    .toArray(new String[csvHeaders.size()]);

            // parse excel and insert data into created table
            csvFileService.parseExcelCsvAndInsertData(file, csvInfo.getSearchName(), headers, csvShardIndex);

        }

        return new CsvShardVo(csvShardIndex, file.getSize());

    }

    @GetMapping(value = "/info/{id}")
    public CsvInfo findCsvInfo(
            @PathVariable("id") Long id) throws SQLException {
        return csvInfoService.findCsvInfo(id);
    }

    @GetMapping(value = "/list")
    public List<CsvInfo> findCsvList() throws SQLException {
        return csvInfoService.findCsvList();
    }

    @GetMapping(value = "/export")
    public void generateBigCsv() {
        // TODO: download file
    }


}
