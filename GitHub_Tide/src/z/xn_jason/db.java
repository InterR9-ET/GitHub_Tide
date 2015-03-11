package z.xn_jason;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import util.GetSocket.socketclient;
import static z.xn_jason.fun.remote_ip;
import static z.xn_jason.fun.remote_port;
import z.xn_jason.strclass._IPCPU;
import z.xn_jason.strclass._IPMEM;
import z.xn_jason.strclass._PERFBOUNDARY_COUNT;
import z.xn_jason.strclass._idcperfermance_tongyong;

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
    public static Hashtable keyHash = new Hashtable();
    public static Hashtable keyHash2 = new Hashtable();
    public static Hashtable keyHash3 = new Hashtable();
    public static Hashtable keyHash4 = new Hashtable();
    public static Hashtable keyHash5 = new Hashtable();
    public static Hashtable keyHash6 = new Hashtable();
    public static Hashtable keyHash7 = new Hashtable();
    public static Hashtable keyHash8 = new Hashtable();
    public static String str_mes = "";
    private static Socket remote_socket = null;
    private static Logger log_alarm = Logger.getLogger(socketclient.class);
    private static String CONFIG_PATH = "conf/ftp.config";

//--------------------------加载数据库------------------------------------------
    public static boolean ini(util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            }
        }
        return _bs;
    }
    
    public static boolean ini() {
        try {

            if (remote_socket == null || remote_socket.isClosed()) {
                if (loadconfig()) {
                    remote_socket = new Socket(remote_ip, Integer.parseInt(remote_port));
                    log_alarm.info("Connection access:" + remote_ip + ":" + remote_port + ".");
                } else {
                    log_alarm.info("配置文件读取错误....");
                }
            }

            try {
                remote_socket.sendUrgentData(0xFF);
            } catch (Exception ex) {
                remote_socket = new Socket(remote_ip, Integer.parseInt(remote_port));
                System.out.println("Connection access:" + remote_ip + ":" + remote_port + ".");
                log_alarm.info("对端断开,重连");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//--------------------------------------------------------------------
    public static String return_node(String _ip, util.GetSql.csnms _csnms) {
        String node_id = "";
        String str_sql_se = "select  n.node_id  as NODE_ID   from  node  n  where  n.inter_net_addr='" + _ip + "'";

        List list_data = new ArrayList();
        try {
            list_data = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_data.size(); i++) {
            HashMap map = (HashMap) list_data.get(i);
            node_id = map.get("NODE_ID").toString();
        }
        return node_id;
    }

    public static boolean return_node_s_ishas(String _table, String _nodeip, String str_title, util.GetSql.csnms _csnms) {
        boolean _bs = false;

        if (_nodeip.length() > 0) {
            String str_sql_se = "select  n.nodeid  as NODE_ID   from  " + _table + "  n  where   n.NODEIP='" + _nodeip + "'  and n.perfid='" + str_title + "'";

            List list_data = new ArrayList();
            try {
                list_data = _csnms.getdata(str_sql_se, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (list_data != null && list_data.size() > 0) {
                _bs = true;
            }
        }
        return _bs;
    }

    public static List get_IPCPUs(String str_where, util.GetSql.csnms _csnms) {
        List _datas = new ArrayList();
        StringBuffer str_sql = new StringBuffer("");
        str_sql.append(" select  ");
        str_sql.append("CPUID").append(",");
        str_sql.append("TYPE").append(",");
        str_sql.append("NAME").append(",");
        str_sql.append("USED").append(",");
        str_sql.append("PERCENT").append(",");
        str_sql.append("NODEID");
        str_sql.append(" from  IPCPU ");
        if (str_where.length() > 0) {
            str_sql.append(" where " + str_where);
        }

        List lst = null;
        try {
            lst = _csnms.getdata(str_sql.toString(), null);
        } catch (Exception e) {
            System.out.println(str_sql.toString());
        }

        for (int i = 0; i < lst.size(); i++) {
            _IPCPU _datas_l = new _IPCPU();
            StringBuffer str_xml = new StringBuffer("");
            HashMap map = (HashMap) lst.get(i);
            try {
                _datas_l.CPUID = map.get("CPUID").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.TYPE = map.get("TYPE").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.NAME = map.get("NAME").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.USED = map.get("USED").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.PERCENT = map.get("PERCENT").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.NODEID = map.get("NODEID").toString();
            } catch (Exception ex) {
            } finally {
            }
            _datas.add(_datas_l);
        }
        return _datas;
    }

    public static void tb_in_up_mes(String type_in_up, List _data, util.GetSql.csnms _csnms) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                strclass._IPCPU _datal = new strclass._IPCPU();
                _datal = (strclass._IPCPU) _data.get(i);
                count_s = count_s + 1;

                if (type_in_up.equals("in")) {
                    String str_sql = "insert into IPCPU ("
                            + "CPUID,"
                            + "TYPE,"
                            + "NAME,"
                            + "USED,"
                            + "PERCENT,"
                            + "NODEID"
                            + ") values ("
                            + Long.parseLong(_datal.CPUID) + ","
                            + Long.parseLong(_datal.TYPE) + ","
                            + "'" + _datal.NAME + "',"
                            + Long.parseLong(_datal.USED) + ","
                            + Double.valueOf(_datal.PERCENT) + ","
                            + Long.parseLong(_datal.NODEID) + ""
                            + ")";
                    _csnms.execute(str_sql, null);

                } else if (type_in_up.equals("up")) {

                    String str_sql = "update IPCPU  set  "
                            + " CPUID=" + Long.parseLong(_datal.CPUID) + ","
                            + " TYPE=" + Long.parseLong(_datal.TYPE) + ","
                            + " NAME='" + _datal.NAME + "',"
                            + " USED=" + Long.parseLong(_datal.TYPE) + ","
                            + " PERCENT=" + Double.valueOf(_datal.PERCENT) + ","
                            + " NODEID=" + Long.parseLong(_datal.NODEID) + ""
                            + " where  NODEID=" + Long.parseLong(_datal.NODEID) + "";
                    _csnms.execute(str_sql, null);

                } else if (type_in_up.equals("del")) {
                    String str_sql = "delete  from IPCPU where  NODEID=" + Long.parseLong(_datal.NODEID) + "";
                    _csnms.execute(str_sql, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }

    public static void tb_in_up_mes_de(String type_in_up, List _data, util.GetSql.csnms _csnms,util.GetTools.tools _tools) {
        //构造预处理
        try {
            int count_s = 0;
            List _list_obj_in = new ArrayList();
            List _list_obj_up = new ArrayList();
            List _list_obj_up2 = new ArrayList();
            List _list_obj_up3 = new ArrayList();
            List _list_obj_up4 = new ArrayList();
            List _list_obj_up5 = new ArrayList();
            List _list_obj_up6 = new ArrayList();
            List _list_obj_up7 = new ArrayList();
            List _list_obj_up8 = new ArrayList();
            List _list_obj_up9 = new ArrayList();
            List _list_obj_up10 = new ArrayList();
            List _list_obj_up11 = new ArrayList();

            if (_data.size() > 0) {
                for (int i = 0, m = _data.size(); i < m; i++) {
                    strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                    _datal = (strclass._tb_shuguangDS_mes_de) _data.get(i);
                    count_s = count_s + 1;

                    //-------------处理时间---------------//
                    String _GETTIME = _datal.GETTIME;
                    SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date _date1 = new Date();
                    _date1 = sdf_temp.parse(_GETTIME);
                    String str = sdf_temp.format(_date1);
                    //------------------------------------//
                    if (type_in_up.equals("in")) {
                        Object[] objs = new Object[]{
                            _tools.string_prase_setTimestamp(str),
                            _datal.PERFID,
                            _datal.PERFERMANCE,
                            _datal.NODEIP,
                            _datal.TYPE,
                            _datal.INFO,
                            Long.parseLong(_datal.NODEID),
                            _datal.IFIDX
                        };
                        _list_obj_in.add(objs);
                    } else if (type_in_up.indexOf("up") != -1) {

                        Object[] objs = new Object[]{
                            _tools.string_prase_setTimestamp(str),
                            _datal.PERFERMANCE,
                            _datal.NODEIP,
                            _datal.TYPE,
                            _datal.IFIDX,
                            Long.parseLong(_datal.NODEID),
                            _datal.PERFID,
                            _datal.INFO
                        };

                        if (type_in_up.equals("up")) {
                            _list_obj_up.add(objs);
                        } else if (type_in_up.equals("up2")) {
                            _list_obj_up2.add(objs);
                        } else if (type_in_up.equals("up3")) {
                            _list_obj_up3.add(objs);
                        } else if (type_in_up.equals("up4")) {
                            _list_obj_up4.add(objs);
                        } else if (type_in_up.equals("up5")) {
                            _list_obj_up5.add(objs);
                        } else if (type_in_up.equals("up6")) {
                            _list_obj_up6.add(objs);
                        } else if (type_in_up.equals("up7")) {
                            _list_obj_up7.add(objs);
                        } else if (type_in_up.equals("up8")) {
                            _list_obj_up8.add(objs);
                        } else if (type_in_up.equals("up9")) {
                            _list_obj_up9.add(objs);
                        } else if (type_in_up.equals("up10")) {
                            _list_obj_up10.add(objs);
                        } else if (type_in_up.equals("up11")) {
                            _list_obj_up11.add(objs);
                        }
                    }
                }

                if (_list_obj_in.size() > 0) {
                    String str_sql = "insert into  DSChannel_mes_de ("
                            + "GETTIME,"
                            + "PERFID,"
                            + "PERFERMANCE,"
                            + "NODEIP,"
                            + "TYPE,"
                            + "INFO,"
                            + "NODEID,"
                            + "IFIDX"
                            + ") values (?,?,?,?,?,?,?,?)";
                    _csnms.execute_listobj(str_sql, _list_obj_in);
                    _list_obj_in.clear();;
                }

                String str_sqlup = "update DSChannel_mes  set  "
                        + " GETTIME=?,"
                        + " PERFERMANCE=? , "
                        + " NODEIP=? , "
                        + " TYPE=? , "
                        + " IFIDX=?  "
                        + " where  "
                        + " NODEID=? "
                        + " and    perfid=? "
                        + " and    info=? ";

                if (_list_obj_up.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSChannel'";
                    _csnms.execute_listobj(str_sql, _list_obj_up);
                    _list_obj_up.clear();
                }

                if (_list_obj_up2.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSController'";
                    _csnms.execute_listobj(str_sql, _list_obj_up2);
                    _list_obj_up2.clear();
                }

                if (_list_obj_up3.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSDisk'";
                    _csnms.execute_listobj(str_sql, _list_obj_up3);
                    _list_obj_up3.clear();
                }

                if (_list_obj_up4.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSEnclosure'";
                    _csnms.execute_listobj(str_sql, _list_obj_up4);
                    _list_obj_up4.clear();
                }

                if (_list_obj_up5.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSEsm'";
                    _csnms.execute_listobj(str_sql, _list_obj_up5);
                    _list_obj_up5.clear();
                }

                if (_list_obj_up6.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSFans'";
                    _csnms.execute_listobj(str_sql, _list_obj_up6);
                    _list_obj_up6.clear();
                }
                if (_list_obj_up7.size() > 0) {
                    String str_sql = str_sqlup + " and  type='DSPowerFan'";
                    _csnms.execute_listobj(str_sql, _list_obj_up7);
                    _list_obj_up7.clear();
                }
                if (_list_obj_up8.equals("up8")) {
                    String str_sql = str_sqlup + " and  type='DSPowerSupplies'";
                    _csnms.execute_listobj(str_sql, _list_obj_up8);
                    _list_obj_up8.clear();
                }
                if (_list_obj_up9.equals("up9")) {
                    String str_sql = str_sqlup + " and  type='DSSFPs'";
                    _csnms.execute_listobj(str_sql, _list_obj_up9);
                    _list_obj_up9.clear();
                }
                if (_list_obj_up10.equals("up10")) {
                    String str_sql = str_sqlup + " and  type='DSTemperature'";
                    _csnms.execute_listobj(str_sql, _list_obj_up10);
                    _list_obj_up10.clear();
                }
                if (_list_obj_up11.equals("up11")) {
                    String str_sql = str_sqlup + " and  type='DSlogicDisk'";
                    _csnms.execute_listobj(str_sql, _list_obj_up11);
                    _list_obj_up11.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }
    
    public static void IDC_alarm(strclass._PERFBOUNDARY_COUNT _per, util.GetSql.csnms _csnms, util.GetTools.tools _tools, org.apache.log4j.Logger log) {

        if (_per.NODEID.length() > 0) {

            //加载  告警设置 信息
            String str_sql = "select "
                    + "t.MAX_CRITICAL,"
                    + "t.MAX_MAJOR,"
                    + "t.MAX_MINOR,"
                    + "t.MAX_WARN,"
                    + "t.MIN_CRITICAL,"
                    + "t.MIN_MAJOR,"
                    + "t.MIN_MINOR,"
                    + "t.MIN_WARN,"
                    + "t.TIMES,"
                    + "t.STATUS,"
                    + "t.MAINTYPE,"
                    + "t.SUBTYPE,"
                    + "t.DEVICEIP_LIST as DEVICEIP_LIST,"
                    + "to_char(t.START_DATE,'yyyy-MM-dd')  as START_DATE  ,"
                    + "to_char(t.END_DATE,'yyyy-MM-dd')  as END_DATE  ,"
                    + "t.START_TIME   ,"
                    + "t.END_TIME "
                    + "from perfboundary t  where t.MAINTYPE=2 and   t.DEVICEIP_LIST like '%" + _per.NODEIP + "%'   and    t.SUBTYPE=" + _per.SUBTYPE;

            List<Map<Object, Object>> _list_data = new ArrayList<Map<Object, Object>>();
            _list_data = _csnms.getdata(str_sql, null);

            //List list_date = new ArrayList();
            if (_list_data != null || _list_data.size() > 0) {
                for (int i = 0; i < _list_data.size(); i++) {
                    strclass._err _es = new strclass._err();
                    HashMap map = (HashMap) _list_data.get(i);
                    String key = map.get("DEVICEIP_LIST").toString();

                    //符合已知设备的    告警设置信息
                    String[] mes_h_l_l = key.split(",");
                    boolean _op = true;
                    for (int i1 = 0, m = mes_h_l_l.length; i1 < m; i1++) {
                        if (mes_h_l_l[i1].toString().equals(_per.NODEIP)) {
                            _op = true;
                        }
                    }

                    if (_op) {

                        _es.MAX_CRITICAL = map.get("MAX_CRITICAL").toString();
                        _es.MAX_MAJOR = map.get("MAX_MAJOR").toString();
                        _es.MAX_MINOR = map.get("MAX_MINOR").toString();
                        _es.MAX_WARN = map.get("MAX_WARN").toString();
                        _es.MIN_CRITICAL = map.get("MIN_CRITICAL").toString();
                        _es.MIN_MAJOR = map.get("MIN_MAJOR").toString();
                        _es.MIN_MINOR = map.get("MIN_MINOR").toString();
                        _es.MIN_WARN = map.get("MIN_WARN").toString();
                        _es.TIMES = map.get("TIMES").toString();
                        _es.STATUS = map.get("STATUS").toString();
                        _es.MAINTYPE = map.get("MAINTYPE").toString();
                        _es.SUBTYPE = map.get("SUBTYPE").toString();
                        _es.DEVICEIP_LIST = map.get("DEVICEIP_LIST").toString();
                        _es.START_DATE = map.get("START_DATE").toString();
                        _es.END_DATE = map.get("END_DATE").toString();
                        _es.START_TIME = map.get("START_TIME").toString();
                        _es.END_TIME = map.get("END_TIME").toString();

                        String _nowtime = _tools.systime_prase_string("");
                        if (_es.STATUS.equals("1")) {//告警设置  是否已启用
                            if (fun.compare_time(_nowtime, _es)) { //当前设备数据，是否已超出时间限制范围
                                //  1   CRITICAL    //  2   MAJOR    //  3   MINOR //  4   WARNING
                                int _alarmlevel = fun.compare_value(_per.VALUE, _es);

                                //开始计数判断
                                if (_alarmlevel != 0) {
                                    strclass._PERFBOUNDARY_COUNT _per_se = new strclass._PERFBOUNDARY_COUNT();
                                    List _list = db.get_PERFBOUNDARY_COUNTs("  nodeid='" + _per.NODEID + "'" + " and nodeip='" + _per.NODEIP + "' and  info='" + _per.INFO + "' and subtype=" + _per.SUBTYPE, _csnms);
                                    if (_list.size() > 0) {//有历史数据就更新
                                        _per_se = (strclass._PERFBOUNDARY_COUNT) _list.get(0);
                                        int _count = Integer.parseInt(_per_se.COUNT) + 1;
                                        _per.COUNT = _count + "";
                                        List _update = new ArrayList();
                                        _update.add(_per);
                                        db.update_PERFBOUNDARY_COUNTs(_update, "  nodeid='" + _per.NODEID + "'" + " and nodeip='" + _per.NODEIP + "' and  info='" + _per.INFO + "' and  subtype=" + _per.SUBTYPE, _csnms);
                                    } else {//没有历史数据就插入新数据
                                        _per.COUNT = "1";
                                        List _insert = new ArrayList();
                                        _insert.add(_per);
                                        insert_PERFBOUNDARY_COUNTs(_insert, _csnms);
                                    }

                                    //判断告警次数 是否已经超过限制
                                    if (!_es.TIMES.equals("-1")) {
                                        if (Integer.parseInt(_per.COUNT) > Integer.parseInt(_es.TIMES)) {
                                            //发送告警
                                            strclass.Alarm _alarm = new strclass.Alarm();
                                            _alarm.node_id = _per.NODEID;
                                            _alarm.node_name = _per.NODENAME;
                                            _alarm.type = "200";

                                            if (_per.SUBTYPE.equals("21")) {
                                                _alarm.alm_Con = "CPU阀值";
                                            } else if (_per.SUBTYPE.equals("22")) {
                                                _alarm.alm_Con = "内存阀值";
                                            } else if (_per.SUBTYPE.equals("23")) {
                                                _alarm.alm_Con = "温度阀值";
                                            } else if (_per.SUBTYPE.equals("24")) {
                                                _alarm.alm_Con = "磁盘阀值" + _per.INFO;
                                            }

                                            if (_alarmlevel == 1) {
                                                _alarm.alarm_level = "CRITICAL";
                                            } else if (_alarmlevel == 2) {
                                                _alarm.alarm_level = "MAJOR";
                                            } else if (_alarmlevel == 3) {
                                                _alarm.alarm_level = "MINOR";
                                            } else if (_alarmlevel == 4) {
                                                _alarm.alarm_level = "WARNING";
                                            }

                                            _alarm.debug_alarm_time = _tools.systime_prase_string("");
                                            _alarm.alarm_update = new Date().getTime() / 1000 + "";

                                            if (_per.INFO.equals("-1")) {
                                                _alarm.description = "<IDC设备告警>设备IP:" + _per.NODEIP + ";设备名称:" + _alarm.node_name + ";检测时间:" + _alarm.debug_alarm_time + ";类型:" + _alarm.alm_Con + ";检测值:" + _per.VALUE + ";超过阀值范围:";
                                            } else {
                                                _alarm.description = "<IDC设备告警>设备IP:" + _per.NODEIP + ";设备名称:" + _alarm.node_name + ";检测时间:" + _alarm.debug_alarm_time + ";类型:" + _alarm.alm_Con + " " + _per.INFO + ";检测值:" + _per.VALUE + ";超过阀值范围:";
                                            }

                                            if (_alarmlevel == 1) {
                                                if (Double.parseDouble(_es.MIN_CRITICAL) != 1.111 && Double.parseDouble(_es.MAX_CRITICAL) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) < Double.parseDouble(_es.MIN_CRITICAL)
                                                            || Double.parseDouble(_per.VALUE) > Double.parseDouble(_es.MAX_CRITICAL)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_CRITICAL + "_" + _es.MIN_CRITICAL;
                                                    }
                                                } else if (Double.parseDouble(_es.MAX_CRITICAL) != 1.111 && Double.parseDouble(_es.MIN_CRITICAL) == 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) >= Double.parseDouble(_es.MAX_CRITICAL)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_CRITICAL;

                                                    } else if (Double.parseDouble(_es.MAX_CRITICAL) == 1.111 && Double.parseDouble(_es.MIN_CRITICAL) != 1.111) {
                                                        if (Double.parseDouble(_per.VALUE) <= Double.parseDouble(_es.MIN_CRITICAL)) {
                                                            _alarm.description = _alarm.description + "  " + _es.MIN_CRITICAL;
                                                        }
                                                    }
                                                }
                                            } else if (_alarmlevel == 2) {
                                                if (Double.parseDouble(_es.MIN_MAJOR) != 1.111 && Double.parseDouble(_es.MAX_MAJOR) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) < Double.parseDouble(_es.MIN_MAJOR)
                                                            || Double.parseDouble(_per.VALUE) > Double.parseDouble(_es.MAX_MAJOR)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_MAJOR + "_" + _es.MIN_MAJOR;
                                                    }
                                                } else if (Double.parseDouble(_es.MAX_MAJOR) != 1.111 && Double.parseDouble(_es.MIN_MAJOR) == 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) >= Double.parseDouble(_es.MAX_MAJOR)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_MAJOR;
                                                    }

                                                } else if (Double.parseDouble(_es.MAX_MAJOR) == 1.111 && Double.parseDouble(_es.MIN_MAJOR) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) <= Double.parseDouble(_es.MIN_MAJOR)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MIN_MAJOR;
                                                    }
                                                }
                                            } else if (_alarmlevel == 3) {
                                                if (Double.parseDouble(_es.MIN_MINOR) != 1.111 && Double.parseDouble(_es.MAX_MINOR) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) < Double.parseDouble(_es.MIN_MINOR)
                                                            || Double.parseDouble(_per.VALUE) > Double.parseDouble(_es.MAX_MINOR)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_MINOR + "_" + _es.MIN_MINOR;
                                                    }
                                                } else if (Double.parseDouble(_es.MAX_MINOR) != 1.111 && Double.parseDouble(_es.MIN_MINOR) == 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) >= Double.parseDouble(_es.MAX_MINOR)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_MINOR;
                                                    }

                                                } else if (Double.parseDouble(_es.MAX_MINOR) == 1.111 && Double.parseDouble(_es.MIN_MINOR) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) <= Double.parseDouble(_es.MIN_MINOR)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MIN_MINOR;
                                                    }
                                                }
                                            } else if (_alarmlevel == 4) {
                                                if (Double.parseDouble(_es.MIN_WARN) != 1.111 && Double.parseDouble(_es.MAX_WARN) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) < Double.parseDouble(_es.MIN_WARN)
                                                            || Double.parseDouble(_per.VALUE) > Double.parseDouble(_es.MAX_WARN)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_WARN + "_" + _es.MIN_WARN;
                                                    }
                                                } else if (Double.parseDouble(_es.MAX_WARN) != 1.111 && Double.parseDouble(_es.MIN_WARN) == 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) >= Double.parseDouble(_es.MAX_WARN)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MAX_WARN;
                                                    }

                                                } else if (Double.parseDouble(_es.MAX_WARN) == 1.111 && Double.parseDouble(_es.MIN_WARN) != 1.111) {
                                                    if (Double.parseDouble(_per.VALUE) <= Double.parseDouble(_es.MIN_WARN)) {
                                                        _alarm.description = _alarm.description + "  " + _es.MIN_WARN;
                                                    }
                                                }
                                            }

                                            //获取告警字符串
                                            String _alarm_mes = fun.alarm2Str(_alarm, log);
                                            //调用发送告警
                                            boolean _isok = fun.sendAlarm(_alarm_mes, _csnms);
                                            if (_isok) { //发送成功后，删除统计表数据
                                                del_PERFBOUNDARY_COUNTs("  nodeid='" + _per.NODEID + "'" + " and nodeip='" + _per.NODEIP + "'  and  info='" + _per.INFO + "' and  subtype=" + _per.SUBTYPE, _csnms);
                                            }
                                        }
                                    }

                                }

                            }
                        }
                    }
                }
            }

        }

    }

    public static List get_IPMEMs(String str_where, util.GetSql.csnms _csnms) {
        List _datas = new ArrayList();
        StringBuffer str_sql = new StringBuffer("");
        str_sql.append(" select  ");
        str_sql.append("MEMID").append(",");
        str_sql.append("TYPE").append(",");
        str_sql.append("NAME").append(",");
        str_sql.append("USED").append(",");
        str_sql.append("PERCENT").append(",");
        str_sql.append("NODEID");
        str_sql.append(" from  IPMEM ");
        if (str_where.length() > 0) {
            str_sql.append(" where " + str_where);
        }

        List lst = null;
        try {
            lst = _csnms.getdata(str_sql.toString(), null);
        } catch (Exception e) {
            System.out.println(str_sql.toString());
        }

        for (int i = 0; i < lst.size(); i++) {
            _IPMEM _datas_l = new _IPMEM();
            StringBuffer str_xml = new StringBuffer("");
            HashMap map = (HashMap) lst.get(i);
            try {
                _datas_l.MEMID = map.get("MEMID").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.TYPE = map.get("TYPE").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.NAME = map.get("NAME").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.USED = map.get("USED").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.PERCENT = map.get("PERCENT").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.NODEID = map.get("NODEID").toString();
            } catch (Exception ex) {
            } finally {
            }
            _datas.add(_datas_l);
        }
        return _datas;
    }

    public static void tb_in_up_del(String type_in_up, List _data, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {
        //构造预处理
        try {

            int count_s = 0;

            List _list_obj_in = new ArrayList();
            List _list_obj_up = new ArrayList();
            List _list_obj_del = new ArrayList();

            if (_data.size() > 0) {
                for (int i = 0, m = _data.size(); i < m; i++) {
                    strclass._idcperfermance_config _datal = new strclass._idcperfermance_config();
                    _datal = (strclass._idcperfermance_config) _data.get(i);
                    count_s = count_s + 1;

                    //-------------处理时间---------------//
                    String _GETTIME = _datal.GETTIME;
                    SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date _date1 = new Date();
                    _date1 = sdf_temp.parse(_GETTIME);
                    String str = sdf_temp.format(_date1);
                    //------------------------------------//

                    if (type_in_up.equals("in")) {
                        Object[] objs = new Object[]{
                            _tools.string_prase_setTimestamp(str),
                            _datal.PERFID,
                            _datal.PERFERMANCE,
                            _datal.NODEIP,
                            _datal.TYPE,
                            _datal.INFO,
                            Long.parseLong(_datal.NODEID),
                            _datal.IFIDX
                        };
                        _list_obj_in.add(objs);

                    } else if (type_in_up.equals("up")) {
                        Object[] objs = new Object[]{
                            _tools.string_prase_setTimestamp(str),
                            _datal.PERFERMANCE,
                            _datal.TYPE,
                            _datal.INFO,
                            Long.parseLong(_datal.NODEID),
                            _datal.IFIDX,
                            _datal.PERFID,
                            _datal.NODEIP
                        };
                        _list_obj_up.add(objs);
                    } else if (type_in_up.equals("del")) {
                        Object[] objs = new Object[]{
                            _datal.NODEIP
                        };
                        _list_obj_del.add(objs);
                    }
                }

                if (_list_obj_in.size() > 0) {
                    String str_sql = "insert into  idcperfermance_config ("
                            + "GETTIME,"
                            + "PERFID,"
                            + "PERFERMANCE,"
                            + "NODEIP,"
                            + "TYPE,"
                            + "INFO,"
                            + "NODEID,"
                            + "IFIDX"
                            + ") values (?,?,?,?,?,?,?,?)";
                    _csnms.execute_listobj(str_sql, _list_obj_in);
                    _list_obj_in.clear();;
                }

                if (_list_obj_up.size() > 0) {
                    String str_sql = "update idcperfermance_config  set  "
                            + " GETTIME=?,"
                            + " PERFERMANCE=? , "
                            + " TYPE=?, "
                            + " INFO=? , "
                            + " NODEID=?, "
                            + " IFIDX=? "
                            + " where  "
                            + " perfid=? "
                            + " and   NODEIP=?";
                    _csnms.execute_listobj(str_sql, _list_obj_up);
                    _list_obj_up.clear();
                }

                if (_list_obj_del.size() > 0) {
                    String str_sql = "delete  from   idcperfermance_config  where   NODEIP=?";
                    _csnms.execute_listobj(str_sql, _list_obj_del);
                    _list_obj_del.clear();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }

    public static void tb_in_up_del_cert(String type_in_up, List _data, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {
        //构造预处理
        try {
            int count_s = 0;

            List _list_obj_in = new ArrayList();
            List _list_obj_up = new ArrayList();
            List _list_obj_del = new ArrayList();
            List _list_obj_del2 = new ArrayList();

            for (int i = 0, m = _data.size(); i < m; i++) {
                //System.out.println("进度：" + (i + 1) + "/" + m);
                _idcperfermance_tongyong _datal = new _idcperfermance_tongyong();
                _datal = (_idcperfermance_tongyong) _data.get(i);
                count_s = count_s + 1;

                //-------------处理时间---------------//
                String _GETTIME = _datal.GETTIME;
                SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _date1 = new Date();
                _date1 = sdf_temp.parse(_GETTIME);
                String str = sdf_temp.format(_date1);
                //------------------------------------//

                if (type_in_up.equals("in")) {

                    if (_datal.NODEID.toString().trim().length() > 0) {
                        Object[] objs = new Object[]{
                            _tools.string_prase_setTimestamp(str),
                            _datal.PERFID,
                            _datal.PERFERMANCE,
                            _datal.NODEIP,
                            _datal.TYPE,
                            _datal.INFO,
                            Long.parseLong(_datal.NODEID),
                            _datal.IFIDX
                        };
                        _list_obj_in.add(objs);
                    } else {
                        System.out.println("NODEID为空");
                    }

                } else if (type_in_up.equals("up")) {

                    if (_datal.NODEID.toString().trim().length() > 0) {

                        Object[] objs = new Object[]{
                            _tools.string_prase_setTimestamp(str),
                            _datal.PERFERMANCE,
                            _datal.NODEIP,
                            _datal.TYPE,
                            _datal.IFIDX,
                            Long.parseLong(_datal.NODEID),
                            _datal.PERFID,
                            _datal.INFO
                        };
                        _list_obj_up.add(objs);
                    } else {
                        System.out.println("NODEID为空");
                    }
                } else if (type_in_up.equals("del")) {
                    Object[] objs = new Object[]{
                        _datal.NODEIP
                    };
                    _list_obj_del.add(objs);

                } else if (type_in_up.equals("del2")) {
                    Object[] objs = new Object[]{
                        _datal.NODEIP,
                        _datal.PERFID
                    };
                    _list_obj_del2.add(objs);
                }
            }

            if (_list_obj_in.size() > 0) {
                String str_sql = "insert into  IDCPERFERMANCERT ("
                        + "GETTIME,"
                        + "PERFID,"
                        + "PERFERMANCE,"
                        + "NODEIP,"
                        + "TYPE,"
                        + "INFO,"
                        + "NODEID,"
                        + "IFIDX"
                        + ") values (?,?,?,?,?,?,?,?)";
                _csnms.execute_listobj(str_sql, _list_obj_in);
                _list_obj_in.clear();
            }
            if (_list_obj_up.size() > 0) {
                String str_sql = "update IDCPERFERMANCERT  set  "
                        + "GETTIME=?,"
                        + "PERFERMANCE=?,"
                        + "NODEIP=?,"
                        + "TYPE=?,"
                        + "IFIDX=?"
                        + " where  "
                        + " NODEID=?"
                        + " and    perfid=?"
                        + " and   info=?";
                _csnms.execute_listobj(str_sql, _list_obj_up);
                _list_obj_up.clear();
            }
            if (_list_obj_del.size() > 0) {
                String str_sql = "delete  from  IDCPERFERMANCERT  where NODEIP=?";
                _csnms.execute_listobj(str_sql, _list_obj_del);
                _list_obj_del.clear();
            }
            if (_list_obj_del2.size() > 0) {
                String str_sql = "delete  from  IDCPERFERMANCERT  where "
                        + " NODEIP=?  "
                        + " and PERFID= ?";
                _csnms.execute_listobj(str_sql, _list_obj_del2);
                _list_obj_del2.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }

    public static void tb_in_up_del_ce(String type_in_up, List _data, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {
        //构造预处理
        try {

            int count_s = 0;

            List _list_obj_in = new ArrayList();
            List _list_obj_up = new ArrayList();
            List _list_obj_del = new ArrayList();
            List _list_obj_del2 = new ArrayList();
            if (_data.size() > 0) {
                for (int i = 0, m = _data.size(); i < m; i++) {
                    //System.out.println("进度：" + (i + 1) + "/" + m);
                    _idcperfermance_tongyong _datal = new _idcperfermance_tongyong();
                    _datal = (_idcperfermance_tongyong) _data.get(i);
                    count_s = count_s + 1;

                    //-------------处理时间---------------//
                    String _GETTIME = _datal.GETTIME;
                    SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date _date1 = new Date();
                    _date1 = sdf_temp.parse(_GETTIME);
                    String str = sdf_temp.format(_date1);
                    //------------------------------------//

                    //System.out.println("time:" + str + "##" + _GETTIME);
                    if (type_in_up.equals("in")) {

                        if (_datal.NODEID.toString().trim().length() > 0) {
                            Object[] objs = new Object[]{
                                _tools.string_prase_setTimestamp(str),
                                _datal.PERFID,
                                _datal.PERFERMANCE,
                                _datal.NODEIP,
                                _datal.TYPE,
                                _datal.INFO,
                                Long.parseLong(_datal.NODEID),
                                _datal.IFIDX
                            };
                            _list_obj_in.add(objs);
                        } else {
                            System.out.println("NODEID为空");
                        }
                    } else if (type_in_up.equals("up")) {
                        if (_datal.NODEID.toString().trim().length() > 0) {
                            Object[] objs = new Object[]{
                                _tools.string_prase_setTimestamp(str),
                                _datal.PERFERMANCE,
                                _datal.NODEIP,
                                _datal.TYPE,
                                _datal.IFIDX,
                                Long.parseLong(_datal.NODEID),
                                _datal.PERFID,
                                _datal.INFO
                            };
                            _list_obj_up.add(objs);
                        } else {
                            System.out.println("NODEID为空");
                        }
                    } else if (type_in_up.equals("del_hasinfo")) {

                        Object[] objs = new Object[]{
                            _datal.NODEIP,
                            _datal.PERFID,
                            _datal.TYPE,
                            _datal.INFO,
                            _tools.string_prase_setTimestamp(str)
                        };
                        _list_obj_del.add(objs);

                    } else if (type_in_up.equals("del_nohasinfo")) {
                        Object[] objs = new Object[]{
                            _datal.NODEIP,
                            _datal.PERFID,
                            _datal.TYPE,
                            _tools.string_prase_setTimestamp(str)
                        };
                        _list_obj_del2.add(objs);
                    }
                }

                if (_list_obj_in.size() > 0) {
                    String str_sql = "insert into  IDCPERFERMANCE ("
                            + "GETTIME,"
                            + "PERFID,"
                            + "PERFERMANCE,"
                            + "NODEIP,"
                            + "TYPE,"
                            + "INFO,"
                            + "NODEID,"
                            + "IFIDX,"
                            + "rowsnum"
                            + ") values (?,?,?,?,?,?,?,?,IDCSEQ.nextval)";
                    _csnms.execute_listobj(str_sql, _list_obj_in);
                    _list_obj_in.clear();
                }
                if (_list_obj_up.size() > 0) {
                    String str_sql = "update IDCPERFERMANCE  set  "
                            + "GETTIME=?,"
                            + "PERFERMANCE=?,"
                            + "NODEIP=?,"
                            + "TYPE=?,"
                            + "IFIDX=?"
                            + " where  "
                            + " NODEID=?"
                            + " and    perfid=?"
                            + " and   info=?";
                    _csnms.execute_listobj(str_sql, _list_obj_up);
                    _list_obj_up.clear();
                }
                if (_list_obj_del.size() > 0) {
                    String str_sql = "delete  from  idcperfermance  where "
                            + " NODEIP=?"
                            + " and    perfid=?"
                            + " and    type=?"
                            + " and    info=?"
                            + " and    gettime=?";
                    _csnms.execute_listobj(str_sql, _list_obj_del);
                    _list_obj_del.clear();
                }
                if (_list_obj_del2.size() > 0) {
                    String str_sql = "delete  from  idcperfermance  where "
                            + " NODEIP=?"
                            + " and    perfid=?"
                            + " and    type=?"
                            + " and    gettime=?";
                    _csnms.execute_listobj(str_sql, _list_obj_del2);
                    _list_obj_del2.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }

    public static List get_PERFBOUNDARY_COUNTs(String str_where, util.GetSql.csnms _csnms) {
        List _datas = new ArrayList();
        StringBuffer str_sql = new StringBuffer("");
        str_sql.append(" select  ");
        str_sql.append("NODEIP").append(",");
        str_sql.append("SUBTYPE").append(",");
        str_sql.append("COUNT").append(",");
        str_sql.append("INFO").append(",");
        str_sql.append(" to_char(UPDATETIME,'yyyy-MM-dd HH24:MI:SS') as   UPDATETIME").append(",");
        str_sql.append("VALUE").append(",");
        str_sql.append("NODEID");
        str_sql.append(" from  PERFBOUNDARY_COUNT ");
        if (str_where.length() > 0) {
            str_sql.append(" where " + str_where);
        }

        List lst = null;
        try {
            lst = _csnms.getdata(str_sql.toString(), null);
        } catch (Exception e) {
            System.out.println(str_sql.toString());
        }

        for (int i = 0; i < lst.size(); i++) {
            strclass._PERFBOUNDARY_COUNT _datas_l = new strclass._PERFBOUNDARY_COUNT();
            StringBuffer str_xml = new StringBuffer("");
            HashMap map = (HashMap) lst.get(i);
            try {
                _datas_l.NODEIP = map.get("NODEIP").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.SUBTYPE = map.get("SUBTYPE").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.COUNT = map.get("COUNT").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.INFO = map.get("INFO").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.UPDATETIME = map.get("UPDATETIME").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.VALUE = map.get("VALUE").toString();
            } catch (Exception ex) {
            } finally {
            }
            try {
                _datas_l.NODEID = map.get("NODEID").toString();
            } catch (Exception ex) {
            } finally {
            }
            _datas.add(_datas_l);
        }
        return _datas;
    }

    public static boolean update_PERFBOUNDARY_COUNTs(List _datas, String str_where, util.GetSql.csnms _csnms) {
        _PERFBOUNDARY_COUNT _datas_l = new _PERFBOUNDARY_COUNT();
        for (int i = 0; i < _datas.size(); i++) {
            _datas_l = (_PERFBOUNDARY_COUNT) _datas.get(i);
            StringBuffer str_sql_key = new StringBuffer("");

            if (_datas_l.NODEIP.length() > 0) {
                str_sql_key.append("NODEIP='" + _datas_l.NODEIP + "'").append(",");
            }

            if (_datas_l.SUBTYPE.length() > 0) {
                str_sql_key.append("SUBTYPE=" + _datas_l.SUBTYPE + "").append(",");
            }

            if (_datas_l.COUNT.length() > 0) {
                str_sql_key.append("COUNT=" + _datas_l.COUNT + "").append(",");
            }

            if (_datas_l.INFO.length() > 0) {
                str_sql_key.append("INFO='" + _datas_l.INFO + "'").append(",");
            }

            if (_datas_l.UPDATETIME.length() > 0) {
                str_sql_key.append("UPDATETIME=TO_DATE('" + _datas_l.UPDATETIME + "','yyyy-MM-dd HH24:MI:SS')").append(",");
            }

            if (_datas_l.VALUE.length() > 0) {
                str_sql_key.append("VALUE='" + _datas_l.VALUE + "'").append(",");
            }

            if (_datas_l.NODEID.length() > 0) {
                str_sql_key.append("NODEID='" + _datas_l.NODEID + "'");
            }

            StringBuffer str_sql = new StringBuffer("");
            str_sql.append("update   PERFBOUNDARY_COUNT  set ");
            str_sql.append(str_sql_key.toString());
            str_sql.append("");
            if (str_where.length() > 0) {
                str_sql.append(" where " + str_where);
            }

            try {
                _csnms.execute(str_sql.toString(), null);
            } catch (Exception e) {
                System.out.println(str_sql.toString());
            }

        }

        return true;
    }

    public static boolean insert_PERFBOUNDARY_COUNTs(List _datas, util.GetSql.csnms _csnms) {
        _PERFBOUNDARY_COUNT _datas_l = new _PERFBOUNDARY_COUNT();
        for (int i = 0; i < _datas.size(); i++) {
            _datas_l = (_PERFBOUNDARY_COUNT) _datas.get(i);
            StringBuffer str_sql_key = new StringBuffer("");
            StringBuffer str_sql_data = new StringBuffer("");

            if (_datas_l.NODEIP.length() > 0) {
                str_sql_key.append("NODEIP").append(",");
                str_sql_data.append("'" + _datas_l.NODEIP + "'").append(",");
            }

            if (_datas_l.SUBTYPE.length() > 0) {
                str_sql_key.append("SUBTYPE").append(",");
                str_sql_data.append("" + _datas_l.SUBTYPE + "").append(",");
            }

            if (_datas_l.COUNT.length() > 0) {
                str_sql_key.append("COUNT").append(",");
                str_sql_data.append("" + _datas_l.COUNT + "").append(",");
            }

            if (_datas_l.INFO.length() > 0) {
                str_sql_key.append("INFO").append(",");
                str_sql_data.append("'" + _datas_l.INFO + "'").append(",");
            }

            if (_datas_l.UPDATETIME.length() > 0) {
                str_sql_key.append("UPDATETIME").append(",");
                str_sql_data.append(" TO_DATE('" + _datas_l.UPDATETIME + "','yyyy-MM-dd HH24:MI:SS')").append(",");

            }

            if (_datas_l.VALUE.length() > 0) {
                str_sql_key.append("VALUE").append(",");
                str_sql_data.append("'" + _datas_l.VALUE + "'").append(",");
            }

            if (_datas_l.NODEID.length() > 0) {
                str_sql_key.append("NODEID");
                str_sql_data.append("'" + _datas_l.NODEID + "'");
            }

            StringBuffer str_sql = new StringBuffer("");
            str_sql.append("insert into  PERFBOUNDARY_COUNT (");
            str_sql.append(str_sql_key.toString());
            str_sql.append(")values(");
            str_sql.append(str_sql_data);
            str_sql.append(")");
            try {
                _csnms.execute(str_sql.toString(), null);
            } catch (Exception e) {
                System.out.println(str_sql.toString());
            }

        }

        return true;
    }

    public boolean sendAlarm(String alarm) {
        boolean _bs = false;
        _bs = sendmessage(alarm);
        return _bs;
    }

    public static boolean sendmessage(String mes) {

        //加载并打开告警服务器
        ini();

        boolean _bs = false;
        str_mes = mes;

        try {

            if (str_mes.length() > 0) {
                // socketclient.out = new PrintWriter(socketclient.remote_socket.getOutputStream(),"UTF-8");
                socketclient.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(remote_socket.getOutputStream(), "GBK")), true);
                String mes1 = str_mes;
                socketclient.out.println(mes1);
                socketclient.out.flush();
                System.out.println("Send access:" + mes1);
                _bs = true;
                //remote_socket.close();
                //记录告警
                log_alarm.info(mes);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (remote_socket != null) {
                try {
                    remote_socket.close();
                } catch (Exception e) {
                }
            }
        } finally {

        }
        return _bs;
    }

    public static boolean loadconfig() {
        boolean _bs = false;
        Properties prop = new Properties();
        File file = new File(CONFIG_PATH);
        try {
            prop.load(new FileInputStream(file));
            System.getProperties().putAll(prop);
            remote_ip = prop.getProperty("sockethost");
            remote_port = prop.getProperty("sockethostport");
            log_alarm.info("GET Config: IP:" + remote_ip + " Port:" + remote_port);
            _bs = true;
        } catch (Exception e) {
            System.out.println("配置文件读取异常:" + e.getMessage().toString());
        }
        return _bs;
    }

    public static boolean del_PERFBOUNDARY_COUNTs(String str_where, util.GetSql.csnms _csnms) {
        StringBuffer str_sql = new StringBuffer("");
        str_sql.append("delete  from PERFBOUNDARY_COUNT  ");
        if (str_where.length() > 0) {
            str_sql.append(" where " + str_where);
        }

        try {
            _csnms.execute(str_sql.toString(), null);
        } catch (Exception e) {
            System.out.println(str_sql.toString());
        }

        return true;
    }

    public static boolean return_node_s_ishas2(String _table, String _nodeip, String str_title, String _info, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_nodeip.length() > 0) {
            String str_sql_se = "select  n.nodeid  as NODE_ID   from  " + _table + "  n  where  n.NODEIP='" + _nodeip + "'  and n.perfid='" + str_title + "'  and n.info='" + _info + "'";

            List list_data = new ArrayList();
            try {
                list_data = _csnms.getdata(str_sql_se, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (list_data != null && list_data.size() > 0) {
                _bs = true;
            }
        }
        return _bs;
    }

    public static void jiexi_ycl(List _list, util.GetSql.csnms _csnms, org.apache.log4j.Logger log) {

        try {

            Hashtable keyHash = new Hashtable();

            int count = 0;
            int rows = 0;
            strclass.port2 _port = new strclass.port2();

            List _list_obj = new ArrayList();

            for (int i = 0, m = _list.size(); i < m; i++) {
                //System.out.println("进度：" + (i + 1) + "/" + m);
                _port = new strclass.port2();
                count = count + 1;
                rows = rows + 1;

                _port = (strclass.port2) _list.get(i);

                String key = _port.DATETIME + "_" + _port.DEVICEID + "_" + _port.PORTINFO;

                Object[] objs = new Object[]{
                    Long.parseLong(_port.DATETIME),
                    _port.DEVICEID,
                    _port.PORTINFO,
                    Long.parseLong(_port.GETWAY),
                    Long.parseLong(_port.IFINOCTETS),
                    Long.parseLong(_port.IFOUTOCTETS),
                    Long.parseLong(_port.IFINERRORS),
                    Long.parseLong(_port.IFOUTERRORS),
                    Long.parseLong(_port.IFINDISCARDS),
                    Long.parseLong(_port.IFOUTDISCARDS),
                    Long.parseLong(_port.IFINUCASTPKTS),
                    Long.parseLong(_port.IFOUTUCASTPKTS),
                    Long.parseLong(_port.IFINNUCASTPKTS),
                    Long.parseLong(_port.IFOUTNUCASTPKTS),
                    Long.parseLong(_port.IFINUNKNOWNPROTOS),
                    Long.parseLong(_port.IFOUTQLEN),
                    Long.parseLong(_port.IFINOCTETSBPS),
                    Long.parseLong(_port.IFOUTOCTETSBPS),
                    Long.parseLong(_port.IFINERRORSPPS),
                    Long.parseLong(_port.IFOUTERRORSPPS),
                    Long.parseLong(_port.IFINDISCARDSPPS),
                    Long.parseLong(_port.IFOUTDISCARDSPPS),
                    Long.parseLong(_port.IFINUCASTPKTSPPS),
                    Long.parseLong(_port.IFOUTUCASTPKTSPPS),
                    Long.parseLong(_port.IFINNUCASTPKTSPPS),
                    Long.parseLong(_port.IFOUTNUCASTPKTSPPS),
                    Long.parseLong(_port.IFINUNKNOWNPROTOSPPS),
                    Long.parseLong(_port.IFOUTQLENPPS),
                    Long.parseLong(_port.DELTATIME)
                };
                _list_obj.add(objs);
            }

            if (_list_obj.size() > 0) {
                String str_sql = "insert into SWITCH_REPORT_OUT("
                        + "DATETIME,"
                        + "DEVICEID,"
                        + "PORTINFO,"
                        + "GETWAY,"
                        + "IFINOCTETS,"
                        + "IFOUTOCTETS,"
                        + "IFINERRORS,"
                        + "IFOUTERRORS,"
                        + "IFINDISCARDS,"
                        + "IFOUTDISCARDS,"
                        + "IFINUCASTPKTS,"
                        + "IFOUTUCASTPKTS,"
                        + "IFINNUCASTPKTS,"
                        + "IFOUTNUCASTPKTS,"
                        + "IFINUNKNOWNPROTOS,"
                        + "IFOUTQLEN,"
                        + "IFINOCTETSBPS,"
                        + "IFOUTOCTETSBPS,"
                        + "IFINERRORSPPS,"
                        + "IFOUTERRORSPPS,"
                        + "IFINDISCARDSPPS,"
                        + "IFOUTDISCARDSPPS,"
                        + "IFINUCASTPKTSPPS,"
                        + "IFOUTUCASTPKTSPPS,"
                        + "IFINNUCASTPKTSPPS,"
                        + "IFOUTNUCASTPKTSPPS,"
                        + "IFINUNKNOWNPROTOSPPS,"
                        + "IFOUTQLENPPS,"
                        + "DELTATIME"
                        + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                _csnms.execute_listobj(str_sql, _list_obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("ycl异常：" + e.getMessage().toString());
        }
    }

    public static boolean ini_return_node_s_ishas(String _nodeip, String _perfid, util.GetSql.csnms _csnms) {
        //构造预处理
        boolean _bs = false;
        try {
            String _table = "f5_mes";
            String str_sql = "select  n.nodeid  as NODE_ID   from  " + _table
                    + " n  where   n.NODEIP='" + _nodeip + "'  and n.perfid='" + _perfid + "'";
            List lst = new ArrayList();
            lst = _csnms.getdata(str_sql, null);
            if (lst != null && lst.size() > 0) {
                _bs = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        } finally {

        }
        return _bs;
    }

    public static boolean ini_return_node_s_ishas2(String _nodeip, String _perfid, String _info, util.GetSql.csnms _csnms) {
        //构造预处理
        boolean _bs = false;
        try {
            String _table = "f5_mes_de";
            String str_sql = "select  n.nodeid  as NODE_ID   from  " + _table
                    + " n  where   n.NODEIP='" + _nodeip + "'  and n.perfid='" + _perfid + "'  and n.info='" + _info + "' ";
            List lst = new ArrayList();
            lst = _csnms.getdata(str_sql, null);
            if (lst != null && lst.size() > 0) {
                _bs = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        } finally {

        }
        return _bs;
    }

    public static boolean ini_return_node_s_ishas4(String _nodeip, String _perfid, String _info, String _type, String _time, util.GetSql.csnms _csnms) {
        //构造预处理
        boolean _bs = false;
        try {

            String _table = "f5_mes_de";
            List lst = new ArrayList();

            if (_info.length() > 0) {

                String str_sql = "select  n.nodeid  as NODE_ID   from  " + _table
                        + " n  where  "
                        + " n.NODEIP='" + _nodeip + "' "
                        + " and n.perfid='" + _perfid + "' "
                        + " and n.info='" + _info + "'"
                        + " and n.type='" + _type + "'"
                        + " and  n.gettime=to_date('" + _time + "','yyyy-mm-dd hh24:mi:ss')";
                lst = _csnms.getdata(str_sql, null);

            } else {

                String str_sql = "select  n.nodeid  as NODE_ID   from  " + _table
                        + " n  where  "
                        + " n.NODEIP='" + _nodeip + "' "
                        + " and n.perfid='" + _perfid + "' "
                        + " and n.type='" + _type + "'"
                        + " and  n.gettime=to_date('" + _time + "','yyyy-mm-dd hh24:mi:ss')";
                lst = _csnms.getdata(str_sql, null);

            }

            if (lst != null && lst.size() > 0) {
                _bs = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        } finally {

        }
        return _bs;
    }

    public static boolean ini_return_node_s_ishas3(String _nodeip, String _perfid, String _info, String _value, util.GetSql.csnms _csnms) {
        //构造预处理
        boolean _bs = false;
        try {
            String _table = "f5_mes_de";
            String str_sql = "select  n.nodeid  as NODE_ID   from  " + _table
                    + " n  where   n.NODEIP='" + _nodeip + "'  and n.perfid='" + _perfid + "'  and n.info='" + _info + "' and  n.PERFERMANCE='" + _value + "'";
            List lst = new ArrayList();
            lst = _csnms.getdata(str_sql, null);
            if (lst != null && lst.size() > 0) {
                _bs = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        } finally {

        }
        return _bs;
    }

    public static boolean return_node_s_ishas6(String _table, String _nodeip, String str_title, String _info, String _type,util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_nodeip.length() > 0) {
            String str_sql_se = "select  n.nodeid  as NODE_ID   from  " + _table + "  n  where  n.NODEIP='" + _nodeip + "'  and n.perfid='" + str_title + "'  and n.info='" + _info + "'  and n.type='" + _type + "'";
            List list_data = new ArrayList();
            try {
                list_data = _csnms.getdata(str_sql_se, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list_data != null && list_data.size() > 0) {
                _bs = true;
            }
        }
        return _bs;
    }
    
}
