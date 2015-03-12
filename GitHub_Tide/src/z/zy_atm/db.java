package z.zy_atm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据库的操作
 * ini----------------------------------------------------------------加载数据库
 * _pathdata----------------------------------------------------------获取mysql上的path数据
 * _pathoracle--------------------------------------------------------数据库中查找是否有此条数据
 * tb_in_up-----------------------------------------------------------添加oracle上的path数据
 * update_path--------------------------------------------------------更新oracle上的path数据
 *
 * @author Liujintai
 */
public class db {

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
    public static List _pathdata(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql) throws IOException {

        List _data_list = new ArrayList();
        List _data = new ArrayList();

        String str_sql = "select p.`name` AS `name`,p.network_id AS network_id,p.path_id AS path_id,p.serviceType "
                + "AS servicetype,p.connectStatus as connectstatus,p.type_str as type_str,p.ems_type as ems_type,"
                + "p.aEndVpi as aendvpi,p.aEndVci as aendvci,p.zEndVpi as zendvpi,p.zEndVci as zendvci,p.atmServiceType "
                + "as atmservicetype,p.aEndNode as aendnode,p.aEndSlot as aendslot,p.aEndSlot as aendslot ,"
                + "p.zEndNode as zendnode,p.zEndSlot as zendslot ,p.zEndPort as zendport,p.pathLevel as pathLevel ,"
                + "p.maxHdlc as maxhdlc,p.tbwidth as tbwidth,p.aAtmIf as aatmif,p.zAtmIf as zatmif,p.sync_result as "
                + "sync_result,p.sync_result_info as sync_result_info,p.sync_time as sync_time ,p.aEndPort as aendport "
                + "from  path  p order by path_id desc;";
        try {
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
                if (map.get("ems_type") != null) {
                    _path.ems_type = Long.parseLong(map.get("ems_type").toString());
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
                    _path.zendnode = -1;
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
                //fun.xieru(_path.toString());
                _data_list.add(_path);
            }
        }
        return _data_list;
    }

