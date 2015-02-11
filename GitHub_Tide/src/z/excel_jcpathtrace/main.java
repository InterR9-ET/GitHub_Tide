package z.excel_jcpathtrace;

import z.xn_port.*;
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
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_excel_jcpathtrace.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();

    /**
     * 程序初始化
     */
    public void run() {
        if (db.ini(log, _csnms)) {
            //while (true) {
            System.out.println("开始运行");
            try {
                //主程序
                doing_main();
                //Thread.sleep(1000 * 60);//60秒
            } catch (Exception e) {
                e.printStackTrace();
            }
            // }
        }
    }

    /**
     * 主程序
     */
    public void doing_main() {
//
        String fileurl_old = "";
        String fileurl_new = "";
        fileurl_old = "表1：VPN和ATM电路核查表-全省2月10日_bk.xls";
        fileurl_new = "表1：VPN和ATM电路核查表-全省2月10日.xls";

        File directory = new File("");// 设定为当前文件夹
        try {
            boolean bs = fun.copy_file(fileurl_old, fileurl_new);
            if (bs) {
                String _url = directory.getCanonicalPath().toString() + "/file/excel/VPN和ATM电路核查表/" + fileurl_new;//全路径
                boolean bs1 = fun.loadfile(_url, "VPN电路");//加载文件成功
                if (bs1) {
                    fun.jc_data(_csnms);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
