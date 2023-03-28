package com.customer.fileprocessing.controller;

import com.customer.fileprocessing.service.FileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api")
@Slf4j
public class FileProcessController {
    private final FileProcessService fileProcessService;
    @Autowired
    public FileProcessController(FileProcessService fileProcessService){
        this.fileProcessService = fileProcessService;
    }

    @GetMapping("/export")
    private String exportFile(){
        return fileProcessService.exportToFile();
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String submitMulti(@RequestParam(value = "file", required = true) MultipartFile mfile){
        return fileProcessService.processFile(mfile);
    }
}
