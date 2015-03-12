/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import org.jdom.Element;
import util.AbstractClass.socket;

/**
 *
 * @author yangzhen
 *
 * @author ini() ：初始化 传值类型的
 * @author ini(String) ：初始化 conf类型的
 *
 */
public class socketclient extends socket {

    private util.GetFile.xmlconf _xmlconf = new util.GetFile.xmlconf();

    private Socket remote_socket = null;
    public static PrintWriter out = null;
    private BufferedReader in = null;
    private String SOCKET_NAME = "";
    private String SOCKET_IP = "";
    private String SOCKET_PORT = "";

    @Override
    public void set_SOCKET_IP(String SOCKET_IP) {
        this.SOCKET_IP = SOCKET_IP;
    }

    @Override
    public void set_SOCKET_PORT(String SOCKET_PORT) {
        this.SOCKET_PORT = SOCKET_PORT;
    }

    @Override
    public String get_SOCKET_IP() {
        return SOCKET_IP.toString();
    }

    @Override
    public String get_SOCKET_PORT() {
        return SOCKET_PORT.toString();
    }

    private boolean load() {
        boolean _bs = false;
        try {
            SOCKET_IP = _xmlconf.getvalue(SOCKET_NAME, "IP");
            SOCKET_PORT = _xmlconf.getvalue(SOCKET_NAME, "PORT");
            _bs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!_bs) {
            System.out.println("SocketClient load error:" + SOCKET_NAME + "#" + SOCKET_IP + "#" + SOCKET_PORT);
        }
        return _bs;
    }

    private boolean open() {
        boolean _bs = false;
        try {
            if (remote_socket == null || remote_socket.isClosed()) {
                //新建连接
                remote_socket = new Socket(SOCKET_IP, Integer.parseInt(SOCKET_PORT));
                //发送空字符串  验证状态
                try {
                    remote_socket.sendUrgentData(0xFF);
                    _bs = true;
                } catch (Exception ex) {
                    //重新连接
                    remote_socket = new Socket(SOCKET_IP, Integer.parseInt(SOCKET_PORT));
                    System.out.println("对端断开,已重连");
                    _bs = true;
                }
            } else {
                //发送空字符串  验证状态
                try {
                    remote_socket.sendUrgentData(0xFF);
                    _bs = true;
                } catch (Exception ex) {
                    //重新连接
                    remote_socket = new Socket(SOCKET_IP, Integer.parseInt(SOCKET_PORT));
                    System.out.println("对端断开,已重连");
                    _bs = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!_bs) {
            System.out.println("SocketClient open error:" + SOCKET_NAME + "#" + SOCKET_IP + "#" + SOCKET_PORT);
        }

        return _bs;
    }

    @Override
    public boolean ini() {
        boolean _bs = false;
        if (open()) {
            _bs = true;
        }
        if (!_bs) {
            System.out.println("SocketClient ini error:" + SOCKET_NAME + "#" + SOCKET_IP + "#" + SOCKET_PORT);
        }
        return _bs;
    }

    @Override
    public boolean ini(String SOCKET_NAME) {
        boolean _bs = false;
        this.SOCKET_NAME = SOCKET_NAME;
        if (load()) {
            if (open()) {
                _bs = true;
            }
        }
        if (!_bs) {
            System.out.println("SocketClient ini error:" + SOCKET_NAME + "#" + SOCKET_IP + "#" + SOCKET_PORT);
        }
        return _bs;
    }

    @Override
    public   boolean sendmessage(String mes) {
        boolean _bs = false;
        try {
            if (mes.length() > 0) {
                socketclient _socketclient = new socketclient();
                // socketclient.out = new PrintWriter(socketclient.remote_socket.getOutputStream(),"UTF-8");
                _socketclient.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(remote_socket.getOutputStream(), "GBK")), true);
                String mes1 = mes;
                _socketclient.out.println(mes1);
                _socketclient.out.flush();
                System.out.println("Send access:" + mes1);
                _bs = true;
                //remote_socket.close();              
            } else {
                System.out.println("Send mes:is null");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (remote_socket != null) {
                try {
                    remote_socket.close();
                } catch (Exception e) {
                }
            }
        } finally {

        }
        return _bs;
    }

}
