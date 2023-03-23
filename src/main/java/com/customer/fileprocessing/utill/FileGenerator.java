package com.customer.fileprocessing.utill;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileGenerator<T> {

    private final EntityManager entityManager;
    private final Class<T> classType;
    private final int threadCount = 6;

    public FileGenerator(EntityManager entityManager, Class<T> classType) {
        this.entityManager = entityManager;
        this.classType = classType;
    }

    public String exportCustomersToFile(int batchSize) {
        StringBuilder stringBuilder = new StringBuilder();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        int totalRecords = getTotalRecords();
        int totalPages = (int) Math.ceil((double) totalRecords / batchSize);
        int recordsProcessed = 0;

        for (int page = 0; page < totalPages; page++) {
            PageRequest pageRequest = PageRequest.of(page, batchSize);
            int pageNumber = page + 1;
            executorService.submit(() -> {
                List<T> customers = getCustomers(pageRequest);
                if (!customers.isEmpty()) {
                    stringBuilder.append(writeCustomersToFile(customers, pageNumber));
                    stringBuilder.append("\n");
                }
                return null;
            });
            recordsProcessed += batchSize;
            System.out.printf("Processed %d out of %d records\n", recordsProcessed, totalRecords);
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return stringBuilder.toString();
    }

    private int getTotalRecords() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(classType)));
        return entityManager.createQuery(countQuery).getSingleResult().intValue();
    }

    private List<T> getCustomers(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(classType);
        Root<T> root = criteriaQuery.from(classType);
        criteriaQuery.select(root);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return query.getResultList();
    }

    private String writeCustomersToFile(List<T> customers, int pageNo) {

        String className = classType.getSimpleName();

        String fileName = Utility.ROOT_FILE_PATH + className+"_page_no_"+ pageNo+".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            customers.forEach(customer -> writer.println(customer.toString()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return fileName;
    }
}
