/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetSocket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author yangzhen
 */
public class alarms {

    private List _list_node = list_node_gl();

    private class Alarm {

        String alarm_id = "1";
        String type = "0";
        String alarm_type = "";
        String alarm_status = "";
        String event_name = "";
        String node_id = "";
        String node_name = "";
        String path_id = "-1";
        String shelf_id = "-1";
        String slot_id = "-1";
        String port_id = "-1";
        String description = "";//<私网设备告警>设备名称:JS-NJ-ZYL-BS.DCN.C6509-2;设备IP:132.228.42.2;检测时间:2014-03-28 13:10:53;检测值:80;超过最大阀值:20,
        String alm_Con = "";
        String alarm_level = "";
        String vendor_type = "";
        String debug_alarm_time = "";
        String alarm_update = "";

    }

    public String return_alarm_path_open(String nodename, String nodeid, String pathid, int _alarmlevel, String _slot, String _port, String _value, String _alm_Con, String _alarm_type, String str_desc,util.GetSql.csnms _csnms) {


        Alarm _alarm = new Alarm();
        /*
         ALARMLEVEL.put("1", "CRITICAL");
         ALARMLEVEL.put("2", "MAJOR");
         ALARMLEVEL.put("3", "MINOR");
         ALARMLEVEL.put("4", "WARNING");
         */

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE  from  node  n   where  n.node_id  =" + nodeid;

        List ls_s = new ArrayList();
        Object[] objs = new Object[]{};
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
        }

        //更改nodeid    
        /*
         String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew.equals(kk)) {
         nodeid = nodeid.substring(0, nodeid.length() - 4);
         break;
         }
         }
         */
        //更改pathod
        String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew2.equals(kk)) {
                pathid = pathid.substring(0, pathid.length() - 4);
                break;
            }
        }

        _alarm.node_name = nodename;
        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*电路告警  */
        StringBuffer sb = new StringBuffer();
        sb.append("type=0")
                .append(",event_name=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=OPEN")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port)
                .append(",path_id=").append(_alarm.path_id);
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public String return_alarm_path_clear(String nodename, String nodeid, String pathid, int _alarmlevel, String _slot, String _port, String _value, String _alm_Con, String _alarm_type, String str_desc,util.GetSql.csnms _csnms) {

        Alarm _alarm = new Alarm();
        /*
         ALARMLEVEL.put("1", "CRITICAL");
         ALARMLEVEL.put("2", "MAJOR");
         ALARMLEVEL.put("3", "MINOR");
         ALARMLEVEL.put("4", "WARNING");
         */

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE,n.full_name as NODE_NAME  from  node  n   where  n.node_id  =" + nodeid;

        List ls_s = new ArrayList();
        Object[] objs = new Object[]{};
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
            _alarm.node_name = map.get("NODE_NAME").toString();
        }

        //更改nodeid    
        /*
         String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew.equals(kk)) {
         nodeid = nodeid.substring(0, nodeid.length() - 4);
         break;
         }
         }
         */
        //更改pathod
        String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew2.equals(kk)) {
                pathid = pathid.substring(0, pathid.length() - 4);
                break;
            }
        }

        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*电路告警  */
        StringBuffer sb = new StringBuffer();
        sb.append("type=0")
                .append(",event_name=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=CLEAR")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port)
                .append(",path_id=").append(_alarm.path_id);

        //System.out.println(sb.toString());
        return sb.toString();
    }

    public String return_alarm_device_open(String nodeid, String pathid, int _alarmlevel, String _alm_Con, String _alarm_type, String str_desc, String _slot, String _port,util.GetSql.csnms _csnms) {

        Alarm _alarm = new Alarm();
        /*
         "1", "CRITICAL" 
         "2", "MAJOR"
         "3", "MINOR"
         "4", "WARNING"
         */

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE,n.full_name as NODE_NAME  from  node  n   where  n.node_id  =" + nodeid;
        List ls_s = new ArrayList();
        Object[] objs = new Object[]{};
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
            _alarm.node_name = map.get("NODE_NAME").toString();
        }

        //更改nodeid    
        String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew.equals(kk)) {
                nodeid = nodeid.substring(0, nodeid.length() - 4);
                break;
            }
        }

        //更改pathod
         /*
         String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());
         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew2.equals(kk)) {
         pathid = pathid.substring(0, pathid.length() - 4);
         break;
         }
         }
         */
        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*设备告警  */
        StringBuffer sb = new StringBuffer();

        sb.append("type=0")
                .append(",alarm_status=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=OPEN")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",_vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port);

        return sb.toString();
    }

    public String return_alarm_device_clear(String nodeid, String pathid, int _alarmlevel, String _alm_Con, String _alarm_type, String str_desc, String _slot, String _port,util.GetSql.csnms _csnms) {


        Alarm _alarm = new Alarm();

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE,n.full_name as NODE_NAME  from  node  n   where  n.node_id  =" + nodeid;
        List ls_s = new ArrayList();
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
            _alarm.node_name = map.get("NODE_NAME").toString();
        }

        //更改nodeid    
        String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew.equals(kk)) {
                nodeid = nodeid.substring(0, nodeid.length() - 4);
                break;
            }
        }

        //更改pathod
         /*
         String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());

         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew2.equals(kk)) {
         pathid = pathid.substring(0, pathid.length() - 4);
         break;
         }
         }
         */
        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*设备告警  */
        StringBuffer sb = new StringBuffer();

        sb.append("type=0")
                .append(",event_name=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=CLEAR")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",_vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port);

        //System.out.println(sb.toString());
        return sb.toString();
    }

    public boolean sendAlarm(String alarm) {
        boolean _bs = false;
        _bs = socketclient.sendmessage(alarm);
        return _bs;
    }

    public static List list_node_gl() {
        List _list = new ArrayList();
        _list.add("0082");
        _list.add("0070");
        _list.add("0073");
        _list.add("0006");
        _list.add("0007");
        _list.add("0052");
        _list.add("0086");
        _list.add("0277");
        _list.add("0276");
        _list.add("0272");
        _list.add("0079");
        _list.add("0017");
        _list.add("0048");
        _list.add("0035");
        _list.add("0025");
        _list.add("0273");
        _list.add("0076");
        _list.add("0074");
        _list.add("0087");
        _list.add("0005");
        _list.add("0016");
        _list.add("0058");
        _list.add("0050");
        _list.add("0008");
        _list.add("0084");
        _list.add("0072");
        _list.add("0284");
        _list.add("0003");
        _list.add("0001");
        _list.add("0002");
        _list.add("0085");
        _list.add("0102");
        _list.add("0029");
        _list.add("0091");
        _list.add("0027");
        _list.add("0047");
        _list.add("0099");
        _list.add("0275");
        _list.add("0078");
        _list.add("0274");
        _list.add("0024");
        _list.add("0051");
        _list.add("0282");
        _list.add("0301");
        _list.add("0022");
        _list.add("0020");
        _list.add("0083");
        _list.add("0032");
        _list.add("0018");
        _list.add("0270");
        _list.add("0081");
        _list.add("0013");
        _list.add("0014");
        _list.add("0036");
        _list.add("0034");
        _list.add("0026");
        _list.add("0069");
        _list.add("0043");
        _list.add("0278");
        _list.add("0280");
        _list.add("0077");
        _list.add("0080");
        _list.add("0075");
        _list.add("0071");
        _list.add("0049");
        _list.add("0009");
        _list.add("0010");
        _list.add("0011");
        _list.add("0015");
        _list.add("0101");
        _list.add("0037");
        _list.add("0031");
        _list.add("0028");
        _list.add("0033");
        _list.add("0000");
        _list.add("0279");
        _list.add("0281");
        return _list;
    }

}

