import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class zzz {
    public static void main(String[] args) {

        // 创建包含1000个User对象的List
        List<String> users = new ArrayList<>();
        for (int i = 0; i < 1001; i++) {
            users.add(String.valueOf(i));
        }

        // 使用Guava的Lists.partition方法把users List分成10份
        List<List<String>> subLists = Lists.partition(users, 100);

        // 创建一个拥有10个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 在每个子List上启动一个线程
        for (List<String> subList : subLists) {
            executorService.submit(() -> {
                // 这里是每个线程需要执行的代码
                for (String user : subList) {
                    System.out.println(user);
                }
            });
        }

        // 关闭线程池，这会使得之前提交的任务执行完毕后关闭线程池
        executorService.shutdown();
    }
}
