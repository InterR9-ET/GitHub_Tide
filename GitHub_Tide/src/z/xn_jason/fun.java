/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_jason;

import java.io.BufferedReader;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
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

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {

   

}
//******************************************************************************
class datas {

   private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

     static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnjason.config");
    }

    public boolean downFile(util.GetFile.A_example _file) {
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

    public boolean read_file(util.GetFile.A_example _file,util.GetSql.csnms _csnms, util.GetTools.tools _tools) {

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
            } catch (Exception ex) {

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

                        /*
                        if (name.indexOf("141.12.101.242") == -1) {
                            continue;
                        }
                                */

                        try {
                            InputStreamReader read = new InputStreamReader(new FileInputStream(name), "GBK");//考虑到编码格式
                            BufferedReader bufferedReader = new BufferedReader(read);
                            String lineTxt = null;
                            String str_mes = "";
                            while ((lineTxt = bufferedReader.readLine()) != null) {
                                str_mes = str_mes + lineTxt;
                            }
                            if (str_mes.length() > 0) {
                                read_jason(str_mes, _csnms, _tools);
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

    private void read_jason(String jsonContent, util.GetSql.csnms _csnms, util.GetTools.tools _tools) {
        try {

            JSONObject obj = new JSONObject(jsonContent);
            Iterator jsoniter = obj.keys();
            int m = 0;
            List list = new ArrayList();
            strings.jason._mes _mes = new strings.jason._mes();
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
                            _mes = new strings.jason._mes();
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

            //开始处理数据
            do_data(list, _csnms, _tools);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void do_data(List list, GetSql.csnms _csnms, GetTools.tools _tools) {

        strings.jason._mes _mes = new strings.jason._mes();
        // System.out.println(_mes.message_type1 + "####" + _mes.message_type2 + "####" + _mes.message_title + "####" + _mes.message_value);
        String op = "";
        String[] mes_h_p = null;

        if (list.size() > 0) {

            //检测类型是否为3大主机
            op = jian_ce_type(list, "主机");//3大主机  Linux  windows  aix
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {  //是3大主机  Linux  windows  aix                 
                    jason_idc.do_zhuji.chuli_zhuji(list, mes_h_p[1].toString(), _csnms, _tools);
                }
            }

            //检测类型是否为Oracle
            op = jian_ce_type(list, "Oracle");
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {  //是3大主机  Linux  windows  aix                 
                    jason_idc.do_oracle.chuli_oracle(list, mes_h_p[1].toString().replace("(oracle)", ""), _csnms, _tools);
                }
            }

            //检测类型是否为FireWall
            op = jian_ce_type(list, "FireWall");
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    jason_idc.do_firewall.chuli_firewall(list, mes_h_p[1].toString(), _csnms, _tools);
                }
            }

            //F5
            op = jian_ce_type(list, "F5"); //  
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    jason_idc.do_f5.chuli_f5(list, mes_h_p[1].toString().trim().replace("省地税外包平台（", "").replace("）", ""), _csnms, _tools);
                }
            }

            //switchdevice
            op = jian_ce_type(list, "SwitchDevice"); //   还是按firewall解析
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    jason_idc.do_firewall.chuli_firewall(list, mes_h_p[1].toString().trim().replace("光纤交换机（", "").replace("）", ""), _csnms, _tools);
                    // jason.do_switchdevice.chuli_switchdevice(list, mes_h_p[1].toString().trim().replace("光纤交换机（", "").replace("）", ""));
                }
            }

            //ShuGuangDS
            op = jian_ce_type(list, "ShuGuangDS"); //  
            mes_h_p = op.toString().split("#");
            if (mes_h_p.length == 2) {
                if (mes_h_p[0].toString().equals("true")) {
                    jason_idc.do_shugaungDS.shugaungDS(list, mes_h_p[1].toString().trim(), _csnms, _tools);
                }
            }
        }
    }

    private String jian_ce_type(List list, String _type) {
        String _ip = "false";
        strings.jason._mes _mes = new strings.jason._mes();
        for (int i = 0; i < list.size(); i++) {
            _mes = new strings.jason._mes();
            _mes = (strings.jason._mes) list.get(i);
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
    
}
