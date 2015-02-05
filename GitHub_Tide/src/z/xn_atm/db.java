/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_atm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private static util.GetTools.tools _datas = new util.GetTools.tools();

    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms,util.GetSql.atm_mysql _mysql) {
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
    
    
    
    //--------------------------------------------------------------------------
    public static void _del_xn_atm(util.GetSql.atm_mysql _mysql,List _list) {
        List _sql_list = new ArrayList();
        tb_atmif_performance.atmif_performance _xn = new tb_atmif_performance.atmif_performance();
        for (int i = 0, m = _list.size(); i < m; i++) {
            //System.out.println("进度：" + (i + 1) + "/" + m);
            _xn = (tb_atmif_performance.atmif_performance) _list.get(i);
            String str_sql = "delete   from atmif_performance2 where  "
                    + " NODEID=" + _xn.NODEID + " and "
                    + " ATMIF= " + _xn.ATMIF + " and "
                    + " VPI=" + _xn.VPI + " and "
                    + " VCI=" + _xn.VCI;
            _sql_list.add(str_sql);
        }

        if (_sql_list.size() > 0) {
            _mysql.execute(_sql_list);
        }
    }
    //--------------------------------------------------------------------------
    public static boolean seach_has_data12_csnms(util.GetSql.csnms _csnms,fun.atmif_performance _xn) {
        boolean _bs = true;
        String str_sql = " select  count(1) as COUNT from  atmif_performance  m  where  "
                + "m.NODEID=" + _xn.NODEID + "  and  "
                + "m.ATMIF=" + _xn.ATMIF + " and "
                + "m.VPI=" + _xn.VPI + " and "
                + "m.VCI=" + _xn.VCI;

        List _list = new ArrayList();
        try {
            _list = _csnms.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_list.size() > 0) {
            HashMap map = (HashMap) _list.get(0);
            try {
                String _count = map.get("COUNT").toString();
                if (_count.equals("0")) {
                    _bs = false;
                } else {
                    _bs = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return _bs;
    }
    //--------------------------------------------------------------------------
       public static void tb_in_up(util.GetSql.csnms _csnms,String type_in_up, List _data) {
        //构造预处理
        try {

            int count_s = 0;
            for (int i = 0, m = _data.size(); i < m; i++) {
                fun.atmif_performance _datal = new fun.atmif_performance();
                _datal = (fun.atmif_performance) _data.get(i);
                count_s = count_s + 1;

                //-------------处理时间---------------//
                String _GETTIME = _datal.DATETIME;
                //System.out.println("#####" + _GETTIME);
                SimpleDateFormat sdf_temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date _date1 = new Date();
                _date1 = sdf_temp.parse(_datas.sjctime_prase_string(_GETTIME));
                //------------------------------------//
                if (type_in_up.equals("in")) {
                    String str_sql = "insert into  atmif_performance2 ("
                            + "NODEID,"
                            + "ATMIF,"
                            + "VPI,"
                            + "VCI,"
                            + "TXCELL,"
                            + "TXCELLDISCARD,"
                            + "RXCELL,"
                            + "RXCELLDISCARD,"
                            + "RXAAL5FRAMEERROR,"
                            + "UPDATETIME,"
                            + "DEVICEID"
                            + ") values ("
                            + "" + Long.parseLong(_datal.NODEID) + ","
                            + "" + Long.parseLong(_datal.ATMIF) + ","
                            + "" + Long.parseLong(_datal.VPI) + ","
                            + "" + Long.parseLong(_datal.VCI) + ","
                            + "0,"
                            + "0,"
                            + "0,"
                            + "0,"
                            + "0,"
                            + "to_date('" + _date1.getTime() + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + "" + Long.parseLong(_datal.DEVICEID) + ""
                            + ")";

                    _csnms.execute(str_sql, null);

                } else if (type_in_up.equals("up")) {
                    String str_sql = "update atmif_performance2  set  "
                            + " TXCELL=" + Long.parseLong(_datal.IFOUTOCTETS) + ","
                            + " TXCELLDISCARD=" + Long.parseLong(_datal.IFOUTDISCARDS) + ","
                            + " RXCELL=" + Long.parseLong(_datal.IFINOCTETS) + ","
                            + " RXCELLDISCARD=" + Long.parseLong(_datal.IFINDISCARDS) + ","
                            + " RXAAL5FRAMEERROR=" + Long.parseLong(_datal.IFINERRORS) + ","
                            + " UPDATETIME=" + "to_date('" + _date1.getTime() + "','yyyy-mm-dd hh24:mi:ss')" + ","
                            + " DEVICEID=" + Long.parseLong(_datal.DEVICEID) + " "
                            + " where  "
                            + " NODEID=" + Long.parseLong(_datal.NODEID) + "  and "
                            + "   ATMIF=" + Long.parseLong(_datal.ATMIF) + "  and"
                            + "   VPI=" + Long.parseLong(_datal.VPI) + "  and"
                            + "   VCI=" + Long.parseLong(_datal.VCI) + " ";
                    _csnms.execute(str_sql, null);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //log.info("构造预处理失败");
        }
    }
    
       
    //--------------------------------------------------------------------------
       public static List get_xn_csnms(util.GetSql.csnms _csnms) {
        List _data_list = new ArrayList();
        List _list = new ArrayList();
        String str_sql = "select  "
                + "a.NODEID as NODEID,"
                + "a.ATMIF as ATMIF,"
                + "a.VPI as VPI,"
                + "a.VCI as VCI,"
                + "a.TXCELL as TXCELL,"
                + "a.TXCELLDISCARD as TXCELLDISCARD,"
                + "a.RXCELL as RXCELL,"
                + "a.RXCELLDISCARD as RXCELLDISCARD,"
                + "a.RXAAL5FRAMEERROR as RXAAL5FRAMEERROR,"
                + "to_char(a.UPDATETIME,'yyyy-mm-dd hh24:mi:ss') as UPDATETIME"
                + "  from  atmif_performance a ";

        List _data = new ArrayList();
        Object[] objs = new Object[]{};
        try {
            _data = _csnms.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fun.atm _atm = new fun.atm();
        //*53*8
        if (_data.size() > 0) {
            for (int i = 0, m = _data.size(); i < m; i++) {
                //System.out.println("进度：" + (i + 1) + "/" + m);
                _atm = new fun.atm();
                HashMap map = (HashMap) _data.get(i);

                _atm.NODEID = map.get("NODEID").toString();
                _atm.ATMIF = map.get("ATMIF").toString();
                _atm.VPI = map.get("VPI").toString();
                _atm.VCI = map.get("VCI").toString();
                _atm.TXCELL = map.get("TXCELL").toString();
                _atm.TXCELLDISCARD = map.get("TXCELLDISCARD").toString();
                _atm.RXCELL = map.get("RXCELL").toString();
                _atm.RXCELLDISCARD = map.get("RXCELLDISCARD").toString();
                _atm.RXAAL5FRAMEERROR = map.get("RXAAL5FRAMEERROR").toString();
                _atm.UPDATETIME = map.get("UPDATETIME").toString();

                _data_list.add(_atm);
            }
        }

        return _data_list;
    }
       
       
    //------------------------------获取56上的性能数据--------------------------------------------
    public static List get_xn_atm(util.GetSql.csnms _csnms,util.GetSql.atm_mysql _mysql) {
        List _data_list = new ArrayList();
        String str_sql = "select  "
                + " a.nodeId as NODEID,"
                + " a.atmif as ATMIF,"
                + " a.vpi as VPI,"
                + " a.vci as VCI,"
                + " a.create_time as CREATE_TIME,"
                + " a.txCell as TXCELL,"
                + " a.txCellDiscard as TXCELLDISCARD,"
                + " a.rxCell as RXCELL,"
                + " a.rxCellDiscard as RXCELLDISCARD,"
                + " a.rxAal5FrameError as RXAAL5FRAMEERROR,"
                + " a.adminState as ADMINSTATE,"
                + " a.operationalState as OPERATIONALSTATE,"
                + " a.usageState as USAGESTATE,"
                + " a.operStatus as OPERSTATUS,"
                + " a.delay  as DELAY ,"
                + " n.inter_net_addr  as IP "
                + " from  atmif_performance  a ,node  n  "
                + " where   a.nodeId=n.node_id  order by  a.create_time limit 5000  ";
        //System.out.println(str_sql);

        List _data = new ArrayList();
        try {
            _data = _mysql.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_data.size() > 0) {
            for (int i = 0, m = _data.size(); i < m; i++) {
                // System.out.println("进度：" + (i + 1) + "/" + m);
                fun._SWITCH_REPORT_OUT _xn = new  fun._SWITCH_REPORT_OUT();
                //_xn = new fun._SWITCH_REPORT_OUT();
                HashMap map = (HashMap) _data.get(i);
                String bs_time = map.get("CREATE_TIME").toString();
                try {
                    long l_time = fun.string2Timestamp(bs_time);
                    _xn.DATETIME = l_time + "";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String _ip = map.get("IP").toString();
                _xn.DEVICEID = get_device_id_csnms( _csnms,_ip);
                //System.out.println(_xn.DEVICEID);
                _xn.PORTINFO = "atmif:" + map.get("ATMIF").toString() + "/vpi:" + map.get("VPI").toString() + "/vci:" + map.get("VCI").toString();

                _xn.IFINOCTETS = map.get("RXCELL").toString();//流入
                _xn.IFOUTOCTETS = map.get("TXCELL").toString();//流出

                _xn.IFINDISCARDS = map.get("RXCELLDISCARD").toString();//流入丢包
                _xn.IFOUTDISCARDS = map.get("TXCELLDISCARD").toString();//流出丢包               

                _xn.IFINERRORS = map.get("RXAAL5FRAMEERROR").toString();//流入误码率

                _xn.NODEID = map.get("NODEID").toString();
                _xn.ATMIF = map.get("ATMIF").toString();

                _xn.VPI = map.get("VPI").toString();
                _xn.VCI = map.get("VCI").toString();

                _data_list.add(_xn);
            }
        }
        return _data_list;
    }
       
    //--------------------------------------------------------------------------
    public static List get_xn_atm_2(util.GetSql.csnms _csnms,util.GetSql.atm_mysql _mysql) {
        List _data_list = new ArrayList();
        String str_sql = "select  "
                + " a.nodeId as NODEID,"
                + " a.atmif as ATMIF,"
                + " a.vpi as VPI,"
                + " a.vci as VCI,"
                + " a.create_time as CREATE_TIME,"
                + " a.txCell as TXCELL,"
                + " a.txCellDiscard as TXCELLDISCARD,"
                + " a.rxCell as RXCELL,"
                + " a.rxCellDiscard as RXCELLDISCARD,"
                + " a.rxAal5FrameError as RXAAL5FRAMEERROR,"
                + " a.adminState as ADMINSTATE,"
                + " a.operationalState as OPERATIONALSTATE,"
                + " a.usageState as USAGESTATE,"
                + " a.operStatus as OPERSTATUS,"
                + " a.delay  as DELAY ,"
                + " n.inter_net_addr  as IP "
                + " from  atmif_performance  a ,node  n  "
                + " where   a.nodeId=n.node_id  order by  a.create_time  limit 5000";
        //System.out.println(str_sql);

        List _data = new ArrayList();
        try {
            _data = _mysql.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_data.size() > 0) {
            for (int i = 0, m = _data.size(); i < m; i++) {
                // System.out.println("进度：" + (i + 1) + "/" + m);
                fun.atmif_performance _xn = new fun.atmif_performance();
                //_xn = new fun.atmif_performance();
                HashMap map = (HashMap) _data.get(i);

                String bs_time = map.get("CREATE_TIME").toString();

                try {
                    long l_time = fun.string2Timestamp(bs_time);
                    _xn.DATETIME = l_time + "";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String _ip = map.get("IP").toString();
                _xn.DEVICEID = get_device_id_csnms(_csnms,_ip);
                //System.out.println(_xn.DEVICEID);
                _xn.PORTINFO = "atmif:" + map.get("ATMIF").toString() + "/vpi:" + map.get("VPI").toString() + "/vci:" + map.get("VCI").toString();

                _xn.IFINOCTETS = map.get("RXCELL").toString();//流入
                _xn.IFOUTOCTETS = map.get("TXCELL").toString();//流出

                _xn.IFINDISCARDS = map.get("RXCELLDISCARD").toString();//流入丢包
                _xn.IFOUTDISCARDS = map.get("TXCELLDISCARD").toString();//流出丢包               

                _xn.IFINERRORS = map.get("RXAAL5FRAMEERROR").toString();//流入误码率

                _xn.NODEID = map.get("NODEID").toString();
                _xn.ATMIF = map.get("ATMIF").toString();

                _xn.VPI = map.get("VPI").toString();
                _xn.VCI = map.get("VCI").toString();
                _data_list.add(_xn);
            }
        }
        return _data_list;
    }

    //--------------------------------------------------------------------------
    public static String get_device_id_csnms(util.GetSql.csnms _csnms,String _ip) {
        // getSql.SQL_Load("csnms");
        String _device_id = "";
        String str_sql = " select  DEVICE_ID   from  dcn_device  d  where  d.loopback_ip='" + _ip + "' ";

        List _list = new ArrayList();
        try {
            _list = _csnms.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_list.size() > 0) {
            HashMap map = (HashMap) _list.get(0);
            try {
                _device_id = map.get("DEVICE_ID").toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (_device_id.length() == 0) {
            String str_sql3 = "  select  max(DEVICE_ID) as  DEVICE_ID  from  dcn_device  d  where  d.gather_id='bd'";
            List _list3 = new ArrayList();
            try {
                _list3 = _csnms.getdata(str_sql3, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (_list3.size() > 0) {
                HashMap map3 = (HashMap) _list3.get(0);

                if (map3.get("DEVICE_ID") == null) {
                    //初始化一个id
                    int bs = 10000;
                    _device_id = bs + "0003";

                } else {
                    _device_id = map3.get("DEVICE_ID").toString();
                    long l = Long.parseLong(_device_id);
                    l = l + 10000;
                    _device_id = l + "";
                }

                String str_sql_in = "insert  into   dcn_device2(loopback_ip,device_id,gather_id) values("
                        + "'" + _ip + "'" + ","
                        + _device_id + ","
                        + "'bd'"
                        + ")";
                try {
                    int opop = _csnms.execute(str_sql_in, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return _device_id;
    }
}
