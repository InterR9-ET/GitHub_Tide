/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.zy_atm;

import java.io.IOException;
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
                    String filename=fun.log_path();
                    if(filename.length()>0){
                    System.out.println("-------------日志文件创建成功-------------");
                    }else{
                    System.out.println("*************日志文件创建失败*************");
                    }
                    System.out.println("---------数据库打开成功，主程序开始执行-----------");
                    doing_main(filename);
                    Thread.sleep(1000 * 60*60*6);//6个小时
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("加载数据库失败");
        }
    }

    public void doing_main(String filename) throws IOException {
        //取数据
        List mysql_path = db.get_xn_atm(_csnms, _mysql,filename);
        //判断是否取到数据
        if (mysql_path.size() > 0) {
            //添加数据
            db.tb_in_up(_csnms, mysql_path,filename);
            //更新数据
            db.update_path(_csnms, mysql_path,filename);
        }
        System.out.println("*************线程执行完一次***************");
    }
}
