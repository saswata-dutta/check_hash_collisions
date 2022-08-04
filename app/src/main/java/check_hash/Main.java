package check_hash;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> pause(1_000, "a"));
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> pause(10_000, "b"));

        try {
            CompletableFuture.allOf(f1, f2).join();
            System.out.println("joined");
            System.out.println(f1.get());
            System.out.println(f2.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String pause(int time, String ans) {
        try {
            Thread.sleep(time);
            System.out.println("paused " + time + ans);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }
}
