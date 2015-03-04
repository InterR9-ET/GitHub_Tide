package z.xn_node;

import java.io.File;
import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnnode.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetThread.thread _thread = new util.GetThread.thread(1);
    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();

//-----------------------------------加载数据库-------------------------------------------
    public void run() {
        if (db.ini(_csnms)) {
            while (true) {
                try {
                    _thread = new util.GetThread.thread(10);
                    doing_main();
                    Thread.sleep(1000 * 30);//60秒
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else {
            log.info("数据库加载失败");
        }
    }

//-----------------------------------主函数-------------------------------------------
    public void doing_main() {
        try {
            log.info("###开始时间" + tools.systime_prase_string(""));

            //-------------加载文件-------------//
            File file = new File("nodeper/");
            File[] files = file.listFiles();
            Arrays.sort(files);//排序
            //-------------加载文件-------------//

            if (files.length > 0) {

                log.info("执行多线程");

                for (int i = 0, leng = files.length; i < leng; i++) {
                    if (!files[i].isDirectory()) {
                        if (files[i].toString().indexOf("bulk") != -1) {
                            if (files[i].toString().indexOf("pm") != -1) {
                                log.info("操作文件：" + files[i]);
                                String fil = files[i].toString();
                                //-------------添加多线程任务----------------//
                                _thread.execute(fun.createTask(fil, log, _csnms));
                                System.out.println("添加任务：" + i);
                                //-------------添加多线程任务----------------//
                            }
                        }
                    }
                }

                //------------------等待线程运行---------------//
                _thread.waitFinish(); //等待所有任务执行完毕   
                _thread.closePool(); //关闭线程池        
                //------------------等待线程运行---------------//

                log.info("多线程执行完成");
            }
            log.info("###结束时间" + tools.systime_prase_string(""));

        } catch (Exception e) {
            log.info("程序异常 e：" + e.getMessage().toString());
            e.printStackTrace();
        }

    }

}
