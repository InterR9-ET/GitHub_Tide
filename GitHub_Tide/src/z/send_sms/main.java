package z.send_sms;

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
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_sendsms.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetSql.donghuan_mysql _donghuan_mysql = new util.GetSql.donghuan_mysql();
    private static util.GetThread.thread _thread = new util.GetThread.thread(2);

    public static Hashtable headstr_yidong = new Hashtable();
    public static Hashtable headstr_liantong = new Hashtable();
    public static Hashtable headstr_dianxin = new Hashtable();
    public static Hashtable _tel_guolv = new Hashtable();

    public void run() {
        if (db.ini(log, _csnms, _donghuan_mysql)) {   //初始化数据库      
            try {
                headstr_yidong = fun.ini_telstr(1);
                headstr_liantong = fun.ini_telstr(2);
                headstr_dianxin = fun.ini_telstr(3);
                _tel_guolv = fun.ini_telstr(4);

                doing_main();//主体程序
                Thread.sleep(1000 * 10);// 5秒
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("初始化数据库失败");
        }
    }

    public void doing_main() {
        log.info("------------------程序开始--------------------");
        log.info(tools.systime_prase_string(""));
        try {
            // --------------------------添加任务------------------------//
            _thread.execute(task_in(headstr_yidong, headstr_liantong, headstr_dianxin));// 发送短信
            _thread.execute(task_up());// 更新状态
            Thread.sleep(500);
            // --------------------------添加任务------------------------//
        } catch (Exception e) {
            e.printStackTrace();
            log.info("线程执行:" + e.getMessage());
        } finally {
            // ------------------等待线程运行---------------//
            _thread.waitFinish(); // 等待所有任务执行完毕
            _thread.closePool(); // 关闭线程池
            // ------------------等待线程运行---------------//
        }
        log.info("------------------程序结束--------------------");

    }

    private Runnable task_in(final Hashtable _yidong, final Hashtable _liantong, final Hashtable _dianxin) {
        return new Runnable() {
            public void run() {
                while (true) {
                    try {
                        send_sms_main();       //短信处理
                        Thread.sleep(1000 * 3);// 间隔轮询一次
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info(ex.getMessage().toString());
                    }
                }
            }

            public void send_sms_main() {
                List _msm_data = db.loaddata(log, _csnms);  // 加载需要发送短信的数据
                List _dianxin = new ArrayList();//声明电信短信数组
                List _yidong_liantong = new ArrayList();//声明移动联通短信数组

                //数据分流   获取电信的短信
                _dianxin = fun.get_sms_boss(1, _msm_data, headstr_yidong, headstr_liantong, headstr_dianxin, _csnms, log);
                //数据分流   获取移动联通的短信
                _yidong_liantong = fun.get_sms_boss(2, _msm_data, headstr_yidong, headstr_liantong, headstr_dianxin, _csnms, log);

                //处理电信短信
                if (_dianxin.size() > 0) {
                    log.info("_dianxin.size:" + _dianxin.size());
                    send_sms_donghuan(_dianxin);
                }

                //处理移动联通短信
                if (_yidong_liantong.size() > 0) {
                    log.info("_yidong_liantong.size:" + _dianxin.size());
                    send_sms_dakehu(_yidong_liantong);
                }
            }

            public void send_sms_dakehu(List _msm_data) {

                if (_msm_data.size() > 0) {
                    // 发送短信
                    try {
                        String urlname = "http://132.228.169.145:8001/ida/axis/services/AsigAxisService";

                        Service s = new Service();
                        Call call = (Call) s.createCall();
                        call.setTimeout(new Integer(5000));
                        call.setOperation("executeXML");
                        call.setTargetEndpointAddress(urlname);

                        for (int i = 0, m = _msm_data.size(); i < m; i++) {
                            z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
                            _sms = (z.allClass.sendsms_sendstr) _msm_data.get(i);
                            Thread.sleep(500);

                            int str_rows = 0;
                            String[] str_tel = _sms.TEL.split(",");
                            //多个号码进行分解
                            for (int n = 0; n < str_tel.length; n++) {
                                Thread.sleep(500);
                                String str_tel_id = str_tel[n].toString();

                                _sms.CONTENT = _sms.CONTENT.replace("如不想再收到此端口下发的短信，请回复00000", "");

                                String str_xml = "";
                                str_xml = str_xml + "<?xml version=\"1.0\" encoding=\"GBK\"?>";
                                str_xml = str_xml + "<SERVICE>";
                                str_xml = str_xml + "<DATASOURCE TYPE=\"Procedure\">";
                                str_xml = str_xml + "<SQL NAME=\"cp_sm_ins_queueex\" CONTENT=\"cp_sm_ins_queueex\">";
                                str_xml = str_xml + "<PARAM NAME=\"i_Telephone\" TYPE=\"12\" TAG=\"0\" VALUE=\"" + _sms.TEL + "\"/>";
                                str_xml = str_xml + "<PARAM NAME=\"i_BussType\" TYPE=\"12\" TAG=\"0\" VALUE=\"SERVICE_LINKAGE\"/>";
                                str_xml = str_xml + "<PARAM NAME=\"i_Content\" TYPE=\"12\" TAG=\"0\" VALUE=\"" + _sms.CONTENT + "\"/>";
                                str_xml = str_xml + "<PARAM NAME=\"i_Areaid\" TYPE=\"12\" TAG=\"0\" VALUE=\"" + _sms.CITYID + "\"/>";
                                str_xml = str_xml + "<PARAM NAME=\"i_CallPhone\" TYPE=\"12\" TAG=\"0\" VALUE=\"\"/>";
                                str_xml = str_xml + "<PARAM NAME=\"o_RetVal\" TYPE=\"4\" TAG=\"1\" VALUE=\"\"/>";
                                str_xml = str_xml + "<PARAM NAME=\"o_SerialNum\" TYPE=\"4\" TAG=\"1\" VALUE=\"\"/>";
                                str_xml = str_xml + "</SQL>";
                                str_xml = str_xml + "</DATASOURCE>";
                                str_xml = str_xml + "</SERVICE>";
                                                            

                                Object[] fn01 = {str_xml};
                                String val = (String) call.invoke(fn01);

                                StringReader read = new StringReader(val);
                                InputSource source = new InputSource(read);
                                SAXBuilder sb = new SAXBuilder();

                                Document doc = sb.build(source);
                                Element root = doc.getRootElement();
                                List jiedian = root.getChildren();
                                Namespace ns = root.getNamespace();
                                Element et = null;
                                String str_o_RetVal = null;//返回的短信发送结果  0成功  1失败
                                String str_o_SerialNum = null;//返回的流水号

                                for (int i2 = 0; i2 < jiedian.size(); i2++) {
                                    et = (Element) jiedian.get(i2);
                                    if (i2 == 0) {
                                        str_o_RetVal = et
                                                .getAttributeValue("VALUE");
                                    }
                                    if (i2 == 1) {
                                        str_o_SerialNum = et
                                                .getAttributeValue("VALUE");
                                    }
                                }

                                log.info("########" + str_o_RetVal + "#######" + str_o_SerialNum + "\r\n" + str_xml);

                                Boolean bsbs = Boolean.valueOf(str_o_RetVal.equals("0"));
                                if (bsbs.booleanValue()) {//发送成功的
                                    try {
                                        //更新用综调接口发出的短信的状态
                                        boolean bs = db.update_status_zongdiao(_csnms, str_o_SerialNum, _sms.STR_ID, true, log);
                                        if (bs) {
                                            // 更新当前告警状态,是否有短信发出                                            
                                            db.update_alarm_info2(_csnms, _sms, true, log);
                                        } else {
                                            // 更新当前告警状态,是否有短信发出       
                                            db.update_alarm_info2(_csnms, _sms, false, log);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log.info("短信发送成功后，更新短信状态时出现异常：" + e.getMessage().toString());
                                    }

                                } else {//发送失败的   
                                        log.info("发送的xml字符串：" + str_xml);
                                    try {
                                        boolean bs = db.update_status_zongdiao(_csnms, "-1", _sms.STR_ID, false, log);
                                        if (bs) {
                                            // 更新当前告警状态,是否有短信发出       
                                            db.update_alarm_info2(_csnms, _sms, false, log);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log.info("短信发送失败后，更新短信状态时出现异常：" + e.getMessage().toString());
                                    }
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info(e.getMessage());
                    }
                }
            }

            public void send_sms_donghuan(List _msm_data) {
                List _msm_list = new ArrayList();
                if (_msm_data.size() > 0) {
                    try {
                        // 写入动环数据库
                        for (int i = 0, m = _msm_data.size(); i < m; i++) {
                            //声明实体类并赋值
                            z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
                            _sms = (z.allClass.sendsms_sendstr) _msm_data.get(i);
                            _sms.CONTENT = _sms.CONTENT.replace("如不想再收到此端口下发的短信，请回复00000", "");
                            _sms.CONTENT = _sms.CONTENT.toString() + " 如不想再收到此端口下发的短信，请回复00000";

                            boolean bs = db.insert_sms_donghuan(_donghuan_mysql, _sms, log);
                            if (bs) {
                                _sms.STATUS = "1";// 写入动环数据库成功
                                log.info("[" + _sms.STR_ID.toString() + "]写入动环数据库成功");
                            } else {
                                _sms.STATUS = "-2";// 写入动环数据库失败
                                log.info("[" + _sms.STR_ID.toString() + "]写入动环数据库失败");
                            }
                            _msm_list.add(_sms);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("动环短信发送模块异常：\r\n" + ex.getMessage().toString());
                    }
                }

                if (_msm_list.size() > 0) {
                    // 更新短信状态到CSNMS
                    for (int i = 0, m = _msm_list.size(); i < m; i++) {
                        String _jindu = "进度：" + i + "/" + m + "";
                        z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
                        _sms = (z.allClass.sendsms_sendstr) _msm_list.get(i);
                        try {
                            boolean bs = db.update_in_status_donghuan(_csnms, _sms.STATUS, _sms.STR_ID, log);
                            if (bs) {
                                log.info("NO.ID#" + _jindu + "#" + _sms.STR_ID + "#" + _sms.STATUS + " 更新状态[HASDH]成功");
                            } else {
                                log.info("NO.ID#" + _jindu + "#" + _sms.STR_ID + "#" + _sms.STATUS + " 更新状态[HASDH]失败");
                            }
                        } catch (Exception e) {
                            log.info("更新状态[HASDH]出现异常：\r\n" + "\r\nERROR：" + e.getMessage().toString());
                        }
                    }
                }

            }

        };
    }

    private Runnable task_up() { // 0成功 1返回错误回执 2没有回执
        return new Runnable() {
            public void run() {
                while (true) {
                    try {
                        up_sms_main();
                        Thread.sleep(1000 * 3);// 间隔轮询一次
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info(ex.getMessage().toString());
                    }

                }
            }

            public void up_sms_main() {
                // 加载需要更新状态的短信
                List _msm_data = db.loaddataupdate(log, _csnms);
                // 数据到动环数据库中查询下结果
                if (_msm_data.size() > 0) {
                    // 获取短信在动环数据库中的状态
                    List _msm_data_new = jc_sms_donghuan(_msm_data);
                    //更新数据到oracle库
                    up_sms_send_status2(_msm_data_new);
                }
            }

            public List jc_sms_donghuan(List _msm_data) {
                List _msm_list = new ArrayList();
                if (_msm_data.size() > 0) {
                    _msm_list.clear();
                    // System.out.println("开始检查短信状态");
                    try {

                        for (int i = 0, m = _msm_data.size(); i < m; i++) {
                            z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
                            _sms = (z.allClass.sendsms_sendstr) _msm_data.get(i);

                            //查询动环的短信发送表                         
                            List _list_data = new ArrayList();
                            _list_data = db.loaddonghuan_send(_donghuan_mysql, _sms.STR_ID, log);
                            //处理数据
                            if (_list_data.size() > 0) {
                                HashMap map = (HashMap) _list_data.get(0);
                                if (map.get("STATUS") != null) {
                                    String _STATUS = map.get("STATUS").toString();
                                    if (_STATUS.equals("1")) {
                                        _sms.STATUS = "1"; // 失败
                                        _msm_list.add(_sms);
                                    } else if (_STATUS.equals("0")) {
                                        _sms.STATUS = "0";// 成功 发送成功的会移到历史表
                                        _msm_list.add(_sms);
                                    }
                                }
                            } else {
                                //查询历史表
                                List _list_data2 = new ArrayList();
                                _list_data2 = db.loaddonghuan_his(_donghuan_mysql, _sms.STR_ID, log);
                                if (_list_data2.size() > 0) {
                                    // 未查到数据------------------------------------------------------------------------------------
                                    _sms.STATUS = "0";// 成功 发送成功的会移到历史表
                                    _msm_list.add(_sms);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("出现异常：\r\nERROR：" + ex.getMessage().toString());
                    } finally {

                    }
                }
                return _msm_list;
            }

            public void up_sms_send_status2(List _msm_data_new) {
                if (_msm_data_new.size() > 0) {
                    // 更新短信写入状态到CSNMS
                    // System.out.println("开始更新短信最终状态");
                    for (int i = 0, m = _msm_data_new.size(); i < m; i++) {
                        z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
                        _sms = (z.allClass.sendsms_sendstr) _msm_data_new.get(i);
                        String str_sql_up = "";
                        if (_sms.STATUS.equals("0")) {// 回执为成功的
                            String _time = tools.systime_prase_string("");
                            String _SERIALNUM = _time.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
                            //更新csnms中短信状态
                            boolean bs = db.update_status_donghuan(_csnms, _SERIALNUM, _sms.STR_ID, true, log);
                            if (bs) {
                                // 更新当前告警短信发送状态
                                db.update_alarm_info2(_csnms, _sms, true, log);
                                log.info("NO.ID##" + _sms.STR_ID + " 更新状态[SERIALNUM]成功");
                            } else {
                                log.info("NO.ID##" + _sms.STR_ID + " 更新状态[SERIALNUM]出错");
                            }
                        } else if (_sms.STATUS.equals("1")) {// 回执为 失败，或者无回执的                          
                            String _SERIALNUM = "-1";
                            //更新csnms中短信状态
                            boolean bs = db.update_status_donghuan(_csnms, _SERIALNUM, _sms.STR_ID, false, log);
                            if (bs) {
                                // 更新当前告警短信发送状态
                                db.update_alarm_info2(_csnms, _sms, false, log);
                                log.info("NO.ID##" + _sms.STR_ID + " 更新状态[SERIALNUM]成功");
                            } else {
                                log.info("NO.ID##" + _sms.STR_ID + " 更新状态[SERIALNUM]出错");
                            }
                        }
                    }
                }
            }
        };
    }
}
