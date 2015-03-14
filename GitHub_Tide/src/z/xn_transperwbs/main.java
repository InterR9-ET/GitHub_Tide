package z.xn_transperwbs;

import java.util.List;

/**
 * 
 * @author Administrator
 */
public class main extends Thread {
    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger
    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_webservice.config");
    }
    //-------------------------------------日志---------------------------------------//
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms(); 
    //private static util.GetThread.thread _thread = new util.GetThread.thread(2);
    private static util.GetThread.thread _thread = new util.GetThread.thread(1);
  
    public static String wbs_url = "http://132.228.241.205:8080/tnms/services/pmService";
    

    public static int count = 0;
    
    //--------------------------判断数据库加载状态--------------------------------
    public void run() {
        boolean _bs_test = true;
        if (_bs_test) {
            fun.do_test("UUID:fe9f932d-cabc-11e3-8690-0050569a1a8a",wbs_url);
        } else {
            if (db.ini(_csnms)) {
                while (true) {
                    try {
                        _thread = new util.GetThread.thread(10);
                        doing_main();
                        Thread.sleep(1000 * 60);//60秒
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                log.info("数据库加载失败");
            }
        }

    
    }

    public void doing_main() {
        try {
            log.info("###开始时间" + tools.systime_prase_string(""));
            count = 0;
            //-----------------------多线程运行------------------------//
            //运行任务  
            //获取UUID的相关数据
            List _list_UUID = db.getUUID(_csnms);
            //开始循环处理UUID
            strclass.UUID _UUID = new strclass.UUID();
            for (int i = 0; i < _list_UUID.size(); i++) { //创建任务
                _UUID = new strclass.UUID();
                _UUID = (strclass.UUID) _list_UUID.get(i);
                //创建任务，添加线程
                _thread.execute(fun.createTask(_UUID,log,_csnms,wbs_url));
                System.out.println("添加任务：" + i);
            }

            //-----------------------------------------------------------//
            _thread.waitFinish(); //等待所有任务执行完毕  
            _thread.closePool(); //关闭线程池  
            //-----------------------多线程运行------------------------//
            log.info("###结束时间" + tools.systime_prase_string(""));

        } catch (Exception e) {
            e.printStackTrace();
        }

    
    }

}
