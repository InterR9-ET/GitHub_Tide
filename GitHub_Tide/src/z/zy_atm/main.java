/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.zy_atm;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class main {

    //-------------------------------------日志---------------------------------------//

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(z.xn_atm.main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnatm.config");
    }
    private util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//
    private util.GetSql.atm_mysql _mysql = new util.GetSql.atm_mysql();
    private util.GetSql.csnms _csnms = new util.GetSql.csnms();

    public void run() {
        if (db.ini(log, _csnms, _mysql)) {
            while (true) {
                try {
                    System.out.println("***********数据库打开成功，主程序开始执行*************");
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
        List mysql_path = db.get_xn_atm(_csnms, _mysql);
        if (mysql_path.size() > 0) {
            db.tb_in_up(_csnms, mysql_path);
            db.update_path(_csnms, mysql_path);
        }
        System.out.println("*************线程执行完一次***************");
    }
}
