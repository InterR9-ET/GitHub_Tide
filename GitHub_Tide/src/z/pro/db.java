/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.pro;

import z.xn_atm.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    private static util.GetTools.tools _datas = new util.GetTools.tools();

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        boolean result = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                result = true;
            }
        }
        return result;
    }
}
