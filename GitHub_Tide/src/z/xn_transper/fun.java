
package z.xn_transper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import static z.xn_transper.db.key_olddate;


public class fun {

    public static Hashtable keyHash6 = new Hashtable();
    public static Hashtable keyHash2 = new Hashtable();
    public static Hashtable keyHash3 = new Hashtable();
    public static Hashtable keyHash4 = new Hashtable();
    public static Hashtable keyHash5 = new Hashtable();
    public static List list_paths = new ArrayList();
    public static List list_jsd2 = new ArrayList();
    public static List list_perfinstance = new ArrayList();
    
    private static util.GetSocket.socketclient get1=new util.GetSocket.socketclient();
    
    /**
     * @param _filename-------------------------------------------------文件名
     * @return 
     */
    //-------------------------------取得相应数据--------------------------------
    public static List jiexi2(String _filename) {
        List list = new ArrayList();

        try {

            InputStreamReader isr = new InputStreamReader(new FileInputStream(_filename), "GB2312");
            BufferedReader reader = new BufferedReader(isr);
            String str = null;

            strclass.Jstranspathperf_detail _scd = new strclass.Jstranspathperf_detail();

            while ((str = reader.readLine()) != null) {
                _scd = new strclass.Jstranspathperf_detail();

                String mes_h_l = str.toString();
                String[] mes_h_l_l = mes_h_l.split("\\|");
                //如果解析的长度小于3则结束本次循环，数据为空数据
                if (mes_h_l_l.length <= 3) {
                    continue;
                }

                //处理正常数据
                _scd.path_name = mes_h_l_l[0].toString();
                _scd.node_name = mes_h_l_l[1].toString();
                _scd.slot_type_port = mes_h_l_l[2].toString();

                String str2[] = _scd.slot_type_port.split("-");
                if (str2.length == 3) {
                    _scd.slot = str2[0].toString();
                    _scd.port = str2[2].toString();
                }

                for (int l = 3, len1 = mes_h_l_l.length; l < len1; l = l + 3) {
                    /*
                     输入光功率当前值
                     总输出光功率当前值
                     输入光功率(dBm)
                     输出光功率(dBm)
                     复用段不可用秒
                     复用段背景块误码
                     复用段误码秒
                     复用段严重误码秒
                     */
                    String _type = mes_h_l_l[l].toString();
                    String _value = mes_h_l_l[l + 1].toString();
                    String _time = mes_h_l_l[l + 2].toString();
                    _scd.gettime = _time;

                    if (_type.equals("输入光功率当前值") || _type.equals("输入光功率(dBm)")) {
                        _scd.inoptical = _value;
                    }

                    if (_type.equals("总输出光功率当前值") || _type.equals("输出光功率(dBm)")) {
                        _scd.outoptical = _value;
                    }

                    if (_type.equals("复用段不可用秒")) {
                        _scd.notavailable2 = _value;
                    }

                    if (_type.equals("复用段背景块误码")) {
                        _scd.backerror = _value;
                    }

                    if (_type.equals("复用段误码秒")) {
                        _scd.str = _value;
                    }

                    if (_type.equals("复用段严重误码秒")) {
                        _scd.severely = _value;
                    }
                }
                list.add(_scd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("解析到的数据量：" + list.size());
        return list;
    }
   
    /**
     * @param list---------------------------------------------解析文件获得的数据
     * @param log----------------------------------------------日志信息
     * @param _csnms-------------------------------------------数据库信息
     * @return 
     */
    //---------------------------将取出的数据写入数据库--------------------------
    public static boolean indata2(List list,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {
        boolean _bs = false;
        List list_in = new ArrayList();

        for (int p = 0, len = list.size(); p < len; p++) {
            //log.info("进度：" + (p + 1) + "/" + len);
            strclass.Jstranspathperf_detail _scd = new strclass.Jstranspathperf_detail();
            _scd = (strclass.Jstranspathperf_detail) list.get(p);
            list_in.add(_scd);
        }
        //添加数据方法
        if (list_in.size() > 0) {
            db.tb_in_up_switch_circuit_jx("in", list_in,log,_csnms);
        }

        //int kk = getSql.SQL_Execute(list_sql);
        log.info("顺利执行");
        _bs = true;

        return _bs;
    }
    
    
    
    public static String getslottypeid(String slottype_name, List list_slottype, String value_type, String value) {

        String mes = "";
        String str_id = "";
        String str_name = "";
        String str_max = "";
        String str_min = "";
        String str_info = "";
        String str_type = "";

        strclass.perfinstance _perfin = new strclass.perfinstance();
        for (int i = 0, len = list_slottype.size(); i < len; i++) {
            _perfin = new strclass.perfinstance();
            _perfin = (strclass.perfinstance) list_slottype.get(i);
            str_id = _perfin.ID;
            str_name = _perfin.PERFTYPE;
            str_max = _perfin.VALUE_MAX;
            str_min = _perfin.VALUE_MIN;
            str_info = _perfin.INFO;
            str_type = _perfin.TYPE;

            if (value_type.equals("inoptical")) {
                try {
                    if (slottype_name.equals(str_name)) {
                        if (str_max.length() > 0 && str_min.length() > 0) {
                            mes = str_id;
                            break;
                        }
                    }
                } catch (Exception e) {

                }

            } else if (value_type.equals("notavailable")) {
                try {
                    if (Double.parseDouble(value) != 0) {
                        mes = "30";
                        break;
                    }
                } catch (Exception e) {
                }
            } else if (value_type.equals("backerror")) {
                try {
                    if (Double.parseDouble(value) != 0) {
                        mes = "31";
                        break;
                    }
                } catch (Exception e) {
                }

            } else if (value_type.equals("str")) {
                try {
                    if (Double.parseDouble(value) != 0) {
                        mes = "32";
                        break;
                    }
                } catch (Exception e) {
                }
            } else if (value_type.equals("severely")) {
                try {
                    if (Double.parseDouble(value) != 0) {
                        mes = "33";
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
        return mes;
    }
    
    
    
    public static void doing_main2(util.GetSql.csnms _csnms,org.apache.log4j.Logger log) {
        keyHash6.clear();
        keyHash2.clear();
        keyHash3.clear();
        list_paths.clear();
        list_jsd2.clear();
        list_perfinstance.clear();
        _csnms.rush();

        int _bs = 0;

        log.info("##Begin##");

        log.info("加载switch_circuit_jx数据");
        List list_data_jx = db.load_switch_circuit_jx(_csnms);
        log.info("加载switch_circuit_jx完成:" + list_data_jx.size());

        log.info("加载jsd2数据");
        list_jsd2 = db.load_Jstranspathperf_detail_2(_csnms);
        log.info("加载jsd2完成:" + list_jsd2.size());

        log.info("加载pathperf数据");
        db.load_pathperf(_csnms);
        log.info("加载pathperf完成");

        //加载当前表历史数据
        db.load_old_data(_csnms);

        chuli_node(list_data_jx,log,_csnms);
        chuli_path(list_data_jx,log,_csnms);

        //清除数据
        del_data(list_data_jx,log,_csnms);
        //释放资源
        list_perfinstance.clear();
        list_data_jx.clear();

        log.info("##End##");
    }
    
    
    public static void chuli_node(List list_cir,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {
        //-----------------------多线程运行------------------------//   
        try {
            log.info("开始处理设备");
            update_data_node(list_cir,log,_csnms);
            log.info("处理设备完成");
            //---------------------------------------------//
        } catch (Exception e) {
            e.printStackTrace();
            log.info("处理设备异常：" + e.getMessage().toString());
        } finally {

        }
        //-----------------------多线程运行------------------------//
    }
    
    
    public static void chuli_path(List list_cir,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {

        //-----------------------多线程运行------------------------//   
        try {
            log.info("开始处理电路");
            db.update_data_path(list_cir, log,_csnms);
            log.info("处理电路完成");
            //---------------------------------------------//
        } catch (Exception e) {
            e.printStackTrace();
            log.info("处理电路异常：" + e.getMessage().toString());
        } finally {

        }
        //-----------------------多线程运行------------------------//
    }
    
    
    public static void update_data_node(List list_cir,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {

        List JSTRANSPATHPERF_DETAIL_add = new ArrayList();

        List JSTRANSPATHPERF_DETAIL_JX_IN = new ArrayList();
        List JSTRANSPATHPERF_DETAIL_JX_UP = new ArrayList();

        if (list_cir.size() > 0) {

            int count_s = 0;
            for (int i = 0, m = list_cir.size(); i < m; i++) {
                log.info("处理设备进度：" + (i + 1) + "/" + m);

                strclass.Jstranspathperf_detail _scd = new strclass.Jstranspathperf_detail();
                _scd = (strclass.Jstranspathperf_detail) list_cir.get(i);

                if (_scd.inoptical.equals("0")) {
                    if (_scd.outoptical.equals("0")) {
                        if (_scd.notavailable2.equals("0")) {
                            if (_scd.backerror.equals("0")) {
                                if (_scd.str.equals("0")) {
                                    if (_scd.severely.equals("0")) {
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                }

                try {
                    _scd.notavailable = return_notavailable(_scd,log).toString();
                } catch (Exception e) {
                    _scd.notavailable = "0";
                }

                if (_scd.node_id.toString().length() == 0) {
                    log.info("系统中没有此NODE，请先处理:" + _scd.node_name);
                    continue;
                }

                if (_scd.path_id.toString().length() == 0 || _scd.path_id.toString().equals("-1")) {
                    log.info("系统中没有此电路，请先处理:" + _scd.path_name);
                    continue;
                }

                JSTRANSPATHPERF_DETAIL_add.add(_scd);

                String key = "";
                if (_scd.path_id != null) {
                    key += "#PATH:" + _scd.path_id.toString();
                }
                if (_scd.node_id != null) {
                    key += "#NODEID:" + _scd.node_id.toString();
                }
                if (_scd.slot_type_port != null) {
                    key += "#SLOT_TYPE_PORT:" + _scd.slot_type_port.toString();
                }

                //---------------------判断当前值-------------------//
                if (!key_olddate.containsKey(key)) {
                    key_olddate.put(key, key);
                    JSTRANSPATHPERF_DETAIL_JX_IN.add(_scd);
                } else {
                    JSTRANSPATHPERF_DETAIL_JX_UP.add(_scd);
                }
                //---------------------判断当前值-------------------//

                count_s = count_s + 1;

                //告警 异常 放多线程里面执行                     
                //开始处理告警
                db.chuli_alarm(_scd, list_perfinstance,log,_csnms);

                if (count_s > 2000) {
                    if (JSTRANSPATHPERF_DETAIL_add.size() > 0) {
                        db.tb_in_up_JSTRANSPATHPERF_DE("in", JSTRANSPATHPERF_DETAIL_add,_csnms,log);
                        JSTRANSPATHPERF_DETAIL_add.clear();
                    }

                    if (JSTRANSPATHPERF_DETAIL_JX_IN.size() > 0) {
                        db.tb_in_up_JSTRANSPATHPERF_DE("in2", JSTRANSPATHPERF_DETAIL_JX_IN,_csnms,log);
                        JSTRANSPATHPERF_DETAIL_JX_IN.clear();
                    }
                    if (JSTRANSPATHPERF_DETAIL_JX_UP.size() > 0) {
                        db.tb_in_up_JSTRANSPATHPERF_DE("up2", JSTRANSPATHPERF_DETAIL_JX_UP,_csnms,log);
                        JSTRANSPATHPERF_DETAIL_JX_UP.clear();
                    }

                    count_s = 0;
                }

            }
        }

        if (JSTRANSPATHPERF_DETAIL_add.size() > 0) {
            db.tb_in_up_JSTRANSPATHPERF_DE("in", JSTRANSPATHPERF_DETAIL_add,_csnms,log);
            JSTRANSPATHPERF_DETAIL_add.clear();
        }

        if (JSTRANSPATHPERF_DETAIL_JX_IN.size() > 0) {
            db.tb_in_up_JSTRANSPATHPERF_DE("in2", JSTRANSPATHPERF_DETAIL_JX_IN,_csnms,log);
            JSTRANSPATHPERF_DETAIL_JX_IN.clear();
        }
        if (JSTRANSPATHPERF_DETAIL_JX_UP.size() > 0) {
            db.tb_in_up_JSTRANSPATHPERF_DE("up2", JSTRANSPATHPERF_DETAIL_JX_UP,_csnms,log);
            JSTRANSPATHPERF_DETAIL_JX_UP.clear();
        }

    }
    
    
    
    
    
    
    public static Double return_notavailable(strclass.Jstranspathperf_detail _scd,org.apache.log4j.Logger log) {
        Double mes = 0.00;
        if (_scd.path_id.length() > 0) {

            strclass.Jstranspathperf_detail_2 _jsd2 = new strclass.Jstranspathperf_detail_2();
            for (int i = 0; i < list_jsd2.size(); i++) {
                _jsd2 = new strclass.Jstranspathperf_detail_2();
                _jsd2 = (strclass.Jstranspathperf_detail_2) list_jsd2.get(i);
                if (_jsd2.PATH_ID.equals(_scd.path_id)) {
                    if (_jsd2.NODE_ID.equals(_scd.node_id)) {
                        if (_jsd2.SLOT_TYPE_PORT.equals(_scd.slot_type_port)) {
                            mes = Double.parseDouble(_jsd2.NOTAVAILABLE2);
                            break;
                        }
                    }
                }
            }
        } else {
            log.info("电路ID数据为空：" + _scd.path_name);
        }
        return mes;
    }
    
    
    
    
    
    public static boolean iserror(String value, String value_type, String slottypeid, List list_slottype) {
        /*
         inoptical         outoptical         notavailable         backerror         str         severely
         */

        boolean bs = true;

        String str_id = "";
        String str_name = "";
        String str_max = "";
        String str_min = "";
        String str_info = "";
        String str_type = "";
        strclass.perfinstance _perfin = new strclass.perfinstance();
        for (int i = 0, len = list_slottype.size(); i < len; i++) {
            _perfin = new strclass.perfinstance();
            _perfin = (strclass.perfinstance) list_slottype.get(i);
            str_id = _perfin.ID;
            str_name = _perfin.PERFTYPE;
            str_max = _perfin.VALUE_MAX;
            str_min = _perfin.VALUE_MIN;
            str_info = _perfin.INFO;
            str_type = _perfin.TYPE;

            if (str_id.equals(slottypeid)) {
                if (value_type.equals("inoptical")) {
                    if (str_max.length() > 0 && str_min.length() > 0) {
                        /*
                         if (Double.parseDouble(value) < Double.parseDouble(str_min)) {
                         bs = false;
                         }
                         if (Double.parseDouble(value) > Double.parseDouble(str_max)) {
                         bs = false;
                         }
                         */
                        if (Double.parseDouble(value) < -28) {
                            bs = false;
                        }
                        if (Double.parseDouble(value) > 0) {
                            bs = false;
                        }

                        break;
                    }
                    continue;
                }
            }

            if (value_type.equals("outoptical")) {
                if (Double.parseDouble(value) != 0) {
                    bs = false;
                    break;
                }
                continue;
            }

            if (value_type.equals("notavailable")) {
                if (Double.parseDouble(value) != 0) {
                    bs = false;
                    break;
                }
                continue;
            }

            if (value_type.equals("backerror")) {
                if (Double.parseDouble(value) != 0) {
                    bs = false;
                    break;
                }
                continue;
            }

            if (value_type.equals("str")) {
                if (Double.parseDouble(value) != 0) {
                    bs = false;
                    break;
                }
                continue;
            }

            if (value_type.equals("severely")) {
                if (Double.parseDouble(value) != 0) {
                    bs = false;
                    break;
                }
                continue;
            }
        }
        return bs;
    }
    
    
    
    public static boolean seach_has_data4(strclass.Jstranspathperf_detail _scd) {
        boolean _bs = true;

        String key2 = _scd.groupid + "#" + _scd.path_id;
        if (!keyHash3.containsKey(key2)) {
            keyHash3.put(key2, key2);
            _bs = false;
        } else {
            _bs = true;
        }

        return _bs;
    }
    
    
    
    public static boolean sendAlarm(String alarm) {
        boolean _bs = false;
        _bs = get1.sendmessage(alarm);
        return _bs;
    }
    
    
    
    public static void del_data(List list_cir,org.apache.log4j.Logger log,util.GetSql.csnms _csnms) {

        List list_del = new ArrayList();
        String _old_name = "";
        String _old_gettime = "";
        int kk = 0;
        String _c_name = "";
        String _c_gettime = "";

        strclass.Jstranspathperf_detail _scd = new strclass.Jstranspathperf_detail();

        for (int i = 0, len = list_cir.size(); i < len; i++) {
            _scd = new strclass.Jstranspathperf_detail();
            _scd = (strclass.Jstranspathperf_detail) list_cir.get(i);
            _c_name = _scd.path_name;
            if (_old_name.equals(_c_name)) {
                continue;//如果遇到相同的数据则跳出本次循环
            }
            _old_name = _c_name;
            list_del.add(_old_name);
            // String str_sql_del = "delete from  switch_circuit_jx  where PATHNAME='" + _old_name + "'";
            //list_del.add(str_sql_del);
        }
        //kk = getSql.SQL_Execute(list_del);

        db.tb_in_up_switch_circuit_jx("del", list_del,log,_csnms);

        log.info("删除switch_circuit_jx完成");
    }
    
    
    
}
