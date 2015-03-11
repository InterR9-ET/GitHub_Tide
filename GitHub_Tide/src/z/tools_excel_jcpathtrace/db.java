/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.tools_excel_jcpathtrace;

import z.xn_port.*;
import java.io.File;
import java.util.*;
import org.apache.log4j.Logger;

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
    private static util.GetFile.excel _excel = new util.GetFile.excel();

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

    public static List haspath(util.GetSql.csnms _csnms, String pathname) {
        List _list = new ArrayList();
        String str_sql = "select p.path_id as PATHID from path  p where p.name  ='" + pathname + "'";
        _list = _csnms.getdata(str_sql, null);
        return _list;
    }

    public static List haspathtrace(util.GetSql.csnms _csnms, String pathid) {
        List _list = new ArrayList();
        String str_sql = "select * from  pathtrace  p  where p.path_id='" + pathid + "'";
        _list = _csnms.getdata(str_sql, null);
        return _list;
    }

}
