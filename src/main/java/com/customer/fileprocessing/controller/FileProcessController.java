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

    @GetMapping
    public String processFile(){
        Set<TestObject> testObjectSet = new HashSet<>();
        List<TestObject> testObjectList = new ArrayList<>();
        TestObject object = new TestObject("Faizul", "Nayan");
        testObjectSet.add(object);
        testObjectSet.add(object);
        testObjectSet.add(object);

        testObjectList.add(object);
        testObjectList.add(object);
        testObjectList.add(object);

        String test = " set size : "+ testObjectSet.size() +" and list size "+ testObjectList.size();
        return "hello   --->> "+test;
    }

    @GetMapping("/export")
    private String exportFile(){
        fileProcessService.exportToFile();
        return "sss";
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
