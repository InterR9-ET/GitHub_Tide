/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_node;

import z.xn_port.*;
import z.send_sms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static z.xn_node.fun.ishaslongid;

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

    private static util.GetTools.tools _tools = new util.GetTools.tools();//声明工具类

    public static boolean ini(util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            }
        }
        return _bs;
    }

    public void retun_date(strclass.nodein _nodein, List list_cpu_in, List list_mem_in, List list_tem_in, List list_cpu_up, List list_mem_up, List list_tem_up) {

        String kk = _nodein.LONGID.toString().trim();
        if (kk.length() >= 4) {
            String uid = kk.substring(kk.length() - 4, kk.length());
            String newuid = uid.replaceAll("^(0+)", "");

            if (_nodein.NODEID.length() == 0) {
                return;
            }

            //过滤
            if (_nodein.TYPENAME.indexOf("SR7750") != -1 && _nodein.TYPENAME.indexOf("板卡温度") != -1) {
                if (_nodein.VALUE.equals("-1.00")) {
                    return;
                }
            }

            if (_nodein.TYPE.equals("1002")) {  //CPU
                strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                _cpu_tem_mem.NODEID = _nodein.NODEID;
                _cpu_tem_mem.BS_ID = newuid;
                _cpu_tem_mem.TYPE = "1";
                _cpu_tem_mem.NAME = _nodein.TYPENAME;
                _cpu_tem_mem.USED = "1";
                _cpu_tem_mem.PERCENT = _nodein.VALUE;
                _cpu_tem_mem.LONGID = _nodein.LONGID;
                if (!ishaslongid(_nodein.LONGID)) {
                    list_cpu_in.add(_cpu_tem_mem);
                } else {
                    list_cpu_up.add(_cpu_tem_mem);
                }
            } else if (_nodein.TYPE.equals("1003")) {//内存

                String _count1 = "";

                //对小数部分进行处理
                String _str_1 = _nodein.VALUE.replace(".00", "");
                if (_str_1.indexOf(".") != -1) {
                    String[] _str_s1 = _str_1.split("[.]");
                    _str_1 = _str_s1[0].toString();
                    _nodein.VALUE = _str_1;
                }

                strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                _cpu_tem_mem.NODEID = _nodein.NODEID;
                _cpu_tem_mem.BS_ID = newuid;
                _cpu_tem_mem.TYPE = "1";
                _cpu_tem_mem.NAME = _nodein.TYPENAME;
                _cpu_tem_mem.USED = "1";
                _cpu_tem_mem.PERCENT = _nodein.VALUE;
                _cpu_tem_mem.LONGID = _nodein.LONGID;

                if (!ishaslongid(_nodein.LONGID)) {
                    list_mem_in.add(_cpu_tem_mem);
                } else {
                    list_mem_in.add(_cpu_tem_mem);
                }

            } else if (_nodein.TYPE.equals("1006")) {//温度

                String _count1 = "";
                strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                _cpu_tem_mem.NODEID = _nodein.NODEID;
                _cpu_tem_mem.BS_ID = newuid;
                _cpu_tem_mem.TYPE = "1";
                _cpu_tem_mem.NAME = _nodein.TYPENAME;
                _cpu_tem_mem.USED = "1";
                _cpu_tem_mem.PERCENT = _nodein.VALUE;
                _cpu_tem_mem.LONGID = _nodein.LONGID;

                if (!ishaslongid(_nodein.LONGID)) {
                    list_tem_in.add(_cpu_tem_mem);
                } else {
                    list_tem_up.add(_cpu_tem_mem);
                }
            }
        }
    }

    
    
    
    
    
    
    public static void ycl_ini(List _date,util.GetSql.csnms _csnms,org.apache.log4j.Logger log) {
                //构造预处理
                try {

                    int count = 0;
                    strclass.nodein _nodein = new strclass.nodein();

                    List _list_obj = new ArrayList();

                    for (int i = 0, m = _date.size(); i < m; i++) {
                        //log.info("处理进度：" + (i + 1) + "/" + m);
                        _nodein = new strclass.nodein();
                        _nodein = (strclass.nodein) _date.get(i);
                        count = count + 1;

                        String kk = _nodein.LONGID.toString().trim();
                        String uid = kk.substring(kk.length() - 4, kk.length());
                        String newuid = uid.replaceAll("^(0+)", "");
                        _nodein.OBJNO = newuid;
                        _nodein.LONGID = _nodein.LONGID.toString().trim();
                        String _val = _nodein.VALUE.toString().trim();

                        String[] strarray = _val.split("\\.");
                        if (strarray.length > 0) {
                            _nodein.VALUE = strarray[0].toString().trim();
                        }
                        //System.out.println(_val + "###" + _nodein.VALUE);
                        System.out.println("##" + _nodein.GETTIME.toString() + "#" + _tools.sjctime_prase_string(_nodein.GETTIME.toString()));

                        Object[] objs = new Object[]{
                            Long.parseLong(_nodein.OBJNO),
                            Long.parseLong(_nodein.NODEID),
                            _tools.string_prase_setTimestamp(_tools.sjctime_prase_string(_nodein.GETTIME.toString())),
                            Long.parseLong(_nodein.VALUE),
                            Long.parseLong(_nodein.TYPE),
                            Long.parseLong(_nodein.SIZES),
                            _nodein.LONGID
                        };
                        _list_obj.add(objs);
                    }
                    if (_list_obj.size() > 0) {
                        String str_sql = "insert into JSHPFMOTHER_NODE ("
                                + "OBJNO,"
                                + "NODEID,"
                                + "GETTIME,"
                                + "VALUE,"
                                + "TYPE,"
                                + "SIZES,"
                                + "LONGID"
                                + ") values (?,?,?,?,?,?,?)";
                        _csnms.execute_listobj(str_sql, _list_obj);
                        _list_obj.clear();;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("构造预处理失败");
                }
            }
    
   
    public static void ycl_ini_cpu(String type_in_up, List _date,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {
                //构造预处理
                try {
                    int count_s = 0;
                    List _list_obj_in = new ArrayList();
                    List _list_obj_up = new ArrayList();
                    for (int i = 0, m = _date.size(); i < m; i++) {
                        log.info("IPCPU:" + (i + 1) + "/" + m);
                        strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                        _cpu_tem_mem = (strclass.cpu_tem_mem) _date.get(i);
                        count_s = count_s + 1;
                        if (type_in_up.equals("in")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                Long.parseLong(_cpu_tem_mem.BS_ID),
                                Long.parseLong(_cpu_tem_mem.TYPE),
                                _cpu_tem_mem.NAME,
                                Long.parseLong(_cpu_tem_mem.USED),
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_in.add(objs);
                        } else if (type_in_up.equals("up")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_up.add(objs);
                        }
                    }

                    if (_list_obj_in.size() > 0) {
                        String str_sql = "insert  into  IPCPU (NODEID,CPUID,TYPE,NAME,USED,PERCENT,LONGID)"
                                + " values (?,?,?,?,?,?,?)";
                        _csnms.execute_listobj(str_sql, _list_obj_in);
                        _list_obj_in.clear();
                    }

                    if (_list_obj_up.size() > 0) {
                        String str_sql = "update IPCPU  set  PERCENT=? where NODEID=? and LONGID=?";
                        _csnms.execute_listobj(str_sql, _list_obj_up);
                        _list_obj_up.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("构造预处理失败");
                }
            }
    
    
    
    public static void ycl_ini_mem(String type_in_up, List _date,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {
                //构造预处理
                try {
                    int count_s = 0;

                    List _list_obj_in = new ArrayList();
                    List _list_obj_up = new ArrayList();

                    for (int i = 0, m = _date.size(); i < m; i++) {
                        log.info("IPMEM:" + (i + 1) + "/" + m);
                        strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                        _cpu_tem_mem = (strclass.cpu_tem_mem) _date.get(i);
                        count_s = count_s + 1;
                        if (type_in_up.equals("in")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                Long.parseLong(_cpu_tem_mem.BS_ID),
                                Long.parseLong(_cpu_tem_mem.TYPE),
                                _cpu_tem_mem.NAME,
                                Long.parseLong(_cpu_tem_mem.USED),
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_in.add(objs);
                        } else if (type_in_up.equals("up")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_up.add(objs);
                        }
                    }

                    if (_list_obj_in.size() > 0) {
                        String str_sql = "insert  into  ipmem (NODEID,MEMID,TYPE,NAME,USED,PERCENT,LONGID)"
                                + " values (?,?,?,?,?,?,?)";
                        _csnms.execute_listobj(str_sql, _list_obj_in);
                        _list_obj_in.clear();
                    }

                    if (_list_obj_up.size() > 0) {
                        String str_sql = "update ipmem  set  PERCENT=? where NODEID=? and LONGID=?";
                        _csnms.execute_listobj(str_sql, _list_obj_up);
                        _list_obj_up.clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("构造预处理失败");
                }
            }
    
    
    
     public static void ycl_ini_tem(String type_in_up, List _date,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {
                //构造预处理
                try {
                    int count_s = 0;
                    List _list_obj_in = new ArrayList();
                    List _list_obj_up = new ArrayList();

                    for (int i = 0, m = _date.size(); i < m; i++) {
                        log.info("IPTEM:" + (i + 1) + "/" + m);
                        strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                        _cpu_tem_mem = (strclass.cpu_tem_mem) _date.get(i);
                        count_s = count_s + 1;

                        if (type_in_up.equals("in")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                Long.parseLong(_cpu_tem_mem.BS_ID),
                                Long.parseLong(_cpu_tem_mem.TYPE),
                                _cpu_tem_mem.NAME,
                                Long.parseLong(_cpu_tem_mem.USED),
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_in.add(objs);
                        } else if (type_in_up.equals("up")) {

                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_up.add(objs);
                        }
                    }

                    if (_list_obj_in.size() > 0) {
                        String str_sql = "insert  into  TEMPERATURE (NODEID,BOARDID,BOARD_TYPE,NAME,STAT,TEMPERATURE,LONGID)"
                                + " values (?,?,?,?,?,?,?)";
                        _csnms.execute_listobj(str_sql, _list_obj_in);
                        _list_obj_in.clear();
                    }

                    if (_list_obj_up.size() > 0) {
                        String str_sql = "update TEMPERATURE  set  TEMPERATURE=? where NODEID=? and LONGID=?";
                        _csnms.execute_listobj(str_sql, _list_obj_up);
                        _list_obj_up.clear();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("构造预处理失败");

                }
            }
    
    
   /** public void ycl_ini_cpu(String type_in_up, List _date,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {
                //构造预处理
                try {
                    int count_s = 0;
                    List _list_obj_in = new ArrayList();
                    List _list_obj_up = new ArrayList();
                    for (int i = 0, m = _date.size(); i < m; i++) {
                        log.info("IPCPU:" + (i + 1) + "/" + m);
                        strclass.cpu_tem_mem _cpu_tem_mem = new strclass.cpu_tem_mem();
                        _cpu_tem_mem = (strclass.cpu_tem_mem) _date.get(i);
                        count_s = count_s + 1;
                        if (type_in_up.equals("in")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                Long.parseLong(_cpu_tem_mem.BS_ID),
                                Long.parseLong(_cpu_tem_mem.TYPE),
                                _cpu_tem_mem.NAME,
                                Long.parseLong(_cpu_tem_mem.USED),
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_in.add(objs);
                        } else if (type_in_up.equals("up")) {
                            Object[] objs = new Object[]{
                                Long.parseLong(_cpu_tem_mem.PERCENT),
                                Long.parseLong(_cpu_tem_mem.NODEID),
                                _cpu_tem_mem.LONGID
                            };
                            _list_obj_up.add(objs);
                        }
                    }

                    if (_list_obj_in.size() > 0) {
                        String str_sql = "insert  into  IPCPU (NODEID,CPUID,TYPE,NAME,USED,PERCENT,LONGID)"
                                + " values (?,?,?,?,?,?,?)";
                        _csnms.execute_listobj(str_sql, _list_obj_in);
                        _list_obj_in.clear();
                    }

                    if (_list_obj_up.size() > 0) {
                        String str_sql = "update IPCPU  set  PERCENT=? where NODEID=? and LONGID=?";
                        _csnms.execute_listobj(str_sql, _list_obj_up);
                        _list_obj_up.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("构造预处理失败");
                }
            }**/
    
    
    
    
    
}
