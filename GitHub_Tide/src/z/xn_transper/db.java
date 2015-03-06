package z.xn_transper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import util.AbstractClass.ftp;
import static z.xn_node.fun.keyHash;
import static z.xn_transper.main.list_perfinstance;

public class db {

    public static boolean _ll = true;
    public static Hashtable keyHash3 = new Hashtable();
    public static Hashtable key_olddate = new Hashtable();
    public static Hashtable keyHash2 = new Hashtable();
    public static Hashtable keyHash4 = new Hashtable();
    public static Hashtable keyHash5 = new Hashtable();
    private static List _list_node = strclass.list_node_gl();

    private static util.GetTools.tools tools = new util.GetTools.tools();//声明工具类

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            }
        }
        return _bs;
    }

    /**
     * @param files------------------------------------------------------文件名
     * @param _csnms 
     */
    //--------------------------------------------------------------------------
    public static void TRUNCATE(File files, util.GetSql.csnms _csnms) {
        int i=0;
        i+=1;
        _csnms.rush();
        String fil = files.toString();
        System.out.println("开始解析文件[" + (i + 1) + "/" + files.length() + "]：" + fil);

        String str_sql_dels = "TRUNCATE   TABLE  SWITCH_CIRCUIT_JX ";
        System.out.println("###" + str_sql_dels);

        Object[] objs = new Object[]{};
        try {
            _csnms.execute(str_sql_dels, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------读取PERFINSTANCE表的信息-----------------------
    public static List load_perfinstance(util.GetSql.csnms _csnms) {
        List list = new ArrayList();
        strclass.perfinstance _perfin = new strclass.perfinstance();
        String str_sql3 = "select s.id as ID,s.PERFTYPE as PERFTYPE,"
                + "s.VALUE_MAX as VALUE_MAX,"
                + "s.VALUE_MIN as VALUE_MIN,"
                + "s.INFO as INFO,"
                + "s.TYPE as TYPE "
                + " from PERFINSTANCE s";
        List list_slottype = new ArrayList();// 
        try {
            list_slottype = _csnms.getdata(str_sql3, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_slottype.size(); i++) {
            HashMap map = (HashMap) list_slottype.get(i);
            //
            _perfin = new strclass.perfinstance();
            if (map.get("ID") != null) {
                _perfin.ID = map.get("ID").toString();
            }
            if (map.get("INFO") != null) {
                _perfin.INFO = map.get("INFO").toString();
            }
            if (map.get("PERFTYPE") != null) {
                _perfin.PERFTYPE = map.get("PERFTYPE").toString();
            }
            if (map.get("TYPE") != null) {
                _perfin.TYPE = map.get("TYPE").toString();
            }
            if (map.get("VALUE_MAX") != null) {
                _perfin.VALUE_MAX = map.get("VALUE_MAX").toString();
            }
            if (map.get("VALUE_MIN") != null) {
                _perfin.VALUE_MIN = map.get("VALUE_MIN").toString();
            }
            list.add(_perfin);//写入list
        }
        return list;
    }

    /**
     * @param type_in_up----------------------------------------执行数据操作标示
     * @param _data---------------------------------------------数据信息
     * @param log
     * @param _csnms 
     */
    //---------------------------添加解析的数据信息------------------------------
    public static void tb_in_up_switch_circuit_jx(String type_in_up, List _data, org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                strclass.Jstranspathperf_detail _datal = new strclass.Jstranspathperf_detail();
                _datal = (strclass.Jstranspathperf_detail) _data.get(i);
                log.info("switch_circuit_jx提交进度：" + (i + 1) + "/" + m);

                //---------------过滤重复的-------------------//
                String key = _datal.path_name + "#" + _datal.node_name + "#" + _datal.slot_type_port + "#" + _datal.gettime;

                if (!keyHash.containsKey(key)) {
                    keyHash.put(key, key);
                } else {
                    log.info("有重复数据：" + "[PATH]" + _datal.path_name + "   [NODE]" + _datal.node_name + "  [SLOT-TYPE-PORT]" + _datal.slot_type_port);
                    continue;
                }
                //---------------过滤重复的-------------------//

                //-------path_id----groupid----nodeidid---slotyid----//
                _datal = return_pathid_groupid_nodeid_slottypeid(_datal, _csnms, log);
                //--------------------------------------------//
                count_s = count_s + 1;

                //-------------处理时间---------------//
                String _GETTIME = _datal.gettime;
                SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _date1 = new Date();
                _date1 = sdf_temp.parse(_GETTIME);
                //------------------------------------//

                String str_sql = "";
                if (type_in_up.equals("in")) {
                    str_sql = "insert   into  SWITCH_CIRCUIT_JX ("
                            + "PATHNAME,"
                            + "NODENAME,"
                            + "GETTIME,"
                            + "INOPTICAL,"
                            + "OUTOPTICAL,"
                            + "NOTAVAILABLE,"
                            + "BACKERROR,"
                            + "STR,"
                            + "SLOT_TYPE_PORT,"
                            + "SEVERELY,"//10                        
                            + "NOTAVAILABLE2,"
                            + "pathid,"
                            + "groupid,"
                            + "SLOT,"
                            + "PORT,"
                            + "NODEID,"
                            + "SLOTYID"
                            + ")values("
                            + "'" + _datal.path_name.toString() + "'" + ","
                            + "'" + _datal.node_name.toString() + "'" + ","
                            + "to_date('" + _datal.gettime.toString() + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + _datal.inoptical.toString() + ","
                            + _datal.outoptical.toString() + ","
                            + _datal.notavailable.toString() + ","
                            + _datal.backerror.toString() + ","
                            + _datal.str.toString() + ","
                            + "'" + _datal.slot_type_port.toString() + "'" + ","
                            + _datal.severely.toString() + ","
                            + _datal.notavailable2.toString() + ","
                            + "'" + _datal.path_id.toString() + "'" + ","
                            + "'" + _datal.groupid.toString() + "'" + ","
                            + "'" + _datal.slot.toString() + "'" + ","
                            + "'" + _datal.port.toString() + "'" + ","
                            + "'" + _datal.node_id.toString() + "'" + ","
                            + "'" + _datal.slottypeid.toString() + "'"
                            + ")";

                } else if (type_in_up.equals("in2")) {
                    str_sql = "insert   into  JSTRANSPATHPERF_DETAIL_JX ("
                            + "PATHNAME,"
                            + "NODENAME,"
                            + "GETTIME,"
                            + "INOPTICAL,"
                            + "OUTOPTICAL,"
                            + "NOTAVAILABLE,"
                            + "BACKERROR,"
                            + "STR,"
                            + "SLOT_TYPE_PORT,"
                            + "SEVERELY,"//10                        
                            + "NOTAVAILABLE2,"
                            + "pathid,"
                            + "groupid,"
                            + "SLOT,"
                            + "PORT,"
                            + "NODEID,"
                            + "SLOTYID"
                            + ")values("
                            + "'" + _datal.path_name.toString() + "'" + ","
                            + "'" + _datal.node_name.toString() + "'" + ","
                            + "to_date('" + _datal.node_name.toString() + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + _datal.inoptical.toString() + ","
                            + _datal.outoptical.toString() + ","
                            + _datal.notavailable.toString() + ","
                            + _datal.backerror.toString() + ","
                            + _datal.str.toString() + ","
                            + "'" + _datal.slot_type_port.toString() + "'" + ","
                            + _datal.severely.toString() + ","
                            + _datal.notavailable2.toString() + ","
                            + "'" + _datal.path_id.toString() + "'" + ","
                            + "'" + _datal.groupid.toString() + "'" + ","
                            + "'" + _datal.slot.toString() + "'" + ","
                            + "'" + _datal.port.toString() + "'" + ","
                            + "'" + _datal.node_id.toString() + "'" + ","
                            + "'" + _datal.slottypeid.toString() + "'"
                            + ")";

                } else if (type_in_up.equals("up2")) {

                } else if (type_in_up.equals("del")) {
                    //str_sql = "delete from  switch_circuit_jx  where PATHNAME=? ";
                }

                if (str_sql.length() > 0) {
                    _csnms.execute(str_sql, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }

    public static strclass.Jstranspathperf_detail return_pathid_groupid_nodeid_slottypeid(strclass.Jstranspathperf_detail _jsd, util.GetSql.csnms _csnms, org.apache.log4j.Logger log) {
        strclass.class_path _class_path = new strclass.class_path();
        String mes = _jsd.path_name + "#" + _jsd.node_name + "#" + _jsd.slot + "#" + _jsd.port;
        String str_sql = "";
        try {

            String str[] = _jsd.slot_type_port.split("-");
            if (str.length == 3) {
                String mes1 = fun.getslottypeid(str[1].toString(), list_perfinstance, "inoptical", _jsd.inoptical);
                if (mes1.length() > 0) {
                    _jsd.slottypeid = mes1;
                }
            }

            //匹配A端设备
            boolean _bs = false;
            str_sql = "select "
                    + " p.path_id as PATH_ID,"
                    + " p.agroup_id as GROUP_ID,"
                    + " p.anode_id as ANODE_ID "
                    + " from  pathid_nodeid_groupid  p  where "
                    + " p.PATHNAME='" + _jsd.path_name.toString() + "'  and  "
                    + " p.ANAME='" + _jsd.node_name.toString() + "' and "
                    + " p.ASLOT_ID='" + _jsd.slot.toString() + "'   and"
                    + " p.APORT_id='" + _jsd.port.toString() + "'"
                    + "";
            List _listpath_2 = _csnms.getdata(str_sql, null);
            if (_listpath_2 != null) {//
                if (_listpath_2.size() > 0) {
                    HashMap map = (HashMap) _listpath_2.get(0);
                    if (map.get("PATH_ID") != null) {
                        _jsd.path_id = map.get("PATH_ID").toString();
                    }
                    if (map.get("GROUP_ID") != null) {
                        _jsd.groupid = map.get("GROUP_ID").toString();
                    }
                    if (map.get("ANODE_ID") != null) {
                        _jsd.node_id = map.get("ANODE_ID").toString();
                    }
                } else if (_listpath_2.size() == 0) {
                    _bs = true;
                }
            } else {
                _bs = true;
            }

            //匹配Z端设备
            if (_bs) {
                str_sql = "select "
                        + " p.path_id as PATH_ID,"
                        + " p.zgroup_id as GROUP_ID,"
                        + " p.znode_id as ZNODE_ID "
                        + " from  pathid_nodeid_groupid  p  where "
                        + " p.PATHNAME='" + _jsd.path_name.toString() + "'  and  "
                        + " p.ZNAME='" + _jsd.node_name.toString() + "' and "
                        + " p.ZSLOT_ID='" + _jsd.slot.toString() + "'   and"
                        + " p.ZPORT_id='" + _jsd.port.toString() + "'"
                        + "";
                List _listpath_3 = _csnms.getdata(str_sql, null);
                if (_listpath_3 != null) {
                    if (_listpath_3.size() > 0) {
                        HashMap map = (HashMap) _listpath_3.get(0);
                        if (map.get("PATH_ID") != null) {
                            _jsd.path_id = map.get("PATH_ID").toString();
                        }
                        if (map.get("GROUP_ID") != null) {
                            _jsd.groupid = map.get("GROUP_ID").toString();
                        }
                        if (map.get("ZNODE_ID") != null) {
                            _jsd.node_id = map.get("ZNODE_ID").toString();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.info("GROUPID,PATHID匹配数据失败：" + mes + "\r\n" + "---SQL:" + str_sql);
        }

        return _jsd;
    }

    public static List load_switch_circuit_jx(util.GetSql.csnms _csnms) {
        List list = new ArrayList();
        strclass.Switch_circuit_new _scn = new strclass.Switch_circuit_new();
        strclass.Jstranspathperf_detail _scd = new strclass.Jstranspathperf_detail();
        String str_sql = "select  "
                + "s.PATHNAME,"
                + "s.NODENAME,"
                + "to_char(s.GETTIME ,'yyyy-mm-dd hh24:mi:ss') as GETTIME,"
                + "s.INOPTICAL,"
                + "s.OUTOPTICAL,"
                + "s.NOTAVAILABLE,"
                + "s.BACKERROR,"
                + "s.STR,"
                + "s.SLOT_TYPE_PORT,"
                + "s.SEVERELY,"
                + "s.NOTAVAILABLE2,"
                + "s.pathid as PATHID,"
                + "s.groupid as GROUPID,"
                + "s.SLOT as SLOT,"
                + "s.PORT as PORT,"
                + "s.NODEID as NODEID,"
                + "s.SLOTYID as SLOTYID "
                + " from  switch_circuit_jx  s "
                + " where  length(s.pathid)>0 order by s.GETTIME";

        List list_sc = new ArrayList();
        try {
            list_sc = _csnms.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_sc.size(); i++) {
            _scd = new strclass.Jstranspathperf_detail();

            //log.info("Load switch_circuit 进度：" + (i + 1) + "/" + list_sc.size());
            HashMap map = (HashMap) list_sc.get(i);

            if (map.get("PATHNAME") != null) {
                _scd.path_name = map.get("PATHNAME").toString();
            }
            if (map.get("NODENAME") != null) {
                _scd.node_name = map.get("NODENAME").toString();
            }

            if (map.get("SLOT_TYPE_PORT") != null) {
                _scd.slot_type_port = map.get("SLOT_TYPE_PORT").toString();
                String str[] = _scd.slot_type_port.split("-");
                if (str.length == 3) {
                    _scd.slot = str[0].toString().trim();
                    _scd.port = str[2].toString().trim();
                }
            }

            if (map.get("GETTIME") != null) {
                _scd.gettime = map.get("GETTIME").toString();
            }

            if (map.get("INOPTICAL") != null) {
                _scd.inoptical = map.get("INOPTICAL").toString();
            }
            if (map.get("OUTOPTICAL") != null) {
                _scd.outoptical = map.get("OUTOPTICAL").toString();
            }
            if (map.get("NOTAVAILABLE") != null) {
                _scd.notavailable = map.get("NOTAVAILABLE").toString();
            }
            if (map.get("BACKERROR") != null) {
                _scd.backerror = map.get("BACKERROR").toString();
            }
            if (map.get("STR") != null) {
                _scd.str = map.get("STR").toString();
            }
            if (map.get("SEVERELY") != null) {
                _scd.severely = map.get("SEVERELY").toString();
            }
            if (map.get("NOTAVAILABLE2") != null) {
                _scd.notavailable2 = map.get("NOTAVAILABLE2").toString();
            }
            if (map.get("PATHID") != null) {
                if (!_scd.path_id.equals("-1")) {
                    _scd.path_id = map.get("PATHID").toString();
                } else {
                    _scd.path_id = "";
                }
            }
            if (map.get("GROUPID") != null) {
                _scd.groupid = map.get("GROUPID").toString();
            }

            if (map.get("SLOT") != null) {
                _scd.slot = map.get("SLOT").toString();
            }

            if (map.get("PORT") != null) {
                _scd.port = map.get("PORT").toString();
            }

            if (map.get("NODEID") != null) {
                _scd.node_id = map.get("NODEID").toString();
            }

            if (map.get("SLOTYID") != null) {
                _scd.slottypeid = map.get("SLOTYID").toString();
            }

            list.add(_scd);//写入list
        }
        return list;

    }

    public static List load_Jstranspathperf_detail_2(util.GetSql.csnms _csnms) {
        List list = new ArrayList();
        strclass.Jstranspathperf_detail_2 _jsd2 = new strclass.Jstranspathperf_detail_2();
        String str_sql3 = " select   "
                + "j.pathid as PATH_ID,"
                + "j.nodeid as NODE_ID,"
                + "j.slot_type_port as SLOT_TYPE_PORT ,"
                + "j.notavailable2 as NOTAVAILABLE2 "
                + "from Jstranspathperf_detail  j  where "
                + "j.gettime=(select  max(k.gettime) from Jstranspathperf_detail  k )";

        List list_l = new ArrayList();
        try {
            list_l = _csnms.getdata(str_sql3, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_l.size(); i++) {
            System.out.println("进度：" + (i + 1) + "/" + list_l.size());
            HashMap map = (HashMap) list_l.get(i);
            _jsd2 = new strclass.Jstranspathperf_detail_2();
            if (map.get("PATH_ID") != null) {
                _jsd2.PATH_ID = map.get("PATH_ID").toString();
            }
            if (map.get("NODE_ID") != null) {
                _jsd2.NODE_ID = map.get("NODE_ID").toString();
            }
            if (map.get("SLOT_TYPE_PORT") != null) {
                _jsd2.SLOT_TYPE_PORT = map.get("SLOT_TYPE_PORT").toString();
            }
            if (map.get("NOTAVAILABLE2") != null) {
                _jsd2.NOTAVAILABLE2 = map.get("NOTAVAILABLE2").toString();
            }
            list.add(_jsd2);//写入list
        }
        return list;

    }

    public static void load_pathperf(util.GetSql.csnms _csnms) {
        List list = new ArrayList();
        strclass.perfinstance _perfin = new strclass.perfinstance();
        String str_sql3 = "select  p.PATHID ,p.groupid as GROUPID  from pathperf  p ";

        List list_slottype = new ArrayList();
        try {
            list_slottype = _csnms.getdata(str_sql3, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_slottype.size(); i++) {
            HashMap map = (HashMap) list_slottype.get(i);
            String _pathid = map.get("PATHID").toString();
            String _groupid = map.get("GROUPID").toString();
            String key = _groupid + "#" + _pathid;
            keyHash3.put(key, key);
        }
    }

    public static void load_old_data(util.GetSql.csnms _csnms) {
        String str_sql = "select  "
                + "j.pathid as PATH_ID,"
                + "j.nodeid as NODE_ID,"
                + "j.slot_type_port as FULLTYPE  "
                + " from JSTRANSPATHPERF_DETAIL_JX  j";

        List _list = _csnms.getdata(str_sql, null);

        if (_list.size() > 0) {
            for (int i = 0, m = _list.size(); i < m; i++) {
                HashMap map = (HashMap) _list.get(0);
                String key = "";
                if (map.get("PATH_ID") != null) {
                    key += "#PATH:" + map.get("PATH_ID").toString();
                }
                if (map.get("NODE_ID") != null) {
                    key += "#NODEID:" + map.get("NODE_ID").toString();
                }
                if (map.get("FULLTYPE") != null) {
                    key += "#SLOT_TYPE_PORT:" + map.get("FULLTYPE").toString();
                }

                if (key.length() > 0) {
                    key_olddate.put(key, key);
                }
            }
        }

    }

    public static void chuli_alarm(strclass.Jstranspathperf_detail _scd, List list_perfinstance, org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        try {
            String str_sql_in = "";
            List list_node_sql = new ArrayList();

            //类型不为空的数据
            boolean _bk = true;

            if (_scd.slottypeid.length() > 0) {

                if (_ll) {
                    //判断是否发送告警
                    if (Double.parseDouble(_scd.inoptical) < -30 && Double.parseDouble(_scd.inoptical) != -60) {
                        log.info("===================>[电路]开始处理告警信息:");
                        log.info("===================>Node_name=" + _scd.node_name + ",Node_id=" + _scd.node_id + ",Path_id=" + _scd.path_id + ",Slot=" + _scd.slot + ",Port=" + _scd.port + ",Value=" + _scd.inoptical);

                        String str_alarm = db.return_alarm_path_open(_scd.node_name, _scd.node_id, _scd.path_id, 3, _scd.slot, _scd.port, _scd.inoptical, "光功率过低", "6", "<光功率过低>光功率过低小于-30 设备名称:" + _scd.node_name + " " + _scd.slot_type_port + " 光功率：" + _scd.inoptical, _csnms);
                        boolean _send = fun.sendAlarm(str_alarm);
                        if (_send) {
                            log.info("===================>[电路]告警发送成功:" + str_alarm);
                        } else {
                            log.info("===================>[电路]告警发送失败:" + str_alarm);
                        }

                        log.info("===================>[设备]开始处理告警信息:");
                        log.info("===================>Node_name=" + _scd.node_name + ",Node_id=" + _scd.node_id + ",Path_id=" + _scd.path_id + ",Slot=" + _scd.slot + ",Port=" + _scd.port + ",Value=" + _scd.inoptical);
                        String str_alarm2 = db.return_alarm_device_open(_scd.node_id, _scd.path_id, 3, "光功率过低", "4", "<光功率过低>光功率过低小于-30 设备名称:" + _scd.node_name + " " + _scd.slot_type_port + " 光功率：" + _scd.inoptical, _scd.slot, _scd.port, _csnms);
                        boolean _send2 = fun.sendAlarm(str_alarm2);
                        if (_send2) {
                            log.info("===================>[设备]告警发送成功:" + str_alarm2);
                        } else {
                            log.info("===================>[设备]告警发送失败:" + str_alarm2);
                        }
                        _bk = false;
                    }
                }

                //处理是否已存在异常
                String str_sql_in2 = " ";
                if (!fun.iserror(_scd.inoptical, "inoptical", _scd.slottypeid, list_perfinstance)) {
                    if (!serch_has_JSPERFERROR_NODE(_scd, _scd.slottypeid, _csnms)) {
                        str_sql_in2 = "insert   into  JSPERFERROR_NODE(GETTIME,PATHID,PERFID,PERFVALUE,NODEID,NODENAME,SLOT,PORT,SLOT_TYPE_PORT)values("
                                + "to_date('" + _scd.gettime + "','yyyy-mm-dd hh24:mi:ss')" + ","
                                + "" + _scd.path_id + "" + ","
                                + "" + _scd.slottypeid + ","
                                + "" + _scd.inoptical + "" + ","
                                + "" + _scd.node_id + "" + ","
                                + "'" + _scd.node_name + "'" + ","
                                + "'" + _scd.slot + "'" + ","
                                + "'" + _scd.port + "'" + ","
                                + "'" + _scd.slot_type_port + "'"
                                + ")";
                        list_node_sql.add(str_sql_in2);
                    }
                }

                if (Double.parseDouble(_scd.notavailable) != 0) {

                    if (!serch_has_JSPERFERROR_NODE(_scd, "30", _csnms)) {
                        str_sql_in2 = "insert   into  JSPERFERROR_NODE(GETTIME,PATHID,PERFID,PERFVALUE,NODEID,NODENAME,SLOT,PORT,SLOT_TYPE_PORT)values("
                                + "to_date('" + _scd.gettime + "','yyyy-mm-dd hh24:mi:ss')" + ","
                                + "" + _scd.path_id + "" + ","
                                + "" + 30 + "" + ","
                                + "" + _scd.notavailable + "" + ","
                                + "" + _scd.node_id + "" + ","
                                + "'" + _scd.node_name + "'" + ","
                                + "'" + _scd.slot + "'" + ","
                                + "'" + _scd.port + "'" + ","
                                + "'" + _scd.slot_type_port + "'"
                                + ")";
                        list_node_sql.add(str_sql_in2);
                    }

                }

                if (Double.parseDouble(_scd.backerror) != 0) {
                    if (!serch_has_JSPERFERROR_NODE(_scd, "31", _csnms)) {
                        str_sql_in2 = "insert   into  JSPERFERROR_NODE(GETTIME,PATHID,PERFID,PERFVALUE,NODEID,NODENAME,SLOT,PORT,SLOT_TYPE_PORT)values("
                                + "to_date('" + _scd.gettime + "','yyyy-mm-dd hh24:mi:ss')" + ","
                                + "" + _scd.path_id + "" + ","
                                + "" + 31 + "" + ","
                                + "" + _scd.backerror + "" + ","
                                + "" + _scd.node_id + "" + ","
                                + "'" + _scd.node_name + "'" + ","
                                + "'" + _scd.slot + "'" + ","
                                + "'" + _scd.port + "'" + ","
                                + "'" + _scd.slot_type_port + "'"
                                + ")";
                        list_node_sql.add(str_sql_in2);
                    }
                }

                if (Double.parseDouble(_scd.str) != 0) {
                    if (!serch_has_JSPERFERROR_NODE(_scd, "32", _csnms)) {
                        str_sql_in2 = "insert   into  JSPERFERROR_NODE(GETTIME,PATHID,PERFID,PERFVALUE,NODEID,NODENAME,SLOT,PORT,SLOT_TYPE_PORT)values("
                                + "to_date('" + _scd.gettime + "','yyyy-mm-dd hh24:mi:ss')" + ","
                                + "" + _scd.path_id + "" + ","
                                + "" + 32 + "" + ","
                                + "" + _scd.str + "" + ","
                                + "" + _scd.node_id + "" + ","
                                + "'" + _scd.node_name + "'" + ","
                                + "'" + _scd.slot + "'" + ","
                                + "'" + _scd.port + "'" + ","
                                + "'" + _scd.slot_type_port + "'"
                                + ")";
                        list_node_sql.add(str_sql_in2);
                    }
                }

                if (Double.parseDouble(_scd.severely) != 0) {
                    if (!serch_has_JSPERFERROR_NODE(_scd, "33", _csnms)) {
                        str_sql_in2 = "insert   into  JSPERFERROR_NODE(GETTIME,PATHID,PERFID,PERFVALUE,NODEID,NODENAME,SLOT,PORT,SLOT_TYPE_PORT)values("
                                + "to_date('" + _scd.gettime + "','yyyy-mm-dd hh24:mi:ss')" + ","
                                + "" + _scd.path_id + "" + ","
                                + "" + 33 + "" + ","
                                + "" + _scd.severely + "" + ","
                                + "" + _scd.node_id + "" + ","
                                + "'" + _scd.node_name + "'" + ","
                                + "'" + _scd.slot + "'" + ","
                                + "'" + _scd.port + "'" + ","
                                + "'" + _scd.slot_type_port + "'"
                                + ")";
                        list_node_sql.add(str_sql_in2);
                    }
                }
            }

            if (_bk) {
                //判断是否有告警  如果有的话，那么发送清除告警
                String str_sql_see = "select  a.node_id from alarm a  where  a.node_id=" + _scd.node_id + "  and a.alarm_type=6  and a.alarm_content='光功率过低' and  a.description like '<光功率过低>光功率过低小于-30 设备名称:" + _scd.node_name + " " + _scd.slot_type_port + " 光功率%'";

                List ls_s2 = new ArrayList();
                try {
                    ls_s2 = _csnms.getdata(str_sql_see, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ls_s2.size() > 0) {
                    log.info("===================>[电路]开始处理告警信息:");
                    log.info("===================>Node_name=" + _scd.node_name + ",Node_id=" + _scd.node_id + ",Path_id=" + _scd.path_id + ",Slot=" + _scd.slot + ",Port=" + _scd.port + ",Value=" + _scd.inoptical);
                    String str_alarm = return_alarm_path_clear(_scd.node_name, _scd.node_id, _scd.path_id, 3, _scd.slot, _scd.port, _scd.inoptical, "光功率过低", "6", "<光功率过低>光功率过低小于-30 设备名称:" + _scd.node_name + " " + _scd.slot_type_port + " 光功率：" + _scd.inoptical, _csnms);
                    boolean _send = fun.sendAlarm(str_alarm);
                    if (_send) {
                        log.info("===================>[电路]清除告警发送成功:" + str_alarm);
                    } else {
                        log.info("===================>[电路]清除告警发送失败:" + str_alarm);
                    }
                }
                ls_s2.clear();

                String str_sql_see2 = "select  a.node_id from alarm a  where  a.node_id=" + _scd.node_id + "  and a.alarm_type=4  and a.alarm_content='光功率过低'  and  a.description like  '<光功率过低>光功率过低小于-30 设备名称:" + _scd.node_name + " " + _scd.slot_type_port + " 光功率%'";

                List ls_s22 = new ArrayList();
                try {
                    ls_s22 = _csnms.getdata(str_sql_see2, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ls_s22.size() > 0) {
                    log.info("===================>[设备]开始处理告警信息:");
                    log.info("===================>Node_name=" + _scd.node_name + ",Node_id=" + _scd.node_id + ",Path_id=" + _scd.path_id + ",Slot=" + _scd.slot + ",Port=" + _scd.port + ",Value=" + _scd.inoptical);
                    String str_alarm2 = return_alarm_device_clear(_scd.node_id, _scd.path_id, 3, "光功率过低", "4", "<光功率过低>光功率过低小于-30 设备名称:" + _scd.node_name + " " + _scd.slot_type_port + " 光功率：" + _scd.inoptical, _scd.slot, _scd.port, _csnms);
                    boolean _send2 = fun.sendAlarm(str_alarm2);
                    if (_send2) {
                        log.info("===================>[设备]清除告警发送成功:" + str_alarm2);
                    } else {
                        log.info("===================>[设备]清除告警发送失败:" + str_alarm2);
                    }
                }
                ls_s22.clear();
            }

            if (list_node_sql.size() > 0) {
                try {
                    _csnms.execute(list_node_sql);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("异常：" + e.getMessage().toString());
                }
            }
        } catch (Exception ex) {
            String mes = ex.getMessage().toString();
            log.info("异常：" + mes);
        }
    }

    public static void tb_in_up_JSTRANSPATHPERF_DE(String type_in_up, List _data, util.GetSql.csnms _csnms, org.apache.log4j.Logger log) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                count_s = count_s + 1;
                strclass.Jstranspathperf_detail _datal = new strclass.Jstranspathperf_detail();
                _datal = (strclass.Jstranspathperf_detail) _data.get(i);

                //-------------处理时间---------------//
                String _GETTIME = _datal.gettime;
                //SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //Date _date1 = new Date();
                //_date1 = sdf_temp.parse(_GETTIME);
                //------------------------------------//
                if (type_in_up.equals("in")) {
                    String str_sql = "insert into JSTRANSPATHPERF_DETAIL("
                            + "PATHID,"
                            + "PATHNAME,"
                            + "GETTIME,"
                            + "INOPTICAL,"
                            + "OUTOPTICAL,"
                            + "NOTAVAILABLE,"
                            + "BACKERROR,"
                            + "STR,"
                            + "severely,"
                            + "NODEID,"
                            + "NODENAME,"
                            + "SLOT,"
                            + "PORT,"
                            + "NOTAVAILABLE2,"
                            + "SLOT_TYPE_PORT,"
                            + "slottypeid"
                            + ") values ("
                            + Long.parseLong(_datal.path_id.toString()) + ","
                            + "'" + _datal.path_name.toString() + "'" + ","
                            + "to_date('" + _GETTIME + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + Double.parseDouble(_datal.inoptical.toString()) + ","
                            + Double.parseDouble(_datal.outoptical.toString()) + ","
                            + Double.parseDouble(_datal.notavailable.toString()) + ","
                            + Double.parseDouble(_datal.backerror.toString()) + ","
                            + Double.parseDouble(_datal.str.toString()) + ","
                            + Double.parseDouble(_datal.severely.toString()) + ","
                            + Double.parseDouble(_datal.node_id.toString()) + ","
                            + "'" + _datal.node_name.toString() + "'" + ","
                            + "'" + _datal.slot.toString() + "'" + ","
                            + "'" + _datal.port.toString() + "'" + ","
                            + Double.parseDouble(_datal.notavailable2.toString()) + ","
                            + "'" + _datal.slot_type_port.toString() + "'" + ","
                            + Double.parseDouble(_datal.slottypeid.toString())
                            + ")";
                    _csnms.execute(str_sql, null);
                } else if (type_in_up.equals("in2")) {
                    String str_sql = "insert into JSTRANSPATHPERF_DETAIL_JX("
                            + "PATHID,"
                            + "PATHNAME,"
                            + "GETTIME,"
                            + "INOPTICAL,"
                            + "OUTOPTICAL,"
                            + "NOTAVAILABLE,"
                            + "BACKERROR,"
                            + "STR,"
                            + "severely,"
                            + "NODEID,"
                            + "NODENAME,"
                            + "SLOT,"
                            + "PORT,"
                            + "NOTAVAILABLE2,"
                            + "SLOT_TYPE_PORT,"
                            + "slottypeid"
                            + ") values ("
                            + Long.parseLong(_datal.path_id.toString()) + ","
                            + "'" + _datal.path_name.toString() + "'" + ","
                            + "to_date('" + _GETTIME + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + Double.parseDouble(_datal.inoptical.toString()) + ","
                            + Double.parseDouble(_datal.outoptical.toString()) + ","
                            + Double.parseDouble(_datal.notavailable.toString()) + ","
                            + Double.parseDouble(_datal.backerror.toString()) + ","
                            + Double.parseDouble(_datal.str.toString()) + ","
                            + Double.parseDouble(_datal.severely.toString()) + ","
                            + Double.parseDouble(_datal.node_id.toString()) + ","
                            + "'" + _datal.node_name.toString() + "'" + ","
                            + "'" + _datal.slot.toString() + "'" + ","
                            + "'" + _datal.port.toString() + "'" + ","
                            + Double.parseDouble(_datal.notavailable2.toString()) + ","
                            + "'" + _datal.slot_type_port.toString() + "'" + ","
                            + Double.parseDouble(_datal.slottypeid.toString())
                            + ")";
                    _csnms.execute(str_sql, null);
                } else if (type_in_up.equals("up2")) {
                    String str_sql = "update  JSTRANSPATHPERF_DETAIL_JX j set "
                            + "j.INOPTICAL=" + Double.parseDouble(_datal.inoptical.toString()) + ","
                            + "j.OUTOPTICAL=" + Double.parseDouble(_datal.outoptical.toString()) + ","
                            + "j.NOTAVAILABLE=" + Double.parseDouble(_datal.notavailable.toString()) + ","
                            + "j.BACKERROR=" + Double.parseDouble(_datal.backerror.toString()) + ","
                            + "j.STR=" + Double.parseDouble(_datal.str.toString()) + ","
                            + "j.SEVERELY=" + Double.parseDouble(_datal.severely.toString()) + ","
                            + "j.NOTAVAILABLE2=" + Double.parseDouble(_datal.notavailable2.toString()) + ","
                            + "j.SLOTTYPEID=" + Double.parseDouble(_datal.slottypeid.toString()) + ""
                            + " where  1=1"
                            + "  and j.PATHID=" + _datal.path_id
                            + "  and j.NODEID=" + _datal.node_id
                            + "  and j.SLOT_TYPE_PORT=" + _datal.slot_type_port
                            + "";
                    _csnms.execute(str_sql, null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("异常：" + e.getMessage().toString());
        }
    }

    public static void update_data_path(List list_cir, org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {

        List list_path_sql = new ArrayList();

        if (list_cir.size() > 0) {

            List JSTRANSPATHPERF_add = new ArrayList();
            List JSTRANSPATHPERF_up = new ArrayList();

            List JSPERFERROR_add = new ArrayList();
            List JSPERFERROR_up = new ArrayList();

            List pathperf_up = new ArrayList();

            int count_s = 0;

            for (int i = 0, m = list_cir.size(); i < m; i++) {
                log.info("处理电路进度：" + (i + 1) + "/" + m);
                count_s = count_s + 1;
                strclass.Jstranspathperf_detail _scd1 = new strclass.Jstranspathperf_detail();
                _scd1 = (strclass.Jstranspathperf_detail) list_cir.get(i);

                String new_pathname = _scd1.path_name;
                String new_nodename = _scd1.node_name;
                String new_portname = _scd1.slot_type_port;
                String key = new_pathname;

                if (!keyHash2.containsKey(key)) {
                    keyHash2.put(key, key);
                } else {
                    continue;
                }

                strclass.Jstranspathperf_detail _scd = new strclass.Jstranspathperf_detail();
                _scd = _scd1;

                String sql_inoptical = "select  min ( s.INOPTICAL) as NUM  from SWITCH_CIRCUIT_JX s where s.PATHNAME='" + _scd.path_name + "'  and  s.GETTIME=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
                String sql_outoptical = "select  max ( s.OUTOPTICAL) as NUM  from SWITCH_CIRCUIT_JX s  where s.PATHNAME='" + _scd.path_name + "'  and  s.GETTIME=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
                String sql_notavailable = "select  min ( s.NOTAVAILABLE2) as NUM  from SWITCH_CIRCUIT_JX s  where  s.PATHNAME='" + _scd.path_name + "'  and  s.GETTIME=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
                String sql_backerror = "select  min ( s.BACKERROR) as NUM  from SWITCH_CIRCUIT_JX s  where  s.PATHNAME='" + _scd.path_name + "'  and  s.GETTIME=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
                String sql_str = "select  min ( s.STR) as NUM  from SWITCH_CIRCUIT_JX s  where  s.PATHNAME='" + _scd.path_name + "'  and  s.GETTIME=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
                String sql_severely = "select  min ( s.SEVERELY) as NUM  from SWITCH_CIRCUIT_JX s  where  s.PATHNAME='" + _scd.path_name + "'  and   s.GETTIME=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";

                String sql_all = sql_inoptical + "  union  all  "
                        + sql_outoptical + "  union  all  "
                        + sql_notavailable + "  union  all  "
                        + sql_backerror + "  union  all  "
                        + sql_str + "  union  all  "
                        + sql_severely;

                List list_data = new ArrayList();
                try {
                    list_data = _csnms.getdata(sql_all, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (int i2 = 0; i2 < list_data.size(); i2++) {
                    HashMap map = (HashMap) list_data.get(i2);

                    if (i2 == 0) {
                        try {
                            _scd.inoptical = map.get("NUM").toString();
                        } catch (Exception ex) {
                            _scd.inoptical = "0";
                        }

                    } else if (i2 == 1) {
                        try {
                            _scd.outoptical = map.get("NUM").toString();
                        } catch (Exception ex) {
                            _scd.outoptical = "0";
                        }

                    } else if (i2 == 2) {
                        try {
                            _scd.notavailable2 = map.get("NUM").toString();
                        } catch (Exception ex) {
                            _scd.notavailable2 = "0";
                        }
                    } else if (i2 == 3) {
                        try {
                            _scd.backerror = map.get("NUM").toString();
                        } catch (Exception ex) {
                            _scd.backerror = "0";
                        }
                    } else if (i2 == 4) {
                        try {
                            _scd.str = map.get("NUM").toString();
                        } catch (Exception ex) {
                            _scd.str = "0";
                        }
                    } else if (i2 == 5) {
                        try {
                            _scd.severely = map.get("NUM").toString();
                        } catch (Exception ex) {
                            _scd.severely = "0";
                        }
                    }
                }

                boolean _bs = true;
                if (_scd.inoptical.equals("0")) {
                    if (_scd.outoptical.equals("0")) {
                        if (_scd.notavailable2.equals("0")) {
                            if (_scd.backerror.equals("0")) {
                                if (_scd.str.equals("0")) {
                                    if (_scd.severely.equals("0")) {
                                        _bs = false;
                                    }
                                }
                            }
                        }
                    }
                }

                if (_bs) {

                    if (_scd.node_id.toString().length() == 0) {
                        //log.info("系统中没有此NODE，请先处理:" + _scd.node_name);
                        _bs = false;
                    }

                    if (_scd.path_id.toString().length() == 0 || _scd.path_id.toString().equals("-1")) {
                        //log.info("系统中没有此电路，请先处理:" + _scd.path_name);
                        _bs = false;
                    }
                }

                List list_sql = new ArrayList();

                boolean _has_his = serch_has_JSTRANSPATHPERF(_scd, _csnms);
                boolean _has_his2 = serch_has_JSPERFERROR(_scd, _csnms);

                if (_bs) {

                    if (_has_his) {
                        JSTRANSPATHPERF_up.add(_scd);
                    } else {
                        JSTRANSPATHPERF_add.add(_scd);
                    }

                    String str_sql2 = "";
                    if (fun.seach_has_data4(_scd)) {
                        pathperf_up.add(_scd);

                        String str_Sql = "select p.path_id as PATH_ID,p.parentid as PARENTID from  path p  where p.path_id<>" + _scd.path_id + " and   p.name='" + _scd.path_name + "' and p.parentid>0";

                        try {
                            List _list_s = _csnms.getdata(str_Sql, null);
                            if (_list_s.size() > 0) {
                                for (int i2 = 0, m2 = _list_s.size(); i2 < m2; i2++) {
                                    HashMap map = (HashMap) _list_s.get(i2);
                                    if (map.get("PARENTID") != null) {
                                        strclass.Jstranspathperf_detail _scd2 = new strclass.Jstranspathperf_detail();

                                        _scd2.inoptical = _scd.inoptical.toString();
                                        _scd2.outoptical = _scd.outoptical.toString();
                                        _scd2.notavailable = _scd.notavailable.toString();
                                        _scd2.backerror = _scd.backerror.toString();
                                        _scd2.str = _scd.str.toString();
                                        _scd2.path_id = _scd.path_id.toString();

                                        String _parentid = "";
                                        _parentid = map.get("PARENTID").toString();
                                        if (_parentid.length() > 0) {
                                            _scd2.path_id = _parentid;
                                            pathperf_up.add(_scd2);
                                        }
                                    }

                                    if (map.get("PATH_ID") != null) {
                                        strclass.Jstranspathperf_detail _scd3 = new strclass.Jstranspathperf_detail();
                                        _scd3.inoptical = _scd.inoptical.toString();
                                        _scd3.outoptical = _scd.outoptical.toString();
                                        _scd3.notavailable = _scd.notavailable.toString();
                                        _scd3.backerror = _scd.backerror.toString();
                                        _scd3.str = _scd.str.toString();
                                        _scd3.path_id = _scd.path_id.toString();

                                        String _pathid = "";
                                        _pathid = map.get("PATH_ID").toString();
                                        if (_pathid.length() > 0) {
                                            _scd3.path_id = _pathid;
                                            pathperf_up.add(_scd3);
                                        }
                                    }

                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    } else {
                        str_sql2 = "insert   into  pathperf ("
                                + "GROUPID,PATHID,IFIN,IFOUT,IFINERROR,IFOUTERROR,IFINDISCAR,IFOUTDISCAR,LATETIME,SHAKE,PERNODEID,SPEED,SERVICETYPE,"
                                + "INOPTICAL,OUTOPTICAL,NOTAVAILABLE,BACKERROR,STR)"
                                + "values("
                                + _scd.groupid + ","
                                + _scd.path_id + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "0" + ","
                                + "17" + ","
                                + _scd.inoptical + ","
                                + _scd.outoptical + ","
                                + _scd.notavailable + ","
                                + _scd.backerror + ","
                                + _scd.str
                                + ")";
                    }
                    if (str_sql2.length() > 0) {
                        list_path_sql.add(str_sql2);
                    }

                    //处理是否异常
                    if (_scd.slottypeid.length() > 0) {

                        boolean _sss = true;

                        if (!fun.iserror(_scd.inoptical, "inoptical", _scd.slottypeid, list_perfinstance)) {
                            if (_sss) {
                                if (_has_his2) {
                                    JSPERFERROR_up.add(_scd);
                                } else {
                                    JSPERFERROR_add.add(_scd);
                                }
                                _sss = false;
                            }
                        }

                        if (!fun.iserror(_scd.outoptical, "outoptical", _scd.slottypeid, list_perfinstance)) {
                            if (_sss) {
                                if (_has_his2) {
                                    JSPERFERROR_up.add(_scd);
                                } else {
                                    JSPERFERROR_add.add(_scd);
                                }
                                _sss = false;
                            }
                        }

                        if (!fun.iserror(_scd.notavailable, "notavailable", _scd.slottypeid, list_perfinstance)) {
                            if (_sss) {
                                if (_has_his2) {
                                    JSPERFERROR_up.add(_scd);
                                } else {
                                    JSPERFERROR_add.add(_scd);
                                }
                                _sss = false;
                            }
                        }

                        if (!fun.iserror(_scd.backerror, "backerror", _scd.slottypeid, list_perfinstance)) {
                            if (_sss) {
                                if (_has_his2) {
                                    JSPERFERROR_up.add(_scd);
                                } else {
                                    JSPERFERROR_add.add(_scd);
                                }
                                _sss = false;
                            }
                        }

                        if (!fun.iserror(_scd.str, "str", _scd.slottypeid, list_perfinstance)) {

                            if (_sss) {
                                if (_has_his2) {
                                    JSPERFERROR_up.add(_scd);
                                } else {
                                    JSPERFERROR_add.add(_scd);
                                }
                                _sss = false;
                            }
                        }

                        if (!fun.iserror(_scd.severely, "severely", _scd.slottypeid, list_perfinstance)) {
                            if (_sss) {
                                if (_has_his2) {
                                    JSPERFERROR_up.add(_scd);
                                } else {
                                    JSPERFERROR_add.add(_scd);
                                }
                                _sss = false;
                            }
                        }
                    }
                }

                if (count_s > 2000) {
                    if (list_path_sql.size() > 0) {
                        _csnms.execute(list_path_sql);
                        list_path_sql.clear();
                    }

                    if (JSTRANSPATHPERF_add.size() > 0) {
                        //getSql.SQL_Thread_Execute(list_path_sql);
                        tb_in_up_JSTRANSPATHPERF("in", JSTRANSPATHPERF_add, _csnms, log);
                        JSTRANSPATHPERF_add.clear();
                    }

                    if (JSTRANSPATHPERF_up.size() > 0) {
                        //getSql.SQL_Thread_Execute(list_path_sql);
                        tb_in_up_JSTRANSPATHPERF("up", JSTRANSPATHPERF_up, _csnms, log);
                        JSTRANSPATHPERF_up.clear();
                    }

                    if (JSPERFERROR_add.size() > 0) {
                        tb_in_up_JSPERFERROR("in", JSPERFERROR_add, _csnms, log);
                        JSPERFERROR_add.clear();
                    }

                    if (JSPERFERROR_up.size() > 0) {
                        tb_in_up_JSPERFERROR("up", JSPERFERROR_up, _csnms, log);
                        JSPERFERROR_up.clear();
                    }

                    if (pathperf_up.size() > 0) {
                        tb_in_up_pathperf("up", pathperf_up, _csnms, log);
                        pathperf_up.clear();
                    }
                    count_s = 0;
                }

            }

            if (list_path_sql.size() > 0) {
                _csnms.execute(list_path_sql);
                list_path_sql.clear();
            }

            if (JSTRANSPATHPERF_add.size() > 0) {
                //getSql.SQL_Thread_Execute(list_path_sql);
                tb_in_up_JSTRANSPATHPERF("in", JSTRANSPATHPERF_add, _csnms, log);
                JSTRANSPATHPERF_add.clear();
            }

            if (JSTRANSPATHPERF_up.size() > 0) {
                //getSql.SQL_Thread_Execute(list_path_sql);
                tb_in_up_JSTRANSPATHPERF("up", JSTRANSPATHPERF_up, _csnms, log);
                JSTRANSPATHPERF_up.clear();
            }

            if (JSPERFERROR_add.size() > 0) {
                tb_in_up_JSPERFERROR("in", JSPERFERROR_add, _csnms, log);
                JSPERFERROR_add.clear();
            }

            if (JSPERFERROR_up.size() > 0) {
                tb_in_up_JSPERFERROR("up", JSPERFERROR_up, _csnms, log);
                JSPERFERROR_up.clear();
            }

            if (pathperf_up.size() > 0) {
                tb_in_up_pathperf("up", pathperf_up, _csnms, log);
                pathperf_up.clear();
            }

        }

    }

    public static boolean serch_has_JSTRANSPATHPERF(strclass.Jstranspathperf_detail _scd, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_scd.path_id.length() > 0) {
            String str_sql = "select  count(1) as COUNT  from  JSTRANSPATHPERF j  where  j.pathid=" + _scd.path_id + " and j.gettime=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
            List _list_count = new ArrayList();
            try {
                _list_count = _csnms.getdata(str_sql, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String count = "0";
            if (_list_count.size() > 0) {
                HashMap map = (HashMap) _list_count.get(0);
                count = map.get("COUNT").toString();
            }

            if (count.equals("0")) {
                _bs = false;
            } else {
                _bs = true;
            }
        }
        return _bs;
    }

    public static String return_alarm_path_open(String nodename, String nodeid, String pathid, int _alarmlevel, String _slot, String _port, String _value, String _alm_Con, String _alarm_type, String str_desc, util.GetSql.csnms _csnms) {

        strclass.Alarm _alarm = new strclass.Alarm();
        /*
         ALARMLEVEL.put("1", "CRITICAL");
         ALARMLEVEL.put("2", "MAJOR");
         ALARMLEVEL.put("3", "MINOR");
         ALARMLEVEL.put("4", "WARNING");
         */

        String str_sql_se = "select  p.ems_type as VENDOR_TYPE from path p  where  p.path_id  =" + pathid;

        List ls_s = new ArrayList();
        Object[] objs = new Object[]{};
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
        }

        //更改nodeid    
        /*
         String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew.equals(kk)) {
         nodeid = nodeid.substring(0, nodeid.length() - 4);
         break;
         }
         }
         */
        //更改pathod
        String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew2.equals(kk)) {
                pathid = pathid.substring(0, pathid.length() - 4);
                break;
            }
        }

        _alarm.node_name = nodename;
        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*电路告警  */
        StringBuffer sb = new StringBuffer();
        sb.append("type=0")
                .append(",event_name=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=OPEN")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port)
                .append(",path_id=").append(_alarm.path_id);
        //System.out.println(sb.toString());
        return sb.toString();
    }

    public static String return_alarm_device_open(String nodeid, String pathid, int _alarmlevel, String _alm_Con, String _alarm_type, String str_desc, String _slot, String _port, util.GetSql.csnms _csnms) {

        strclass.Alarm _alarm = new strclass.Alarm();
        /*
         "1", "CRITICAL" 
         "2", "MAJOR"
         "3", "MINOR"
         "4", "WARNING"
         */

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE,n.full_name as NODE_NAME  from  node  n   where  n.node_id  =" + nodeid;
        List ls_s = new ArrayList();
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
            _alarm.node_name = map.get("NODE_NAME").toString();
        }

        //更改nodeid    
        String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew.equals(kk)) {
                nodeid = nodeid.substring(0, nodeid.length() - 4);
                break;
            }
        }

        //更改pathod
         /*
         String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());
         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew2.equals(kk)) {
         pathid = pathid.substring(0, pathid.length() - 4);
         break;
         }
         }
         */
        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*设备告警  */
        StringBuffer sb = new StringBuffer();

        sb.append("type=0")
                .append(",alarm_status=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=OPEN")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",_vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port);

        return sb.toString();
    }

    public static boolean serch_has_JSPERFERROR_NODE(strclass.Jstranspathperf_detail _scd, String slottypeid, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_scd.path_id.length() > 0) {
            String str_sql = "select  count(1) as COUNT  from  JSPERFERROR_NODE j  where  "
                    + " j.gettime=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS') and "
                    + " j.PATHID=" + _scd.path_id + " and "
                    + " j.PERFID=" + slottypeid + " and "
                    + " j.SLOT_TYPE_PORT='" + _scd.slot_type_port + "' and "
                    + " j.NODEID=" + _scd.node_id;

            List _list_count = new ArrayList();
            try {
                _list_count = _csnms.getdata(str_sql, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String count = "0";
            if (_list_count.size() > 0) {
                HashMap map = (HashMap) _list_count.get(0);
                count = map.get("COUNT").toString();
            }

            if (count.equals("0")) {
                _bs = false;
            } else {
                _bs = true;
            }
        }
        return _bs;
    }

    public static String return_alarm_path_clear(String nodename, String nodeid, String pathid, int _alarmlevel, String _slot, String _port, String _value, String _alm_Con, String _alarm_type, String str_desc, util.GetSql.csnms _csnms) {

        strclass.Alarm _alarm = new strclass.Alarm();
        /*
         ALARMLEVEL.put("1", "CRITICAL");
         ALARMLEVEL.put("2", "MAJOR");
         ALARMLEVEL.put("3", "MINOR");
         ALARMLEVEL.put("4", "WARNING");
         */

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE,n.full_name as NODE_NAME  from  node  n   where  n.node_id  =" + nodeid;

        List ls_s = new ArrayList();
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
            _alarm.node_name = map.get("NODE_NAME").toString();
        }

        //更改nodeid    
        /*
         String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew.equals(kk)) {
         nodeid = nodeid.substring(0, nodeid.length() - 4);
         break;
         }
         }
         */
        //更改pathod
        String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew2.equals(kk)) {
                pathid = pathid.substring(0, pathid.length() - 4);
                break;
            }
        }

        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*电路告警  */
        StringBuffer sb = new StringBuffer();
        sb.append("type=0")
                .append(",event_name=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=CLEAR")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port)
                .append(",path_id=").append(_alarm.path_id);

        //System.out.println(sb.toString());
        return sb.toString();
    }

    public static String return_alarm_device_clear(String nodeid, String pathid, int _alarmlevel, String _alm_Con, String _alarm_type, String str_desc, String _slot, String _port, util.GetSql.csnms _csnms) {

        strclass.Alarm _alarm = new strclass.Alarm();

        String str_sql_se = "select n.vendor_type as VENDOR_TYPE,n.full_name as NODE_NAME  from  node  n   where  n.node_id  =" + nodeid;
        List ls_s = new ArrayList();
        try {
            ls_s = _csnms.getdata(str_sql_se, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ls_s.size() > 0) {
            HashMap map = (HashMap) ls_s.get(0);
            _alarm.vendor_type = map.get("VENDOR_TYPE").toString();
            _alarm.node_name = map.get("NODE_NAME").toString();
        }

        //更改nodeid    
        String StrNew = nodeid.substring(nodeid.length() - 4, nodeid.length());

        for (int i = 0; i < _list_node.size(); i++) {
            String kk = _list_node.get(i).toString();
            if (StrNew.equals(kk)) {
                nodeid = nodeid.substring(0, nodeid.length() - 4);
                break;
            }
        }

        //更改pathod
         /*
         String StrNew2 = pathid.substring(pathid.length() - 4, pathid.length());

         for (int i = 0; i < _list_node.size(); i++) {
         String kk = _list_node.get(i).toString();
         if (StrNew2.equals(kk)) {
         pathid = pathid.substring(0, pathid.length() - 4);
         break;
         }
         }
         */
        _alarm.node_id = nodeid;
        _alarm.path_id = pathid;

        if (_alarmlevel == 1) {
            _alarm.alarm_level = "CRITICAL";
        } else if (_alarmlevel == 2) {
            _alarm.alarm_level = "MAJOR";
        } else if (_alarmlevel == 3) {
            _alarm.alarm_level = "MINOR";
        } else if (_alarmlevel == 4) {
            _alarm.alarm_level = "WARNING";
        }

        /*设备告警  */
        StringBuffer sb = new StringBuffer();

        sb.append("type=0")
                .append(",event_name=UP")
                .append(",alarm_level=").append(_alarm.alarm_level)
                .append(",alarm_status=CLEAR")
                .append(",Alm_Con=").append(_alm_Con)
                .append(",alarm_update=").append(new Date().getTime() / 1000)
                .append(",alarm_type=").append(_alarm_type)
                .append(",_vendor_type=").append(_alarm.vendor_type)
                .append(",node_name=").append(_alarm.node_name)
                .append(",description=").append(str_desc)
                .append(",node_id=").append(_alarm.node_id)
                .append(",shelf_id=1")
                .append(",slot_id=").append(_slot)
                .append(",port_id=").append(_port);

        //System.out.println(sb.toString());
        return sb.toString();
    }

    public static boolean serch_has_JSPERFERROR(strclass.Jstranspathperf_detail _scd, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_scd.path_id.length() > 0) {
            String str_sql = "select  count(1) as COUNT  from  JSPERFERROR j  where  j.pathid=" + _scd.path_id + " and j.gettime=to_date('" + _scd.gettime + "','yyyy-MM-dd HH24:MI:SS')";
            Object[] objs = new Object[]{};
            List _list_count = new ArrayList();
            try {
                _list_count = _csnms.getdata(str_sql, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String count = "0";
            if (_list_count.size() > 0) {
                HashMap map = (HashMap) _list_count.get(0);
                count = map.get("COUNT").toString();
            }

            if (count.equals("0")) {
                _bs = false;
            } else {
                _bs = true;
            }

        }
        return _bs;
    }

    public static void tb_in_up_JSTRANSPATHPERF(String type_in_up, List _data, util.GetSql.csnms _csnms, org.apache.log4j.Logger log) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                count_s = count_s + 1;
                strclass.Jstranspathperf_detail _datal = new strclass.Jstranspathperf_detail();
                _datal = (strclass.Jstranspathperf_detail) _data.get(i);

                //---------------------处理时间------------------------//
                String _GETTIME = _datal.gettime;
                SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _date1 = new Date();
                _date1 = sdf_temp.parse(_GETTIME);
                //-----------------------------------------------------//

                if (type_in_up.equals("in")) {

                    String key = _datal.gettime + "#" + _datal.path_id;
                    if (!keyHash5.containsKey(key)) {
                        keyHash5.put(key, key);
                    } else {
                        continue;
                    }

                    String str_sql = "insert into JSTRANSPATHPERF("
                            + "PATHID,"
                            + "PATHNAME,"
                            + "GETTIME,"
                            + "INOPTICAL,"
                            + "OUTOPTICAL,"
                            + "NOTAVAILABLE,"
                            + "BACKERROR,"
                            + "STR,"
                            + "severely"
                            + ") values ("
                            + Long.parseLong(_datal.path_id.toString()) + ","
                            + "'" + _datal.path_name.toString() + "'" + ","
                            + "to_date('" + _GETTIME + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + Double.parseDouble(_datal.inoptical.toString()) + ","
                            + Double.parseDouble(_datal.outoptical.toString()) + ","
                            + Double.parseDouble(_datal.notavailable.toString()) + ","
                            + Double.parseDouble(_datal.backerror.toString()) + ","
                            + Double.parseDouble(_datal.str.toString()) + ","
                            + Double.parseDouble(_datal.severely.toString()) + ""
                            + ")";
                    _csnms.execute(str_sql, null);

                } else if (type_in_up.equals("up")) {
                    String str_sql = "update JSTRANSPATHPERF set "
                            + "INOPTICAL=" + _datal.inoptical.toString() + ","
                            + "OUTOPTICAL=" + _datal.outoptical.toString() + ","
                            + "NOTAVAILABLE=" + _datal.notavailable.toString() + ","
                            + "BACKERROR=" + _datal.backerror.toString() + ","
                            + "STR=" + _datal.str.toString() + ","
                            + "severely=" + _datal.severely.toString() + ""
                            + " where   pathid=" + _datal.path_id.toString() + ""
                            + " and  gettime=to_date('" + _date1.getTime() + "','yyyy-mm-dd hh24:mi:ss')";
                    _csnms.execute(str_sql, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("异常：" + e.getMessage().toString());
        }
    }

    public static synchronized void tb_in_up_JSPERFERROR(String type_in_up, List _data, util.GetSql.csnms _csnms, org.apache.log4j.Logger log) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                strclass.Jstranspathperf_detail _datal = new strclass.Jstranspathperf_detail();
                _datal = (strclass.Jstranspathperf_detail) _data.get(i);
                count_s = count_s + 1;
                //-------------处理时间---------------//
                String _GETTIME = _datal.gettime;
                SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _date1 = new Date();
                _date1 = sdf_temp.parse(_GETTIME);
                //------------------------------------//

                if (type_in_up.equals("in")) {
                    //过滤
                    String key = _datal.gettime + "#" + _datal.path_id + "#" + _datal.slottypeid + "#" + _datal.severely;
                    if (!keyHash4.containsKey(key)) {
                        keyHash4.put(key, key);
                    } else {
                        continue;
                    }
                    String str_sql = "insert   into  JSPERFERROR("
                            + "GETTIME,"
                            + "PATHID,"
                            + "PERFID,"
                            + "PERFVALUE"
                            + ") values ("
                            + "to_date('" + _date1.getTime() + "','yyyy-mm-dd hh24:mi:ss'),"
                            + Long.parseLong(_datal.path_id.toString()) + ","
                            + Double.parseDouble(_datal.slottypeid.toString()) + ","
                            + Double.parseDouble(_datal.severely.toString())
                            + ")";
                    _csnms.execute(str_sql, null);
                } else if (type_in_up.equals("up")) {
                    String str_sql = "update JSPERFERROR set "
                            + " PERFID=" + Double.parseDouble(_datal.slottypeid) + ","
                            + " PERFVALUE=" + Double.parseDouble(_datal.severely) + ""
                            + " where   pathid=" + Long.parseLong(_datal.path_id) + " "
                            + " and  gettime=to_date('" + _date1.getTime() + "','yyyy-mm-dd hh24:mi:ss')";
                    _csnms.execute(str_sql, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("异常：" + e.getMessage().toString());
            //log.info("构造预处理失败");
        }
    }

    public static void tb_in_up_pathperf(String type_in_up, List _data, util.GetSql.csnms _csnms, org.apache.log4j.Logger log) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                strclass.Jstranspathperf_detail _datal = new strclass.Jstranspathperf_detail();
                _datal = (strclass.Jstranspathperf_detail) _data.get(i);
                count_s = count_s + 1;
                if (type_in_up.equals("in")) {

                } else if (type_in_up.equals("up")) {
                    if (_datal.path_id.length() == 0) {
                        continue;
                    }
                    String str_sql = "update pathperf set "
                            + " INOPTICAL=" + Double.parseDouble(_datal.inoptical) + ","
                            + " OUTOPTICAL=" + Double.parseDouble(_datal.outoptical) + ","
                            + " NOTAVAILABLE=" + Double.parseDouble(_datal.notavailable) + ","
                            + " BACKERROR=" + Double.parseDouble(_datal.backerror) + ","
                            + " STR=" + Double.parseDouble(_datal.str) + ""
                            + " where PATHID=" + _datal.path_id;
                    _csnms.execute(str_sql, null);
                } else if (type_in_up.equals("del")) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("异常：" + e.getMessage().toString());
            //log.info("构造预处理失败");
        }
    }

}
