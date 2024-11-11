package ru.netology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(1000);

        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> processRoute());
        }

        executor.shutdown();
        while (!executor.isTerminated()) {

        }

        printFrequencyMap();
    }

    private static void processRoute() {
        String route = generateRoute("RLRFR", 100);
        int countR = countR(route);

        synchronized (sizeToFreq) {
            sizeToFreq.merge(countR, 1, Integer::sum);
        }
    }

    private static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    private static int countR(String route) {
        int count = 0;
        for (char c : route.toCharArray()) {
            if (c == 'R') {
                count++;
            }
        }
        return count;
    }

    private static void printFrequencyMap() {
        System.out.println("Самое частое количество повторений:");
        int maxCount = 0;
        int mostFrequentCount = 0;

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostFrequentCount = entry.getKey();
            }
            System.out.println(entry.getKey() + " (" + entry.getValue() + " раз)");
        }

        System.out.println("Самое частое количество повторений " + mostFrequentCount + " (встретилось " + maxCount + " раз)");

        int finalMaxCount = maxCount;
        sizeToFreq.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    if (entry.getValue() < finalMaxCount) {
                        System.out.println(entry.getKey() + " (" + entry.getValue() + " раз)");
                    }
                });
    }
}