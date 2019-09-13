package com.noosh.csvapi.resouce;

import com.noosh.csvapi.service.CsvDataService;
import com.noosh.csvapi.service.CsvFileService;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import com.noosh.csvapi.vo.CsvVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

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
     * 1. we can use headerLineNum to replace skipCount, and read headerLine and get headers
     * 2. we will need fileShardIndex, when it's 0, we will read header and create header table
     * 3. csvName should use file name as default, but the user can change it, will check if it is exist
     */
    @PostMapping(value = "/upload")
    public Map<String, String> uploadCsv(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "headerLineNum", required = false, defaultValue = "0")
                    int skipCount) {

        // TODO: get a unique csvName
        String csvName = "uhg";

//        String[] headers = new String[]{"UniqueName", "Setid", "Name", "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};

        // get headers
        String[] headers = csvFileService.getCsvHeaders(file, skipCount);

        // create table
        csvDataService.createCsvHeaderAndDataTable(csvName, headers);

        // parse excel and insert data into created table
        csvFileService.parseExcelCsv(file, csvName, headers, skipCount);

        // return search url
        Map<String, String> result = new HashMap<>();
        result.put("searchUrl", "/csv/_search/" + csvName);
        return result;
    }

    @GetMapping(value = "/_search/{csvName}")
    public CsvSearchResultVo searchCsv(
            @PathVariable("csvName") String csvName) {
        return csvDataService.searchCsv(csvName);
    }

    // todo: post search by field and criteria

}
