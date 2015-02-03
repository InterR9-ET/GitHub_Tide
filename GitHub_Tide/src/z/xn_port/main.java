package z.xn_port;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;

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
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnport.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetSql.donghuan_mysql _donghuan_mysql = new util.GetSql.donghuan_mysql();
    private static util.GetThread.thread _thread = new util.GetThread.thread(10);
    private static util.GetTools.tools _tools = new util.GetTools.tools();
    private static util.GetFile.excel _excel = new util.GetFile.excel();
    public static Hashtable lst_p2 = new Hashtable();

    /**
     * 程序初始化
     */
    public void run() {
        if (db.ini(log,_csnms)) {
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
        List file = new ArrayList();
        List file3 = new ArrayList();
        File file1 = new File("portper/");
        File file2 = new File("portper/onu");
        file = new fun().huoqwuwenjian(file1);    //获取文件
        file3 = new fun().huoqwuwenjian(file2);   //获取文件
        file.addAll(file3);

        List list_in = new ArrayList();
        fun.port _port = new fun.port();
        /**
         * 加载数据用于进行筛选
         */
        List lst_p = db.get_device_data(_csnms);  //调用get_device_data，获取device_id，去除重复的
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
        fun f=new fun();
        f.pand(log,file,_thread,_csnms);                //文件数量大于0
    }
}
