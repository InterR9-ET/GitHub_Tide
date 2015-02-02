/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.send_sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author yangzhen
 * @author 功能：数据库加载
 * @author 描述：
 * @author 数据库加载判断
 *
 *
 */
public class db {

    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            } else {
                log.info("csnms打开出错");
            }
        } else {
            log.info("csnms加载出错");
        }
        return _bs;
    }

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.donghuan_mysql _donghuan_mysql) {
        boolean _bs = false;
        if (_donghuan_mysql.load()) {
            if (_donghuan_mysql.open()) {
                _bs = true;
            } else {
                log.info("donghuan_mysql打开出错");
            }
        } else {
            log.info("donghuan_mysql加载出错");
        }
        return _bs;
    }

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.donghuan_mysql _donghuan_mysql) {
        boolean _bs = false;
        if (ini(log, _csnms)) {
            if (ini(log, _donghuan_mysql)) {
                _bs = true;
            }
        }
        return _bs;
    }

    public static List loaddata(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        // --------------------------------------临时数据补丁：-----------------------------------------------//
        // 20141205 crertetime，hasdh 由于界面映射，未能有默认值进去

        String bug_str_sql = "select s.id as ID from sendmessage s where (s.createtime is NULL or s.HASDH is null) and  s.id >44797";
        List bug_list = _csnms.getdata(bug_str_sql, null);
        if (bug_list.size() > 0) {
            for (int i = 0, m = bug_list.size(); i < m; i++) {
                HashMap map = (HashMap) bug_list.get(i);
                if (map.get("ID") != null) {
                    String _msm_id = map.get("ID").toString();
                    if (_msm_id.length() > 0) {
                        String bug_sql_up = "update  sendmessage s  set s.createtime=sysdate,s.HASDH='-1' where s.id=" + _msm_id;
                        _csnms.execute(bug_sql_up, null);
                    }
                }
            }
        }
        // ------------------------------------------------------------------------------------//

        List _msm_list = new ArrayList();
        String str_sql = "";

        str_sql = "select s.id as ID,s.phone as PHONE,s.description as DEC,s.cityid as CITYID,s.LINKNAME as LINKNAME ,s.ALARM_ID as ALARM_ID "
                + " from sendmessage s where s.serialnum is NULL and (s.HASDH='-1' or s.HASDH is null) "
                + " and (to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') - to_date(to_char(s.createtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') )*24*60*60<60";

        List lst = new ArrayList();
        Object[] objs = new Object[]{};
        try {
            lst = _csnms.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0, m = lst.size(); i < m; i++) {
            z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
            HashMap map = (HashMap) lst.get(i);
            StringBuffer log_txt = new StringBuffer("");
            try {

                boolean _bs_1 = true;

                if (map.get("ID") != null) {
                    _sms.STR_ID = map.get("ID").toString();
                    log_txt.append("加载需要发送的数据[")
                            .append((i + 1) + "/" + m).append("]\r\n");
                    log_txt.append("ID:").append(_sms.STR_ID)
                            .append("\r\n");
                } else {
                    log_txt.append("加载需要发送的数据[")
                            .append((i + 1) + "/" + m).append("]\r\n");
                    log_txt.append("ID:").append("is NULL")
                            .append("\r\n");
                    _bs_1 = false;
                }
                if (map.get("PHONE") != null) {
                    _sms.TEL = map.get("PHONE").toString();
                    log_txt.append("PHONE:").append(_sms.TEL)
                            .append("\r\n");
                } else {
                    log_txt.append("PHONE:").append("is NULL")
                            .append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("LINKNAME") != null) {
                    _sms.USERNAME = map.get("LINKNAME").toString();
                    log_txt.append("LINKNAME:").append(_sms.USERNAME)
                            .append("\r\n");
                } else {
                    log_txt.append("LINKNAME:").append("is NULL")
                            .append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("CITYID") != null) {
                    _sms.CITYID = map.get("CITYID").toString();
                    log_txt.append("CITYID:").append(_sms.CITYID)
                            .append("\r\n");
                } else {
                    log_txt.append("CITYID:").append("is NULL")
                            .append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("DEC") != null) {
                    String str_Content = map.get("DEC").toString();
                    str_Content = str_Content.replace("&", "")
                            .replace("<", "").replace(">", "");
                    _sms.CONTENT = str_Content;
                    log_txt.append("DEC:").append(_sms.CONTENT)
                            .append("\r\n");
                } else {
                    log_txt.append("DEC:").append("is NULL")
                            .append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("ALARM_ID") != null) {
                    _sms.ALARM_ID = map.get("ALARM_ID").toString();
                    log_txt.append("ALARMID:").append(_sms.ALARM_ID)
                            .append("\r\n");
                }

                if (_bs_1) {
                    _msm_list.add(_sms);
                    log.info(log_txt);
                } else {
                    log.info(log_txt);
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.info("ID#" + _sms.STR_ID + "#出现异常："
                        + e.getMessage().toString());
            }
        }

        log.info("加载所有短信:" + _msm_list.size());
        return _msm_list;
    }

    public static List loaddataupdate(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        List _list = new ArrayList();
        String str_sql = "";
        String _time = tools.systime_prase_string("");
        str_sql = "select "
                + " s.id as ID,"
                + " s.phone as PHONE,"
                + " s.description as DEC,"
                + " s.cityid as CITYID,"
                + " s.LINKNAME as LINKNAME,"
                + " to_char(s.sendtime,'yyyy-mm-dd hh24:mi:ss')  as STIME"
                + " from sendmessage s "
                + " where "
                + " s.HASDH='1' "
                + " and (to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') - to_date(to_char(s.sendtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') )*24*60*60 <600" // 10分钟
                + " and (to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') - to_date(to_char(s.sendtime,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') )*24*60*60 >30";// 30秒
        List lst = new ArrayList();
        try {
            lst = _csnms.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0, m = lst.size(); i < m; i++) {
            z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
            HashMap map = (HashMap) lst.get(i);
            StringBuffer log_txt = new StringBuffer("");

            try {

                boolean _bs_1 = true;

                if (map.get("ID") != null) {
                    _sms.STR_ID = map.get("ID").toString();
                    log_txt.append("加载需要更新的数据[").append((i + 1) + "/" + m).append("]\r\n");
                    log_txt.append("ID:").append(_sms.STR_ID).append("\r\n");
                } else {
                    log_txt.append("加载需要更新的数据[").append((i + 1) + "/" + m).append("]\r\n");
                    log_txt.append("ID:").append("is NULL").append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("PHONE") != null) {
                    _sms.TEL = map.get("PHONE").toString();
                    log_txt.append("PHONE:").append(_sms.TEL).append("\r\n");
                } else {
                    log_txt.append("PHONE:").append("is NULL").append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("LINKNAME") != null) {
                    _sms.USERNAME = map.get("LINKNAME").toString();
                    log_txt.append("LINKNAME:").append(_sms.USERNAME).append("\r\n");
                } else {
                    log_txt.append("LINKNAME:").append("is NULL").append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("CITYID") != null) {
                    _sms.CITYID = map.get("CITYID").toString();
                    log_txt.append("CITYID:").append(_sms.CITYID).append("\r\n");
                } else {
                    log_txt.append("CITYID:").append("is NULL").append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("DEC") != null) {
                    String str_Content = map.get("DEC").toString();
                    str_Content = str_Content.replace("&", "").replace("<", "").replace(">", "");
                    _sms.CONTENT = str_Content;
                    log_txt.append("DEC:").append(_sms.CONTENT).append("\r\n");
                } else {
                    log_txt.append("DEC:").append("is NULL").append("\r\n");
                    _bs_1 = false;
                }

                if (map.get("STIME") != null) {
                    _sms.STIME = map.get("STIME").toString();
                    log_txt.append("STIME:").append(_sms.STIME).append("\r\n");
                } else {
                    log_txt.append("STIME:").append("is NULL").append("\r\n");
                    _bs_1 = false;
                }

                if (_bs_1) {
                    _list.add(_sms);
                    System.out.println(log_txt);
                } else {
                    System.out.println(log_txt);
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.info("ID#" + _sms.STR_ID + "#出现异常：" + e.getMessage().toString());
            }
        }
        log.info("加载所有短信:" + _list.size());
        return _list;

    }

    public static boolean update_description(util.GetSql.csnms _csnms, String mes, String smsid) {
        boolean bs = false;
        String sql = "update  sendmessage  s  set s.description='" + mes + "' where  s.id=" + smsid;
        int count = _csnms.execute(sql, null);
        if (count > 0) {
            bs = true;
        }
        return bs;
    }

    public static boolean update_status_zongdiao(util.GetSql.csnms _csnms, String SerialNum, String smsid, boolean status) {
        boolean bs = false;
        String sql = "";
        if (status) {
            sql = "update sendmessage set "
                    + "SERIALNUM='" + SerialNum + "',"
                    + "dealflag='发送成功" + tools.systime_prase_string("") + "',"
                    + "SENDTIME=to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),"
                    + "HASDH='9'  "
                    + "where 1=1  "
                    + "and id=" + smsid;
        } else {
            sql = "update sendmessage set "
                    + "SERIALNUM='-1',"
                    + "dealflag='发送失败" + tools.systime_prase_string("") + "', "
                    + "SENDTIME=to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ,"
                    + "HASDH='-9' "
                    + "where 1=1 "
                    + "and id=" + smsid;
        }
        if (sql.length() > 0) {
            int count = _csnms.execute(sql, null);
            if (count > 0) {
                bs = true;
            }
        }
        return bs;
    }

    public static boolean update_in_status_donghuan(util.GetSql.csnms _csnms, String STATUS, String ID) {
        boolean bs = false;
        String sql = "";
        sql = "update  sendmessage  set  HASDH='"
                + STATUS
                + "', SENDTIME=to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where id="
                + ID;

        if (sql.length() > 0) {
            int count = _csnms.execute(sql, null);
            if (count > 0) {
                bs = true;
            }
        }
        return bs;
    }

    public static boolean update_status_donghuan(util.GetSql.csnms _csnms, String SerialNum, String smsid, boolean status) {
        boolean bs = false;
        String sql = "";
        if (status) {
            sql = "update sendmessage set "
                    + "SERIALNUM='DH" + SerialNum + "',"
                    + "dealflag='发送成功" + tools.systime_prase_string("") + "',"
                    + "SENDTIME=to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'),"
                    + "HASDH='9'  "
                    + "where 1=1  "
                    + "and id=" + smsid;
        } else {
            sql = "update sendmessage set "
                    + "SERIALNUM='-1',"
                    + "dealflag='发送失败" + tools.systime_prase_string("") + "', "
                    + "SENDTIME=to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') ,"
                    + "HASDH='-9' "
                    + "where 1=1 "
                    + "and id=" + smsid;
        }
        if (sql.length() > 0) {
            int count = _csnms.execute(sql, null);
            if (count > 0) {
                bs = true;
            }
        }
        return bs;
    }

    public static boolean insert_sms_donghuan(util.GetSql.donghuan_mysql _donghuan_mysql, z.allClass.sendsms_sendstr _sms) {
        boolean bs = false;
        String sql = "insert into command_send_csnms ("
                + "rtuno,"
                + "msg_id,"
                + "user_name,"
                + "command"
                + ")values("
                + "'" + _sms.TEL.toString() + "'" + ","
                + "'" + _sms.STR_ID.toString() + "'" + ","
                + "'" + _sms.USERNAME.toString() + "'" + ","
                + "'" + _sms.CONTENT + "'"
                + ")";
        int count = _donghuan_mysql.execute(sql, null);
        if (count > 0) {
            bs = true;
        }
        return bs;
    }

    public static boolean update_alarm_info2(util.GetSql.csnms _csnms, z.allClass.sendsms_sendstr _sms, boolean _issucess) {
        boolean bs = false;
        if (_sms.ALARM_ID.toString().trim().length() > 0) {
            String status = "";
            if (_issucess) {
                status = "sendmessage_sucess";
            } else {
                status = "sendmessage_error";
            }
            if (status.length() > 0) {
                String str_sql = "update  alarm  a   set a.info2='" + status + "'  where  a.id=" + _sms.ALARM_ID.toString();
                int n = _csnms.execute(str_sql, null);
                if (n == 0) {
                    /*
                     * for (int i = 1; i <= 12; i++) { String str_sql2 =
                     * "update  historyalarm_" + i + "  a   set a.info2='" +
                     * status + "'  where  a.id=" + _sms.ALARM_ID.toString();
                     * int n2 = _csnms.execute(str_sql2, null); if (n2 > 0) {
                     * break; } }
                     */
                } else {
                    // 更新成功
                    bs = true;
                }
            }
        }
        return bs;
    }

    public static List loaddonghuan_send(util.GetSql.donghuan_mysql _donghuan_mysql, String ID) {
        List _list = new ArrayList();
        String str_sql = "select  status as STATUS from command_send_csnms  where msg_id='" + ID + "'";
        _list = _donghuan_mysql.getdata(str_sql, null);
        return _list;
    }

    public static List loaddonghuan_his(util.GetSql.donghuan_mysql _donghuan_mysql, String ID) {
        List _list = new ArrayList();
        String str_sql = "select  status as STATUS from command_send_csnms_history  where msg_id='" + ID + "'";
        _list = _donghuan_mysql.getdata(str_sql, null);
        return _list;
    }

    public static int get_max_id(util.GetSql.csnms _csnms) {
        int _max_id = 0;
        String sql1 = "select  max(id) as ID from SENDMESSAGE s  ";

        List _list1 = new ArrayList();
        try {
            _list1 = _csnms.getdata(sql1, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String _id = "";
        if (_list1.size() > 0) {
            HashMap map = (HashMap) _list1.get(0);
            _id = map.get("ID").toString();
            _max_id = Integer.parseInt(_id) + 1;
        }
        return _max_id;
    }

}