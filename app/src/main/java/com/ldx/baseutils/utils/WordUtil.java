package com.ldx.baseutils.utils;

import android.text.TextUtils;

/**
 * Created by babieta on 2018/12/14.
 */

public class WordUtil {
    public static String capitalize(String str) {
        return capitalize(str, (char[]) null);
    }

    public static String capitalize(String str, char... delimiters) {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (!TextUtils.isEmpty(str) && delimLen != 0) {
            char[] buffer = str.toCharArray();
            boolean capitalizeNext = true;

            for (int i = 0; i < buffer.length; ++i) {
                char ch = buffer[i];
                if (isDelimiter(ch, delimiters)) {
                    capitalizeNext = true;
                } else if (capitalizeNext) {
                    buffer[i] = Character.toTitleCase(ch);
                    capitalizeNext = false;
                }
            }

            return new String(buffer);
        } else {
            return str;
        }
    }

    private static boolean isDelimiter(char ch, char[] delimiters) {
        if (delimiters == null) {
            return Character.isWhitespace(ch);
        } else {
            char[] arr$ = delimiters;
            int len$ = delimiters.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                char delimiter = arr$[i$];
                if (ch == delimiter) {
                    return true;
                }
            }

            return false;
        }
    }
    /** firstname.addTextChangedListener(new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    if (StringUtils.isEmpty(firstname.getText().toString().trim())) {
    firstnameTitle.setTextColor(getResources().getColor(R.color.text_red));
    } else {
    firstnameTitle.setTextColor(getResources().getColor(R.color.text_normal));
    }
    try {
    String inputString = "" + firstname.getText().toString();
    String firstLetterCapString = WordUtil.capitalize(inputString);
    if (!firstLetterCapString.equals("" + firstname.getText().toString())) {
    firstname.setText("" + firstLetterCapString);
    firstname.setSelection(firstname.getText().length());
    }
    } catch (Exception e) {
    e.printStackTrace();
    }

    }

    @Override public void afterTextChanged(Editable s) {

    }
    });**/

}
