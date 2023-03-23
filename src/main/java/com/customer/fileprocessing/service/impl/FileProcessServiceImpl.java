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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    public void processFile(MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<Customer> customerList = new ArrayList<>();
        List<InvalidCustomer> invalidCustomerList = new ArrayList<>();
        Map<String , String> contactMap = new HashMap<>();

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
                    if(!contactMap.containsKey(phoneNumber)){
                        contactMap.put(phoneNumber, emailAddress);
                        customerList.add(customer);
                    }else {
                        String exEmail = contactMap.get(phoneNumber);
                        if(exEmail.equalsIgnoreCase(emailAddress)){
                            throw new DuplicateDataFoundException();
                        }else {
                            customerList.add(customer);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
                }catch (InvalidContactException exception){
                    invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
                }catch (DuplicateDataFoundException exception){
                    invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
                }catch (Exception exception){
                    invalidCustomerList.add(InvalidCustomer.builder().input(line).build());
                }
            }
        });

        processForBatchInsert(customerList, invalidCustomerList);

        System.out.println(invalidCustomerList.size());
        System.out.println(customerList.size());
        System.out.println(customerList.size() + invalidCustomerList.size());
    }

    @Override
    public void exportToFile() {


        FileGenerator<InvalidCustomer> invalidCustomerFileGenerator = new FileGenerator<>(entityManager, InvalidCustomer.class);
        invalidCustomerFileGenerator.exportCustomersToFile(100000);

        FileGenerator<Customer> validCustomerFileGenerator = new FileGenerator<>(entityManager, Customer.class);
        validCustomerFileGenerator.exportCustomersToFile(100000);
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
