/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_ping;

import java.util.*;

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

    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            } else {
                log.info("csnms打开出错");
            }
        } else {
            log.info("csnms加载出错");
        }
        return _bs;
    }

    //--------------------------------------------------------------------------
    public boolean ycl(util.GetSql.csnms _csnms, List _list_date, org.apache.log4j.Logger log) {
        boolean _bs = false;
        try {
            List _list_obj = new ArrayList();
            for (int i = 0, m = _list_date.size(); i < m; i++) {
                //进度 
                System.out.println("进度：" + (i + 1) + "/" + m);
                //提取数据
                fun.ping _ping = new fun.ping();
                _ping = (fun.ping) _list_date.get(i);
                Object[] objs = new Object[]{
                    _ping.str1.toString(),
                    _ping.str2.toString(),
                    _ping.str3.toString(),
                    _ping.str4.toString(),
                    _ping.str5.toString()
                };
                _list_obj.add(objs);
            }
            if (_list_obj.size() > 0) {
                String str_sql = "insert into SWITCH_PING("
                        + "STARTIP,"
                        + "PORTIP,"
                        + "CENTERIP,"
                        + "TIME,"
                        + "PINGTIME"
                        + ") values (?,?,?,?,?)";
                _csnms.execute_listobj(str_sql, _list_obj);              //加判断
            }
             _bs = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("Ping处理异常：" + ex.getMessage().toString());
        }                                                       //_bs = true; return _bs;
        return _bs;
    }

}
