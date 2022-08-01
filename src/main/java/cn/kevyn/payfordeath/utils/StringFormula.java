package cn.kevyn.payfordeath.utils;

public class StringFormula {

    private static boolean isNumber(String str) {

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.' && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static Double getResult(String str) {

        // 递归头
        if (str.isEmpty() || isNumber(str)) {
            return str.isEmpty() ? 0 : Double.parseDouble(str);
        }

        //递归体
        if (str.contains(")")) {
            // 最后一个左括号
            int lIndex = str.lastIndexOf("(");
            // 对于的右括号
            int rIndex = str.indexOf(")", lIndex);
            return getResult(str.substring(0, lIndex) + getResult(str.substring(lIndex + 1, rIndex)) + str.substring(rIndex + 1));
        }
        if (str.contains("+")) {
            int index = str.lastIndexOf("+");
            return getResult(str.substring(0, index)) + getResult(str.substring(index + 1));
        }
        if (str.contains("-")) {
            int index = str.lastIndexOf("-");
            return getResult(str.substring(0, index)) - getResult(str.substring(index + 1));
        }
        if (str.contains("*")) {
            int index = str.lastIndexOf("*");
            return getResult(str.substring(0, index)) * getResult(str.substring(index + 1));
        }
        if (str.contains("/")) {
            int index = str.lastIndexOf("/");
            return getResult(str.substring(0, index)) / getResult(str.substring(index + 1));
        }

        // 出错
        return 0.0;
    }

    public static double calculate(String formula, PFDBean pfdBean) {
        String lv = pfdBean.getPlayer().getLevel()+"";
        String bal = pfdBean.getBalance()+"";
        double ransom = getResult(formula.replace("<lv>", lv).replace("<bal>", bal));
        return ransom > 0 ? ransom : 0;
    }

}
