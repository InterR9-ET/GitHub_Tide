package z.xn_jason;

/**
 *
 * @author yangzhen
 * @author 功能：jason格式的性能文件解析
 * @author 描述：
 * @author
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnjason.config");
    }
    private static util.GetTools.tools _tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetFile.A_example _file = new util.GetFile.A_example();

    //------------------------------程序运行入口---------------------------------
    public void run() {
        if (db.ini(_csnms)) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 60 * 5 + 2000);//10秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            log.info("数据库加载失败");
        }
    }

    public void doing_main() {
        try {
            //加载数据的文件
            boolean _bs = fun.downFile(_file,log);
            if (_bs) {
                //处理数据文件
                boolean _bs2 = fun.read_file(_file, _csnms, _tools,log);
                if (_bs2) {
                    log.info("处理完成");
                } else {
                    log.info("处理失败");
                }
            } else {
                log.info("下载文件失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
