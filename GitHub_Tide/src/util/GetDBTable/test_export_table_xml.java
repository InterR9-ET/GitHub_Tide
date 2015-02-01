/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetDBTable;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author yangzhen
 */
//import Utils.getSql2;
public class test_export_table_xml {

    private static util.GetSql.test _test = new util.GetSql.test();

    public static boolean ini() {
        boolean _bs = false;
        if (_test.load()) {
            if (_test.open()) {
                _bs = true;
            } else {
                System.out.println("test打开出错");
            }
        } else {
            System.out.println("test加载出错");
        }
        return _bs;
    }

    public static void main(String[] args) {
        if (ini()) {
            String sql = "select  count(1) as COUNT from alarm  a";
            Object[] objs = new Object[]{};
            List _list = _test.getdata(sql, objs);
            for (int i = 0; i < _list.size(); i++) {
                HashMap map = (HashMap) _list.get(0);
                try {
                    String COUNT = map.get("COUNT").toString();
                    System.out.println("COUNT:" + COUNT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
