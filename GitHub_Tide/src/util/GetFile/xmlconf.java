/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetFile;

import util.GetSocket.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import org.jdom.Element;

/**
 *
 * @author yangzhen
 */
public class xmlconf {

    private util.GetFile.xml _xml = new util.GetFile.xml();

    public String getvalue(String keyname, String Element) {
        String str_values = "";
        String _url = "/conf/conf.xml";
        try {
            if (keyname.length() > 0) {
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

                                    if (mess2_KEYNAME.equals(keyname.toString())) {
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

                                            if (mess3.equals(Element)) {
                                                str_values = mess3_text;                                           
                                                String mess=keyname + "#" + Element + "#" + str_values+"\r\n";
                                                System.out.println(mess);
                                                return str_values;
                                            }
                                        }
                                        mes.append("-----------------------").append("\r\n");
                                        System.out.println(mes.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("keyname  is  null");
            }
        } catch (Exception e) {
            System.out.println(_url + " \r\n" + e.getMessage());
        }
        return str_values;
    }
}
