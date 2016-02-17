package com.tws.cassandra;

import com.datastax.driver.mapping.Result;
import com.google.common.util.concurrent.ListenableFuture;
import com.tws.cassandra.model.HistoryIntervalDB;
import com.tws.cassandra.repo.HistoryIntervalRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutionException;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Hello World!");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("cassandra-spring.xml");
        HistoryIntervalRepository historyIntervalRepository = ctx.getBean(HistoryIntervalRepository.class);
        HistoryIntervalDB historyIntervalDB = new HistoryIntervalDB();
        historyIntervalDB.setSymbol("AAPL");
        historyIntervalDB.setInterval(1);
        historyIntervalDB.setTime(System.currentTimeMillis());
        historyIntervalDB.setOpen(123.1212f);
        historyIntervalDB.setHigh(213.12121f);
        historyIntervalRepository.saveIntervalInTime(historyIntervalDB);
        ListenableFuture<Result<HistoryIntervalDB>> future = historyIntervalRepository.getIntervalInTime("AAPL", 1, 1455520682792L, 1455520748569L);
        for (HistoryIntervalDB intervalDB : future.get()) {
            System.out.println(intervalDB);
        }
    }
}
