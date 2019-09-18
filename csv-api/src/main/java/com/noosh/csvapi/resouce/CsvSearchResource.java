package com.noosh.csvapi.resouce;

import com.noosh.csvapi.service.CsvDataService;
import com.noosh.csvapi.vo.CsvSearchResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@CrossOrigin
@RestController
@RequestMapping("/csv")
public class CsvSearchResource {

    @Autowired
    private CsvDataService csvDataService;


    @GetMapping(value = "/_search/{csvName}")
    public CsvSearchResultVo searchCsv(
            @PathVariable("csvName") String csvName,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return csvDataService.searchCsv(csvName, page, size);
    }

    // todo: post search by field and criteria

}
