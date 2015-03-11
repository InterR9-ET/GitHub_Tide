package z.send_donghuan_fazhi;

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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/**
 *
 * @author yangzhen
 * @author 功能：动环设备的阈值处理
 * @author 描述：
 * @author 
 *
 * 
 */


public class main extends Thread {

    //-------------------------------日志-------------------------------------// 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(z.send_donghuan_command.main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_donghuanfazhi.config");
    }
    private static util.GetTools.tools _tools = new util.GetTools.tools();
    //-------------------------------日志-------------------------------------//
    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetThread.thread _thread = new util.GetThread.thread(1);

    public static ResultSet resultset = null;
    public static PreparedStatement prestmt = null;

    public void run() {
        if (db.ini(log, _csnms)) {
            doing_main();
        } else {
            log.info("加载数据库出错");
        }
    }

    public static void doing_main() {
        log.info("------------------程序开始--------------------");
        log.info(_tools.systime_prase_string(""));
        try {
            //--------------------------添加任务------------------------//
            _thread.execute(task_in());//发送
            Thread.sleep(500);
            //--------------------------添加任务------------------------//
        } catch (Exception e) {
            e.printStackTrace();
            log.info("线程执行:" + e.getMessage());
        } finally {
            //------------------等待线程运行---------------//
            _thread.waitFinish(); //等待所有任务执行完毕  
            _thread.closePool(); //关闭线程池                
            //------------------等待线程运行---------------//
        }
        log.info("------------------程序结束--------------------");
    }

    private static Runnable task_in() {
        return new Runnable() {
            public void run() {
                while (true) {
                    try {
                        log.info("加载数据");
                        List _list_data = db.load_fazhi_csnms(_csnms);
                        int m = _list_data.size();
                        log.info("加载完成:" + _list_data.size());
                        //发送数据
                        if (m > 0) {
                            send_fazhi_main(_list_data);
                        }
                        //延时
                        Thread.sleep(1000 * 5);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("发送失败：" + ex.getMessage() + " " + _tools.systime_prase_string(""));
                    }
                }
            }

            public void send_fazhi_main(List _list_data) {
                //发送到动环
                if (_list_data.size() > 0) {
                    try {
                        for (int i = 0, m = _list_data.size(); i < m; i++) {
                            z.allClass.donghaunfazhi_mes _sms = new z.allClass.donghaunfazhi_mes();
                            _sms = (z.allClass.donghaunfazhi_mes) _list_data.get(i);
                            if (_sms.TYPE.toString().equals("1")) {
                                getSignalThresholdByAD(_sms);
                            } else {
                                setSignalThresholdByAD(_sms);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("出现异常：\r\nERROR：" + ex.getMessage().toString());
                    }
                }
            }

            public void getSignalThresholdByAD(z.allClass.donghaunfazhi_mes _sms) throws Exception {

                String lscid = _sms.LSCID.toString();
                String equipid = _sms.EQUIPID.toString();

                String _url = "http://132.228.198.11:8081/iems_js_smgp_service/services/snmpService";
                Service service = new Service();
                Call call;
                call = (Call) service.createCall();
                call.setTargetEndpointAddress(_url);
                call.setOperationName("getSignalThresholdByAD");// WSDL里面描述的接口名称
                call.addParameter("lscId", org.apache.axis.encoding.XMLType.XSD_STRING,
                        javax.xml.rpc.ParameterMode.IN);// 接口的参数
                call.addParameter("signalId",
                        org.apache.axis.encoding.XMLType.XSD_STRING,
                        javax.xml.rpc.ParameterMode.IN);// 接口的参数
                call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型 
                String retvar = (String) call.invoke(new String[]{lscid, equipid});

                if (retvar.length() > 0) {
                    log.info("getSignalThresholdByAD收到返回值：\r\n" + retvar);
                    _sms.CONTENT = retvar;
                    db.update(log, _csnms, _sms);
                }
            }

            public void setSignalThresholdByAD(z.allClass.donghaunfazhi_mes _sms) throws Exception {

                String lscid = _sms.LSCID.toString();
                String equipid = _sms.EQUIPID.toString();
                String maxvalue = _sms.MAXVALUE.toString();
                String minvalue = _sms.MINVALUE.toString();

                String _url = "http://132.228.198.11:8081/iems_js_smgp_service/services/snmpService";
                Service service = new Service();
                Call call;
                call = (Call) service.createCall();
                call.setTargetEndpointAddress(_url);
                call.setOperationName("setSignalThresholdByAD");// WSDL里面描述的接口名称
                call.addParameter("lscId", org.apache.axis.encoding.XMLType.XSD_STRING,
                        javax.xml.rpc.ParameterMode.IN);// 接口的参数
                call.addParameter("signalId",
                        org.apache.axis.encoding.XMLType.XSD_STRING,
                        javax.xml.rpc.ParameterMode.IN);// 接口的参数
                call.addParameter("minVal",
                        org.apache.axis.encoding.XMLType.XSD_STRING,
                        javax.xml.rpc.ParameterMode.IN);// 接口的参数

                call.addParameter("maxVal",
                        org.apache.axis.encoding.XMLType.XSD_STRING,
                        javax.xml.rpc.ParameterMode.IN);// 接口的参数

                call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型

                String retvar = (String) call.invoke(new String[]{lscid, equipid,
                    minvalue, maxvalue});

                if (retvar.length() > 0) {
                    log.info("setSignalThresholdByAD收到返回值：\r\n" + retvar);
                    _sms.CONTENT = retvar;
                    db.update(log, _csnms, _sms);
                }
            }

        };
    }

}
