/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.send.alarm;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author yangzhen
 * @author 功能：告警转发
 * @author 描述：
 * @author 王峰将紫图告警写入转发表(以扩展支持其他人写入类似的告警字符串)
 * @author 此程序会将告警转发给告警服务器，转发完成后从数据表中删除数据
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_sendalarm.config");
    }
    //-------------------------------------日志---------------------------------------//

    private static util.GetThread.thread _thread = new util.GetThread.thread(2);   //声明多线程 
    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();   //声明数据库

    public void run() {//启动线程      
        if (db.ini(log,_csnms)) {  //加载数据库
            doing_main();  //运行主方法
        } else {
            log.info("数据库初始化失败");
        }
    }

    public void doing_main() {
        try {
            _thread.execute(createTask1());//多线程运行1    告警发送线程   转发告警  成功之后删除转发表的告警
            _thread.execute(createTask2());//多线程运行2    心跳线程  防止长时间无告警时造成socket断开
            _thread.waitFinish(); //等待所有任务执行完毕  
            _thread.closePool(); //关闭线程池 
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("程序出现异常[" + ex.getMessage().toString() + "]");
        } finally {

        }
    }

    private static Runnable createTask2() throws Exception {
        return new Runnable() {
            public void run() {
                log.info("心跳线程启动");
                while (true) {
                    z.allClass.sendalarm_sendtable _alarm = new z.allClass.sendalarm_sendtable();
                    util.GetSocket.alarms _alarms = new util.GetSocket.alarms();
                    boolean bs = _alarms.sendAlarm("紫图心跳");
                    if (bs) {
                        log.info("心跳发送成功");
                    } else {
                        log.info("心跳发送失败");
                    }
                    try {
                        Thread.sleep(1000 * 30);//中断30秒再执行
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private static Runnable createTask1() throws Exception {
        return new Runnable() {
            public void run() {

                log.info("告警发送线程启动");
                util.GetSocket.alarms alarmsocket = new util.GetSocket.alarms();//加载 socket 工具类

                while (true) {
                    try {
                        Thread.sleep(1000 * 5);//间隔5秒循环一次执行
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //获取告警数据
                    List list = new ArrayList();
                    list = db.loaddata(_csnms);

                    if (list.size() > 0) {
                        //申明一个实体类   
                        z.allClass.sendalarm_sendtable _alarm = new z.allClass.sendalarm_sendtable();
                        for (int i = 0; i < list.size(); i++) {
                            log.info("进度：" + (i + 1) + "/" + list.size());
                            HashMap map = (HashMap) list.get(i);
                            //重复初始化使用 省资源  
                            _alarm = new z.allClass.sendalarm_sendtable();
                            //对实体类赋值                           
                            _alarm.ID = map.get("ID").toString();
                            _alarm.ALARM = map.get("ALARM").toString();
                            //发送告警
                            boolean bs = alarmsocket.sendAlarm(_alarm.ALARM);
                            if (bs) {
                                log.info("告警发送成功：Alarm_id:" + _alarm.ID + "\r\n" + _alarm.ALARM + "\r\n");
                                //删除发送成功的告警                                
                                if (db.deldata(_csnms, _alarm.ID)) {
                                    log.info("删除告警成功：" + _alarm.ID);
                                } else {
                                    log.info("删除告警失败：" + _alarm.ID);
                                }
                            } else {
                                log.info("告警发送失败：" + _alarm);
                            }
                            if (i == list.size() - 1) {
                                list.clear();//释放资源
                            }
                        }
                    }
                }
            }
        };
    }
}
