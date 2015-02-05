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

/**
 *
 * @author yangzhen
 */
public class socketclient {

    private util.GetFile.xmlconf _xmlconf = new util.GetFile.xmlconf();

    private Socket remote_socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String socket_name = "";
    private String socket_ip = "";
    private String socket_port = "";

    private boolean load() {
        boolean _bs = false;
        try {
            socket_ip = _xmlconf.getvalue(socket_name, "IP");
            socket_port = _xmlconf.getvalue(socket_name, "PORT");
            _bs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!_bs) {
            System.out.println("SocketClient load error:" + socket_name + "#" + socket_ip + "#" + socket_port);
        }

        return _bs;

    }

    private boolean open() {
        boolean _bs = false;
        try {
            if (remote_socket == null || remote_socket.isClosed()) {
                //新建连接
                remote_socket = new Socket(socket_ip, Integer.parseInt(socket_port));
                //发送空字符串  验证状态
                try {
                    remote_socket.sendUrgentData(0xFF);
                    _bs = true;
                } catch (Exception ex) {
                    //重新连接
                    remote_socket = new Socket(socket_ip, Integer.parseInt(socket_port));
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
                    remote_socket = new Socket(socket_ip, Integer.parseInt(socket_port));
                    System.out.println("对端断开,已重连");
                    _bs = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (!_bs) {
            System.out.println("SocketClient open error:" + socket_name + "#" + socket_ip + "#" + socket_port);
        }

        return _bs;
    }

    public boolean ini(String socket_name) {
        boolean _bs = false;
        this.socket_name = socket_name;
        if (load()) {
            if (open()) {
                _bs = true;
            }
        }

        if (!_bs) {
            System.out.println("SocketClient ini error:" + socket_name + "#" + socket_ip + "#" + socket_port);
        }

        return _bs;
    }

    public boolean sendmessage(String mes) {
        boolean _bs = false;
        try {
            if (ini(socket_name)) {
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
