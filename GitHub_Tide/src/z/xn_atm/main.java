package z.xn_atm;

import z.xn_transperwbs.*;
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

import javax.xml.rpc.ServiceException;

/**
 *
 * @author yangzhen
 * @author 功能：短信发送
 * @author 描述：
 * @author 系统内部短信发送，用于信息提醒。 电信走动环，移动联通走综调。
 * @author 动环的需要等一段时间才有回执，综调可以立刻得到回执。 通过回执的状态判断短信的发送状态
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnatm.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//
    private static util.GetSql.atm_mysql _mysql = new util.GetSql.atm_mysql();
    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetThread.thread _thread = new util.GetThread.thread(2);

    public void run() {
        if (db.ini(log, _csnms, _mysql)) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 30);//30秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("加载数据库失败");
        }
    }

    public void doing_main() {
        try {
            log.info("开始获取atmif_performance(56)性能数据");
            List _SWITCH_REPORT_OUT = db.get_xn_atm(_csnms, _mysql);             //获取性能信息
            List _atmif_performance = db.get_xn_atm_2(_csnms, _mysql);
            log.info("获取数据完成");

            if (_SWITCH_REPORT_OUT.size() > 0) {
                log.info("开始补全atmif_performance(oracle)数据");
                fun.insert_csnms(_csnms, _atmif_performance);
                log.info("补全数据完成");

                log.info("开始加载atmif_performance(oracle)数据");
                List _atm_data = db.get_xn_csnms(_csnms);
                log.info("加载数据完成");

                log.info("开始进行数据运算");
                List _atm_data2 = fun.get_xn_csnms2(_SWITCH_REPORT_OUT, _atm_data);
                log.info("数据运算完成");

                log.info("开始同步性能数据");
                fun.insert_csnms(_csnms, _atm_data2);
                log.info("同步数据完成");

                log.info("开始更新atmif_performance数据");
                fun.insert_csnms2(_csnms, _atmif_performance);
                log.info("更新完成");

                log.info("删除历史数据");
                db._del_xn_atm(_mysql, _atmif_performance);
                log.info("删除数据完成");
                System.out.println("-------------------执行完成-----------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
