/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_jason;

import com.tidestone.dateTime.DateUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.GetSocket.socketclient;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {  
    
    private static Socket remote_socket = null;
    public static String str_mes = "";
    public static PrintWriter out = null;
    public static BufferedReader in = null;
    public static String remote_ip = "132.228.5.56";
    public static String remote_port = "9005";
    private static String CONFIG_PATH = "conf/ftp.config";
    public static Hashtable deviceHash = new Hashtable();
    
    public static List _list_fu = strclass.list_title_fuhao();
    public static List _list_long = strclass.list_title_long();
    public static List _list_title_null = strclass.list_title_null();
    public static List _list_cpu = strclass.list_cpu();
    
    private static Logger log_alarm = Logger.getLogger(socketclient.class);
    
    //------------------------------下载并加载文件------------------------------------
    public static boolean downFile(util.GetFile.A_example _file, org.apache.log4j.Logger log) {
        boolean _bs = false;
        //读取配置文件
        String CONFIG_PATH = "conf/ftp.config";
        String host;
        int port;
        String username;
        String password;
        String remotePath;
        String localPath;
        String _open_load = "false";

        Properties prop = new Properties();
        File file = new File(CONFIG_PATH);
        try {
            prop.load(new FileInputStream(file));
            System.getProperties().putAll(prop);
            host = "" + prop.getProperty("host");
            port = Integer.parseInt(prop.getProperty("port"));
            username = "" + prop.getProperty("username");
            password = "" + prop.getProperty("password");
            remotePath = "" + prop.getProperty("remotepath");
            localPath = "" + prop.getProperty("localpath");
            _open_load = prop.getProperty("status");

            //开始下载文件
            if (_open_load.equals("true")) {
                FTPClient ftp = new FTPClient();
                try {
                    int reply;
                    ftp.connect(host, port);
                    // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
                    ftp.login(username, password);// 登录
                    reply = ftp.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        ftp.disconnect();
                    }

                    //创建文件夹
                    _file.create_folder(localPath);

                    //删除历史文件
                    try {
                        File directory = new File("");// 设定为当前文件夹
                        String _url = directory.getCanonicalPath().toString() + "/" + localPath;
                        File _folder_url = new File(_url);
                        File[] files = _folder_url.listFiles();//获取文件夹列表
                        List list_in = new ArrayList();
                        if (files.length > 0) {
                            int k = 0;
                            for (int i = 0, m = files.length; i < m; i++) {
                                if (!files[i].isDirectory()) {
                                    String name = files[i].toString();
                                    File file2 = new File(name);
                                    file2.delete();
                                    k = k + 1;
                                    log.info("删除历史文件[" + k + "]:" + name);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("删除历史文件发生异常:" + e.getMessage().toString());
                    }

                    ftp.setFileType(FTP.BINARY_FILE_TYPE);
                    ftp.enterLocalPassiveMode();
                    ftp.changeWorkingDirectory(remotePath); // 转移到FTP服务器目录
                    FTPFile[] fs = ftp.listFiles();
                    log.info("服务器中存在文件数量： " + fs.length);
                    OutputStream is = null;
                    File localFile = null;
                    int i = 0;
                    for (FTPFile ff : fs) {
                        i = i + 1;
                        String filename = new String(ff.getName()
                                .getBytes("iso-8859-1"), "GBK");
                        localFile = new File(localPath + "/" + filename);
                        is = new FileOutputStream(localFile);
                        ftp.retrieveFile(ff.getName(), is);
                        System.out.println("获取文件[" + i + "]: " + filename);
                    }
                    is.close();
                    ftp.logout();
                    _bs = true;
                } catch (IOException e) {
                    e.printStackTrace();
                    log.info("下载文件出现异常：" + e.getMessage().toString());
                } finally {
                    if (ftp.isConnected()) {
                        try {
                            ftp.disconnect();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.info("配置文件读取错误....");
        }
        return _bs;
    }

    //------------------------------处理下载的数据文件------------------------------------
    public static boolean read_file(util.GetFile.A_example _file, util.GetSql.csnms _csnms, util.GetTools.tools _tools, org.apache.log4j.Logger log) {

        boolean _bs = false;
        try {
            //读取配置文件
            String CONFIG_PATH = "conf/ftp.config";
            String host = "";
            int port;
            String username = "";
            String password = "";
            String remotePath = "";
            String localPath = "";
            String _open_load = "false";

            Properties prop = new Properties();
            File file = new File(CONFIG_PATH);
            try {
                prop.load(new FileInputStream(file));
                System.getProperties().putAll(prop);
                host = "" + prop.getProperty("host");
                port = Integer.parseInt(prop.getProperty("port"));
                username = "" + prop.getProperty("username");
                password = "" + prop.getProperty("password");
                remotePath = "" + prop.getProperty("remotepath");
                localPath = "" + prop.getProperty("localpath");
                _open_load = prop.getProperty("status");
            } catch (Exception e) {
                e.printStackTrace();
            }

            File directory = new File("");// 设定为当前文件夹
            String _url = directory.getCanonicalPath().toString() + "/" + localPath;
            File _folder_url = new File(_url);
            File[] files = _folder_url.listFiles();//获取文件夹列表
            List list_in = new ArrayList();
            if (files.length > 0) {
                int k = 0;
                for (int i = 0, m = files.length; i < m; i++) {
                    if (!files[i].isDirectory()) {
                        String name = files[i].toString();
                        log.info("处理文件进度[" + (i + 1) + "/" + m + "]：" + name);
                        try {
                            InputStreamReader read = new InputStreamReader(new FileInputStream(name), "GBK");//考虑到编码格式
                            BufferedReader bufferedReader = new BufferedReader(read);
                            String lineTxt = null;
                            String str_mes = "";
                            while ((lineTxt = bufferedReader.readLine()) != null) {
                                str_mes = str_mes + lineTxt;
                            }
                            if (str_mes.length() > 0) {
                                read_jason(str_mes, _csnms, _tools,log);//-----------------------------------------------------
                            }
                        } catch (Exception e) {
                            log.info("解析文件异常：" + name + "  #" + e.getMessage().toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("完成");
        return _bs;
    }
/**
 * @param jsonContent--------------------------------------------------文件内容
 * @param _csnms
 * @param _tools
 * @param log 
 */
    //---------------------------开始解析文件------------------------------------
    private static void read_jason(String jsonContent, util.GetSql.csnms _csnms, util.GetTools.tools _tools,org.apache.log4j.Logger log) {
        try {

            JSONObject obj = new JSONObject(jsonContent);
            Iterator jsoniter = obj.keys();
            int m = 0;
            List list = new ArrayList();
            strclass._mes _mes = new strclass._mes();
            while (jsoniter.hasNext()) {
                //初始化
                String _type1 = "";
                String _type2 = "";
                String _title = "";
                String _value = "";
                String jsonkey = jsoniter.next().toString();
                _type1 = jsonkey;
                JSONObject reobj = obj.getJSONObject(jsonkey);
                Iterator rejsoniter = reobj.keys();
                while (rejsoniter.hasNext()) {
                    String rejsonkey = rejsoniter.next().toString();
                    _type2 = rejsonkey;
                    JSONArray array = reobj.getJSONArray(rejsonkey);
                    String _time = "";
                    for (int i = 0; i < array.length(); i++) {
                        Iterator iter = array.getJSONObject(i).keys();
                        Iterator iter2 = array.getJSONObject(i).keys();
                        while (iter2.hasNext()) {
                            String s = iter2.next().toString();
                            String str = array.getJSONObject(i).getString(s);
                            _title = s;
                            _value = str;
                            if (_title.equals("time")) {
                                _time = _value;
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < array.length(); i++) {
                        Iterator iter = array.getJSONObject(i).keys();

                        if (_time.length() == 0) {
                            _time = _tools.systime_prase_string("");
                        } else if (_time.equals("1970-01-01 08:00:00")) {
                            _time = _tools.systime_prase_string("");
                        }

                        while (iter.hasNext()) {
                            _mes = new strclass._mes();
                            String s = iter.next().toString();
                            String str = array.getJSONObject(i).getString(s);
                            _title = s;
                            _value = str;
                            _mes.message_type1 = _type1.trim().toString();
                            _mes.message_type2 = _type2.trim().toString();
                            _mes.message_title = _title.trim().toString();
                            _mes.message_value = _value.trim().toString();
                            _mes.message_time = _time;
                            if (_type1.length() > 0) {
                                if (_type2.length() > 0) {
                                    if (_title.length() > 0) {
                                        //System.out.println(_type1 + "#" + _type2 + "#" + _title + "#" + _value + "#" + _time);
                                        list.add(_mes);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            do_data(list, _csnms, _tools,log); //------------------------------------------------------------------开始处理数据
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //开始处理数据
    private static void do_data(List list, util.GetSql.csnms _csnms, util.GetTools.tools _tools,org.apache.log4j.Logger log) {

        strclass._mes _mes = new strclass._mes();
        String op = "";
        String[] mes_h_p = null;

        if (list.size() > 0) {

            //检测类型是否为3大主机
            op = jian_ce_type(list, "主机");//3大主机  Linux  windows  aix
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {  //是3大主机  Linux  windows  aix                 
                    chuli.chuli_zhuji(list, mes_h_p[1].toString(), _csnms, _tools,log);
                }
            }

            //检测类型是否为Oracle
            op = jian_ce_type(list, "Oracle");
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {  //是3大主机  Linux  windows  aix                 
                    chuli.chuli_oracle(list, mes_h_p[1].toString().replace("(oracle)", ""), _csnms, _tools);
                }
            }

            //检测类型是否为FireWall
            op = jian_ce_type(list, "FireWall");
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    chuli.chuli_firewall(list, mes_h_p[1].toString(), _csnms, _tools,log);
                }
            }

            //F5
            op = jian_ce_type(list, "F5"); //  
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    chuli.chuli_f5(list, mes_h_p[1].toString().trim().replace("省地税外包平台（", "").replace("）", ""), _csnms, _tools);
                }
            }

            //switchdevice
            op = jian_ce_type(list, "SwitchDevice"); //   还是按firewall解析
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    chuli.chuli_firewall(list, mes_h_p[1].toString().trim().replace("光纤交换机（", "").replace("）", ""), _csnms, _tools,log);
                    // jason.do_switchdevice.chuli_switchdevice(list, mes_h_p[1].toString().trim().replace("光纤交换机（", "").replace("）", ""));
                }
            }

            //ShuGuangDS
            op = jian_ce_type(list, "ShuGuangDS"); //  
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    chuli.shugaungDS(list, mes_h_p[1].toString().trim(), _csnms, _tools);
                }
            }
        }
    }

    //-------------------------------检测数据类型---------------------------------------
    private static String jian_ce_type(List list, String _type) {
        String _ip = "false";
        strclass._mes _mes = new strclass._mes();
        for (int i = 0; i < list.size(); i++) {
            _mes = new strclass._mes();
            _mes = (strclass._mes) list.get(i);
            //System.out.println(_mes.message_type1.toString() + "#" + _mes.message_title.toString());

            if (_type.equals("主机")) {
                if (_mes.message_type1.equals("Windows")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                } else if (_mes.message_type1.equals("Linux")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                } else if (_mes.message_type1.equals("AIX")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                }
            } else if (_type.equals("Oracle")) {
                if (_mes.message_type1.equals("L_ORACLE")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                }
            } else if (_type.equals("FireWall")) {
                if (_mes.message_type1.equals("FireWall")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                }
            } else if (_type.equals("F5")) {
                if (_mes.message_type1.equals("F5")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                }
            } else if (_type.equals("SwitchDevice")) {
                if (_mes.message_type1.equals("switchDevice")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                }
            } else if (_type.equals("ShuGuangDS")) {
                if (_mes.message_type1.equals("ShuGuangDS")) {
                    if (_mes.message_title.equals("caption")) {
                        _ip = "true" + "#" + _mes.message_value;
                        break;
                    }
                }
            }
        }
        return _ip;
    }

    static String return_value(String tite, String value) {

        //处理空值转换成0     
        for (int i = 0; i < _list_title_null.size(); i++) {
            String kk = _list_title_null.get(i).toString();
            if (tite.equals(kk)) {
                value = value.trim();
                if (value.length() == 0) {
                    value = "0";
                    break;
                }
            }
        }

        //处理","转换    
        for (int i = 0; i < _list_fu.size(); i++) {
            String kk = _list_fu.get(i).toString();
            if (tite.equals(kk)) {
                value = value.replace(",", "");
                break;
            }
        }

        //转换CPU
        for (int i = 0; i < _list_cpu.size(); i++) {
            String kk = _list_cpu.get(i).toString();
            if (tite.equals(kk)) {
                value = value.replace(",", "");
                long l = Long.parseLong(value);
                double p = 0.00;
                l = l / 1000 / 1000 / 10;
                p = l * 0.01;
                value = p + "";
                break;
            }
        }

        //处理MB转换     
        for (int i = 0; i < _list_long.size(); i++) {
            String kk = _list_long.get(i).toString();
            if (tite.equals(kk)) {
                value = value.replace(",", "");
                if (value.indexOf("e+") != -1) {
                    BigDecimal bd = new BigDecimal(value);
                    long l = Long.parseLong(bd.toPlainString() + "");
                    if (kk.equals("带宽")) {
                        l = l / 1000 / 1000 / 1000;
                    } else {
                        l = l / 1024 / 1024;
                    }
                    value = l + "";
                    break;
                } else {
                    long l = Long.parseLong(value);
                    if (kk.equals("带宽")) {
                        l = l / 1000 / 1000 / 1000;
                    } else {
                        l = l / 1024 / 1024;
                    }
                    value = l + "";
                    break;
                }

            }
        }

        return value;
    }

    public static boolean sendAlarm(String alarm,util.GetSql.csnms _csnms) {
        boolean _bs = false;
        _bs = sendmessage(alarm,_csnms);
        return _bs;
    }

    public static boolean sendmessage(String mes, util.GetSql.csnms _csnms) {

        //加载并打开告警服务器
        db.ini(_csnms);

        boolean _bs = false;
        str_mes = mes;

        try {

            if (str_mes.length() > 0) {
                // socketclient.out = new PrintWriter(socketclient.remote_socket.getOutputStream(),"UTF-8");
                fun.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(remote_socket.getOutputStream(), "GBK")), true);
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
    
    public static boolean compare_time(String nowtime, strclass._err _es) {
        boolean _bs = false;
        String _time_begin = _es.START_DATE.substring(0, 10) + " " + _es.START_TIME;
        String _time_end = _es.END_DATE.substring(0, 10) + " " + _es.END_TIME;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(_time_begin);
            Date dt2 = df.parse(_time_end);
            Date dt3 = df.parse(nowtime);

            if (dt1.getTime() < dt3.getTime()) {
                if (dt3.getTime() < dt2.getTime()) {
                    _bs = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return _bs;
    }
    
    public static int compare_value(String _value, strclass._err _es) {
        int _bs = 0;
        /*
         ALARMLEVEL.put("1", "CRITICAL");
         ALARMLEVEL.put("2", "MAJOR");
         ALARMLEVEL.put("3", "MINOR");
         ALARMLEVEL.put("4", "WARNING");
         */
        try {

            //CRITICAL 严重
            if (Double.parseDouble(_es.MIN_CRITICAL) != 1.111 && Double.parseDouble(_es.MAX_CRITICAL) != 1.111) {
                if (Double.parseDouble(_value) < Double.parseDouble(_es.MIN_CRITICAL)
                        || Double.parseDouble(_value) > Double.parseDouble(_es.MAX_CRITICAL)) {
                    _bs = 1;
                    return _bs;
                }
            } else if (Double.parseDouble(_es.MAX_CRITICAL) != 1.111 && Double.parseDouble(_es.MIN_CRITICAL) == 1.111) {
                if (Double.parseDouble(_value) >= Double.parseDouble(_es.MAX_CRITICAL)) {
                    _bs = 1;
                    return _bs;
                }

            } else if (Double.parseDouble(_es.MAX_CRITICAL) == 1.111 && Double.parseDouble(_es.MIN_CRITICAL) != 1.111) {
                if (Double.parseDouble(_value) <= Double.parseDouble(_es.MIN_CRITICAL)) {
                    _bs = 1;
                    return _bs;
                }
            }

            //MAJOR 主要
            if (Double.parseDouble(_es.MIN_MAJOR) != 1.111 && Double.parseDouble(_es.MAX_MAJOR) != 1.111) {
                if (Double.parseDouble(_value) < Double.parseDouble(_es.MIN_MAJOR)
                        || Double.parseDouble(_value) > Double.parseDouble(_es.MAX_MAJOR)) {
                    _bs = 2;
                    return _bs;
                }
            } else if (Double.parseDouble(_es.MAX_MAJOR) != 1.111 && Double.parseDouble(_es.MIN_MAJOR) == 1.111) {
                if (Double.parseDouble(_value) >= Double.parseDouble(_es.MAX_MAJOR)) {
                    _bs = 2;
                    return _bs;
                }

            } else if (Double.parseDouble(_es.MAX_MAJOR) == 1.111 && Double.parseDouble(_es.MIN_MAJOR) != 1.111) {
                if (Double.parseDouble(_value) <= Double.parseDouble(_es.MIN_MAJOR)) {
                    _bs = 2;
                    return _bs;
                }
            }

            //MINOR 次要
            if (Double.parseDouble(_es.MIN_MINOR) != 1.111 && Double.parseDouble(_es.MAX_MINOR) != 1.111) {
                if (Double.parseDouble(_value) < Double.parseDouble(_es.MIN_MINOR)
                        || Double.parseDouble(_value) > Double.parseDouble(_es.MAX_MINOR)) {
                    _bs = 3;
                    return _bs;
                }
            } else if (Double.parseDouble(_es.MAX_MINOR) != 1.111 && Double.parseDouble(_es.MIN_MINOR) == 1.111) {
                if (Double.parseDouble(_value) >= Double.parseDouble(_es.MAX_MINOR)) {
                    _bs = 3;
                    return _bs;
                }

            } else if (Double.parseDouble(_es.MAX_MINOR) == 1.111 && Double.parseDouble(_es.MIN_MINOR) != 1.111) {
                if (Double.parseDouble(_value) <= Double.parseDouble(_es.MIN_MINOR)) {
                    _bs = 3;
                    return _bs;
                }
            }

            //WARNING  警告
            if (Double.parseDouble(_es.MIN_WARN) != 1.111 && Double.parseDouble(_es.MAX_WARN) != 1.111) {
                if (Double.parseDouble(_value) < Double.parseDouble(_es.MIN_WARN)
                        || Double.parseDouble(_value) > Double.parseDouble(_es.MAX_WARN)) {
                    _bs = 4;
                    return _bs;
                }
            } else if (Double.parseDouble(_es.MAX_WARN) != 1.111 && Double.parseDouble(_es.MIN_WARN) == 1.111) {
                if (Double.parseDouble(_value) >= Double.parseDouble(_es.MAX_WARN)) {
                    _bs = 4;
                    return _bs;
                }

            } else if (Double.parseDouble(_es.MAX_WARN) == 1.111 && Double.parseDouble(_es.MIN_WARN) != 1.111) {
                if (Double.parseDouble(_value) <= Double.parseDouble(_es.MIN_WARN)) {
                    _bs = 4;
                    return _bs;
                }
            }

        } catch (Exception e) {
            _bs = 0;
        }
        return _bs;
    }
    
    public static String alarm2Str(strclass.Alarm _alarm,org.apache.log4j.Logger log) {

        log.info("组合生成相应的告警字串---");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append("alarm_id=1,type=0").append(",");
        sb.append("alarm_type=").append(_alarm.alarm_type).append(",");
        sb.append("alarm_status=").append(_alarm.alarm_status).append(",");
        sb.append("event_name=Alarm").append(",");
        sb.append("node_id=").append(_alarm.node_id).append(",");
        sb.append("node_name=").append(_alarm.node_name).append(",");
        //sb.append("path_id=").append(path_id).append(",");
        sb.append("shelf_id=").append(_alarm.shelf_id).append(",");
        sb.append("slot_id=").append(_alarm.slot_id).append(",");
        sb.append("port_id=").append(_alarm.port_id).append(",");
        sb.append("description=").append(_alarm.description).append(",");
        sb.append("Alm_Con=").append(_alarm.alm_Con).append(",");
        sb.append("alarm_level=").append(_alarm.alarm_level).append(",");
        sb.append("vendor_type=").append(_alarm.vendor_type).append(",");
        sb.append("debug_alarm_time=").append(_alarm.debug_alarm_time).append(",");
        sb.append("alarm_update=").append(_alarm.alarm_update);
        return sb.toString();
    }
    
    public static String stat(String port, String ip, String input, String out, String date, util.GetSql.csnms _csnms,org.apache.log4j.Logger log) {
        String sql = "";
        try {

            List _list = new ArrayList();
            String devid = (String) deviceHash.get(ip);
            if (devid != null) {
                input = input.replace(",", "");
                out = out.replace(",", "");
                long gettime = getDateToSeconds(date);
                strclass.port2 _port = new strclass.port2();
                _port.DATETIME = gettime + "";
                _port.DEVICEID = devid;
                _port.PORTINFO = port;
                _port.GETWAY = "3";
                _port.IFINOCTETS = input;
                _port.IFOUTOCTETS = out;
                _port.IFINOCTETSBPS = input;
                _port.IFOUTOCTETSBPS = out;
                _list.add(_port);

                // sql = "insert into SWITCH_REPORT_OUT values
                //(" + gettime + "," + devid + ",'" + port + "',3," + input + "," + out + ",0,0,0,0,0,0,0,0,0,0,
                //" + input + "," + out + ",0,0,0,0,0,0,0,0,0,0,0)";
                //System.out.println(sql);
            }
            if (_list.size() > 0) {
                db.jiexi_ycl(_list, _csnms,log);
            }
            //deviceHash.clear();          
        } catch (Exception e) {
            log.info("写入SWITCH_REPORT_OUT出现异常：" + e.getMessage().toString());
            e.printStackTrace();
        }
        return sql;
    }
    
    public static Long getDateToSeconds(String dt) {
        Date datee = DateUtil.StrToDate(dt);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = "" + format;

        String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(datee);
        String sss = "" + datee.getTime();
        return Long.parseLong(sss) / 1000;
    }
    
   
    
     
   
     
     
     
    
}



