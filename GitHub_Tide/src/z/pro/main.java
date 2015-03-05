package z.pro;

import z.xn_atm.*;
import java.util.List;

/**
 *
 * @author yangzhen
 * @author ------------------------------------------------------检测PING性能文件
 * @author ------------------------------------------------------检测PORT性能文件
 * @author ------------------------------------------------------检测NODE性能文件
 * @author ------------------------------------------------------检查atm性能采集
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_pro.config");
    }
    private util.GetTools.tools _tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//   
    private util.GetSql.csnms _csnms = new util.GetSql.csnms();

    //-------------------------------------程序入口------------------------------------
    public void run() {
        if (db.ini(log,_csnms)) {
            doing_main();
        } else {
            log.info("数据库加载失败");
        }
    }

    //-------------------------------------主程序------------------------------------
    public void doing_main() {
		try {
			log.info("开始运行:" + _tools.systime_prase_string("")+ "---------------------------");
			// 检测功能配置
			fun.jc_doing(log,_csnms);
			// 输出结果
			//fun.out_result(log);
			log.info("运行结束:" + _tools.systime_prase_string("")
					+ "---------------------------");
		} catch (Exception ex) {
			ex.printStackTrace();
			log.info("异常：" + ex.getMessage());
		}

	
    }

}
