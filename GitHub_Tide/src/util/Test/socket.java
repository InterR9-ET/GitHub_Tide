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
public class socket {

    public static void main(String[] args) {
        test1();
        //test2();
    }

    public static void test1() {
        util.GetSocket.socketclient _client = new util.GetSocket.socketclient();
        _client.set_SOCKET_IP("132.228.5.56");
        _client.set_SOCKET_PORT("9005");
        _client.ini();
    }

    public static void test2() {
        util.GetSocket.socketclient _client = new util.GetSocket.socketclient();
        _client.ini("socketc_alarmserver");
    }


}
