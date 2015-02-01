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
public class file {

    private static util.GetFile.excel _ex = new util.GetFile.excel();

    public static void main(String[] args) {
        ini _ini = new ini();
        _ini.run();
    }

    public static class ini extends Thread {

        public void run() {

            if (_ex.delete_file("/test1/test2/1.txt")) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }

        }
    }
}
