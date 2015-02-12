/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.alarm_hecha;

/**
 *
 * @author zhen
 *
 */
public class main extends Thread {
    
    
    private static util.GetSql.csnms _csnms = new  util.GetSql.csnms();
    private static  util.GetSql.zhizhen _zhizhen = new  util.GetSql.zhizhen();
    private static  util.GetSql.ess _ess = new  util.GetSql.ess();
    private static  util.GetTools.tools _tools = new  util.GetTools.tools();
    private static  util.GetFile.txt _txt = new  util.GetFile.txt();

    public  void run() {
        
        
        
        
        
        
        
        strclass.ALARM_MES _mes = new strclass.ALARM_MES();
        _mes.SE_ALARM_ID = "";
        _mes.SE_PATH_NAME = "广州苏州ETN0001NP";
        _mes.SE_ALARM_SER_ID = "";
        _mes.SE_ALARM_MONTH = "1";
        _mes.SE_VERDOR = "";
       jcziyuan _do = new jcziyuan();
        _mes.SE_PATH_NAME = _mes.SE_PATH_NAME.toString().trim();
        _do.run(_mes);

        
        //读取log
        String message = "";
        String wg = "";

        message = "木渎";
        //wg="南京";
        int _day =9;
        alarm.getdata_to_excel _do2 = new alarm.getdata_to_excel();  //写入Excel       
       _do2.run(message,wg,_day);

        //写入数据库
        //alarm.getdata_to_database _do = new alarm.getdata_to_database();
    }

}
