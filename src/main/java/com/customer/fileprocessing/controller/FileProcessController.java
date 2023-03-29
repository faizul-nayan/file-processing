package com.customer.fileprocessing.controller;

import com.customer.fileprocessing.service.FileProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
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
    private ResponseEntity<String> exportFile(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String exportFileLocation = fileProcessService.exportToFile();
        stopWatch.stop();
        long timeTaken = stopWatch.getTotalTimeMillis();
        String response = "Process execution time "+ timeTaken +" and "+exportFileLocation;
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/uploads", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> submitMulti(@RequestParam(value = "file", required = true) MultipartFile mfile){
        return ResponseEntity.ok(fileProcessService.processFile(mfile));
    }
}
