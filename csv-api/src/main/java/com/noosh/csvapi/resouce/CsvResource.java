package com.noosh.csvapi.resouce;

import com.noosh.csvapi.dao.CsvInfo;
import com.noosh.csvapi.service.CsvDataService;
import com.noosh.csvapi.service.CsvFileService;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

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
    private CsvDataService csvDataService;

    /**
     * TODO:
     * 2. we will need fileShardIndex, when it's 0, we will read header and create header table
     * 3. csvName should use file name as default, but the user can change it, will check if it is exist
     */
    @PostMapping(value = "/upload")
    public CsvInfo uploadCsv(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "headerLineNum", required = false, defaultValue = "0")
                    int skipCount) throws SQLException {

        // create csv info table if not exist
        csvDataService.createCsvInfoTable();

        String csvFileName = file.getOriginalFilename();

        String uniqueCsvSearchName = csvFileName.substring(0, csvFileName.lastIndexOf(".")).toLowerCase();
        if(csvDataService.isCsvExist(uniqueCsvSearchName)) {
            // TODO:
            // 1. we can update the table instead of throw exception, but the front-end should have a confirmation for updating
            // 2. we can also not delete and update, we can create new table, can rename the table name to switch it, and then drop the old table
            throw new RuntimeException("csv is exist");
        } else {
            // insert csv info if not exist
            CsvInfo csvInfo = csvDataService.insertCsvInfo(csvFileName, uniqueCsvSearchName);

            // get headers
            String[] headers = csvFileService.getCsvHeaders(file, skipCount);

            // create csv header and data table
            csvDataService.createCsvHeaderAndDataTable(uniqueCsvSearchName, headers);

            // parse excel and insert data into created table
            csvFileService.parseExcelCsv(file, uniqueCsvSearchName, headers, skipCount);

            // return csvInfo
            return csvInfo;
        }

    }

    @GetMapping(value = "/info/{id}")
    public CsvInfo findCsvInfo(
            @PathVariable("id") Long id) throws SQLException {
        return csvDataService.findCsvInfo(id);
    }

    @GetMapping(value = "/list")
    public List<CsvInfo> findCsvList() throws SQLException {
        return csvDataService.findCsvList();
    }

    @GetMapping(value = "/_search/{csvName}")
    public CsvSearchResultVo searchCsv(
            @PathVariable("csvName") String csvName) {
        return csvDataService.searchCsv(csvName);
    }

    // todo: post search by field and criteria

}
