public class sss {
    public static void main(String[] args) {
        String operation = "账户支付";
        String personInCharge = assignPersonInCharge(operation);
        System.out.println("负责人: " + personInCharge);
    }

    private static String assignPersonInCharge(String operation) {
        String[] operationsTangFan = {"转账", "支付", "电子账户转入"};
        String[] operationsShenYi = {"银行卡"};
        String[] operationsYingWen = {"账户", "登录", "注册"};
        String[] operationsFengYi = {"消息中心", "动账"};
        String[] operationsZhangSan = {"搜索"};

        String[] groups[] = {operationsTangFan, operationsShenYi, operationsYingWen, operationsFengYi, operationsZhangSan};
        String[] persons = {"唐凡", "沈逸", "应文", "冯一", "张三"};

        // 先检查是否有完全匹配的操作
        for (int i = 0; i < groups.length; i++) {
            for (String op : groups[i]) {
                if (operation.equals(op)) {
                    return persons[i];
                }
            }
        }

        // 如果没有完全匹配的操作，再进行部分匹配
        int maxCount = 0;
        String personInCharge = "";

        for (int i = 0; i < groups.length; i++) {
            int count = 0;
            for (String op : groups[i]) {
                if (operation.contains(op)) {
                    count += op.length();
                }
            }
            if (count > maxCount) {
                maxCount = count;
                personInCharge = persons[i];
            }
        }

        return personInCharge;
    }
}
