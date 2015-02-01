/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author yangzhen
 */
//import Utils.getSql2;
public class xml {

    private static util.GetFile.xml _xml = new util.GetFile.xml();
    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();
    private static util.GetTools.tools _tools = new util.GetTools.tools();

    public static void main(String[] args) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            }
        }
        if (_bs) {
            System.out.println("Conn Success");
            test1();
        } else {
            System.out.println("Conn error");
        }
    }

    public static void test1() {
        _tools.create_tablefile(_csnms, "SENDMESSAGE", "");
    }

    public static void test2() {

        String _url = "/conf/dbcof.xml";
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
                            }

                            mes.append("-----------------------").append("\r\n");
                            System.out.println(mes.toString());
                        }

                    }
                }

            }
        }
    }

}
