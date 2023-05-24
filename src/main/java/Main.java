import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static ArrayBlockingQueue<String> arrayBlockingQueue1 = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> arrayBlockingQueue2 = new ArrayBlockingQueue<>(100);
    static ArrayBlockingQueue<String> arrayBlockingQueue3 = new ArrayBlockingQueue<>(100);
    static volatile AtomicInteger countA = new AtomicInteger(0);
    static volatile AtomicInteger countB = new AtomicInteger(0);
    static volatile AtomicInteger countC = new AtomicInteger(0);

    public static void main(String[] args) {
        List<Integer> listForA = new ArrayList<>();
        List<Integer> listForB = new ArrayList<>();
        List<Integer> listForC = new ArrayList<>();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                arrayBlockingQueue1.add(generateText("abc", 100));
                arrayBlockingQueue2.add(generateText("abc", 100));
                arrayBlockingQueue3.add(generateText("abc", 100));
            }

        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Thread thread1 = new Thread(() -> {
            for (String e : arrayBlockingQueue1) {
                for (int i = 0; i < e.length(); i++) {
                    if (e.charAt(i) == 'a') {
                        countA.getAndIncrement();

                    }
                }
                listForA.add(countA.get());
                countA.getAndSet(0);

            }
            countA.getAndSet(Collections.max(listForA));

        });
        Thread thread2 = new Thread(() -> {
            for (String e : arrayBlockingQueue2) {
                for (int i = 0; i < e.length(); i++) {
                    if (e.charAt(i) == 'b') {
                        countB.getAndIncrement();
                    }
                }
                listForB.add(countB.get());
                countB.getAndSet(0);

            }
            countB.getAndSet(Collections.max(listForB));

        });
        Thread thread3 = new Thread(() -> {
            for (String e : arrayBlockingQueue3) {
                for (int i = 0; i < e.length(); i++) {
                    if (e.charAt(i) == 'c') {
                        countC.getAndIncrement();
                    }
                }
                listForC.add(countC.get());
                countC.getAndSet(0);

            }
            countC.getAndSet(Collections.max(listForC));
        });
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Максимальное количство символа 'a' в строке " + countA);
        System.out.println("Максимальное количство символа 'b' в строке " + countB);
        System.out.println("Максимальное количство символа 'c' в строке " + countC);


    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