    //--------------------------------数据库中查找是否有此条数据-----------------------------------------
    public static boolean _pathoracle(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql, long path) {
        List _data = new ArrayList();
        boolean result = false;
        String _strsql = "select p.path_id from path p where p.path_id=" + path;
        try {
            //fun.xieru(_strsql, filename);
            _data = _csnms.getdata(_strsql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (_data.size() > 0) {
            result = true;
        }
        return result;
    }

    //------------------------------添加oracle上的path数据--------------------------------------------
    public static int intsert_oraclepath(util.GetSql.csnms _csnms, List path) {
        int execute = 0;
        //构造预处理
        try {
            String str_sql = "insert into path (name, network_id, path_id,  aEndPort) values "
                    + "('" + path.get(0).toString() + "'," + Long.parseLong(path.get(1).toString()) + "," + Long.parseLong(path.get(2).toString()) + ","
                    + Long.parseLong(path.get(3).toString()) + ")";//5
            execute = _csnms.execute(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }

    //------------------------------更新oracle上的path数据--------------------------------------------
    public static int update_path(util.GetSql.csnms _csnms, fun.mysql_path _datal) {
        int execute = 0;
        //构造预处理
        try {
            String str_sql = "update path set "
                    + "network_id=" + _datal.network_id + ","
                    + "servicetype=" + _datal.servicetype + ","
                    + "connectstatus=" + _datal.connectstatus + ","
                    + "bandwidth=" + _datal.bandwidth + ","
                    + "isfree=" + _datal.isfree + ","
                    + "type_str='" + _datal.type_str + "',"
                    + "acustomer_id =" + _datal.acustomer_id + ","
                    + "agroup_id=" + _datal.zgroup_id + ","
                    + "zgroup_id=" + _datal.agroup_id + ","
                    + "zcustomer_id =" + _datal.acustomer_id + ","
                    + "slacontractid =" + _datal.slacontractid + ","
                    + "parentid=" + _datal.parentid + ","
                    + "serial=" + _datal.serial + ","
                    + "ems_type=" + _datal.ems_type + ","
                    + "aendvpi=" + _datal.zendvpi + ","
                    + "aendvci=" + _datal.zendvci + ","
                    + "aenddlci=" + _datal.zenddlci + ","
                    + "zendvpi=" + _datal.aendvpi + ","
                    + "zendvci=" + _datal.aendvci + ","
                    + "zenddlci=" + _datal.aenddlci + ","
                    + "pcr=" + _datal.pcr + ","
                    + "mbs=" + _datal.mbs + ","
                    + "cdvt=" + _datal.cdvt + ","
                    + "rsr=" + _datal.rsr + ","
                    + "atmservicetype='" + _datal.atmservicetype + "',"
                    + "aendshelf=" + _datal.zendshelf + ","
                    + "aendslot=" + _datal.zendslot + ","
                    + "aendport=" + _datal.zendport + ","
                    + "aendtimeslot=" + _datal.zendtimeslot + ","
                    + "zendshelf=" + _datal.aendshelf + ","
                    + "zendslot=" + _datal.aendslot + ","
                    + "zendport=" + _datal.aendport + ","
                    + "zendtimeslot=" + _datal.aendtimeslot + ","
                    + "owner_id =" + _datal.owner_id + ","
                    + "ownergroup_id =" + _datal.ownergroup_id + ","
                    + "ownerheadoffice_id=" + _datal.ownerheadoffice_id + ","
                    + "aendinput='" + _datal.zendinput + "',"
                    + "zendinput='" + _datal.aendinput + "',"
                    + "aendalcatel='" + _datal.zendalcatel + "',"
                    + "zendalcatel='" + _datal.aendalcatel + "',"
                    + "aendlineno='" + _datal.zendlineno + "',"
                    + "zendlineno='" + _datal.aendlineno + "',"
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
                    + "aendname='" + _datal.zendname + "',"
                    + "zendname='" + _datal.aendname + "',"
                    + "datasource='" + _datal.datasource + "',"
                    + "nmsync=" + _datal.nmsync + ","
                    + "aendaddressabbr='" + _datal.zendaddressabbr + "',"
                    + "aendcontactor='" + _datal.zendcontactor + "',"
                    + "aendtel ='" + _datal.zendtel + "',"
                    + "zendaddressabbr='" + _datal.aendaddressabbr + "',"
                    + "zendcontactor='" + _datal.aendcontactor + "',"
                    + "zendtel='" + _datal.aendtel + "',"
                    + "aendportinnm=" + _datal.zendportinnm + ","
                    + "zendportinnm=" + _datal.aendportinnm + ","
                    + "idinnm=" + _datal.idinnm + ","
                    + "description ='" + _datal.description + "',"
                    + "starttime='" + _datal.starttime + "',"
                    + "endtime='" + _datal.endtime + "',"
                    + "isimportance='" + _datal.isimportance + "',"
                    + "aendpathid=" + _datal.zendpathid + ","
                    + "zendpathid=" + _datal.aendpathid + ","
                    + "backuppathid=" + _datal.backuppathid + ","
                    + "remarksite='" + _datal.remarksite + "',"
                    + "aatmif='" + _datal.zatmif + "',"
                    + "zatmif='" + _datal.aatmif + "',"
                    + "sync_result=" + _datal.sync_result + ","
                    + "sync_result_info='" + _datal.sync_result_info + "',"
                    + "sync_time='" + _datal.sync_time + "',"
                    + "sortid=" + _datal.sortid + ","
                    + "main_circuit_terminal=" + _datal.main_circuit_terminal + ","
                    + "path_alias='" + _datal.path_alias + "',";

            String zn, an;
            if (_datal.zendnode == -1) {
                zn = "aendnode=" + _datal.zendnode + ",";
            } else {
                zn = "aendnode=" + _datal.zendnode + "0003,";
            }
            if (_datal.aendnode == -1) {
                an = "zendnode=" + _datal.aendnode + "";
            } else {
                an = "zendnode=" + _datal.aendnode + "0003";
            }
            str_sql = str_sql + zn + an + " where path_id=" + _datal.path_id;//5
            //System.out.println("******" + str_sql);
            execute = _csnms.execute(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }

    //------------------------------获取mysql上的node数据--------------------------------------------
    public static List _nodedata(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql) throws IOException {

        List _data_list = new ArrayList();
        List _data = new ArrayList();

        String str_sql = "select n.full_name as full_name,n.short_name as short_name,n.network_id as network_id,n.node_id as node_id,n.vendor_type as vendor_type,"
                + "n.type as type,n.type_str as type_str,n.`status` as status,n.admin_status as admin_status,n.inter_net_addr as inter_net_addr,"
                + "n.no_of_shelves as no_of_shelves,n.internal_addr as internal_addr,n.isSyncOK as isSyncOK,n.syncErrorInfo as syncErrorInfo,n.userAccount as userAccount,"
                + "n.userPassword as userPassword,n.nodeversion as nodeversion,n.cityid as cityid,n.areaid as areaid,n.siteid as siteid,"
                + "n.switch_shelves as switch_shelves,n.enablepwd as enablepwd,n.snmprpwd as snmprpwd,n.remark as remark,n.resid as resid,"
                + "n.factory as factory,n.RoomIDInNM as RoomIDInNM,n.CityIDInNM as CityIDInNM,n.Address as Address,n.NameInNM as NameInNM,"
                + "n.AddtionalInfo as AddtionalInfo,n.NmSync as NmSync,n.netLayer as netLayer,n.vendor as vendor,n.dataSource as dataSource from node n";
        try {
            _data = _mysql.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_data.size() > 0) {
            for (int i = 0, m = _data.size(); i < m; i++) {
                // System.out.println("进度：" + (i + 1) + "/" + m);
                strclass.node _node = new strclass.node();
                //_xn = new fun._SWITCH_REPORT_OUT();
                HashMap map = (HashMap) _data.get(i);//_path.name=map.get(i).toString();
                //System.out.println(_data.get(i) + "-----------------------------------------");
                _node.full_name = map.get("full_name").toString();
                _node.short_name = map.get("short_name").toString();
                if (map.get("network_id") != null) {
                    _node.network_id = Integer.parseInt(map.get("network_id").toString());

                } else {
                    _node.network_id = 0;
                }
                if (map.get("node_id") != null) {
                    _node.node_id = Integer.parseInt(map.get("node_id").toString());
                } else {
                    _node.node_id = 0;
                }
                if (map.get("vendor_type") != null) {
                    _node.vendor_type = Integer.parseInt(map.get("vendor_type").toString());
                } else {
                    _node.vendor_type = 0;
                }
                if (map.get("type") != null) {
                    _node.type = Integer.parseInt(map.get("type").toString());
                } else {
                    _node.type = 0;
                }
                if (map.get("status") != null) {
                    _node.status = Integer.parseInt(map.get("status").toString());
                } else {
                    _node.status = 0;
                }
                if (map.get("inter_net_addr") != null) {
                    _node.inter_net_addr = map.get("inter_net_addr").toString();
                } else {
                    _node.inter_net_addr = "";
                }
                if (map.get("no_of_shelves") != null) {
                    _node.no_of_shelves = Integer.parseInt(map.get("no_of_shelves").toString());
                } else {
                    _node.no_of_shelves = 0;
                }
                if (map.get("internal_addr") != null) {
                    _node.internal_addr = map.get("internal_addr").toString();
                } else {
                    _node.internal_addr = "";
                }
                if (map.get("useraccount") != null) {
                    _node.useraccount = map.get("useraccount").toString();
                } else {
                    _node.useraccount = "";
                }
                if (map.get("userpassword") != null) {
                    _node.userpassword = map.get("userpassword").toString();
                } else {
                    _node.userpassword = "";
                }
                if (map.get("nodeversion") != null) {
                    _node.nodeversion = map.get("nodeversion").toString();
                } else {
                    _node.nodeversion = "";
                }
                if (map.get("cityid") != null) {
                    _node.cityid = Integer.parseInt(map.get("cityid").toString());
                } else {
                    _node.cityid = 0;
                }
                _node.areaid = Integer.parseInt(map.get("areaid").toString());
                _node.siteid = Integer.parseInt(map.get("siteid").toString());
                _node.switch_shelves = Long.parseLong(map.get("switch_shelves").toString());
                _node.resid = Integer.parseInt(map.get("resid").toString());
                _node.factory = Integer.parseInt(map.get("factory").toString());
                if (map.get("syncerrorinfo") != null) {
                    _node.syncerrorinfo = map.get("syncerrorinfo").toString();
                } else {
                    _node.syncerrorinfo = "";
                }
                if (map.get("enablepwd") != null) {
                    _node.enablepwd = map.get("enablepwd").toString();
                } else {
                    _node.enablepwd = "";
                }
                if (map.get("snmprpwd") != null) {
                    _node.snmprpwd = map.get("snmprpwd").toString();
                } else {
                    _node.snmprpwd = "";
                }
                if (map.get("roomidinnm") != null) {
                    _node.roomidinnm = Integer.parseInt(map.get("roomidinnm").toString());
                } else {
                    _node.roomidinnm = 0;
                }
                if (map.get("cityidinnm") != null) {
                    _node.cityidinnm = Integer.parseInt(map.get("cityidinnm").toString());
                } else {
                    _node.cityidinnm = 0;
                }
                if (map.get("address") != null) {
                    _node.address = map.get("address").toString();
                } else {
                }
                if (map.get("nameinnm") != null) {
                    _node.nameinnm = map.get("nameinnm").toString();
                } else {
                }
                if (map.get("nmsync") != null) {
                    _node.nmsync = Integer.parseInt(map.get("nmsync").toString());
                } else {
                    _node.nmsync = 0;
                }
                if (map.get("netlayer") != null) {
                    _node.netlayer = map.get("netlayer").toString();
                } else {
                    _node.netlayer = "";
                }
                if (map.get("vendor") != null) {
                    _node.vendor = map.get("vendor").toString();
                } else {
                    _node.vendor = "";
                }
                if (map.get("datasource") != null) {
                    _node.datasource = map.get("datasource").toString();
                } else {
                    _node.datasource = "";
                }
                if (map.get("synlasttime") != null) {
                    _node.synlasttime = map.get("synlasttime").toString();
                } else {
                    _node.synlasttime = "";
                }
                if (map.get("type_str") != null) {
                    _node.type_str = map.get("type_str").toString();
                } else {
                    _node.type_str = "";
                }
                if (map.get("remark") != null) {
                    _node.remark = map.get("remark").toString();
                } else {
                }
                if (map.get("gisx") != null) {
                    _node.gisx = Integer.parseInt(map.get("gisx").toString());
                } else {
                    _node.gisx = 0;
                }
                if (map.get("gisy") != null) {
                    _node.gisy = Integer.parseInt(map.get("gisy").toString());
                } else {
                    _node.gisy = 0;
                }
                if (map.get("eventtypes") != null) {
                    _node.eventtypes = Integer.parseInt(map.get("eventtypes").toString());
                } else {
                    _node.eventtypes = 0;
                }
                if (map.get("stationid") != null) {
                    _node.stationid = Integer.parseInt(map.get("stationid").toString());
                } else {
                    _node.stationid = 0;
                }
                if (map.get("eventid") != null) {
                    _node.eventid = Integer.parseInt(map.get("eventid").toString());
                } else {
                    _node.eventid = 0;
                }
                if (map.get("addtionalinfo") != null) {
                    _node.addtionalinfo = map.get("addtionalinfo").toString();
                } else {
                    _node.addtionalinfo = "";
                }
                //fun.xieru(_path.toString());
                _data_list.add(_node);
            }
        }
        return _data_list;
    }

    public static boolean _nodeoracle(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql, strclass.node node) {
        List _data = new ArrayList();
        boolean result = false;
        String _strsql = "select * from node2 n where n.node_id=" + node.node_id + "0003" + " and n.vendor_type=" + node.vendor_type;
        try {
            //fun.xieru(_strsql, filename);
            _data = _csnms.getdata(_strsql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (_data.size() > 0) {
            result = true;
        }
        return result;
    }

    public static int update_node(util.GetSql.csnms _csnms, strclass.node _datal) {
        int execute = 0;
        //构造预处理
        try {
            String str_sql = "update node2 set "
                    + "full_name='" + _datal.full_name + "',"
                    + "short_name='" + _datal.short_name + "',"
                    + "network_id=" + _datal.network_id + ","
                    + "type=" + _datal.type + ","
                    + "status=" + _datal.status + ","
                    + "admin_status=" + _datal.admin_status + ","
                    + "inter_net_addr= '" + _datal.inter_net_addr + "',"
                    + "no_of_shelves=" + _datal.no_of_shelves + ","
                    + "internal_addr='" + _datal.internal_addr + "',"
                    + "issyncok=" + _datal.issyncok + ","
                    + "useraccount='" + _datal.useraccount + "',"
                    + "userpassword='" + _datal.userpassword + "',"
                    + "nodeversion='" + _datal.nodeversion + "',"
                    + "cityid=" + _datal.cityid + ","
                    + "areaid=" + _datal.areaid + ","
                    + "siteid=" + _datal.siteid + ","
                    + "switch_shelves=" + _datal.switch_shelves + ","
                    + "resid=" + _datal.resid + ","
                    + "factory=" + _datal.factory + ","
                    + "syncerrorinfo='" + _datal.syncerrorinfo + "',"
                    + "enablepwd='" + _datal.enablepwd + "',"
                    + "snmprpwd='" + _datal.snmprpwd + "',"
                    + "roomidinnm=" + _datal.roomidinnm + ","
                    + "cityidinnm=" + _datal.cityidinnm + ","
                    + "address=" + _datal.address + ","
                    + "nameinnm=" + _datal.nameinnm + ","
                    + "nmsync=" + _datal.nmsync + ","
                    + "netlayer='" + _datal.netlayer + "',"
                    + "vendor='" + _datal.vendor + "',"
                    + "datasource='" + _datal.datasource + "',"
                    + "synlasttime='" + _datal.synlasttime + "',"
                    + "type_str='" + _datal.type_str + "',"
                    + "remark=" + _datal.remark + ","
                    + "gisx=" + _datal.gisx + ","
                    + "gisy=" + _datal.gisy + ","
                    + "eventtypes=" + _datal.eventtypes + ","
                    + "stationid=" + _datal.stationid + ","
                    + "eventid=" + _datal.eventid + ","
                    + "addtionalinfo='" + _datal.addtionalinfo + "'"
                    + " where node_id=" + _datal.node_id + "0003" + " and vendor_type=" + _datal.vendor_type;
            execute = _csnms.execute(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return execute;
    }

    public static int intsert_oraclenode(util.GetSql.csnms _csnms, strclass.node n) {
        int result = 0;
        //构造预处理
        try {
            String str_sql = "INSERT INTO node2("
                    + "full_name,short_name, network_id, node_id,vendor_type,"
                    + "type, type_str, status, admin_status, inter_net_addr, "
                    + "no_of_shelves, internal_addr,issyncok, syncErrorInfo, userAccount,"
                    + " userPassword, nodeversion, cityid, areaid, siteid,"
                    + " switch_shelves, enablepwd, snmprpwd, remark, resid,"
                    + " factory, RoomIDInNM, CityIDInNM, Address, NameInNM, "
                    + "AddtionalInfo, NmSync, netLayer, vendor, dataSource) VALUES ('"
                    + n.full_name + "','" + n.short_name + "'," + n.network_id + "," + n.node_id + "0003" + "," + n.vendor_type + ","
                    + n.type + "0003" + ",'" + n.type_str + "'," + n.status + "," + n.admin_status + ",'" + n.inter_net_addr + "',"
                    + n.no_of_shelves + ",'" + n.inter_net_addr + "'," + n.issyncok + ",'" + n.syncerrorinfo + "','" + n.useraccount + "','"
                    + n.userpassword + "','" + n.nodeversion + "'," + n.cityid + "," + n.areaid + "," + n.siteid + ","
                    + n.switch_shelves + ",'" + n.enablepwd + "','" + n.snmprpwd + "'," + n.remark + "," + n.resid + ","
                    + n.factory + "," + n.roomidinnm + "," + n.cityidinnm + "," + n.address + "," + n.nameinnm + ",'"
                    + n.addtionalinfo + "'," + n.nmsync + ",'" + n.netlayer + "','" + n.vendor + "','" + n.datasource + "')";//5
            result = _csnms.execute(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List _pathtrca(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql) {
        List _data_list = new ArrayList();
        List res = new ArrayList();
        String Strsql = "select p.network_id as network_id,p.path_id as path_id,p.serial_id as serial_id,p.routeseq as routeseq,p.channelSeq as channelSeq,"
                + "              p.type as type,p.channelType as channelType,p.orentation as orentation, p.anode_id as anode_id,p.ashelf_id as ashelf_id,"
                + "              p.aslot_id as aslot_id,p.aport_id as aport_id,p.atimeslot_id as asatimeslot_id,p.znode_id as znode_id,p.zshelf_id as zshelf_id,"
                + "              p.zslot_id as zslot_id,p.zport_id as zport_id,p.ztimeslot_id as ztimeslot_id,p.adlci as adlci,p.avpi as avpi,"
                + "              p.avci as avci,p.zdlci as zdlci,p.zvpi as zvpi,p.zvci as zvci,p.loopid as loopid,"
                + "              p.aNeIdNM as aNeIdNM,p.aPortIdNM as aPortIdNM,p.aCtpIdNM as aCtpIdNM,p.zNeIdNM as zNeIdNM,p.zPortIdNM as zPortIdNM,"
                + "              p.zCtpIdNM as zCtpIdNM,p.subloopid as subloopid,p.aAtmif as aAtmif,p.zAtmif as zAtmif,p.anode_name as anode_name,"
                + "              p.znode_name as znode_name,p.trip_delay as trip_delay from pathtrace p";
        try {
            res = _mysql.getdata(Strsql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res.size() > 0) {
            for (int i = 0, l = res.size(); i < l; i++) {
                strclass.pathtrace pt = new strclass.pathtrace();
                HashMap map = (HashMap) res.get(i);
                if (map.get("network_id").toString() != null) {
                    pt.network_id = Long.parseLong(map.get("network_id").toString());
                } else {
                    pt.network_id = 0;
                }
                if (map.get("path_id") != null) {
                    pt.path_id = Long.parseLong(map.get("path_id").toString());
                } else {
                    pt.path_id = 0;
                }
                if (map.get("serial_id") != null) {
                    pt.serial_id = Long.parseLong(map.get("serial_id").toString());
                } else {
                    pt.serial_id = 0;
                }
                if (map.get("anode_id") != null) {
                    pt.anode_id = Long.parseLong(map.get("anode_id").toString()+"0003");//---------------------加0003
                } else {
                    pt.anode_id = 0;
                }
                if (map.get("ashelf_id") != null) {
                    pt.ashelf_id = Long.parseLong(map.get("ashelf_id").toString());
                } else {
                    pt.ashelf_id = 0;
                }
                if (map.get("aslot_id") != null) {
                    pt.aslot_id = Long.parseLong(map.get("aslot_id").toString());
                } else {
                    pt.aslot_id = 0;
                }
                if (map.get("aport_id") != null) {
                    pt.aport_id = Long.parseLong(map.get("aport_id").toString());
                } else {
                    pt.aport_id = 0;
                }
                if (map.get("atimeslot_id") != null) {
                    pt.atimeslot_id = Long.parseLong(map.get("atimeslot_id").toString());
                } else {
                    pt.atimeslot_id = -1;
                }
                if (map.get("znode_id") != null) {
                    pt.znode_id = Long.parseLong(map.get("znode_id").toString()+"0003");//------------------------------加0003
                } else {
                    pt.znode_id = 0;
                }
                if (map.get("zshelf_id") != null) {
                    pt.zshelf_id = Long.parseLong(map.get("zshelf_id").toString());
                } else {
                    pt.zshelf_id = 0;
                }
                if (map.get("zslot_id") != null) {
                    pt.zslot_id = Long.parseLong(map.get("zslot_id").toString());
                } else {
                    pt.zslot_id = 0;
                }
                if (map.get("zport_id") != null) {
                    pt.zport_id = Long.parseLong(map.get("zport_id").toString());
                } else {
                    pt.zport_id = 0;
                }
                if (map.get("ztimeslot_id") != null) {
                    pt.ztimeslot_id = Long.parseLong(map.get("ztimeslot_id").toString());
                } else {
                    pt.ztimeslot_id = -1;
                }
                if (map.get("adlci") != null) {
                    pt.adlci = Long.parseLong(map.get("adlci").toString());
                } else {
                    pt.adlci = -1;
                }
                if (map.get("avpi") != null) {
                    pt.avpi = Long.parseLong(map.get("avpi").toString());
                } else {
                    pt.avpi = -1;
                }
                if (map.get("avci") != null) {
                    pt.avci = Long.parseLong(map.get("avci").toString());
                } else {
                    pt.avci = -1;
                }
                if (map.get("zdlci") != null) {
                    pt.zdlci = Long.parseLong(map.get("zdlci").toString());
                } else {
                    pt.zdlci = -1;
                }
                if (map.get("zvpi") != null) {
                    pt.zvpi = Long.parseLong(map.get("zvpi").toString());
                } else {
                    pt.zvpi = -1;
                }
                if (map.get("zvci") != null) {
                    pt.zvci = Long.parseLong(map.get("zvci").toString());
                } else {
                    pt.zvci = -1;
                }
                if (map.get("loopid") != null) {
                    pt.loopid = Long.parseLong(map.get("loopid").toString());
                } else {
                    pt.loopid = -1;
                }
                if (map.get("subloopid") != null) {
                    pt.subloopid = Long.parseLong(map.get("subloopid").toString());
                } else {
                    pt.subloopid = -1;
                }
                if (map.get("actpidnm") != null) {
                    pt.actpidnm = Long.parseLong(map.get("actpidnm").toString());
                } else {
                    pt.actpidnm = -1;
                }
                if (map.get("zctpidnm") != null) {
                    pt.zctpidnm = Long.parseLong(map.get("zctpidnm").toString());
                } else {
                    pt.zctpidnm = -1;
                }
                if (map.get("routeseq") != null) {
                    pt.routeseq = Long.parseLong(map.get("routeseq").toString());
                } else {
                    pt.routeseq = -1;
                }
                if (map.get("channelseq") != null) {
                    pt.channelseq = Long.parseLong(map.get("channelseq").toString());
                } else {
                    pt.channelseq = -1;
                }
                if (map.get("type") != null) {
                    pt.type = Long.parseLong(map.get("type").toString());
                } else {
                    pt.type = -1;
                }
                if (map.get("channeltype") != null) {
                    pt.channeltype = Long.parseLong(map.get("channeltype").toString());
                } else {
                    pt.channeltype = -1;
                }
                if (map.get("orentation") != null) {
                    pt.orentation = Long.parseLong(map.get("orentation").toString());
                } else {
                    pt.orentation = -1;
                }
                if (map.get("aneldnm") != null) {
                    pt.aneldnm = Long.parseLong(map.get("aneldnm").toString());
                } else {
                    pt.aneldnm = -1;
                }
                if (map.get("aportldnm") != null) {
                    pt.aportldnm = Long.parseLong(map.get("aportldnm").toString());
                } else {
                    pt.aportldnm = -1;
                }
                if (map.get("actpldnm") != null) {
                    pt.actpldnm = Long.parseLong(map.get("actpldnm").toString());
                } else {
                    pt.actpldnm = -1;
                }
                if (map.get("zneldnm") != null) {
                    pt.zneldnm = Long.parseLong(map.get("zneldnm").toString());
                } else {
                    pt.zneldnm = -1;
                }
                if (map.get("zportldnm") != null) {
                    pt.zportldnm = Long.parseLong(map.get("zportldnm").toString());
                } else {
                    pt.zportldnm = -1;
                }
                if (map.get("pt.zportldnm =") != null) {
                    pt.zportldnm = Long.parseLong(map.get("pt.zportldnm =").toString());
                } else {
                    pt.zportldnm = -1;
                }
                if (map.get("aslot_type") != null) {
                    pt.aslot_type = map.get("aslot_type").toString();
                } else {
                    pt.aslot_type = "";
                }
                if (map.get("zslot_type") != null) {
                    pt.zslot_type = map.get("zslot_type").toString();
                } else {
                    pt.zslot_type = "";
                }
                if (map.get("aendddf") != null) {
                    pt.aendddf = map.get("aendddf").toString();
                } else {
                    pt.aendddf = "";
                }
                if (map.get("aendoppddf") != null) {
                    pt.aendoppddf = map.get("aendoppddf").toString();
                } else {
                    pt.aendoppddf = "";
                }
                if (map.get("zendddf") != null) {
                    pt.zendddf = map.get("zendddf").toString();
                } else {
                    pt.zendddf = "";
                }
                if (map.get("zendoppddf") != null) {
                    pt.zendoppddf = map.get("zendoppddf").toString();
                } else {
                    pt.zendoppddf = "";
                }
                if (map.get("channelno") != null) {
                    pt.channelno = map.get("channelno").toString();
                } else {
                    pt.channelno = "";
                }
                if (map.get("trip_delay") != null) {
                    pt.trip_delay = map.get("trip_delay").toString();
                } else {
                    pt.trip_delay = "";
                }
                if (map.get("aatmif") != null) {
                    pt.aatmif = map.get("aatmif").toString();
                } else {
                    pt.aatmif = "";
                }
                if (map.get("zatmif") != null) {
                    pt.zatmif = map.get("zatmif").toString();
                } else {
                    pt.zatmif = "";
                }
                if (map.get("anode_name") != null) {
                    pt.anode_name = map.get("anode_name").toString();
                } else {
                    pt.anode_name = "";
                }
                if (map.get("znode_name") != null) {
                    pt.znode_name = map.get("znode_name").toString();
                } else {
                    pt.znode_name = "";
                }
                if (map.get("aneidnm") != null) {
                    pt.aneidnm = Long.parseLong(map.get("aneidnm").toString());
                } else {
                    pt.aneidnm = 0;
                }
                if (map.get("aportidnm") != null) {
                    pt.aportidnm = Long.parseLong(map.get("aportidnm").toString());
                } else {
                    pt.aportidnm = 0;
                }
                if (map.get("zneidnm") != null) {
                    pt.zneidnm = Long.parseLong(map.get("zneidnm").toString());
                } else {
                    pt.zneidnm = 0;
                }
                if (map.get("zportidnm") != null) {
                    pt.zportidnm = Long.parseLong(map.get("zportidnm").toString());
                } else {
                    pt.zportidnm = 0;
                }
            _data_list.add(pt);
            }
        }
        return _data_list;
    }

    public static boolean _pathtrcaoracle(org.apache.log4j.Logger log, util.GetSql.csnms _csnms, util.GetSql.atm_mysql _mysql, strclass.pathtrace pt) {
        boolean res = false;
        int res1 = 0;
        String Strsql = "select * from pathtrace20150311ce p where p.path_id =" + pt.path_id + " and p.serial_id=" + pt.serial_id;
        try {
            res1 = _csnms.execute(Strsql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res1 > 0) {
            res = true;
        }
        return res;
    }

    public static int update_pathtrace(util.GetSql.csnms _csnms, strclass.pathtrace pt) {
        int res = 0;
        String Strsql = "UPDATE pathtrace20150311ce SET "
                + "network_id='" + pt.network_id + "',"
                + " path_id='" + pt.path_id + "',"
                + " serial_id='" + pt.serial_id + "', "
                + "routeseq='" + pt.routeseq + "',"
                + " channelSeq='" + pt.channelseq + "',"
                + " type=" + pt.type + ","
                + " channelType='" + pt.channeltype + "',"
                + " orentation='" + pt.orentation + "', "
                + "anode_id='" + pt.anode_id + "', "
                + "ashelf_id='" + pt.ashelf_id + "',"
                + " aslot_id='" + pt.aslot_id + "',"
                + " aport_id='" + pt.aport_id + "',"
                + " atimeslot_id='" + pt.atimeslot_id + "', "
                + "znode_id='" + pt.znode_id + "',"
                + "zshelf_id='" + pt.zshelf_id + "', "
                + "zslot_id='" + pt.zslot_id + "', "
                + "zport_id='" + pt.zport_id + "',"
                + "ztimeslot_id='" + pt.ztimeslot_id + "',"
                + " adlci='" + pt.adlci + "',"
                + " avpi='" + pt.avpi + "', "
                + "avci='" + pt.avci + "', "
                + "zdlci='" + pt.zdlci + "',"
                + " zvpi='" + pt.zvpi + "',"
                + " zvci='" + pt.zvci + "',"
                + "loopid='" + pt.loopid + "', "
                + "aNeIdNM='" + pt.aneidnm + "',"
                + " aPortIdNM='" + pt.aportidnm + "',"
                + " aCtpIdNM='" + pt.actpidnm + "', "
                + "zNeIdNM='" + pt.zneidnm + "',"
                + "zPortIdNM='" + pt.zportidnm + "', "
                + "zCtpIdNM='" + pt.zctpidnm + "',"
                + " subloopid='" + pt.subloopid + "',"
                + " aAtmif='" + pt.aatmif + "',"
                + "zAtmif='" + pt.zatmif + "', "
                + "anode_name='" + pt.anode_name + "',"
                + "znode_name='" + pt.znode_name + "',"
                + " trip_delay='" + pt.trip_delay + "' "
                + " WHERE (network_id=" + pt.network_id + ") AND (path_id='" + pt.path_id + "')"
                + " AND (serial_id='" + pt.serial_id + "') AND (loopid='" + pt.loopid + "')";
        try {
            res = _csnms.execute(Strsql, null);
            if (res > 0) {
                System.out.println("[pathtrace更新成功！！]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static int intsert_oraclepathtrca(util.GetSql.csnms _csnms, strclass.pathtrace pt) {
        int res = 0;
        String Strsql = "INSERT INTO pathtrace20150311ce ("
                + "network_id, path_id, serial_id,routeseq, channelSeq,"
                + " type, channelType, orentation, anode_id, ashelf_id,"
                + " aslot_id, aport_id, atimeslot_id, znode_id, zshelf_id,"
                + " zslot_id, zport_id, ztimeslot_id, adlci, avpi, "
                + "avci, zdlci, zvpi, zvci, loopid,"
                + " aNeIdNM, aPortIdNM, aCtpIdNM, zNeIdNM, zPortIdNM,"
                + " zCtpIdNM, subloopid, aAtmif, zAtmif, anode_name,"
                + " znode_name, trip_delay)"
                + " VALUES ('" + pt.network_id + "', '" + pt.path_id + "', '" + pt.serial_id + "', '" + pt.routeseq + "', '" + pt.channelseq + "',"
                + " NULL, '" + pt.channeltype + "', '" + pt.orentation + "', '" + pt.anode_id + "', '" + pt.ashelf_id + "',"
                + " '" + pt.aslot_id + "', '" + pt.aport_id + "', '" + pt.atimeslot_id + "', '" + pt.znode_id + "', '" + pt.zshelf_id + "', "
                + "'" + pt.zslot_id + "', '" + pt.zport_id + "', '" + pt.ztimeslot_id + "', '" + pt.adlci + "', '" + pt.avpi + "',"
                + " '" + pt.avci + "', '" + pt.zdlci + "', '" + pt.zvpi + "','" + pt.zvci + "', '" + pt.loopid + "', "
                + "'" + pt.aneidnm + "', '" + pt.aportidnm + "', '" + pt.actpidnm + "', '" + pt.zneidnm + "', '" + pt.zportidnm + "',"
                + " '" + pt.zctpidnm + "', '" + pt.subloopid + "', '" + pt.aatmif + "', '" + pt.zatmif + "', '" + pt.anode_name + "',"
                + " '" + pt.znode_name + "', '" + pt.trip_delay + "')";
        try {
            res = _csnms.execute(Strsql, null);
            if (res > 0) {
                System.out.println("插入成功！！！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

}
