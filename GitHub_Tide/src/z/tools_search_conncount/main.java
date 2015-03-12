/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.tools_search_conncount;

import z.send_alarm.*;
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
 * @author 功能： 查询数据库连接数并计入日志 共核查
 * @author 描述：
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_tools_conncount.config");
    }
    
  //  zhang,kehu,wamg,dian   
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();   //声明数据库

    public void run() {//启动线程      
        if (db.ini(log, _csnms)) {  //加载数据库
            while (true) {
                try {
                    doing_main();  //运行主方法  进行查询并记录数据
                    Thread.sleep(1000 * 10);//5秒执行一次
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.info("程序运行异常：" + ex.getMessage().toString());
                }

            }
        } else {
            log.info("数据库初始化失败");
        }
    }

    public void doing_main() {
        List _list = db.loaddata(_csnms);
        if (_list.size() > 0) {
            String mes = "";
            for (int i = 0; i < _list.size(); i++) {
                HashMap map = (HashMap) _list.get(i);
                String count = "";
                String type = "";
                String times = "";
                if (map.get("COUNT") != null) {
                    count = map.get("COUNT").toString();
                }
                if (map.get("TYPE") != null) {
                    type = map.get("TYPE").toString();
                }
                if (map.get("TIMES") != null) {
                    times = map.get("TIMES").toString();
                }
                if (i > 0) {
                    mes += "####";
                }else{
                 mes="执行时间："+times+"####";
                }
                mes += type + ":" + count ;
            }
            log.info(mes);
        }
    }
}
