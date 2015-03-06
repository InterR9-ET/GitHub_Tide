/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.alarm_hecha;

import z.allprocess.*;
import z.xn_transperwbs.*;
import z.xn_transper.*;
import z.xn_port.*;
import z.send_sms.*;
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
    //-------------------------------加载数据库----------------------------------

    public static boolean ini(util.GetSql.csnms _csnms, util.GetSql.zhizhen _zhizhen, util.GetSql.ess _ess) {
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

    //--------------------------------------------------------------
    public static strclass.ALARM_MES get_alarm(strclass.ALARM_MES _mes, util.GetSql.csnms _csnms) {
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

    //--------------------------------------------------------------
    public static List get_zhizhen_pathtrace(strclass.ALARM_MES _alarm, util.GetSql.zhizhen _zhizhen) {
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

    public static List get_ess_path(strclass.ALARM_MES _alarm, util.GetSql.ess _ess) {
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
/**
 * @param _csnms--------------------------------------------------数据库
 * @param _mes----------------------------------------------------要查的路由信息
 * @return 
 */
    public static int seach_has_node(util.GetSql.csnms _csnms, strclass.ALARM_MES _mes) {
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

    public static int seach_has_pathtrace_slot(util.GetSql.csnms _csnms, String _node_id, String _slot) {
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

    public static int seach_has_pathtrace_slot_port(util.GetSql.csnms _csnms, String _node_id, String _slot, String _port) {
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

    public static List get_dk_pathtrace(strclass.ALARM_MES _alarm, util.GetSql.csnms _csnms) {
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

    
    
}



