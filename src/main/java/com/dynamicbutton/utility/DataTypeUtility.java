package com.dynamicbutton.utility;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.util.UriUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTypeUtility {
    private static String KEY = "S!A4UMvNBE7A#MQ8";
    public static String FILEKEY = "PPXW04567JHJJCVB";
    protected static String[] englishdateUnits = {"", "First", "Second", "Third", "Fourth", "Fifth", "Sixth", "Seventh", "Eighth", "Ninth", "Tenth", "Eleventh", "Twelfth", "Thirteenth", "Fourteenth", "Fifteenth", "Sixteenth", "Seventeenth", "Eighteenth", "Nineteenth", "Twentieth"};
    protected static String[] englishdateTens = {"", "Tenth", "Twenty", "Thirty", "Fortieth", "Fiftieth", "Sixtieth" + "", "Seventy", "Eighty", "Ninety"};
    protected static String[] englishUnits = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    protected static String[] englishTens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty" + "", "Seventy", "Eighty", "Ninety"};
    public static String[] DAY = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public static String[] MONTHNAME = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static String[] SHORTMONTHNAME = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static String[] SHORTMONTHNAMEWITHACADEMICYEAR = {"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "Jan", "Feb", "Mar"};

    private static String toggle_1 = "<div class='common-checkbox-toggle b2 ms-3'><input type='checkbox' disabled class='checkbox-toggle-btn'";
    private static String toggle_2 = "'/><div class='knobs'><span></span></div><div class='layer'></div>/div> ";
    
    private static String bucketRegion = "ap-south-1";

//    public static String getLimit2(Integer limit, HttpServletRequest req) {
//        if (limit != null) {
//            int constantlimit = Constants.LIMIT;
//            int ilimit = limit;
//            int startlimit = 0;
//            int endlimit = 0;
//            if (ilimit > 1) {
//                endlimit = limit * constantlimit;
//                startlimit = endlimit - constantlimit;
//
//            } else {
//                startlimit = 0;
//                endlimit = constantlimit;
//            }
//            if (req != null) {
//                req.setAttribute("startindex1", startlimit + 1);
//                req.setAttribute("endindex1", endlimit);
//            }
//            String str = " limit " + startlimit + " , " + constantlimit;
//            return str;
//        }
//        return "";
//    }

    public static LinkedHashSet<Long> getLinkedHashSetFromCommaValue(String str_data) {
        str_data = DataTypeUtility.stringValue(str_data);
        String[] str_array = str_data.split(",");
        LinkedHashSet<Long> set = new LinkedHashSet<>();
        for (String str : str_array) {
            str = DataTypeUtility.stringValue(str.trim());
            str = str.replace("'", "");
            if (str.length() > 0 && DataTypeUtility.getForeignKeyValue(str) != null) set.add(longValue(str));
        }
        return set;
    }

    public static Set<Long> getSetFromCommaValue(String str_data) {
        str_data = DataTypeUtility.stringValue(str_data);
        String[] str_array = str_data.split(",");
        Set<Long> set = new HashSet<>();
        for (String str : str_array) {
            str = DataTypeUtility.stringValue(str.trim());
            if (str.length() > 0 && DataTypeUtility.getForeignKeyValue(str) != null) {
                if (longValue(str) != null) {
                    set.add(longValue(str));
                }
            }
        }

        return set;
    }

    public static ArrayList<Long> stringToListLong(String str, String separator) {
        ArrayList<Long> list = new ArrayList<>();
        if (str != null && str.length() > 0) {
            String[] str_array = str.split(separator);
            for (String str_id : str_array) {
                list.add(DataTypeUtility.getForeignKeyValue(DataTypeUtility.stringValue(str_id)));
            }
        }
        return list;
    }

    public static LinkedHashSet<Long> getOldValuesfromLinkedHashSet(String oldvalues) {
        String[] oldArray = null;
        LinkedHashSet<Long> old_set = new LinkedHashSet<>();
        if (oldvalues != null && oldvalues.length() > 0) {
            oldArray = oldvalues.split(",");
            for (int counter = 0; counter < oldArray.length; counter++) {
                Long old_value = DataTypeUtility.longValue((oldArray[counter]).trim());
                old_set.add(old_value);
            }
        }
        return old_set;
    }



    public static int getCurrentYear() {
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        return cal.get(Calendar.YEAR);
    }
    

    public static String getUSDateFormatFromDB(Object date1) {

        String date = DataTypeUtility.stringValue(date1);
        date = replaceTfromDateTime(date);

//        // System.out.println("date1:" + date1);
        if (date != null && date.length() > 5) {
            //date = date.replaceAll(" ", "");
            Date dateObject = null;


            try {
                dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(date);
            } catch (ParseException e) {
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e1) {
                    try {
                        dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                    } catch (ParseException e2) {

                    }
                }
            }

            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);
        }
        return null;

    }


    public static String getTimeWithoutSecond(String time1) {
        if (time1 != null && time1.length() > 0) {
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

            try {
                Date d1 = format1.parse(time1);
                return format2.format(d1);
            } catch (Exception e) {
                return time1;
            }
        }

        return "";
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

   

    public static String getIndianDateMonthYearFormatFromUSFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("dd MMM, yy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }
    
    public static String getCommaValueFromStringArray(String[] array) {
        String str = null;
        if (array != null) {
            for (int counter = 0; counter < array.length; counter++) {
                if (!DataTypeUtility.stringValue(array[counter]).equalsIgnoreCase("-1") && !DataTypeUtility.stringValue(array[counter]).equalsIgnoreCase("")) {
                    if (str == null) {
                        str = array[counter];
                    } else {
                        str = str + "," + array[counter];
                    }
                }
            }
        }

        if ((str != null && str.equalsIgnoreCase("null"))) {
            str = null;
        }

        if (str != null && str.isEmpty()) {
            str = null;
        }

        return str;
    }

    public static Date getDateObjectFromUsFormat(String date) {
        try {
            if (date != null && date.length() > 0) {
                if (date.contains("/")) {


                    return new SimpleDateFormat("yyyy/MM/dd").parse(date);
                } else {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(date);
                }
            }
            return null;
        } catch (Exception e) {
            //// System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public static String getLimitExam(Integer limit) {


        if (limit != null) {
            int startlimit = 0;
            String str = " limit " + startlimit + " , " + limit;
            return str;
        }

        return "";
    }


    public static String getOrderBy(String orderby) {
        orderby = stringValue(orderby);
        if (!orderby.toLowerCase().contains("order by") && orderby.length() > 0) {
            orderby = " order by " + orderby;
        }
        return orderby;
    }

    public static String getGroupBy(String groupby) {
        groupby = stringValue(groupby);
        if (groupby.length() > 0) {
            groupby = " group by " + groupby;
        }
        return groupby;
    }

    public static float floatValue(Object value) {

        if (value instanceof Float) {
            return ((Float) value).floatValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).intValue();
        } else if (value instanceof Long) {
            return ((Long) value).longValue();
        } else if (value instanceof String) {
            String val = DataTypeUtility.stringValue(value);
            if (val.length() > 0) {
                return Float.parseFloat(value.toString());
            } else {
                value = null;
            }
        }
        if (value instanceof Double) {
            return ((Double) value).floatValue();
        }

        return (value == null ? 0f : (Float) value);
    }

    public static Float floatObjectValue(Object value) {
        try {
            if (value instanceof Float) {
                return ((Float) value).floatValue();
            } else if (value instanceof Integer) {
                return (float) ((Integer) value).intValue();
            } else if (value instanceof Long) {
                return (float) ((Long) value).longValue();
            } else if (value instanceof String) {
                if (DataTypeUtility.stringValue(value).length() > 0) {
                    return (float) (double) Double.parseDouble(DataTypeUtility.stringValue(value));
                }
                return null;
            } else if (value instanceof Double) {
                return (float) ((Double) value).doubleValue();
            }

            return (value == null ? null : (Float) value);
        } catch (Exception e) {
            return 0f;
        }
    }

    public static Double doubleObjectValue(Object value) {

        if (value instanceof Double) {
            return ((Double) value).doubleValue();
        } else if (value instanceof Float) {
            return (double) ((Float) value).floatValue();
        } else if (value instanceof Integer) {
            return (double) ((Integer) value).intValue();
        } else if (value instanceof Long) {
            return (double) ((Long) value).longValue();
        } else if (value instanceof String) {
            if (value.toString().length() > 0) {
                return Double.parseDouble(value.toString());
            }
            return null;
        }

        return (value == null ? null : (Double) value);
    }
    public static String stringValue(Object value) {
        if (value == null || value.toString().equalsIgnoreCase("undefined") || value.toString().equalsIgnoreCase("null")) {
            return "";
        }
        return (value.toString()).trim();
    }

    public static Integer integerNullValue(Object value) {

        if (value instanceof String) {
            String svalue = DataTypeUtility.stringValue(value);
            if (svalue.length() == 0) {
                return null;
            }
            return Integer.parseInt(svalue);
        }
        if (value instanceof Float) {
            return (int) (float) (Float) value;
        }
        if (value instanceof Double) {
            return (int) (double) (Double) value;
        }
        if (value instanceof Long) {
            return (int) (long) (Long) value;
        }
        return (value == null ? null : (Integer) value);
    }

    public static Long getForeignKeyValue(Object value) {


        if (value == null) {
            return null;
        }


        Long val = DataTypeUtility.longValue(value);


        if (val == null || val <= 0l) {
            val = null;
        }
        return val;
    }

    public static Long getForeignKeyValue(Long value) {
        if (value == null) {
            return null;
        }
        if (value <= 0l) {
            value = null;
        }
        return value;
    }

    public static Long getForeignKeyZeroValue(Long value) {
        if (value == null || value <= 0l) {
            value = 0l;
        }
        return value;
    }

    public static Long longValue(Object value) {
        if (value instanceof String) {
            value = stringValue(value);
            String svalue = DataTypeUtility.stringValue(value);
            if (svalue.equalsIgnoreCase("all") || svalue.equalsIgnoreCase("null") || svalue.length() == 0) {
                return null;
            }
            if (svalue.contains("null")) {
                return null;
            }

            return Long.parseLong(svalue);
//            Long val = (long) (float) fvalue;
//            return val;
        }
        if (value instanceof Double) {
            Double svalue = (Double) value;
            Long val = (long) (float) (double) svalue;
            return val;
        }
        if (value instanceof Float) {
            Float svalue = (Float) value;
            Long val = (long) (float) svalue;
            return val;
        }
        if (value instanceof BigInteger) {
            BigInteger svalue = (BigInteger) value;
            Long val = svalue.longValue();
            return val;
        }
        if (value instanceof Integer) {
            return (long) (int) (Integer) value;
        }
        return (value == null ? null : (Long) value);
    }
    public static String stringNullValue(Object value) {
        if (value == null || value.toString().length() == 0 || value.toString().equalsIgnoreCase("null")) {
            return null;
        }
        return (value.toString()).trim();
    }
    public static int integerValue(Object value) {
//        //// System.out.println("inside integerValue " + value instanceof String);
        if (value instanceof String) {
            String svalue = DataTypeUtility.stringValue(value);
            if (svalue.length() == 0) {
                return 0;
            } else if (svalue.equalsIgnoreCase("null")) {
                return 0;
            }
            return Integer.parseInt(svalue);
        }

        if (value instanceof Double) {
            return (int) (double) (Double) value;
        }
        if (value instanceof Float) {
            return (int) (float) (Float) value;
        }
        if (value instanceof Long) {
            return (int) (long) (Long) value;
        }
        return (value == null ? 0 : (Integer) value);
    }

    public static boolean booleanValue(Object value) {
        if (value == null) {
            return false;
        }
        boolean isboolean = false;
        if (value instanceof Integer) {
            int iday = ((Integer) value).intValue();
            if (iday == 1) {
                isboolean = true;
            }
        } else if (value instanceof Long) {
            long iday = ((Long) value).longValue();
            if (iday == 1l) {
                isboolean = true;
            }
        } else if (value instanceof Short) {
            int iday = ((Short) value).shortValue();
            if (iday == 1) {
                isboolean = true;
            }
        } else if (value instanceof Boolean) {
            isboolean = (Boolean) value;
        } else if (value instanceof String) {
            String str = (String) value;
            if (str.equalsIgnoreCase("on") || str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes")) {
                return true;
            } else if (str.equalsIgnoreCase("1")) {
                return true;
            }
        }

        return isboolean;

    }

    public static boolean requireNonNullCollection(Collection collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }

    public static Map<Object, String> getOrderByMap(LinkedHashMap<Object, String> sortedMap, Object id, String title) {
        sortedMap.put(id, title);
        return sortedMap;
    }
    


    public static SecretKeySpec getKey() {
        return new SecretKeySpec(KEY.getBytes(), "AES");
    }


    public static String getUSDateFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getUSDateFormat(String date) {
        try {
            date = DataTypeUtility.stringValue(date);
            if (date != null && date.length() > 5) {
                date = date.replaceAll(" ", "");
                Date dateObject = null;

                if (date.contains("/")) {
                    dateObject = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } else {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                }

                return new SimpleDateFormat("yyyy-MM-dd").format(dateObject);
            }
            return null;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("" + e.getMessage());
        }
    }

    public static Double doubleZeroValue(Object value) {
        if (value != null && !value.toString().equalsIgnoreCase("undefined")) {
            if (value instanceof Float) {
                Float svalue = (Float) value;
                Double val = Double.valueOf(svalue.toString());
                return val;
            }
            if (value instanceof Long) {
                Long svalue = (Long) value;
                Double val = (double) (long) svalue;
                return val;
            }

            if (value.toString().length() != 0) {
                return Double.valueOf(value.toString());
            }
        }
        return 0d;
    }
    public static void setColumnArray(String column_id, String column_label, LinkedList<Map<String, Object>> column_array, Integer width, boolean is_srno, String style, String classes, String type, boolean is_checkbox, String th_classes,boolean isColumDefaultHidden) throws Exception {
        Map<String, Object> column_json = new HashMap<>();
        column_json.put("is_srno", DataTypeUtility.booleantointvalue(is_srno));
        column_json.put("is_checkbox", DataTypeUtility.booleantointvalue(is_checkbox));
        column_json.put("column_id", column_id);
        column_json.put("column_label", column_label);
        column_json.put("width", width);
        column_json.put("text_style", style);
        column_json.put("classes", classes);
        column_json.put("th_classes", th_classes);
        column_json.put("type", type);
        column_json.put("isColumDefaultHidden", isColumDefaultHidden);
        if (is_checkbox) {
            LinkedList<Map<String, Object>> column_array_new = new LinkedList<>();
            column_array_new.add(column_json);
            column_array_new.addAll(column_array);
            column_array.clear();
            column_array.addAll(column_array_new);
        } else {
            column_array.add(column_json);
        }
    }

    public static int booleantointvalue(Boolean value) {
        if (value != null && value) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Float floatZeroValue(Object value) {

        if (value != null) {
            if (DataTypeUtility.stringValue(value).length() != 0) {
                return Float.valueOf(value.toString());
            }
        }
        return 0f;
    }

    public static Long longZeroValue(Object value) {
        if (value instanceof String) {
            value = stringValue(value);
            String svalue = (String) value;
            if (svalue.equalsIgnoreCase("null") || svalue.length() == 0) {
                return 0l;
            }
            return Long.parseLong(svalue);
        }
        if (value instanceof Double) {
            Double svalue = (Double) value;
            Long val = (long) (float) (double) svalue;
            return val;
        }
        if (value instanceof BigInteger) {
            BigInteger svalue = (BigInteger) value;
            Long val = svalue.longValue();
            return val;
        }
        if (value instanceof BigDecimal) {
            BigDecimal svalue = (BigDecimal) value;
            Long val = svalue.longValue();
            return val;
        }
        if (value instanceof Float) {
            Float svalue = (Float) value;
            Long val = (long) (float) svalue;
            return val;
        }
        if (value instanceof Integer) {
            return (long) (int) (Integer) value;
        }
        return (value == null ? 0l : (Long) value);
    }

    public static String getDateTimeInIndianFormat(String date, HttpServletRequest request) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String replaceTfromDateTime(String date) {
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            date = date.replaceAll("T", " ");
            return date;
        } else {
            return null;
        }
    }
    

    public static Date getCurrentDateTime() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        date = cal.getTime();
        return date;
    }
    
    public static String getDateTimeObjectInIndianFormat(Date date) {

        if (date != null) {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);
        }
        return "";
    }
    
    public static boolean equal_value_list(Object val1, Object val2, String type, boolean ismultiple, ArrayList selected_valuelist) {
        if (ismultiple) {
            if (selected_valuelist != null && selected_valuelist.contains(DataTypeUtility.longValue(val2))) {
                return true;
            }
        } else if (type.equalsIgnoreCase("long")) {
            if (val1 != null && val2 != null && DataTypeUtility.longValue(val1).equals(DataTypeUtility.longValue(val2))) {
                return true;
            }
        }
        return false;
    }

    public static boolean equal_value_list_string(Object val1, Object val2, String type, boolean ismultiple, ArrayList selected_valuelist) {
        if (ismultiple) {
            if (selected_valuelist != null && selected_valuelist.contains(DataTypeUtility.stringValue(val2))) {
                return true;
            }
        } else if (type.equalsIgnoreCase("StringValueDropDown")) {
            if (val1 != null && val2 != null && DataTypeUtility.stringValue(val1).equals(DataTypeUtility.stringValue(val2))) {
                return true;
            }
        }
        return false;
    }

    public static String getFilter(String filter) {
        filter = stringValue(filter);
        if (filter.length() > 0) {
            filter = filter.replace("where", "").trim();
            filter = " where " + filter;
        }
        return filter;
    }

    public static String getCommaValueFilter(String itemstr, Long id) {
        if (id != null) {
            if (itemstr.length() == 0) {
                itemstr = "" + id;
            } else {
                itemstr = itemstr + "," + id;
            }
        }
        return itemstr;
    }

    public static String getCommaValueFilter(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr.length() == 0) {
                itemstr = "'" + str + "'";
            } else {
                itemstr = itemstr + ", '" + str + "'";
            }
        }
        return itemstr;
    }

    public static String getCommaValuesFilter(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr.length() == 0) {
                itemstr = "'" + str + "'";
            } else {
                itemstr = itemstr + "," + str + "";
            }
        }
        return itemstr;
    }

    public static String getCommasValueFilter(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr.length() == 0) {
                itemstr = "" + str + "";
            } else {
                itemstr = itemstr + "," + str + "";
            }
        }
        return itemstr;
    }

    public static String getCommaValue(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr == null || itemstr.length() == 0) {
                itemstr = str;
            } else {
                itemstr = itemstr + ", " + str;
            }

        }
        return itemstr;
    }

    public static String getCommaValuewithoutspace(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr == null || itemstr.length() == 0) {
                itemstr = str;
            } else {
                itemstr = itemstr + "," + str;
            }

        }
        return itemstr;
    }

    public static String getNewLineValue(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr == null || itemstr.length() == 0) {
                itemstr = str;
            } else {
                itemstr = itemstr + "\n" + str;
            }

        }
        return itemstr;
    }

    public static String getCommaValueFromSet(HashSet<Long> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (Long str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + "," + str;
                }
            }

        }
        return itemstr;
    }

    public static String getCommaValueStringFromSet(Set<String> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (String str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "'" + str + "'";
                } else {
                    itemstr = itemstr + ", '" + str + "'";
                }
            }

        }
        return itemstr;
    }

    public static String getCommaValueFromStringSet(HashSet<String> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (String str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + ", " + str;
                }
            }

        }
        return itemstr;
    }

    public static String getCommaValueFromStrSet(HashSet<String> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (String str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + ", " + str;
                }
            }

        }
        return itemstr;
    }

    public static String getCommaStringValueFromSet(Set<String> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (String str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + ", " + str;
                }
            }

        }
        return itemstr;
    }

    public static String getIndianDateFormat(String date) {
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("dd-MM-yyyy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("error" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String getCustomDateFormat(String date, String sourceFormat, String targetFormat) {
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat(sourceFormat).parse(date);
                return new SimpleDateFormat(targetFormat).format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String getIndianDateMonthFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("dd MMM").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String booleantoString(Boolean value) {
        if (value != null && value) {
            return "Yes";
        }
        return "No";
    }

    public static String booleantocheckboxvalue(Boolean value) {
        if (value != null && value) {
            return "checked";
        }
        return "";
    }

    public static String getCurrentDateTimeInUSFormat() {
        Date date = DataTypeUtility.getServerCurrentDateTime();

        return DataTypeUtility.getDateTimeObjectInUSFormat(date);
    }

    public static Date getServerCurrentDateTime() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        date = cal.getTime();
//        //// System.out.println("***************************** Date - " + date);
        return date;
    }

    public static String getCurrentDateTimeInUSFormatExact() {
        Date date = new Date();

        return DataTypeUtility.getDateTimeObjectInUSFormat(date);
    }

    public static Date getAWSCurrentDateTime() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        date = cal.getTime();
//        //// System.out.println("***************************** Date - " + date);
        return date;
    }

    public static String getAWSCurrentDateTimeInUSFormat() {
        Date date = DataTypeUtility.getAWSCurrentDateTime();
        return DataTypeUtility.getDateTimeObjectInUSFormat(date);
    }

    public static String getActualCurrentDateTimeInUSFormat() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        date = cal.getTime();
        return DataTypeUtility.getDateTimeObjectInUSFormat(date);
    }

    public static String getDateTimeObjectInUSFormat(Date date) {
        if (date != null) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }
        return "";
    }
    
    public static boolean requireNonNullString(String string) {
        if (string != null && string.length() > 0) {
//            //// System.out.println("string>>>>" + string);
            return true;
        }
        return false;
    }

    public static String getIndianDateMonthYearDetailFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                return datetoword(dateObject, false);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String datetoword(Date date) {
        return datetoword(date, false);
    }

    public static String datetoword(Date date, boolean is_hundred) {

        if (date != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            int idate = cal.get(Calendar.DATE);
            int year = cal.get(Calendar.YEAR);
            String month = new SimpleDateFormat("MMMMM").format(date);
            String year_str = "";
            if (is_hundred) {
                year_str = toyearSimpleWords_2(year);
            } else {
                year_str = toyearSimpleWords(year);
            }

            String str = todateSimpleWords(idate) + " " + month + " " + year_str;

            return str;
        } else {
            return "";
        }

    }

    public static String todateSimpleWords(int number) {

        if (number >= 2000) {
            return todateSimpleWords(number / 1000) + " Thousand " + todateSimpleWords(number % 1000);
        } else if (number >= 100) {
            int rem = number % 100;
            return todateSimpleWords(number / 100) + " " + todateSimpleWords(rem);
        } else {
            if (number <= 20)
                return englishdateUnits[number];
            else if (number == 30)
                return "Thirtieth";
            int rem = number % 10;
            return englishdateTens[number / 10] + (rem == 0 ? "" : " " + englishdateUnits[rem]);
        }
    }

    public static String toyearSimpleWords(int number) {

        if (number >= 2000) {
            return toyearSimpleWords(number / 1000) + " Thousand " + toyearSimpleWords(number % 1000);
        } else if (number >= 100) {
            int rem = number % 100;
            return toyearSimpleWords(number / 100) + " " + toyearSimpleWords(rem);
        } else {
            if (number < 20)
                return englishUnits[number];
            int rem = number % 10;
            return englishTens[number / 10] + (rem == 0 ? "" : " " + englishUnits[rem]);
        }
    }

    public static String toyearSimpleWords_2(int number) {

        if (number >= 2000 && number <= 2099) {
            return toyearSimpleWords_2(number / 1000) + " Thousand " + toyearSimpleWords_2(number % 1000);
        } else if (number >= 100) {
            int rem = number % 100;
            return toyearSimpleWords_2(number / 100) + " Hundred " + toyearSimpleWords_2(rem);
        } else {
            if (number < 20)
                return englishUnits[number];
            int rem = number % 10;
            return englishTens[number / 10] + (rem == 0 ? "" : " " + englishUnits[rem]);
        }
    }

    public static boolean equal_value(Object val1, Object val2, String type) {
        if (type.equalsIgnoreCase("long")) {
            if (val1 != null && val2 != null && DataTypeUtility.longValue(val1).equals(DataTypeUtility.longValue(val2))) {
                return true;
            }
        }
        if (type.equalsIgnoreCase("String")) {
            if (val1 != null && val2 != null && DataTypeUtility.stringValue(val1).equals(DataTypeUtility.stringValue(val2))) {
                return true;
            }
        }

        return false;
    }
    
    public static String year_month_day_str(String max, String min) {

        return year_month_day_str(DataTypeUtility.getDateObjectFromIndianFormat(max), DataTypeUtility.getDateObjectFromIndianFormat(min));

    }

    public static String year_month_day_str(Date max, Date min) {

        if (max == null || min == null) {
            return "";
        }


        Calendar startDate = Calendar.getInstance();
        startDate.setTime(max);
        long years = 0;
        long months = 0;
        long days = 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(min);
        int month = startDate.get(Calendar.MONTH) + 1;
        int year = startDate.get(Calendar.YEAR);
        int date = startDate.get(Calendar.DAY_OF_MONTH);

        LocalDate date1 = LocalDate.of(year, month, date);

        month = endDate.get(Calendar.MONTH) + 1;
        year = endDate.get(Calendar.YEAR);
        date = endDate.get(Calendar.DAY_OF_MONTH);


        LocalDate date2 = LocalDate.of(year, month, date);
        Period p = Period.between(date1, date2);

        String str = p.getYears() + " Years " + p.getMonths() + " Months and " + p.getDays() + " Days";
        return str;

//        if (max == null || min == null) {
//            return "";
//        }
//
//        int[] monthDay = {31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
//        Calendar fromDate;
//        Calendar toDate;
//        int increment = 0;
//        int year;
//        int month;
//        int day;
//
//
//        Calendar max_calender = new GregorianCalendar();
//        max_calender.setTime(max);
//
//        Calendar min_calender = new GregorianCalendar();
//        min_calender.setTime(min);
//
//        if (max_calender.getTime().getTime() > min_calender.getTime().getTime()) {
//            fromDate = min_calender;
//            toDate = max_calender;
//        } else {
//            fromDate = max_calender;
//            toDate = min_calender;
//        }
//
//        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate
//                .get(Calendar.DAY_OF_MONTH)) {
//            increment = monthDay[fromDate.get(Calendar.MONTH)];
//        }
//
//        GregorianCalendar cal = new GregorianCalendar();
//        boolean isLeapYear = cal.isLeapYear(fromDate.get(Calendar.YEAR));
//
//        if (increment == -1) {
//            if (isLeapYear) {
//                increment = 29;
//            } else {
//                increment = 28;
//            }
//        }
//
//        // DAY CALCULATION
//        if (increment != 0) {
//            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment)
//                    - fromDate.get(Calendar.DAY_OF_MONTH);
//            increment = 1;
//        } else {
//            day = toDate.get(Calendar.DAY_OF_MONTH)
//                    - fromDate.get(Calendar.DAY_OF_MONTH);
//        }
//
//        // MONTH CALCULATION
//        if ((fromDate.get(Calendar.MONTH) + increment) > toDate
//                .get(Calendar.MONTH)) {
//            month = (toDate.get(Calendar.MONTH) + 12)
//                    - (fromDate.get(Calendar.MONTH) + increment);
//            increment = 1;
//        } else {
//            month = (toDate.get(Calendar.MONTH))
//                    - (fromDate.get(Calendar.MONTH) + increment);
//            increment = 0;
//        }
//
//        // YEAR CALCULATION
//        year = toDate.get(Calendar.YEAR)
//                - (fromDate.get(Calendar.YEAR) + increment);
//
//        String str = year + " Years " + month + " Months and " + day + " Days";
//        return str;
    }

    public static boolean requireNonNullStringForAdmission(String string) {
        if (string != null && string.length() > 0 && string != "null") {
//            //// System.out.println("string>>>>" + string);
            return true;
        }
        return false;
    }
    
    public static String decrypt(Object encryptedValue) throws Exception {
        if (encryptedValue == null) {
            return "";
        } else {
            return decrypt(encryptedValue.toString());
        }
    }
    
  
    
    public static String getDataModelWithTiitle(List<Map<String, Object>> model_array_map, Map<String, Object> param_map, HttpServletRequest request) {

        String modal_data = "";
        int count = 1;
        for (Iterator iter = model_array_map.iterator(); iter.hasNext(); ) {
            Map<String, Object> filter_json = (Map<String, Object>) iter.next();
            String filter_title = DataTypeUtility.stringValue(filter_json.get("title"));
            String title = filter_title;
            String onchange_event = DataTypeUtility.stringValue(filter_json.get("onchange_event"));
            String filter_name = DataTypeUtility.stringValue(filter_json.get("name"));
            String type = DataTypeUtility.stringValue(filter_json.get("type"));
            String style = DataTypeUtility.stringValue(filter_json.get("style"));
            String divcount = DataTypeUtility.stringValue(filter_json.get("divcount"));
            String filter_type = DataTypeUtility.stringValue(filter_json.get("filtertype"));
            Boolean isHidden = DataTypeUtility.booleanValue(filter_json.get("hidden"));
            String hidden_value = DataTypeUtility.stringValue(filter_json.get("hidden_value"));
            String classes = DataTypeUtility.stringValue(filter_json.get("classes"));
            String fileExts = DataTypeUtility.stringValue(filter_json.get("fileExts"));
//            if (filter_type.equalsIgnoreCase(Constants.SIMPLE_FILTER)) {
//                continue;
//            }
            if(!StringUtils.isBlank(fileExts)){
                fileExts = "accept='"+fileExts+"'";
            }
            String function = DataTypeUtility.stringValue(filter_json.get("function"));
            if (onchange_event == null || onchange_event.length() == 0) {
                onchange_event = function;
            } else {
                onchange_event = onchange_event + ";" + function;
            }
            String class_str = "";
            if (onchange_event != null || onchange_event.length() > 0) {
                class_str = "";
            }
            if (type.length() == 0) {
                continue;
            }
            boolean only_data = DataTypeUtility.booleanValue(filter_json.get("only_data"));
            boolean multiple = DataTypeUtility.booleanValue(filter_json.get("multiple"));
//                    // System.out.println("multiple>>> " + multiple);
            String multiple_select = "";
            String multiple_select_placeholder = "";
            if (multiple) {
                multiple_select = "multiple";
                multiple_select_placeholder = "data-placeholder='Select "+title+"'";
            }
            if (classes.contains("mandatoryvalue")) {
                filter_title = filter_title + "<span class=\"text-danger\">*</span>";
            }

            String selectClass = "customFloatingSelect " + classes;
            if (multiple) {
                selectClass = "customFlaotingMultiselect " + classes;
            }

            String margin_bottom = "";
            if (count < model_array_map.size()) {
                margin_bottom = "mb-3";
            }
            String formgroupdivclass = "align-items-center mb-0";
            if (type.equalsIgnoreCase("checkbox")) {
                formgroupdivclass = formgroupdivclass + " d-flex";
            }
            if (type.equalsIgnoreCase("long")) {
                formgroupdivclass = formgroupdivclass + " select2Part";
            }

            if (style.contains("display:none") && !isHidden) {
                modal_data = modal_data + "<div style='" + style + "' class='col-md-" + divcount + " " + margin_bottom + "'><div class='" + formgroupdivclass + " form-group floating-group'>";
                style = style.replaceAll("display:none", "");
            } else if (!isHidden) {
                modal_data = modal_data + "<div class='col-md-" + divcount + " " + margin_bottom + "'><div class='" + formgroupdivclass + " form-group floating-group'>";
            }
            if (type.equalsIgnoreCase("long")) {
                //JSONArray reference_result_array = filter_json.optJSONArray("result_array");
                LinkedList<Map<String, String>> reference_result_array = (LinkedList<Map<String, String>>) filter_json.get("filter_data");

                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<select " + multiple_select_placeholder + " " + multiple_select + " title='" + title + "' onchange='" + onchange_event + "' name='" + filter_name + "' id='" + filter_name + "' class='" + selectClass + " floating-control modaldata' style='width:100%;" + style + "'>";

                if (!only_data) {
                    modal_data = modal_data + "<option value=-1> " + title + "</option>";
                }

                if (reference_result_array != null) {
                    ArrayList<Long> select_valuelist = new ArrayList<Long>();
                    if (multiple) {
                        String selected_value = DataTypeUtility.stringValue(request.getParameter(filter_name));
//                                    // System.out.println("filter_name>>> " + filter_name);
//                                    // System.out.println("selected_value>>> " + selected_value);
                        for (String pram : selected_value.split(",")) {
                            select_valuelist.add(DataTypeUtility.longValue(pram));
                        }
                    }

//                                   for (int filter_value_count = 0; reference_result_array.length() > filter_value_count; filter_value_count++) {
//                                        JSONObject ref_data_json = reference_result_array.optJSONObject(filter_value_count);
                    for (Iterator iter2 = reference_result_array.iterator(); iter2.hasNext(); ) {
                        Map<String, Object> ref_data_json = (Map<String, Object>) iter2.next();
                        String key = DataTypeUtility.stringValue(ref_data_json.get("__key__"));
                        String keyname = DataTypeUtility.stringValue(ref_data_json.get("__keyname__"));
                        if (DataTypeUtility.equal_value_list(request.getAttribute(filter_name), key, "long", multiple, select_valuelist)) {
                            modal_data = modal_data + "<option selected='selected' value='" + key + "'>" + keyname + "</option>";
                        } else {
                            modal_data = modal_data + "<option value='" + key + "'>" + keyname + "</option>";

                        }
                    }
                }

                modal_data = modal_data + " </select>";
            } else if (!isHidden && type.equalsIgnoreCase("textbox") || type.equalsIgnoreCase("fts")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input type='text' title='" + title + "' autocomplete='off' class='form-control table_filter floating-control modaldata " + classes + "' " + "value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" + " id='" + filter_name + "' style='width: 100%; " + style + "'/>";

            } else if (!isHidden && type.equalsIgnoreCase("number")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input type='number' title='" + title + "' autocomplete='off' class='form-control table_filter floating-control modaldata " + classes + "' " + "value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" + " id='" + filter_name + "' style='width: 100%; " + style + "'/>";

            } else if (!isHidden && type.equalsIgnoreCase("date") || type.equalsIgnoreCase("fromdate") || type.equalsIgnoreCase("todate")) {

                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input type='text' title='" + title + "' autocomplete='off' class='form-control pick-date floating-control modaldata " + classes + "' " + "value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" + " id='" + filter_name + "' style='width: 100%;" + style + "'/><img src='../../../../assets/images/calendar-icon.svg' class='pull-right up-mar25 me-2'>";

            } else if (!isHidden && type.equalsIgnoreCase("datetime")) {

                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input type='text' title='" + title + "' autocomplete='off' class='form-control pick-date-time floating-control modaldata " + classes + "' " + "value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" + " id='" + filter_name + "' style='width: 100%;" + style + "'/><img src='../../../../assets/images/calendar-icon.svg' class='pull-right up-mar25 me-2'>";

            } else if (!isHidden && type.equalsIgnoreCase("textarea")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<textarea title='" + title + "' class='form-control floating-control modaldata " + classes + "' id='" + filter_name + "' rows='3' style='width: 100%;" + style + "' ></textarea>";

            } else if (!isHidden && type.equalsIgnoreCase("checkbox")) {
//                modal_data = modal_data + "<p class='mb-0'>" + filter_title + "</p><div class='common-checkbox-toggle b2 ms-3'><input type='checkbox' class='checkbox-toggle-btn modaldata' id='" + filter_name + "'/>" +
//                        " <div class='knobs'><span></span></div><div class='layer'></div></div>";
                if (!StringUtils.isEmpty(onchange_event)) {
                    modal_data = modal_data + "<label class='mb-0'>" + filter_title + "</label><div class='common-checkbox-toggle b2 ms-3'><input type='checkbox' class='checkbox-toggle-btn modaldata " +classes+"' title='" + title + "' onchange='" + onchange_event + "' id='" + filter_name + "'/>" + " <div class='knobs'><span></span></div><div class='layer'></div></div>";
                } else {
                    modal_data = modal_data + "<label class='mb-0'>" + filter_title + "</label><div class='common-checkbox-toggle b2 ms-3'><input type='checkbox' title='" + title + "' class='checkbox-toggle-btn modaldata " +classes+"'  id='" + filter_name + "'/>" + " <div class='knobs'><span></span></div><div class='layer'></div></div>";
                }
            } else if (!isHidden && type.equalsIgnoreCase("editor")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<div class='custom-ck-editor floating-control modaldata' id='" + filter_name + "'></div>";
            } else if (!isHidden && type.equalsIgnoreCase("issearchbox")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input title='" + title + "' class='form-control floating-control modaldata' autocomplete='off' role='combobox' list='' id='" + filter_name + "' name='browsers' onkeyup='" + onchange_event + "'>";
                modal_data = modal_data + "<datalist id='browsers' role='listbox'></datalist>";
            } else if (!isHidden && type.equalsIgnoreCase("time")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input type='text' title='" + title + "' autocomplete='off' class='form-control table_filter floating-control pick-time modaldata " + classes + "' id='" + filter_name + "' name='" + filter_name + "' value=''/>";

            } else if (type.equalsIgnoreCase("attachment_file")) {
                modal_data = modal_data + "<div class='col-md-6 documents-upload " + divcount + "'  id='attachment_div'><input type='file' name='" + filter_name + "' id='" + filter_name + "' class='inputfile inputfile-1 modaldata " + divcount + "' data-multiple-caption='{count} files selected'  "+fileExts+" onchange='fileCheck(this)'/>";
                modal_data = modal_data + "<label for='attachment'>";
                modal_data = modal_data + "<img src='/student_new_assests/images/tc_request/attachment-icon.svg' alt='attachment-icon' />";
                modal_data = modal_data + "<span id='span_atchmnt'>Attachment</span>";
                modal_data = modal_data + "<span class='text-danger' style='display: none' id='attachment_impt'>*</span></label>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "<div class='col-md-6 documents-upload " + divcount + "' id='old_attachment_span' style='float:left; padding-left:10px;padding-right:10px;display: none'>";
                modal_data = modal_data + "<input name='old_" + filter_name + "' value='{}' id='old_" + filter_name + "'  type='hidden'>";
                modal_data = modal_data + "<a href='' id='fileanchor'></a>";
                modal_data = modal_data + "<label for='fileanchor' class='mb-1 mt-1'>";
                modal_data = modal_data + "<img src='/student_new_assests/images/tc_request/attachment-icon.svg' alt='attachment-icon' />";
                modal_data = modal_data + "</label>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "<div class='row px-4'>";
                modal_data = modal_data + "<div class='col-md-12 mx-1 " + divcount + "' id='attachment_existfile' style='display: none;'>";
                modal_data = modal_data + "<a target='_blank' rel='noopener noreferrer nofollow' id='attachment_existfile_src' href='' class='sk_attachment_button " + divcount + "'></a>";
                modal_data = modal_data + "<a href='#' onclick='changefilewithids(\"" + filter_name + "\")'>Remove File</a>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "</div>";

            }  else if (type.equalsIgnoreCase("attachment_file_new")) {
                modal_data = modal_data + "<div  class='col-sm-"+divcount+ " mb-3'>";
                modal_data = modal_data + "<label class='mb-1'>"+filter_title+"</label>";
                modal_data = modal_data + "<div class='file-upload'><div class='file-select position-relative'>";
                modal_data = modal_data + "<div class='file-select-button'>Choose File</div>";
                modal_data = modal_data + "<div id='"+filter_name+"_name' class='file-select-name flex-col-wide'></div>";
                modal_data = modal_data + "<input filesize='10240' type='file' id='"+filter_name+"' name='"+filter_title+"' class='choose-file-control "+classes+"' "+fileExts+">";
                modal_data = modal_data + "<div class='file-remove position-relative top-50 end-0 translate-middle-y z-index-top me-2 curser'>";
                modal_data = modal_data + "<a id='"+filter_name+"_a_tag' href='' class='d-none'>";
                modal_data = modal_data + "<i class='fa fa-download fs-12 text-black-50 position-relative top-2 p-10'>";
                modal_data = modal_data + "</i></a>";
                modal_data = modal_data + "<i id='"+filter_name+"_close_but' class='d-none fa fa-close fs-12 text-black-50 position-relative top-2'></i>";
                modal_data = modal_data + "</div></div></div></div>";

            } else if (!isHidden && type.equalsIgnoreCase("color")) {
                modal_data = modal_data + "<label class='mb-0'>" + filter_title + "</label><div class='align-items-center d-flex'><input type='color' title='" + title + "' id='" + filter_name + "' value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "' class='clr-picker-control form-control ms-2 h-30' colorpick-eyedropper-active='true'>" + "</div>";
            }else if (!isHidden && type.equalsIgnoreCase("studenttokenizer")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                //modal_data = modal_data + "<input type='text' title='" + title + "' autocomplete='off' class='form-control table_filter floating-control modaldata " + classes + "' onkeyup='" + onchange_event + "' value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" + " id='" + filter_name + "' style='width: 100%; " + style + "'/>";
                modal_data = modal_data + "<input title='" + title + "' class='form-control floating-control modaldata' autocomplete='off' role='combobox' list='' id='" + filter_name + "' name='browsers' onkeyup='" + onchange_event + "'>";
                modal_data = modal_data + "<datalist id='browsers' role='listbox'></datalist>";
                modal_data = modal_data + "<div class='col-md-12 mb-3'>" +
                        "                            <div class='border bg-light-blue  rounded6' id='studentdata'>" +
                        "                            </div>" +
                        " <div class='align-items-center d-flex h-p100 justify-content-center flex-column py-3 d-none' id='studentnotfounddivid'>" +
                        "                                    <img src='/assets/images/stu-empty-ui.svg' class='mb-3'>" +
                        "                                    <h5 class='text-dark fs-14 fw-500 mb-1'>No Data Available!</h5>" +
                        "                                    <p class='fs-12 mb-0'>Search Student Name either Admission no in search box...</p>" +
                        "                                </div>" +
                        "                        </div>";

            }
            if (isHidden) {
                modal_data = modal_data + "<input type='hidden' value='" + hidden_value + "' id='" + filter_name + "' onchange='" + onchange_event + "' name='" + filter_name + "' class='"+classes+"'/>";
            }
            if (!isHidden) {
                modal_data = modal_data + "</div></div>";
            }

            count++;

        }

        return modal_data;

    }

    public static String getDataModel
            (List<Map<String, Object>> model_array_map, Map<String, Object> param_map, HttpServletRequest request) {

        String modal_data = "";
        int count = 1;
        for (Iterator iter = model_array_map.iterator(); iter.hasNext(); ) {
            Map<String, Object> filter_json = (Map<String, Object>) iter.next();
            String filter_title = DataTypeUtility.stringValue(filter_json.get("title"));
            String label_title = DataTypeUtility.stringValue(filter_json.get("title"));
            String onchange_event = DataTypeUtility.stringValue(filter_json.get("onchange_event"));
            String filter_name = DataTypeUtility.stringValue(filter_json.get("name"));
            String type = DataTypeUtility.stringValue(filter_json.get("type"));
            String style = DataTypeUtility.stringValue(filter_json.get("style"));
            String divcount = DataTypeUtility.stringValue(filter_json.get("divcount"));
            String filter_type = DataTypeUtility.stringValue(filter_json.get("filtertype"));
            Boolean isHidden = DataTypeUtility.booleanValue(filter_json.get("hidden"));
            String hidden_value = DataTypeUtility.stringValue(filter_json.get("hidden_value"));
            String classes = DataTypeUtility.stringValue(filter_json.get("classes"));
//            if (filter_type.equalsIgnoreCase(Constants.SIMPLE_FILTER)) {
//                continue;
//            }
            String function = DataTypeUtility.stringValue(filter_json.get("function"));
            if (onchange_event == null || onchange_event.length() == 0) {
                onchange_event = function;
            } else {
                onchange_event = onchange_event + ";" + function;
            }
            String class_str = "";
            if (onchange_event != null || onchange_event.length() > 0) {
                class_str = "";
            }
            if (type.length() == 0) {
                continue;
            }
            boolean only_data = DataTypeUtility.booleanValue(filter_json.get("only_data"));
            boolean multiple = DataTypeUtility.booleanValue(filter_json.get("multiple"));
//                    // System.out.println("multiple>>> " + multiple);
            String multiple_select = "";
            if (multiple) {
                multiple_select = "multiple";
            }
            if (classes.contains("mandatoryvalue")) {
                filter_title = filter_title + "<span class=\"text-danger\">*</span>";
            }

            String selectClass = "customFloatingSelect " + classes;
            if (multiple) {
                selectClass = "customFlaotingMultiselect " + classes;
            }

            String margin_bottom = "";
            if (count < model_array_map.size()) {
                margin_bottom = "mb-3";
            }
            String formgroupdivclass = "align-items-center mb-0";
            if (type.equalsIgnoreCase("checkbox")) {
                formgroupdivclass = formgroupdivclass + " d-flex";
            }
            if (type.equalsIgnoreCase("long") || type.equalsIgnoreCase("StringValueDropDown")) {
                formgroupdivclass = formgroupdivclass + " select2Part";
            }

            if (style.contains("display:none") && !isHidden) {
                modal_data = modal_data + "<div style='" + style + "' class='col-md-" + divcount + " " + margin_bottom + "'><div class='" + formgroupdivclass + " form-group floating-group'>";
                style = style.replaceAll("display:none", "");
            } else if (!isHidden) {
                modal_data = modal_data + "<div class='col-md-" + divcount + " " + margin_bottom + "'><div class='" + formgroupdivclass + " form-group floating-group'>";
            }
            if (type.equalsIgnoreCase("long")) {
                //JSONArray reference_result_array = filter_json.optJSONArray("result_array");
                LinkedList<Map<String, String>> reference_result_array = (LinkedList<Map<String, String>>) filter_json.get("filter_data");

                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<select title='" + label_title + "' " + multiple_select + " onchange='" + onchange_event + "' name='" + filter_name + "' id='" + filter_name + "' class='" + selectClass + " floating-control modaldata' style='width:100%;" + style + "'>";

                if (!only_data && !multiple) {
                    modal_data = modal_data + "<option value=-1> " + filter_title + "</option>";
                }

                if (reference_result_array != null) {
                    ArrayList<Long> select_valuelist = new ArrayList<Long>();
                    if (multiple) {
                        String selected_value = DataTypeUtility.stringValue(request.getParameter(filter_name));
//                                    // System.out.println("filter_name>>> " + filter_name);
//                                    // System.out.println("selected_value>>> " + selected_value);
                        for (String pram : selected_value.split(",")) {
                            select_valuelist.add(DataTypeUtility.longValue(pram));
                        }
                    }

//                                    for (int filter_value_count = 0; reference_result_array.length() > filter_value_count; filter_value_count++) {
//                                        JSONObject ref_data_json = reference_result_array.optJSONObject(filter_value_count);
                    for (Iterator iter2 = reference_result_array.iterator(); iter2.hasNext(); ) {
                        Map<String, Object> ref_data_json = (Map<String, Object>) iter2.next();
                        String key = DataTypeUtility.stringValue(ref_data_json.get("__key__"));
                        String keyname = DataTypeUtility.stringValue(ref_data_json.get("__keyname__"));
                        if (DataTypeUtility.equal_value_list(request.getAttribute(filter_name), key, "long", multiple, select_valuelist)) {
                            modal_data = modal_data + "<option selected='selected' value='" + key + "'>" + keyname + "</option>";
                        } else {
                            modal_data = modal_data + "<option value='" + key + "'>" + keyname + "</option>";

                        }
                    }
                }

                modal_data = modal_data + " </select>";
            }
            if (type.equalsIgnoreCase("StringValueDropDown")) {
                //JSONArray reference_result_array = filter_json.optJSONArray("result_array");
                LinkedList<Map<String, String>> reference_result_array = (LinkedList<Map<String, String>>) filter_json.get("filter_data");

                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<select title='" + label_title + "' data-placeholder='Select " + DataTypeUtility.stringValue(filter_json.get("title")) + "'" + multiple_select + " onchange='" + onchange_event + "' name='" + filter_name + "' id='" + filter_name + "' class='" + selectClass + " floating-control modaldata' style='width:100%;" + style + "'>";

                if (!only_data && !multiple) {
                    modal_data = modal_data + "<option value=-1> " + filter_title + "</option>";
                }

                if (reference_result_array != null) {
                    ArrayList<String> select_valuelist = new ArrayList<>();
                    if (multiple) {
                        String selected_value = DataTypeUtility.stringValue(request.getParameter(filter_name));
//                                    // System.out.println("filter_name>>> " + filter_name);
//                                    // System.out.println("selected_value>>> " + selected_value);
                        for (String pram : selected_value.split(",")) {
                            select_valuelist.add(DataTypeUtility.stringValue(pram));
                        }
                    }

//                                    for (int filter_value_count = 0; reference_result_array.length() > filter_value_count; filter_value_count++) {
//                                        JSONObject ref_data_json = reference_result_array.optJSONObject(filter_value_count);
                    for (Iterator iter2 = reference_result_array.iterator(); iter2.hasNext(); ) {
                        Map<String, Object> ref_data_json = (Map<String, Object>) iter2.next();
                        String key = DataTypeUtility.stringValue(ref_data_json.get("__key__"));
                        String keyname = DataTypeUtility.stringValue(ref_data_json.get("__keyname__"));
                        if (DataTypeUtility.equal_value_list_string(request.getAttribute(filter_name), key, "StringValueDropDown", multiple, select_valuelist)) {
                            modal_data = modal_data + "<option selected='selected' value='" + key + "'>" + keyname + "</option>";
                        } else {
                            modal_data = modal_data + "<option value='" + key + "'>" + keyname + "</option>";

                        }
                    }
                }

                modal_data = modal_data + " </select>";
            } else if (!isHidden && type.equalsIgnoreCase("textbox") || type.equalsIgnoreCase("fts")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input  title='" + label_title + "' type='text' autocomplete='off' class='form-control table_filter floating-control modaldata " + classes + "' " +
                        "value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" +
                        " id='" + filter_name + "' style='width: 100%; " + style + "'/>";

            } else if (!isHidden && type.equalsIgnoreCase("date") || type.equalsIgnoreCase("fromdate") || type.equalsIgnoreCase("todate")) {

                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input title='" + label_title + "'  type='text' autocomplete='off' class='form-control pick-date floating-control modaldata " + classes + "' " +
                        "value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "'" +
                        " id='" + filter_name + "' style='width: 100%;" + style + "'/><img src='../../../../assets/images/calendar-icon.svg' class='pull-right up-mar25 me-2'>";

            } else if (!isHidden && type.equalsIgnoreCase("textarea")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<textarea title='" + label_title + "' class='form-control floating-control modaldata " + classes + "' id='" + filter_name + "' rows='3' style='width: 100%;" + style + "' ></textarea>";

            } else if (!isHidden && type.equalsIgnoreCase("checkbox")) {
//                modal_data = modal_data + "<p class='mb-0'>" + filter_title + "</p><div class='common-checkbox-toggle b2 ms-3'><input type='checkbox' class='checkbox-toggle-btn modaldata' id='" + filter_name + "'/>" +
//                        " <div class='knobs'><span></span></div><div class='layer'></div></div>";
                if (!StringUtils.isEmpty(onchange_event)) {
                    modal_data = modal_data + "<label class='mb-0'>" + filter_title + "</label><div class='common-checkbox-toggle b2 ms-3'><input type='checkbox' title='" + label_title + "' class='checkbox-toggle-btn modaldata'  onchange='" + onchange_event + "' id='" + filter_name + "'/>" +
                            " <div class='knobs'><span></span></div><div class='layer'></div></div>";
                } else {
                    modal_data = modal_data + "<label class='mb-0'>" + filter_title + "</label><div class='common-checkbox-toggle b2 ms-3'><input title='" + label_title + "' type='checkbox' class='checkbox-toggle-btn modaldata'  id='" + filter_name + "'/>" +
                            " <div class='knobs'><span></span></div><div class='layer'></div></div>";
                }
            } else if (!isHidden && type.equalsIgnoreCase("editor")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<div class='custom-ck-editor floating-control modaldata' id='" + filter_name + "'></div>";
            } else if (!isHidden && type.equalsIgnoreCase("issearchbox")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input title='" + label_title + "' class='form-control floating-control modaldata' autocomplete='off' role='combobox' list='' id='" + filter_name + "' name='browsers' onkeyup='" + onchange_event + "'>";
                modal_data = modal_data + "<datalist id='browsers' role='listbox'></datalist>";
            } else if (!isHidden && type.equalsIgnoreCase("time")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input title='" + label_title + "' type='text' autocomplete='off' class='form-control table_filter floating-control pick-time modaldata " + classes + "' id='" + filter_name + "' name='" + filter_name + "' value=''/>";

            }else if (!isHidden && type.equalsIgnoreCase("datetime")) {
                modal_data = modal_data + "<label class='floating-label'>" + filter_title + "</label>";
                modal_data = modal_data + "<input title='" + label_title + "' type='text' autocomplete='off' class='form-control table_filter floating-control pick-date-time modaldata " + classes + "' id='" + filter_name + "' name='" + filter_name + "' value=''/>";

            } else if (type.equalsIgnoreCase("attachment_file")) {
                modal_data = modal_data + "<div class='file-upload-wrapper documents-upload  col-md-" + divcount + "'  id='attachment_div'><input type='file' name='" + filter_name + "' id='" + filter_name + "' class='inputfile inputfile-1 modaldata " + divcount + "' data-multiple-caption='{count} files selected' ext='jpg,jpeg,png,pdf,doc,docx,zip,xls,pptx,ppt,xlsx,csv' accept='image/png, image/gif, image/jpeg' onchange='fileCheck(this)'/>";
                modal_data = modal_data + "<label for='" + filter_name + "'>";
//                modal_data = modal_data + "<img src='/student_new_assests/images/tc_request/attachment-icon.svg' alt='attachment-icon' />";
                modal_data = modal_data + "<span id='" + filter_name + "_span'>Attachment</span>";
                modal_data = modal_data + "</label>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "<div class='documents-upload col-md-" + divcount + "' id='old_attachment_span' style='display: none'>";
                modal_data = modal_data + "<div class='d-flex align-items-center file-upload-wrapper ps-2'><input name='old_" + filter_name + "' value='{}' id='old_" + filter_name + "'  type='hidden'>";
                modal_data = modal_data + "<label for='fileanchor' class='mb-1 mt-1 me-2'>";
                modal_data = modal_data + "<img src='/student_new_assests/images/tc_request/attachment-icon.svg' alt='attachment-icon' />";
                modal_data = modal_data + "</label>";
                modal_data = modal_data + "<a href='' class='text-nowrap' id='fileanchor'></a>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "<div class='position-absolute end-0 me-1 mt-2 top-0'>";
                modal_data = modal_data + "<div class='mx-1 " + divcount + "' id='attachment_existfile' style='display: none;'>";
                modal_data = modal_data + "<a target='_blank' rel='noopener noreferrer nofollow' id='attachment_existfile_src' href='' class='sk_attachment_button " + divcount + "'></a>";
                modal_data = modal_data + "<a href='#' onclick='changefilewithids(\"" + filter_name + "\")'><img src=\"../../../../assets/images/del_icon_drop-hover.svg\" alt=\"delete icon\"></a>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "</div>";
                modal_data = modal_data + "</div>";

            } else if (type.equalsIgnoreCase("attachment_file_new")) {
                modal_data = modal_data + "<div  class='col-sm-"+divcount+ " mb-3'>";
                modal_data = modal_data + "<label class='mb-1'>"+filter_title+"</label>";
                modal_data = modal_data + "<div class='file-upload'><div class='file-select position-relative'>";
                modal_data = modal_data + "<div class='file-select-button'>Choose File</div>";
                modal_data = modal_data + "<div id='"+filter_name+"_name' class='file-select-name flex-col-wide'></div>";
                modal_data = modal_data + "<input filesize='10240' type='file' id='"+filter_name+"' name='"+filter_title+"' class='choose-file-control "+classes+"'>";
                modal_data = modal_data + "<div class='file-remove position-relative top-50 end-0 translate-middle-y z-index-top me-2 curser'>";
                modal_data = modal_data + "<a id='"+filter_name+"_a_tag' href='' class='d-none'>";
                modal_data = modal_data + "<i class='fa fa-download fs-12 text-black-50 position-relative top-2 p-10'>";
                modal_data = modal_data + "</i></a>";
                modal_data = modal_data + "<i id='"+filter_name+"_close_but' class='d-none fa fa-close fs-12 text-black-50 position-relative top-2'></i>";
                modal_data = modal_data + "</div></div></div></div>";

            } else if (!isHidden && type.equalsIgnoreCase("color")) {
                modal_data = modal_data + "<label class='mb-0'>" + filter_title + "</label><div class='align-items-center d-flex'><input type='color' id='" + filter_name + "' value='" + DataTypeUtility.isnullvalue(param_map.get(filter_name)) + "' name='" + filter_name + "' class='clr-picker-control form-control ms-2 h-30' colorpick-eyedropper-active='true'>" +
                        "</div>";
            }
            if (isHidden) {
                modal_data = modal_data + "<input type='hidden' value='" + hidden_value + "' id='" + filter_name + "' name='" + filter_name + "'/>";
            }
            if (!isHidden) {
                modal_data = modal_data + "</div></div>";
            }

            count++;

        }

        return modal_data;

    }

    public static Object isnullvalue(Object value) {
        if (value == null) {
            value = "";
        }
        return value;
    }

    public static Object isnullvaluenew(Object value) {
        if (value == null || value == "null" || value.equals("null")) {
            value = "";
        }
        return value;
    }

    public static Object ishyperlink(Object value) {
        if (value == null || value.toString().trim().length() == 0) {
            value = "#";
        }
        return value;
    }

    public static String getCurrentDateInIndianFormat() {
        Date date = new Date();
        return DataTypeUtility.getDateObjectInIndianFormat(date);
    }

    public static String getDateObjectInIndianFormat(Date date) {
        if (date != null) {
            return new SimpleDateFormat("dd-MM-yyyy").format(date);
        }
        return "";
    }

    public static String getCurrentDateInIndianFormatInSlash() {
        Date date = new Date();
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public static String getCurrentDateTimeInIndianFormat() {
        Date date = new Date();
        return getDateTimeInIndianFormatActual(date);
    }

    public static String getCurrentDateTimeInIndianformat() {
        Date date = new Date();
        return getDateTimeInIndianformats(date);
    }
    
    public static Date getCurrentDateTimeWithOutAdd() {
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        date = cal.getTime();
        return date;
    }

    public static String getDateTimeInIndianformats(Date date) {
        if (date != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            date = cal.getTime();
            return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(date);
        } else {
            return "";
        }

    }
    
    public static String getDateTimeInIndianFormatActual(Date date) {
        if (date != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            date = cal.getTime();
            return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(date);
        } else {
            return "";
        }
    }
    
    public static String getIndianDateForamt(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("dd-MM-yyyy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }
    
    public static String getDateObjectInUSFormat(Date date) {
        if (date != null) {
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return "";
    }
    
    public static Date getDateObject(String date) {

        try {

            if (date != null && DataTypeUtility.stringValue(date).length() > 0) {
                return new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } else {
                return null;
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            //// System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }

    }

    public static Date getDateObjectFromIndianFormat(String date) {
        date = replaceTfromDateTime(date);
        try {
            if (date != null && date.length() > 0) {
                if (date.contains("/")) {
                    return new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } else if (date.contains("-")) {
                    return new SimpleDateFormat("dd-MM-yyyy").parse(date);
                } else {
                    return new SimpleDateFormat("dd MMM yyyy").parse(date);
                }
            }
            return null;
        } catch (Exception e) {
            //// System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean isMaxDateTime(Date max, Date min) {
        if (max.compareTo(min) >= 0) {
            return true;
        }
        return false;
    }

    public static String getIndianDateFormatFromIndianDateTime(Object dateObject) {
        Date date = null;
        String dateformat = null;
        if (dateObject != null) {
            SimpleDateFormat queryDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat updateDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat queryDateFormat_2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat updateDateFormat_2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            try {
                date = queryDateFormat.parse("" + dateObject);
                dateformat = getDateObjectInIndianFormat(date);

            } catch (ParseException e) {
                try {
                    date = updateDateFormat.parse("" + dateObject);
                    dateformat = getDateObjectInIndianFormat(date);
                } catch (ParseException e1) {
                    try {
                        date = queryDateFormat_2.parse("" + dateObject);
                        dateformat = getDateObjectInIndianFormat(date);
                    } catch (ParseException e2) {
                        try {
                            date = updateDateFormat_2.parse("" + dateObject);
                            dateformat = getDateObjectInIndianFormat(date);
                        } catch (ParseException e3) {
                            e1.printStackTrace();
                            throw new RuntimeException("date is not parsable" + e.getMessage());
                        }
                    }
                }
            }
        }
        return dateformat;
    }


    public static String getDateFromIndainDateTime(String date) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                } catch (Exception e) {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static HashSet<Long> getOldValuesToLongSet(String oldvalues) {
        String[] oldArray = null;
        HashSet<Long> old_set = new HashSet<>();
        if (oldvalues != null && oldvalues.length() > 0) {
            oldArray = oldvalues.split(",");
            for (int counter = 0; counter < oldArray.length; counter++) {
                Long old_value = DataTypeUtility.longValue((oldArray[counter]).trim());
                old_set.add(old_value);
            }
        }
        return old_set;
    }

    public static DecimalFormat getNumberDecimalFormat() throws Exception {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String getRoundOffValue(Long round_off_id, Object value_obj) {

        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        return getDecimalRoundOffValue(round_off_id, value_obj, decimalFormat);
    }

    public static String getDecimalRoundOffValue(Long round_off_id, Object value_obj, DecimalFormat decimalFormat) {


        boolean is_negative_value = false;
        if (round_off_id != null && value_obj != null) {

            Double value = DataTypeUtility.doubleZeroValue(value_obj);

            if (value < 0) {
                is_negative_value = true;
                value = -value;
            }

            if (round_off_id == 1l) {

                value = value + .5000001d;

                int i_value = (int) (double) value;

                if (is_negative_value) {
                    i_value = -i_value;
                }
                return decimalFormat.format(i_value);
            } else if (round_off_id == 2l) {
                value = value + .990d;
                int i_value = (int) (double) value;
                if (is_negative_value) {
                    i_value = -i_value;
                }
                return decimalFormat.format(i_value);
            } else if (round_off_id == 3l) {
                value = value + .05d;
                double val = value % 1;
                val = val + .000001d;
                int main_number = (int) (double) value / 1;
                val = val * 10;
                int first_decimal = (int) val / 1;
                value = main_number + (first_decimal / 10d);
                if (is_negative_value) {
                    value = -value;
                }
                return decimalFormat.format(value);
            } else if (round_off_id == 4l) {
                double per_decimal = value % 1;
                int per_value = (int) (double) value / 1;
                if (per_decimal > .01f && per_decimal <= .50d) {
                    per_decimal = per_value + .50d;
                } else if (per_decimal > .50d) {
                    per_decimal = per_value + 1d;
                } else {
                    per_decimal = per_value;
                }
                value = per_decimal;
                if (is_negative_value) {
                    value = -value;
                }
                return decimalFormat.format(value);
            } else if (round_off_id == 5l) {
                double per_decimal = value % 1;
                int per_value = (int) (double) value / 1;
                if (per_decimal > .01d && per_decimal <= .25d) {
                    per_decimal = per_value + .25d;
                } else if (per_decimal > .01d && per_decimal <= .50d) {
                    per_decimal = per_value + .50d;
                } else if (per_decimal > .01d && per_decimal <= .75d) {
                    per_decimal = per_value + .75d;
                } else if (per_decimal > .75d) {
                    per_decimal = per_value + 1d;
                } else {
                    per_decimal = per_value;
                }
                value = per_decimal;
                if (is_negative_value) {
                    value = -value;
                }
                return decimalFormat.format(value);
            } else if (round_off_id == 6l) {

                value = value + .0051d;
                double val = value % 1;

                int main_number = (int) (double) value / 1;
                val = val * 100;
                int first_decimal = (int) val / 1;
                double friction = first_decimal / 100d;


                value = main_number + (friction);
                if (is_negative_value) {
                    value = -value;
                }
                return decimalFormat.format(value);
            } else if (round_off_id == 7l) {
                int i_value = (int) (double) value;
                if (is_negative_value) {
                    i_value = -i_value;
                }
                return decimalFormat.format(i_value);
            } else if (round_off_id == 8l) {
                double val = value % 1;

                int main_number = (int) (double) value / 1;
                val = val * 1000;
                int first_decimal = (int) val / 1;
                double friction = first_decimal / 1000d;

                value = main_number + (friction);
                if (is_negative_value) {
                    value = -value;
                }
                return decimalFormat.format(value);
            } else if (round_off_id == 9l) {
                // Two Decimal Round off Value
                value = value + .005d;
                double val = value % 1;
                val = val + .000001d;
                int main_number = (int) (double) value / 1;
                val = val * 100;
                int first_decimal = (int) val / 1;
                value = main_number + (first_decimal / 100d);
                return decimalFormat.format(value);

            } else {
                return "0";
            }

        } else if (round_off_id == null && value_obj != null) {
            return decimalFormat.format(value_obj);
        } else {
            return "0";
        }

    }

    public static String getNumberValueDecimalFormat(Object number) {
        try {
            if (number != null) {
                DecimalFormat decimalFormat = getNumberDecimalFormat();
                number = DataTypeUtility.doubleZeroValue(getRoundOffValue(6l, number));
                return decimalFormat.format(number);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "";
    }


    public static Date getDateObjectFromIndianDateTime(String date) {
        date = replaceTfromDateTime(date);
        try {
            if (date != null && date.length() > 0) {
                return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
            }
            return null;
        } catch (Exception e) {
            try {
                if (date != null && date.length() > 0) {
                    return new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                }
                return null;
            } catch (Exception e1) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getFTSString(String fts) {

        fts = DataTypeUtility.stringValue(fts);
        fts = fts.replaceAll("'", "");
        fts = fts.replaceAll("\"", "");
        fts = fts.toLowerCase();
        if (fts.contains("drop") || fts.contains("delete") || fts.contains("update") || fts.contains("alert")) {
            return "";
        }
        return fts;
    }

    public static boolean isMaxDateTimeMore(Date max, Date min) {
        if (max.compareTo(min) > 0) {
            return true;
        }
        return false;
    }

    public static String getDateTimeInUSFormatMinusTime(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                } catch (Exception e) {
                    try {
                        dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                    } catch (Exception e1) {
                        dateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                    }
                }


                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);

                dateObject = cal.getTime();
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static void minusTimeZone(Calendar cal) {
//        if (Registry.IS_GAE) {
//        String timeZone = stringValue(Utility.getTimezone());
        String timeZone = "";
        if (timeZone.length() == 0) {
                timeZone = "+00:00";
                timeZone = "+05:30";
          
        }
        char symbol = timeZone.charAt(0);
        timeZone = timeZone.substring(1);
        int hour = integerValue(timeZone.split(":")[0]);
        int minute = integerValue(timeZone.split(":")[1]);

        cal.add(Calendar.HOUR_OF_DAY, (symbol == '+' ? (-1 * hour) : hour));
        cal.add(Calendar.MINUTE, (symbol == '+' ? (-1 * minute) : minute));
//        }
    }

    public static String getTimeFromIndianDateTime(String dateObject) {
        Date date = null;
        dateObject = replaceTfromDateTime(dateObject);

        String dateformat = null;
        if (dateObject != null) {
            SimpleDateFormat queryDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat updateDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                date = queryDateFormat.parse(dateObject);
                Calendar cal = new GregorianCalendar();
                cal.setTime(date);

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                String s_hour = "" + hour;
                if (hour < 10) {
                    s_hour = "0" + hour;
                }

                String s_minute = "" + minute;
                if (minute < 10) {
                    s_minute = "0" + minute;
                }

                dateformat = s_hour + ":" + s_minute;

            } catch (ParseException e) {
                try {
                    date = updateDateFormat.parse("" + dateObject);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(date);

                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    String s_hour = "" + hour;
                    if (hour < 10) {
                        s_hour = "0" + hour;
                    }

                    String s_minute = "" + minute;
                    if (minute < 10) {
                        s_minute = "0" + minute;
                    }

                    dateformat = s_hour + ":" + s_minute;
                } catch (ParseException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException("date is not parsable" + e.getMessage());
                }
            }
        }
        return dateformat;
    }


    public static String getDateTimeInUSFormatForUI(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
            } catch (ParseException e) {
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                } catch (ParseException e1) {
                    throw new RuntimeException("" + e1.getMessage());
                }
            }
            Calendar cal = new GregorianCalendar();
            cal.setTime(dateObject);
            dateObject = cal.getTime();
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);

        } else {
            return "";
        }

    }

    public static Date getDateTimeFromUSDatetime(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                return cal.getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public static String[] getStringArrayFromCommaValue(String str_data) {
        str_data = DataTypeUtility.stringValue(str_data);
        return str_data.split(",");
    }

    public static String stringValueWithHyphen(Object value) {
        if (value == null || value.toString().equalsIgnoreCase("undefined") || value.toString().equalsIgnoreCase("null")) {
            return "-";
        }
        return (value.toString()).trim();
    }

    public static ArrayList<Long> getListFromCommaValue(String str_data) {
        str_data = DataTypeUtility.stringValue(str_data);
        String[] str_array = str_data.split(",");
        ArrayList<Long> list = new ArrayList<>();
        for (String str : str_array) {
            str = DataTypeUtility.stringValue(str.trim());
            if (str.length() > 0 && DataTypeUtility.getForeignKeyValue(str) != null) {
                list.add(longValue(str));
            }
        }

        return list;
    }

    public static String getDateTimeInIndianFormat(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static void addTimeZoneInServer(Calendar cal,HttpServletRequest
                                            request) {
//        String timeZone = stringValue(Utility.getTimezone());
        String timeZone = "";
        if (timeZone.length() == 0) {
                timeZone = "+00:00";
          
        }

        char symbol = timeZone.charAt(0);
        timeZone = timeZone.substring(1);
        int hour = integerValue(timeZone.split(":")[0]);
        int minute = integerValue(timeZone.split(":")[1]);
        cal.add(Calendar.HOUR_OF_DAY, (symbol == '+' ? hour : (-1 * hour)));
        cal.add(Calendar.MINUTE, (symbol == '+' ? minute : (-1 * minute)));
//        }
    }

    public static boolean requireNonNullLong(Long value) {
        if (value != null && value > 0) {
            return true;
        }
        return false;
    }

    public static boolean requireNonNullDouble(Double value) {
        if (value != null && value > 0.0d) {
            return true;
        }
        return false;
    }

    public static boolean requireNonNullFloat(Float value) {
        if (value != null && value > 0.0f) {
            return true;
        }
        return false;
    }

    public static boolean requireNonNullMap(Map map) {
        if (map != null && map.size() > 0) {
            return true;
        }
        return false;
    }

    public static String getDayHour(String dateStart, String dateStop) {
        String dayhour = "";
        dateStart = replaceTfromDateTime(dateStart);
        dateStop = replaceTfromDateTime(dateStop);
        if (dateStart != null && dateStop != null && dateStart.length() > 0 && dateStop.length() > 0) {
            SimpleDateFormat queryDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date dt1 = null;
            Date dt2 = null;
            try {
                dt1 = queryDateFormat.parse(dateStart);
                dt2 = queryDateFormat.parse(dateStop);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long different = dt2.getTime() - dt1.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            if (elapsedDays > 0l) {
                dayhour = elapsedDays + " Days " + elapsedHours + " Hours and " + elapsedMinutes + " Minutes";
            } else if (elapsedHours > 0l) {
                dayhour = elapsedHours + " Hours and " + elapsedMinutes + " Minutes";
            } else {
                dayhour = elapsedMinutes + " Minutes";
            }
        }
        return dayhour;
    }

    public static String getMonthYearFormat(String date) {
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("MMM yyyy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }
    
    public static Date getDateObjectFromUsDateTimeFormat(String date) {
        date = replaceTfromDateTime(date);

        try {
            if (date != null && date.length() > 0) {
                if (date.contains("/")) {


                    return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
                } else {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                }
            }
            return null;
        } catch (Exception e) {
            //// System.out.println(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static int getAgeInYear(Date max, Date min) {

        if (max == null || min == null) {
            return 0;
        }

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(max);
        long years = 0;
        long months = 0;
        long days = 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(min);
        int month = startDate.get(Calendar.MONTH) + 1;
        int year = startDate.get(Calendar.YEAR);
        int date = startDate.get(Calendar.DAY_OF_MONTH);

        LocalDate date1 = LocalDate.of(year, month, date);

        month = endDate.get(Calendar.MONTH) + 1;
        year = endDate.get(Calendar.YEAR);
        date = endDate.get(Calendar.DAY_OF_MONTH);


        LocalDate date2 = LocalDate.of(year, month, date);
        Period p = Period.between(date1, date2);
        return p.getYears();

    }

    public static int getAgeInMonth(Date max, Date min) {

        if (max == null || min == null) {
            return 0;
        }
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(max);
        long years = 0;
        long months = 0;
        long days = 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(min);
        int month = startDate.get(Calendar.MONTH) + 1;
        int year = startDate.get(Calendar.YEAR);
        int date = startDate.get(Calendar.DAY_OF_MONTH);

        LocalDate date1 = LocalDate.of(year, month, date);

        month = endDate.get(Calendar.MONTH) + 1;
        year = endDate.get(Calendar.YEAR);
        date = endDate.get(Calendar.DAY_OF_MONTH);


        LocalDate date2 = LocalDate.of(year, month, date);
        Period p = Period.between(date1, date2);

        return p.getMonths();
    }

    public static int getAgeInDay(Date max, Date min) {


        if (max == null || min == null) {
            return 0;
        }

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(max);
        long years = 0;
        long months = 0;
        long days = 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(min);
        int month = startDate.get(Calendar.MONTH) + 1;
        int year = startDate.get(Calendar.YEAR);
        int date = startDate.get(Calendar.DAY_OF_MONTH);

        LocalDate date1 = LocalDate.of(year, month, date);

        month = endDate.get(Calendar.MONTH) + 1;
        year = endDate.get(Calendar.YEAR);
        date = endDate.get(Calendar.DAY_OF_MONTH);


        LocalDate date2 = LocalDate.of(year, month, date);
        Period p = Period.between(date1, date2);

        return p.getDays();
    }
    

    public static String getDateTimeInIndianFormatForUI(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
//                addTimeZone(cal);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }



    public static String decryptDataKey(String input) {
        return input;
    }

    public static String getDateObjectIndiaFormat() {
        Date date = new Date();

        if (date != null) {
            return new SimpleDateFormat("dd-MM-yyyy").format(date);
        }
        return "";
    }



    public static String timevalue(Object value) {

        String time = stringValue(value);
        String updated_time = "";
        if (time.length() > 0) {
            String[] timearray = time.split(":");
            for (int counter = 0; timearray.length > counter && counter < 2; counter++) {
                if (updated_time.length() == 0) {
                    updated_time = timearray[counter];
                } else {
                    updated_time = updated_time + ":" + timearray[counter];
                }
            }
        }

        return updated_time;
    }

    public static String getIndianDateTimeFormatFromUsDateTime(Object dateObject) {
        Date date = null;
        String dateformat = null;
        if (dateObject != null) {
            SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat updateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat queryDateFormat_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat updateDateFormat_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat indianformat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                date = queryDateFormat.parse("" + dateObject);
                dateformat = indianformat.format(date);

            } catch (ParseException e) {
                try {
                    date = updateDateFormat.parse("" + dateObject);
                    dateformat = indianformat.format(date);
                } catch (ParseException e1) {
                    try {
                        date = queryDateFormat_2.parse("" + dateObject);
                        dateformat = indianformat.format(date);
                    } catch (ParseException e2) {
                        try {
                            date = updateDateFormat_2.parse("" + dateObject);
                            dateformat = indianformat.format(date);
                        } catch (ParseException e3) {
                            e1.printStackTrace();
                            throw new RuntimeException("date is not parsable" + e.getMessage());
                        }
                    }
                }
            }
        }
        return dateformat;
    }

    public static Boolean isValidEmailId(String emailid) {
        if (emailid != null && emailid.length() > 0) {
            String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            Pattern p = Pattern.compile(EMAIL_PATTERN);
            Matcher m = p.matcher(emailid);
            return m.matches();
        }
        return false;
    }

    public static String getBackSlashResolvedForDB(String value) {
        String separator = "\\";
        if (value.contains("\\")) {
            String[] temp_arr = value.split(Pattern.quote(separator));
            StringBuilder navit_build = new StringBuilder();
            for (int i = 0; i < temp_arr.length; i++) {
                navit_build.append(temp_arr[i]);
                if (i < temp_arr.length - 1)
                    navit_build.append("\\\\");
            }
            value = navit_build.toString();
        }
        return value;
    }

    public static long daysBetween(Date max, Date min) {
        return (max.getTime() - min.getTime()) / 86400000;
    }

    public static boolean getTwoDateBetween(String maxDate, String minDate, String actualDate) {

        Date date_maxDate = (Date) DataTypeUtility.getDateObject("" + maxDate);
        Date date_minDate = (Date) DataTypeUtility.getDateObject("" + minDate);
        Date date_actualDate = (Date) DataTypeUtility.getDateObject("" + actualDate);

        if (date_actualDate.before(date_maxDate) && date_minDate.before(date_actualDate)) {
            return true;
        } else {
            return false;
        }

    }




    public static String getIndianDateTimeFromUSFormatTask(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);

                } catch (Exception e) {
                    try {
                        dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                    } catch (Exception e1) {
                        dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    }
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);

                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String getDateTimeInIndianInspiniaTask(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy hh:mm aa").format(dateObject).toUpperCase();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String getDayHourFromUsDateTimeFormat(String dateStart, String dateStop) {
        String dayhour = "";
        dateStart = replaceTfromDateTime(dateStart);
        dateStop = replaceTfromDateTime(dateStop);
        if (dateStart != null && dateStop != null && dateStart.length() > 0 && dateStop.length() > 0) {
            SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dt1 = null;
            Date dt2 = null;
            try {
                dt1 = queryDateFormat.parse(dateStart);
                dt2 = queryDateFormat.parse(dateStop);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long different = dt2.getTime() - dt1.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            if (elapsedDays > 0l) {
                dayhour = elapsedDays + " Days " + elapsedHours + " Hours and " + elapsedMinutes + " Minutes";
            } else if (elapsedHours > 0l) {
                dayhour = elapsedHours + " Hours and " + elapsedMinutes + " Minutes";
            } else {
                dayhour = elapsedMinutes + " Minutes";
            }
        }
        return dayhour;
    }

    public static String ZeroPad(long number, Long width) {
        StringBuffer result = new StringBuffer("");
        if (width != null) {
            for (int i = 0; i < width - Long.toString(number).length(); i++)
                result.append("0");
        }
        result.append(Long.toString(number));
        return result.toString();
    }

    public static String getMonthFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("MMM").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String isToggleTrue(Object val) {

        String checkedvalue = "";
        if (val != null && DataTypeUtility.booleanValue(val)) {
            checkedvalue = "checked='true'";
        }

        return toggle_1 + checkedvalue + toggle_2;
    }






    public static String getNumberValueDecimalFormat_Zero(Float number) {
        try {
            if (number != null) {
                DecimalFormat decimalFormat = getNumberDecimalFormat();
                Double number1 = DataTypeUtility.doubleZeroValue(getRoundOffValue(6l, number));
                return decimalFormat.format(number1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "0.00";
    }
    
    public static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // put sorted list into map again
        //LinkedHashMap make sure order in which keys were inserted
        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    

    public static String getEncryptedPassword(String password) {
        try {
            MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(password.getBytes(), 0, password.length());
            return new BigInteger(1, mdEnc.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("problem while " + e.getMessage(), e);
        }
    }
    
    public static String getNumberWithZero(Long i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }

    public static boolean isMaxDateTimeWithoutEqual(Date max, Date min) {
        return isMaxDateTimeOnly(max, min);
    }

    public static boolean isMaxDateTimeOnly(Date max, Date min) {

        if (max.compareTo(min) > 0) {
            return true;
        }
        return false;
    }

    public static String getTimeWithSecond(String time1) {
        if (time1 != null && time1.length() > 0) {
            time1 = replaceTfromDateTime(time1);
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

            try {
                Date d1 = format1.parse(time1);
                return format2.format(d1);
            } catch (Exception e) {
                return time1;
            }
        }
        return "";
    }

    public static String getUSDateFromUSDateTime(String dateObject) {
        dateObject = replaceTfromDateTime(dateObject);


        SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (dateObject != null) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").format(queryDateFormat.parse(dateObject));
            } catch (ParseException e) {
                throw new RuntimeException("date is not parsable" + e.getMessage());
            }
        }
        return null;
    }
    

    public static String getUSDateTimeFromLongDate(Long time) {
        if (time != null) {
            Date date = new Date(time);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } else {
            return "";
        }
    }

    public static String getIndianDateTimeInUSFormat(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                } catch (Exception e) {
                    try {
                        dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                    } catch (Exception e1) {
                        dateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                    }
                }


                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                
//                addTimeZone(cal);
                dateObject = cal.getTime();
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static void printParameters(HttpServletRequest req) {
        printParameters(req, true);
    }

    public static void printParameters(HttpServletRequest req, boolean islogs) {
        //// System.out.println("Parameter Method Calling");
        if (islogs) {
            Map<String, String[]> paramMap = (Map<String, String[]>) req.getParameterMap();
            int size = paramMap == null ? 0 : paramMap.size();


            Set<Map.Entry<String, String[]>> entrySet = paramMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {

                String[] values = (String[]) entry.getValue();
                int valueCount = values == null ? 0 : values.length;
                String val = "";
                for (int i = 0; i < valueCount; i++) {
                    val = DataTypeUtility.getCommaValue(val, values[i]);
                }
                System.out.println("Req Param Value: [" + entry.getKey() + "]valueCount[" + valueCount + "] and Value is: " + val);
            }
        }
    }

    public static void printParametersPG(HttpServletRequest req, boolean islogs) {
        //// System.out.println("Parameter Method Calling");
        if (islogs) {
            Map<String, String[]> paramMap = (Map<String, String[]>) req.getParameterMap();
            Set<Map.Entry<String, String[]>> entrySet = paramMap.entrySet();
            for (Map.Entry<String, String[]> entry : entrySet) {

                String[] values = (String[]) entry.getValue();
                int valueCount = values == null ? 0 : values.length;
                String val = "";
                for (int i = 0; i < valueCount; i++) {
                    val = DataTypeUtility.getCommaValue(val, values[i]);
                }
               
            }
        }
    }
    
    
    public static Boolean getMaxDateWithoutTime(String date1, String date2) {

        Date date11 = (Date) DataTypeUtility.getDateObject("" + date1);
        Date date21 = (Date) DataTypeUtility.getDateObject("" + date2);
        if (date11.before(date21)) {
            return true;
        } else {
            return false;
        }
    }

    public static HashSet<String> getOldValuesToSet(String oldvalues) {
        String[] oldArray = null;
        HashSet<String> old_set = new HashSet<String>();
        if (oldvalues != null && oldvalues.length() > 0) {
            oldArray = oldvalues.split(",");
            for (int counter = 0; counter < oldArray.length; counter++) {
                String old_value = (oldArray[counter]).trim();
                old_set.add(old_value);
            }
        }
        return old_set;
    }

    public static String getHARPStringValueFromSet(Set<String> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (String str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + "___HARP___" + str;
                }
            }

        }
        return itemstr;
    }

    public static LocalDateTime addMinsToJavaUtilDate(LocalDateTime dateTime, Long time) {
        LocalDateTime newDateTime = dateTime.plusMinutes(time);
        // System.out.println(newDateTime);
        return newDateTime;
    }

    public static LocalDateTime subHrsToJavaUtilDate(LocalDateTime dateTime, Long time) {
        LocalDateTime newDateTime = dateTime.minusHours(time);
        // System.out.println(newDateTime);
        return newDateTime;
    }

    public static LocalDateTime addDaysToJavaUtilDate(LocalDateTime dateTime, Long time) {
        LocalDateTime newDateTime = dateTime.plusDays(time);
        // System.out.println(newDateTime);
        return newDateTime;
    }


    public static LocalDateTime addHrsToJavaUtilDate(LocalDateTime dateTime, Long time) {
        LocalDateTime newDateTime = dateTime.plusHours(time);
        // System.out.println(newDateTime);
        return newDateTime;
    }

    public static LinkedHashMap<Long, String> levelOfSeverityMap() {
        LinkedHashMap<Long, String> map = new LinkedHashMap<>();
        map.put(1l, "Low");
        map.put(2l, "Medium");
        map.put(3l, "High");
        return map;
    }
    

    public static String getMinimumDate(String date1, String date2) {
        Date date11 = (Date) DataTypeUtility.getDateObject("" + date1);
        Date date21 = (Date) DataTypeUtility.getDateObject("" + date2);
        if (date11.before(date21)) {
            return date1;
        } else {
            return date2;
        }
    }

    public static String getMaxDate(String date1, String date2) {
        Date date11 = (Date) DataTypeUtility.getDateObject("" + date1);
        Date date21 = (Date) DataTypeUtility.getDateObject("" + date2);
        if (date11.before(date21)) {
            return date2;
        } else {
            return date1;
        }
    }

    public static List<String> getDatesBetweenList(Date startDate, Date endDate) {
        List<String> datesInRange = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        String end_Date = format.format(endDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            String result1 = format.format(calendar.getTime());
            datesInRange.add(result1);
            calendar.add(Calendar.DATE, 1);
        }
        datesInRange.add(end_Date);
        return datesInRange;
    }

    public static LinkedList<String> getDayMinutes() {
        LinkedList<String> dayminutelist = new LinkedList<String>();
        for (int counter = 0; counter <= 59; counter++) {
            String scounter = "";
            if (counter < 10) {
                scounter = "0" + counter;
            } else {
                scounter = "" + counter;
            }
            dayminutelist.add(scounter);
        }
        return dayminutelist;
    }

    public static LinkedList<String> getDayHours() {
        LinkedList<String> dayhrlist = new LinkedList<String>();
        for (int counter = 0; counter <= 23; counter++) {
            String scounter = "";
            if (counter < 10) {
                scounter = "0" + counter;
            } else {
                scounter = "" + counter;
            }
            dayhrlist.add(scounter);
        }
        return dayhrlist;
    }

    public static String getCommaValueFromSet(Set<Long> set) {
        String itemstr = "";
        if (set != null && set.size() > 0) {
            for (Long str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + ", " + str;
                }
            }

        }
        return itemstr;
    }


 
    
    public static float getUpperRoundOffValue(Float value) {
        int int_val = DataTypeUtility.integerValue(value);
        float float_val = DataTypeUtility.floatZeroValue(int_val);

        if (float_val < value) {
            int_val = int_val + 1;
        }
        return DataTypeUtility.floatZeroValue(int_val);
    }
    
    public static String getCommaValuewithoutSpace(String itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (itemstr.length() == 0) {
                itemstr = str.trim();
            } else {
                itemstr = itemstr.trim() + "," + str.trim();
            }
            itemstr.trim();
        }
        return itemstr;
    }

    public static String getYearFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("YYYY").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static LinkedHashMap<Long, String> getCandidateStatusMap() {
        LinkedHashMap<Long, String> map = new LinkedHashMap<>();
        map.put(1l, "Application Received");
        map.put(2l, "Shortlist");
        map.put(3l, "In Progress");
        map.put(5l, "Job Offer");
        map.put(6l, "Hired");
        map.put(4l, "Rejected");

        return map;
    }

    public static String[] getEmailIds(String emailid) {
        HashSet<String> email_list = new HashSet<>();
        if (emailid != null && emailid.length() > 2) {
            String[] email_array_1 = emailid.split(",");
            for (String email_str_1 : email_array_1) {
                email_str_1 = DataTypeUtility.stringValue(email_str_1);
                if (email_str_1.length() > 2) {
                    String[] email_array_2 = email_str_1.split(";");
                    for (String email_str_2 : email_array_2) {
                        email_str_2 = DataTypeUtility.stringValue(email_str_2);
                        if (email_str_2.length() > 2) {
                            email_list.add(email_str_2);
                        }
                    }

                }
            }
        }

        if (email_list.size() > 0) {
            String[] email_array = new String[email_list.size()];
            email_array = email_list.toArray(email_array);
            return email_array;
        } else {
            return null;
        }

    }

    public static void getIncrementValueHashMap(HashMap<Long, Integer> teacher_period_count, Long teacherid) {
        Integer period_count = DataTypeUtility.integerValue(teacher_period_count.get(teacherid));
        period_count++;
        teacher_period_count.put(teacherid, period_count);
    }

    public static HashSet<String> stringToSet(String str, String separator) {
        HashSet<String> set = new HashSet<>();
        if (str != null && str.length() > 0) {
            String[] str_array = str.split(separator);
            for (String str_id : str_array) {
                set.add(DataTypeUtility.stringValue(str_id));
            }
        }
        return set;
    }

    public static String getForeignkeyValueStr(String str_data) {
        str_data = DataTypeUtility.stringValue(str_data);
        String[] str_array = str_data.split(",");
        String new_str = "";
        for (String str : str_array) {
            str = DataTypeUtility.stringValue(str);
            if (str.length() > 0 && DataTypeUtility.getForeignKeyValue(str) != null) {
                new_str = DataTypeUtility.getCommaValue(new_str, str);
            }
        }
        if (new_str.length() == 0) {
            new_str = null;
        }
        return new_str;
    }

    public static String getCommaValueFromSetUsingSB(Set<Long> set) {
        StringBuilder itemstr = new StringBuilder();
        if (set != null && set.size() > 0) {
            for (Long str : set) {
                if (itemstr.length() > 0) {
                    itemstr.append(",");
                }
                itemstr.append(str);
            }
        }
        return "" + itemstr.toString();
    }


    public static LocalDateTime subDaysToJavaUtilDate(LocalDateTime dateTime, Long time) {
        LocalDateTime newDateTime = dateTime.minusDays(time);
        // System.out.println(newDateTime);
        return newDateTime;
    }

    public static LocalDateTime subMinsToJavaUtilDate(LocalDateTime dateTime, Long time) {
        LocalDateTime newDateTime = dateTime.minusMinutes(time);
        // System.out.println(newDateTime);
        return newDateTime;
    }

    public static boolean isUSDateFormat(String date) {
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getHidefromadmissionforms(String key) {
        HashSet<String> hidefromadmissionformsSet = new HashSet<>();
        hidefromadmissionformsSet.add("admission_enquirytypes");
        hidefromadmissionformsSet.add("nationality");
        hidefromadmissionformsSet.add("sms_qualifications");
        hidefromadmissionformsSet.add("admission_preferredmodeofcommunication");
        hidefromadmissionformsSet.add("admission_sourceofinformations");
        hidefromadmissionformsSet.add("sms_studenttypes");
        hidefromadmissionformsSet.add("sms_studentcategories");
        hidefromadmissionformsSet.add("sms_religions");
        hidefromadmissionformsSet.add("admission_categories");
        hidefromadmissionformsSet.add("sms_professions");
        hidefromadmissionformsSet.add("genders");
        if (hidefromadmissionformsSet != null && hidefromadmissionformsSet.size() > 0 && key != null && key.length() > 0 && hidefromadmissionformsSet.contains(key)) {
            if (key.equals("sms_professions")) {
                return "(hide_on_website=0 or hide_on_website is null)";
            } else {
                return "(hidefromadmissionforms=0 or hidefromadmissionforms is null)";
            }
        } else {
            return "";
        }
    }

    public static String getCurrencyRoundOffValue(Long round_off_id, Object value_obj) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return getDecimalRoundOffValue(round_off_id, value_obj, decimalFormat);
    }

    public static String convertCollectionToFilterString(Collection<?> collection) {
        String filter = "-1";
        if (requireNonNullCollection(collection)) {
            for (Object object : collection) {
                filter = getCommaValueFilter(filter, object.toString());
            }
        }
        return filter;
    }

    public static String getAdvanceDateTime(String date, long days) {
        Date dateObj = getDateObjectFromIndianDateTime(date);
        Calendar c = Calendar.getInstance();
        c.setTime(dateObj); // Now use today date.
        c.add(Calendar.DATE, integerValue(days));
        date = getDateTimeObjectInIndianFormat(c.getTime());
        return date;
    }

    public static String getActualNumberDecimalFormat(Object number) throws Exception {

        if (number != null) {
            return getRoundOffValue(6l, number);
        } else {
            return "";
        }
    }

    public static long dayBetween(Date max, Date min) {
        long diff = max.getTime() - min.getTime();


        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffDays;
    }

    public static String getActualCurrencyDecimalFormat(Object number) throws Exception {

        if (number != null) {
            return getCurrencyRoundOffValue(6l, number);
        } else {
            return "";
        }
    }

    public static String ZeroPad(long number, long width) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < width - Long.toString(number).length(); i++)
            result.append("0");
        result.append(Long.toString(number));
        return result.toString();
    }
    
    public static String[] getDayOfWeek(String date) {
        if (date != null && date.length() > 5) {
            Date dateObject = getDateObject(date);
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObject);
            String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            int dayid = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayid == 0) {
                dayid = 7;
            }
            String[] array = {DataTypeUtility.stringValue(day), DataTypeUtility.stringValue(dayid)};
            return array;
        } else {
            return null;
        }
    }

    public static void timeLogs(long starttime, int i) {
        if (starttime > 0l) {
            Date enddatetime = new Date();
            long endtime = enddatetime.getTime();
            long totaltime = endtime - starttime;
            // System.out.println(i + ".........>>><<.......Total Time[" + totaltime + "]");
        }
    }

    public static Date getCurrentDateAsItIs() {
        String usdate = getCurrentDateInUSFormatAsItIs();
        return getDateObject(usdate);
    }

    public static String getCurrentDateInUSFormatAsItIs() {
        Date date = new Date();
        date = addServerTimeAsItIs(date);
        return DataTypeUtility.getDateObjectInUSFormat(date);
    }

    public static Date addServerTimeAsItIs(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, -0);
            cal.add(Calendar.MINUTE, -0);
        return cal.getTime();
    }

    public static String getAdvanceDate(String date, long days) {
        Date dateObj = getDateObjectFromIndianFormat(date);
        Calendar c = Calendar.getInstance();
        c.setTime(dateObj); // Now use today date.
        c.add(Calendar.DATE, integerValue(days));
        date = getDateObjectInIndianFormat(c.getTime());
        return date;
    }

    public static String getTimeFromDateTime(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                } catch (Exception e) {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static boolean isEqualDateTime(Date max, Date min) {

        if (max.compareTo(min) == 0) {
            return true;
        }
        return false;
    }




    public static String getTimeInIndianFormatForReport(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                return new SimpleDateFormat("HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                try {
                    Date dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                    return new SimpleDateFormat("HH:mm").format(dateObject);
                } catch (ParseException e1) {
                    throw new RuntimeException("" + e1.getMessage());
                }
            }
//                Calendar cal = new GregorianCalendar();
//                cal.setTime(dateObject);
//                if (Registry.IS_GAE) {
//                    cal.add(Calendar.HOUR_OF_DAY, 5);
//                    cal.add(Calendar.MINUTE, 30);
//                }
//                dateObject = cal.getTime();


        } else {
            return "";
        }

    }


    public static Long getDaysBetweenTwoDates(String fromdate, String todate) {
        LocalDate dateBefore = LocalDate.parse(fromdate);
        LocalDate dateAfter = LocalDate.parse(todate);
        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
        return noOfDaysBetween;
    }


    public static String getIndianDateTimeFormatFromUsDateTimeWithTimeZone(Object dateObject, HttpServletRequest request) {
        Date date = null;
        String dateformat = null;
        if (dateObject != null) {
            SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat updateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat queryDateFormat_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat updateDateFormat_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat indianformat = new SimpleDateFormat("dd-MM-yyy HH:mm");

            try {
                date = queryDateFormat.parse("" + dateObject);
                Calendar cal = new GregorianCalendar();
                cal.setTime(date);
                date = cal.getTime();
                dateformat = indianformat.format(date);

            } catch (ParseException e) {
                try {
                    date = updateDateFormat.parse("" + dateObject);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(date);
                    date = cal.getTime();
                    dateformat = indianformat.format(date);
                } catch (ParseException e1) {
                    try {
                        date = queryDateFormat_2.parse("" + dateObject);
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(date);
                        date = cal.getTime();
                        dateformat = indianformat.format(date);
                    } catch (ParseException e2) {
                        try {
                            date = updateDateFormat_2.parse("" + dateObject);
                            Calendar cal = new GregorianCalendar();
                            cal.setTime(date);
                            date = cal.getTime();
                            dateformat = indianformat.format(date);
                        } catch (ParseException e3) {
                            e1.printStackTrace();
                            throw new RuntimeException("date is not parsable" + e.getMessage());
                        }
                    }
                }
            }
        }
        return dateformat;
    }

    public static String getIndainDatefromMonthDateYearAndTime(String date) {
        date = stringValue(date);
        if (date.length() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss");

            try {
                return getRealDateInIndianFormats(dateFormat.parse(date));
            } catch (ParseException var3) {
                return getIndianDateFormat(date);
            }
        } else {
            return "";
        }
    }

    public static String getRealDateInIndianFormats(Date date) {
        if (date != null) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            date = cal.getTime();
            return (new SimpleDateFormat("dd-MM-yyyy")).format(date);
        } else {
            return "";
        }
    }

    public static LocalDate findNextDay(LocalDate localdate, Long plusdays) {
        return localdate.plusDays(plusdays);
    }

    public static LocalDate findPreviousDay(LocalDate localdate, Long minusday) {
        return localdate.minusDays(minusday);
    }

    public static String getTimeFromUSDateTimeWithoutTimeZone(String date) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (Exception e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String getActualDateTimeInIndianTimeFormat(String date, HttpServletRequest request) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String diffBetweenTwoTime(String time1, String time2) {

        String dateformat = "";
        if (time1 != null && time2 != null && time1.length() > 0 && time2.length() > 0) {
            time1 = replaceTfromDateTime(time1);
            time2 = replaceTfromDateTime(time2);

            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");


            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format1.parse(time1);
            } catch (Exception e) {
                try {
                    d1 = format2.parse(time1);
                } catch (Exception e1) {
                }
            }


            try {
                d2 = format1.parse(time2);
            } catch (Exception e) {
                try {
                    d2 = format2.parse(time2);
                } catch (Exception e1) {
                }
            }
            ////// System.out.println(d1);
            ////// System.out.println(d2);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            ////// System.out.println(diff);
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;

            String s_hour = "" + diffHours;
            if (diffHours < 10 && diffHours >= 0) {
                s_hour = "0" + diffHours;
            } else if (diffHours < 0) {
                s_hour = "0";
            }

            String s_minute = "" + diffMinutes;
            if (diffMinutes < 10 && diffMinutes >= 0) {
                s_minute = "0" + diffMinutes;
            } else if (diffMinutes < 0) {
                s_minute = "0";
            }

            dateformat = s_hour + ":" + s_minute;
        }
        ////// System.out.println(dateformat);
        return dateformat;
    }

    public static ArrayList<String> getForeignkeyValueList(String str_data) {
        str_data = DataTypeUtility.stringValue(str_data);
        String[] str_array = str_data.split(",");
        ArrayList<String> list = new ArrayList<>();
        for (String str : str_array) {
            str = DataTypeUtility.stringValue(str);
            if (str.length() > 0 && DataTypeUtility.getForeignKeyValue(str) != null) {
                list.add(str);
            }
        }
        return list;
    }

    public static boolean isAlpha(String name) {
        return name.matches(".*[a-zA-Z]+.*");
    }

    public static Date getDateFromIndianDateTime(String dateObject) {
        Date date = null;
        dateObject = replaceTfromDateTime(dateObject);

        SimpleDateFormat queryDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat updateDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        SimpleDateFormat queryDateFormat_2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat updateDateFormat_2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        if (dateObject != null && dateObject.length() > 0) {
            try {
                date = queryDateFormat.parse(dateObject);
            } catch (ParseException e) {
                try {
                    date = updateDateFormat.parse(dateObject);
                } catch (ParseException e1) {
                    try {
                        date = queryDateFormat_2.parse(dateObject);
                    } catch (ParseException e2) {
                        try {
                            date = updateDateFormat_2.parse(dateObject);
                        } catch (ParseException e3) {
                            e1.printStackTrace();
                            throw new RuntimeException("date is not parsable" + e.getMessage());
                        }
                    }


                }
            }
        }
        return date;
    }

    public static Date dateValue(Object dateObject) {
        Date date = null;
        SimpleDateFormat queryDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat updateDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (dateObject != null && dateObject.toString().length() > 4) {
                date = queryDateFormat.parse("" + dateObject);
            }
        } catch (ParseException e) {
            try {
                date = updateDateFormat.parse("" + dateObject);
            } catch (ParseException e1) {
                e1.printStackTrace();
                throw new RuntimeException("date is not parsable" + e.getMessage());
            }
        }
        return date;
    }


    public static String todoubleSimpleWords(int number) {
        if (number >= 10000000) {
            return todoubleSimpleWords(number / 10000000) + " Crore " + todoubleSimpleWords(number % 10000000);
        } else if (number >= 100000) {
            return todoubleSimpleWords(number / 100000) + " Lakh " + todoubleSimpleWords(number % 100000);
        } else if (number >= 1000) {
            return todoubleSimpleWords(number / 1000) + " Thousand " + todoubleSimpleWords(number % 1000);
        } else if (number >= 100) {
            int rem = number % 100;
            return todoubleSimpleWords(number / 100) + " Hundred" + (rem == 0 ? "" : " " + todoubleSimpleWords(rem));
        } else if (number < 0) {
            return "";
        } else {
            if (number < 20)
                return englishUnits[number];
            int rem = number % 10;
            return englishTens[number / 10] + (rem == 0 ? "" : " " + englishUnits[rem]);
        }
    }
    
    public static String getIndianDateMonthwithhipenFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("dd-MMM").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String getTimeFromUSDateTime(String date) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (Exception e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }


    public static String getDecimalFormatvalue(DecimalFormat df, Object value) {
        if (value != null && value != "") {
            return df.format(value);
        } else {
            return "";
        }
    }

    public static String toSimpleWords(int number) {
        if (number >= 10000000) {
            return toSimpleWords(number / 10000000) + " Crore " + toSimpleWords(number % 10000000);
        } else if (number >= 100000) {
            return toSimpleWords(number / 100000) + " Lakh " + toSimpleWords(number % 100000);
        } else if (number >= 1000) {
            return toSimpleWords(number / 1000) + " Thousand " + toSimpleWords(number % 1000);
        } else if (number >= 100) {
            int rem = number % 100;
            return toSimpleWords(number / 100) + " Hundred" + (rem == 0 ? "" : " and " + toSimpleWords(rem));
        } else if (number < 0) {
            return "";
        } else {
            if (number < 20)
                return englishUnits[number];
            int rem = number % 10;
            return englishTens[number / 10] + (rem == 0 ? "" : " " + englishUnits[rem]);
        }
    }

    public static float getMarksRoundOffValue(Long round_off_id, Float value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        if (round_off_id != null && value != null) {
            if (round_off_id == 1l) {
                value = value + .5f;
            } else if (round_off_id == 2l) {
                value = value + .999f;
            }
            int i_value = (int) (float) value;
            return floatZeroValue(decimalFormat.format(i_value));
        } else if (round_off_id == null && value != null) {
            return floatZeroValue(decimalFormat.format(value));
        } else {
            return 0f;
        }
    }

    public static String ZeroPad(String number, long width) {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < width - number.length(); i++)
            result.append("0");
        result.append(number);
        return result.toString();
    }

    public static synchronized String getTransactionNo(String uid) {
        long time = (new Date()).getTime();
        String transactionno = uid + "" + time;
        transactionno = DataTypeUtility.ZeroPad(transactionno, 20);
        System.out.println(transactionno);
        return transactionno;
    }

    public static String getCurrentDateInUSFormatWithoutTimeZone() {
        Date date = new Date();
        return DataTypeUtility.getDateObjectInUSFormat(date);
    }

    public static String getActualDateTimeInIndianFormat(String date, HttpServletRequest request) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);

                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String getDateTimeInUSFormatForBio(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                dateObject = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                try {
                    dateObject = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(date);
                } catch (ParseException e1) {
                    throw new RuntimeException("" + e1.getMessage());
                }
            }
            Calendar cal = new GregorianCalendar();
            cal.setTime(dateObject);
            dateObject = cal.getTime();
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);

        } else {
            return "";
        }

    }

    public static Date getDateTimeFromUSDatetimeAddTimeZone(String date, HttpServletRequest request) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                return cal.getTime();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return null;
        }
    }
    
    public static String getActualDateTimeInIndianFormatWithoutAdd(String date) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = null;
                try {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                } catch (ParseException e) {
                    dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
                }

                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }



    public static String getStartDateOfMonth(String date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DataTypeUtility.getDateObject(date));
        int yearId = cal.get(Calendar.YEAR);                                           //2017
        int monthId = cal.get(Calendar.MONTH) + 1;
        String firstDateOfMonth = "";
        if (monthId < 10) {
            firstDateOfMonth = yearId + "-0" + monthId + "-01";
        } else {
            firstDateOfMonth = yearId + "-" + monthId + "-01";
        }

        return firstDateOfMonth;
    }

    public static String getEndDateOfMonth(String date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DataTypeUtility.getDateObject(date));
        int yearId = cal.get(Calendar.YEAR);
        int monthId = cal.get(Calendar.MONTH) + 1;
        String monthid_str = "0" + monthId;
        if (monthId > 9) {
            monthid_str = "" + monthId;
        }
        String firstDateOfMonth = yearId + "-" + monthid_str + "-" + 1;
        Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstDateOfMonth);
        cal = new GregorianCalendar();
        cal.setTime(firstDate);
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        String endDateOfMonth = yearId + "-" + monthid_str + "-" + maxDate;
        return endDateOfMonth;

    }
    
    public static long monthBetween(Date max, Date min) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(min);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(max);

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }
    public static String getStringArraytoStr(String[] strarray) {
        String str_data = "";
        if (strarray != null && strarray.length > 0)
            for (int counter = 0; counter < strarray.length; counter++) {
                String str = DataTypeUtility.stringValue(strarray[counter]);
                if (str.length() > 0) {
                    str_data = DataTypeUtility.getCommaValue(str_data, str);
                }

            }
        return str_data;
    }
    public static String getPreviousMonthDateUSFormat(String date) {

        Date date_obj = getDateObject(date);
        Calendar cal = new GregorianCalendar();
        cal.setTime(date_obj);
        cal.add(Calendar.MONTH, -1);
        return DataTypeUtility.getUSDateFormat(cal.getTime());
    }

    public static List<String> getSchedulerTimeList() {
        List<String> schedularList = new ArrayList();
        schedularList.add("00:15");
        schedularList.add("00:30");
        schedularList.add("00:45");
        schedularList.add("01:15");
        schedularList.add("01:30");
        schedularList.add("01:45");
        schedularList.add("02:15");
        schedularList.add("02:30");
        schedularList.add("02:45");
        schedularList.add("03:00");
        schedularList.add("03:15");
        schedularList.add("03:45");
        schedularList.add("04:30");
        schedularList.add("05:30");
        schedularList.add("06:30");
        schedularList.add("08:30");
        schedularList.add("09:30");
        schedularList.add("10:30");
        schedularList.add("11:30");
        schedularList.add("12:30");
        schedularList.add("13:30");
        schedularList.add("14:30");
        schedularList.add("15:30");
        schedularList.add("16:30");
        schedularList.add("18:15");
        schedularList.add("18:30");
        schedularList.add("20:30");
        schedularList.add("22:30");
        return schedularList;
    }

    public static String[] getDomainAndProtocol(String url) {
        String[] protocols = {"http://www.", "https://www.", "www.", "http://", "https://"};
        String protocol = "";
        for (int i = 0; i < protocols.length; i++) {
            if (url.startsWith(protocols[i])) {
                protocol = protocols[i];
                url = url.substring(protocol.length());
                break;
            }
        }
        if (url.indexOf('/') != -1) {
            url = url.substring(0, url.indexOf('/'));
        }
        if (protocol.length() == 0) {
            protocol = "https://";
        }
        return new String[]{url, protocol};
    }

    public static String getUSDateTimeFromIndianDateTime(String date,HttpServletRequest request) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
                } catch (Exception e) {
                    try {
                        dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                    } catch (Exception e1) {
                        dateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                    }
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                addTimeZoneInServer(cal, request);
                dateObject = cal.getTime();
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String modifiedKey(String key) {
        key = key.toLowerCase();
        key = key.replaceAll(" ", "_");
        key = key.replaceAll("\\(", "_");
        key = key.replaceAll("\\)", "_");
        key = key.replaceAll(",", "_");
        key = key.replaceAll("\\.", "_");
        key = key.replaceAll("\\&", "_");
        key = key.replaceAll("\\|", "_");
        return key;
    }

    public static String year_month_str(Date max, Date min) {

        if (max == null || min == null) {
            return "";
        }
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(max);
        long years = 0;
        long months = 0;
        long days = 0;
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(min);
        Calendar tmpdate = Calendar.getInstance();
        tmpdate.setTime(startDate.getTime());

        tmpdate.add(Calendar.YEAR, 1);
        while (tmpdate.compareTo(endDate) <= 0) {
            startDate.add(Calendar.YEAR, 1);
            tmpdate.add(Calendar.YEAR, 1);
            years++;
        }
        tmpdate.setTime(startDate.getTime());
        tmpdate.add(Calendar.MONTH, 1);
        while (tmpdate.compareTo(endDate) <= 0) {
            startDate.add(Calendar.MONTH, 1);
            tmpdate.add(Calendar.MONTH, 1);
            months++;
        }
        tmpdate.setTime(startDate.getTime());
        tmpdate.add(Calendar.DATE, 1);
        while (tmpdate.compareTo(endDate) <= 0) {
            startDate.add(Calendar.DATE, 1);
            tmpdate.add(Calendar.DATE, 1);
            days++;
        }

        String str = years + " Years and " + months + " Months";
        return str;
    }

    public static String getAplphbetCounter(Long counter) {
        char ch;
        ch = 'a';
        ch = (char) (ch + counter);
        return "" + ch;
    }

    // Function to convert decimal to Roman Numerals
    public static String getRomanNo(int number) {
        char c[] = new char[10001];
        int i = 0;

        // If number entered is not valid
        if (number <= 0) {
//            System.out.printf("Invalid number");
            return "0";
        }

        // TO convert decimal number to roman numerals
        while (number != 0) {
            // If base value of number is greater than 1000
            if (number >= 1000) {
                // Add 'M' number/1000 times after index i
                i = digit('M', number / 1000, i, c);
                number = number % 1000;
            } // If base value of number is greater than or
            // equal to 500
            else if (number >= 500) {
                // To add base symbol to the character array
                if (number < 900) {
                    // Add 'D' number/1000 times after index i
                    i = digit('D', number / 500, i, c);
                    number = number % 500;
                } // To handle subtractive notation in case of number
                // having digit as 9 and adding corresponding base
                // symbol
                else {
                    // Add C and M after index i/.
                    i = sub_digit('C', 'M', i, c);
                    number = number % 100;
                }
            } // If base value of number is greater than or equal to 100
            else if (number >= 100) {
                // To add base symbol to the character array
                if (number < 400) {
                    i = digit('C', number / 100, i, c);
                    number = number % 100;
                } // To handle subtractive notation in case of number
                // having digit as 4 and adding corresponding base
                // symbol
                else {
                    i = sub_digit('C', 'D', i, c);
                    number = number % 100;
                }
            } // If base value of number is greater than or equal to 50
            else if (number >= 50) {
                // To add base symbol to the character array
                if (number < 90) {
                    i = digit('L', number / 50, i, c);
                    number = number % 50;
                } // To handle subtractive notation in case of number
                // having digit as 9 and adding corresponding base
                // symbol
                else {
                    i = sub_digit('X', 'C', i, c);
                    number = number % 10;
                }
            } // If base value of number is greater than or equal to 10
            else if (number >= 10) {
                // To add base symbol to the character array
                if (number < 40) {
                    i = digit('X', number / 10, i, c);
                    number = number % 10;
                } // To handle subtractive notation in case of
                // number having digit as 4 and adding
                // corresponding base symbol
                else {
                    i = sub_digit('X', 'L', i, c);
                    number = number % 10;
                }
            } // If base value of number is greater than or equal to 5
            else if (number >= 5) {
                if (number < 9) {
                    i = digit('V', number / 5, i, c);
                    number = number % 5;
                } // To handle subtractive notation in case of number
                // having digit as 9 and adding corresponding base
                // symbol
                else {
                    i = sub_digit('I', 'X', i, c);
                    number = 0;
                }
            } // If base value of number is greater than or equal to 1
            else if (number >= 1) {
                if (number < 4) {
                    i = digit('I', number, i, c);
                    number = 0;
                } // To handle subtractive notation in case of
                // number having digit as 4 and adding corresponding
                // base symbol
                else {
                    i = sub_digit('I', 'V', i, c);
                    number = 0;
                }
            }
        }

        // Printing equivalent Roman Numeral
        String romanno = "";
        for (int j = 0; j < i; j++) {
            romanno = romanno + c[j];
        }
        return romanno;
    }

    static int digit(char ch, int n, int i, char[] c) {
        for (int j = 0; j < n; j++) {
            c[i++] = ch;
        }
        return i;
    }

    static int sub_digit(char num1, char num2, int i, char[] c) {
        c[i++] = num1;
        c[i++] = num2;
        return i;
    }

    public static String toSimpleWords(double number, String main_str, String sub_str) {
        String main_data = "";
        int amount_rs = (int) number;
        if (amount_rs > 0) {
            main_data = todoubleSimpleWords(amount_rs) + " " + main_str;
        }
        double amount_paise = number % 1d;
        String val = new DecimalFormat("#.##").format(amount_paise);
        amount_paise = DataTypeUtility.doubleZeroValue(val);
        Double d = amount_paise * 100d;
        String val1 = new DecimalFormat("#.##").format(d);
        d = DataTypeUtility.doubleZeroValue(val1);

        int amount_paise_i = DataTypeUtility.integerValue(d);
        if (amount_paise_i > 0) {
            String sub_main = todoubleSimpleWords(amount_paise_i) + " " + sub_str;
            main_data = main_data.length() > 0 ? main_data + " and " + sub_main : sub_main;
        }
        return main_data;

    }
    public static String getDateTimeInUSFormatForUIWithoutchange(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            Date dateObject = null;
            try {
                dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                try {
                    dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(date);
                } catch (ParseException e1) {
                    throw new RuntimeException("" + e1.getMessage());
                }
            }
            Calendar cal = new GregorianCalendar();
            cal.setTime(dateObject);
            dateObject = cal.getTime();
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateObject);

        } else {
            return "";
        }

    }
    
    public static Set<String> getSetFromCommaSeperatedStringBuilder(StringBuilder str){
        int start = 0;
        int end = 0;
        Set<String> resultSet = new HashSet<>();
        while(end < str.length()){
            while(end < str.length() && str.charAt(end) != ',' ){
                end++;
            }
            resultSet.add(str.substring(start, end));
            start = end + 1;
            end++;
        }

        return resultSet;

    }

    public static void getCommaValueFilter(StringBuilder itemstr, Long id) {
        if (id != null) {
            if (StringUtils.isBlank(itemstr.toString())) {
                itemstr.append("" + id);
            } else {
                itemstr.append("," + id);
            }
        }
    }

    public static void getCommaValueFilter(StringBuilder itemstr, String str) {
        if (str != null && str.length() > 0) {
            if (StringUtils.isBlank(itemstr.toString())) {
                itemstr.append( "'" + str + "'");
            } else {
                itemstr.append(", '" + str + "'");
            }
        }
    }

    public static Map<Long,Long> getOldNewApplicationid(HttpServletRequest request){
        Map<Long,Long> applicationmap =new HashMap<>();
        applicationmap.put( 131l, 51l); //frontoffice
        return applicationmap;
    }

    public static String getQueryString(String querystring) throws Exception {
        String query = "";
        if (querystring != null) {
            query = querystring.replaceAll("@-@", "&");
            query = query.replaceAll("__", "=");
            query = query.replaceAll("@--@", "/");
        }
        return query;
    }
    
    public static String AddTime(String old_time, String new_time) {
        long seconds = 0;
        if (DataTypeUtility.requireNonNullString(old_time)) {
            String[] hhmmss = old_time.split(":");
            seconds += Integer.valueOf(hhmmss[0]) * 60 * 60;
            seconds += Integer.valueOf(hhmmss[1]) * 60;
            if (hhmmss.length > 2)
                seconds += Integer.valueOf(hhmmss[2]);
        }
        if (DataTypeUtility.requireNonNullString(new_time)) {
            String[] hhmmss = new_time.split(":");
            seconds += Integer.valueOf(hhmmss[0]) * 60 * 60;
            seconds += Integer.valueOf(hhmmss[1]) * 60;
            if (hhmmss.length > 2)
                seconds += Integer.valueOf(hhmmss[2]);
        }
        long hh = seconds / 60 / 60;
        long mm = (seconds / 60) % 60;
        long ss = seconds % 60;
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }
    public static String getCommaValueFromSetForIn(Set<Long> set) {
        String itemstr = "-1";
        if (set != null && set.size() > 0) {
            for (Long str : set) {
                if (itemstr.length() == 0) {
                    itemstr = "" + str;
                } else {
                    itemstr = itemstr + ", " + str;
                }
            }

        }
        return itemstr;
    }
    public static HashSet<Long> getCRMColorStatusCode() {
        HashSet<Long> statusCode = new HashSet<>();
        statusCode.add(1l);
        statusCode.add(6l);
        statusCode.add(10l);
        statusCode.add(2l);
        statusCode.add(3l);
        statusCode.add(4l);
        statusCode.add(15l);
        return statusCode;
    }
    public static String getDateFormat(String date) {

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("dd").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }

    public static String getDiffTimeFromDate(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String s_hour = "" + diffHours;
        if (diffHours < 10 && diffHours >= 0) {
            s_hour = "0" + diffHours;
        }

        String s_minute = "" + diffMinutes;
        if (diffMinutes < 10 && diffMinutes >= 0) {
            s_minute = "0" + diffMinutes;
        }

        String dateformat = s_hour + ":" + s_minute + ":" + diffDays;
        return dateformat;
    }

    public static String getDayOfWeekOnly(String date) {
        if (date != null && date.length() > 5) {
            Date dateObject = getDateObject(date);
            String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObject);
            String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            int dayid = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayid == 0) {
                dayid = 7;
            }
            return DataTypeUtility.stringValue(day);
        } else {
            return null;
        }
    }

    public static String diffBetweenTwoTimeEndbyMinWithoutString(String time1, String time2) {
        String dateformat = "";
        if (time1 != null && time2 != null && time1.length() > 0 && time2.length() > 0) {
            SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

            Date d1 = null;
            Date d2 = null;

            try {
                d1 = format1.parse(time1);
            } catch (Exception e) {
                try {
                    d1 = format2.parse(time1);
                } catch (Exception e1) {
                }
            }

            try {
                d2 = format1.parse(time2);
            } catch (Exception e) {
                try {
                    d2 = format2.parse(time2);
                } catch (Exception e1) {
                }
            }

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            //// System.out.println(diff);
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;

            String s_hour = "" + diffHours;
            if (diffHours < 10 && diffHours >= 0) {
                s_hour = "0" + diffHours;
            }

            String s_minute = "" + diffMinutes;
            if (diffMinutes < 10 && diffMinutes >= 0) {
                s_minute = "0" + diffMinutes;
            }

            if (diffHours > 0) {
                dateformat = s_hour + ":" + s_minute;
            } else if (diffMinutes >= 0 && diffHours == 0) {
                dateformat = s_hour + ":" + s_minute;
            } else {
                dateformat = s_hour + ":" + s_minute;
            }

        }
        return dateformat;
    }
    public static String getCommaValueFromArryTypeStrng(String input) {
        return stringValue(input).replace("[", "").replace("]", "").replace("\"", "");
    }


    public static String getNumberWithZeroInt(Integer i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return i + "";
        }
    }
    
    public static LinkedList<String> getAbsentTypeName() {
        LinkedList<String> absent_type_list = new LinkedList<>();
        absent_type_list.add("AB");
        absent_type_list.add("CHT");
        absent_type_list.add("COMP");
        absent_type_list.add("MDL");
        absent_type_list.add("NS");
        absent_type_list.add("UFM");
        absent_type_list.add("LS");
        absent_type_list.add("NPr");
        absent_type_list.add("JL");
        absent_type_list.add("55");
        absent_type_list.add("56");
        absent_type_list.add("57");
        absent_type_list.add("58");
        absent_type_list.add("59");
        absent_type_list.add("60");
        absent_type_list.add("61");
        absent_type_list.add("62");
        absent_type_list.add("63");
        absent_type_list.add("64");
        absent_type_list.add("65");
        absent_type_list.add("66");
        absent_type_list.add("67");
        absent_type_list.add("68");
        absent_type_list.add("69");
        absent_type_list.add("70");
        absent_type_list.add("71");
        absent_type_list.add("72");
        absent_type_list.add("73");
        absent_type_list.add("74");
        absent_type_list.add("75");
        absent_type_list.add("76");
        absent_type_list.add("77");
        absent_type_list.add("78");
        absent_type_list.add("79");
        absent_type_list.add("80");


        return absent_type_list;

    }

    public static LinkedList<Long> getAbsentType() {
        LinkedList<Long> absent_type_list = new LinkedList<>();
        absent_type_list.add(1l);
        absent_type_list.add(21l);
        absent_type_list.add(22l);
        absent_type_list.add(23l);
        absent_type_list.add(24l);

        absent_type_list.add(51l);
        absent_type_list.add(52l);
        absent_type_list.add(53l);
        absent_type_list.add(54l);
        absent_type_list.add(55l);
        absent_type_list.add(56l);
        absent_type_list.add(57l);
        absent_type_list.add(58l);
        absent_type_list.add(59l);
        absent_type_list.add(60l);
        absent_type_list.add(61l);
        absent_type_list.add(62l);
        absent_type_list.add(63l);
        absent_type_list.add(64l);
        absent_type_list.add(65l);
        absent_type_list.add(66l);
        absent_type_list.add(67l);
        absent_type_list.add(68l);
        absent_type_list.add(69l);
        absent_type_list.add(70l);
        absent_type_list.add(71l);
        absent_type_list.add(72l);
        absent_type_list.add(73l);
        absent_type_list.add(74l);
        absent_type_list.add(75l);
        absent_type_list.add(76l);
        absent_type_list.add(77l);
        absent_type_list.add(78l);
        absent_type_list.add(79l);
        absent_type_list.add(80l);

        return absent_type_list;

    }
    public static boolean isabsentData(Long typeid) {
        boolean is_absent = false;
        if (typeid != null) {
            LinkedList<Long> absentlist = getAbsentType();
            if (absentlist.contains(typeid)) {
                is_absent = true;
            }

        }
        return is_absent;
    }
    public static String[] getDayMonthYear(Date dateObject, Boolean isfullname) {

        if (dateObject != null) {
            String[] days = null;
            String[] months = null;
            if (isfullname) {
                days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                months = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTTOBER", "NOVEMBER", "DECEMBER"};
            } else {
                days = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                months = new String[]{"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateObject);

            String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            String month = months[calendar.get(Calendar.MONTH)];
            int year = calendar.get(Calendar.YEAR);
            int date = calendar.get(Calendar.DATE);

            String[] array = {DataTypeUtility.stringValue(day), DataTypeUtility.stringValue(month), DataTypeUtility.stringValue(year), DataTypeUtility.stringValue(date)};
            return array;
        } else {
            return null;
        }
    }

    public static boolean getTwoDateBetweenWithEquals(Date date_maxDate, Date date_minDate, Date date_actualDate) {

        if ((date_actualDate.before(date_maxDate) || date_actualDate.equals(date_maxDate)) && (date_minDate.before(date_actualDate) || date_actualDate.equals(date_minDate))) {
            return true;
        } else {
            return false;
        }

    }

    public static String getDateInIndianFormatForReportFromUs(String date) {
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
//                addTimeZone(cal);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String getDateInIndianFormatForReport(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
//                addTimeZone(cal);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd MMM yyyy").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String getDateTimeInIndianFormatWithOutZoneWithAMPM(String date, HttpServletRequest request) {
        try {
            if (date != null && date.length() > 0) {
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date inputDate = inputFormat.parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(inputDate);
                inputDate = cal.getTime();
                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy | hh:mm a");
                String outputDateString = outputFormat.format(inputDate);
                return outputDateString;
            }

        } catch (Exception e) {

        }
        return "";
    }
    public static boolean getTwoDateBetweenWithEquals(String maxDate, String minDate, String actualDate) {

        Date date_maxDate = (Date) DataTypeUtility.getDateObject("" + maxDate);
        Date date_minDate = (Date) DataTypeUtility.getDateObject("" + minDate);
        Date date_actualDate = (Date) DataTypeUtility.getDateObject("" + actualDate);

        if ((date_actualDate.before(date_maxDate) || date_actualDate.equals(date_maxDate)) && (date_minDate.before(date_actualDate) || date_minDate.equals(date_actualDate))) {
            return true;
        } else {
            return false;
        }

    }

    public String getIconBasedColumnEntry(String primary, List<String> secondaryList) {
        String primaryStart="";
        String primaryEnd="";
        if(primary!=null && !StringUtils.isBlank(primary)){
            primaryStart = "<div class='d-flex align-items-center'>" +
                    "<div class='position-relative top-3'>" +
                    "<h6 class='mb-0 text-dark'>" + primary + "</h6>";
            primaryEnd = "</div>" + "</div>";
        }
        if (secondaryList != null && secondaryList.size() > 0) {
            String secondaryStart = "<ul class='common-bullet-list ps-0 mb-0 d-flex align-items-center flex-wrap lh-1-0'>";
            String secondaryEnd = "</ul>";
            int count = 0;
            for (String secondary : secondaryList) {
                if (StringUtils.isBlank(secondary)) continue;
                count++;
                secondaryStart += "<li class='text-secondary'>" +
                        "<span class='fs-12'>" + secondary + "</span>" +
                        "</li>";
            }
            if (count > 0) return primaryStart + secondaryStart + secondaryEnd + primaryEnd;
        }
        return primaryStart + primaryEnd;
    }


    public static String getPreviousDate(String date, Long days) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calReturn = Calendar.getInstance();
        calReturn.setTime(dateFormat.parse(date));
        calReturn.add(Calendar.DATE, DataTypeUtility.integerValue(days *= -1));

        return dateFormat.format(calReturn.getTime());
    }

    public static <T> T getModelFromMap(Map<String, Object> map, Class<T> classType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        Constructor<T> constructor = classType.getDeclaredConstructor();
        constructor.setAccessible(true);
        T obj = constructor.newInstance();
        Field[] declaredFields = classType.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String columnName = field.isAnnotationPresent(Column.class) ? field.getAnnotation(Column.class).name() : field.getName();
            Object value = map.get(columnName);
            if (value != null) {
                field.set(obj, typeConverter.convertIfNecessary(value, field.getType()));
            }
        }
        return obj;
    }
    public static String getDateTimeWithSecondInIndianFormatForReport(String date) {
        date = replaceTfromDateTime(date);
        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
//                addTimeZone(cal);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }

    }

    public static String getDateTimeInIndianFormatInSeconds(String date) {
        date = replaceTfromDateTime(date);

        if (date != null && date.length() > 0 && !date.equalsIgnoreCase("null")) {
            try {
                Date dateObject = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                Calendar cal = new GregorianCalendar();
                cal.setTime(dateObject);
                dateObject = cal.getTime();
                return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dateObject);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException("" + e.getMessage());
            }
        } else {
            return "";
        }
    }
    
    public static String diffBetweenTime(String dateStart, String dateStop) {
        dateStart = replaceTfromDateTime(dateStart);
        dateStop = replaceTfromDateTime(dateStop);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String dateformat = "";
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;

            String s_hour = "" + diffHours;
            if (diffHours < 10 && diffHours >= 0) {
                s_hour = "0" + diffHours;
            }

            String s_minute = "" + diffMinutes;
            if (diffMinutes < 10 && diffMinutes >= 0) {
                s_minute = "0" + diffMinutes;
            }

            dateformat = s_hour + ":" + s_minute;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateformat;
    }

   







}