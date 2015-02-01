/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.donghuan_fazhi;

import z.send.alarm.*;
import java.util.ArrayList;
import java.util.HashMap;
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

    private static util.GetTools.tools _tools = new util.GetTools.tools();

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

    public static List load_fazhi_csnms(util.GetSql.csnms _csnms) {
        List _list = new ArrayList();
        //加载数据库      
        String str_sql = "";
        //未发送的
        str_sql = "select   "
                + " e.id as ID, "
                + " e.lscid as LSCID, "
                + " e.equipid as EQUIPID,"
                + " e.maxvalue as MAXVALUE ,"
                + " e.minvalue as MINVALUE,"
                + " e.type as TYPE,"
                //+ " e.content as CONTENT,  "
                + " e.status as STATUS"
                + " from  equipboundy  e"
                + " where  e.status=1";

        if (str_sql.length() > 0) {
            List lst = new ArrayList();
            try {
                lst = _csnms.getdata(str_sql, null);

                for (int i = 0, m = lst.size(); i < m; i++) {
                    z.allClass.donghaunfazhi_mes _sms = new z.allClass.donghaunfazhi_mes();
                    HashMap map = (HashMap) lst.get(i);

                    if (map.get("ID") != null) {
                        _sms.ID = map.get("ID").toString();
                    } else {
                        continue;
                    }

                    if (map.get("LSCID") != null) {
                        _sms.LSCID = map.get("LSCID").toString();
                    } else {
                        continue;
                    }

                    if (map.get("EQUIPID") != null) {
                        _sms.EQUIPID = map.get("EQUIPID").toString();
                    } else {
                        continue;
                    }

                    if (map.get("MAXVALUE") != null) {
                        _sms.MAXVALUE = map.get("MAXVALUE").toString();
                    }

                    if (map.get("MINVALUE") != null) {
                        _sms.MINVALUE = map.get("MINVALUE").toString();
                    }

                    if (map.get("TYPE") != null) {
                        _sms.TYPE = map.get("TYPE").toString();
                    } else {
                        continue;
                    }

                    if (map.get("STATUS") != null) {
                        _sms.STATUS = map.get("STATUS").toString();
                    } else {
                        continue;
                    }

                    /*
                     if (map.get("CONTENT") != null) {
                     _sms.CONTENT = map.get("CONTENT").toString();
                     } else {
                     continue;
                     }
                     */
                    _list.add(_sms);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _list;
    }

    public static boolean update(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, z.allClass.donghaunfazhi_mes _sms) {
        boolean bs = false;
        try {
            String str_sql = "update equipboundy  "
                    + " set  "
                    + " content=?,"
                    + " update_time=? ,"
                    + " status=?"
                    + " where "
                    + " lscid=? "
                    + " and EQUIPID=?"
                    + " and ID=?";
            Object[] obj = new Object[]{
                _sms.CONTENT,
                _tools.string_prase_setTimestamp(_tools.systime_prase_string("")),
                "2",
                _sms.LSCID,
                _sms.EQUIPID,
                _sms.ID
            };
            _csnms.execute(str_sql, obj);
            bs = true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage().toString());
        }

        return bs;
    }

}
