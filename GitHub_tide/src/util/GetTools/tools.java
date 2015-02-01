/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetTools;

import util.AbstractClass.*;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;

/**
 *
 * @author yangzhen public class excel extends files {
 */
public class tools extends datatools {

    private static util.GetFile.txt _txt = new util.GetFile.txt();

    @Override
    public int string_parse_int(String _data_l) {
        int _return_d = Integer.parseInt(_data_l);
        return _return_d;
    }

    @Override
    public BigDecimal string_parse_bigdecimal(String _data_l, int _legth, int type) {
        BigDecimal _return_d = new BigDecimal(_data_l);
        /*
         0  setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3 
         1  setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
         2  setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4         
         3  setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍        
         */
        if (type == 0) {
            _return_d = _return_d.setScale(_legth, BigDecimal.ROUND_DOWN);//    直接删除多余的小数位   
        } else if (type == 1) {
            _return_d = _return_d.setScale(_legth, BigDecimal.ROUND_HALF_UP);//  四舍五入(5进位)
        } else if (type == 2) {
            _return_d = _return_d.setScale(_legth, BigDecimal.ROUND_UP);//  进位处理
        } else if (type == 3) {
            _return_d = _return_d.setScale(_legth, BigDecimal.ROUND_HALF_DOWN);//  四舍五入(5去尾)
        }
        return _return_d;
    }

    @Override
    public long string_parse_long(String _data_l) {
        long _return_d = Long.parseLong(_data_l);
        return _return_d;
    }

