package com.jq.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ASUS on 17-Feb-18.
 */

public class Utility {

    private static String SOCCERSKILL_DATA = "FITLAB_DATA";

    public static SpannableStringBuilder customSubStringColor(int start, int end, String msg, ForegroundColorSpan foregroundColorSpan) {

        final SpannableStringBuilder sb = new SpannableStringBuilder(msg);

// Span to set text color to some RGB value

// Span to make_payment_edittext_bg text bold
        final StyleSpan bss = new StyleSpan(Typeface.NORMAL);

// Set the text color for first 4 characters
        sb.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

// make_payment_edittext_bg them also bold
        sb.setSpan(bss, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);


        return sb;
    }


    public static void setIntegerSharedPreference(Context context, String name, int value) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void setFloatSharedPreference(Context context, String name, float value) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(name, value);
        editor.commit();
    }


    public static int getIntegerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        return settings.getInt(name, 0);
    }

    public static float getFloatSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);

            return settings.getFloat(name, 0);
    }

    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        return settings.getString(name, "");
    }

    public static void setSharedPreference(Context context, String name, String value) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }


    public static void setSharedPreferenceBoolean(Context context, String name, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static boolean getSharedPreferencesBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(SOCCERSKILL_DATA, 0);
        return settings.getBoolean(name,false);
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static byte[] bitmapTOByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

}
