package z.zy_atm;

import java.io.IOException;

/**
 *
 * 功能： 实现mysql库里面的path与oracle库里的path数据同步
 *
 * @author Liujt
 */
public class main implements Runnable {

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
        
        //------------------------------------------------同步path
        path P = new path();
        P.setCsnms(_csnms);
        P.setLog(log);
        P.setMysql(_mysql);
        Thread thread = new Thread(P); 
        thread.start();
        
        //------------------------------------------------同步node
        node N=new node();
        N.setCsnms(_csnms);
        N.setLog(log);
        N.setMysql(_mysql);
        Thread no=new Thread(N);
        no.start();
        //------------------------------------------------同步pathtrace
        pathtrca pt=new pathtrca();
        pt.setCsnms(_csnms);
        pt.setLog(log);
        pt.setMysql(_mysql);
        Thread PT=new Thread(pt);
        PT.start();
        log.info("[同步完成]");
        
    }
    
   /** public void path() throws IOException {

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
    }**/
    
    /**public void node() throws IOException {
        //-----------------------------取数据----------------------------------
        List mysql_node = db._nodedata(log, _csnms, _mysql);
        if (mysql_node.size() > 0) {
            boolean re = false;
            for (int i = 0, m = mysql_node.size(); i < m; i++) {
                strclass.node node_datal = (strclass.node) mysql_node.get(i);
                re = db._nodeoracle(log, _csnms, _mysql, node_datal);
                if (re) {
                    //根据pathid更新数据
                    db.update_node(_csnms, node_datal);
                    log.info("[存在数据]:已完成更新 path id :" + node_datal.node_id);
                } else {
                    //添加数据
                    int resu = db.intsert_oraclenode(_csnms, node_datal);
                    if (resu > 0) {
                        System.out.println("插入成功！！！");
                    }
                }
            }
        }
    }**/
    
    public void pathtrace() throws IOException {

        /**
         * //取数据mysql的数据 List mysql_pathtrace = db._pathtracedata(log, _csnms,
         * _mysql);
         *
         * //判断是否取到数据 if (mysql_pathtrace.size() > 0) { for (int i = 0, m =
         * mysql_pathtrace.size(); i < m; i++) {
         *
         * // 逐条取出所有信息
         * fun.mysql_path pathid_datal = (fun.mysql_path) mysql_pathtrace.get(i);
         * //***************添信息**************************
         * List insert = new ArrayList();
         * String name = pathid_datal.name;
         * int network = pathid_datal.network_id;
         * int aendport = pathid_datal.aendport;   //aEndPort
         * long path = pathid_datal.path_id;
         * insert.add(name);
         * insert.add(network);
         * insert.add(path);
         * insert.add(aendport);
         *
         * //拿mysql的pathid到oracle里去匹配
         * boolean re = db._pathoracle(log, _csnms, _mysql, path);
         *
         * //如果oracle里面有，更新数据
         * if (re) {
         * //根据pathid更新数据
         * db.update_path(_csnms, pathid_datal);
         * log.info("[存在数据]:已完成更新 path id :" + pathid_datal.path_id);
         * } //如果oracle里面没有，添加之后在更新
         * else {
         * //添加数据
         * db.intsert_oraclepath(_csnms, insert);
         * //更新数据
         * try {
         * int res = db.update_path(_csnms, pathid_datal);
         * if (res > 0) { log.info("[存在数据]:已完成更新 path id :" +
         * pathid_datal.path_id); } else { log.info("【更新失败】" +
         * pathid_datal.path_id); } } catch (Exception e) { e.printStackTrace();
         * } } }
        }*
         */
    }
}
