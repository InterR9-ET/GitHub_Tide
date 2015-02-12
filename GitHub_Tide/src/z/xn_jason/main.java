package z.xn_jason;

import java.io.File;
import z.xn_transper.*;
import z.xn_port.*;
import z.send_sms.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import java.lang.Thread;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

/**
 *
 * @author yangzhen
 * @author 功能：jason格式的性能文件解析
 * @author 描述：
 * @author
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnjason.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetThread.thread _thread = new util.GetThread.thread(2);
    private static util.GetFile.A_example _file = new util.GetFile.A_example();
    private static util.GetFtp.ftpclient _ftp = new util.GetFtp.ftpclient();

    public void run() {
        if (_ftp.ini()) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 60 * 5 + 2000);//10秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("数据库加载失败");
        }
    }

    public void doing_main() {
        String localPath;
        Properties prop = new Properties();
        localPath = "" + prop.getProperty("localpath");
        if (_ftp.createDir(localPath)) {
            try {
                File directory = new File("");// 设定为当前文件夹
                String _url = directory.getCanonicalPath().toString() + "/" + localPath;
                if (_ftp.downloadFile(_file.toString(), _url)) {
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
