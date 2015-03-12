/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_jason;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class strclass {
    
    public static class _mes {

        public String message_type1 = "";
        public String message_type2 = "";
        public String message_title = "";
        public String message_value = "";
        public String message_ip = "";
        public String message_nodeid = "";
        public String message_time = "";
    }
    
    public static class _idcperfermance_config {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
        
    public static List list_title_null() {
        List _list = new ArrayList();
        _list.add("最大空闲块大小");
        _list.add("空闲大小(M)");
        _list.add("已用大小(M)");
        _list.add("物理读");
        _list.add("逻辑读");
        return _list;
    }
        
    public static List list_title_fuhao() {
        List _list = new ArrayList();
        _list.add("最大空闲块大小");
        _list.add("空闲大小(M)");
        _list.add("已用大小(M)");
        _list.add("物理读");
        _list.add("逻辑读");
        _list.add("inode空闲数");
        _list.add("流入速率(bps)");
        _list.add("流出速率(bps)");
        _list.add("capacity(GB)");
        _list.add("CAPACITY(GB)");
        return _list;
    }
        
    public static List list_title_long() {
        List _list = new ArrayList();
        _list.add("SGA大小(MB)");
        _list.add("shared_pool大小(MB)");
        _list.add("data_cache大小(MB)");
        _list.add("pga_aggregate_target大小(MB)");
        _list.add("db_keep_cache大小(MB)");
        _list.add("表空间大小(M)");
        _list.add("log_buffer大小(MB)");
        _list.add("文件系统大小");
        _list.add("物理内存大小");
        _list.add("swap区大小");
        _list.add("带宽");
        _list.add("流入速率(bps)");
        _list.add("流出速率(bps)");
        return _list;
    }
    
    public static List list_cpu() {
        List _list = new ArrayList();
        _list.add("CPU主频");
        return _list;
    }
        
    public static class Alarm {

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
        
    public static class _idcperfermance_tongyong {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
    
    public static class _IPCPU {

        public String CPUID = "";
        public String TYPE = "";
        public String NAME = "";
        public String USED = "";
        public String PERCENT = "";
        public String NODEID = "";
    }
         
    public static class _PERFBOUNDARY_COUNT {

        public String NODEIP = "";
        public String SUBTYPE = "";
        public String COUNT = "";
        public String INFO = "";
        public String UPDATETIME = "";
        public String NODEID = "";
        public String VALUE = "";
        public String NODENAME = "";
    }
        
    public static class _err {

        public String NODEID = "";
        public String MAX_CRITICAL = "";
        public String MAX_MAJOR = "";
        public String MAX_MINOR = "";
        public String MAX_WARN = "";
        public String MIN_CRITICAL = "";
        public String MIN_MAJOR = "";
        public String MIN_MINOR = "";
        public String MIN_WARN = "";
        public String TIMES = "";
        public String STATUS = "";
        public String MAINTYPE = "";
        public String SUBTYPE = "";
        public String DEVICEIP_LIST = "";
        public String START_DATE = "";
        public String END_DATE = "";
        public String START_TIME = "";
        public String END_TIME = "";
    }
         
    public static class _IPMEM {

        public String MEMID = "";
        public String TYPE = "";
        public String NAME = "";
        public String USED = "";
        public String PERCENT = "";
        public String NODEID = "";
    }
        
    public static class _oracle_mes {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
   
    public static class _oracle_mes_de {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
        
    public static class port2 {

        public String DATETIME = "";
        public String DEVICEID = "";
        public String PORTINFO = "";
        public String GETWAY = "";
        public String IFINOCTETS = "0";
        public String IFOUTOCTETS = "0";
        public String IFINERRORS = "0";
        public String IFOUTERRORS = "0";
        public String IFINDISCARDS = "0";
        public String IFOUTDISCARDS = "0";
        public String IFINUCASTPKTS = "0";
        public String IFOUTUCASTPKTS = "0";
        public String IFINNUCASTPKTS = "0";
        public String IFOUTNUCASTPKTS = "0";
        public String IFINUNKNOWNPROTOS = "0";
        public String IFOUTQLEN = "0";
        public String IFINOCTETSBPS = "0";
        public String IFOUTOCTETSBPS = "0";
        public String IFINERRORSPPS = "0";
        public String IFOUTERRORSPPS = "0";
        public String IFINDISCARDSPPS = "0";
        public String IFOUTDISCARDSPPS = "0";
        public String IFINUCASTPKTSPPS = "0";
        public String IFOUTUCASTPKTSPPS = "0";
        public String IFINNUCASTPKTSPPS = "0";
        public String IFOUTNUCASTPKTSPPS = "0";
        public String IFINUNKNOWNPROTOSPPS = "0";
        public String IFOUTQLENPPS = "0";
        public String DELTATIME = "0";

    }

    public static class _tb_f5_mes {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
        
    public static class _tb_f5_mes_de {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
        
    public static class _tb_shuguangDS_mes {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
    }
        
    public static class _tb_shuguangDS_mes_de {

        public String IFIDX = "";
        public String NODEID = "";
        public String GETTIME = "";
        public String PERFID = "";
        public String PERFERMANCE = "";
        public String NODEIP = "";
        public String TYPE = "";
        public String INFO = "";
        public String INFO2 = "";
    }
}
