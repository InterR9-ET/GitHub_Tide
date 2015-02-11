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
public class ftp {

    public static void main(String[] args) {
        test1();
        //test2();
    }

    public static void test1() {
        util.GetFtp.ftpclient _client = new util.GetFtp.ftpclient();
        _client.set_FTP_IP("132.228.36.66");
        _client.set_FTP_USERNAME("taisitong");
        _client.set_FTP_PWD("Telecom@123");
        _client.ini();
    }

    public static void test2() {
        util.GetFtp.ftpclient _client = new util.GetFtp.ftpclient();
        _client.ini("ftp_ping_sz");
    }
}
