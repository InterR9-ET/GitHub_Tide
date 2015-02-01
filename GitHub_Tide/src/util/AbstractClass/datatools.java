/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.AbstractClass;

import java.math.BigDecimal;
import java.sql.Connection;

/**
 *
 * @author yangzhen
 *
 */
public abstract class datatools {

    public abstract int string_parse_int(String _data_l);

    public abstract long string_parse_long(String _data_l);

    public abstract String systime_prase_string(String _type);

    public abstract String systime_prase_string_sjc(String _time);

    public abstract String sjctime_prase_string(String _sjc_time);

    public abstract String string_prase_md5(String _data_l);

    /*
     0  setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3 
     1  setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
     2  setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4         
     3  setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍        
     */
    public abstract BigDecimal string_parse_bigdecimal(String _data_l, int _legth, int type);

    public abstract String compare_time(String time1, String time2, String type_d_h_m_s_all);

    public abstract String date_calculate(String time, boolean is_fulltime, int _year, int _month, int _day);

    public abstract int date_get_monthlydays(int _year, int _month);

    public abstract boolean create_tablefile(Object _DBANME, String _TableName, String _URL);

}
