/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_jason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import static z.xn_jason.db.ini_return_node_s_ishas2;
import static z.xn_jason.db.ini_return_node_s_ishas3;
import static z.xn_jason.db.ini_return_node_s_ishas4;
import static z.xn_jason.db.keyHash6;
import static z.xn_jason.db.keyHash7;
import static z.xn_jason.db.keyHash8;
import static z.xn_jason.db.return_node_s_ishas2;

/**
 *
 * @author Administrator
 */
public class chuli {

    public static Hashtable keyHash = new Hashtable();
    public static Hashtable keyHash2 = new Hashtable();
    public static Hashtable keyHash3 = new Hashtable();
    public static Hashtable keyHash4 = new Hashtable();
    public static Hashtable keyHash5 = new Hashtable();

    //-------------------------------处理主机---------------------------------------
    public static void chuli_zhuji(List list, String _IP, util.GetSql.csnms _csnms, util.GetTools.tools _tools, org.apache.log4j.Logger log) {

        //定义
        List list_idcperfermance_config_del = new ArrayList();
        List list_idcperfermance_config_in = new ArrayList();
        List list_idcperfermance_config_up = new ArrayList();

        List list_IDCPERFERMANCE_in = new ArrayList();
        List list_IDCPERFERMANCE_up = new ArrayList();
        List list_IDCPERFERMANCE_del_hasinfo = new ArrayList();
        List list_IDCPERFERMANCE_del_noinfo = new ArrayList();

        List list_IDCPERFERMANCERT_in = new ArrayList();
        List list_IDCPERFERMANCERT_up = new ArrayList();
        List list_IDCPERFERMANCERT_del = new ArrayList();

        strclass._mes _mes = new strclass._mes();

        String _nodeid = db.return_node(_IP, _csnms);

        for (int i = 0; i < list.size(); i++) {
            //System.out.println("zhuji进度:" + (i + 1) + "/" + list.size());

            _mes = new strclass._mes();
            _mes = (strclass._mes) list.get(i);

            _mes.message_ip = _IP;
            _mes.message_nodeid = _nodeid;

            //System.out.println((i + 1) + "/" + list.size() + "#" + _IP + "#" + _nodeid + "#" + _mes.message_type1 + "#" + _mes.message_type2 + "#" + _mes.message_title + "#" + _mes.message_value + "#" + _mes.message_time);
            //System.out.println("####" + _mes.message_type1);
            //基本信息   写入idcperfermance_config表
            if (_mes.message_type1.equals("Windows") || _mes.message_type1.equals("Linux") || _mes.message_type1.equals("AIX")) {
                strclass._idcperfermance_config _datal = new strclass._idcperfermance_config();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type1;
                //_datal.INFO= _mes.message_type2;

                if (_mes.message_type1.equals("Linux")) {
                    if (_mes.message_type2.indexOf(":/") != -1) {
                        String[] mes_h2 = _mes.message_type2.toString().split(":");
                        _datal.INFO = mes_h2[1].toString();
                    }
                }

                //判断是否发送告警
                if (!_datal.NODEID.endsWith("-1")) {
                    if (_datal.PERFID.equals("ping状态")) {
                        if (!_datal.PERFERMANCE.endsWith("YES")) {
                            System.out.println("===================>开始处理Ping告警信息:");
                            System.out.println("===================>Node_ID=" + _datal.NODEID + ",Node_IP=" + _datal.NODEIP + ",Ping状态=" + _datal.PERFERMANCE);

                            alarms _alarms = new alarms();
                            String str_alarm = _alarms.return_alarm_device_open(_datal.NODEID, _datal.NODEIP, 3, "Ping不通", "200", "<Ping状态>设备IP:" + _datal.NODEIP + " Ping状态=" + _datal.PERFERMANCE, "-1", "-1", _csnms);
                            boolean _send = _alarms.sendAlarm(str_alarm);
                            if (_send) {
                                System.out.println("===================>告警发送成功:");
                            } else {
                                System.out.println("===================>告警发送失败:");
                            }
                        } else {//清除告警

                            String str_sql_see = "select  a.node_id from alarm a  where  a.node_id=" + _datal.NODEID + "  and a.alarm_type=200  and a.alarm_content='Ping不通'";

                            List ls_s = new ArrayList();
                            try {
                                ls_s = _csnms.getdata(str_sql_see, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (ls_s.size() > 0) {
                                System.out.println("===================>开始处理Ping告警信息:");
                                System.out.println("===================>Node_ID=" + _datal.NODEID + ",Node_IP=" + _datal.NODEIP + ",Ping状态=" + _datal.PERFERMANCE);
                                alarms _alarms = new alarms();
                                String str_alarm = _alarms.return_alarm_device_clear(_datal.NODEID, _datal.NODEIP, 3, "Ping不通", "200", "<Ping状态>设备IP:" + _datal.NODEIP + " Ping状态=" + _datal.PERFERMANCE, "-1", "-1", _csnms);
                                boolean _send = _alarms.sendAlarm(str_alarm);
                                if (_send) {
                                    System.out.println("===================>告警发送成功:");
                                } else {
                                    System.out.println("===================>告警发送失败:");
                                }
                            }

                        }
                    }
                }

                list_idcperfermance_config_del.add(_datal);
                if (db.return_node_s_ishas("idcperfermance_config", _datal.NODEIP, _datal.PERFID, _csnms)) {
                    list_idcperfermance_config_up.add(_datal);
                } else {
                    list_idcperfermance_config_in.add(_datal);
                }

            } else if (_mes.message_type1.equals("windows-cpu") || _mes.message_type1.equals("host_cpu")) {
                strclass._idcperfermance_tongyong _datal = new strclass._idcperfermance_tongyong();

                //---------------------------过滤不需要的信息------------------------------//
                String str_gl = "caption,dotId,entendCaption,platform,time,盘符,CPU个数,CPU主频,CPU运行队列中进程个数";
                //str_gl = str_gl + "," + "ping包个数,ping状态,snmp状态,time,设备名称";
                List _return = new ArrayList();
                String[] sss = str_gl.split(",");
                for (int i2 = 0; i2 < sss.length; i2++) {
                    _return.add(sss[i2].toString());
                }
                boolean _kkk = false;
                for (int i2 = 0, m = _return.size(); i2 < m; i2++) {
                    if (_mes.message_title.equals(_return.get(i2).toString())) {
                        _kkk = true;
                        break;
                    }
                }
                if (_kkk) {
                    continue;
                }
                //---------------------------过滤不需要的信息------------------------------//

                _datal = new strclass._idcperfermance_tongyong();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type1;

                //-----------------------处理数据--------------------------//
                //idcperfermancert  已有数据的话，先清除  再插入
                list_IDCPERFERMANCERT_del.add(_datal);
                if (_datal.INFO.length() > 0) {
                    list_IDCPERFERMANCE_del_hasinfo.add(_datal);
                } else {
                    list_IDCPERFERMANCE_del_noinfo.add(_datal);
                }
                //插入新数据
                list_IDCPERFERMANCE_in.add(_datal);
                //同样的数据写入历史记录表
                list_IDCPERFERMANCERT_in.add(_datal);
                //-----------------------处理数据--------------------------//

                //对cpu性能进行转存                
                //取值初始化
                boolean _opo = false;
                String sql_se_node = "select  NAME,MAPNODEID from  GROUPVPNMAPNODE where NODEIP='" + _datal.NODEIP + "'";
                List<Map<Object, Object>> _node = new ArrayList<Map<Object, Object>>();

                Object[] objs = new Object[]{};
                try {
                    _node = _csnms.getdata(sql_se_node, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String _Name = "";
                String _MAPNODEID = "";
                if (_node != null && _node.size() > 0) {
                    HashMap map = (HashMap) _node.get(0);
                    if (map.get("NAME") != null) {
                        _Name = map.get("NAME").toString();
                        _opo = true;
                    } else {
                        _Name = "-1";
                        _opo = true;
                    }

                    if (_opo) {
                        String type_key = _datal.PERFID;
                        if (_datal.TYPE.equals("windows-cpu") || _datal.TYPE.equals("host_cpu")) {
                            if (type_key.indexOf("CPU占用率") != -1 || type_key.indexOf("使用百分比(用户+系统)") != -1) {
                                strclass _tb_ipcpu = new strclass();
                                strclass._IPCPU _ipcpu = new strclass._IPCPU();
                                String sql2 = " select  "
                                        + "j.OBJNO as OBJNO ,"
                                        + "j.NODEID as NODEID, "
                                        + "j.VALUE as VALUE , "
                                        + "j.TYPE as  TYPE , "
                                        + "j.SIZES as SIZES  "
                                        + " from jshpfmother_node j where "
                                        + " j.NODEID=" + _datal.NODEID
                                        + " and j.TYPE=" + 1002
                                        + " order by   j.gettime  desc";

                                List lst = new ArrayList();
                                Object[] objs2 = new Object[]{};
                                try {
                                    lst = _csnms.getdata(sql2, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                _ipcpu.CPUID = "-1";
                                if (lst != null && lst.size() > 0) {
                                    HashMap map2 = (HashMap) lst.get(0);
                                    _ipcpu.CPUID = map2.get("OBJNO").toString();
                                }
                                if (_Name == null) {
                                    _ipcpu.NAME = " ";
                                } else {
                                    _ipcpu.NAME = _Name;
                                }
                                _ipcpu.NODEID = _datal.NODEID;
                                _ipcpu.PERCENT = _datal.PERFERMANCE;
                                _ipcpu.TYPE = "1002";
                                _ipcpu.USED = "1";

                                List _in_list = new ArrayList();
                                _in_list.add(_ipcpu);

                                List _se_rows = db.get_IPCPUs(" nodeid=" + _datal.NODEID, _csnms);
                                if (_se_rows.size() > 0) {
                                    db.tb_in_up_mes("up", _in_list, _csnms);
                                    //_tb_ipcpu.update_IPCPUs(_in_list, "   nodeid=" + _datal.NODEID);
                                } else {
                                    db.tb_in_up_mes("in", _in_list, _csnms);
                                    //_tb_ipcpu.insert_IPCPUs(_in_list);
                                }

                                //对CPU使用率进行告警处理
                                strclass._PERFBOUNDARY_COUNT _perfboundary = new strclass._PERFBOUNDARY_COUNT();
                                _perfboundary.NODEID = _datal.NODEID;
                                _perfboundary.NODEIP = _datal.NODEIP;
                                _perfboundary.NODENAME = _Name;
                                _perfboundary.VALUE = _datal.PERFERMANCE;
                                _perfboundary.SUBTYPE = "21";
                                _perfboundary.UPDATETIME = _tools.systime_prase_string("");
                                _perfboundary.INFO = "-1";
                                if (_datal.NODEIP.length() > 0) {
                                    db.IDC_alarm(_perfboundary, _csnms, _tools, log);
                                }

                            }
                        }
                    }
                }

            } else if (_mes.message_type1.equals("windows-disk") || _mes.message_type1.equals("filesystem")) {
                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);

                strclass._idcperfermance_tongyong _datal = new strclass._idcperfermance_tongyong();

                //---------------------------过滤不需要的信息------------------------------//
                String str_gl = "caption,dotId,entendCaption,platform,time,盘符";
                //str_gl = str_gl + "," + "ping包个数,ping状态,snmp状态,time,设备名称";
                List _return = new ArrayList();
                String[] sss = str_gl.split(",");
                for (int i2 = 0; i2 < sss.length; i2++) {
                    _return.add(sss[i2].toString());
                }
                boolean _kkk = false;
                for (int i2 = 0, m = _return.size(); i2 < m; i2++) {
                    if (_mes.message_title.equals(_return.get(i2).toString())) {
                        _kkk = true;
                        break;
                    }
                }
                if (_kkk) {
                    continue;
                }

                //---------------------------过滤不需要的信息------------------------------//
                _datal = new strclass._idcperfermance_tongyong();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2;
                if (_mes.message_type1.equals("windows-disk")) {
                    _datal.INFO = _mes.message_type2.replace("Disk:", "");
                }

                if (_mes.message_type1.equals("filesystem")) {
                    String[] _dss = _mes.message_type2.split(":");
                    if (_dss.length > 1) {
                        _datal.INFO = _dss[1].toString();
                    } else {
                        _datal.INFO = _dss[0].toString();
                    }

                }

                //-----------------------处理数据--------------------------//
                //idcperfermancert  已有数据的话，先清除  再插入
                list_IDCPERFERMANCERT_del.add(_datal);
                if (_datal.INFO.length() > 0) {
                    list_IDCPERFERMANCE_del_hasinfo.add(_datal);
                } else {
                    list_IDCPERFERMANCE_del_noinfo.add(_datal);
                }
                //插入新数据
                list_IDCPERFERMANCE_in.add(_datal);
                //同样的数据写入历史记录表
                list_IDCPERFERMANCERT_in.add(_datal);
                //-----------------------处理数据--------------------------//

                //对文件系统性能进行告警                
                //取值初始化
                boolean _opo = false;
                String sql_se_node = "select  NAME,MAPNODEID from  GROUPVPNMAPNODE where NODEIP='" + _datal.NODEIP + "'";

                List _node = new ArrayList();
                Object[] objs2 = new Object[]{};
                try {
                    _node = _csnms.getdata(sql_se_node, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String _Name = "";
                String _MAPNODEID = "";
                if (_node != null && _node.size() > 0) {
                    HashMap map = (HashMap) _node.get(0);
                    if (map.get("NAME") != null) {
                        _Name = map.get("NAME").toString();
                        _opo = true;
                    } else {
                        _Name = "-1";
                        _opo = true;
                    }

                    if (_opo) {

                        String type_key = _datal.PERFID;
                        if (type_key.indexOf("硬盘使用率") != -1 || type_key.indexOf("文件系统使用率") != -1) {
                            String _nodeip = _mes.message_ip;
                            String _disk = _datal.INFO;
                            if (_mes.message_type1.equals("Linux")) {
                                if (_mes.message_type2.indexOf(":/") != -1) {
                                    String[] mes_h2 = _mes.message_type2.toString().split(":");
                                    _disk = mes_h2[1].toString();
                                }
                            }

                            //对磁盘使用率进行告警处理
                            strclass._PERFBOUNDARY_COUNT _perfboundary = new strclass._PERFBOUNDARY_COUNT();
                            _perfboundary.NODEID = _datal.NODEID;
                            _perfboundary.NODEIP = _datal.NODEIP;
                            _perfboundary.NODENAME = _Name;
                            _perfboundary.VALUE = _datal.PERFERMANCE;
                            _perfboundary.SUBTYPE = "24";
                            _perfboundary.UPDATETIME = _tools.systime_prase_string("");
                            _perfboundary.INFO = _disk;
                            if (_datal.NODEIP.length() > 0) {
                                db.IDC_alarm(_perfboundary, _csnms, _tools, log);
                            }
                        }
                    }
                }

            } else if (_mes.message_type1.equals("windows-mem") || _mes.message_type1.equals("host_mem")) {

                strclass._idcperfermance_tongyong _datal = new strclass._idcperfermance_tongyong();

                //---------------------------过滤不需要的信息------------------------------//
                String str_gl = "caption,dotId,entendCaption,platform,time,盘符";
                //str_gl = str_gl + "," + "ping包个数,ping状态,snmp状态,time,设备名称";
                List _return = new ArrayList();
                String[] sss = str_gl.split(",");
                for (int i2 = 0; i2 < sss.length; i2++) {
                    _return.add(sss[i2].toString());
                }
                boolean _kkk = false;
                for (int i2 = 0, m = _return.size(); i2 < m; i2++) {
                    if (_mes.message_title.equals(_return.get(i2).toString())) {
                        _kkk = true;
                        break;
                    }
                }
                if (_kkk) {
                    continue;
                }

                //---------------------------过滤不需要的信息------------------------------//
                _datal = new strclass._idcperfermance_tongyong();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type1;

                //-----------------------处理数据--------------------------//
                //idcperfermancert  已有数据的话，先清除  再插入
                list_IDCPERFERMANCERT_del.add(_datal);
                /*if (_datal.INFO.length() > 0) {
                 list_IDCPERFERMANCE_del_hasinfo.add(_datal);
                 } else {
                 list_IDCPERFERMANCE_del_noinfo.add(_datal);
                 }*/
                //插入新数据
                list_IDCPERFERMANCE_in.add(_datal);
                //同样的数据写入历史记录表
                list_IDCPERFERMANCERT_in.add(_datal);
                //-----------------------处理数据--------------------------//

                //对内存数据进行转存
                //取值初始化
                boolean _opo = false;
                String sql_se_node = "select  NAME,MAPNODEID from  GROUPVPNMAPNODE where NODEIP='" + _datal.NODEIP + "'";

                List _node = new ArrayList();
                try {
                    _node = _csnms.getdata(sql_se_node, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String _Name = "";
                String _MAPNODEID = "";
                if (_node != null && _node.size() > 0) {
                    HashMap map = (HashMap) _node.get(0);
                    if (map.get("NAME") != null) {
                        _Name = map.get("NAME").toString();
                        _opo = true;
                    } else {
                        _Name = "-1";
                        _opo = true;
                    }
                    if (_opo) {
                        String type_key = _datal.PERFID;
                        if (_datal.TYPE.equals("windows-mem") || _datal.TYPE.equals("host_mem")) {
                            if (type_key.indexOf("真实内存使用率") != -1) {
                                db _tb_ipmem = new db();
                                strclass._IPMEM _ipmem = new strclass._IPMEM();
                                String sql2 = " select  "
                                        + "j.OBJNO as OBJNO ,"
                                        + "j.NODEID as NODEID, "
                                        + "j.VALUE as VALUE , "
                                        + "j.TYPE as  TYPE , "
                                        + "j.SIZES as SIZES  "
                                        + " from jshpfmother_node j where "
                                        + " j.NODEID=" + _datal.NODEID
                                        + " and j.TYPE=" + 1003
                                        + " order by   j.gettime  desc";

                                List lst = new ArrayList();
                                Object[] objs3 = new Object[]{};
                                try {
                                    lst = _csnms.getdata(sql2, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                _ipmem.MEMID = "-1";
                                if (lst != null && lst.size() > 0) {
                                    HashMap map2 = (HashMap) lst.get(0);
                                    _ipmem.MEMID = map2.get("OBJNO").toString();
                                }
                                _ipmem.NAME = _Name;
                                _ipmem.NODEID = _datal.NODEID;
                                _ipmem.TYPE = "1003";
                                _ipmem.USED = "1";
                                _ipmem.PERCENT = _datal.PERFERMANCE;
                                List _in_list = new ArrayList();
                                _in_list.add(_ipmem);

                                List _se_rows = _tb_ipmem.get_IPMEMs("   nodeid=" + _datal.NODEID, _csnms);
                                if (_se_rows.size() > 0) {
                                    _tb_ipmem.tb_in_up_mes("up", _in_list, _csnms);
                                    //_tb_ipmem.update_IPMEMs(_in_list, "   nodeid=" + _datal.NODEID);
                                } else {
                                    _tb_ipmem.tb_in_up_mes("in", _in_list, _csnms);
                                    //_tb_ipmem.insert_IPMEMs(_in_list);
                                }

                                //对内存使用率进行告警处理
                                strclass._PERFBOUNDARY_COUNT _perfboundary = new strclass._PERFBOUNDARY_COUNT();
                                _perfboundary.NODEID = _datal.NODEID;
                                _perfboundary.NODEIP = _datal.NODEIP;
                                _perfboundary.NODENAME = _Name;
                                _perfboundary.VALUE = _datal.PERFERMANCE;
                                _perfboundary.SUBTYPE = "22";
                                _perfboundary.UPDATETIME = _tools.systime_prase_string("");
                                _perfboundary.INFO = "-1";
                                if (_datal.NODEIP.length() > 0) {
                                    db.IDC_alarm(_perfboundary, _csnms, _tools, log);
                                }
                            }
                        }
                    }
                }

            } else {//非基本信息的数据     写入性能表

            }
        }

        //-----------------------------idcperfermance_config---------------------------//
        System.out.println("zhuji开始处理数据-idcperfermance_config");
        if (list_idcperfermance_config_del.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del("del", list_idcperfermance_config_del, _csnms, _tools);
            list_idcperfermance_config_del.clear();
        }

        if (list_idcperfermance_config_in.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del("in", list_idcperfermance_config_in, _csnms, _tools);
            list_idcperfermance_config_in.clear();
        }

        if (list_idcperfermance_config_up.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del("up", list_idcperfermance_config_up, _csnms, _tools);
            list_idcperfermance_config_up.clear();
        }
        //-----------------------------idcperfermance_config---------------------------//

        //-------------------------IDCPERFERMANCERT------------------------------------//
        System.out.println("zhuji开始处理数据-IDCPERFERMANCERT");

        if (list_IDCPERFERMANCERT_del.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del_cert("del", list_IDCPERFERMANCERT_del, _csnms, _tools);
            list_IDCPERFERMANCERT_del.clear();
        }

        if (list_IDCPERFERMANCERT_in.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del_cert("in", list_IDCPERFERMANCERT_in, _csnms, _tools);
            list_IDCPERFERMANCERT_in.clear();
        }

        if (list_IDCPERFERMANCERT_up.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del_cert("up", list_IDCPERFERMANCERT_up, _csnms, _tools);
            list_IDCPERFERMANCERT_up.clear();
        }

        //-------------------------IDCPERFERMANCERT------------------------------------//
        //-------------------------IDCPERFERMANCE------------------------------------//
        System.out.println("zhuji开始处理数据-IDCPERFERMANCE");
        if (list_IDCPERFERMANCE_in.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del_ce("in", list_IDCPERFERMANCE_in, _csnms, _tools);
            list_IDCPERFERMANCE_in.clear();
        }

        if (list_IDCPERFERMANCE_up.size() > 0) {
            db _does = new db();
            _does.tb_in_up_del_ce("up", list_IDCPERFERMANCE_up, _csnms, _tools);
            list_IDCPERFERMANCE_up.clear();
        }

        if (list_IDCPERFERMANCE_del_hasinfo.size() > 0) {
            db _does = new db();
            //_does.tb_in_up_del_ce("del_hasinfo", list_IDCPERFERMANCE_del_hasinfo);
            list_IDCPERFERMANCE_del_hasinfo.clear();
        }

        if (list_IDCPERFERMANCE_del_noinfo.size() > 0) {
            //_does.tb_in_up_del_ce("del_noinfo", list_IDCPERFERMANCE_del_noinfo);
            list_IDCPERFERMANCE_del_noinfo.clear();
        }
        //-------------------------IDCPERFERMANCE------------------------------------//
        System.out.println("zhuji处理完成");
    }

    //-------------------------------处理oracle-----------------------------------
    public static void chuli_oracle(List list, String _IP, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {

        keyHash.clear();

        List list_in_idc1 = new ArrayList();
        List list_in_idc2 = new ArrayList();
        List list_IDCPERFERMANCERT_del = new ArrayList();

        strclass._mes _mes = new strclass._mes();
        List list_in = new ArrayList();
        List list_up = new ArrayList();

        List list_in_de = new ArrayList();
        List list_up_de = new ArrayList();

        int yy = 0;

        String mm_time = "";
        String _nodeid = db.return_node(_IP, _csnms);

        for (int i = 0; i < list.size(); i++) {
            _mes = new strclass._mes();
            _mes = (strclass._mes) list.get(i);
            _mes.message_ip = _IP;
            _mes.message_nodeid = _nodeid;

            //System.out.println("SS:" + (i + 1) + " " + _mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
            //基本信息    写入oracle_mes表
            if (_mes.message_type1.trim().equals("L_ORACLE")) {
                strclass._oracle_mes _datal = new strclass._oracle_mes();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type1;

                if (db.return_node_s_ishas("oracle_mes", _datal.NODEIP, _datal.PERFID, _csnms)) {
                    list_up.add(_datal);
                } else {
                    String key = _datal.NODEIP + "#" + _datal.PERFID;
                    if (!keyHash.containsKey(key)) {
                        keyHash.put(key, key);
                        list_in.add(_datal);
                    } else {
                        continue;
                    }
                }

                if (_mes.message_title.equals("DataCache命中率") || _mes.message_title.equals("共享内存使用率")) {
                    //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                    //多余信息写入性能表
                    strclass._idcperfermance_tongyong _datal2 = new strclass._idcperfermance_tongyong();
                    _datal2 = new strclass._idcperfermance_tongyong();
                    _datal2.IFIDX = "1";
                    if (_mes.message_nodeid.length() == 0) {
                        _datal.NODEID = "-1";
                    } else {
                        _datal2.NODEID = _mes.message_nodeid;
                    }

                    //命中率 等时间，  取下面oracle表的 数据时间
                    if (mm_time.length() == 0) {
                        _datal2.GETTIME = _mes.message_time;
                    } else {
                        _datal2.GETTIME = mm_time;
                    }
                    _datal2.PERFID = _mes.message_title;
                    _datal2.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                    _datal2.NODEIP = _mes.message_ip;
                    _datal2.TYPE = _mes.message_type1;

                    //idcperfermancert  已有数据的话，先清除  再插入
                    list_IDCPERFERMANCERT_del.add(_datal2);
                    //插入新数据
                    list_in_idc1.add(_datal2);
                    //同样的数据写入历史记录表
                    list_in_idc2.add(_datal2);
                }

            } else if (_mes.message_type1.equals("ora_tablespace")) {

                mm_time = _mes.message_time;
                //表信息进行记录
                strclass._oracle_mes_de _datal = new strclass._oracle_mes_de();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2;

                if (return_node_s_ishas2("ORACLE_MES_DE", _mes.message_ip, _mes.message_title, _mes.message_type2, _csnms)) {
                    list_up_de.add(_datal);
                } else {
                    String key = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO;
                    if (!keyHash.containsKey(key)) {
                        keyHash.put(key, key);
                        list_in_de.add(_datal);
                    } else {
                        continue;
                    }
                }
            } else {
                //多余信息写入性能表

                /*
                 jason.tb_idcperfermancert._idcperfermancert _datal = new jason.tb_idcperfermancert._idcperfermancert();
                 jason.tb_idcperfermancert _does = new jason.tb_idcperfermancert();
                 _datal = new jason.tb_idcperfermancert._idcperfermancert();
                 _datal.IFIDX = "1";
                 if (_mes.message_nodeid.length() == 0) {
                 _datal.NODEID = "-1";
                 } else {
                 _datal.NODEID = _mes.message_nodeid;
                 }
                 _datal.GETTIME = _mes.message_time;
                 _datal.PERFID = _mes.message_title;
                 _datal.PERFERMANCE = jason.jason_a.return_value(_mes.message_title, _mes.message_value);
                 _datal.NODEIP = _mes.message_ip;
                 _datal.TYPE = _mes.message_type1;

                 //idcperfermancert  已有数据的话，先清除  再插入
                 if (jason.jason_a.return_node_s_ishas("IDCPERFERMANCERT", _mes.message_ip, _mes.message_title)) {
                 String _del = "delete  from  idcperfermancert  where   NODEIP='" + _datal.NODEIP + "' and perfid !='DataCache命中率'  and perfid !='共享内存使用率'  ";
                 getSql.SQL_Execute(_del);
                 }

                 //插入新数据
                 List list2 = new ArrayList();
                 list2.add(_datal);
                 String kk = _does.insert_IDCPERFERMANCERTs(list2);
                 list_in.add(kk);
                 //同样的数据写入历史记录表
                 String kk1 = _does.insert_IDCPERFERMANCE(list2);
                 list_in.add(kk1);
                 */
            }

        }

        if (list_IDCPERFERMANCERT_del.size() > 0) {
            db.tb_in_up_del_cert("del2", list_IDCPERFERMANCERT_del, _csnms, _tools);
        }

        if (list_in.size() > 0) {
            db.tb_in_up_mes("in", list_in, _csnms);
        }

        if (list_up.size() > 0) {
            db.tb_in_up_mes("up", list_up, _csnms);
        }

        if (list_in_de.size() > 0) {
            db.tb_in_up_mes("in", list_in_de, _csnms);
        }

        if (list_up_de.size() > 0) {
            db.tb_in_up_mes("up", list_up_de, _csnms);
        }

        if (list_in_idc1.size() > 0) {
            db.tb_in_up_del_ce("in", list_in_idc1, _csnms, _tools);
        }

        if (list_in_idc2.size() > 0) {
            db.tb_in_up_del_cert("in", list_in_idc2, _csnms, _tools);
        }

    }

    //-----------------------------------------------------------------
    public static void chuli_firewall(List list, String _IP, util.GetSql.csnms _csnms, util.GetTools.tools _tools, org.apache.log4j.Logger log) {
        strclass._mes _mes = new strclass._mes();
        List list_in = new ArrayList();
        List list_up = new ArrayList();
        String port = "";
        String ip = "";
        String input = "";
        String out = "";
        String date = "";
        String _nodeid = db.return_node(_IP, _csnms);

        for (int i = 0; i < list.size(); i++) {
            _mes = new strclass._mes();
            _mes = (strclass._mes) list.get(i);
            _mes.message_ip = _IP;
            _mes.message_nodeid = _nodeid;

            //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
            //基本信息    写入oracle_mes表
            if (_mes.message_type1.equals("net_device_Port")) {
                port = _mes.message_type2;
                if (_mes.message_title.equals("流入速率(bps)")) {
                    input = _mes.message_value;
                } else if (_mes.message_title.equals("流出速率(bps)")) {
                    out = _mes.message_value;
                }

                date = _mes.message_time;
                ip = _mes.message_ip;

                if (port.length() > 0 && ip.length() > 0 && input.length() > 0 && out.length() > 0 && date.length() > 0) {
                    String ll = fun.stat(port, ip, input, out, date, _csnms, log);
                    list_in.add(ll);
                    port = "";
                    ip = "";
                    input = "";
                    out = "";
                    date = "";
                }
            } else {

                //多余信息写入性能表
                /*
                
                 jason.tb_idcperfermancert._idcperfermancert _datal = new jason.tb_idcperfermancert._idcperfermancert();
                 jason.tb_idcperfermancert _does = new jason.tb_idcperfermancert();
                 _datal = new jason.tb_idcperfermancert._idcperfermancert();
                 _datal.IFIDX = "1";
                 if (_mes.message_nodeid.length() == 0) {
                 _datal.NODEID = "-1";
                 } else {
                 _datal.NODEID = _mes.message_nodeid;
                 }
                 _datal.GETTIME = _mes.message_time;
                 _datal.PERFID = _mes.message_title;
                 _datal.PERFERMANCE = jason.jason_a.return_value(_mes.message_title, _mes.message_value);
                 _datal.NODEIP = _mes.message_ip;
                 _datal.TYPE = _mes.message_type1;

                 //idcperfermancert  已有数据的话，先清除  再插入
                 if (jason.jason_a.return_node_s_ishas("IDCPERFERMANCERT", _mes.message_ip, _mes.message_title)) {
                 String _del = "delete  from  idcperfermancert  where   NODEIP='" + _datal.NODEIP + "'";
                 getSql.SQL_Execute(_del);
                 }

                 //插入新数据
                 List list2 = new ArrayList();
                 list2.add(_datal);
                 String kk = _does.insert_IDCPERFERMANCERTs(list2);
                 list_in.add(kk);
                 //同样的数据写入历史记录表
                 String kk1 = _does.insert_IDCPERFERMANCE(list2);
                 list_in.add(kk1);
                 */
            }

        }

        if (list_in.size() > 0) {
            _csnms.execute(list_in);
        }

        if (list_up.size() > 0) {
            _csnms.execute(list_up);
        }
    }

    //-----------------------------------------------------------------
    public static void chuli_f5(List list, String _IP, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {

        keyHash.clear();
        keyHash2.clear();
        keyHash3.clear();
        keyHash4.clear();
        keyHash5.clear();
        keyHash6.clear();
        keyHash7.clear();
        keyHash8.clear();

        strclass._mes _mes = new strclass._mes();
        List list_in = new ArrayList();
        List list_up = new ArrayList();

        List list_in_de = new ArrayList();
        List list_up_de = new ArrayList();
        List list_up_de2 = new ArrayList();
        List list_up_de3 = new ArrayList();

        List list_in_idc1 = new ArrayList();
        List list_in_idc2 = new ArrayList();

        List list_IDCPERFERMANCERT_del = new ArrayList();

        int yy = 0;

        String mm_time = "";
        String _nodeid = db.return_node(_IP, _csnms);

        String s_time = "";
        String s_status = "";

        for (int i = 0; i < list.size(); i++) {
            _mes = new strclass._mes();
            _mes = (strclass._mes) list.get(i);
            _mes.message_ip = _IP;
            _mes.message_nodeid = _nodeid;

            //System.out.println("SS:" + (i + 1) + " " + _mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
            //基本信息    写入f5_mes表
            if (_mes.message_type1.trim().equals("F5")) {
                strclass._tb_f5_mes _datal = new strclass._tb_f5_mes();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;

                if (db.ini_return_node_s_ishas(_mes.message_ip, _mes.message_title, _csnms)) {
                    list_up.add(_datal);
                } else {

                    String key = _datal.NODEIP + "#" + _datal.PERFID;
                    if (!keyHash.containsKey(key)) {
                        keyHash.put(key, key);
                        list_in.add(_datal);
                    } else {
                        continue;
                    }

                }
            } else if (_mes.message_type1.equals("F5_vsserver")) {

                //---------------------------过滤不需要的信息------------------------------//
                String str_gl = "caption,dotId,entendCaption,platform";
                List _return = new ArrayList();
                String[] sss = str_gl.split(",");
                for (int i2 = 0; i2 < sss.length; i2++) {
                    _return.add(sss[i2].toString());
                }
                boolean _kkk = false;
                for (int i2 = 0, m = _return.size(); i2 < m; i2++) {
                    if (_mes.message_title.equals(_return.get(i2).toString())) {
                        _kkk = true;
                        break;
                    }
                }
                if (_kkk) {
                    continue;
                }
                //---------------------------过滤不需要的信息------------------------------//

                mm_time = _mes.message_time;

                //表信息进行记录
                strclass._tb_f5_mes_de _datal = new strclass._tb_f5_mes_de();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type2;
                _datal.INFO = "";

                if (_datal.PERFID.equals("状态")) {
                    s_status = _mes.message_value;
                    continue;
                }
                if (_mes.message_title.equals("time")) {
                    s_time = _mes.message_value;
                    continue;
                }

                if (_mes.message_type2.equals("vsserver")) {
                    _datal.INFO = _mes.message_value;
                }

                String key2 = "";

                if (_mes.message_title.equals("VServer")) {
                    if (s_status.length() > 0 && s_time.length() > 0) {
                        String str_sql_k = "";
                        //time
                        _datal.PERFID = "time";
                        _datal.PERFERMANCE = s_time;
                        _datal.INFO = _mes.message_value;

                        if (db.ini_return_node_s_ishas2(_mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_de.add(_datal);
                            s_time = "";
                        } else {
                            key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO + "#" + _datal.PERFERMANCE;
                            if (!keyHash2.containsKey(key2)) {
                                keyHash2.put(key2, key2);
                                list_in_de.add(_datal);
                            } else {
                                continue;
                            }

                            s_time = "";
                        }

                        //statue
                        _datal.PERFID = "状态";
                        _datal.PERFERMANCE = s_status;
                        _datal.INFO = _mes.message_value;

                        if (ini_return_node_s_ishas4(_mes.message_ip, _datal.PERFID, _datal.INFO, s_time, _datal.TYPE, _csnms)) {
                            list_up_de2.add(_datal);
                            s_time = "";
                        } else {
                            key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO + "#" + _datal.PERFERMANCE;
                            if (!keyHash2.containsKey(key2)) {
                                keyHash2.put(key2, key2);
                                list_in_de.add(_datal);
                            } else {
                                continue;
                            }
                            s_time = "";
                        }
                    }
                } else {
                    if (ini_return_node_s_ishas2(_mes.message_ip, _mes.message_title, _datal.INFO, _csnms)) {
                        list_up_de.add(_datal);
                    } else {
                        key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO + "#" + _datal.PERFERMANCE;
                        if (!keyHash2.containsKey(key2)) {
                            keyHash2.put(key2, key2);
                            list_in_de.add(_datal);
                        } else {
                            continue;
                        }
                    }

                }

            } else if (_mes.message_type1.equals("f5_port_pm")) {

                //---------------------------过滤不需要的信息------------------------------//
                String str_gl = "caption,dotId,entendCaption,platform";
                //str_gl = str_gl + "," + "ping包个数,ping状态,snmp状态,time,设备名称";
                List _return = new ArrayList();
                String[] sss = str_gl.split(",");
                for (int i2 = 0; i2 < sss.length; i2++) {
                    _return.add(sss[i2].toString());
                }
                boolean _kkk = false;
                for (int i2 = 0, m = _return.size(); i2 < m; i2++) {
                    if (_mes.message_title.equals(_return.get(i2).toString())) {
                        _kkk = true;
                        break;
                    }
                }
                if (_kkk) {
                    continue;
                }
                //---------------------------过滤不需要的信息------------------------------//

                mm_time = _mes.message_time;
                //表信息进行记录
                strclass._tb_f5_mes_de _datal = new strclass._tb_f5_mes_de();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type2;
                _datal.INFO = "";
                if (_mes.message_type2.equals("summary")) {
                    if (_mes.message_title.equals("端口名称")) {
                        _datal.INFO = _mes.message_value;
                    } else {
                        _datal.INFO = "summary";
                    }
                }
                if (_mes.message_title.equals("端口状态")) {
                    s_status = _mes.message_value;
                    continue;
                }
                if (_mes.message_title.equals("time")) {
                    s_time = _mes.message_value;
                    continue;
                }

                String key2 = "";
                if (ini_return_node_s_ishas3(_mes.message_ip, _mes.message_title, _datal.INFO, _mes.message_value, _csnms)) {
                    list_up_de3.add(_datal);
                } else {

                    key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO + "#" + _datal.PERFERMANCE;
                    if (!keyHash2.containsKey(key2)) {
                        keyHash2.put(key2, key2);
                        list_in_de.add(_datal);
                    } else {
                        continue;
                    }

                }

                if (_mes.message_title.equals("端口名称")) {
                    if (s_status.length() > 0 && s_time.length() > 0) {
                        String str_sql_k = "";
                        //time
                        _datal.PERFID = "time";
                        _datal.PERFERMANCE = s_time;
                        _datal.INFO = _mes.message_value;

                        if (ini_return_node_s_ishas3(_mes.message_ip, _datal.PERFID, _datal.INFO, _datal.PERFERMANCE, _csnms)) {
                            list_up_de3.add(_datal);
                            s_time = "";
                        } else {
                            key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO + "#" + _datal.PERFERMANCE;
                            if (!keyHash2.containsKey(key2)) {
                                keyHash2.put(key2, key2);
                                list_in_de.add(_datal);
                            } else {
                                continue;
                            }
                            s_time = "";
                        }

                        //statue
                        _datal.PERFID = "端口状态";
                        _datal.PERFERMANCE = s_status;
                        _datal.INFO = _mes.message_value;

                        if (ini_return_node_s_ishas3(_mes.message_ip, _datal.PERFID, _datal.INFO, _datal.PERFERMANCE, _csnms)) {
                            list_up_de3.add(_datal);
                            s_status = "";
                        } else {

                            key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO + "#" + _datal.PERFERMANCE;
                            if (!keyHash2.containsKey(key2)) {
                                keyHash2.put(key2, key2);
                                list_in_de.add(_datal);
                            } else {
                                continue;
                            }

                            s_status = "";
                        }
                    }
                }

            } else if (_mes.message_type1.equals("route_pm")) {
                //信息写入性能表
                //---------------------------过滤不需要的信息------------------------------//
                String str_gl = "caption,dotId,entendCaption,platform,ping包个数,ping状态,snmp状态,time,设备名称,设备名";
                //str_gl = str_gl + "," + "ping包个数,ping状态,snmp状态,time,设备名称";
                List _return = new ArrayList();
                String[] sss = str_gl.split(",");
                for (int i2 = 0; i2 < sss.length; i2++) {
                    _return.add(sss[i2].toString());
                }
                boolean _kkk = false;
                for (int i2 = 0, m = _return.size(); i2 < m; i2++) {
                    if (_mes.message_title.equals(_return.get(i2).toString())) {
                        _kkk = true;
                        break;
                    }
                }
                if (_kkk) {
                    continue;
                }
                //---------------------------过滤不需要的信息------------------------------//

                strclass._idcperfermance_tongyong _datal = new strclass._idcperfermance_tongyong();
                _datal = new strclass._idcperfermance_tongyong();
                _datal.IFIDX = "1";
                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;

                //idcperfermancert  已有数据的话，先清除  再插入
                //idcperfermancert  已有数据的话，先清除  再插入
                list_IDCPERFERMANCERT_del.add(_datal);

                //插入新数据
                list_in_idc1.add(_datal);
                //同样的数据写入历史记录表
                list_in_idc2.add(_datal);
            }

        }

        if (list_IDCPERFERMANCERT_del.size() > 0) {
            db.tb_in_up_del_cert("del", list_IDCPERFERMANCERT_del, _csnms, _tools);
        }

        if (list_in.size() > 0) {
            db.tb_in_up_mes("in", list_in, _csnms);
        }

        if (list_up.size() > 0) {
            db.tb_in_up_mes("up", list_up, _csnms);
        }

        if (list_in_de.size() > 0) {
            db.tb_in_up_mes("in", list_in_de, _csnms);
        }

        if (list_up_de.size() > 0) {
            db.tb_in_up_mes("up", list_up_de, _csnms);
        }

        if (list_up_de2.size() > 0) {
            db.tb_in_up_mes("up2", list_up_de2, _csnms);
        }

        if (list_up_de3.size() > 0) {
            db.tb_in_up_mes("up3", list_up_de3, _csnms);
        }

        if (list_in_idc1.size() > 0) {
            db.tb_in_up_del_ce("in", list_in_idc1, _csnms, _tools);
        }

        if (list_in_idc2.size() > 0) {
            db.tb_in_up_del_cert("in", list_in_idc2, _csnms, _tools);
        }

    }

    //-----------------------------------------------------------------
    public static void shugaungDS(List list, String _IP, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {

        keyHash.clear();
        keyHash2.clear();
        keyHash3.clear();
        keyHash4.clear();
        keyHash5.clear();

        strclass._mes _mes = new strclass._mes();
        List list_in_ShuGuangDS = new ArrayList();
        List list_up_ShuGuangDS = new ArrayList();

        List list_in_DSChannel = new ArrayList();
        List list_up_DSChannel = new ArrayList();

        List list_in_DSController = new ArrayList();
        List list_up_DSController = new ArrayList();

        List list_in_DSDisk = new ArrayList();
        List list_up_DSDisk = new ArrayList();

        List list_in_DSEnclosure = new ArrayList();
        List list_up_DSEnclosure = new ArrayList();

        List list_in_DSEsm = new ArrayList();
        List list_up_DSEsm = new ArrayList();

        List list_in_DSFans = new ArrayList();
        List list_up_DSFans = new ArrayList();

        List list_in_DSPowerFan = new ArrayList();
        List list_up_DSPowerFan = new ArrayList();

        List list_in_DSPowerSupplies = new ArrayList();
        List list_up_DSPowerSupplies = new ArrayList();

        List list_in_DSSFPs = new ArrayList();
        List list_up_DSSFPs = new ArrayList();

        List list_in_DSTemperature = new ArrayList();
        List list_up_DSTemperature = new ArrayList();

        List list_in_DSlogicDisk = new ArrayList();
        List list_up_DSlogicDisk = new ArrayList();

        String s_CTRL_A_LINK = "";
        String s_CTRL_B_LINK = "";
        String s_Channel = "";
        String s_port = "";
        String s_status = "";
        String s_time = "";
        String s_info = "";

        String d_CAPACITY = "";
        String d_SLOT = "";
        String d_STATUS = "";
        String d_TRAY = "";
        String d_time = "";

        String a_capacity_GB = "";
        String a_name = "";
        String a_Array = "";
        String a_raidLevel = "";
        String a_status = "";
        String a_time = "";
        String a_info = "";

        String a1_status = "";
        String a1_Location = "";
        String a1_info = "";

        String _nodeid = db.return_node(_IP, _csnms);

        for (int i = 0; i < list.size(); i++) {
            _mes = new strclass._mes();
            _mes = (strclass._mes) list.get(i);
            _mes.message_ip = _IP;
            _mes.message_nodeid = _nodeid;

            //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
            //基本信息    DSChannel_mes
            if (_mes.message_type1.equals("ShuGuangDS")) {
                strclass._tb_shuguangDS_mes _datal = new strclass._tb_shuguangDS_mes();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = "";
                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                if (db.return_node_s_ishas("DSChannel_mes", _mes.message_ip, _mes.message_title, _csnms)) {
                    list_up_ShuGuangDS.add(_datal);
                } else {

                    String key = _datal.NODEIP + "#" + _datal.PERFID;
                    if (!keyHash.containsKey(key)) {
                        keyHash.put(key, key);
                        list_in_ShuGuangDS.add(_datal);
                    } else {
                        continue;
                    }

                }

            } else if (_mes.message_type1.equals("DSChannel")) {
                boolean _sd = false;
                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();

                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;

                //去除无用的数据
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                _sd = true;
                                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                            }
                        }
                    }
                }

                if (_sd) {

                    if (_mes.message_title.equals("CTRL B LINK")) {
                        s_CTRL_B_LINK = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("Channel")) {
                        s_Channel = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("port")) {
                        s_port = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("status")) {
                        s_status = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("time")) {
                        s_time = _mes.message_value;
                        continue;
                    }

                    if (_mes.message_title.equals("CTRL A LINK")) {
                        s_info = s_Channel;
                        _datal.INFO = s_info;

                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _mes.message_title, _datal.INFO, _csnms)) {
                            list_up_DSChannel.add(_datal);

                        } else {

                            String key2 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO;
                            if (!keyHash2.containsKey(key2)) {
                                keyHash2.put(key2, key2);
                                list_in_DSChannel.add(_datal);
                            } else {
                                continue;
                            }

                        }
                    }

                    if (_mes.message_title.equals("CTRL A LINK")) {
                        if (s_status.length() > 0 && s_time.length() > 0) {
                            String str_sql_k = "";

                            //Channel
                            _datal.PERFID = "Channel";
                            _datal.PERFERMANCE = s_Channel;
                            _datal.INFO = s_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSChannel.add(_datal);
                                s_Channel = "";
                            } else {
                                list_in_DSChannel.add(_datal);
                                s_Channel = "";
                            }

                            //CTRL B LINK
                            _datal.PERFID = "CTRL B LINK";
                            _datal.PERFERMANCE = s_CTRL_B_LINK;
                            _datal.INFO = s_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSChannel.add(_datal);
                                s_CTRL_B_LINK = "";
                            } else {
                                list_in_DSChannel.add(_datal);
                                s_CTRL_B_LINK = "";
                            }

                            //port
                            _datal.PERFID = "port";
                            _datal.PERFERMANCE = s_port;
                            _datal.INFO = s_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSChannel.add(_datal);
                                s_port = "";
                            } else {
                                list_in_DSChannel.add(_datal);
                                s_port = "";
                            }

                            //status
                            _datal.PERFID = "status";
                            _datal.PERFERMANCE = s_status;
                            _datal.INFO = s_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSChannel.add(_datal);
                                s_status = "";
                            } else {
                                list_in_DSChannel.add(_datal);
                                s_status = "";
                            }

                            //time
                            _datal.PERFID = "time";
                            _datal.PERFERMANCE = s_time;
                            _datal.INFO = s_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSChannel.add(_datal);
                                s_time = "";
                            } else {
                                list_in_DSChannel.add(_datal);
                                s_time = "";
                            }

                        }
                    }

                }

            } else if (_mes.message_type1.equals("DSController")) {
                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2;

                if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {

                    list_up_DSController.add(_datal);
                } else {

                    String key3 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO;
                    if (!keyHash3.containsKey(key3)) {
                        keyHash3.put(key3, key3);
                        list_in_DSController.add(_datal);
                    } else {
                        continue;
                    }

                }
            } else if (_mes.message_type1.equals("DSDisk")) {
                boolean _sd = false;

                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);

                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = "";

                //去除无用的数据
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                _sd = true;
                            }
                        }
                    }
                }

                if (_sd) {

                    if (_mes.message_title.equals("SLOT")) {
                        d_SLOT = _mes.message_value;
                        continue;
                    }

                    if (_mes.message_title.equals("STATUS")) {
                        d_STATUS = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("TRAY")) {
                        d_TRAY = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("time")) {
                        d_time = _mes.message_value;
                        continue;
                    }

                    String str_info = "";
                    if (d_SLOT.length() > 0 & d_TRAY.length() > 0) {
                        str_info = d_TRAY + "/" + d_SLOT;
                        _datal.INFO = str_info;
                    }
                    if (_mes.message_title.equals("CAPACITY(GB)")) {
                        _datal.PERFID = "CAPACITY(GB)";
                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_DSDisk.add(_datal);
                        } else {

                            String key4 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO;
                            if (!keyHash4.containsKey(key4)) {
                                keyHash4.put(key4, key4);
                                list_in_DSDisk.add(_datal);
                            } else {
                                continue;
                            }

                        }
                    }

                    if (_mes.message_title.equals("CAPACITY(GB)")) {
                        if (d_STATUS.length() > 0 && d_time.length() > 0) {
                            String str_sql_k = "";

                            //SLOT
                            _datal.PERFID = "SLOT";
                            _datal.PERFERMANCE = d_SLOT;
                            _datal.INFO = str_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSDisk.add(_datal);
                                d_SLOT = "";
                            } else {
                                list_in_DSDisk.add(_datal);
                                d_SLOT = "";
                            }

                            //STATUS
                            _datal.PERFID = "STATUS";
                            _datal.PERFERMANCE = d_STATUS;
                            _datal.INFO = str_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSDisk.add(_datal);
                                d_STATUS = "";
                            } else {
                                list_in_DSDisk.add(_datal);
                                d_STATUS = "";
                            }

                            //TRAY
                            _datal.PERFID = "TRAY";
                            _datal.PERFERMANCE = d_TRAY;
                            _datal.INFO = str_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSDisk.add(_datal);
                                d_TRAY = "";
                            } else {
                                list_in_DSDisk.add(_datal);
                                d_TRAY = "";
                            }

                            //time
                            _datal.PERFID = "time";
                            _datal.PERFERMANCE = d_time;
                            _datal.INFO = str_info;

                            if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                                list_up_DSDisk.add(_datal);
                                d_time = "";
                            } else {
                                list_in_DSDisk.add(_datal);
                                d_time = "";
                            }

                        }
                    }

                }

            } else if (_mes.message_type1.equals("DSEnclosure")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2;

                if (_datal.PERFERMANCE == null || _datal.PERFERMANCE.toString().length() == 0) {
                    continue;
                }

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        _sd = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (_sd) {
                    //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                    if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                        list_up_DSEnclosure.add(_datal);
                    } else {

                        String key5 = _datal.NODEIP + "#" + _datal.PERFID + "#" + _datal.INFO;
                        if (!keyHash5.containsKey(key5)) {
                            keyHash5.put(key5, key5);
                            list_in_DSEnclosure.add(_datal);
                        } else {
                            continue;
                        }

                    }
                }

            } else if (_mes.message_type1.equals("DSEsm")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2.replace("DSEsm", "");

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        if (!_mes.message_type2.equals("DSEsm")) {
                                            _sd = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (_mes.message_title.equals("status")) {
                    a1_status = _mes.message_value;
                    continue;
                }

                if (_sd) {
                    _datal.INFO = _datal.INFO + "_" + _mes.message_value;
                    //System.out.println(_mes.message_type1 + "#1#" + _mes.message_type2 + "#2#" + _mes.message_title + "#3#" + _mes.message_value + "#4#" + _datal.INFO);

                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSEsm.add(_datal);
                    } else {
                        list_in_DSEsm.add(_datal);
                    }

                    //STATUS
                    _datal.PERFID = "status";
                    _datal.PERFERMANCE = a1_status;

                    String str_sql_k = "";
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSEsm.add(_datal);
                    } else {
                        list_in_DSEsm.add(_datal);
                    }

                }

            } else if (_mes.message_type1.equals("DSFans")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2.replace("DSFans", "");

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        if (!_mes.message_type2.equals("DSFans")) {
                                            _sd = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (_mes.message_title.equals("Status")) {
                    a1_status = _mes.message_value;
                    continue;
                }

                if (_sd) {
                    _datal.INFO = _datal.INFO + "_" + _mes.message_value;

                    //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSFans.add(_datal);
                    } else {
                        list_in_DSFans.add(_datal);
                    }

                    //STATUS
                    _datal.PERFID = "Status";
                    _datal.PERFERMANCE = a1_status;

                    String str_sql_k = "";
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSFans.add(_datal);
                    } else {
                        list_in_DSFans.add(_datal);
                    }

                }

            } else if (_mes.message_type1.equals("DSPowerFan")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2.replace("DSPowerFan", "");

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        if (!_mes.message_type2.equals("DSPowerFan")) {
                                            _sd = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (_mes.message_title.equals("status")) {
                    a1_status = _mes.message_value;
                    continue;
                }

                if (_sd) {
                    _datal.INFO = _datal.INFO + "_" + _mes.message_value;
                    //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSPowerFan.add(_datal);
                    } else {
                        list_in_DSPowerFan.add(_datal);
                    }

                    //STATUS
                    _datal.PERFID = "status";
                    _datal.PERFERMANCE = a1_status;

                    String str_sql_k = "";
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSPowerFan.add(_datal);
                    } else {
                        list_in_DSPowerFan.add(_datal);
                    }
                }

            } else if (_mes.message_type1.equals("DSPowerSupplies")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2.replace("DSPowerSupplies", "");

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        if (!_mes.message_type2.equals("DSPowerSupplies")) {
                                            _sd = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (_mes.message_title.equals("status")) {
                    a1_status = _mes.message_value;
                    continue;
                }

                if (_sd) {
                    _datal.INFO = _datal.INFO + "_" + _mes.message_value;
                    //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSPowerSupplies.add(_datal);
                    } else {
                        list_in_DSPowerSupplies.add(_datal);
                    }

                    //STATUS
                    _datal.PERFID = "status";
                    _datal.PERFERMANCE = a1_status;

                    String str_sql_k = "";
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSPowerSupplies.add(_datal);
                    } else {
                        list_in_DSPowerSupplies.add(_datal);
                    }
                }

            } else if (_mes.message_type1.equals("DSSFPs")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2.replace("DSSFPs", "");

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        if (!_mes.message_type2.equals("DSSFPs")) {
                                            _sd = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (_sd) {

                    if (!_mes.message_title.equals("status")) {
                        a1_info = _datal.INFO + "_" + _mes.message_value;
                        _datal.INFO = a1_info;
                    } else {
                        _datal.INFO = a1_info;
                        a1_info = "";
                    }

                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {

                        list_up_DSSFPs.add(_datal);

                    } else {
                        list_in_DSSFPs.add(_datal);
                    }
                }

            } else if (_mes.message_type1.equals("DSTemperature")) {

                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;
                _datal.INFO = _mes.message_type2.replace("DSTemperature", "");

                //去除无用的数据
                boolean _sd = false;
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                if (!_mes.message_title.equals("编号")) {
                                    if (!_mes.message_title.equals("time")) {
                                        if (!_mes.message_type2.equals("DSTemperature")) {
                                            _sd = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (_mes.message_title.equals("status")) {
                    a1_status = _mes.message_value;
                    continue;
                }

                if (_sd) {
                    _datal.INFO = _datal.INFO + "_" + _mes.message_value;
                    //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {

                        list_up_DSTemperature.add(_datal);
                    } else {

                        list_in_DSTemperature.add(_datal);
                    }

                    //STATUS
                    _datal.PERFID = "status";
                    _datal.PERFERMANCE = a1_status;

                    String str_sql_k = "";
                    if (db.return_node_s_ishas6("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _datal.TYPE, _csnms)) {
                        list_up_DSTemperature.add(_datal);
                    } else {
                        list_in_DSTemperature.add(_datal);
                    }

                }

            } else if (_mes.message_type1.equals("DSlogicDisk")) {
                boolean _sd = false;

                strclass._tb_shuguangDS_mes_de _datal = new strclass._tb_shuguangDS_mes_de();
                //System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);

                _datal.IFIDX = "1";

                if (_mes.message_nodeid.length() == 0) {
                    _datal.NODEID = "-1";
                } else {
                    _datal.NODEID = _mes.message_nodeid;
                }
                _datal.GETTIME = _mes.message_time;
                _datal.PERFID = _mes.message_title;
                _datal.PERFERMANCE = fun.return_value(_mes.message_title, _mes.message_value);
                _datal.NODEIP = _mes.message_ip;
                _datal.TYPE = _mes.message_type1;

                //去除无用的数据
                if (!_mes.message_title.equals("caption")) {
                    if (!_mes.message_title.equals("dotId")) {
                        if (!_mes.message_title.equals("entendCaption")) {
                            if (!_mes.message_title.equals("platform")) {
                                _sd = true;
                            }
                        }
                    }
                }

                if (_sd) {

                    if (_mes.message_title.equals("name")) {
                        a_name = _mes.message_value;
                        a_info = _mes.message_value;
                        continue;
                    }

                    if (_mes.message_title.equals("Array")) {
                        a_Array = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("raidLevel")) {
                        a_raidLevel = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("status")) {
                        a_status = _mes.message_value;
                        continue;
                    }
                    if (_mes.message_title.equals("time")) {
                        a_time = _mes.message_value;
                        continue;
                    }
                    _datal.INFO = a_info;

                    if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                        list_up_DSlogicDisk.add(_datal);
                    } else {
                        list_in_DSlogicDisk.add(_datal);
                    }

                    if (_mes.message_title.equals("capacity(GB)")) {
                        String str_sql_k = "";

                        //a_name
                        _datal.PERFID = "name";
                        _datal.PERFERMANCE = a_name;
                        _datal.INFO = a_info;

                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_DSlogicDisk.add(_datal);
                            a_name = "";
                        } else {
                            list_in_DSlogicDisk.add(_datal);
                            a_name = "";
                        }

                        //a_Array
                        _datal.PERFID = "Array";
                        _datal.PERFERMANCE = a_Array;
                        _datal.INFO = a_info;

                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_DSlogicDisk.add(_datal);
                            a_Array = "";
                        } else {
                            list_in_DSlogicDisk.add(_datal);
                            a_Array = "";
                        }

                        //raidLevel
                        _datal.PERFID = "raidLevel";
                        _datal.PERFERMANCE = a_raidLevel;
                        _datal.INFO = a_info;

                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_DSlogicDisk.add(_datal);
                            a_raidLevel = "";
                        } else {
                            list_in_DSlogicDisk.add(_datal);
                            a_raidLevel = "";
                        }

                        //status
                        _datal.PERFID = "status";
                        _datal.PERFERMANCE = a_status;
                        _datal.INFO = a_info;

                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_DSlogicDisk.add(_datal);
                            a_status = "";
                        } else {
                            list_in_DSlogicDisk.add(_datal);
                            a_status = "";
                        }

                        //time
                        _datal.PERFID = "time";
                        _datal.PERFERMANCE = a_time;
                        _datal.INFO = a_info;

                        if (db.return_node_s_ishas2("DSChannel_mes_de", _mes.message_ip, _datal.PERFID, _datal.INFO, _csnms)) {
                            list_up_DSlogicDisk.add(_datal);
                            a_time = "";
                        } else {
                            list_in_DSlogicDisk.add(_datal);
                            a_time = "";
                        }

                    }

                }
            }
        }

        if (list_in_ShuGuangDS.size() > 0) {
            db.tb_in_up_mes("in", list_in_ShuGuangDS, _csnms);
        }
        if (list_up_ShuGuangDS.size() > 0) {
            db.tb_in_up_mes("up", list_up_ShuGuangDS, _csnms);
        }

        if (list_in_DSChannel.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSChannel, _csnms, _tools);
        }
        if (list_up_DSChannel.size() > 0) {
            db.tb_in_up_mes_de("up", list_up_DSChannel, _csnms, _tools);
        }

        if (list_in_DSController.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSController, _csnms, _tools);
        }
        if (list_up_DSController.size() > 0) {
            db.tb_in_up_mes_de("up2", list_up_DSController, _csnms, _tools);
        }

        if (list_in_DSDisk.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSDisk, _csnms, _tools);
        }
        if (list_up_DSDisk.size() > 0) {
            db.tb_in_up_mes_de("up3", list_up_DSDisk, _csnms, _tools);
        }

        if (list_in_DSEnclosure.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSEnclosure, _csnms, _tools);
        }
        if (list_up_DSEnclosure.size() > 0) {
            db.tb_in_up_mes_de("up4", list_up_DSEnclosure, _csnms, _tools);
        }

        if (list_in_DSEsm.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSEsm, _csnms, _tools);
        }
        if (list_up_DSEsm.size() > 0) {
            db.tb_in_up_mes_de("up5", list_up_DSEsm, _csnms, _tools);
        }

        if (list_in_DSFans.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSFans, _csnms, _tools);
        }
        if (list_up_DSFans.size() > 0) {
            db.tb_in_up_mes_de("up6", list_up_DSFans, _csnms, _tools);
        }

        if (list_in_DSPowerFan.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSPowerFan, _csnms, _tools);
        }
        if (list_up_DSPowerFan.size() > 0) {
            db.tb_in_up_mes_de("up7", list_up_DSPowerFan, _csnms, _tools);
        }

        if (list_in_DSPowerSupplies.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSPowerSupplies, _csnms, _tools);
        }
        if (list_up_DSPowerSupplies.size() > 0) {
            db.tb_in_up_mes_de("up8", list_up_DSPowerSupplies, _csnms, _tools);
        }

        if (list_in_DSSFPs.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSSFPs, _csnms, _tools);
        }
        if (list_up_DSSFPs.size() > 0) {
            db.tb_in_up_mes_de("up9", list_up_DSSFPs, _csnms, _tools);
        }

        if (list_in_DSTemperature.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSTemperature, _csnms, _tools);
        }
        if (list_up_DSTemperature.size() > 0) {
            db.tb_in_up_mes_de("up10", list_up_DSTemperature, _csnms, _tools);
        }

        if (list_in_DSlogicDisk.size() > 0) {
            db.tb_in_up_mes_de("in", list_in_DSlogicDisk, _csnms, _tools);
        }
        if (list_up_DSlogicDisk.size() > 0) {
            db.tb_in_up_mes_de("up11", list_up_DSlogicDisk, _csnms, _tools);
        }

    }

}
