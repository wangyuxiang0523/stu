package com.fh.util.wxPayUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

public static final String yyyyMMddhhmmss="yyyyMMddHHmmss";

public static  String getYyyyMMddhhmmss(Date date,String pattern){
        if(date != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            String result = simpleDateFormat.format(date);
            return result;
        }
        return "";
}
}
