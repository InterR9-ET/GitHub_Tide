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
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.Element;

/**
 *
 * @author yangzhen
 */
public class socketclient {

     private util.GetFile.xml _xml = new util.GetFile.xml();
     
    private Socket remote_socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String socket_name = "SocketClient";
    private String socket_ip = null;
    private String socket_port = null;
    
    public boolean load() {
        boolean _bs = false;
        String _url = "/conf/conf.xml";
        try {
            if (socket_name.length() > 0) {
                Element root = _xml.get_Elements(_url);
                List _list = _xml.get_ChildrenElements(root, null);

                if (_list.size() > 0) {
                    for (int i = 0, m = _list.size(); i < m; i++) {
                        Element dv1 = (Element) _list.get(i);
                        String mess1 = _xml.get_Element_Name(dv1);
                        List _list2 = _xml.get_ChildrenElements(dv1, null);
                        if (_list2.size() > 0) {
                            for (int i2 = 0, m2 = _list2.size(); i2 < m2; i2++) {
                                Element dv2 = (Element) _list2.get(i2);
                                String mess2 = _xml.get_Element_Name(dv2);
                                String mess2_KEYNAME = _xml.get_Element_AttributeValue(dv2, "KEYNAME");//得到节点具体属性值 
                                List _list3 = _xml.get_ChildrenElements(dv2, null);
                                if (_list3.size() > 0) {

                                    if (mess2_KEYNAME.equals(socket_name.toString())) {
                                        StringBuffer mes = new StringBuffer();
                                        mes.append("-----------------------").append("\r\n");
                                        mes.append("--").append(mess1).append("\r\n");
                                        mes.append("------").append(mess2).append("||").append(mess2_KEYNAME).append("\r\n");//属性

                                        //循环获取子元素的名称，并根据名称解析值
                                        for (int i3 = 0, m3 = _list3.size(); i3 < m3; i3++) {
                                            Element dv3 = (Element) _list3.get(i3);
                                            String mess3 = _xml.get_Element_Name(dv3);
                                            String mess3_text = _xml.get_Element_ChildText(dv2, mess3);//得到元素的值                                
                                            mes.append("------------").append(mess3).append("#").append(mess3_text).append("\r\n");

                                            if (mess3.equals("IP")) {
                                              socket_ip=(mess3_text);
                                            }
                                            if (mess3.equals("PORT")) {
                                                socket_port=(mess3_text);
                                            }
                                        }
                                        mes.append("-----------------------").append("\r\n");
                                        System.out.println(mes.toString());

                                        _bs = true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (!_bs) {
                    System.out.println("read " + _url + "  error");
                }

            } else {
                System.out.println("db_name  is  null");
            }

        } catch (Exception e) {
            System.out.println(_url + " \r\n" + e.getMessage());
        }
        return _bs;

    }

    public boolean open() {
        boolean _bs = false;
        try {
            if (remote_socket == null || remote_socket.isClosed()) {
                //新建连接
                remote_socket = new Socket(socket_ip, Integer.parseInt(socket_port));
                System.out.println("Connection access:" + socket_ip + ":" + socket_port + ".");
                //发送空字符串  验证状态
                try {
                    remote_socket.sendUrgentData(0xFF);
                    _bs = true;
                } catch (Exception ex) {
                    //重新连接
                    remote_socket = new Socket(socket_ip, Integer.parseInt(socket_port));
                    System.out.println("Connection access:" + socket_ip + ":" + socket_port + ".");
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
                    System.out.println("Connection access:" + socket_ip + ":" + socket_port + ".");
                    System.out.println("对端断开,已重连");
                    _bs = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return _bs;
    }

    public boolean ini() {
        boolean _bs = false;
        if (load()) {
            if (open()) {
                _bs = true;
            } else {
                System.out.println("Socket 连接失败....");
            }
        } else {
            System.out.println("配置文件读取错误....");
        }
        return _bs;
    }

    public boolean sendmessage(String mes, org.apache.log4j.Logger log) {
        boolean _bs = false;
        try {
            if (mes.length() > 0 && ini()) {
                socketclient _socketclient = new socketclient();
                // socketclient.out = new PrintWriter(socketclient.remote_socket.getOutputStream(),"UTF-8");
                _socketclient.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(remote_socket.getOutputStream(), "GBK")), true);
                String mes1 = mes;
                _socketclient.out.println(mes1);
                _socketclient.out.flush();
                System.out.println("Send access:" + mes1);
                _bs = true;
                //remote_socket.close();
                //记录告警
                log.info(mes);
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
