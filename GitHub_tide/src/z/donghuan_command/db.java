/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.donghuan_command;

import z.send.sms.*;
import z.send.alarm.*;
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

    private static util.GetTools.tools tools = new util.GetTools.tools();

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

    public static List load_command_csnms(util.GetSql.csnms _csnms) {
        List _list = new ArrayList();
        //加载数据库      
        String str_sql = "";
        //未发送的
        str_sql = "select  "
                + "c.ID as ID,"
                + "c.TRU_ID as TRU_ID,"
                + "c.COMMAND as COMMAND,"
                + "c.STATUS as STATUS "
                //+ "to_char(c.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss')  as STIME"
                + " from command_send  c where  c.STATUS='2' and "
                + " (to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')-to_date(to_char(c.CREATE_TIME2,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'))*24*60*60<300";

        if (str_sql.length() > 0) {
            List lst = new ArrayList();
            try {
                lst = _csnms.getdata(str_sql, null);

                for (int i = 0, m = lst.size(); i < m; i++) {
                    z.allClass.donghuancommand_mes _sms = new z.allClass.donghuancommand_mes();
                    HashMap map = (HashMap) lst.get(i);

                    if (map.get("ID") != null) {
                        _sms.ID = map.get("ID").toString();
                    } else {
                        continue;
                    }

                    if (map.get("TRU_ID") != null) {
                        _sms.TRU_ID = map.get("TRU_ID").toString();
                    } else {
                        continue;
                    }

                    if (map.get("COMMAND") != null) {
                        _sms.COMMAND = map.get("COMMAND").toString();
                    } else {
                        continue;
                    }

                    if (map.get("STATUS") != null) {
                        _sms.STATUS = map.get("STATUS").toString();
                    } else {
                        continue;
                    }

                    _list.add(_sms);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _list;
    }

    public static List load_command_csnms2(util.GetSql.csnms _csnms) {
        List _list = new ArrayList();
        //获取时间差大于60秒的数据   
        String str_sql = "";
        str_sql = " select  "
                + " c.ID as ID,"
                + " c.TRU_ID as TRU_ID,"
                + " c.COMMAND as COMMAND,"
                + " c.STATUS as STATUS,"
                + "  (to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')-to_date(to_char(c.CREATE_TIME2,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'))*24*60*60 as STIME"
                + " from command_send  c where  c.STATUS='3' and "
                + " (to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')-to_date(to_char(c.CREATE_TIME2,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss'))*24*60*60>90";

        if (str_sql.length() > 0) {
            List lst = new ArrayList();
            Object[] objs = new Object[]{};
            try {
                lst = _csnms.getdata(str_sql, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0, m = lst.size(); i < m; i++) {
                z.allClass.donghuancommand_mes _sms = new z.allClass.donghuancommand_mes();
                HashMap map = (HashMap) lst.get(i);

                if (map.get("ID") != null) {
                    _sms.ID = map.get("ID").toString();
                } else {
                    continue;
                }

                if (map.get("TRU_ID") != null) {
                    _sms.TRU_ID = map.get("TRU_ID").toString();
                } else {
                    continue;
                }

                if (map.get("COMMAND") != null) {
                    _sms.COMMAND = map.get("COMMAND").toString();
                } else {
                    continue;
                }

                if (map.get("STATUS") != null) {
                    _sms.STATUS = map.get("STATUS").toString();
                } else {
                    continue;
                }

                if (map.get("STIME") != null) {
                    _sms.STIME = map.get("STIME").toString();
                } else {
                    continue;
                }

                _list.add(_sms);
            }
        }
        return _list;
    }

    public static boolean insert_command_donghuan(util.GetSql.donghuan_mysql _donghuan_mysql, z.allClass.donghuancommand_mes _sms) {
        boolean bs = false;
        String sql = "insert into command_send ("
                + "rtu_id,"
                + "msg_id,"
                + "command"
                + ")values("
                + "'" + _sms.TRU_ID + "'" + ","
                + "'" + _sms.ID + "'" + ","
                + "'" + _sms.COMMAND + "'"
                + ")";
        int count = _donghuan_mysql.execute(sql, null);
        if (count > 0) {
            bs = true;
        }
        return bs;
    }

    public static boolean update_command_csnms(util.GetSql.csnms _csnms, z.allClass.donghuancommand_mes _sms) {
        boolean bs = false;
        String sql = "update  command_send   set "
                + " STATUS='" + _sms.STATUS + "' " + ","
                + " update_time=to_date('" + tools.systime_prase_string("") + "','yyyy-mm-dd hh24:mi:ss') " + ""
                // + " create_time=to_date('" + _tools.systime_prase_string("") + "','yyyy-mm-dd hh24:mi:ss') "
                + " where id=" + _sms.ID;
        int count = _csnms.execute(sql, null);
        if (count > 0) {
            bs = true;
        }
        return bs;
    }

    public static boolean update_powerequitlist(util.GetSql.csnms _csnms, z.allClass.donghuancommand_devicemes _DEVICE_MES) {
        boolean bs = false;
        String sql = "update powerequitlist  set "
                + " CURRENTVALUE='" + _DEVICE_MES.INIT_VALUE + "'" + ","
                + " updatetime=to_date('" + _DEVICE_MES.VALUE_UPDATE_TIME + "','yyyy-mm-dd hh24:mi:ss') "
                + " where "
                + " siteid= " + _DEVICE_MES.B_NEW_SITE_ID + " and "
                + " equipid= " + _DEVICE_MES.SIGNAL_ID + "  and "
                + " equiptypeid=" + _DEVICE_MES.B_NEW_DEVICE_ID;
        int count = _csnms.execute(sql, null);
        if (count > 0) {
            bs = true;
        }
        return bs;

    }

    public static boolean update_command(util.GetSql.csnms _csnms, String _command_id, String status) {
        boolean bs = false;
        String sql = "update  command_send s  set "
                + " s.STATUS='" + status + "',"
                + " s.update_time=sysdate "
                + " where 1=1 "
                + " and s.id=" + _command_id;
        int count = _csnms.execute(sql, null);
        if (count > 0) {
            bs = true;
        }
        return bs;

    }

    public static List get_data(org.apache.log4j.Logger log, util.GetSql.donghuan_mysql _donghuan, util.GetSql.csnms _csnms, z.allClass.donghuancommand_mes _sms) {
        List _list = new ArrayList();
        if (_sms.TRU_ID.length() > 0) {
            try {
                String str_sql = "select "
                        + " s.lsc_id as LSC_ID,"
                        + " s.signal_id as SIGNAL_ID,"
                        + " s.device_id as DEVICE_ID,"
                        + " s.station_id as STATION_ID,"
                        + " s.init_value as INIT_VALUE,"
                        + " s.value_desc as VALUE_DESC,"
                        + " s.value_update_time as VALUE_UPDATE_TIME,"
                        + " d.rtu_id  as RTU_ID"
                        + " from topo_signal s ,topo_device d  "
                        + " where  "
                        + " s.lsc_id=d.lsc_id and"
                        + " s.device_id=d.device_id  and"
                        + " d.rtu_id=" + _sms.TRU_ID.toString();
                List _list2 = _donghuan.getdata(str_sql, null);
                log.info("DH:" + str_sql);

                if (_list2.size() > 0) {
                    for (int i = 0, m = _list2.size(); i < m; i++) {
                        HashMap map = (HashMap) _list2.get(i);
                        z.allClass.donghuancommand_devicemes _DEVICE_MES = new z.allClass.donghuancommand_devicemes();
                        _DEVICE_MES.RTU_ID = _sms.TRU_ID;
                        _DEVICE_MES.CSNMS_COMMAND_ID = _sms.ID;
                        if (map.get("LSC_ID") != null) {
                            _DEVICE_MES.LSC_ID = map.get("LSC_ID").toString();
                        }

                        if (map.get("SIGNAL_ID") != null) {
                            _DEVICE_MES.SIGNAL_ID = map.get("SIGNAL_ID").toString();
                        }

                        if (map.get("DEVICE_ID") != null) {
                            _DEVICE_MES.DEVICE_ID = map.get("DEVICE_ID").toString();
                        }

                        if (map.get("STATION_ID") != null) {
                            _DEVICE_MES.STATION_ID = map.get("STATION_ID").toString();
                        }

                        if (map.get("INIT_VALUE") != null) {
                            _DEVICE_MES.INIT_VALUE = map.get("INIT_VALUE").toString();
                        }

                        if (map.get("VALUE_DESC") != null) {
                            _DEVICE_MES.VALUE_DESC = map.get("VALUE_DESC").toString();
                        }

                        if (map.get("VALUE_UPDATE_TIME") != null) {
                            String _time = map.get("VALUE_UPDATE_TIME").toString();
                            //System.out.println("time:"+_time);
                            _DEVICE_MES.VALUE_UPDATE_TIME = tools.systime_prase_string(_time, "年月日时分秒");
                        }

                        if (map.get("RTU_ID") != null) {
                            _DEVICE_MES.RTU_ID = map.get("RTU_ID").toString();
                        }

                        if (_DEVICE_MES.LSC_ID.length() > 0 && _DEVICE_MES.STATION_ID.length() > 0) {
                            //B_CITY_CODE
                            _DEVICE_MES.B_CITY_CODE = get_city_code(log, _donghuan, _DEVICE_MES.LSC_ID, _DEVICE_MES.STATION_ID);

                            //B_SITE_ID,B_RESITEID,B_EMS
                            String str_se = "select   "
                                    + " s.siteid as SITEID ,"
                                    + " s.ems  as EMS ,"
                                    + " s.resiteid as RESITEID "
                                    + " from  station_site  s "
                                    + " where "
                                    + " s.Lsc_Id=" + _DEVICE_MES.LSC_ID.toString() + " and"
                                    + " s.stationid=" + _DEVICE_MES.STATION_ID.toString();

                            List _data2 = _csnms.getdata(str_se, null);
                            if (_data2.size() > 0) {
                                HashMap map2 = (HashMap) _data2.get(0);
                                _DEVICE_MES.B_SITE_ID = map2.get("SITEID").toString();
                                _DEVICE_MES.B_RESITEID = map2.get("RESITEID").toString();
                                _DEVICE_MES.B_EMS = map2.get("EMS").toString();

                                //B_NEW_DEVICE_ID
                                //设备ID计算逻辑  （deviceIdStr*1000+ 机房&mes）&0801
                                if (_DEVICE_MES.DEVICE_ID.length() > 0 && _DEVICE_MES.B_RESITEID.length() > 0 && _DEVICE_MES.B_EMS.length() > 0) {
                                    Long l = Long.parseLong(_DEVICE_MES.DEVICE_ID.toString()) * 1000 + Long.parseLong(_DEVICE_MES.B_RESITEID + "" + _DEVICE_MES.B_EMS);
                                    String _deviceIdStr_new = l + "0801";
                                    _DEVICE_MES.B_NEW_DEVICE_ID = _deviceIdStr_new;

                                    _DEVICE_MES.B_NEW_SITE_ID = _DEVICE_MES.B_SITE_ID + "" + _DEVICE_MES.B_EMS;

                                    _list.add(_DEVICE_MES);

                                    StringBuffer _kk = new StringBuffer();
                                    _kk.append("");
                                    _kk.append("LSC_ID:" + _DEVICE_MES.LSC_ID.toString()).append("\r\n");
                                    _kk.append("SIGNAL_ID:" + _DEVICE_MES.SIGNAL_ID.toString()).append("\r\n");
                                    _kk.append("DEVICE_ID:" + _DEVICE_MES.DEVICE_ID.toString()).append("\r\n");
                                    _kk.append("STATION_ID:" + _DEVICE_MES.STATION_ID.toString()).append("\r\n");
                                    _kk.append("INIT_VALUE:" + _DEVICE_MES.INIT_VALUE.toString()).append("\r\n");
                                    _kk.append("VALUE_DESC:" + _DEVICE_MES.VALUE_DESC.toString()).append("\r\n");
                                    _kk.append("VALUE_UPDATE_TIME:" + _DEVICE_MES.VALUE_UPDATE_TIME.toString()).append("\r\n");
                                    _kk.append("RTU_ID:" + _DEVICE_MES.RTU_ID.toString()).append("\r\n");
                                    _kk.append("B_CITY_CODE:" + _DEVICE_MES.B_CITY_CODE.toString()).append("\r\n");
                                    _kk.append("B_SITE_ID:" + _DEVICE_MES.B_SITE_ID.toString()).append("\r\n");
                                    _kk.append("B_RESITEID:" + _DEVICE_MES.B_RESITEID.toString()).append("\r\n");
                                    _kk.append("B_EMS:" + _DEVICE_MES.B_EMS.toString()).append("\r\n");
                                    _kk.append("B_NEW_DEVICE_ID:" + _DEVICE_MES.B_NEW_DEVICE_ID.toString()).append("\r\n");
                                    _kk.append("B_NEW_SITE_ID:" + _DEVICE_MES.B_NEW_SITE_ID.toString()).append("\r\n");
                                    log.info(_kk.toString());
                                } else {
                                    StringBuffer _err = new StringBuffer();
                                    _err.append("Error: is null:DEVICE_ID,B_RESITEID,B_EMS");
                                    log.info(_err.toString());
                                }
                            } else {
                                StringBuffer _err = new StringBuffer();
                                _err.append("Error: is null:").append("\r\n");
                                _err.append("SQL:" + str_se).append("\r\n");
                                log.info(_err.toString());
                            }

                        } else {
                            StringBuffer _err = new StringBuffer();
                            _err.append("Error: is null:LSC_ID,STATION_ID");
                            log.info(_err.toString());
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _list;
    }

    public static String get_city_code(org.apache.log4j.Logger log, util.GetSql.donghuan_mysql _donghuan, String _lsc_id, String _station_id) {
        String str_mes = "";
        String str_sql = "select "
                + " region_code as CITY_CODE "
                + " from topo_station "
                + " where "
                + " lsc_id=" + _lsc_id + " and "
                + " station_id=" + _station_id;
        //System.out.println("#####" + str_sql + "\r\n");
        try {
            List _list = _donghuan.getdata(str_sql, null);
            if (_list.size() > 0) {
                HashMap map = (HashMap) _list.get(0);
                if (map.get("CITY_CODE") != null) {
                    str_mes = map.get("CITY_CODE").toString();
                }
            }
        } catch (Exception ex) {
            StringBuffer _error = new StringBuffer();
            _error.append("Error：出现异常").append("\r\n");
            _error.append("SQL：").append(str_sql);
            log.info(_error.toString());
        }
        return str_mes;
    }

}
