/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.zy_atm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能： 实现mysql库里面的path与oracle库里的path数据同步
 * run()---------------------------------数据库加载 doing_main(String
 * filename)-----------主程序 filename：日志文件
 *
 * @author Liujintai
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
//------------------------------数据库加载--------------------------------------

    public void run() {
        if (db.ini(log, _csnms, _mysql)) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 60 * 60 * 6);//6个小时
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("加载数据库失败");
        }
    }
//------------------------------主程序---------------------------------------------

    public void doing_main() throws IOException {

        //取数据,filename:日志文件
        List mysql_path = db._pathdata(log,_csnms, _mysql);
        //判断是否取到数据
        if (mysql_path.size() > 0) {
            for (int i = 0, m = mysql_path.size(); i < m; i++) {
                // 取出mysql的pathid
                
                fun.mysql_path pathid_datal = new fun.mysql_path();
                pathid_datal = (fun.mysql_path) mysql_path.get(i);
                //***************添信息**************************
                List insert = new ArrayList();
                String name = pathid_datal.name;
                int network = pathid_datal.network_id;
                int aendport = pathid_datal.aendport;   //aEndPort
                long path = pathid_datal.path_id;
                insert.add(name);
                insert.add(network);
                insert.add(path);
                insert.add(aendport);

                //拿mysql的pathid到oracle里去匹配
                boolean re = db._pathoracle(log,_csnms, _mysql, path);

                //如果oracle里面有，更新数据
                if (re) {
                    //根据pathid更新数据
                    db.update_path(_csnms, pathid_datal);
                    System.out.println("****************存在数据，已完成更新***************");
                } //如果oracle里面没有，添加之后在更新
                else {
                    //添加数据
                    db.tb_in_up(_csnms, insert);
                    //更新数据
                    db.update_path(_csnms, pathid_datal);
                    System.out.println("****************新数据，已完成插入***************");
                }
            }
        }
        System.out.println("*************线程执行完一次***************");
    }
}
