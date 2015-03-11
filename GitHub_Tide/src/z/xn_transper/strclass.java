/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_transper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class strclass {

    public static class Switch_circuit {

        public String str1 = null;
        public String str2 = null;
        public String str3 = null;
        public String str4 = null;
        public String str5 = null;
        public String str6 = null;
    }

    public static class perfinstance {

        public String ID = "";
        public String PERFTYPE = "";
        public String VALUE_MAX = "";
        public String VALUE_MIN = "";
        public String INFO = "";
        public String TYPE = "";
    }

    public static class Jstranspathperf_detail {

        public String path_id = "";
        public String path_name = "";
        public String node_id = "";
        public String node_name = "";
        public String slot = "";
        public String port = "";
        public String gettime = "";
        public String inoptical = "0";
        public String outoptical = "0";
        public String notavailable = "0";
        public String notavailable2 = "0";
        public String backerror = "0";
        public String str = "0";
        public String slottypeid = "0";
        public String slot_type_port = "";
        public String severely = "0";
        public String groupid = "";
    }

    public static class class_path {

        public String PATH_ID = "";
        public String NAME = "";
        public String GROUPID = "";
        public String PARENTID = "";
        public String EMS_TYPE = "";
    }

    public static class Switch_circuit_new {

        public String name = null;
        public String wy_name = null;
        public String slot_port = null;
        public String type = null;
        public String value = null;
        public String gettime = null;
    }

    public static class Jstranspathperf_detail_2 {

        public String PATH_ID = "";
        public String NODE_ID = "";
        public String SLOT_TYPE_PORT = "";
        public String GETTIME = "";
        public String NOTAVAILABLE2 = "";
    }

    public static class Alarm {

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
