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
@RestController
@RequestMapping("/csv")
public class CsvResource {

    @Autowired
    private CsvFileService csvFileService;
    @Autowired
    private CsvDataService csvDataService;

    @PostMapping(value = "/upload")
    public Map<String, String> uploadCsv(
            @RequestPart("file") MultipartFile file,
            @RequestParam("csvName") String csvName,
            @RequestParam("headers") String[] headers,
            @RequestParam(value = "skipCount", required = false, defaultValue = "0")
                    int skipCount) {
        csvName = "uhg";
        headers = new String[]{"UniqueName", "Setid", "Name", "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};

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

}
