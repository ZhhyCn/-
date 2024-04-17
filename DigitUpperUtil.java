package com.chinaums.tsbx.util;

import java.math.BigDecimal;

/**
 * 金额转大写
 */
public class DigitUpperUtil {

    private final static String[] upper = new String[]{"零","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    private final static String[] unitMetric = new String[]{"拾","佰","仟","万","亿"};
//    private final static Map<BigDecimal,String> unit = new HashMap<>();
//
//    static {
//        unit.put(BigDecimal.valueOf(Math.pow(10,1)),"拾");
//        unit.put(BigDecimal.valueOf(Math.pow(10,2)),"佰");
//        unit.put(BigDecimal.valueOf(Math.pow(10,3)),"仟");
//        unit.put(BigDecimal.valueOf(Math.pow(10,4)),"万");
//        unit.put(BigDecimal.valueOf(Math.pow(10,8)),"亿");
//    }

    public static String toUpper(String amount){

        String reg = "(\\d+).(\\d{2})";

        String amt = amount;

        String intp = amt.replaceAll(reg,"$1");//取小数点后两位
        BigDecimal intamt = BigDecimal.valueOf(Double.valueOf(intp));

        int[] mod = new int[100];
        int len = 0;
        while(intamt.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal[] temp = intamt.divideAndRemainder(BigDecimal.valueOf(10));
            mod[len++] = temp[1].toBigInteger().intValue();
            intamt = temp[0];
        }

        String intPart = parseModArr2(mod, len);

        if(!intPart.trim().equals("")){
            intPart = intPart.concat("元");
        }

        boolean matches = amt.matches(reg);

        //有小数点
        if(matches){
            int[] fltmod = new int[2];
            int cnt = 0;
            String flt = amt.replaceAll(reg,"$2");//取小数点后两位
            BigDecimal fltbd = BigDecimal.valueOf(Double.valueOf(flt));
            while(fltbd.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal[] temp = fltbd.divideAndRemainder(BigDecimal.valueOf(10));
                fltmod[cnt++] = temp[1].toBigInteger().intValue();
                fltbd = temp[0];
            }
            if(cnt == 1){
                intPart = intPart.concat(upper[fltmod[0]] + "分");
            } else if(cnt == 2){
                intPart = intPart.concat(upper[fltmod[1]] + "角");
                intPart = intPart.concat(fltmod[0] == 0 ? "" : upper[fltmod[0]] + "分");
            }
        }

        return intPart;
    }

    public static String parseModArr(int[] mod,int len){

        StringBuffer sb = new StringBuffer();
        String unit = "";

        //逆向遍历余数数组
        for (int i = len - 1; i >= 0;) {
            if (i > 8) {
                unit = unitMetric[i - 9];
            } else if(i == 8){
                unit = "亿";
            } else if(i > 4) {
                unit = unitMetric[i - 5];
            } else if(i == 4) {
                unit = "万";
            } else if(i == 3) {
                unit = "仟";
            }  else if(i == 2) {
                unit = "佰";
            }  else if(i == 1) {
                unit = "拾";
            } else {
                unit = "";
            }
            if(i >= 8){
                if (mod[i] == 0) {
                    boolean flag = false;
                    int loc = 0;
                    for (int k = i; k >= 8; k--) {
                        if (mod[k] != 0){
                            flag = true;
                            loc = k;
                            break;
                        }
                    }

                    if (flag) {
                        sb.append("零");
//                    int[] temp = new int[loc + 1];
//                    for (int j = loc; j >= 0; j--) {
//                        temp[j] = mod[j];
//                    }
//                    parseModArr(temp, loc + 1);
                        i = loc;
                    } else {
                        sb.append(unit);
                        i--;
                    }
                } else {
                    sb.append(upper[mod[i]] + unit);
                    i--;
                }
            } else if (i >= 4) {
                if (mod[i] == 0) {
                    boolean flag = false;
                    int loc = 0;
                    for (int k = i; k >= 4; k--) {
                        if (mod[k] != 0){
                            flag = true;
                            loc = k;
                            break;
                        }
                    }

                    if (flag) {
                        sb.append("零");
//                    int[] temp = new int[loc + 1];
//                    for (int j = loc; j >= 0; j--) {
//                        temp[j] = mod[j];
//                    }
//                    parseModArr(temp, loc + 1);
                        i = loc;
                    } else {
                        sb.append(unit);
                        i--;
                    }
                } else {
                    sb.append(upper[mod[i]] + unit);
                    i--;
                }
            } else {
                if (mod[i] == 0) {
                    boolean flag = false;
                    int loc = 0;
                    for (int k = i; k >= 0; k--) {
                        if (mod[k] != 0){
                            flag = true;
                            loc = k;
                            break;
                        }
                    }

                    if (flag) {
                        sb.append("零");
//                    int[] temp = new int[loc + 1];
//                    for (int j = loc; j >= 0; j--) {
//                        temp[j] = mod[j];
//                    }
//                    parseModArr(temp, loc + 1);
                        i = loc;
                    } else {
                        break;
                    }
                } else {
                    sb.append(upper[mod[i]] + unit);
                    i--;
                }
            }
        }
        return sb.toString();
    }

    /**
     * 支持千亿级，千亿级以上会出错
     * @param mod
     * @param len
     * @return
     */
    public static String parseModArr2(int[] mod,int len){

        StringBuffer sb = new StringBuffer();
        String unit = "";

        //逆向遍历余数数组
        for (int i = len - 1; i >= 0;) {
            if (i > 8) {
                unit = unitMetric[i - 9];
            } else if(i == 8){
                unit = "亿";
            } else if(i > 4) {
                unit = unitMetric[i - 5];
            } else if(i == 4) {
                unit = "万";
            } else if(i == 3) {
                unit = "仟";
            }  else if(i == 2) {
                unit = "佰";
            }  else if(i == 1) {
                unit = "拾";
            } else {
                unit = "";
            }
            if (mod[i] == 0) {
                boolean flag = false;
                int loc = 0;
                for (int k = i; k >= 0; k--) {
                    if (mod[k] != 0){
                        flag = true;
                        loc = k;
                        break;
                    }
                }

                if (flag) {
                    if(i >=8 && loc < 8){
                        sb.append("亿零");
                    } else if(i >=4 && loc < 4) {
                        sb.append("万零");
                    } else {
                        sb.append("零");
                    }
                    i = loc;
                } else {
                    break;
                }
            } else {
                sb.append(upper[mod[i]] + unit);
                i--;
            }
        }

        return sb.toString();
    }

}
