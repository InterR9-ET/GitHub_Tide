package z.xn_transper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xntransper.config");
    }
    //-------------------------------------日志---------------------------------------//

    public static Hashtable keyHash = new Hashtable();
    public static List list_perfinstance = new ArrayList();

    private static util.GetFile.excel _excel = new util.GetFile.excel();
    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();

    public void run() {
        if (db.ini(log, _csnms)) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 30);//60秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doing_main() {
        try {
            log.info("[——程序开始执行——]");
            keyHash.clear();
            File file = new File("transper/");
            File[] files = file.listFiles();
            Arrays.sort(files);//文件进行排序

            List _list = new ArrayList();
            boolean _bhj = false;
            System.out.println("==========文件列表==========");
            for (int i = 0, len = files.length; i < len; i++) {
                if (!files[i].isDirectory()) {
                    //--------------------流量
                    if (files[i].toString().indexOf("circuit_pm") != -1) {
                        String fil = files[i].toString();
                        System.out.println(fil);
                        _bhj = true;
                    }
                }
            }
            System.out.println("==========文件列表==========");

            if (_bhj) {
                log.info("加载perfinstance");
                list_perfinstance = db.load_perfinstance(_csnms);
                log.info("perfinstance数据:" + list_perfinstance.size());
            }

            Thread.sleep(1000 * 1);//60秒

            for (int i = 0, len = files.length; i < len; i++) {

                if (!files[i].isDirectory()) {
                    //--------------------流量
                    if (files[i].toString().indexOf("circuit_pm") != -1) {
                        //
                        db.TRUNCATE(files[i], _csnms);
                        System.out.println("历史数据清除");
                        //开始解析获取数据 
                        String fil = files[i].toString();
                        _list = fun.jiexi2(fil);

                        //将解析的数据写入数据库
                        if (_list.size() > 0) {
                            fun.indata2(_list, log, _csnms);//执行写入   
                        }
                        Thread.sleep(1000 * 1);//2秒
                        
                        
                        //解析完成之后拷贝文件
                        File directory = new File("");
                        String bfpath = "/file/xingneng/transper";
                        String _url = directory.getCanonicalPath() + bfpath;
                        File file2 = new File(fil);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            
                        //解析完成之后删除文件
                        file2.delete();
                        log.info("删除文件:" + fil);
                        Thread.sleep(1000 * 1);//2秒

                        list_perfinstance.clear();
                        fun.doing_main2(_csnms, log);
                    }
                }
            }
            _list.clear();
            keyHash.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
