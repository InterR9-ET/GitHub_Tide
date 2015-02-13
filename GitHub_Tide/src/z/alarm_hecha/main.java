/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.alarm_hecha;

import static z.alarm_hecha.fun.com_dake;
import static z.alarm_hecha.db.get_alarm;
import static z.alarm_hecha.db.ini;

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
        
        //核查电路
        String SE_PATH_NAME="互联通(竹辉托管)-国产实业混凝土/30N0001NP";
        //main.Duqpath(SE_PATH_NAME);
        
        //筛选log信息
        String message = "木渎";
        main.Shaixxx(message);
       
    }
    //----------------------------核查电路--------------------------------------
    public static void Duqpath(String SE_PATH_NAME){
        strclass.ALARM_MES _mes = new strclass.ALARM_MES();
        _mes.SE_ALARM_ID = "";
        _mes.SE_PATH_NAME = SE_PATH_NAME;
        _mes.SE_ALARM_SER_ID = "";
        _mes.SE_ALARM_MONTH = "";
        _mes.SE_VERDOR = "";
        //jcziyuan _do = new jcziyuan();
        _mes.SE_PATH_NAME = _mes.SE_PATH_NAME.toString().trim();
        //加载数据库
         if (ini(_csnms,_zhizhen,_ess)) {
            //加载告警信息
            if (_mes.SE_ALARM_ID.length() > 0) {
                _mes = get_alarm(_mes,_csnms);
            }
            //比对数据问题            
            com_dake(_mes,_csnms,_zhizhen,_ess);
        } else {
            System.out.println("数据库连接失败");
        }
        
    }
    //--------------------------筛选log信息-------------------------------------
    public static void Shaixxx(String message){
        String wg = "";
        //wg="南京";
        int _day =9;
        fun _do2 = new fun();  //写入Excel   
        //message筛选条件       _day文件路径
       _do2.run(message,wg,_day);
    }

}
