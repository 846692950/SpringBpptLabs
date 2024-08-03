import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class EvaluationChecker {

    /**
     * 判断是否逾期的方法
     * @param actualAssessDate 实际评估日期，未评估时为null
     * @param latestAssessDate 最晚评估日期
     * @return 是否逾期，true为逾期，false为未逾期
     */
    public static boolean isOverdue(String actualAssessDate, String latestAssessDate) {
        // 定义日期的格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 解析最晚评估日期
        LocalDate latestDate = LocalDate.parse(latestAssessDate, formatter);

        if (actualAssessDate == null) {
            // 如果实际评估日期为null，表示未评估，判断当前日期是否超过最晚评估日期
            return LocalDate.now().isAfter(latestDate);
        } else {
            // 如果实际评估日期不为null，表示已评估，判断实际评估日期是否超过最晚评估日期
            LocalDate actualDate = LocalDate.parse(actualAssessDate, formatter);
            return actualDate.isAfter(latestDate);
        }
    }

    public static void main(String[] args) {
        System.out.println(isOverdue("2024-05-14","2024-04-14"));
    }

    public static String addDays(String dateString, int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateString, formatter);
        LocalDate newDate = date.plusDays(days);
        return newDate.format(formatter);
    }
}
