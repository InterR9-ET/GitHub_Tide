/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_transperwbs;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import z.xn_transper.*;
import z.xn_port.*;
import z.send_sms.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 *
 * @author yangzhen
 * @author 功能：数据库加载
 * @author 描述：
 * @author 数据库加载判断
 *
 *
 */
public class db {

    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    //---------------------------------数据库加载-----------------------------------------
    public static boolean ini(util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.rush()) {
                _bs = true;
            }
        }
        return _bs;
    }
    
    /**
     * @param _csnms--------------------------------------------------数据库链接
     * @return 
     */
    //------------------------------得到NODEUUID的先关信息--------------------------------------------
    public static List getUUID(util.GetSql.csnms _csnms) {
        List _list_UUID = new ArrayList();
        String sql_search = "select  WYUUID,WYNAME,PORTUUID,PORTNAME,TYPE  from NODEUUID ";
        List _list = _csnms.getdata(sql_search, null);
        if (_list.size() > 0) {
            strclass.UUID _UUID = new strclass.UUID();
            for (int i = 0, m = _list.size(); i < m; i++) {
                HashMap map = (HashMap) _list.get(i);
                _UUID = new strclass.UUID();
                try {
                    _UUID.WYNAME = map.get("WYNAME").toString();
                } catch (Exception ex) {
                }
                try {
                    _UUID.WYUUID = map.get("WYUUID").toString();
                } catch (Exception ex) {
                }
                try {
                    _UUID.PORTUUID = map.get("PORTUUID").toString();
                } catch (Exception ex) {
                }
                try {
                    _UUID.PORTNAME = map.get("PORTNAME").toString();
                } catch (Exception ex) {
                }
                try {
                    _UUID.TYPE = map.get("TYPE").toString();
                } catch (Exception ex) {
                }
                _list_UUID.add(_UUID);
            }
        }
        return _list_UUID;
    }
    
    
    //--------------------------------------------------------------------------
    public static void jiexi(String _xmlDoc,util.GetSql.csnms _csnms,org.apache.log4j.Logger log) {
                List list = new ArrayList();
                List list_sql = new ArrayList();
                //创建一个新的字符串
                StringReader read = new StringReader(_xmlDoc);
                //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
                InputSource source = new InputSource(read);
                //创建一个新的SAXBuilder
                SAXBuilder sb = new SAXBuilder();
                try {
                    //通过输入源构造一个Document
                    Document doc = sb.build(source);
                    //取的根元素
                    Element root = doc.getRootElement();
                    //System.out.println("根元素名称：" + root.getName());//输出根元素的名称（测试）
                    //得到根元素所有子元素的集合
                    List jiedian = root.getChildren();
                    //获得XML中的命名空间（XML中未定义可不写）
                    Namespace ns = root.getNamespace();
                    Element et = null;

                    for (int i = 0; i < jiedian.size(); i++) {
                        et = (Element) jiedian.get(i);//循环依次得到子元素 

                        //获取  nodes
                        //System.out.println("当前节点:" + et.getName());
                        List jiedian2 = et.getChildren();
                        for (int i2 = 0; i2 < jiedian2.size(); i2++) {
                            et = (Element) jiedian2.get(i2);//循环依次得到子元素 
                            //获取  nodes
                            //System.out.println("当前节点:" + et.getName());
                            List jiedian3 = et.getChildren();

                            String _emsname = "";
                            String _mename = "";
                            String _port_name = "";

                            for (int i3 = 0; i3 < jiedian3.size(); i3++) {
                                et = (Element) jiedian3.get(i3);//循环依次得到子元素 
                                //System.out.println("当前节点:" + et.getName());

                                if (et.getName().equals("emsname")) {
                                    _emsname = et.getText().toString().trim();//电路名称
                                }

                                if (et.getName().equals("mename")) {
                                    _mename = et.getText().toString().trim();//设备名称
                                }

                                if (et.getName().equals("ptpname")) {
                                    _port_name = et.getText().toString().trim();//端口名称
                                }

                                //当节点为"indexlist"时，进行下一次分析
                                if (et.getName().equals("indexlist")) {
                                    List jiedian4 = et.getChildren();

                                    String _out = "";
                                    String _in = "";
                                    String _collecttime = "";

                                    for (int i4 = 0; i4 < jiedian4.size(); i4++) {
                                        et = (Element) jiedian4.get(i4);//循环依次得到子元素 
                                        //System.out.println("当前节点:" + et.getName());

                                        //当节点为"index"时，进行下一次分析
                                        if (et.getName().equals("index")) {
                                            List jiedian5 = et.getChildren();

                                            String _indexid = "";
                                            String _value = "";

                                            for (int i5 = 0; i5 < jiedian5.size(); i5++) {
                                                et = (Element) jiedian5.get(i5);//循环依次得到子元素 
                                                //System.out.println("当前节点:" + et.getName());
                                                if (et.getName().equals("collecttime")) {
                                                    _collecttime = et.getText().toString().trim();
                                                }

                                                if (et.getName().equals("indexid")) {
                                                    _indexid = et.getText().toString().trim();
                                                }

                                                if (et.getName().equals("value")) {
                                                    _value = et.getText().toString().trim();
                                                }
                                            }

                                            if (_collecttime.length() > 0 && _indexid.length() > 0 && _value.length() > 0) {
                                                if (_indexid.equals("总输出光功率当前值")) {
                                                    _out = _value;
                                                }

                                                if (_indexid.equals("输入光功率当前值")) {
                                                    _in = _value;
                                                }
                                            }
                                        }
                                    }

                                    if (_out.length() > 0 || _in.length() > 0) {
                                        String _sql_nodeid = "  select  n.mapnodeid as NODEID from  groupvpnmapnode   n where   n.name  =  '" + _mename + "'";

                                        List _node_id = new ArrayList();
                                        Object[] objs = new Object[]{};
                                        try {
                                            _node_id = _csnms.getdata(_sql_nodeid, null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        String _nodeid = "";
                                        if (_node_id.size() > 0) {
                                            HashMap map = (HashMap) _node_id.get(0);
                                            _nodeid = map.get("NODEID").toString();
                                        }

                                        if (_nodeid.length() > 0) {
                                            strclass.data _data = new strclass.data();
                                            _data.NODENAME = _mename;
                                            _data.PORTNAME = _port_name;
                                            _data.OUT_VALUE = _out;
                                            _data.IN_VALUE = _in;
                                            _data.GETTIME = _collecttime;
                                            _data.NODEID = _nodeid;
                                            _data.EMSNAME = _emsname;
                                            list_sql.add(_data);

                                            String _sql_in = " insert  into SWITCH_CIRCUIT_GA(NODENAME,PORTNAME,OUT_VALUE,IN_VALUE,GETTIME,NODEID,EMSNAME)"
                                                    + " values('" + _mename + "','" + _port_name + "','" + _out + "','" + _in + "',to_date('" + _collecttime + "','yyyy-mm-dd hh24:mi:ss'),'" + _nodeid + "','" + _emsname + "')";
                                            //list_sql.add(_sql_in);
                                            log.info(_sql_in);
                                        } else {
                                            log.info("node_id未匹配到,name:" + _mename);
                                        }
                                    }

                                }
                            }
                        }
                    }

                    if (list_sql.size() > 0) {
                        //写入数据库
                        tb_in_up("in", list_sql,_csnms);
                        //getSql.SQL_Execute(list_sql);
                    }

                } catch (Exception e) {
                    // TODO 自动生成 catch 块
                    e.printStackTrace();
                }
            }
    
    //--------------------------------------------------------------------------
    public static void tb_in_up(String type_in_up, List _data,util.GetSql.csnms _csnms) {
                //构造预处理
                try {
                    int count_s = 0;
                    for (int i = 0, m = _data.size(); i < m; i++) {
                        strclass.data _datal = new strclass.data();
                        _datal = (strclass.data) _data.get(i);
                        count_s = count_s + 1;

                        //-------------处理时间---------------//
                        String _GETTIME = _datal.GETTIME;
                        SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date _date1 = new Date();
                        _date1 = sdf_temp.parse(_GETTIME);
                        //------------------------------------//

                        if (type_in_up.equals("in")) {
                            String str_sql = "insert into  SWITCH_CIRCUIT_GA ("
                                    + "NODENAME,"
                                    + "PORTNAME,"
                                    + "OUT_VALUE,"
                                    + "IN_VALUE,"
                                    + "GETTIME,"
                                    + "NODEID,"
                                    + "EMSNAME"
                                    + ") values ("
                                    + "'" + _datal.NODENAME + "',"
                                    + "'" + _datal.PORTNAME + "',"
                                    + "'" + _datal.OUT_VALUE + "',"
                                    + "'" + _datal.IN_VALUE + "',"
                                    + "to_date('" + _date1.getTime() + "','yyyy-mm-dd hh24:mi:ss')"
                                    + "'" + _datal.NODEID + "',"
                                    + "'" + _datal.EMSNAME + "',"
                                    + ")";
                            _csnms.execute(str_sql, null);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //log.info("构造预处理失败");
                }
            }
}
