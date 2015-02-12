package z.zy_atm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class db {

    private static util.GetTools.tools _datas = new util.GetTools.tools();

    //--------------------------------加载数据库------------------------------------------
    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql) {
        boolean _bs = false;
        boolean _bs1 = false;
        boolean _bs2 = false;

        if (_mysql.load()) {
            if (_mysql.open()) {
                _bs1 = true;
            }
        }

        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs2 = true;
            }
        }

        if (_bs1) {
            if (_bs2) {
                _bs = true;
            }
        }

        return _bs;
    }

    //------------------------------获取mysql上的path数据--------------------------------------------
    public static List get_xn_atm(util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql ,String filename) throws IOException {

        List _data_list = new ArrayList();
        List _data = new ArrayList();

        String str_sql = "select p.`name` AS `name`,p.network_id AS network_id,p.path_id AS path_id,p.serviceType "
                + "AS servicetype,p.connectStatus as connectstatus,p.type_str as type_str,p.ems_type as ems_type,"
                + "p.aEndVpi as aendvpi,p.aEndVci as aendvci,p.zEndVpi as zendvpi,p.zEndVci as zendvci,p.atmServiceType "
                + "as atmservicetype,p.aEndNode as aendnode,p.aEndSlot as aendslot,p.aEndSlot as aendslot ,"
                + "p.zEndNode as zendnode,p.zEndSlot as zendslot ,p.zEndPort as zendport,p.pathLevel as pathLevel ,"
                + "p.maxHdlc as maxhdlc,p.tbwidth as tbwidth,p.aAtmIf as aatmif,p.zAtmIf as zatmif,p.sync_result as "
                + "sync_result,p.sync_result_info as sync_result_info,p.sync_time as sync_time ,p.aEndPort as aendport "
                + "from  path  p;";
        try {
            fun.xieru(str_sql, filename);
            _data = _mysql.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_data.size() > 0) {
            for (int i = 0, m = _data.size(); i < m; i++) {
                // System.out.println("进度：" + (i + 1) + "/" + m);
                fun.mysql_path _path = new fun.mysql_path();
                //_xn = new fun._SWITCH_REPORT_OUT();
                HashMap map = (HashMap) _data.get(i);//_path.name=map.get(i).toString();
                //System.out.println(_data.get(i) + "-----------------------------------------");
                _path.name = map.get("name").toString();
                _path.network_id = Integer.parseInt(map.get("network_id").toString());
                _path.path_id = Long.parseLong(map.get("path_id").toString());

                if (map.get("servicetype") != null) {
                    _path.servicetype = Integer.parseInt(map.get("servicetype").toString());
                    //System.out.println(_path.servicetype + "////////////////////////////////////////////////////");
                } else {
                    _path.servicetype = 0;
                }
                if (map.get("connectstatus") != null) {
                    _path.connectstatus = Integer.parseInt(map.get("connectstatus").toString());
                } else {
                    _path.connectstatus = 0;
                }
                if (map.get("bandwidth") != null) {
                    _path.bandwidth = Integer.parseInt(map.get("bandwidth").toString());
                } else {
                    _path.bandwidth = 0;
                }
                if (map.get("isfree") != null) {
                    _path.isfree = Integer.parseInt(map.get("isfree").toString());
                } else {
                    _path.isfree = 0;
                }
                if (map.get("type_str") != null) {
                    _path.type_str = map.get("type_str").toString();
                } else {
                    _path.type_str = "";
                }
                if (map.get("acustomer_id") != null) {
                    _path.acustomer_id = Integer.parseInt(map.get("acustomer_id").toString());
                }
                if (map.get("agroup_id") != null) {
                    _path.agroup_id = Integer.parseInt(map.get("agroup_id").toString());
                } else {
                    _path.agroup_id = 0;
                }
                if (map.get("zgroup_id") != null) {
                    _path.zgroup_id = Integer.parseInt(map.get("zgroup_id").toString());
                } else {
                    _path.zgroup_id = 0;
                }
                if (map.get("zcustomer_id") != null) {
                    _path.zcustomer_id = Integer.parseInt(map.get("zcustomer_id").toString());
                } else {
                    _path.zcustomer_id = 0;
                }
                if (map.get("slacontractid") != null) {
                    _path.slacontractid = Integer.parseInt(map.get("slacontractid").toString());
                } else {
                    _path.slacontractid = 0;
                }
                if (map.get("parentid") != null) {
                    _path.parentid = Integer.parseInt(map.get("parentid").toString());
                } else {
                    _path.parentid = 0;
                }
                if (map.get("serial") != null) {
                    _path.serial = Long.parseLong(map.get("serial").toString());
                } else {
                    _path.serial = 0;
                }
                if (map.get("serial") != null) {
                    _path.ems_type = Long.parseLong(map.get("serial").toString());
                } else {
                    _path.ems_type = 0;
                }
                if (map.get("aendvpi") != null) {
                    _path.aendvpi = Integer.parseInt(map.get("aendvpi").toString());
                } else {
                    _path.aendvpi = 0;
                }
                if (map.get("aendvci") != null) {
                    _path.aendvci = Integer.parseInt(map.get("aendvci").toString());
                } else {
                    _path.aendvci = 0;
                }
                if (map.get("aenddlci") != null) {
                    _path.aenddlci = Integer.parseInt(map.get("aenddlci").toString());
                } else {
                    _path.aenddlci = 0;
                }
                if (map.get("zendvpi") != null) {
                    _path.zendvpi = Integer.parseInt(map.get("zendvpi").toString());
                } else {
                    _path.zendvpi = 0;
                }
                if (map.get("zendvci") != null) {
                    _path.zendvci = Integer.parseInt(map.get("zendvci").toString());
                } else {
                    _path.zendvci = 0;
                }
                if (map.get("zenddlci") != null) {
                    _path.zenddlci = Integer.parseInt(map.get("zenddlci").toString());
                } else {
                    _path.zenddlci = 0;
                }
                if (map.get("pcr") != null) {
                    _path.pcr = Integer.parseInt(map.get("pcr").toString());
                } else {
                    _path.pcr = 0;
                }
                if (map.get("mbs") != null) {
                    _path.mbs = Integer.parseInt(map.get("mbs").toString());
                } else {
                    _path.mbs = 0;
                }
                if (map.get("cdvt") != null) {
                    _path.cdvt = Integer.parseInt(map.get("cdvt").toString());
                } else {
                    _path.cdvt = 0;
                }
                if (map.get("rsr") != null) {
                    _path.rsr = Integer.parseInt(map.get("rsr").toString());
                } else {
                    _path.rsr = 0;
                }
                if (map.get("atmservicetype") != null) {
                    _path.atmservicetype = map.get("atmservicetype").toString();
                } else {
                    _path.atmservicetype = "";
                }
                if (map.get("aendnode") != null) {
                    _path.aendnode = Integer.parseInt(map.get("aendnode").toString());
                } else {
                    _path.aendnode = 0;
                }
                if (map.get("aendshelf") != null) {
                    _path.aendshelf = Integer.parseInt(map.get("aendshelf").toString());
                } else {
                    _path.aendshelf = 0;
                }
                if (map.get("aendslot") != null) {
                    _path.aendslot = Integer.parseInt(map.get("aendslot").toString());
                } else {
                    _path.aendslot = 0;
                }
                if (map.get("aendport") != null) {
                    _path.aendport = Integer.parseInt(map.get("aendport").toString());
                } else {
                    _path.aendport = 0;
                }
                if (map.get("aendtimeslot") != null) {
                    _path.aendtimeslot = Integer.parseInt(map.get("aendtimeslot").toString());
                } else {
                    _path.aendtimeslot = 0;
                }
                if (map.get("zendnode") != null) {
                    _path.zendnode = Integer.parseInt(map.get("zendnode").toString());
                } else {
                    _path.zendnode = 0;
                }
                if (map.get("zendshelf") != null) {
                    _path.zendshelf = Integer.parseInt(map.get("zendshelf").toString());
                } else {
                    _path.zendshelf = 0;
                }
                if (map.get("zendslot") != null) {
                    _path.zendslot = Integer.parseInt(map.get("zendslot").toString());
                } else {
                    _path.zendslot = 0;
                }
                if (map.get("zendport") != null) {
                    _path.zendport = Integer.parseInt(map.get("zendport").toString());
                } else {
                    _path.zendport = 0;
                }
                if (map.get("zendtimeslot") != null) {
                    _path.zendtimeslot = Integer.parseInt(map.get("zendtimeslot").toString());
                } else {
                    _path.zendtimeslot = 0;
                }
                if (map.get("owner_id") != null) {
                    _path.owner_id = Integer.parseInt(map.get("owner_id").toString());
                } else {
                    _path.owner_id = 0;
                }
                if (map.get("ownergroup_id") != null) {
                    _path.ownergroup_id = Integer.parseInt(map.get("ownergroup_id").toString());
                } else {
                    _path.ownergroup_id = 0;
                }
                if (map.get("ownerheadoffice_id") != null) {
                    _path.ownerheadoffice_id = Integer.parseInt(map.get("ownerheadoffice_id").toString());
                } else {
                    _path.ownerheadoffice_id = 0;
                }
                if (map.get("aendinput") != null) {
                    _path.aendinput = map.get("aendinput").toString();
                } else {
                    _path.aendinput = "";
                }
                if (map.get("zendinput") != null) {
                    _path.zendinput = map.get("zendinput").toString();
                } else {
                    _path.zendinput = "";
                }
                if (map.get("aendalcatel") != null) {
                    _path.aendalcatel = map.get("aendalcatel").toString();
                } else {
                    _path.aendalcatel = "";
                }
                if (map.get("zendalcatel") != null) {
                    _path.zendalcatel = map.get("zendalcatel").toString();
                } else {
                    _path.zendalcatel = "";
                }
                if (map.get("aendlineno") != null) {
                    _path.aendlineno = map.get("aendlineno").toString();
                } else {
                    _path.aendlineno = "";
                }
                if (map.get("zendlineno") != null) {
                    _path.zendlineno = map.get("zendlineno").toString();
                } else {
                    _path.zendlineno = "";
                }
                if (map.get("manageno") != null) {
                    _path.manageno = map.get("manageno").toString();
                } else {
                    _path.manageno = "";
                }
                if (map.get("pathlevel") != null) {
                    _path.pathlevel = map.get("pathlevel").toString();
                } else {
                    _path.pathlevel = "N";
                }
                if (map.get("priority") != null) {
                    _path.priority = Integer.parseInt(map.get("priority").toString());
                } else {
                    _path.priority = 0;
                }
                if (map.get("recovery") != null) {
                    _path.recovery = Integer.parseInt(map.get("recovery").toString());
                } else {
                    _path.recovery = 0;
                }
                if (map.get("ismultiple") != null) {
                    _path.ismultiple = Integer.parseInt(map.get("ismultiple").toString());
                } else {
                    _path.ismultiple = 0;
                }
                if (map.get("subrate") != null) {
                    _path.subrate = Integer.parseInt(map.get("subrate").toString());
                } else {
                    _path.subrate = 0;
                }
                if (map.get("rate") != null) {
                    _path.rate = Integer.parseInt(map.get("rate").toString());
                } else {
                    _path.rate = 0;
                }
                if (map.get("speed") != null) {
                    _path.speed = Integer.parseInt(map.get("speed").toString());
                } else {
                    _path.speed = 0;
                }
                if (map.get("dataoctets") != null) {
                    _path.dataoctets = Integer.parseInt(map.get("dataoctets").toString());
                } else {
                    _path.dataoctets = 0;
                }
                if (map.get("buffertime") != null) {
                    _path.buffertime = Integer.parseInt(map.get("buffertime").toString());
                } else {
                    _path.buffertime = 0;
                }
                if (map.get("hdlc") != null) {
                    _path.hdlc = Integer.parseInt(map.get("hdlc").toString());
                } else {
                    _path.hdlc = 0;
                }
                if (map.get("maxhdlc") != null) {
                    _path.maxhdlc = Integer.parseInt(map.get("maxhdlc").toString());
                } else {
                    _path.maxhdlc = 0;
                }
                if (map.get("tbwidth") != null) {
                    _path.tbwidth = Integer.parseInt(map.get("tbwidth").toString());
                } else {
                    _path.tbwidth = 0;
                }
                if (map.get("bumping") != null) {
                    _path.bumping = Integer.parseInt(map.get("bumping").toString());
                } else {
                    _path.bumping = 0;
                }
                if (map.get("longdistance") != null) {
                    _path.longdistance = Integer.parseInt(map.get("longdistance").toString());
                } else {
                    _path.longdistance = 0;
                }
                if (map.get("filterid") != null) {
                    _path.filterid = Integer.parseInt(map.get("filterid").toString());
                } else {
                    _path.filterid = 0;
                }
                if (map.get("scr") != null) {
                    _path.scr = Integer.parseInt(map.get("scr").toString());
                } else {
                    _path.scr = 0;
                }
                if (map.get("isloop") != null) {
                    _path.isloop = Integer.parseInt(map.get("isloop").toString());
                } else {
                    _path.isloop = 0;
                }
                if (map.get("code") != null) {
                    _path.code = map.get("code").toString();
                } else {
                    _path.code = "";
                }
                if (map.get("nmpathno") != null) {
                    _path.nmpathno = map.get("nmpathno").toString();
                } else {
                    _path.nmpathno = "";
                }
                if (map.get("customeridinnm") != null) {
                    _path.customeridinnm = Integer.parseInt(map.get("customeridinnm").toString());
                } else {
                    _path.customeridinnm = 0;
                }
                if (map.get("vendor") != null) {
                    _path.vendor = map.get("vendor").toString();
                } else {
                    _path.vendor = "";
                }
                if (map.get("aendname") != null) {
                    _path.aendname = map.get("aendname").toString();
                } else {
                    _path.aendname = "";
                }
                if (map.get("zendname") != null) {
                    _path.zendname = map.get("zendname").toString();
                } else {
                    _path.zendname = "";
                }
                if (map.get("datasource") != null) {
                    _path.datasource = map.get("datasource").toString();
                } else {
                    _path.datasource = "";
                }
                if (map.get("nmsync") != null) {
                    _path.nmsync = Integer.parseInt(map.get("nmsync").toString());
                } else {
                    _path.nmsync = 0;
                }
                if (map.get("aendaddressabbr") != null) {
                    _path.aendaddressabbr = map.get("aendaddressabbr").toString();
                } else {
                    _path.aendaddressabbr = "";
                }
                if (map.get("aendcontactor") != null) {
                    _path.aendcontactor = map.get("aendcontactor").toString();
                } else {
                    _path.aendcontactor = "";
                }
                if (map.get("aendtel") != null) {
                    _path.aendtel = map.get("aendtel").toString();
                } else {
                    _path.aendtel = "";
                }
                if (map.get("zendaddressabbr") != null) {
                    _path.zendaddressabbr = map.get("zendaddressabbr").toString();
                } else {
                    _path.zendaddressabbr = "";
                }
                if (map.get("zendcontactor") != null) {
                    _path.zendcontactor = map.get("zendcontactor").toString();
                } else {
                    _path.zendcontactor = "";
                }
                if (map.get("zendtel") != null) {
                    _path.zendtel = map.get("zendtel").toString();
                } else {
                    _path.zendtel = "";
                }
                if (map.get("aendportinnm") != null) {
                    _path.aendportinnm = Integer.parseInt(map.get("aendportinnm").toString());
                } else {
                    _path.aendportinnm = 0;
                }
                if (map.get("zendportinnm") != null) {
                    _path.zendportinnm = Integer.parseInt(map.get("zendportinnm").toString());
                } else {
                    _path.zendportinnm = 0;
                }
                if (map.get("idinnm") != null) {
                    _path.idinnm = Integer.parseInt(map.get("idinnm").toString());
                } else {
                    _path.idinnm = 0;
                }
                if (map.get("description") != null) {
                    _path.description = map.get("description").toString();
                } else {
                    _path.description = "";
                }
                if (map.get("starttime") != null) {
                    _path.starttime = map.get("starttime").toString();
                } else {
                    _path.starttime = "";
                }
                if (map.get("endtime") != null) {
                    _path.endtime = map.get("endtime").toString();
                } else {
                    _path.endtime = "";
                }
                if (map.get("isimportance") != null) {
                    _path.isimportance = map.get("isimportance").toString();
                } else {
                    _path.isimportance = "";
                }
                if (map.get("aendpathid") != null) {
                    _path.aendpathid = Integer.parseInt(map.get("aendpathid").toString());
                } else {
                    _path.aendpathid = 0;
                }
                if (map.get("zendpathid") != null) {
                    _path.zendpathid = Integer.parseInt(map.get("zendpathid").toString());
                } else {
                    _path.zendpathid = 0;
                }
                if (map.get("backuppathid") != null) {
                    _path.backuppathid = Integer.parseInt(map.get("backuppathid").toString());
                } else {
                    _path.backuppathid = 0;
                }
                if (map.get("remarksite") != null) {
                    _path.remarksite = map.get("remarksite").toString();
                } else {
                    _path.remarksite = "";
                }
                if (map.get("aatmif") != null) {
                    _path.aatmif = map.get("aatmif").toString();
                } else {
                    _path.aatmif = "";
                }
                if (map.get("zatmif") != null) {
                    _path.zatmif = map.get("zatmif").toString();
                } else {
                    _path.zatmif = "";
                }
                if (map.get("sync_result") != null) {
                    _path.sync_result = Integer.parseInt(map.get("sync_result").toString());
                } else {
                    _path.sync_result = 0;
                }
                if (map.get("sync_result_info") != null) {
                    _path.sync_result_info = map.get("sync_result_info").toString();
                } else {
                    _path.sync_result_info = "";
                }
                if (map.get("sync_time") != null) {
                    _path.sync_time = map.get("sync_time").toString();
                } else {
                    _path.sync_time = "";
                }
                if (map.get("sortid") != null) {
                    _path.sortid = Integer.parseInt(map.get("sortid").toString());
                } else {
                    _path.sortid = 0;
                }
                if (map.get("main_circuit_terminal") != null) {
                    _path.main_circuit_terminal = Integer.parseInt(map.get("main_circuit_terminal").toString());
                } else {
                    _path.main_circuit_terminal = 0;
                }
                if (map.get("path_alias") != null) {
                    _path.path_alias = map.get("path_alias").toString();
                } else {
                    _path.path_alias = "";
                }
                fun.xieru(_path.toString(),filename);
                _data_list.add(_path);
            }
        }
        return _data_list;
    }

    //------------------------------添加oracle上的path数据--------------------------------------------
    public static void tb_in_up(util.GetSql.csnms _csnms, List _data,String filename) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                fun.mysql_path _datal = new fun.mysql_path();
                _datal = (fun.mysql_path) _data.get(i);
                count_s = count_s + 1;
                String str_sql = "insert into path2(name, network_id, path_id,  aEndPort) values "
                        + "('" + _datal.name + "'," + _datal.network_id + "," + _datal.path_id + ","
                        + _datal.aendport + ")";//5
                fun.xieru(str_sql, filename);
                int execute = _csnms.execute(str_sql, null);
                if(execute>0){
                fun.xieru("--------------插入成功---------------------", filename);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    //------------------------------更新oracle上的path数据--------------------------------------------
    public static void update_path(util.GetSql.csnms _csnms, List _data,String filename) {
        //构造预处理
        try {
            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                fun.mysql_path _datal = new fun.mysql_path();
                _datal = (fun.mysql_path) _data.get(i);
                count_s = count_s + 1;
                String str_sql = "update path2 set "
                        + "servicetype=" + _datal.servicetype + ","
                        + "connectstatus=" + _datal.connectstatus + ","
                        + "bandwidth=" + _datal.bandwidth + ","
                        + "isfree=" + _datal.isfree + ","
                        + "type_str='" + _datal.type_str + "',"
                        + "acustomer_id =" + _datal.acustomer_id + ","
                        + "agroup_id=" + _datal.agroup_id + ","
                        + "zgroup_id=" + _datal.zgroup_id + ","
                        + "zcustomer_id =" + _datal.zcustomer_id + ","
                        + "slacontractid =" + _datal.slacontractid + ","
                        + "parentid=" + _datal.parentid + ","
                        + "serial=" + _datal.serial + ","
                        + "ems_type=" + _datal.ems_type + ","
                        + "aendvpi=" + _datal.aendvpi + ","
                        + "aendvci=" + _datal.aendvci + ","
                        + "aenddlci=" + _datal.aenddlci + ","
                        + "zendvpi=" + _datal.zendvpi + ","
                        + "zendvci=" + _datal.zendvci + ","
                        + "zenddlci=" + _datal.zenddlci + ","
                        + "pcr=" + _datal.pcr + ","
                        + "mbs=" + _datal.mbs + ","
                        + "cdvt=" + _datal.cdvt + ","
                        + "rsr=" + _datal.rsr + ","
                        + "atmservicetype='" + _datal.atmservicetype + "',"
                        + "aendnode=" + _datal.aendnode + ","
                        + "aendshelf=" + _datal.aendshelf + ","
                        + "aendslot=" + _datal.aendslot + ","
                        + "aendport=" + _datal.aendport + ","
                        + "aendtimeslot=" + _datal.aendtimeslot + ","
                        + "zendnode=" + _datal.zendnode + ","
                        + "zendshelf=" + _datal.zendshelf + ","
                        + "zendslot=" + _datal.zendslot + ","
                        + "zendport=" + _datal.zendport + ","
                        + "zendtimeslot=" + _datal.zendtimeslot + ","
                        + "owner_id =" + _datal.owner_id + ","
                        + "ownergroup_id =" + _datal.ownergroup_id + ","
                        + "ownerheadoffice_id=" + _datal.ownerheadoffice_id + ","
                        + "aendinput='" + _datal.aendinput + "',"
                        + "zendinput='" + _datal.zendinput + "',"
                        + "aendalcatel='" + _datal.aendalcatel + "',"
                        + "zendalcatel='" + _datal.zendalcatel + "',"
                        + "aendlineno='" + _datal.aendlineno + "',"
                        + "zendlineno='" + _datal.zendlineno + "',"
                        + "manageno='" + _datal.manageno + "',"
                        + "pathlevel='" + _datal.pathlevel + "',"
                        + "priority=" + _datal.priority + ","
                        + "recovery =" + _datal.recovery + ","
                        + "ismultiple =" + _datal.ismultiple + ","
                        + "subrate=" + _datal.subrate + ","
                        + "rate =" + _datal.rate + ","
                        + "speed=" + _datal.speed + ","
                        + "dataoctets=" + _datal.dataoctets + ","
                        + "buffertime=" + _datal.buffertime + ","
                        + "hdlc =" + _datal.hdlc + ","
                        + "maxhdlc=" + _datal.maxhdlc + ","
                        + "tbwidth =" + _datal.tbwidth + ","
                        + "bumping =" + _datal.bumping + ","
                        + "longdistance=" + _datal.longdistance + ","
                        + "filterid=" + _datal.filterid + ","
                        + "scr=" + _datal.scr + ","
                        + "isloop=" + _datal.isloop + ","
                        + "code='" + _datal.code + "',"
                        + "nmpathno='" + _datal.nmpathno + "',"
                        + "customeridinnm =" + _datal.customeridinnm + ","
                        + "vendor='" + _datal.vendor + "',"
                        + "aendname='" + _datal.aendname + "',"
                        + "zendname='" + _datal.zendname + "',"
                        + "datasource='" + _datal.datasource + "',"
                        + "nmsync=" + _datal.nmsync + ","
                        + "aendaddressabbr='" + _datal.aendaddressabbr + "',"
                        + "aendcontactor='" + _datal.aendcontactor + "',"
                        + "aendtel ='" + _datal.aendtel + "',"
                        + "zendaddressabbr='" + _datal.zendaddressabbr + "',"
                        + "zendcontactor='" + _datal.zendcontactor + "',"
                        + "zendtel='" + _datal.zendtel + "',"
                        + "aendportinnm=" + _datal.aendportinnm + ","
                        + "zendportinnm=" + _datal.zendportinnm + ","
                        + "idinnm=" + _datal.idinnm + ","
                        + "description ='" + _datal.description + "',"
                        + "starttime='" + _datal.starttime + "',"
                        + "endtime='" + _datal.endtime + "',"
                        + "isimportance='" + _datal.isimportance + "',"
                        + "aendpathid=" + _datal.aendpathid + ","
                        + "zendpathid=" + _datal.zendpathid + ","
                        + "backuppathid=" + _datal.backuppathid + ","
                        + "remarksite='" + _datal.remarksite + "',"
                        + "aatmif='" + _datal.aatmif + "',"
                        + "zatmif='" + _datal.zatmif + "',"
                        + "sync_result=" + _datal.sync_result + ","
                        + "sync_result_info='" + _datal.sync_result_info + "',"
                        + "sync_time='" + _datal.sync_time + "',"
                        + "sortid=" + _datal.sortid + ","
                        + "main_circuit_terminal=" + _datal.main_circuit_terminal + ","
                        + "path_alias='" + _datal.path_alias + "'where path_id=" + _datal.path_id;//5
                fun.xieru(str_sql, filename);
                int execute = _csnms.execute(str_sql, null);
                if (execute!=0) {
                   fun.xieru("------------更新成功！！！------------------",filename);
                }else{
                    fun.xieru("------------更新失败。。。-----------------",filename);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
