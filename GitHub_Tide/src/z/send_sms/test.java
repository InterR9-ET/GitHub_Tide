package z.send_sms;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import java.rmi.RemoteException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import java.lang.Thread;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class test extends Thread {

    //-------------------------------------日志---------------------------------------//
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(main.class.getName()); //获得logger
    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    static {
        org.apache.log4j.PropertyConfigurator.configureAndWatch("conf/log4j_sendsms.config");
    }
    //-------------------------------------日志---------------------------------------//

    private static util.GetSql.csnms _csnms = new util.GetSql.csnms();

    public void test() {

        if (db.ini(log, _csnms)) {

            String str_sql = "insert into sendmessage (id,phone,description,cityid,linkname,sendtype) values((select max(s2.id) from sendmessage s2)+1,'15358462836','短信收发测试','600837440','羊振','恢复通知')";
            //_csnms.execute(str_sql, null);
        }

    }

    public void test1() {

        int str_rows = 0;

        //String str_tel_id = "18914731993";
        String str_tel_id = "15306188026";
        String str_i_Content = "测试短信，车通升下发 ";
        String str_CITYID = "15";

        String str_xml = "";
        str_xml = str_xml + "<?xml version=\"1.0\" encoding=\"GBK\"?>";
        str_xml = str_xml + "<SERVICE>";
        str_xml = str_xml + "<DATASOURCE TYPE=\"Procedure\">";
        str_xml = str_xml + "<SQL NAME=\"cp_sm_ins_queueex\" CONTENT=\"cp_sm_ins_queueex\">";
        str_xml = str_xml + "<PARAM NAME=\"i_Telephone\" TYPE=\"12\" TAG=\"0\" VALUE=\"" + str_tel_id + "\"/>";
        str_xml = str_xml + "<PARAM NAME=\"i_BussType\" TYPE=\"12\" TAG=\"0\" VALUE=\"SERVICE_LINKAGE\"/>";
        str_xml = str_xml + "<PARAM NAME=\"i_Content\" TYPE=\"12\" TAG=\"0\" VALUE=\"" + str_i_Content + "\"/>";
        str_xml = str_xml + "<PARAM NAME=\"i_Areaid\" TYPE=\"12\" TAG=\"0\" VALUE=\"" + str_CITYID + "\"/>";
        str_xml = str_xml + "<PARAM NAME=\"i_CallPhone\" TYPE=\"12\" TAG=\"0\" VALUE=\"���к���\"/>";
        str_xml = str_xml + "<PARAM NAME=\"o_RetVal\" TYPE=\"4\" TAG=\"1\" VALUE=\"\"/>";
        str_xml = str_xml + "<PARAM NAME=\"o_SerialNum\" TYPE=\"4\" TAG=\"1\" VALUE=\"\"/>";
        str_xml = str_xml + "</SQL>";
        str_xml = str_xml + "</DATASOURCE>";
        str_xml = str_xml + "</SERVICE>";
        try {
            String urlname = "http://132.228.169.145:8001/ida/axis/services/AsigAxisService";

            Service s = new Service();
            Call call = (Call) s.createCall();
            call.setTimeout(new Integer(5000));
            call.setOperation("executeXML");
            call.setTargetEndpointAddress(urlname);
            Object[] fn01 = {str_xml};
            String val = (String) call.invoke(fn01);

            StringReader read = new StringReader(val);
            InputSource source = new InputSource(read);
            SAXBuilder sb = new SAXBuilder();

            Document doc = sb.build(source);
            Element root = doc.getRootElement();
            List jiedian = root.getChildren();
            Namespace ns = root.getNamespace();
            Element et = null;
            String str_o_RetVal = null;
            String str_o_SerialNum = null;

            for (int i = 0; i < jiedian.size(); i++) {
                et = (Element) jiedian.get(i);

                if (i == 0) {
                    str_o_RetVal = et.getAttributeValue("VALUE");
                }
                if (i == 1) {
                    str_o_SerialNum = et.getAttributeValue("VALUE");
                }
            }

            System.out.println(str_o_RetVal + "#" + str_o_SerialNum);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
