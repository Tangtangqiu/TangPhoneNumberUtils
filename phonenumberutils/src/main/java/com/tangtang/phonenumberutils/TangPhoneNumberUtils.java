package com.tangtang.phonenumberutils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TangPhoneNumberUtils {

    private static final String TAG = "PhoneNumberUtils";

    public static boolean isValidNumber(String phoneNumber) {
        String trim = phoneNumber.trim();
        if (TextUtils.isEmpty(trim)) {
            return false;
        } else {
            if (trim.length() > 18) {//考虑到网络号码
                return false;
            } else {
                Pattern pattern = Pattern.compile("\\+?\\d{3,20}");
                Matcher matcher = pattern.matcher(phoneNumber);
                return matcher.matches();
            }
        }
    }

    /**
     * 判断来电号码是否存在联系人
     *
     * @return true 存在 false 不存在
     */
    public static boolean isEqual(Context context, String phoneBookNumber, String incomingNumber) {

        if (TextUtils.isEmpty(phoneBookNumber) || TextUtils.isEmpty(incomingNumber)) {
            return false;
        }

        String formatPhoneBookNumber = getFormatNumber(context, phoneBookNumber);
        String formatIncomingNumber = getFormatNumber(context, incomingNumber);

        return formatPhoneBookNumber.equals(formatIncomingNumber);
    }

    /**
     * 获取号码（去除了所有空格、符号、区域码）
     *
     * @param number 传入的号码
     * @return 新的号码  格式
     */
    public static String getFormatNumber(Context context, String number) {

        boolean existAddSymbol = isExistAddSymbol(number);
        StringBuilder stringBuilder = new StringBuilder();
        if (existAddSymbol) {
            stringBuilder.append("+");
        }
        String result = matchString(number);
        stringBuilder.append(result);

        String newPhoneNumber = stringBuilder.toString();

        if (existAddSymbol) {//如果有+号需要去掉
            int countryZipCode = CountryCodeUtils.getCountryZipCode(context);
            int length = String.valueOf(countryZipCode).length() + 1;
            return newPhoneNumber.substring(length);
        } else if (number.startsWith("00")) {
            int countryZipCode = CountryCodeUtils.getCountryZipCode(context);
            int length = String.valueOf(countryZipCode).length() + 2;
            if (newPhoneNumber.length() > length)
                return newPhoneNumber.substring(length);
        }
        Log.e(TAG, "getFormatNumber_newPhoneNumber=" + newPhoneNumber);
        return newPhoneNumber;
    }

    /**
     * 获取除去所有符号的号码（保留了+）
     *
     * @param number 传入的号码
     * @return 新号码
     */
    public static String getRemoveSymbolNumber(String number) {
        boolean existAddSymbol = isExistAddSymbol(number);
        StringBuilder stringBuilder = new StringBuilder();
        if (existAddSymbol) {
            stringBuilder.append("+");
        }
        String result = matchString(number);
        stringBuilder.append(result);

        String newPhoneNumber = stringBuilder.toString();
        Log.e(TAG, "getRemoveSymbolNumber_newPhoneNumber=" + newPhoneNumber);
        return newPhoneNumber;
    }

    /**
     * 给号码添加上“+”与区域码
     *
     * @param context
     * @param number  传入的号码
     * @return 新号码
     */
    public static String getAddSymbolNumber(Context context, String number) {
        StringBuilder stringBuilder = new StringBuilder();

        String result = matchString(number);

        if (!isExistAddSymbol(number)) {
            stringBuilder.append("+");
            int countryZipCode = CountryCodeUtils.getCountryZipCode(context);
            stringBuilder.append(countryZipCode);
        }
        stringBuilder.append(result);
        String newPhoneNumber = stringBuilder.toString();
        Log.e(TAG, "getAddSymbolNumber_newPhoneNumber=" + newPhoneNumber);
        return newPhoneNumber;
    }

    /**
     * 给号码添加上"00"与区域码
     *
     * @param context
     * @param number  传入的号码
     * @return 新号码
     */
    public static String getAddDoubleZeroNumber(Context context, String number) {
        StringBuilder stringBuilder = new StringBuilder();
        String result = matchString(number);
        boolean existAddSymbol = isExistAddSymbol(number);

        if (existAddSymbol) {
            stringBuilder.append("00");
        } else {
            if (!result.startsWith("00")) {
                stringBuilder.append("00");
                int countryZipCode = CountryCodeUtils.getCountryZipCode(context);
                stringBuilder.append(countryZipCode);
            }
        }
        stringBuilder.append(result);
        String newPhoneNumber = stringBuilder.toString();
        Log.e(TAG, "getAddDoubleZeroNumber_newPhoneNumber=" + newPhoneNumber);
        return newPhoneNumber;
    }

    private static boolean isExistAddSymbol(String number) {
        return !TextUtils.isEmpty(number) && number.startsWith("+");
    }

    private static String matchString(String number) {
        String regex = "\\D";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        String result = matcher.replaceAll("");
        Log.e(TAG, "matchString_result=" + result);
        return result;
    }

}
