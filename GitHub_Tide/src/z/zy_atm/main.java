package z.zy_atm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 功能： 实现mysql库里面的path与oracle库里的path数据同步
 *
 * @author Liujt
 */
public class main {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(z.xn_atm.main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_zyatm.config");
    }
    //-------------------------------------日志---------------------------------------//
    private util.GetSql.atm_mysql _mysql = new util.GetSql.atm_mysql();
    private util.GetSql.csnms _csnms = new util.GetSql.csnms();

//------------------------------数据库加载--------------------------------------
    public void run() {
        if (!db.ini(log, _csnms, _mysql)) {
            log.info("加载数据库失败");
        } else {
            while (true) {
                try {
                    log.info("开始程序运行");
                    doing_main();
                    log.info("程序完成");
                    Thread.sleep(1000 * 60 * 60 * 1);//6个小时
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

//------------------------------主程序---------------------------------------------
    public void doing_main() throws IOException {
        //取数据mysql的数据
        List mysql_path = db._pathdata(log, _csnms, _mysql);
        //判断是否取到数据
        if (mysql_path.size() > 0) {
            for (int i = 0, m = mysql_path.size(); i < m; i++) {

                // 逐条取出所有信息
                fun.mysql_path pathid_datal = (fun.mysql_path) mysql_path.get(i);
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
                boolean re = db._pathoracle(log, _csnms, _mysql, path);

                //如果oracle里面有，更新数据
                if (re) {
                    //根据pathid更新数据
                    db.update_path(_csnms, pathid_datal);
                    log.info("[存在数据]:已完成更新 path id :" + pathid_datal.path_id);
                } //如果oracle里面没有，添加之后在更新
                else {
                    //添加数据
                    db.intsert_oraclepath(_csnms, insert);
                    //更新数据
                    try {
                        int res = db.update_path(_csnms, pathid_datal);
                        if (res > 0) {
                            log.info("[存在数据]:已完成更新 path id :" + pathid_datal.path_id);
                        } else {
                            log.info("【更新失败】" + pathid_datal.path_id);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
