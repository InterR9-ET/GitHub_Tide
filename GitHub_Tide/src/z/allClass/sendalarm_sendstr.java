/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.allClass;

/**
 *
 * @author yangzhen
 * @author 功能：alarm类的声明
 * @author 描述：alarm类的声明
 *
 */
public class sendalarm_sendstr {
    public String alarm_id = "1";
    public String type = "0";
    public String alarm_type = "";
    public String alarm_status = "";
    public String event_name = "";
    public String node_id = "";
    public String node_name = "";
    public String path_id = "-1";
    public String shelf_id = "-1";
    public String slot_id = "-1";
    public String port_id = "-1";
    public String description = "";//<私网设备告警>设备名称:JS-NJ-ZYL-BS.DCN.C6509-2;设备IP:132.228.42.2;检测时间:2014-03-28 13:10:53;检测值:80;超过最大阀值:20,
    public String alm_Con = "";
    public String alarm_level = "";
    public String vendor_type = "";
    public String debug_alarm_time = "";
    public String alarm_update = "";
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
