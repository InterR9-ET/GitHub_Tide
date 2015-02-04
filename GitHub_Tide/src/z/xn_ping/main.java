package z.xn_ping;

import java.io.File;
import java.io.IOException;
import java.util.List;



/**
 *
 * @author yangzhen
 * @author 功能：ping数据的 解析入库
 * @author 描述：
 *
 */
public class main extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger
    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_xnping.config");
    }
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms(); 
    private static util.GetThread.thread _thread = new util.GetThread.thread(2);

  

    public void run() {
        //加载数据库
        if (db.ini(log,_csnms)) {
            while (true) {
                try {
                    doing_main();
                    Thread.sleep(1000 * 60 * 1);//60秒
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void doing_main() throws IOException {
       File file1 = new File("pingper/");//-------------------------------------------------------------
       File file2 = new File("pingper/dcn/");//                                 获
       File file3 = new File("pingper/wx/");//                                  
       File file4 = new File("pingper/lyg/");//                                 取
       File file5 = new File("pingper/jyw/");//                                 
       File file6 = new File("pingper/sz/");//                                 
       fun f=new fun();                     //                                  件
       List file=f.huoqwuwenjian(file1);//                             
       List file22=f.huoqwuwenjian(file2);//                                    文
       List file33=f.huoqwuwenjian(file3);//                                 
       List file44=f.huoqwuwenjian(file4);//                                    
       List file55=f.huoqwuwenjian(file5);//                                 
       List file66=f.huoqwuwenjian(file6);//                                 
      if(file22.size()>0){
          file.add(file22);
      } if(file33.size()>0){
          file.add(file33 );// 
      }  
      if(file44.size()>0){
       file.add(file44);//
      }            
      if(file55.size()>0){
       file.add(file55);//  
      }             
      if(file66.size()>0){
       file.add(file66);//--------------------------------------------------------------------------------
      }
       //传入文件名，处理文件
       f.chulwj(log, file, _csnms);
    }

}
