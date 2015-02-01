/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author yangzhen
 */
public class socketclient {

    private static Logger log_alarm = Logger.getLogger(socketclient.class);

    private static Socket remote_socket = null;

    static {
        PropertyConfigurator.configureAndWatch("ini/log4j_alarm.config");
    }

    /**
     * @param args the command line arguments
     */
    public static PrintWriter out = null;
    public static BufferedReader in = null;
    public static String remote_ip = "132.228.5.56";
    public static String remote_port = "9005";
    private static String CONFIG_PATH = "conf/ftp.config";

    public static String str_mes = "";

    public static void main(String[] args) {
        //String str_alarm = "type=0,event_name=UP,alarm_level=MINOR,alarm_status=OPEN,Alm_Con=光功率,alarm_update=1397112215,alarm_type=100,vendor_type=79,node_name=222-苏宁电器数据中心-2500,description=<IDC>光功率过低,node_id=171007510070,shelf_id=1,slot_id=-1,port_id=-1";
        //String str_alarm = "type=0,event_name=UP,alarm_level=MINOR,alarm_status=CLEAR,Alm_Con=光功率过低,alarm_update=1397292571,alarm_type=6,vendor_type=70,node_name=200-南湖-3500,description=<光功率过低>光功率过低小于-30 设备名称:200-南湖-3500 板卡=15 端口=2 值=-60.0,node_id=171007550070,shelf_id=1,slot_id=-1,port_id=-1,path_id=1399408";

        for (int i = 0; i < 20; i++) {
            String str_alarm = "type=0,event_name=UP,alarm_level=MINOR,alarm_status=CLEAR,Alm_Con=光功率过低,alarm_update=1397293953,alarm_type=6,vendor_type=70,node_name=180-游府西街衔接点2-9500,description=<光功率过低>光功率过低小于-30 设备名称:180-游府西街衔接点2-9500 板卡=19 端口=1 值=-29.0,node_id=171005420070,shelf_id=1,slot_id=-1,port_id=-1,path_id=245626";
            str_mes = str_alarm;
            sendmessage(str_mes);
        }

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
}
