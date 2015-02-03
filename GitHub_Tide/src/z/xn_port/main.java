package z.xn_port;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author yangzhen
 * @author 功能：端口性能数据（流入，流出）解析ftp文件并写入数据库
 * @author 描述：
 * @author
 * @author
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnport.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetSql.donghuan_mysql _donghuan_mysql = new util.GetSql.donghuan_mysql();
    private static util.GetThread.thread _thread = new util.GetThread.thread(10);
    private Hashtable lst_p2 = new Hashtable();

    /**
     * 程序初始化
     */
    public void run() {
        if (db.ini(log, _csnms)) {
            while (true) {
                try {
                    //主程序
                    doing_main();
                    Thread.sleep(1000 * 5);//60秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 主程序
     */
    public void doing_main() {
        List file_list_all = new ArrayList();
        List file_list1 = new ArrayList();
        List file_list2 = new ArrayList();

        File file1 = new File("portper/");
        File file2 = new File("portper/onu");

        file_list1 = new fun().huoqwuwenjian(file1);    //获取文件
        file_list2 = new fun().huoqwuwenjian(file2);   //获取文件

        file_list_all.addAll(file_list1);//合并list
        file_list_all.addAll(file_list2);//合并list

        List list_in = new ArrayList();
        fun.port _port = new fun.port();
        /**
         * 加载数据用于进行筛选
         */
        List lst_p = db.get_device_data(_csnms);  //调用get_device_data，获取device_id，去除重复用
        int m = lst_p.size();
        for (int p = 0; p < m; p++) {
            HashMap map = (HashMap) lst_p.get(p);
            String key = map.get("DEVICEID").toString();
            //去除重复的
            if (!lst_p2.containsKey(key)) {
                lst_p2.put(key, key);
            }
        }
        lst_p.clear();
        fun f = new fun();
        //日志、文件名、线程池、数据连接
        f.pand(log, file_list_all, _thread, _csnms, lst_p2);                //文件数量大于0
    }
}
