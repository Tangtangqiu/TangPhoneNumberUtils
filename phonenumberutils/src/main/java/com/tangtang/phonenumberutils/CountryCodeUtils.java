package com.tangtang.phonenumberutils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import java.util.Locale;

public class CountryCodeUtils {

    private static final String TAG = "CountryCodeUtils";

    public static int getCountryZipCode(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        assert manager != null;
        String countryID = getCurrentCountryIso(context);
//        Log.e(TAG, "countryID=" + countryID);
        int countryZipCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryID);
        Log.e(TAG, "countryZipCode=" + countryZipCode);
        return countryZipCode;
    }
    public static String getCurrentCountryIso(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String result = null;
        if (isNetworkCountryCodeAvailable(manager)) {
            result = getNetworkBasedCountryIso(manager);
            Log.e(TAG, " getNetworkBasedCountryIso_result" + result);
        }

        if (TextUtils.isEmpty(result)) {
            result = getSimBasedCountryIso(manager);
            Log.e(TAG, "getSimBasedCountryIso_result" + result);
        }
        if (TextUtils.isEmpty(result)) {
            result = getLocaleBasedCountryIso();
            Log.e(TAG, "getLocaleBasedCountryIso_result" + result);
        }
        if (TextUtils.isEmpty(result)) {
            result = "CN";
            Log.e(TAG, "DEFAULT_COUNTRY_ISO_result" + result);
        }
        Log.e(TAG, " result ==  " + result);
        return result.toUpperCase(Locale.getDefault());
    }
    
    /**
     * @return the country code of the current telephony network the user is connected to.
     */
    private static String getNetworkBasedCountryIso(TelephonyManager manager) {
        return manager.getNetworkCountryIso();
    }

    /**
     * @return the country code of the SIM card currently inserted in the device.
     */
    private static String getSimBasedCountryIso(TelephonyManager manager) {
        return manager.getSimCountryIso();
    }

    /**
     * @return the country code of the user's currently selected locale.
     */
    private static String getLocaleBasedCountryIso() {
        Locale defaultLocale = Locale.getDefault();
        return defaultLocale.getCountry();
    }
    
    private static boolean isNetworkCountryCodeAvailable(TelephonyManager manager) {
        // On CDMA TelephonyManager.getNetworkCountryIso() just returns the SIM's country code.
        // In this case, we want to ignore the value returned and fallback to location instead.
        return manager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM;
    }
}
