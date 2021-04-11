package com.oop5;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    static Logger LOGGER;
    static {
        try(FileInputStream ins = new FileInputStream("log.config.txt")){
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Main.class.getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Automobile car1 = new Automobile(15);
        Automobile car2 = new Automobile(10);
        Thread first = new Thread(car1);
//        first.setPriority(10);
        first.start();
        LOGGER.log(Level.INFO, "Запущен первый поток");
        Thread second = new Thread(car2);
//        second.setPriority(1);
        second.start();
        LOGGER.log(Level.INFO, "Запущен второй поток");

        Runnable checker = new Runnable() {
            boolean firstPaused = false;
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (!firstPaused && (car1.getDistance() - car2.getDistance()) > 3000) {
                        car1.pause();
                        firstPaused = true;
                        LOGGER.log(Level.INFO,"Первый автомобиль остановлен");
                    }
                    try {
                        Thread.sleep(Automobile.getSleepTime());
                    } catch (InterruptedException ex) {
                    }
                    if (firstPaused && (car2.getDistance() - car1.getDistance()) > 3000) {
                        car1.unpause();
                        firstPaused = false;
                        LOGGER.log(Level.INFO,"Первый автомобиль продолжил движение");

                    }
                }
            }
        };
        Thread control = new Thread(checker);
//        control.setPriority(10);
        control.start();
        LOGGER.log(Level.INFO, "Запущен контролирующий поток");
    }
}