    @Override
    public String systime_prase_string(String _type) {
        String _return_d = "";
        java.util.Date timeDate = null;
        timeDate = new Date();
       // java.sql.Time dateTime = new java.sql.Time(timeDate.getTime());
        if (_type == null || _type.toString().length() == 0 || _type.equals("年月日时分秒")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            _return_d = df.format(timeDate);
        } else if (_type.equals("年月日时分")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            _return_d = df.format(timeDate);
        } else if (_type.equals("年月日时")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");
            _return_d = df.format(timeDate);
        } else if (_type.equals("年月日")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            _return_d = df.format(timeDate);
        } else if (_type.equals("年月")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            _return_d = df.format(timeDate);
        } else if (_type.equals("年")) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            _return_d = df.format(timeDate);
        } else if (_type.equals("月")) {
            SimpleDateFormat df = new SimpleDateFormat("MM");
            _return_d = df.format(timeDate);
        } else if (_type.equals("日")) {
            SimpleDateFormat df = new SimpleDateFormat("dd");
            _return_d = df.format(timeDate);
        } else if (_type.equals("时")) {
            SimpleDateFormat df = new SimpleDateFormat("HH");
            _return_d = df.format(timeDate);
        } else if (_type.equals("分")) {
            SimpleDateFormat df = new SimpleDateFormat("mm");
            _return_d = df.format(timeDate);
        } else if (_type.equals("秒")) {
            SimpleDateFormat df = new SimpleDateFormat("ss");
            _return_d = df.format(timeDate);
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            _return_d = df.format(timeDate);
        }
        return _return_d;
    }

    public Timestamp string_prase_setTimestamp(String _time) {
        Timestamp str_return = null;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            str_return = new Timestamp(sdf.parse(_time).getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str_return;
    }

    public String systime_prase_string(String _time, String _type) {
        String _return_d = "";

        try {
            if (_type.toString().length() == 0 || _type.equals("年月日时分秒")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
            } else if (_type.equals("年月日时分")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
            } else if (_type.equals("年月日时")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
            } else if (_type.equals("年月日")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
            } else if (_type.equals("年月")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
            } else if (_type.equals("年")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
                String[] mes = _return_d.split("-");
                if (mes.length == 3) {
                    _return_d = mes[0].toString();
                }
            } else if (_type.equals("月")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
                String[] mes = _return_d.split("-");
                if (mes.length == 3) {
                    _return_d = mes[1].toString();
                }
            } else if (_type.equals("日")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dt = sdf.parse(_time);
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(dt);
                _return_d = sdf.format(rightNow.getTime()).toString();
                String[] mes = _return_d.split("-");
                if (mes.length == 3) {
                    _return_d = mes[2].toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return _return_d;
    }

    @Override
    public String systime_prase_string_sjc(String _time) {
        String _return_d = "";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long l = 0L;
            l = f.parse(_time).getTime() / 1000;
            _return_d = l + "";
        } catch (ParseException ex) {
            Logger.getLogger(tools.class.getName()).log(Level.SEVERE, null, ex);
        }
        return _return_d;
    }

    @Override
    public String sjctime_prase_string(String _sjc_time) {
        String _return_d = "";
        Long timestamp = Long.parseLong(_sjc_time);
        _return_d = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
        return _return_d;
    }

    @Override
    public String string_prase_md5(String _data_l) {
        String _return_d = "";
        StringBuffer sb = new StringBuffer(32);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(_data_l.getBytes("utf-8"));
            byte[] array = md.digest();
            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
            _return_d = sb.toString();
        } catch (Exception e) {
            System.out.println("Can not encode the string '" + _data_l + "' to MD5!" + e.getMessage().toString());
        }
        return _return_d;
    }

    @Override
    public String compare_time(String time1, String time2, String type_d_h_m_s_all) {
        String _return_d = "";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(time1);
            two = df.parse(time2);
            long _time1 = one.getTime();
            long _time2 = two.getTime();
            long diff;
            if (_time1 < _time2) {
                diff = _time2 - _time1;
            } else {
                diff = _time1 - _time2;
            }

            if (type_d_h_m_s_all.toUpperCase().endsWith("ALL")) {
                day = diff / (24 * 60 * 60 * 1000);
                hour = (diff / (60 * 60 * 1000) - day * 24);
                min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
                sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                _return_d = day + "-" + hour + "-" + min + "-" + sec;
            }

            if (type_d_h_m_s_all.toUpperCase().endsWith("D")) {
                _return_d = diff / (24 * 60 * 60 * 1000) + "";
            }

            if (type_d_h_m_s_all.toUpperCase().endsWith("H")) {
                _return_d = diff / (60 * 60 * 1000) + "";
            }

            if (type_d_h_m_s_all.toUpperCase().endsWith("M")) {
                _return_d = diff / (60 * 1000) + "";
            }

            if (type_d_h_m_s_all.toUpperCase().endsWith("S")) {
                _return_d = diff / 1000 + "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _return_d;
    }

    @Override
    public String date_calculate(String _time, boolean is_fulltime, int _year, int _month, int _day) {
        String _return_d = "";
        try {
            String geshi = "";
            if (is_fulltime) {
                geshi = "yyyy-MM-dd HH:mm:ss";
            } else {
                geshi = "yyyy-MM-dd";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(geshi);
            Date dt = sdf.parse(_time);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);

            if (_year != 0) {
                rightNow.add(Calendar.YEAR, _year);//当时日期 处理年    正数=加  负数=减
                _return_d = sdf.format(rightNow.getTime()).toString();
            }

            if (_month != 0) {
                rightNow.add(Calendar.MONTH, _month);//当时日期  处理月  正数=加  负数=减
                _return_d = sdf.format(rightNow.getTime()).toString();
            }

            if (_day != 0) {
                rightNow.add(Calendar.DAY_OF_YEAR, _day);//当时日期  处理天  正数=加  负数=减
                _return_d = sdf.format(rightNow.getTime()).toString();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return _return_d;
    }

    @Override
    public int date_get_monthlydays(int _year, int _month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, _year);
        a.set(Calendar.MONTH, _month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天  
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天  
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    @Override
    public boolean create_tablefile(Object _DBNAME, String _TableName, String _URL) {
        boolean _bs = false;
        boolean _bs_open = false;

        String DBdriver = "";
        String DBname = "";
        //匹配方法
        if (_DBNAME instanceof util.GetSql.csnms) {
            if (((util.GetSql.csnms) _DBNAME).load()) {
                if (((util.GetSql.csnms) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.csnms) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.csnms) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        } else if (_DBNAME instanceof util.GetSql.atm_mysql) {
            if (((util.GetSql.atm_mysql) _DBNAME).load()) {
                if (((util.GetSql.atm_mysql) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.atm_mysql) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.atm_mysql) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        } else if (_DBNAME instanceof util.GetSql.csnms_mysql) {
            if (((util.GetSql.csnms_mysql) _DBNAME).load()) {
                if (((util.GetSql.csnms_mysql) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.csnms_mysql) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.csnms_mysql) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        } else if (_DBNAME instanceof util.GetSql.donghuan_mysql) {
            if (((util.GetSql.donghuan_mysql) _DBNAME).load()) {
                if (((util.GetSql.donghuan_mysql) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.donghuan_mysql) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.donghuan_mysql) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        } else if (_DBNAME instanceof util.GetSql.ess) {
            if (((util.GetSql.ess) _DBNAME).load()) {
                if (((util.GetSql.ess) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.ess) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.ess) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        } else if (_DBNAME instanceof util.GetSql.yog) {
            if (((util.GetSql.yog) _DBNAME).load()) {
                if (((util.GetSql.yog) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.yog) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.yog) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        } else if (_DBNAME instanceof util.GetSql.zhizhen) {
            if (((util.GetSql.zhizhen) _DBNAME).load()) {
                if (((util.GetSql.zhizhen) _DBNAME).open()) {
                    DBdriver = ((util.GetSql.zhizhen) _DBNAME).get_db_driver();
                    DBname = ((util.GetSql.zhizhen) _DBNAME).get_db_name();
                    if (DBdriver.length() > 0 && DBname.length() > 0) {
                        _bs_open = true;
                    }
                }
            }
        }

        if (_bs_open) {
            String str_sql = "";
            //根据驱动匹配sql 
            if (DBdriver.equals("oracle.jdbc.driver.OracleDriver")) {
                str_sql = "select "
                        + "t.COLUMN_NAME ,"
                        + "t.DATA_TYPE ,"
                        + "t.DATA_PRECISION,"
                        + "t.DATA_LENGTH,"
                        + "t.DATA_DEFAULT,"
                        + "t.NULLABLE"
                        + " from user_tab_columns t where 1=1 "
                        + " and t.Table_Name='" + _TableName + "' ";
            } else if (DBdriver.equals("com.mysql.jdbc.Driver")) {

            } else if (DBdriver.equals("com.sybase.jdbc2.jdbc.SybDriver")) {

            }

            //获取数据
            List _list = new ArrayList();
            if (str_sql.length() > 0) {
                if (_DBNAME instanceof util.GetSql.csnms) {
                    _list = ((util.GetSql.csnms) _DBNAME).getdata(str_sql, null);
                } else if (_DBNAME instanceof util.GetSql.atm_mysql) {
                    _list = ((util.GetSql.atm_mysql) _DBNAME).getdata(str_sql, null);
                } else if (_DBNAME instanceof util.GetSql.csnms_mysql) {
                    _list = ((util.GetSql.csnms_mysql) _DBNAME).getdata(str_sql, null);
                } else if (_DBNAME instanceof util.GetSql.donghuan_mysql) {
                    _list = ((util.GetSql.donghuan_mysql) _DBNAME).getdata(str_sql, null);
                } else if (_DBNAME instanceof util.GetSql.ess) {
                    _list = ((util.GetSql.ess) _DBNAME).getdata(str_sql, null);
                } else if (_DBNAME instanceof util.GetSql.yog) {
                    _list = ((util.GetSql.yog) _DBNAME).getdata(str_sql, null);
                } else if (_DBNAME instanceof util.GetSql.zhizhen) {
                    _list = ((util.GetSql.zhizhen) _DBNAME).getdata(str_sql, null);
                }
            }

            if (_list.size() > 0) {
                //-------------------------产生文件--------------------------//
                StringBuffer _str = new StringBuffer();
                for (int i = 0, m = _list.size(); i < m; i++) {
                    HashMap map = (HashMap) _list.get(i);

                    if (i == 0) {
                        _str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
                        _str.append("<TABLE TBNAME=\"" + _TableName + "\" DBNAME=\"" + DBname + "\"  >").append("\r\n");
                    }

                    if (map.get("COLUMN_NAME") != null) {
                        _str.append("<COLUMN_NAME NAME=\"" + str_rel(map.get("COLUMN_NAME").toString()) + "\">").append("\r\n");
                    }

                    if (map.get("DATA_TYPE") != null) {
                        _str.append("<DATA_TYPE>").append(str_rel(map.get("DATA_TYPE").toString())).append("</DATA_TYPE>").append("\r\n");
                    }

                    if (map.get("DATA_PRECISION") != null) {
                        _str.append("<DATA_PRECISION>").append(str_rel(map.get("DATA_PRECISION").toString())).append("</DATA_PRECISION>").append("\r\n");
                    } else {
                        _str.append("<DATA_PRECISION>").append("</DATA_PRECISION>").append("\r\n");
                    }

                    if (map.get("DATA_LENGTH") != null) {
                        _str.append("<DATA_LENGTH>").append(str_rel(map.get("DATA_LENGTH").toString())).append("</DATA_LENGTH>").append("\r\n");
                    }

                    if (map.get("NULLABLE") != null) {
                        _str.append("<NULLABLE>").append(str_rel(map.get("NULLABLE").toString())).append("</NULLABLE>").append("\r\n");
                    }

                    if (map.get("COLUMN_NAME") != null) {
                        _str.append("</COLUMN_NAME>").append("\r\n");
                    }

                    if (i == m - 1) {
                        _str.append("</TABLE>").append("\r\n");
                    }
                }

                try {
                    String file_path = "";
                    if (_URL == null || _URL.toString().trim().length() == 0) {
                        File directory = new File("");
                        String default_url = directory.getCanonicalPath().toString();
                        default_url = default_url + "/conf/" + _TableName.toString() + ".xml";
                        file_path = default_url;
                    } else {
                        file_path = _URL;
                    }
                    boolean _bs2 = _txt.create_file(file_path);
                    if (_bs2) {
                        System.out.println(file_path);
                        if (_str.toString().length() > 0) {
                            _txt.write_file(file_path, _str.toString());
                        }
                        _bs = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //-------------------------产生文件--------------------------//
            }
        } else {
            System.out.println("数据库打开失败");
        }
        return _bs;
    }

    public boolean create_tablefile_class(Object _DBNAME, String _TableName, String _XMLURL, String _URL) {
        boolean _bs = false;
        boolean _bs_open = false;

        String DBdriver = "";
        String DBname = "";

        StringBuffer _str = new StringBuffer();
        //---------------------- ------解析文件-----------------------------------//
        try {
            String file_path_xml = "";
            if (_XMLURL == null || _XMLURL.toString().trim().length() == 0) {
                File directory = new File("");
                String default_url = directory.getCanonicalPath().toString();
                default_url = default_url + "/conf/" + _TableName.toString() + ".xml";
                file_path_xml = default_url;
            } else {
                file_path_xml = _XMLURL;
            }
            if (file_path_xml.length() > 0) {
                _str = return_javaclass(_DBNAME, file_path_xml);
            }
        } catch (Exception e) {
            System.out.println("解析文件异常：" + e.getMessage().toString());
        }
        //--------------------- -------解析文件-----------------------------------//
        //-----------------------------生成文件-----------------------------------// 
        try {
            String file_path = "";
            if (_URL == null || _URL.toString().trim().length() == 0) {
                File directory = new File("");
                String default_url = directory.getCanonicalPath().toString();
                default_url = default_url + "/conf/" + _TableName.toString() + ".java";
                file_path = default_url;
            } else {
                file_path = _URL;
            }
            boolean _bs2 = _txt.create_file(file_path);
            if (_bs2) {
                System.out.println(file_path);
                if (_str.toString().length() > 0) {
                    _txt.write_file(file_path, _str.toString());
                }
                _bs = true;
            }
        } catch (Exception e) {
            System.out.println("生成文件异常：" + e.getMessage().toString());
        }
        //-----------------------------生成文件---------------------------------// 

        return _bs;
    }

    private String str_rel(String str_con) {
        boolean _bs = false;
        if (str_con.indexOf(">") != -1) {
            _bs = true;
        } else if (str_con.indexOf("<") != -1) {
            _bs = true;
        } else if (str_con.indexOf("&") != -1) {
            _bs = true;
        } else if (str_con.indexOf("\"") != -1) {
            _bs = true;
        } else if (str_con.indexOf(",") != -1) {
            _bs = true;
        }

        if (_bs) {
            str_con = "<![CDATA[" + str_con + "]]>";
        }
        /*
         str_con = str_con.replace(">", "&gt;");
         str_con = str_con.replace("<", "&lt;");
         str_con = str_con.replace("&", "&amp;");
         str_con = str_con.replace("\"", "&quot;");
         str_con = str_con.replace("'", "&apos;");
         */
        return str_con;
    }

    private StringBuffer return_javaclass(Object _DBNAME, String _fileurl) {
        StringBuffer _str = new StringBuffer();

        System.out.println();
        

        return _str;
    }

}
