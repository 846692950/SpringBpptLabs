package mysql.utils;

import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StableDiffusionWebUI {
    @SneakyThrows
    public static void main(String[] args) {
        Integer button = 1; //1:生成 2:过滤
        switch (button) {
            case 1:
                getUrl();
                break;
            case 2:
                getCheckUrl();
                break;
            default:
                System.out.println("请输入正确的值！");
                break;
        }
    }

    static final Integer START_THREED = 500; //开启的线程数
    static final Integer MIN_PORT = 10000; //最小端口
    static final Integer MAX_PORT = 65500; //最大端口
    static Integer number = new Integer("0");

    /**
     * 直接获得url并存档到电脑桌面txt文件里
     */
    private static void getUrl() {
        String writeFile = "Stable_Diffusion_WebUI.txt";
        String writePath = System.getProperty("user.home") + "/Desktop/" + writeFile;
        List<String> list = new ArrayList<>();
        list.add("http://region-3.seetacloud.com");
        list.add("http://region-8.seetacloud.com");
        list.add("http://region-31.seetacloud.com");
        list.add("http://region-41.seetacloud.com");
        list.add("http://region-101.seetacloud.com");
        //"-----------------------------------------------------------------"
        for (int i = 0; i < list.size(); i++) {
            int bigThreeNum = i;
            List<String> urls = new ArrayList<>();
            for (int j = MIN_PORT; j <= MAX_PORT; j++) {
                urls.add(list.get(bigThreeNum) + ":" + j);
            }
            //"-----------------------------------------------------------------"
            List<List<String>> groupList = partition(urls, START_THREED);
            int size = groupList.size();
            List<Callable<Void>> threads = new ArrayList<>();
            ExecutorService executor = Executors.newFixedThreadPool(size);
            for (int j = 0; j < size; j++) {
                Lock lock = new ReentrantLock();
                int smallThreeNum = j;
                threads.add(() -> {
                    Thread thread = Thread.currentThread();
                    thread.setName("线程：" + smallThreeNum);
                    try {
                        lock.lock();
                        List<String> urlList = groupList.get(smallThreeNum);
                        for (String url : urlList) {
                            checkUrl(url, writePath);
                        }
                    } finally {
                        lock.unlock();
                    }
                    int threeNum = bigThreeNum + 1;
                    System.out.println("总线程<【[" + threeNum + "]->" + thread.getName() + "】" + ">执行结束！");
                    return null;
                });
            }
            System.out.println("大线程 <" + bigThreeNum + 1 + "> 开启线程数为：" + size);
            try {
                executor.invokeAll(threads);
                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                System.out.println(" --、程序执行结束！--、");
            }
        }
    }

    /**
     * 获取能用的url并存入电脑桌面Stable_Diffusion_WebUI.txt
     */
    private static void getCheckUrl() {
        String readFile = "sss.txt";
        String writeFile = "Stable_Diffusion_WebUI.txt";
        String readPath = System.getProperty("user.home") + "/Desktop/" + readFile;
        String writePath = System.getProperty("user.home") + "/Desktop/" + writeFile;
        //"-----------------------------------------------------------------"
        List<String> list = readTxtFile(readPath);
        List<List<String>> groupList = partition(list, START_THREED);
        int size = groupList.size();
        List<Callable<Void>> threads = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(size);
        //"-----------------------------------------------------------------"
        for (int j = 0; j < size; j++) {
            int smallThreeNum = j;
            threads.add(() -> {
                Thread thread = Thread.currentThread();
                thread.setName("线程：" + smallThreeNum);
                List<String> urlList = groupList.get(smallThreeNum);
                for (String url : urlList) {
                    checkUrl(url, writePath);
                }
                System.out.println("【" + thread.getName() + "】" + " 执行结束！\n");
                return null;
            });
        }
        try {
            executor.invokeAll(threads);
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(" --、程序执行结束！--、");
        }
    }

    /**
     * 把一个集合分割成指定的份数
     *
     * @param list   要被分割的集合
     * @param numCut 需要分割的份数
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> partition(List<T> list, int numCut) {
        List<List<T>> partitions = new ArrayList<>();
        int size = list.size();
        int elementsPerPartition = (int) Math.ceil((double) size / numCut); // 每个分区的元素数量
        int startIndex = 0;
        for (int i = 0; i < numCut; i++) {
            int endIndex = Math.min(startIndex + elementsPerPartition, size); // 分区的结束索引
            List<T> partition = list.subList(startIndex, endIndex); // 获取分区的子列表
            partitions.add(partition); // 将子列表添加到分区列表
            startIndex = endIndex; // 更新下一个分区的起始索引
        }
        return partitions; // 返回分区列表
    }

    /**
     * 检查传入的url地址是否有效，有效存入桌面Stable_Diffusion_WebUI.txt
     *
     * @param address
     * @param writePath
     */
    private static void checkUrl(String address, String writePath) {
        try {
            URL url = new URL(address + "/sdapi/v1/txt2img");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // 设置连接超时时间为 5 秒
            connection.setReadTimeout(5000); // 设置读取超时时间为 5 秒
            int responseCode = connection.getResponseCode();
            if (responseCode != 405) {
                return;
            }
            URL ur = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) ur.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000); // 设置连接超时时间为 10 秒
            conn.setReadTimeout(10000); // 设置读取超时时间为 10 秒
            int resCode = conn.getResponseCode();
            if (resCode == 200) {
                String response = getResponse(conn);
                if (response.contains("Stable Diffusion") && !response.contains("\"auth_required\":true")) {
                    writeData(address, writePath);
                }
            }
        } catch (IOException e) {
            System.err.println("<" + address + "> 编号[" + number++ + "] 发生异常！不可使用！\n");
        }
    }

    /**
     * 根据 connection 获取相应结果
     *
     * @param connection
     * @throws IOException
     */
    private static String getResponse(HttpURLConnection connection) throws IOException {
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * 把txt数据根写到指定位置
     *
     * @param address   要写的数据
     * @param writePath 写入的地址
     * @throws IOException
     */
    private static void writeData(String address, String writePath) throws IOException {
        try (FileWriter writer = new FileWriter(writePath, true)) {
            writer.write(address + "\n");
        }
        System.out.println("<" + address + "> 编号[" + number++ + "] 可以使用！\n");
        File file = new File(writePath);
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("创建文件：" + writePath);
        }
    }

    /**
     * 把指定的txt文件数据读取成集合
     *
     * @param filePath 要读取的文件位置
     * @return List<String> 读取的数据集合
     */
    public static List<String> readTxtFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
