package z.send_donghuan_command;

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


/**
 *
 * @author yangzhen
 * @author 功能：动环设备一些数据的更新
 * @author 描述：
 * @author 发送获取数据的命令到动环，根据取到的数据，更新csnms的数据
 *
 * 
 */



public class main extends Thread {

    //-------------------------------日志-------------------------------------// 
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_donghuancommand.config");
    }
    private static util.GetTools.tools _tools = new util.GetTools.tools();
    //-------------------------------日志-------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetSql.donghuan_mysql _donghuan = new util.GetSql.donghuan_mysql();
    private static util.GetThread.thread _thread = new util.GetThread.thread(2);

    public static ResultSet resultset = null;
    public static PreparedStatement prestmt = null;

    public void run() {
        if (db.ini(log, _csnms, _donghuan)) {
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
            _thread.execute(task_up());//更新
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
                        List _list_data = db.load_command_csnms(_csnms);
                        //发送数据
                        if (_list_data.size() > 0) {
                            send_command_main(_list_data);
                            log.info("发送成功" + " " + _tools.systime_prase_string(""));
                        }
                        //延时
                        Thread.sleep(1000 * 5);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("发送失败：" + ex.getMessage() + " " + _tools.systime_prase_string(""));
                    }
                }
            }

            public void send_command_main(List _list_data) {
                //发送到动环
                if (_list_data.size() > 0) {
                    try {
                        //写入动环数据库
                        for (int i = 0, m = _list_data.size(); i < m; i++) {
                            //System.out.println("写入动环数据库");
                            z.allClass.donghuancommand_mes _sms = new z.allClass.donghuancommand_mes();
                            _sms = (z.allClass.donghuancommand_mes) _list_data.get(i);
                            boolean bs = db.insert_command_donghuan(_donghuan, _sms);
                            if (bs) {
                                _sms.STATUS = "3";//写入动环数据库成功
                            } else {
                                _sms.STATUS = "-3";//写入动环数据库失败
                                log.info("#" + _sms.ID + "#" + _sms.TRU_ID + "#" + _sms.COMMAND + ":写入动环数据库失败");
                            }

                            //更新csnms                            
                            boolean bs2 = db.update_command_csnms(_csnms, _sms);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("出现异常：\r\nERROR：" + ex.getMessage().toString());
                    }
                }
            }

        };
    }

    private static Runnable task_up() {
        return new Runnable() {
            public void run() {
                while (true) {
                    try {
                        //加载数据
                        List _list_data = db.load_command_csnms2(_csnms);
                        if (_list_data.size() > 0) {
                            //查询并更新指令状态
                            if (_list_data.size() > 0) {
                                up_sms_main(_list_data);
                            }
                        }
                        //延时
                        Thread.sleep(1000 * 5);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("更新状态失败：" + ex.getMessage());
                    }
                }
            }

            private void up_sms_main(List _msm_data) {
                //数据到动环数据库中查询下结果
                if (_msm_data.size() > 0) {
                    List _msm_data_new = jc_sms_donghuan(_msm_data);
                }
            }

            private List jc_sms_donghuan(List _msm_data) {
                List _msm_list = new ArrayList();
                if (_msm_data.size() > 0) {

                    try {
                        Map map = null;
                        //System.out.println("开始检查指令状态");
                        for (int i = 0, m = _msm_data.size(); i < m; i++) {
                            z.allClass.donghuancommand_mes _sms = new z.allClass.donghuancommand_mes();
                            _sms = (z.allClass.donghuancommand_mes) _msm_data.get(i);
                            if (_sms.TRU_ID.toString().length() > 0) {
                                //获取信号的值
                                _msm_list = db.get_data(log, _donghuan, _csnms, _sms);
                                //更新信号值
                                update_data(_msm_list);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("读取动环数据出现异常：" + ex.getMessage().toString());
                    }
                }
                return _msm_list;
            }

            private void update_data(List _list) {
                int m = _list.size();
                if (m > 0) {
                    boolean _bs = false;
                    String _rtu_id = "";
                    String _command_id = "";

                    for (int i = 0; i < m; i++) {
                        z.allClass.donghuancommand_devicemes _DEVICE_MES = new z.allClass.donghuancommand_devicemes();
                        _DEVICE_MES = (z.allClass.donghuancommand_devicemes) _list.get(i);
                        _rtu_id = _DEVICE_MES.RTU_ID.toString();
                        _command_id = _DEVICE_MES.CSNMS_COMMAND_ID.toString();
                        if (_DEVICE_MES.RTU_ID.length() > 0) {
                            _bs = db.update_powerequitlist(_csnms, _DEVICE_MES);
                            if (!_bs) {
                                StringBuffer _error = new StringBuffer();
                                _error.append("更新数据失败：").append("\r\n");
                                log.info(_error.toString());
                            }
                        }
                    }

                    if (_bs) {
                        if (_rtu_id.length() > 0 && _command_id.length() > 0) {
                            if (db.update_command(_csnms, _command_id, "1")) {
                                StringBuffer _error = new StringBuffer();
                                _error.append("更新数据成功：ID_" + _command_id).append("\r\n");
                                log.info(_error.toString());
                            }
                        } else {
                            StringBuffer _error = new StringBuffer();
                            _error.append("IS NULL：RTU_ID，COMMAND_ID").append("\r\n");
                            log.info(_error.toString());
                        }
                    } else {
                        if (_rtu_id.length() > 0 && _command_id.length() > 0) {
                            db.update_command(_csnms, _command_id, "0");
                        } else {
                            StringBuffer _error = new StringBuffer();
                            _error.append("IS NULL：RTU_ID，COMMAND_ID").append("\r\n");
                            log.info(_error.toString());
                        }
                    }
                }
            }

        };
    }
}
