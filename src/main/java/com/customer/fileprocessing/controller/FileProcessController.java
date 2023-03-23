package com.customer.fileprocessing.controller;

import com.customer.fileprocessing.TestObject;
import com.customer.fileprocessing.service.FileProcessService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api")
@Slf4j
public class FileProcessController {

    @GetMapping("/export")
    private String exportFile(){
        return fileProcessService.exportToFile();
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String submitMulti(@RequestParam(value = "file", required = true) MultipartFile mfile){
        fileProcessService.processFile(mfile);
       return mfile.getOriginalFilename();
    }

    private final FileProcessService fileProcessService;

    @Autowired
    public FileProcessController(FileProcessService fileProcessService){
        this.fileProcessService = fileProcessService;
    }
}