/*  告警级别
 ALARMLEVEL.put("1", "CRITICAL");
 ALARMLEVEL.put("2", "MAJOR");
 ALARMLEVEL.put("3", "MINOR");
 ALARMLEVEL.put("4", "WARNING");
 */

/*  设备告警字符串
 sb.append("type=0,event_name=UP,alarm_level=MINOR,alarm_status=OPEN,Alm_Con=Ping不通,alarm_update=")
 .append(new Date().getTime() / 1000)
 .append(",alarm_type=200,vendor_type=")
 .append(_alarm.vendor_type)
 .append(",node_name=")
 .append(_alarm.node_name)
 .append(",description=<Ping状态>设备名称:")
 .append(_alarm.node_name)
 .append(" Ping状态=")
 .append(_value)
 .append(",node_id=")
 .append(_alarm.node_id)
 .append(",shelf_id=1,slot_id=-1,port_id=-1");
 */

/*  电路告警字符串
 StringBuffer sb = new StringBuffer();
 sb.append("type=0,event_name=UP,alarm_level=MINOR,alarm_status=OPEN,Alm_Con=光功率过低,alarm_update=")
 .append(new Date().getTime() / 1000)
 .append(",alarm_type=6,vendor_type=")
 .append(_alarm.vendor_type)
 .append(",node_name=")
 .append(_alarm.node_name)
 .append(",description=<光功率>光功率过低小于-30 设备名称:")
 .append(_alarm.node_name)
 .append(" 板卡=")
 .append(_slot)
 .append(" 端口=")
 .append(_port)
 .append(" 值=")
 .append(_value)
 .append(",node_id=")
 .append(_alarm.node_id)
 .append(",shelf_id=1,slot_id=-1,port_id=-1,path_id=")
 .append(_alarm.path_id);
 */
