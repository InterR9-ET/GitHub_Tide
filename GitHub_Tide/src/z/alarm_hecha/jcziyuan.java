/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.alarm_hecha;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author yangzhen
 *
 */
public class jcziyuan extends Thread {

    private static GetSql.csnms _csnms = new GetSql.csnms();
    private static GetSql.zhizhen _zhizhen = new GetSql.zhizhen();
    private static GetSql.ess _ess = new GetSql.ess();

    private static GetTools.tools _tools = new GetTools.tools();
    private static GetFile.txt _txt = new GetFile.txt();

    public static Hashtable _data = new Hashtable();

    public static boolean ini() {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            } else {
                _bs = false;
                System.out.println("_csnms打开失败");
            }
        } else {
            _bs = false;
        }

        if (_zhizhen.load()) {
            if (_zhizhen.open()) {
                _bs = true;
            } else {
                _bs = false;
                System.out.println("_zhizhen打开失败");
            }
        } else {
            _bs = false;
        }

        if (_ess.load()) {
            if (_ess.open()) {
                _bs = true;
            } else {
                _bs = false;
                System.out.println("_ess打开失败");
            }
        } else {
            _bs = false;
        }

        return _bs;
    }

    public void run(strclass.ALARM_MES _mes) {
        if (ini()) {
            //加载告警信息
            if (_mes.SE_ALARM_ID.length() > 0) {
                _mes = get_alarm(_mes);
            }
            //比对数据问题            
            com_dake(_mes);
        } else {
            System.out.println("数据库连接失败");
        }
    }

    public static ALARM_MES get_alarm(ALARM_MES _mes) {
        List _list = new ArrayList();
        String str_sql = "";
        if (_mes.SE_ALARM_MONTH.equals("0")) {
            str_sql = "select "
                    + "s.id as ID,"
                    + "s.alarm_type as ALARM_TYPE,"
                    + "s.path_id as PATH_ID,"
                    + "(select  p.name  from  path  p where p.path_id=s.path_id) as PATH_NAME,"
                    + "s.node_name as NODE_NAME,"
                    + "s.node_id as NODE_ID,"
                    + "s.slot_id as NODE_SLOT,"
                    + "s.port_id as NODE_PORT, "
                    + "s.description as DESCRIPTION,"
                    + "s.alarm_start as ALARM_START,"
                    + "s.alarm_update as ALARM_UPDATE  "
                    + " from  alarm  s where  1=1 ";
        } else {
            str_sql = "select "
                    + "s.id as ID,"
                    + "s.alarm_type as ALARM_TYPE,"
                    + "s.path_id as PATH_ID,"
                    + "(select  p.name  from  path  p where p.path_id=s.path_id) as PATH_NAME,"
                    + "s.node_name as NODE_NAME,"
                    + "s.node_id as NODE_ID,"
                    + "s.slot_id as NODE_SLOT,"
                    + "s.port_id as NODE_PORT, "
                    + "s.description as DESCRIPTION,"
                    + "s.alarm_start as ALARM_START,"
                    + "s.alarm_update as ALARM_UPDATE  "
                    + "from  historyalarm_" + _mes.SE_ALARM_MONTH + "  s  where  1=1 ";
        }
        if (str_sql.length() > 0) {
            if (_mes.SE_ALARM_ID.length() > 0) {
                str_sql = str_sql + " and s.id=" + _mes.SE_ALARM_ID;
            }
            if (_mes.SE_ALARM_SER_ID.length() > 0) {
                str_sql = str_sql + " and s.description like '%" + _mes.SE_ALARM_SER_ID + "%'";
            }
            //System.out.println(str_sql);
            _list = _csnms.getdata(str_sql, null);

            if (_list.size() > 0) {
                for (int i = 0, m = _list.size(); i < m; i++) {
                    HashMap map = (HashMap) _list.get(i);
                    if (map.get("ID") != null) {
                        _mes.ALARM_ID = map.get("ID").toString();
                    }

                    if (map.get("ALARM_TYPE") != null) {
                        _mes.ALARM_TYPE = map.get("ALARM_TYPE").toString();
                    }

                    if (map.get("PATH_ID") != null) {
                        _mes.PATH_ID = map.get("PATH_ID").toString();
                    }

                    if (map.get("NODE_NAME") != null) {
                        _mes.NODE_NAME = map.get("NODE_NAME").toString();
                    }

                    if (map.get("NODE_ID") != null) {
                        _mes.NODE_ID = map.get("NODE_ID").toString();
                    }

                    if (map.get("NODE_SLOT") != null) {
                        _mes.NODE_SLOT = map.get("NODE_SLOT").toString();
                    }

                    if (map.get("NODE_PORT") != null) {
                        _mes.NODE_PORT = map.get("NODE_PORT").toString();
                    }

                    if (map.get("DESCRIPTION") != null) {
                        _mes.DESCRIPTION = map.get("DESCRIPTION").toString();
                    }

                    if (map.get("ALARM_START") != null) {
                        _mes.ALARM_START = map.get("ALARM_START").toString();
                    }

                    if (map.get("ALARM_UPDATE") != null) {
                        _mes.ALARM_UPDATE = map.get("ALARM_UPDATE").toString();
                    }

                    if (map.get("PATH_NAME") != null) {
                        _mes.PATH_NAME = map.get("PATH_NAME").toString();
                    }
                }
            }
        }
        return _mes;
    }

    public static void com_dake(ALARM_MES _mes) {

        StringBuffer _str = new StringBuffer();
        _str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
        _str.append("<告警核查>").append("\r\n");

        _str.append("<告警信息>").append("\r\n");
        if (_mes.SE_ALARM_ID.length() > 0) {
            _str.append(get_alarmm_xml(_mes));
        }
        _str.append("</告警信息>").append("\r\n");

        _str.append("<大客路由信息>").append("\r\n");
        _str.append(get_dk_xml(_mes));
        _str.append("</大客路由信息>").append("\r\n");

        _str.append("<ESS电路信息>").append("\r\n");
        _str.append(get_ess_xml(_mes));
        _str.append("</ESS电路信息>").append("\r\n");

        _str.append("<直真路由信息>").append("\r\n");
        _str.append(get_zhizhen_xml(_mes));
        _str.append("</直真路由信息>").append("\r\n");

        _str.append("</告警核查>").append("\r\n");

        String file_path = "C:\\Users\\zhen\\Desktop\\com_alarm.xml";
        boolean _bs = _txt.create_file(file_path);
        if (_bs) {
            _txt.write_file(file_path, _str.toString());
            try {
                java.awt.Desktop.getDesktop().open(new File(file_path));
            } catch (Exception ex) {
                System.out.println("调用默认程序打开程序失败:" + ex.getMessage());
            }
        }

    }

    private static String get_alarmm_xml(ALARM_MES _mes) {

        int node_count = seach_has_node(_csnms, _mes);
        if (node_count == 0) {
            _mes.NODE_MES = "大客无此设备";
        } else {
            _mes.NODE_MES = "大客匹配正常";
        }

        //设备正常  核查slot
        if (node_count > 0) {
            int slot_count = seach_has_pathtrace_slot(_csnms, _mes.NODE_ID, _mes.NODE_SLOT);
            if (slot_count == 0) {
                _mes.SLOT_MES = "大客未匹配到SLOT";
            } else {
                _mes.SLOT_MES = "大客匹配正常";
                //设备正常  slot正常   核查port
                int port_count = seach_has_pathtrace_slot_port(_csnms, _mes.NODE_ID, _mes.NODE_SLOT, _mes.NODE_PORT);
                if (port_count == 0) {
                    _mes.PORT_MES = "大客未匹配到PORT";
                } else {
                    _mes.PORT_MES = "大客匹配正常";
                }
            }
        }

        StringBuffer _str = new StringBuffer();

        _str.append("<Alarm_ID>").append(str_rel(_mes.ALARM_ID)).append("</Alarm_ID>").append("\r\n");
        _str.append("<Alarm_TYPE>").append(str_rel(_mes.ALARM_TYPE)).append("</Alarm_TYPE>").append("\r\n");
        _str.append("<DESCRIPTION>").append(str_rel(_mes.DESCRIPTION)).append("</DESCRIPTION>").append("\r\n");
        _str.append("<ALARM_START>").append(str_rel(_mes.ALARM_START)).append("</ALARM_START>").append("\r\n");
        _str.append("<ALARM_UPDATE>").append(str_rel(_mes.ALARM_UPDATE)).append("</ALARM_UPDATE>").append("\r\n");

        _str.append("<Path>").append("\r\n");
        _str.append("<PATH_ID>").append(str_rel(_mes.PATH_ID)).append("</PATH_ID>").append("\r\n");
        _str.append("<PATH_NAME>").append(str_rel(_mes.PATH_NAME)).append("</PATH_NAME>").append("\r\n");
        _str.append("</Path>").append("\r\n");

        _str.append("<NODE>").append("\r\n");
        _str.append("<NODE_ID>").append(str_rel(_mes.NODE_ID)).append("</NODE_ID>").append("\r\n");
        _str.append("<NODE_NAME>").append(str_rel(_mes.NODE_NAME)).append("</NODE_NAME>").append("\r\n");
        _str.append("<SHORT_NAME>").append(str_rel(_mes.SHORT_NAME)).append("</SHORT_NAME>").append("\r\n");

        _str.append("<NODE_SLOT>").append(str_rel(_mes.NODE_SLOT)).append("</NODE_SLOT>").append("\r\n");
        _str.append("<NODE_PORT>").append(str_rel(_mes.NODE_PORT)).append("</NODE_PORT>").append("\r\n");
        _str.append("</NODE>").append("\r\n");

        _str.append("<NODE_MES>").append(str_rel(_mes.NODE_MES)).append("</NODE_MES>").append("\r\n");
        _str.append("<SLOT_MES>").append(str_rel(_mes.SLOT_MES)).append("</SLOT_MES>").append("\r\n");
        _str.append("<PORT_MES>").append(str_rel(_mes.PORT_MES)).append("</PORT_MES>").append("\r\n");

        return _str.toString();
    }

    private static String get_ess_xml(ALARM_MES _alarm) {
        StringBuffer _str = new StringBuffer();
        List _list_dk = get_ess_path(_alarm);
        int m_dk = _list_dk.size();
        if (m_dk > 0) {
            _str.append("<电路>").append("\r\n");
            for (int i = 0; i < m_dk; i++) {
                HashMap map = (HashMap) _list_dk.get(i);

                _str.append("<PATH ");
                _str.append(" 编号=\"");
                if (map.get("CIRCD") != null) {
                    _str.append(str_rel(map.get("CIRCD").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("CIRNAME") != null) {
                    _str.append(str_rel(map.get("CIRNAME").toString()));
                }
                _str.append("\"");

                _str.append(" A端客户=\"");
                if (map.get("ACUSTOMID") != null) {
                    String name = "[" + map.get("ACUSTOMID").toString() + "]";
                    if (map.get("ANAME") != null) {
                        name += map.get("ANAME").toString();
                    }
                    _str.append(str_rel(name));
                }
                _str.append("\"");

                _str.append(" Z端客户=\"");
                if (map.get("ZCUSTOMID") != null) {
                    String name = "[" + map.get("ZCUSTOMID").toString() + "]";
                    if (map.get("ZNAME") != null) {
                        name += map.get("ZNAME").toString();
                    }
                    _str.append(str_rel(name));
                }
                _str.append("\"");

                _str.append(" />").append("\r\n");
            }
            _str.append("</电路>").append("\r\n");
        }
        return _str.toString();
    }

    private static String get_zhizhen_xml(ALARM_MES _alarm) {
        StringBuffer _str = new StringBuffer();
        List _list_dk = get_zhizhen_pathtrace(_alarm);
        int m_dk = _list_dk.size();
        if (m_dk > 0) {
            _str.append("<路由>").append("\r\n");
            for (int i = 0; i < m_dk; i++) {
                HashMap map = (HashMap) _list_dk.get(i);

                _str.append("<SERIA ");
                _str.append(" 序号=\"");
                if (map.get("SEQUENCE") != null) {
                    _str.append(str_rel(map.get("SEQUENCE").toString()));
                }
                _str.append("\"");

                _str.append(" 电路=\"");
                if (map.get("PATH_NAME") != null) {
                    _str.append(str_rel(map.get("PATH_NAME").toString()));
                }
                _str.append("\"");
                _str.append(" >");

                _str.append("<ANODE ");
                _str.append(" 编号=\"");
                if (map.get("ANODE_ID") != null) {
                    _str.append(str_rel(map.get("ANODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ANODE_NAME") != null) {
                    _str.append(str_rel(map.get("ANODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 类型=\"");
                if (map.get("ANODE_TYPE") != null) {
                    _str.append(str_rel(map.get("ANODE_TYPE").toString()));
                }
                _str.append("\"");

                _str.append(" 信息=\"");
                if (map.get("ANODE_MES") != null) {
                    _str.append(str_rel(map.get("ANODE_MES").toString()));
                }
                _str.append("\"");
                _str.append(" />");

                _str.append("<ZNODE ");
                _str.append(" 编号=\"");
                if (map.get("ZNODE_ID") != null) {
                    _str.append(str_rel(map.get("ZNODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ZNODE_NAME") != null) {
                    _str.append(str_rel(map.get("ZNODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 类型=\"");
                if (map.get("ZNODE_TYPE") != null) {
                    _str.append(str_rel(map.get("ZNODE_TYPE").toString()));
                }
                _str.append("\"");

                _str.append(" 信息=\"");
                if (map.get("ZNODE_MES") != null) {
                    _str.append(str_rel(map.get("ZNODE_MES").toString()));
                }
                _str.append("\"");
                _str.append(" />");

                _str.append("</SERIA>").append("\r\n");
            }
            _str.append("</路由>").append("\r\n");
        }
        return _str.toString();
    }

    private static String get_dk_xml(ALARM_MES _alarm) {
        StringBuffer _str = new StringBuffer();
        List _list_dk = get_dk_pathtrace(_alarm);
        int m_dk = _list_dk.size();
        if (m_dk > 0) {
            _str.append("<路由>").append("\r\n");
            for (int i = 0; i < m_dk; i++) {
                HashMap map = (HashMap) _list_dk.get(i);

                _str.append("<SERIA ");
                _str.append(" 序号=\"");
                if (map.get("SERIAL_ID") != null) {
                    _str.append(str_rel(map.get("SERIAL_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 电路编号=\"");
                if (map.get("PATH_ID") != null) {
                    _str.append(str_rel(map.get("PATH_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 电路=\"");
                if (map.get("PATH_NAME") != null) {
                    _str.append(str_rel(map.get("PATH_NAME").toString()));
                }
                _str.append("\"");
                _str.append(" >");

                _str.append("<ANODE ");
                _str.append(" 编号=\"");
                if (map.get("ANODE_ID") != null) {
                    _str.append(str_rel(map.get("ANODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ANODE_NAME") != null) {
                    _str.append(str_rel(map.get("ANODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 简称=\"");
                if (map.get("ASHORT_NAME") != null) {
                    _str.append(str_rel(map.get("ASHORT_NAME").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" SHELF_ID=\"");
                 if (map.get("ASHELF_ID") != null) {
                 _str.append(str_rel(map.get("ASHELF_ID").toString())).append("\"");
                 }*/
                _str.append(" 板卡=\"");
                if (map.get("ASLOT_ID") != null) {
                    _str.append(str_rel(map.get("ASLOT_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 端口=\"");
                if (map.get("APORT_ID") != null) {
                    _str.append(str_rel(map.get("APORT_ID").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" 时隙=\"");
                 if (map.get("ATIMESLOT") != null) {
                 _str.append(str_rel(map.get("ATIMESLOT").toString()));
                 }
                 _str.append("\"");
                 */
                _str.append(" />");

                _str.append("<ZNODE ");
                _str.append(" 编号=\"");
                if (map.get("ZNODE_ID") != null) {
                    _str.append(str_rel(map.get("ZNODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ZNODE_NAME") != null) {
                    _str.append(str_rel(map.get("ZNODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 简称=\"");
                if (map.get("ZSHORT_NAME") != null) {
                    _str.append(str_rel(map.get("ZSHORT_NAME").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" SHELF_ID=\"");
                 if (map.get("ZSHELF_ID") != null) {
                 _str.append(str_rel(map.get("ZSHELF_ID").toString()));
                 }
                 _str.append("\"");
                 */
                _str.append(" 板卡=\"");
                if (map.get("ZSLOT_ID") != null) {
                    _str.append(str_rel(map.get("ZSLOT_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 端口=\"");
                if (map.get("ZPORT_ID") != null) {
                    _str.append(str_rel(map.get("ZPORT_ID").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" TIMESLOT=\"");
                 if (map.get("ZTIMESLOT") != null) {
                 _str.append(str_rel(map.get("ZTIMESLOT").toString()));
                 }
                 _str.append("\"");
                 */
                _str.append("/>");

                _str.append("</SERIA>").append("\r\n");
            }
            _str.append("</路由>").append("\r\n");
        }
        return _str.toString();
    }

    private static List get_ess_path(ALARM_MES _alarm) {
        List _list = new ArrayList();
        if (_alarm.SE_PATH_NAME.length() > 0) {
            String str_sql = "select "
                    + " i.circd as CIRCD,"
                    + " i.cirname as CIRNAME , "
                    + " i.aincustomid as ACUSTOMID,"
                    + " (select  k.customname  from intf_custom_info k  where k.incustomid=i.aincustomid )  as ANAME,"
                    + " i.zincustomid as ZCUSTOMID,"
                    + " (select  k.customname  from intf_custom_info k  where k.incustomid=i.zincustomid )  as ZNAME "
                    + " from intf_cir_info i "
                    + " where  "
                    + " i.circd = '" + _alarm.SE_PATH_NAME + "' "
                    + " or "
                    + " i.cirname = '" + _alarm.SE_PATH_NAME + "'";
            _list = _ess.getdata(str_sql, null);
        } else if (_alarm.SE_PATH_NAME.length() == 0 && _alarm.PATH_NAME.length() > 0) {
            String str_sql = "select "
                    + " i.circd as CIRCD,"
                    + " i.cirname as CIRNAME , "
                    + " i.aincustomid as ACUSTOMID,"
                    + " (select  k.customname  from intf_custom_info k  where k.incustomid=i.aincustomid )  as ANAME,"
                    + " i.zincustomid as ZCUSTOMID,"
                    + " (select  k.customname  from intf_custom_info k  where k.incustomid=i.zincustomid )  as ZNAME "
                    + " from intf_cir_info i "
                    + " where  "
                    + " i.circd = '" + _alarm.PATH_NAME + "' "
                    + " or "
                    + " i.cirname = '" + _alarm.PATH_NAME + "'";
            _list = _ess.getdata(str_sql, null);
        }
        return _list;
    }

    private static List get_zhizhen_pathtrace(ALARM_MES _alarm) {
        List _list = new ArrayList();
        if (_alarm.SE_PATH_NAME.length() > 0 || _alarm.PATH_ID.length() > 0) {
            String str_sql = "select "
                    + " rc.domain as CITY, "
                    + " rc.circuitname as PATH_NAME,"
                    + " cr.sequence as SEQUENCE,"
                    + " cr.aendmeid as ANODE_ID,"
                    + " cr.aendmename as ANODE_NAME,"
                    + " cr.aendmemodel as ANODE_TYPE,"
                    + " cr.aendptp as ANODE_MES,"
                    + " cr.zendmeid as ZNODE_ID,"
                    + " cr.zendmename as ZNODE_NAME,"
                    + " cr.zendmemodel as ZNODE_TYPE,"
                    + " cr.zendptp as ZNODE_MES"
                    + " from tnmsdb2.rms_circuit rc"
                    + " left join tnmsdb2.rms_circuit_route cr"
                    + " on rc.circuitid = cr.circuitid"
                    + " where rc.isdelete = 0";
            str_sql = str_sql + "  and rc.circuitname = '" + _alarm.SE_PATH_NAME + "'";
            str_sql = str_sql + " order  by  cr.sequence  ";
            _list = _zhizhen.getdata(str_sql, null);
        } else if (_alarm.SE_PATH_NAME.length() == 0 && _alarm.PATH_NAME.length() > 0) {
            String str_sql = "select "
                    + " rc.domain as CITY, "
                    + " rc.circuitname as PATH_NAME,"
                    + " cr.sequence as SEQUENCE,"
                    + " cr.aendmeid as ANODE_ID,"
                    + " cr.aendmename as ANODE_NAME,"
                    + " cr.aendmemodel as ANODE_TYPE,"
                    + " cr.aendptp as ANODE_MES,"
                    + " cr.zendmeid as ZNODE_ID,"
                    + " cr.zendmename as ZNODE_NAME,"
                    + " cr.zendmemodel as ZNODE_TYPE,"
                    + " cr.zendptp as ZNODE_MES"
                    + " from tnmsdb2.rms_circuit rc"
                    + " left join tnmsdb2.rms_circuit_route cr"
                    + " on rc.circuitid = cr.circuitid"
                    + " where rc.isdelete = 0";
            str_sql = str_sql + "  and rc.circuitname = '" + _alarm.PATH_NAME + "'";
            str_sql = str_sql + " order  by  cr.sequence  ";
            _list = _zhizhen.getdata(str_sql, null);
        }

        return _list;
    }

    private static List get_dk_pathtrace(ALARM_MES _alarm) {
        List _list = new ArrayList();
        if (_alarm.SE_PATH_NAME.length() > 0 || _alarm.PATH_ID.length() > 0) {
            String str_sql = "select   "
                    + " p1.path_id as PATH_ID " + ","
                    + "(select  p.name  from  path  p where p.path_id=p1.path_id) as PATH_NAME,"
                    + " p1.serial_id as SERIAL_ID " + ","
                    + "(select  n.full_name  from  node   n  where  n.node_id=p1.anode_id) as ANODE_NAME,"
                    + "(select  n.short_name  from  node   n  where  n.node_id=p1.anode_id) as ASHORT_NAME,"
                    + " p1.anode_id as ANODE_ID " + ","
                    + " p1.ashelf_id as ASHELF_ID " + ","
                    + " p1.aslot_id as ASLOT_ID " + ","
                    + " p1.aport_id as APORT_ID " + ","
                    + " p1.atimeslot_id as ATIMESLOT " + ","
                    + "(select  n.full_name   from  node   n  where  n.node_id=p1.znode_id) as ZNODE_NAME,"
                    + "(select  n.short_name  from  node   n  where  n.node_id=p1.anode_id) as ZSHORT_NAME,"
                    + " p1.znode_id as ZNODE_ID " + ","
                    + " p1.zshelf_id as ZSHELF_ID " + ","
                    + " p1.zslot_id as ZSLOT_ID " + ","
                    + " p1.zport_id as ZPORT_ID " + ","
                    + " p1.ztimeslot_id as ZTIMESLOT "
                    + " from pathtrace  p1 ,path  p2  "
                    + " where  p1.path_id=p2.path_id   and  ";
            if (_alarm.SE_PATH_NAME.length() > 0) {
                str_sql = str_sql + " p2.name='" + _alarm.SE_PATH_NAME + "'";
                if (_alarm.PATH_ID.length() > 0) {
                    str_sql = str_sql + " or p2.path_id=" + _alarm.PATH_ID + " ";
                }
            } else {
                str_sql = str_sql + "  p2.path_id=" + _alarm.PATH_ID + " ";
            }
            str_sql = str_sql + " order  by  p1.serial_id  ";
            _list = _csnms.getdata(str_sql, null);
        }
        return _list;
    }

    private static String str_rel(String str_con) {

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

    private static int seach_has_node(GetSql.csnms _csnms, ALARM_MES _mes) {
        int count = 0;
        if (_mes.NODE_ID.length() > 0) {
            String str_sql = " select  count(1) as COUNT from   pathtrace n  where  n.anode_id  =" + _mes.NODE_ID + " or  n.znode_id  =" + _mes.NODE_ID;
            List _list = _csnms.getdata(str_sql, null);
            if (_list.size() > 0) {
                HashMap map = (HashMap) _list.get(0);
                try {
                    String _count = map.get("COUNT").toString();
                    count = Integer.parseInt(_count);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return count;
    }

    private static int seach_has_pathtrace_slot(GetSql.csnms _csnms, String _node_id, String _slot) {
        int count = 0;
        String str_sql = " select  count(1) as COUNT from   pathtrace p  "
                + " where  "
                + " (p.anode_id=" + _node_id + " and p.aslot_id=" + _slot + " )  "
                + " or  "
                + " (p.znode_id=" + _node_id + " and p.zslot_id=" + _slot + ")  ";
        //System.out.println(str_sql);
        List _list = _csnms.getdata(str_sql, null);
        if (_list.size() > 0) {
            HashMap map = (HashMap) _list.get(0);
            try {
                String _count = map.get("COUNT").toString();
                count = Integer.parseInt(_count);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return count;
    }

    private static int seach_has_pathtrace_slot_port(GetSql.csnms _csnms, String _node_id, String _slot, String _port) {
        int count = 0;
        String str_sql = " select  count(1) as COUNT from   pathtrace p  "
                + " where  "
                + " (p.anode_id=" + _node_id + " and p.aslot_id=" + _slot + "  and p.aport_id=" + _port + ")  "
                + " or  "
                + " (p.znode_id=" + _node_id + " and p.zslot_id=" + _slot + "  and p.zport_id=" + _port + ")  ";
        //System.out.println(str_sql);
        List _list = _csnms.getdata(str_sql, null);
        if (_list.size() > 0) {
            HashMap map = (HashMap) _list.get(0);
            try {
                String _count = map.get("COUNT").toString();
                count = Integer.parseInt(_count);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return count;
    }

}
