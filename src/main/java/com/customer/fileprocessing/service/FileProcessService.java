package com.customer.fileprocessing.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessService {
    void processFile(MultipartFile file);
    void exportToFile();
}
