/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author yangzhen
 * @author 转发告警程序
 */
public class run_sendalarm {

    public static void main(String[] args) {

        z.send_alarm.main _do1 = new z.send_alarm.main();
        try {
            _do1.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
