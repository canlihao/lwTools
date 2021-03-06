package com.canlihao.lwutil;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YY_MM_HH_MM = "MM-dd HH:mm";
    public static final String YYMMDDHHMMSS = "yyMMddHHmmss";
    public static final String MM_DD = "MM.dd";
    public static final String YY_MM = "yyyy.MM";
    public static final String YYYY_MM_DD_DOT = "yyyy.MM.dd";
    public static final String YYYY_MM_DD_HH_MM = "yyyy年MM月dd日   HH:mm";

    private static String TAG = "Tools";

    /**
     * 格式化消息列表时间
     *
     * @param milliseconds
     * @return
     */
    public static String dateFormatTime(long milliseconds) {
        Date date = new Date(milliseconds);// 计算的时间
        Date today = new Date(System.currentTimeMillis());// 当前时间
        int hour = date.getHours();
        // 判断是否是今天
        if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth()
                && today.getDate() == date.getDate()) {
            return completionTime(hour) + ":" + completionTime(date.getMinutes());
        } else if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth()
                && today.getDate() - 1 == date.getDate()) {
            // 昨天
            return "昨天 " + completionTime(hour) + ":" + completionTime(date.getMinutes());
        } else if (today.getYear() == date.getYear() && today.getMonth() == date.getMonth()
                && today.getDate() - 2 == date.getDate()) {
            // 前天
            return "前天 " + completionTime(hour) + ":" + completionTime(date.getMinutes());
        } else {
            return DateToFormatDate(date, YYYY_MM_DD);
        }
    }

    public static String completionTime(int num) {
        return num < 10 ? ("0" + num) : (num + "");
    }

    /**
     * 判断身份证号码是否正确
     *
     * @param idNum 身份证号码
     */
    public static boolean checkUserId(String idNum) {
        int idLength = idNum.length();
        if (idLength != 15 && idLength != 18) {
            return false;
        }
        String m = "";
        String d = "";
        if (idLength == 15) {
            m = idNum.substring(8, 10);
            d = idNum.substring(10, 12);
        } else {
            m = idNum.substring(10, 12);
            d = idNum.substring(12, 14);
        }
        int mInt = Integer.parseInt(m);
        int dInt = Integer.parseInt(d);
        if (mInt > 12 || dInt > 31) {
            return false;
        }
        if (idLength == 15) {
            return true;
        }

        int w[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            String a = idNum.substring(i, i + 1);
            int aInt = Integer.parseInt(a);
            sum += aInt * w[i];
        }
        int mod = sum % 11;
        boolean isValid = false;
        String lastC = idNum.substring(17, 18);
        switch (mod) {
            case 0:
                isValid = (lastC.equals("1") ? true : false);
                break;
            case 1:
                isValid = (lastC.equals("0") ? true : false);
                break;
            case 2:
                isValid = (lastC.equals("X") ? true : false);
                if (!isValid) {
                    isValid = (lastC.equals("x") ? true : false);
                }
                break;
            case 3:
                isValid = (lastC.equals("9") ? true : false);
                break;
            case 4:
                isValid = (lastC.equals("8") ? true : false);
                break;
            case 5:
                isValid = (lastC.equals("7") ? true : false);
                break;
            case 6:
                isValid = (lastC.equals("6") ? true : false);
                break;
            case 7:
                isValid = (lastC.equals("5") ? true : false);
                break;
            case 8:
                isValid = (lastC.equals("4") ? true : false);
                break;
            case 9:
                isValid = (lastC.equals("3") ? true : false);
                break;
            case 10:
                isValid = (lastC.equals("2") ? true : false);
                break;
            default:
                break;
        }
        return isValid;
    }

    /**
     * 判断用户名是否符合要求
     *
     * @param name 用户名
     * @return
     */
    public static String checkUserName(String name) {

        int nameLength = name.length();
        if (nameLength < 6 || nameLength > 30) {
            return "用户名不能少于6位且不能大于30位，请您重新设置";
        }

        if (Tools.isNumeric(name) && nameLength > 11) {
            return "纯数字用户名不能超过11位，请您重新设置。";
        }

        if (nameLength == 15 || nameLength == 18) {

            String firstName = "";
            char lastName;
            if (nameLength == 15) {
                firstName = name.substring(0, 14);
                lastName = name.charAt(14);
            } else {
                firstName = name.substring(0, 17);
                lastName = name.charAt(17);
            }

            if (Tools.isNumeric(firstName) && Character.isLetter(lastName)) {
                return "用户名不能与身份证号相似，请您重新设置。";
            }
        }

        String curChar_f = name.substring(0, 1);
        if (curChar_f.equals("_") || curChar_f.equals("-")) {
            return "用户名的首位不可以是\"_\"或\"-\"，请您重新设置。";
        }

        String regex1 = "^[a-zA-Z0-9_-]{1,}$";
        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(name);
        if (m1.matches()) {
            return "true";
        } else {
            return "用户名必须是数字与字母的组合，请您重新设置。";
        }

    }

    /**
     * 只显示身份证号码中的生日，其他用*代替
     *
     * @param mId 身份证号码
     * @return
     */
    public static String encryIdNum(String mId) {
        String mEncryId = "";
        StringBuffer buffer = new StringBuffer();

        if (mId.length() == 15) {
            buffer.append("******");
            buffer.append(mId.substring(6, 12));
            buffer.append("***");
        } else if (mId.length() == 18) {
            buffer.append("******");
            buffer.append(mId.substring(6, 14));
            buffer.append("****");
        }
        mEncryId = buffer.toString();
        return mEncryId;
    }

    /**
     * 包装用户真实姓名
     *
     * @param mTrueName 待处理的真实姓名
     * @return 处理过后的真实姓名
     */
    public static String dealTrueName(String mTrueName) {
        try {
            StringBuffer buffer = new StringBuffer();
            int length = mTrueName.length();
            if (length < 2) {
                throw new Exception();
            } else if (length == 2) {
                buffer.append("*");
                buffer.append(mTrueName.substring(1));
            } else if (length == 3) {
                buffer.append(mTrueName.substring(0, 1));
                buffer.append("*");
                buffer.append(mTrueName.substring(2, 3));
            } else if (length > 3) {
                buffer.append(mTrueName.substring(0, 1));
                for (int i = 1; i < length - 1; i++) {
                    if (i % 2 == 0) {
                        buffer.append("*");
                    } else {
                        buffer.append(mTrueName.substring(i, i + 1));
                    }
                }
                buffer.append(mTrueName.substring(length - 1, length));
            }
            mTrueName = buffer.toString();
            return mTrueName;
        } catch (Exception e) {
            return mTrueName;
        }
    }

    /**
     * 判断密码是否符合
     */
    public static String isCorrectPWD(String password, String phoneNumber, String userName) {
        String value = password.trim();
        String lowPw = value.toLowerCase();
        String regex1 = "^[a-zA-Z0-9]{6,16}$";
        String regex2 = "^[a-zA-Z]{6,16}$";
        String regex3 = "^[0-9]{6,16}$";

        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(value);
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(value);
        Pattern p3 = Pattern.compile(regex3);
        Matcher m3 = p3.matcher(value);

        if (value.length() < 6) {
            return "密码不能少于6位，请您重新设置。";
        }
        if (value.length() > 16) {
            return "密码不能超过16位，请您重新设置。";
        }
        if (!m1.matches()) {
            return "密码不能含有特殊符号和空格，请您重新设置。";
        }
        if (m2.matches()) {
            return "请不要设置纯字母的密码，请您重新设置。";
        }
        if (userName != null && !userName.equals("")) {
            String lowUid = userName.toLowerCase();

            if (lowUid.equals(lowPw)) {
                return "请不要将自己的账号作为密码，请您重新设置。";
            }
        }
        if (m3.matches()) {
            if (digitCheck(value)) {
                return "密码中不能出现连续4位及以上数字升、降序排列，请您重新设置。";
            }
            if (digitCount(value)) {
                return "密码中同一数字不能出现连续3次及以上重复，请您重新设置。";
            }
            if (CountChar(value, 2, 3)) {
                return "密码中不能出现连续2个数字连续3次及以上重复，请您重新设置。";
            }
            if (CountChar(value, 3, 2)) {
                return "密码中不能出现连续3个数字连续2次及以上重复，请您重新设置。";
            }
        }

        int moblileLength = phoneNumber.length();
        if (moblileLength > 7) {
            String pre = phoneNumber.substring(0, 6);
            String suf = phoneNumber.substring(moblileLength - 6, moblileLength);
            if (value.contains(pre) || value.contains(suf)) {
                return "密码不能是电话号码前、后6位，请您重新设置。";
            }
        }

        return "true";
    }

    /**
     * 判断密码是否符合
     */
    public static String isCorrectPWD(String password, String phoneNumber) {
        String value = password.trim();
        String lowPw = value.toLowerCase();
        String regex1 = "^[a-zA-Z0-9]{6,16}$";
        String regex2 = "^[a-zA-Z]{6,16}$";
        String regex3 = "^[0-9]{6,16}$";

        Pattern p1 = Pattern.compile(regex1);
        Matcher m1 = p1.matcher(value);
        Pattern p2 = Pattern.compile(regex2);
        Matcher m2 = p2.matcher(value);
        Pattern p3 = Pattern.compile(regex3);
        Matcher m3 = p3.matcher(value);

        if (value.length() < 6) {
            return "密码不能少于6位，请您重新设置。";
        }
        if (value.length() > 16) {
            return "密码不能超过16位，请您重新设置。";
        }
        if (!m1.matches()) {
            return "密码不能含有特殊符号和空格，请您重新设置。";
        }
        if (m2.matches()) {
            return "请不要设置纯字母的密码，请您重新设置。";
        }
        if (m3.matches()) {
            if (digitCheck(value)) {
                return "密码中不能出现连续4位及以上数字升、降序排列，请您重新设置。";
            }
            if (digitCount(value)) {
                return "密码中同一数字不能出现连续3次及以上重复，请您重新设置。";
            }
            if (CountChar(value, 2, 3)) {
                return "密码中不能出现连续2个数字连续3次及以上重复，请您重新设置。";
            }
            if (CountChar(value, 3, 2)) {
                return "密码中不能出现连续3个数字连续2次及以上重复，请您重新设置。";
            }
        }

        int moblileLength = phoneNumber.length();
        if (moblileLength > 7) {
            String pre = phoneNumber.substring(0, 6);
            String suf = phoneNumber.substring(moblileLength - 6, moblileLength);
            if (value.contains(pre) || value.contains(suf)) {
                return "密码不能是电话号码前、后6位，请您重新设置。";
            }
        }

        return "true";
    }

    /**
     * 检查密码中不能出现 dn个数字的连续rn次及以上
     */
    private static boolean CountChar(String password, int dn, int rn) {
        boolean flag = false;
        String subString = "";
        int pLength = password.length();
        for (int loop = 0; loop < dn; loop++) {
            int repeatCount = 0;
            subString = password.substring(loop, loop + dn);
            for (int i = loop; (i + dn * 2) <= pLength; i++) {
                // String lString = password.substring(i+dn, dn*2+i);
                if (subString.equals(password.substring(i + dn, dn * 2 + i))) {
                    repeatCount++;
                    if (repeatCount >= rn - 1) {
                        loop += dn;
                        flag = true;
                        break;
                    }
                }
            }
        }

        return flag;
    }

    /**
     * 判断数字连续不能超过3次
     */
    private static boolean digitCount(String password) {
        String c1, c2, c3;
        int pwLength = password.length();
        for (int i = 0; (i + 3) <= pwLength; i++) {
            c1 = password.substring(i, i + 1);
            c2 = password.substring(i + 1, i + 2);
            c3 = password.substring(i + 2, i + 3);
            int in1 = Integer.parseInt(c1);
            int in2 = Integer.parseInt(c2);
            int in3 = Integer.parseInt(c3);
            if ((0 < in1 && in1 < 9) && (0 < in2 && in2 < 9) && (0 < in3 && in3 < 9)) {
                if ((in1 == in2) && (in2 == in3)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 判断密码是否为升降序如：1234567
     */
    private static boolean digitCheck(String password) {
        String c1, c2, c3, c4;
        int pwLength = password.length();
        for (int i = 0; (i + 4) <= pwLength; i++) {
            c1 = password.substring(i, i + 1);
            c2 = password.substring(i + 1, i + 2);
            c3 = password.substring(i + 2, i + 3);
            c4 = password.substring(i + 3, i + 4);
            int in1 = Integer.parseInt(c1);
            int in2 = Integer.parseInt(c2);
            int in3 = Integer.parseInt(c3);
            int in4 = Integer.parseInt(c4);
            if ((0 < in1 && in1 < 9) && (0 < in2 && in2 < 9) && (0 < in3 && in3 < 9) && (0 < in4 && in4 < 9)) {
                if ((in1 == in2 + 1) && (in2 == in3 + 1) && (in3 == in4 + 1) || (in1 == in2 - 1) && (in2 == in3 - 1)
                        && (in3 == in4 - 1)) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 判断邮箱格式是否正确
     *
     * @param strEmail 带验证邮箱地址
     * @return true 正确 false 错误
     */
    public static boolean isEmail(String strEmail) {
        // String strPattern =
        // "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        String strPattern = "[a-zA-Z0-9]{1,}[a-zA-Z0-9_-]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail.trim());
        if (m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 将参数和值封装成xml节点数据
     */
    public static String format2Xml(String element, String value) {
        StringBuilder builder = new StringBuilder();
        if (!isEmpty(element)) {
            value = value == null ? "" : value;
            builder.append("<" + element + ">").append("<![CDATA[").append(value).append("]]>")
                    .append("</" + element + ">");
        }
        return builder.toString();
    }

    /**
     * 将参数和值封装成xml节点数据
     */
    public static String format2Xml(String element, int value) {
        StringBuilder builder = new StringBuilder();
        if (!isEmpty(element)) {
            builder.append("<" + element + ">").append("<![CDATA[").append(value).append("]]>")
                    .append("</" + element + ">");
        }
        return builder.toString();
    }

    /**
     * 将参数和值封装成xml节点数据
     */
    public static String format2Xml(String element, boolean value) {
        StringBuilder builder = new StringBuilder();
        if (!isEmpty(element)) {
            builder.append("<" + element + ">").append("<![CDATA[").append(value).append("]]>")
                    .append("</" + element + ">");
        }
        return builder.toString();
    }

    /**
     * 得到key
     */
    public static String getKey(String... keys) {
        if (keys == null)
            return "";
        StringBuffer sbKey = new StringBuffer();
        for (int i = 0; i < keys.length; i++) {
            sbKey.append(keys[i]);
            if (i == keys.length - 1) {
                break;
            }
            sbKey.append(File.separator);
        }
        return sbKey.toString();
    }

    /**
     * 判断字符长度
     */
    public static boolean isLength(String str, int maxLen) {
        char[] cs = str.toCharArray();
        int count = 0;
        int last = cs.length;
        for (int i = 0; i < last; i++) {
            if (cs[i] > 255)
                count += 2;
            else
                count++;
        }
        if (count >= maxLen)
            return true;
        else
            return false;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位>
     */
    public static int getLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 格式化显示金额字符串 "0.00"格式
     *
     * @param amount 要转换的金额double数字
     * @return 格式化后的金额字符串
     */
    public static String getAmountFormat(double amount) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(amount);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化显示金额字符串 "0.00"格式
     */
    public static String getStringAmountFormat(String amount) {
        try {
            double amount2 = Double.parseDouble(amount);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(amount2);
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 格式化显示金额字符串 "0.00"格式
     */
    public static String getStringAmountFormat1(String amount) {
        try {
            if (amount == null || amount.trim().equals("")) {
                amount = "0.00";
            }
            double amount2 = Double.parseDouble(amount);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(amount2);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化显示金额字符串 "0.00"格式
     *
     * @param amount 要转换的金额字符串
     * @return 格式化后的金额字符串
     */
    public static String getAmountFormat(String amount) {
        try {
            Double db = Double.parseDouble(amount);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(db);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化显示金额字符串(如果输入参数转换后为空字符则返回默认0.00) "0.00"格式
     *
     * @param amount 要转换的金额字符串
     * @return 格式化后的金额字符串
     */
    public static String getAmountFormatWithEmpty(String amount) {
        amount = getAmountFormat(amount);
        if (amount.equals("")) {
            return "0.00";
        } else {
            return amount;
        }
    }

    /**
     * 格式化显示净值字符串(如果输入参数转换后为空字符则返回默认0.0000) "0.0000"格式
     *
     * @param amount 要转换的净值字符串
     * @return 格式化后的净值字符串
     */
    public static String getNetvalueFormatWithEmpty(String amount) {
        try {
            Double db = Double.parseDouble(amount);
            DecimalFormat df = new DecimalFormat("0.0000");
            return df.format(db);
        } catch (Exception e) {
            return "0.0000";
        }
    }

    /**
     * 格式化显示净值字符串(不四舍五入，只保留4位小数、出现异常返回"") "#.####"格式
     *
     * @param netvalue 要转换的净值字符串
     * @return 格式化后的净值字符串
     */
    public static String getNetvalueFormat(String netvalue) {
        try {
            // 得到截取后的字符串
            String s = cutDecimal(netvalue, 4);
            double db = Double.parseDouble(s);
            DecimalFormat df = new DecimalFormat("#.####");
            return df.format(db);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 补前导零，截取指定数量的小数位，不四舍五入
     */
    private static String cutDecimal(String value, int count) throws NumberFormatException {
        // 检查是否为数值
        // double tempD = Double.parseDouble(value);
        Double.parseDouble(value);
        // 如果不是以零开头补零
        String tempS = value.startsWith(".") ? "0" + value : value;
        // 找到截取的位置
        int index = tempS.indexOf(".") + 1 + count;
        int end = index > tempS.length() ? tempS.length() : index;

        String s = tempS.substring(0, end);

        return s;
    }

    /**
     * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位
     *
     * @param str                原始字符串
     * @param specialCharsLength 截取长度(汉、日、韩文字符长度为2)
     * @return
     */
    public static String trim(String str, int specialCharsLength) {
        if (str == null || "".equals(str) || specialCharsLength < 1) {
            return "";
        }
        char[] chars = str.toCharArray();

        int charsLength = getCharsLength(chars, specialCharsLength);
        if (charsLength < chars.length)
            return new String(chars, 0, charsLength) + "...";
        return new String(chars, 0, charsLength);
    }

    /**
     * 获取一个指定格式的String date
     *
     * @param figure 相对时间上加减 相应月数
     * @param format 格式化
     * @param date   相对时间
     * @return String date
     */
    public static String getStrDataDistanceByMonth(int figure, String format, Date date) {
        SimpleDateFormat asf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, figure);
        Date time = calendar.getTime();
        return asf.format(time);
    }

    /**
     * 获取加减月份前的日期
     *
     * @param figure 相对时间上加减 相应月数
     * @param format 格式化
     * @param date   相对时间
     * @return String date
     * @throws ParseException 非法异常
     */
    public static String getStrDataDistanceByMonth(int figure, String format, String date) throws ParseException {
        SimpleDateFormat asf = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        Date tempDate = asf.parse(date);
        calendar.setTime(tempDate);
        calendar.add(Calendar.MONTH, figure);
        Date time = calendar.getTime();
        return asf.format(time);
    }

    public static String getNatureStrDataDistance(int figure, String date, String format) {
        try {
            SimpleDateFormat asf = new SimpleDateFormat(format);
            Calendar calendar = Calendar.getInstance();
            Date tempDate = asf.parse(date);
            calendar.setTime(tempDate);
            calendar.add(Calendar.MONTH, figure);
            calendar.set(Calendar.DATE, 1);
            Date time = calendar.getTime();
            return asf.format(time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return date;

    }

    public static long getLongDataDistanceByMonth(int figure, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, figure);
        return calendar.getTimeInMillis();
    }

    public static Calendar getCalendarDistanceByYear(int figure, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, figure);
        return calendar;
    }

    /**
     * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
     *
     * @param chars              一段字符
     * @param specialCharsLength 输入长度，汉、日、韩文字符长度为2
     * @return 输出长度，所有字符均长度为1
     */
    public static int getCharsLength(char[] chars, int specialCharsLength) {
        int count = 0;
        int normalCharsLength = 0;
        for (int i = 0; i < chars.length; i++) {
            int specialCharLength = getSpecialCharLength(chars[i]);
            if (count <= specialCharsLength - specialCharLength) {
                count += specialCharLength;
                normalCharsLength++;
            } else {
                break;
            }
        }
        return normalCharsLength;
    }

    /**
     * 计算一段字符的长度，汉、日、韩文字符长度为2，其他为1
     *
     * @param chars 要计算长度的char数组
     * @return
     */
    public static int getTotalCharsLength(char[] chars) {
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            int specialCharLength = getSpecialCharLength(chars[i]);
            count += specialCharLength;
        }
        return count;
    }

    /**
     * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
     *
     * @param c 字符
     * @return 字符长度
     */
    private static int getSpecialCharLength(char c) {
        if (isLetter(c)) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     *
     * @param c, 需要判断的字符
     * @return boolean, 返回true,Ascill字符
     */
    private static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0;
    }

    /**
     * 是否为空
     */
    public static boolean isEmpty(String value) {
        if (value == null|| value.length() == 0 ||  "null".equals(value) || "\"\"".equals(value)) {
            return true;
        } else
            return false;
    }

    /**
     * 是否为数字
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        String str = obj.toString();
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 包装要显示的卡号信息，显示前四位后四位，中间用四位*代替
     *
     * @param cardNum 待处理卡号
     * @return 处理过后的卡号
     */
    public static String dealTheCradNum(String cardNum) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append(cardNum.substring(0, 4));
            buffer.append("****");
            int length = cardNum.length();
            if (length < 8) {
                throw new Exception();
            }
            buffer.append(cardNum.substring(length - 4, length));
            cardNum = buffer.toString();
            return cardNum;
        } catch (Exception e) {
            return cardNum;
        }
    }

    /**
     * 包装要显示的卡号信息，显示后四位，前面用四位*代替
     *
     * @param cardNum 待处理卡号
     * @return 处理过后的卡号
     */
    public static String showAcountNum(String cardNum) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("****");
            int length = cardNum.length();
            if (length < 4) {
                throw new Exception();
            }
            buffer.append(cardNum.substring(length - 4, length));
            cardNum = buffer.toString();
            return cardNum;
        } catch (Exception e) {
            return cardNum;
        }
    }

    /**
     * 包装要显示的手机号信息，显示前三位后后位，中间用五位*代替
     *
     * @param phoneNum 待处理手机号
     * @return 处理过后的手机号
     */
    public static String dealThePhoneNum(String phoneNum) {
        try {
            int length = phoneNum.length();
            if (length < 11) {
                throw new Exception();
            }
            StringBuffer buffer = new StringBuffer();
            buffer.append(phoneNum.substring(0, 3));
            buffer.append("*****");
            buffer.append(phoneNum.substring(length - 3, length));
            phoneNum = buffer.toString();
            return phoneNum;
        } catch (Exception e) {
            return phoneNum;
        }
    }

    /**
     * 字符串按字节截取
     *
     * @param str 原字符
     * @param len 截取长度
     */
    public static String splitString(String str, int len) {
        if(str.length()>len){
            return splitString(str, len, "...");
        }else{
            return str;
        }

    }

    /**
     * 字符串按字节截取
     *
     * @param str   原字符
     * @param len   截取长度
     * @param elide 省略符
     */
    public static String splitString(String str, int len, String elide) {
        if (str == null)
            return "";
        int strlen = str.length();
        if (strlen - len > 0) {
            str = str.substring(0, len) + elide.trim();
        }
        return str;
    }

    /**
     * 格式化利率
     *
     * @param strRetestRate 利率的字符串
     * @return 返回最多保留4位小数的百分比利率串
     */
    public static String formatReterestRate(String strRetestRate) {
        double retestRate;
        try {
            retestRate = Double.parseDouble(strRetestRate);
        } catch (Exception e) {
            return "0%";
        }
        DecimalFormat df = new DecimalFormat("####.####");
        return df.format(retestRate) + "%";
    }

    /**
     * 格式化利率(不四舍五入，只保留最多4位小数) "#.####"格式
     *
     * @param strRetestRate 利率的字符串
     * @return 返回最多保留4位小数的百分比利率串
     */
    public static String formatReterestRateNew(String strRetestRate) {
        if (isEmpty(strRetestRate) || strRetestRate.equalsIgnoreCase("null")) {
            return "";
        }
        try {
            // 得到截取后的字符串
            String s = cutDecimal(strRetestRate, 4);

            double db = Double.parseDouble(s);
            DecimalFormat df = new DecimalFormat("#.####");
            return df.format(db) + "%";
        } catch (Exception e) {
            return strRetestRate;
        }
    }

    /**
     * 判断是否为0，价格显示时，为零则要显示
     */
    public static boolean isStringZero(String str) {
        if (str == null)
            return false;
        String s = str.replace(".", "");
        try {
            int num = Integer.valueOf(s);
            if (num == 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 改变背景的透明度
     */
    public static void changeBackgroundAlpha(View v) {
        Drawable mDrawable1 = v.getBackground();
        mDrawable1.setAlpha(200);
        v.invalidate();
        mDrawable1.setAlpha(255);
        v.invalidate();
    }

    /**
     * 改变背景的透明度
     */
    public static void changeBackgroundAlpha(View v, int alpha) {
        Drawable mDrawable1 = v.getBackground();
        mDrawable1.setAlpha(alpha);
        v.invalidate();
    }

    /*************************** 有关字符串处理方法 **************************/

    /**
     * 去除String字符串中指定字符
     *
     * @param str 需去除指定字符的字符串
     * @param s   指定去除的字符
     * @return 处理后的字符串 注：已测试通过
     */
    public static String split(String str, String s) {
        StringBuffer sb = new StringBuffer();
        String[] str2 = str.split(s);
        for (int i = 0; i < str2.length; i++)
            sb.append(str2[i]);
        return sb.toString();
    }

    /**
     * 将日期类型转换为Date类型
     */
    public static String dateStringToDate(String dateString) {
        if (isEmpty(dateString)) {
            return "";
        }
        if (dateString.length() < 8) {
            return "";
        } else if (dateString.length() > 8) {
            return dateString;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(dateString);
        sb.insert(4, "-");
        sb.insert(7, "-");
        return sb.toString();
    }

    /**
     * 将日期类型转换为Date类型
     */
    public static String yMStringToDate(String dateString) {
        StringBuffer sb = new StringBuffer();
        sb.append(dateString);
        sb.insert(4, "-");
        return sb.toString();
    }

    /**
     * 对形如2010-10-10格式的日期转换成20101010
     */
    public static String dateToDateString(String date) {
        if (isEmpty(date)) {
            return null;
        }
        String date2 = date.replaceAll("-", "");
        return date2.trim();
    }

    public static boolean isNumberDecimal(String decimal) {
        try {
            Float.parseFloat(decimal);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    /*************************** 有关日期/时间处理方法 *************************/
    /**
     * 把日期从旧到新排序。
     *
     * @param monthList 月份列表或日期列表 列表元素格式如：20110102（日期）、201108（年月）
     * @return int[]
     */
    public static int[] sortMonth(String[] monthList) {
        if (monthList != null) {
            int len = monthList.length;
            int[] temp = new int[len];
            int t = 0;
            for (int i = 0; i < len; i++) {
                temp[i] = Integer.parseInt(monthList[i]);
            }
            for (int i = 0; i < len; i++) {
                for (int j = i + 1; j < len; j++) {
                    if (temp[i] > temp[j]) {
                        t = temp[i];
                        temp[i] = temp[j];
                        temp[j] = t;
                    }
                }
            }
            return temp;
        } else
            return null;
    }

    public static String longToFormatDate(Long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date(time));
        return date;
    }

    /**
     * 格式化时间
     */
    public static String DateToFormatDate(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(time);
        return date;
    }

    /**
     * 将日期转成日历
     */
    public static Calendar dateToCalendar(Date time) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(time);
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(4, 6);
        String day = dateStr.substring(6);

        Calendar currentTime = Calendar.getInstance();

        currentTime.clear();
        currentTime.set(Calendar.YEAR, Integer.parseInt(year));
        currentTime.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        currentTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return currentTime;
    }

    /**
     * 将字符串转成Data
     */
    public static Date StringToDate(String date, String formart) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formart);
        return sdf.parse(date);
    }

    /**
     * 将字符串转成Data
     */
    public static String StringToDate(String date, String fromFormart, String toFormart) {
        if (date != null && fromFormart != null && toFormart != null) {
            try {
                SimpleDateFormat fromSDF = new SimpleDateFormat(fromFormart);
                SimpleDateFormat sdf = new SimpleDateFormat(toFormart);
                Date tempDate = fromSDF.parse(date);
                return sdf.format(tempDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static String StringToDateByCut(String date) {
        if (date != null && date.length() >= 8) {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            return year + "-" + month + "-" + day;
        }
        return date;
    }

    /**
     * 获得指定时间戳
     */
    public static String getDateToString(String format) {

        return new SimpleDateFormat(format).format(new Date()).toString();
    }

    public static long convert2long(String date, String format) {
        try {
            if (!Tools.isEmpty(date)) {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.parse(date).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 格式化时间
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 格式化时间
     */
    public static Date format(String dateStr, String format) {
        try {
            Date date = new SimpleDateFormat(format).parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化时间
     */
    public static String format(Calendar calendar, String format) {
        return format(calendar.getTime(), format);
    }

    public static Calendar strToCalendar(String dateStr, String format) {
        SimpleDateFormat fromSDF = new SimpleDateFormat(format);
        Date dateBeginDate = null;
        try {
            dateBeginDate = fromSDF.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateBeginDate);
        return calendar;
    }


    /**
     * 格式把Calendar对象格式化化成 yyyy年MM月dd日(星期X)"的样式
     */
    public static String formatDateWithWeek(Calendar calendar) {
        return formatDateWithWeek(calendar, "MM月dd日 周");
    }

    public static String getDateToTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String formatDate(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat(YY_MM);
        return format.format(date);
    }

    /**
     * 取得日期的字符串
     */
    public static String formatDate(Calendar calendar) {
        return formatDate(calendar, "yyyy年MM月dd日");
    }

    /**
     * 取得日期加星期的字符串
     */
    public static String formatDate(Calendar calendar, String format) {
        String formatStr = format(calendar, format);
        return formatStr.substring(5);
    }

    /**
     * 取得日期加星期的字符串
     */
    public static String formatDateWithWeek(Calendar calendar, String format) {
        String formatStr = format(calendar, format);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String week = null;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                week = "日";
                break;
            case Calendar.MONDAY:
                week = "一";
                break;
            case Calendar.TUESDAY:
                week = "二";
                break;
            case Calendar.WEDNESDAY:
                week = "三";
                break;
            case Calendar.THURSDAY:
                week = "四";
                break;
            case Calendar.FRIDAY:
                week = "五 ";
                break;
            case Calendar.SATURDAY:
                week = "六";
                break;
        }
        formatStr = formatStr + week;
        return formatStr;
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += " 星期日";
        }
        if (wek == 2) {
            Week += " 星期一";
        }
        if (wek == 3) {
            Week += " 星期二";
        }
        if (wek == 4) {
            Week += " 星期三";
        }
        if (wek == 5) {
            Week += " 星期四";
        }
        if (wek == 6) {
            Week += " 星期五";
        }
        if (wek == 7) {
            Week += " 星期六";
        }
        return Week;
    }

    /**
     * 取得日期星期几的字符串
     *
     * @param dateStr 20151005
     * @return 2015年10月05日    (星期一)
     */
    public static String getDateWeekStr(String dateStr) {
        String s = "";
        Date d = format(dateStr, YYYY_MM_DD);
        Calendar c = dateToCalendar(d);
        s = formatDateWithWeek(c);
        return s;
    }

    /**
     * 取得日期的字符串
     * @param dateStr 20151005
     * @return 10月05日
     */
    public static String getDateStr(String dateStr) {
        String s = "";
        Date d = format(dateStr, YYYY_MM_DD);
        Calendar c = dateToCalendar(d);
        s = formatDate(c);
        return s;
    }

    /**
     * 返回当前日期后面的五天日期数组(服务器时间) 2015-10-05 12:00:05
     *
     * @return 第0天  ：20151011
     * 第1天  ：20151012
     * 第2天  ：20151013
     * 第3天  ：20151014
     * 第4天  ：20151015
     */
    public static String[] get5DayDate(String serviceTime) {
        MyLog.d(TAG, "取得服务器的时间是：" + serviceTime);
        String[] dates = new String[5];
        String currentYMD = Tools.dateToDateString(serviceTime.substring(0, 10));
        for (int i = 0; i < 5; i++) {
            currentYMD = getNextDate(currentYMD);
            dates[i] = currentYMD;
        }
        return dates;
    }


    /**
     * 返回当前日期及后面的四天日期数组(服务器时间) 2015-10-05 12:00:05
     *
     * @return 第0天  ：20151011
     * 第1天  ：20151012
     * 第2天  ：20151013
     * 第3天  ：20151014
     * 第4天  ：20151015
     */
    public static String[] get5DayDate1(String serviceTime) {
        MyLog.d(TAG, "取得服务器的时间是：" + serviceTime);
        String[] dates = new String[5];
        String currentYMD = Tools.dateToDateString(serviceTime.substring(0, 10));
        currentYMD = getLastDate(currentYMD);
        for (int i = 0; i < 5; i++) {
            currentYMD = getNextDate(currentYMD);
            dates[i] = currentYMD;
        }
        return dates;
    }

    /**
     * 返回当前日期后面的五天日期数组(手机系统时间)
     *
     * @return 第0天  ：20151011
     * 第1天  ：20151012
     * 第2天  ：20151013
     * 第3天  ：20151014
     * 第4天  ：20151015
     */
    public static String[] get5DayDate() {
        String[] dates = new String[5];
        String currentYMD = getCurrentYMD();
        for (int i = 0; i < 5; i++) {
            currentYMD = getNextDate(currentYMD);
            dates[i] = currentYMD;
        }
        return dates;
    }

    /**
     * 取得当前的年月日
     *
     * @return 返回：  20151010
     */
    public static String getCurrentYMD() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDD);
        return dateFormat.format(date);
    }

    /**
     * 得到传入日期的后一天日期
     *
     * @param date 20151010
     * @return 返回   20151011
     */
    public static String getNextDate(String date) {
        String nextDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        try {
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            nextDate = sdf.format(calendar.getTime());
        } catch (ParseException e) {

        }
        return nextDate;
    }

    /**
     * 得到传入日期的前一天日期
     *
     * @param date 20151010
     * @return 返回   20151011
     */
    public static String getLastDate(String date) {
        String nextDate = date;
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        try {
            Date d = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            nextDate = sdf.format(calendar.getTime());
        } catch (ParseException e) {

        }
        return nextDate;
    }

    /***************************************************************************/

    /**
     * 列表倒序
     */
    public static List<Map<String, Object>> reverse(List<Map<String, Object>> list) {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        int size = list.size();
        for (int i = size; i > 0; i--) {
            l.add(list.get(i));
        }
        return l;
    }

    /**
     * 计算总记录页数
     *
     * @param count    总记录数
     * @param pageSize 每页记录数
     */
    public static int getTotalPageCount(String count, String pageSize) {
        int c = Integer.valueOf(count).intValue();
        int ps = Integer.valueOf(pageSize).intValue();
        if (0 == c) { // 总记录数为0
            return 0;
        }
        if (c <= ps) { // 总记录数小于一页
            return 1;
        } else { // 总记录数不为每页记录数的整数倍
            int n = c / ps;
            if (c % ps != 0) {
                n++;
            }
            return n;
        }
    }

    /**
     * @param executePeriod 0：每天;1：每周;2：每月
     * @param executeDate   执行周期为每天：0 执行周期为每周：1、2、3、4、5、6、0(周日)执行周期为每月：1到28、32（月末）
     * @param executeTime   00：上午 01：下午
     * @return
     */
    public static String getExecuteTime(String executePeriod, String executeDate, String executeTime) {
        StringBuilder timeinfo = new StringBuilder();

        if (!Tools.isEmpty(executePeriod)) {
            if ("0".equals(executePeriod))
                timeinfo.append("每天");
            if ("1".equals(executePeriod))
                timeinfo.append("每周");
            if ("2".equals(executePeriod))
                timeinfo.append("每月");
        }

        timeinfo.append(" ");

        if (!Tools.isEmpty(executeDate)) {
            if ("1".equals(executePeriod)) {
                if ("0".equals(executeDate))
                    timeinfo.append("星期天");
                if ("1".equals(executeDate))
                    timeinfo.append("星期一");
                if ("2".equals(executeDate))
                    timeinfo.append("星期二");
                if ("3".equals(executeDate))
                    timeinfo.append("星期三");
                if ("4".equals(executeDate))
                    timeinfo.append("星期四");
                if ("5".equals(executeDate))
                    timeinfo.append("星期五");
                if ("6".equals(executeDate))
                    timeinfo.append("星期六");
            }
            if ("2".equals(executePeriod)) {
                timeinfo.append(executeDate);
            }
        }

        timeinfo.append(" ");

        if (!Tools.isEmpty(executeTime)) {
            if ("00".equals(executeTime))
                timeinfo.append("上午");
            if ("01".equals(executeTime))
                timeinfo.append("下午");
        }

        return timeinfo.toString();
    }

    /**
     * @param executeDate 执行周期为单次：日期（年月日），日期格式：yyyy-MM-dd
     * @param executeTime 00：上午 01：下午
     */
    public static String getExecuteTime(String executeDate, String executeTime) {
        StringBuilder timeinfo = new StringBuilder();

        if (!Tools.isEmpty(executeDate)) {
            Date date = Tools.format(executeDate, "yyyy-MM-dd");
            timeinfo.append(Tools.format(date, "yyyy年  MM月  dd日"));
        }

        timeinfo.append(" ");

        if (!Tools.isEmpty(executeTime)) {
            if ("00".equals(executeTime))
                timeinfo.append("上午");
            if ("01".equals(executeTime))
                timeinfo.append("下午");
        }

        return timeinfo.toString();
    }

    /**
     * 在日期前加一个0 如　1 返回 "01" 10 返回　"10"
     */
    public static String formatDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return String.valueOf(day);
    }

    /**
     * 从字符串日期中滤出年月日 如2011-11-11 Calander.YEAR 得 2011 Calander.MONTH 得11
     * Calander.DAY_OF_MONTH 得11
     */
    public static int getDateFieldFormStringDate(int field, String dateString) {
        if (isEmpty(dateString)) {
            throw new RuntimeException("Empty dateString");
        }
        if (dateString.length() < 10) {
            throw new RuntimeException("dateString length error");
        }

        String[] dates = dateString.split("-");

        switch (field) {
            case Calendar.YEAR:
                return Integer.parseInt(dates[0]);
            case Calendar.MONTH:
                return Integer.parseInt(dates[1]);
            case Calendar.DAY_OF_MONTH:
                return Integer.parseInt(dates[2]);
        }
        return -1;
    }

    /**
     * 从字符串日期中滤出年月 如2011-11 Calander.YEAR 得 2011 Calander.MONTH 得11
     */
    public static int getDateFieldFormStringYearAndMonth(int field, String dateString) {
        if (isEmpty(dateString)) {
            throw new RuntimeException("Empty dateString");
        }
        if (dateString.length() < 7) {
            throw new RuntimeException("dateString length error");
        }

        String[] dates = dateString.split("-");

        switch (field) {
            case Calendar.YEAR:
                return Integer.parseInt(dates[0]);
            case Calendar.MONTH:
                return Integer.parseInt(dates[1]);
        }
        return -1;
    }

    /**
     * 获取根据屏幕获取实际大小 在自定义控件中，根据屏幕的大小来获取实际的大小
     */
    public static int getActualSize(Context ctx, int orgSize) {
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        float density = (float) displayMetrics.density;

        return (int) (orgSize * density);
    }

    /**
     * 把list的String用“,”拼接起来 如list有“我”、“是”、“超”、“人”，拼接后是：“我,是,超,人”
     *
     * @param list 拼接前的list
     * @return nectString 拼接后的字符串
     */
    public static String connectStringByComma(List<String> list) {
        String nectString = "";
        if (list != null && list.size() > 0) {
            int i;
            int len = list.size();
            for (i = 0; i < len; i++) {
                nectString = nectString + list.get(i) + ",";
            }
            nectString = nectString.substring(0, nectString.length() - 1);
        }
        return nectString;
    }

    /**
     * 获取地球上，两个经纬度点的距离
     */
    private final static double EARTH_RADIUS = 6378.137;

    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.000);
        double radLat2 = (lat_b * Math.PI / 180.000);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.000;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.floor(s * 1000 + 0.5) / 1000;
        return s;
    }

    /**
     * 获取屏幕的深度值
     */
    public static float getScreenDensity(Context ctx) {
        WindowManager mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        return density;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth(Context ctx) {
        WindowManager mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight(Context ctx) {
        WindowManager mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        return mWindowManager.getDefaultDisplay().getHeight();
    }

    public static String getStrFromMap(Map<String, Object> map, String key) {
        Object ob = map.get(key);
        if (ob != null) {
            return ob.toString();
        }
        return "";
    }

    /**
     * 判断电话号码是否正确
     * 1、移动号段有134,135,136,137, 138,139,147,150,151, 152,157,158,159,178,182,183,184,187,188。
     * 2、联通号段有130，131，132，155，156，185，186，145，176 ,170,171 。
     * 3、电信号段有133，153，177，180，181，189 ,199。
     */
    public static boolean isMobilePhoneNumber(String phoneNumber) {

        // 移动手机号码
        String mPattern = "(^134[0-8]\\d{7}$)|(^13[5-9]\\d{8}$)|(^14[7]\\d{8}$)|(^15[012789]\\d{8}$)|(^17[8]\\d{8}$)|(^18[23478]\\d{8}$)";

        // 联通号码
        String uniPattern = "(^1349\\d{7}$)|(^13[012]\\d{8}$)|(^15[56]\\d{8}$)|(^18[56]\\d{8}$)|(^14[5]\\d{8}$)|(^17[6]\\d{8}$)|(^17[0]\\d{8}$)|(^17[1]\\d{8}$)";

        // 电信号码验证正则表达式
        String ePattern = "(^1[35]3\\d{8}$)|(^18[019]\\d{8}$)|(^17[37]\\d{8}$)|(^199\\d{8}$)";

        Pattern pm = Pattern.compile(mPattern);
        Matcher mm = pm.matcher(phoneNumber.trim());

        Pattern um = Pattern.compile(uniPattern);
        Matcher mu = um.matcher(phoneNumber.trim());

        Pattern em = Pattern.compile(ePattern);
        Matcher me = em.matcher(phoneNumber.trim());

        if (mm.matches() || mu.matches() || me.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 验证正在备注字符串
     */
    public static boolean formCheckout(String str) {
        if (Tools.isEmpty(str))
            return true;

        boolean result = true;
        char[] charArray = str.toCharArray();

        for (char c : charArray) {
            // 判断是否为大小写英文字母或数字
            result = isLetterOrDigit(c);
            if (!result) {
                // 如果是汉字
                if (c <= 0x9fa5 && c >= 0x4e00) {
                    result = java.nio.charset.Charset.forName("GB2312").newEncoder().canEncode(c);
                } else {
                    result = isSpecialLetter(c);
                }
            }

            if (result == false) {
                return result;
            }
        }

        return result;
    }

    /**
     * 是否是特殊字符
     *
     * @param c
     * @return
     */
    public static boolean isSpecialLetter(char c) {
        String spLetter = ".,-()/=+?!*<>;@";
        if (spLetter.indexOf(c) > -1) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符是否为大小写英文字母或数字
     *
     * @param c
     * @return
     */
    private static boolean isLetterOrDigit(char c) {
        if ((c >= 0x30 && c <= 0x39) || (c >= 0x61 && c <= 0x7a) || c >= 0x41 && c <= 0x5a) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 取得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    public static int getBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 取得图片的高度
     *
     * @param context
     * @param resource
     * @return
     */
    public static int getBitmapHeight(Context context, int resource) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resource, options);
        return options.outHeight;
    }

    /**
     * 将字符串进行utf-8编码
     *
     * @param str
     * @return
     */
    public static String encodeUtf8(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 重写的eques方法
     *
     * @param ob1
     * @param ob2
     * @return
     */
    public static boolean equals(Object ob1, Object ob2) {
        if (ob1 == null) {
            if (ob1 == ob2) {
                return true;
            }
        } else {
            if (ob1.equals(ob2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除空格和特殊符号
     *
     * @param msg
     * @return
     */
    public static String removeSpaces(String msg) {
        String result = "";
        result = msg.replace(" ", "");
//		result = result.replace("%", "");
//		result = result.replace("<", "");
//		result = result.replace(">", "");
//		result = result.replace("{", "");
//		result = result.replace("}", "");
//		result = result.replace("}", "");
//		result = result.replace("^0^", "");
//		result = result.replace("^o^", "");
        return result;
    }

    /**
     * 取得星期几
     *
     * @param dataStr 2015-11-13
     * @return (星期五)
     */
    public static String getWeekByDataStr(String dataStr) {
        String dataStr01 = dateToDateString(dataStr);
        Date d = format(dataStr01, YYYYMMDD);
        Calendar calendar = dateToCalendar(d);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String week = null;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                week = "日";
                break;
            case Calendar.MONDAY:
                week = "一";
                break;
            case Calendar.TUESDAY:
                week = "二";
                break;
            case Calendar.WEDNESDAY:
                week = "三";
                break;
            case Calendar.THURSDAY:
                week = "四";
                break;
            case Calendar.FRIDAY:
                week = "五 ";
                break;
            case Calendar.SATURDAY:
                week = "六";
                break;
        }

        return "(星期" + week + ")";
    }

    /**
     * @return
     */
    public static long getTwoDaydistance(String lastLoginData) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(YYYY_MM_DD);
            long to = df.parse(lastLoginData).getTime();
            long from = df.parse(getCurrentYMD1()).getTime();
            return ((to - from) / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取得当前的年月日
     *
     * @return 返回：  2015-10-10
     */
    public static String getCurrentYMD1() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        return dateFormat.format(date);
    }

    /**
     * 取得当前的年月日
     */
    public static String getCurrentYM() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(YY_MM);
        return dateFormat.format(date);
    }

    /**
     * 取得当前时间
     */
    public static String getData() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return dateFormat.format(date);
    }

    /**
     * 取得时间戳
     *
     * @return
     */
    public static String getCreateTime() {
        long timeStampSec = System.currentTimeMillis() / 1000;
//        MyLog.d("Tools Time" , timeStampSec + "");
        String timestamp = String.format("%010d", timeStampSec);
//        return timestamp;
        //获取10位字符串格式的时间戳
//        String str = String.valueOf(timeStampSec);
//        MyLog.d("Tools Time" , timestamp + " ---- " + str);
        return timestamp;
    }
    /**
     * 将时间戳转换为时间
     */
    public static String getDate2String(Long time) {
        String res;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果它本来就是long类型的,则不用写这一步
       /* MyLog.i("thistime",time);
        long lt = new Long(time);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);*/
        //MyLog.i("thistime",time+"");
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);

    }

    /**
     * 将时间戳转换为年月日
     */
    public static String getDateyear(Long time) {
        String res;
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果它本来就是long类型的,则不用写这一步
       /* MyLog.i("thistime",time);
        long lt = new Long(time);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);*/
        //MyLog.i("thistime",time+"");
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        return format.format(date);

    }

    /**
     * 将当前时间转换为时间戳
     */
    public static String dateToStamp() {
        //获取当前时间
        String currentTime = getData();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(currentTime);
            long ts = date.getTime();
            return String.valueOf(ts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 取得时间戳
     *
     * @return
     */
    public static long getCurrentTime() {
//		String format = Tools.format(new Date(), Tools.YYYY_MM_DD_HH_MM_SS);
//		return format;
        long time = System.currentTimeMillis();
        return time;
    }

    /**
     * 替换文本中的\n和\r
     */
    public static String replaceNewLine(String inputText) {
        if (!isEmpty(inputText)) {
            String replaceContent = inputText.replace("\n", "<br/>");
            String resultContent = replaceContent.replace("\r", "<br/>");
            return resultContent;
        }
        return inputText;
    }

    /**
     * 替换文本中的<br/>
     */
    public static String replaceBr(String inputText) {
        if (!isEmpty(inputText)) {
            String replaceContent = inputText.replace("<br/>", "\n");
            return replaceContent;
        }
        return inputText;
    }

    /**
     * 设置添加屏幕的背景透明度
     * 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Context context, float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        ((Activity) context).getWindow().setAttributes(lp);
    }

//    public static void showChangeToast(Context context, int res) {
//
//        Toast toast = new Toast(context);
//
//        //设置窗体的的内容
//        View view = View.inflate(context, R.layout.show_toast_view,null) ;
//        ImageView ivToast = (ImageView) view.findViewById(R.id.iv_show_toast);
//        ivToast.setImageResource(res);
//
//        toast.setView(view);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//
//        toast.show();
//    }
//
//
//    public static void showNetFailToast(Context context, int res) {
//        Toast toast = new Toast(context);
//        //设置窗体的的内容
//        View view = View.inflate(context, R.layout.show_image_toast_view, null);
//        ImageView ivToast = (ImageView) view.findViewById(R.id.iv_show_toast);
//        ivToast.setImageResource(res);
//
//        toast.setView(view);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//
//        toast.show();
//    }


    public static double change(double a) {
        return a * Math.PI / 180;
    }

    public static double changeAngle(double a) {
        return a * 180 / Math.PI;
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }


    /**
     * SHA加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    /**
     * 验证手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        // // 匹配手机号码正则字符串+匹配固话号码正则字符串（支持手机号码，3-4位区号，7-8位直播号码，1－4位分机号）
        // String phonePatternRuleString =
        // "(d{11})|^((d{7,8})|(d{4}|d{3})-(d{7,8})|(d{4}|d{3})-(d{7,8})-(d{4}|d{3}|d{2}|d{1})|(d{7,8})-(d{4}|d{3}|d{2}|d{1}))$";
        // 匹配手机号码正则字符串
        String mobilePhonePatternRuleString = "^((13[0-9])|(14[6,7])|(15[0-9])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
        try {

            /* 创建Pattern */
            Pattern pattern = Pattern.compile(mobilePhonePatternRuleString);
            /* 将Pattern 以参数传入Matcher作Regular expression */
            Matcher matcher = pattern.matcher(mobiles);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 判断是否为身份证号码
     */
    public static boolean isIdCardNo(String idCardNo) {
        String validation = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
        Pattern pattern = Pattern.compile(validation);
        Matcher matcher = pattern.matcher(idCardNo);
        return matcher.matches();
    }

    /**
     * 判断输入的字符串是否为正确格式的密码
     *
     * @param phone
     * @return
     */
    public static boolean isPassword(String phone) {
        String validation = "^[0-9a-zA-Z]{6,16}$";
        Pattern pattern = Pattern.compile(validation);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 判断输入的字符串是否是邮箱
     *
     * @param code
     * @return
     */
    public static boolean isMail(String code) {
        String validation = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(validation);
        Matcher matcher = pattern.matcher(code);
        return matcher.matches();
    }

    /**
     * 校验Tag Alias 只能是数字,英文字母和中文
     *
     * @param s
     * @return
     */
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static String getNumStr(double obj) { //保留2位小数

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.format(obj);

        return decimalFormat.format(obj);
    }

    public static String getNumStr2(double obj) { //保留整数

        DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.format(obj);

        return decimalFormat.format(obj);
    }

    public static boolean isAppInstalled(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 将phone中间4个字符隐藏为*
     */
    public static String getPhoneNum(String phoneNum) {
        try {
            if (phoneNum == null) return "";

            int length = phoneNum.length();

            if (length > 3) {
                String startNum = phoneNum.substring(0, 3);
                String endNum = phoneNum.substring(length - 4, length);
                phoneNum = startNum + "****" + endNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNum;

    }

    /**
     * 将证件中间5个字符隐藏为*
     */
    public static String getCardNum(String phoneNum) {
        try {
            if (phoneNum == null) return "";

            int length = phoneNum.length();

            if (length > 3) {
                String startNum = phoneNum.substring(0, 3);
                String endNum = phoneNum.substring(length - 4, length);
                phoneNum = startNum + " ***** " + endNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneNum;

    }


    /**
     * 将phone中间5个字符隐藏为*
     */
    public static String getNickNum(String NickNum) {
        try {
            if (NickNum == null) return "";

            int length = NickNum.length();

            if (length > 3) {
                String startNum = NickNum.substring(0, 1);
                String endNum = NickNum.substring(length - 1, length);
                NickNum = startNum + " **** " + endNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NickNum;

    }

    /**
     * 将phone中间5个字符隐藏为*
     */
    public static String getUserNum(String NickNum) {
        try {
            if (NickNum == null) return "";

            int length = NickNum.length();

            if (length == 11) {
                String startNum = NickNum.substring(0, 3);
                String endNum = NickNum.substring(length - 2, length);
                NickNum = startNum + " **** " + endNum;
            } else {
                String startNum = NickNum.substring(0, 1);
                String endNum = NickNum.substring(length - 1, length);
                NickNum = startNum + " **** " + endNum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NickNum;
    }

    /**
     * 将json格式的字符串转成Map对象
     */
    public static Map<String, String> jsonToHashMap(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            Map<String, String> map = new HashMap<>();
            Iterator it = jsonObject.keys();
            // 遍历jsonObject数据，添加到Map对象
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                //注意：这里获取value使用的是optString
                // optString 和getString的区别：单来说就是optString会在得不到你想要的值时候返回空字符串”“，而getString会抛出异常。
                String value = jsonObject.optString(key);
                map.put(key, value);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将html文本内容中包含img标签的图片，宽度变为屏幕宽度，高度根据宽度比例自适应
     **/
//    public static String getNewContent(String htmlText) {
//        try {
//            Document doc = Jsoup.parse(htmlText);
//            Elements elements = doc.getElementsByTag("img");
//            for (Element element : elements) {
//                element.attr("width", "100%")
//                        .attr("height", "auto")
//                        .attr("style", "width: 100%; height: auto;");
//            }
////            Elements elements2 = doc.getElementsByTag("p");
////            for (Element element : elements2) {
////                element.attr("line-height", "0");
////                element.attr("margin", "0");
////                element.attr("border", "0");
////            }
//            return doc.toString();
//        } catch (Exception e) {
//            return htmlText;
//        }
//    }
    public static void setHeaderIconColorBlack(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void setHeaderIconColorWhite(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //实现状态栏图标和文字颜色为浅色
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 获取图片资源
     */
    public static int[] getIntImgArray(Context context, int imgRes) {
        TypedArray mTypedArray = context.getResources().obtainTypedArray(imgRes);
        int[] imgs = new int[mTypedArray.length()];
        //参数含义，第一个参数为 ：所取图片在数组中的索引，第二个参数为：未找到时，返回的默认值。
        for (int i = 0; i < mTypedArray.length(); i++) {
            imgs[i] = mTypedArray.getResourceId(i, 0);
        }
        mTypedArray.recycle();
        return imgs;
    }

    /**
     * 复制
     */
    public static void copy(Context context, String content) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 处理控件在键盘上方的问题
     */
    public static void shuoViewUpKeyborad(Activity context, final View view) {
        final View decorView = context.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);
                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (view.getPaddingBottom() != diff) {
                        view.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (view.getPaddingBottom() != 0) {
                        view.setPadding(0, 0, 0, 0);
                    }
                }
            }
        });
    }

    public static Bitmap getImageViewBitmap(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
        imageView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());  //获取到Bitmap的图片
        imageView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void setTextSize(Context context, TextView textView, int dimenRes) {
        int dimen = context.getResources().getDimensionPixelSize(dimenRes);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen);
    }

    /**
     * 两个时间相差多少
     */
    public static String dateDiff(String startTime, String endTime) {
        MyLog.i("startTime",startTime);
        MyLog.i("endTime",endTime);
        String resultDate = "";
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            MyLog.i("diff",diff+"");
            day = diff / nd;// 计算差多少天
            MyLog.i("day",day+"");

            long hour = diff % nd / nh;// 计算差多少小时
            MyLog.i("hour",hour+"");
            long min = diff % nd % nh / nm;// 计算差多少分钟
            MyLog.i("min",min+"");
            long mit = diff % nd % nh % nm / ns;//计算相差的秒
            MyLog.i("mit",mit+"");
            if (day <= 0) {
                if (hour <= 0) {
                    resultDate = min + "分"+mit+"秒";
                } else {
                    resultDate = hour + "时" + min + "分"+mit+"秒";
                }
            } else {
                resultDate = day + "天" + hour + "时" + min + "分"+mit+"秒";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
    /**
     * 两个时间相差de 小时
     */
    public static int datehour(String startTime, String endTime) {
        int resultDate = 0;
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时

                if (hour <= 0) {
                    resultDate = 0;
                } else {
                    resultDate = Integer.valueOf(hour+"").intValue();
                }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
    /**
     * 两个时间相差分钟
     */
    public static int datemin(String startTime, String endTime) {
        int resultDate = 0;
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long mit = diff % nd % nh % nm / ns;//计算相差的秒

                if (min <= 0) {
                    resultDate = 0;
                } else {
                    resultDate =  Integer.valueOf(min+"").intValue() ;
                }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
    /**
     * 两个时间相差分钟
     */
    public static int datemiao(String startTime, String endTime) {
        int resultDate = 0;
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime()
                    - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long mit = diff % nd % nh % nm / ns;//计算相差的秒

            if (mit <= 0) {
                resultDate = 0;
            } else {
                resultDate =  Integer.valueOf(mit+"").intValue() ;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }
    public static boolean hasPermission(Context c, String p) {
        if (isAndroidM()) {
            return c.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }
     public static boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }
    public static boolean hasAllPermissions(Context c, String... p) {
        if (isAndroidM()) {
            String[] var2 = p;
            int var3 = p.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String s = var2[var4];
                if (!hasPermission(c, s)) {
                    return false;
                }
            }
        }

        return true;
    }
    @TargetApi(23)
    public static void requestPermission(Activity a, int code, String... permissions) {
        List<String> list = new ArrayList();
        String[] var4 = permissions;
        int var5 = permissions.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String permission = var4[var6];
            if (!hasPermission(a, permission)) {
                list.add(permission);
            }
        }

        if (list.size() > 0) {
            a.requestPermissions((String[])list.toArray(new String[0]), code);
        }

    }
    public static Bitmap base64ToBitmap(String base64) {
        Bitmap bitmap = null;

        try {
            byte[] bitmapArray = Base64.decode(base64, 0);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return bitmap;
    }
    //延迟执行
    public static void runDelayed(Runnable runnable, long delayed) {
        (new Handler()).postDelayed(runnable, delayed);
    }
    public static boolean notSupportMD() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isSupportMD() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT;
    }

    public static boolean isAndroidL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isAndroidM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean isAndroidP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
//    TypedValue typedValue = new TypedValue();
//            activity.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
//            btnMenu.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(activity,typedValue.resourceId)));
    public static void setTint(Context context,ImageView view,int resId){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, typedValue, true);
        view.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context,typedValue.resourceId)));
    }
    public static String getExtensionName(String name) {
        String suffix = "";
        int idx = name.lastIndexOf(".");
        if (idx > 0) {
            suffix = name.substring(idx + 1);
        }

        return suffix.toLowerCase();
    }
    public static String getMimeType(String fileName) {
        String mime = "*/*";
        String tmp = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtensionName(fileName));
        if (tmp != null) {
            mime = tmp;
        }

        return mime;
    }
    @SuppressLint("WrongConstant")
    public static void installAPK(Context context, @NonNull File file, String provider) {
        if (file.exists()) {
            Intent intent;
            if (isAndroidM()) {
                intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS", Uri.parse("package:" + context.getPackageName()));
            } else {
                intent = new Intent();
            }

            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(268435456);
            String mime = getMimeType(file.getName());
            if (isAndroidN()) {
                Uri contentUri = FileProvider.getUriForFile(context, provider, file);
                intent.addFlags(1);
                intent.setDataAndType(contentUri, mime);
            } else {
                intent.setDataAndType(Uri.fromFile(file), mime);
            }

            context.startActivity(intent);
        }

    }
}
