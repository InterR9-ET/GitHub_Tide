/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.send_alarm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yangzhen
 * @author 功能：数据库加载
 * @author 描述：
 * @author 数据库加载判断
 *
 *
 */
public class db {

   

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        //加载数据库配置        
        if (_csnms.load()) {
            //打开数据库配置
            if (_csnms.open()) {
                _bs = true;
            } else {
                StringBuffer logtxt = new StringBuffer();
                logtxt.append("数据库打开失败");
                log.info(logtxt.toString());
            }
        } else {
            StringBuffer logtxt = new StringBuffer();
            logtxt.append("数据库加载配置信息失败");
            log.info(logtxt.toString());
        }
        return _bs;
    }

    public static List loaddata(util.GetSql.csnms _csnms) {
        List list = new ArrayList();
        String sql = "select  s.ID,s.ALARM  from  runalarmmsge  s  order  by  s.alarm_start";
        Object[] objs = new Object[]{};
        list = _csnms.getdata(sql, null);
        return list;
    }

    public static boolean deldata(util.GetSql.csnms _csnms, String alarmid) {
        boolean bs = false;
        String str_del = "delete   from  runalarmmsge where ID=" + alarmid;
        int count = _csnms.execute(str_del, null);
        if (count > 0) {
            bs = true;
        }
        return bs;
    }
}
