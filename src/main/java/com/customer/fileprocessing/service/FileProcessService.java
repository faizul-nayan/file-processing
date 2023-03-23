package com.customer.fileprocessing.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessService {
    String processFile(MultipartFile file);
    String exportToFile();
}
