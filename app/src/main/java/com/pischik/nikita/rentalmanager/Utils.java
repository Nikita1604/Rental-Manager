package com.pischik.nikita.rentalmanager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains helper methods and fields.
 */
public class Utils {

    private static List<AddressesItem> listAddresses = null;
    private static String responseString;

    public static List<AddressesItem> getListAddresses() {
        return listAddresses;
    }

    public static void setListAddresses(List<AddressesItem> listAddresses) {
        Utils.listAddresses = listAddresses;
    }

    /**
     * method return true if string is not a zero-length
     */
    public static boolean CheckCorrectStrings(String text) {
        return text.length() != 0;
    }

    /**
     * method that return true if both dates are filled and the first date is less than a second
     */
    public static boolean CheckCorrectDate(String dateIn, String dateOut, Context context) {
        if (dateIn.equals(context.getResources().getString(R.string.date_in_hint)) ||
                dateOut.equals(context.getResources().getString(R.string.date_out_hint))) {
            return false;
        }
        int yearIn = Integer.parseInt(dateIn.substring(0,4));
        int monthIn = Integer.parseInt(dateIn.substring(5,7));
        int dayIn = Integer.parseInt(dateIn.substring(8));
        int yearOut = Integer.parseInt(dateOut.substring(0,4));
        int monthOut = Integer.parseInt(dateOut.substring(5,7));
        int dayOut = Integer.parseInt(dateOut.substring(8));

        if (yearIn < yearOut || (yearIn == yearOut &&
                (monthIn < monthOut || (monthIn == monthOut && dayIn <= dayOut)))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean CheckCorrectRent(String text) {
        try {
            Double.parseDouble(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getResponseString() {
        return responseString;
    }

    public static void setResponseString(String responseString) {
        Utils.responseString = responseString;
    }

}
