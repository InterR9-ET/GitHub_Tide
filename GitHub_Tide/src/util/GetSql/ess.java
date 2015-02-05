/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetSql;

/**
 *
 * @author yangzhen
 */
import util.AbstractClass.databases;
import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class ess extends databases {

    private String db_name = "ess";
    private String db_driver = null;
    private String db_url = null;
    private String db_user = null;
    private String db_pwd = null;
    private Connection db_conn = null;
    
    private static util.GetFile.xml _xml = new util.GetFile.xml();

    @Override
    public void close(Object[] param) {
        if (param != null) {
            for (Object obj : param) {
                try {
                    if ((obj instanceof ResultSet)) {
                        ((ResultSet) obj).close();
                        obj = null;
                    }
                    if ((obj instanceof PreparedStatement)) {
                        ((PreparedStatement) obj).close();
                        obj = null;
                    }

                    if ((obj instanceof Statement)) {
                        ((Statement) obj).close();
                        obj = null;
                    }

                    if ((obj instanceof Connection)) {
                        ((Connection) obj).close();
                        obj = null;
                    }
                    if ((obj instanceof BufferedReader)) {
                        ((BufferedReader) obj).close();
                        obj = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void set_db_name(String db_name) {
        this.db_name = db_name;
    }

    @Override
    public void set_db_driver(String db_driver) {
        this.db_driver = db_driver;
    }

    @Override
    public void set_db_url(String db_url) {
        this.db_url = db_url;
    }

    @Override
    public void set_db_user(String db_user) {
        this.db_user = db_user;
    }

    @Override
    public void set_db_pwd(String db_pwd) {
        this.db_pwd = db_pwd;
    }

    @Override
    public String get_db_name() {
        return db_name.toString();
    }

    @Override
    public String get_db_driver() {
        return db_driver.toString();
    }

    @Override
    public String get_db_url() {
        return db_url.toString();
    }

    @Override
    public String get_db_user() {
        return db_user.toString();
    }

    @Override
    public String get_db_pwd() {
        return db_pwd.toString();
    }

    @Override
      public boolean load() {
        boolean _bs = false;

        String _url = "/conf/conf.xml";
        try {

            if (this.get_db_name().toString().length() > 0) {
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

                                    if (mess2_KEYNAME.equals(this.db_name.toString())) {
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

                                            if (mess3.equals("DRIVER")) {
                                                set_db_driver(mess3_text);
                                            }
                                            if (mess3.equals("URL")) {
                                                set_db_url(mess3_text);
                                            }

                                            if (mess3.equals("USER")) {
                                                set_db_user(mess3_text);
                                            }

                                            if (mess3.equals("PWD")) {
                                                set_db_pwd(mess3_text);
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
                    System.out.println("read " + _url + "  error:"+db_name);
                }

            } else {
                System.out.println("db_name  is  null");
            }

        } catch (Exception e) {
            System.out.println(_url + " \r\n" + e.getMessage());
        }
        return _bs;
    }

      
    @Override
    public boolean open() {
        boolean _bs = false;
        try {
            if (db_conn == null || db_conn.isClosed()) {
                if (get_db_driver().indexOf("com.sybase.jdbc2.jdbc.SybDriver") != -1) {
                    Class.forName(get_db_driver()).newInstance();
                } else {
                    Class.forName(get_db_driver());
                }
                db_conn = DriverManager.getConnection(get_db_url(), get_db_user(), get_db_pwd());
                _bs = true;
            } else {
                if (close()) {
                    if (get_db_driver().indexOf("com.sybase.jdbc2.jdbc.SybDriver") != -1) {
                        Class.forName(get_db_driver()).newInstance();
                    } else {
                        Class.forName(get_db_driver());
                    }
                    db_conn = DriverManager.getConnection(get_db_url(), get_db_user(), get_db_pwd());
                    _bs = true;
                }
            }
        } catch (Exception ex) {
            System.out.println("连接异常：" + get_db_name() + "[" + get_db_url() + "-" + get_db_user() + "-" + get_db_pwd() + "]");
            System.out.println(ex.getMessage().toString());
        }
        return _bs;
    }

    @Override
    public boolean close() {
        boolean _bs = false;
        try {
            if (db_conn == null || db_conn.isClosed()) {
                _bs = true;
            } else {
                close(new Object[]{db_conn});
                _bs = true;
            }
        } catch (Exception ex) {
            System.out.println("关闭数据库异常：" + get_db_name() + "[" + get_db_url() + "-" + get_db_user() + "-" + get_db_pwd() + "]");
            System.out.println(ex.getMessage().toString());
        }
        return _bs;
    }

    @Override
    public boolean rush() {
        boolean _bs = false;
        try {
            if (db_conn == null || db_conn.isClosed()) {
                if (get_db_driver().indexOf("com.sybase.jdbc2.jdbc.SybDriver") != -1) {
                    Class.forName(get_db_driver()).newInstance();
                } else {
                    Class.forName(get_db_driver());
                }
                db_conn = DriverManager.getConnection(get_db_url(), get_db_user(), get_db_pwd());
            }
            _bs = true;
        } catch (Exception ex) {
            System.out.println("刷新数据库异常：" + get_db_name() + "[" + get_db_url() + "-" + get_db_user() + "-" + get_db_pwd() + "]");
            System.out.println(ex.getMessage().toString());
        }
        return _bs;
    }

    @Override
    public int execute(String sql, Object[] objs) {
        // 声明一个PreparedStatement对象
        PreparedStatement ps = null;

        // 受影响的行数
        int count = 0;

        try {
            //保持常连接
            if (rush()) {

                //构建预处理
                ps = db_conn.prepareStatement(sql);

                if (objs != null) {
                    // 遍历objs数组，给ps对象赋值
                    for (int i = 0; i < objs.length; i++) {
                        ps.setObject(i + 1, objs[i]);
                    }
                }

                // 得到变得的数据行数
                count = ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(new Object[]{ps});
        }
        // 受影响的行数
        return count;
    }

    @Override
    public List<Map<Object, Object>> getdata(String sql, Object[] objs) {
        //声明一个List
        List lst = new ArrayList();
        // 声明一个PreparedStatement对象
        PreparedStatement ps = null;

        ResultSet rs = null;
        ResultSetMetaData rsd = null;
        Map map = null;

        try {
            //保持常连接
            if (rush()) {
                //构建预处理
                ps = db_conn.prepareStatement(sql);

                // 遍历objs数组，给ps对象赋值
                if (objs != null) {
                    for (int i = 0; i < objs.length; i++) {
                        ps.setObject(i + 1, objs[i]);
                    }
                }

                rs = ps.executeQuery();
                if (rs != null) {
                    rsd = rs.getMetaData();
                    while (rs.next()) {
                        int columnCount = rsd.getColumnCount();
                        map = new HashMap();
                        for (int i = 1; i <= columnCount; i++) {
                            map.put(rsd.getColumnName(i), rs.getObject(i));
                        }
                        lst.add(map);
                    }
                }
                rsd = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(new Object[]{ps, rs, rsd});
        }
        //返回数据集合
        return lst;
    }
    
     @Override
    public int execute(List sql) {
        // 声明一个PreparedStatement对象
        PreparedStatement ps = null;
        // 受影响的行数
        int count = 0;
        int count_sum = 0;
        try {
            //保持常连接
            if (rush()) {
                //构建预处理
                for (int i = 0, m = sql.size(); i < m; i++) {
                    String str_sql = sql.get(i).toString();
                    if (str_sql.length() > 0) {
                        ps = db_conn.prepareStatement(str_sql);
                        // 得到变得的数据行数
                        count = ps.executeUpdate();
                        count_sum = count_sum + count;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(new Object[]{ps});
        }
        // 受影响的行数
        return count_sum;
    }

    @Override
   public int execute_listobj(String sql, List Objects) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean load_web() {
       boolean _bs = false;
        URL url = ess.class.getClassLoader().getResource("../conf/db.xml");//注意
        String path = url.getPath();     
        SAXBuilder sb = new SAXBuilder();
        Document doc = null;
        Element root = null;

        try {
            if (this.get_db_name().toString().length() > 0) {
                doc = sb.build(path);
                root = doc.getRootElement();
                List jiedian = root.getChildren();
                Namespace ns = root.getNamespace();
                for (int i = 0; i < jiedian.size(); i++) {
                    Element et = null;
                    et = (Element) jiedian.get(i);//
                    if (et.getAttributeValue("name").equals(this.db_name.toString())) {
                        set_db_name(et.getAttributeValue("name").toString());
                        set_db_driver(et.getAttributeValue("driver").toString());
                        set_db_url(et.getAttributeValue("url").toString());
                        set_db_user(et.getAttributeValue("user").toString());
                        set_db_pwd(et.getAttributeValue("pwd").toString());
                        _bs = true;
                    }
                }
                if (!_bs) {
                    System.out.println("read  xml_db  error");
                }
            } else {
                System.out.println("db_name  is  null");
            }
        } catch (Exception ex) {
            System.out.println("./conf/db.xml \r\n" + ex.getMessage());
        }
        return _bs;   }

    @Override
    public boolean create_tablefile(String DBName, String TableName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
