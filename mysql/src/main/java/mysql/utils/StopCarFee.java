package mysql.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StopCarFee {
    @SneakyThrows
    public static void main(String[] args) {

        String start = "2020-05-22 00:00:01";
        String end = "2020-05-22 07:15:01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = formatter.parse(start);
        Date endTime = formatter.parse(end);

        System.out.println(calculateParkingFee(startTime, endTime));
    }

    /**
     * 题目：计算停车费用，精确到秒
     * 要求：
     * 1.停车前15分钟免费
     * 2.超过15分钟收费1元
     * 3.超过30分钟收费2元
     * 4.从第2小时开始每半小时收费2元，不满半小时按半小时计算，以此类推
     * 5.晚上8:00 到 早上7:00 之间不收费,其余时间按正常时间累计计算
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return String 停车费用（元）
     */
    public static String calculateParkingFee(Date startTime, Date endTime) {

        // 每半小时收费2元
        double feePerHalfHour = 2.0;
        // 1秒钟毫秒数
        long oneSecond = 1000;
        // 1分钟毫秒数
        long oneMinutes = 60 * 1000;
        // 15分钟毫秒数
        long freeMinutes = 15 * 60 * 1000;
        // 30分钟毫秒数
        long halfHour = 30 * 60 * 1000;
        // 60分钟毫秒数
        long minutesPerHour = 60 * 60 * 1000;
        // 1天毫秒数
        long minutesPerDay = 60 * 60 * 24 * 1000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        int startHour = calendar.get(Calendar.HOUR_OF_DAY); // 获取开始时间的小时数
        int endHour = calendar.get(Calendar.HOUR_OF_DAY); // 获取结束时间的小时数

        long duration = endTime.getTime() - startTime.getTime(); // 计算停车时长
        long halfHours = (long) Math.ceil((double) duration / halfHour); // 计算半小时数
        long halfDays = (long) Math.ceil((double) duration / minutesPerDay); // 计算相差天数
        double fee = feePerHalfHour * halfHours; // 计算费用

        if (startHour >= 20 && halfDays < 1 || endHour < 7 && halfDays < 1 || duration <= 0) {
            fee = 0;
        } else if (duration > freeMinutes && duration <= halfHour) {
            fee = 1;
        } else if (duration > halfHour && duration <= minutesPerHour) {
            fee = 2;
        } else {
            // 计算免费时间
            long freeTime = 0;
            calendar.setTime(startTime);
            for (int i = 0; i < halfDays; i++) {
                while (calendar.getTime().before(endTime)) {
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    if (hour >= 20 || hour < 7) {
                        freeTime += oneSecond;
                    }
                    calendar.add(Calendar.SECOND, 1);
                }
            }
            fee -= (freeTime / halfHour) * feePerHalfHour + 2.0;
            double l = ((double) duration - freeTime) / oneMinutes;
            if (l <= 30 && l > 15) {
                fee = 1.0;
            }
            if (fee <= 0.0) {
                fee = 0.0;
            }
        }
        return "停车费用为：" + fee + "元";
    }


}


