/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Test;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author yangzhen
 */
//import Utils.getSql2;
public class sql {

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetSql.yog _yog = new util.GetSql.yog();

    public static void main(String[] args) {
        ini _ini = new ini();
        _ini.run();
    }

    public static class ini extends Thread {

        public void run() {

            if (_csnms.load()) {
                if (_csnms.open()) {

                    for (int k = 0; k < 10; k++) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        String sql = "select  count(1) as COUNT from dcn_device  d ";
                        Object[] objs = new Object[]{};
                        List _list = _csnms.getdata(sql, objs);
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
        }
    }
}
