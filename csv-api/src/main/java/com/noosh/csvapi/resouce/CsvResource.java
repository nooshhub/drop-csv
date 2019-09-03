package com.noosh.csvapi.resouce;

import com.noosh.csvapi.service.CsvDataService;
import com.noosh.csvapi.service.CsvFileService;
import com.noosh.csvapi.vo.CsvVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/csv")
public class CsvResource {

    @Autowired
    private CsvFileService csvService;
    @Autowired
    private CsvDataService csvSearchService;

    @PostMapping(value = "/upload")
    public CsvVo uploadCsv(
            @RequestPart("file") MultipartFile file,
            @RequestParam("csvName") String csvName,
            @RequestParam("headers") String[] headers,
            @RequestParam(value = "skipCount", required = false, defaultValue = "0")
                    int skipCount) {
        csvName = "uhg";
        headers = new String[]{"UniqueName", "Setid", "Name", "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};
        // return search url
        return csvService.parseExcelCsv(file, headers, skipCount);
    }

    @GetMapping(value = "/_search/{csvName}")
    public CsvVo searchCsv(
            @PathVariable("csvName") String csvName) {
        csvName = "uhg";
        return csvSearchService.searchCsv(csvName);
    }

}
