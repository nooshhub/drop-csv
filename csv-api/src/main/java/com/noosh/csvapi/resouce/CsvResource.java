package com.noosh.csvapi.resouce;

import com.noosh.csvapi.service.CsvService;
import com.noosh.csvapi.vo.CsvVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;

/**
 * @author Neal Shan
 * @since 0.0.1
 */
@RestController
@RequestMapping("/csv")
public class CsvResource {

    @Autowired
    private CsvService csvService;

    @PostMapping(value = "/upload")
    public CsvVo uploadCsv(
            @RequestParam("file") MultipartFile file,
            @RequestParam("headers") String[] headers,
            @RequestParam(value = "skipCount", required = false, defaultValue = "0")
                    int skipCount) throws IOException {
        headers = new String[]{"UniqueName", "Setid", "Name", "cus_REINS", "cus_DESCRSHORT", "cus_REINS_DESCR_", "cus_SETID_REINS_"};
        return csvService.parseExcelCsv(file, headers, skipCount);
    }

}
