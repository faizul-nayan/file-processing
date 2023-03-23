package com.customer.fileprocessing.utill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerBatchProcess<T> {

    private final JpaRepository<T, Long> repository;
    private final List<T> dataList;
    private final int numberOfThreads = 8;
    private int batchSize = 1000;

    public CustomerBatchProcess(JpaRepository<T, Long> repository, List<T> dataList) {
        this.repository = repository;
        this.dataList = dataList;
        batchSize = dataList.size() / numberOfThreads;
    }

    public void proceed(){

        List<List<T>> dataBatch = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i+=batchSize){
            int lastIndex = Math.min(dataList.size(), i + batchSize);
            dataBatch.add(dataList.subList(i, lastIndex));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (List<T> data : dataBatch){
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try {
                    repository.saveAll(data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, executorService);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        executorService.shutdown();

        /*List<Thread> threadList = new ArrayList<>();

        for (List<T> data : dataBatch){
            Thread thread = new Thread(()->repository.saveAll(data));
            thread.start();
            threadList.add(thread);
        }

        for (Thread thread : threadList){
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
    }
}
