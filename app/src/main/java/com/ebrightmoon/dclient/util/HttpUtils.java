package com.ebrightmoon.dclient.util;

import android.content.Context;
import android.widget.ImageView;


import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wyy on 2018/1/29.
 */

public class HttpUtils {


    private HttpUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }






    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isContainChinese(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }



    /**
     * 拼接参数
     *
     * @param hashmap
     * @return
     * @throws UnsupportedEncodingException
     */
    public static StringBuffer spellPostParams(Map<String, String> hashmap) {
        List<BasicNameValuePair> array = new ArrayList();
        Object[] key = hashmap.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            if (false) {
                String encode = null;
                try {
                    encode = URLEncoder.encode(hashmap.get(key[i]), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                array.add(new BasicNameValuePair(key[i] + "", encode + ""));
            } else {
                array.add(new BasicNameValuePair(key[i] + "", hashmap.get(key[i]) + ""));
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.size(); i++) {
            if ((array.size() - 1) == i) {
                sb.append(array.get(i).toString());
            } else {
                sb.append(array.get(i).toString() + "&");
            }
        }
        return sb;
    }


}


