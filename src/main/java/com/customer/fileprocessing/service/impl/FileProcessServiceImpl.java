package com.customer.fileprocessing.service.impl;

import com.customer.fileprocessing.entity.Customer;
import com.customer.fileprocessing.entity.InvalidCustomer;
import com.customer.fileprocessing.exception.DuplicateDataFoundException;
import com.customer.fileprocessing.exception.InvalidContactException;
import com.customer.fileprocessing.repo.CustomerRepository;
import com.customer.fileprocessing.repo.InvalidCustomerRepository;
import com.customer.fileprocessing.service.FileProcessService;
import com.customer.fileprocessing.utill.CustomerBatchProcess;
import com.customer.fileprocessing.utill.FileGenerator;
import com.customer.fileprocessing.utill.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileProcessServiceImpl implements FileProcessService {

    @Autowired
    EntityManager entityManager;

    private final CustomerRepository customerRepository;
    private final InvalidCustomerRepository invalidCustomerRepository;

    @Autowired
    public FileProcessServiceImpl(CustomerRepository customerRepository, InvalidCustomerRepository invalidCustomerRepository){
        this.customerRepository = customerRepository;
        this.invalidCustomerRepository = invalidCustomerRepository;
    }

    @Override
    public String processFile(MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<Customer> customerList = new ArrayList<>();
        List<InvalidCustomer> invalidCustomerList = new ArrayList<>();
        Set<String> contactSet = ConcurrentHashMap.newKeySet();

        bufferedReader.lines().parallel().forEachOrdered(line->{
            String[] values = line.split(",");
            if(8 != values.length){
                invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
            }else {
                try {
                    String phoneNumber = values[5].trim();
                    String emailAddress = values[6].trim();

                    if(!Utility.validatePhoneNumber(phoneNumber) || !Utility.validateEmail(emailAddress)){
                        throw new InvalidContactException();
                    }
                    Customer customer = Customer.builder().firstName(values[0].trim()).lastName(values[1].trim()).city(values[2].trim())
                            .street(values[3].trim()).streetCode(values[4].trim()).phoneNumber(phoneNumber).email(emailAddress)
                            .ipAddress(values[7].trim()).build();
                    //checking duplicate
                    if(contactSet.add(phoneNumber + emailAddress)){
                        customerList.add(customer);
                    }else {
                        throw new DuplicateDataFoundException();
                    }

                } catch (ArrayIndexOutOfBoundsException | InvalidContactException | DuplicateDataFoundException e) {
                    invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
                } catch (Exception exception){
                    invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
                }
            }
        });

        processForBatchInsert(customerList, invalidCustomerList);

        System.out.println(invalidCustomerList.size());
        System.out.println(customerList.size());
        System.out.println(customerList.size() + invalidCustomerList.size());

        return String.format("Write %d row for valid customer and %d row for invalid customer and total %d row inserted", customerList.size(), invalidCustomerList.size(), customerList.size() + invalidCustomerList.size());
    }

    @Override
    public String exportToFile() {


        FileGenerator<InvalidCustomer> invalidCustomerFileGenerator = new FileGenerator<>(entityManager, InvalidCustomer.class);
        String invalidCustomerFileName = invalidCustomerFileGenerator.exportCustomersToFile(-1);

        FileGenerator<Customer> validCustomerFileGenerator = new FileGenerator<>(entityManager, Customer.class);
        String validCustomerFileName = validCustomerFileGenerator.exportCustomersToFile(100000);

        return "valid customer location are: \n"+validCustomerFileName + " \n"+"invalid customer location are: \n"+invalidCustomerFileName;
    }


    private void processForBatchInsert(List<Customer> validCustomerList, List<InvalidCustomer> invalidCustomerList){
        CustomerBatchProcess<Customer> validCustomerProcess = new CustomerBatchProcess<>(this.customerRepository, validCustomerList);
        CustomerBatchProcess<InvalidCustomer> invalidCustomerProcess = new CustomerBatchProcess<>(this.invalidCustomerRepository, invalidCustomerList);


        Thread validCustomerThread = new Thread(()->validCustomerProcess.proceed());
        validCustomerThread.start();
        Thread invalidCustomerThread = new Thread(()->invalidCustomerProcess.proceed());
        invalidCustomerThread.start();
        try {
            validCustomerThread.join();
            invalidCustomerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
