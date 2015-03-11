/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.tools_search_conncount;

import z.send_alarm.*;
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
        String sql = "select count(1) as COUNT,'连接数' as TYPE  , to_char(sysdate,'yyyy-mm-dd HH24:mi:ss') as TIMES    from v$session    "
                + "union  "
                + "Select count(1) as COUNT,'并发连接数' as TYPE , to_char(sysdate,'yyyy-mm-dd HH24:mi:ss') as TIMES  from v$session where status='ACTIVE'";
        Object[] objs = new Object[]{};
        list = _csnms.getdata(sql, null);
        return list;
    }

}
