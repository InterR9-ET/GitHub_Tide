/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_atm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {

    private static util.GetTools.tools _datas = new util.GetTools.tools();

    //-------------------------------------------------------------------------
    public static void insert_csnms2(util.GetSql.csnms _csnms, List _list) {//type=0  默认不启用   type=1启动update

        List _List_in = new ArrayList();
        List _List_up = new ArrayList();

        if (_list.size() > 0) {
            for (int i = 0, m = _list.size(); i < m; i++) {
                fun.atmif_performance _xn;

                System.out.println("进度：" + (i + 1) + "/" + m);
                _xn = (fun.atmif_performance) _list.get(i);
                boolean _bs = db.seach_has_data12_csnms(_csnms, _xn);
                String str_sql = "";
                if (!_bs) {
                    _List_in.add(_xn);
                } else {
                    _List_up.add(_xn);
                }
            }

            if (_List_in.size() > 0) {
                tb_atmif_performance does = new tb_atmif_performance();
                db.tb_in_up(_csnms, "in", _List_in);

            }

            if (_List_up.size() > 0) {
                tb_atmif_performance does = new tb_atmif_performance();
                db.tb_in_up(_csnms, "up", _List_up);

            }
        }
    }

    //-------------------------------------------------------------------------

    public static String return_value(String value1, String value2) {
        String mes = "";
        long _d1 = Long.parseLong(value1);
        long _d2 = Long.parseLong(value2);
        long _dbs = 0;
        _dbs = _d1 - _d2;
        System.out.println("value1-value2=" + _d1 + "-" + _d2 + "=" + _dbs);
        if (_dbs >= 0) {
            mes = _dbs + "";
        } else {
            mes = _d1 + "";
        }
        return mes;
    }

    //-------------------------------------------------------------------------

    public static List get_xn_csnms2(List _SWITCH_REPORT_OUT, List _atm_data) {
        List _list_data = new ArrayList();

        atm _atm = new atm();

        for (int i = 0, k = _SWITCH_REPORT_OUT.size(); i < k; i++) {
            // System.out.println("进度：" + (i + 1) + "/" + k);
            tb_SWITCH_REPORT_OUT._SWITCH_REPORT_OUT _xn = new tb_SWITCH_REPORT_OUT._SWITCH_REPORT_OUT();
            _xn = (tb_SWITCH_REPORT_OUT._SWITCH_REPORT_OUT) _SWITCH_REPORT_OUT.get(i);
            for (int i2 = 0, m = _atm_data.size(); i2 < m; i2++) {
                _atm = new atm();
                _atm = (atm) _atm_data.get(i2);

                //筛选选符合条件的数据,重新赋值
                boolean _kkk_s = false;
                if (_xn.NODEID.equals(_atm.NODEID)) {
                    if (_xn.ATMIF.equals(_atm.ATMIF)) {
                        if (_xn.VCI.equals(_atm.VCI)) {
                            if (_xn.VPI.equals(_atm.VPI)) {
                                _kkk_s = true;
                            }
                        }
                    }
                }

                if (_kkk_s) {
                    System.out.println(_xn.DEVICEID + "#" + _xn.PORTINFO + "#" + _xn.VCI + "#" + _xn.VPI);
                    _xn.IFINOCTETS = return_value(_xn.IFINOCTETS, _atm.RXCELL);  //流入
                    _xn.IFOUTOCTETS = return_value(_xn.IFOUTOCTETS, _atm.TXCELL);//流出

                    String _time_s_1 = "";
                    try {
                        String ss1 = _xn.DATETIME + "000";
                        _time_s_1 = _datas.sjctime_prase_string(ss1 + "");
                    } catch (Exception e) {

                    }

                    long _time_s = Long.parseLong(_datas.compare_time(_time_s_1, _atm.UPDATETIME, "S"));

                    long _d1 = 0;
                    long _d2 = 0;

                    if (_time_s > 0) {
                        _d1 = Long.parseLong(_xn.IFINOCTETS) / _time_s;
                    } else {
                        _d1 = Long.parseLong(_xn.IFINOCTETS);
                    }
                    if (_time_s > 0) {
                        _d2 = Long.parseLong(_xn.IFOUTOCTETS) / _time_s;
                    } else {
                        _d2 = Long.parseLong(_xn.IFOUTOCTETS);
                    }

                    //换成字节
                    _d1 = _d1 * 53;
                    _d2 = _d2 * 53;

                    _xn.IFINOCTETS = _d1 + "";
                    _xn.IFOUTOCTETS = _d2 + "";

                    //--------20140703  流入流出调换顺序---------------------------//
                    String _dh_bs1 = _xn.IFINOCTETS;
                    String _dh_bs2 = _xn.IFOUTOCTETS;
                    _xn.IFINOCTETS = _dh_bs2;  //流入
                    _xn.IFOUTOCTETS = _dh_bs1;//流出
                    //--------20140703  流入流出调换顺序---------------------------//

                    //换成位
                    _d1 = _d1 * 8;
                    _d2 = _d2 * 8;
                    _xn.IFINOCTETSBPS = _d1 + "";
                    _xn.IFOUTOCTETSBPS = _d2 + "";

                    //--------20140703  流入流出调换顺序---------------------------//
                    String _dh_bs3 = _xn.IFINOCTETSBPS;
                    String _dh_bs4 = _xn.IFOUTOCTETSBPS;
                    _xn.IFINOCTETSBPS = _dh_bs4 + "";
                    _xn.IFOUTOCTETSBPS = _dh_bs3 + "";
                    //--------20140703  流入流出调换顺序---------------------------//

                    //--------20140703  丢包调换顺序---------------------------//
                    String _dh_bs5 = _xn.IFINDISCARDS;
                    String _dh_bs6 = _xn.IFOUTDISCARDS;
                    _xn.IFINDISCARDS = _dh_bs6 + "";
                    _xn.IFOUTDISCARDS = _dh_bs5 + "";
                    //--------20140703  丢包调换顺序---------------------------//

                    //--------20140703  误码率调换顺序---------------------------//
                    String _dh_bs7 = _xn.IFINERRORS;
                    String _dh_bs8 = _xn.IFOUTERRORS;
                    _xn.IFINERRORS = _dh_bs8 + "";
                    _xn.IFOUTERRORS = _dh_bs7 + "";
                    //--------20140703  误码率调换顺序---------------------------//

                    _list_data.add(_xn);
                }
            }
        }

        return _list_data;
    }

    //--------------------------------------------------------------------------
    public static long string2Timestamp(String dateString) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        long temp = date1.getTime();//JAVA的时间戳长度是13位     
        return temp;
    }

//-----------------------------调用添加信息的方法-------------------------------------------------
    public static void insert_csnms(util.GetSql.csnms _csnms, List _list,org.apache.log4j.Logger log) {

        tb_SWITCH_REPORT_OUT does = new tb_SWITCH_REPORT_OUT();
        //_tb_in.insert_SWITCH_REPORT_OUTs(_list);
        if (_list.size() > 0) {
            db.tb_in_up(_csnms, "in", _list);
        }else{
            log.info("[oracle没有数据]");
        }
    }

//------------------------------------------------------------------------------
    public static class atmif_performance {

        public String DATETIME = "";
        public String DEVICEID = "";
        public String PORTINFO = "";
        public String GETWAY = "";
        public String IFINOCTETS = "0";
        public String IFOUTOCTETS = "0";
        public String IFINERRORS = "0";
        public String IFOUTERRORS = "0";
        public String IFINDISCARDS = "0";
        public String IFOUTDISCARDS = "0";
        public String IFINUCASTPKTS = "0";
        public String IFOUTUCASTPKTS = "0";
        public String IFINNUCASTPKTS = "0";
        public String IFOUTNUCASTPKTS = "0";
        public String IFINUNKNOWNPROTOS = "0";
        public String IFOUTQLEN = "0";
        public String IFINOCTETSBPS = "0";
        public String IFOUTOCTETSBPS = "0";
        public String IFINERRORSPPS = "0";
        public String IFOUTERRORSPPS = "0";
        public String IFINDISCARDSPPS = "0";
        public String IFOUTDISCARDSPPS = "0";
        public String IFINUCASTPKTSPPS = "0";
        public String IFOUTUCASTPKTSPPS = "0";
        public String IFINNUCASTPKTSPPS = "0";
        public String IFOUTNUCASTPKTSPPS = "0";
        public String IFINUNKNOWNPROTOSPPS = "0";
        public String IFOUTQLENPPS = "0";
        public String DELTATIME = "0";

        public String NODEID = "";
        public String ATMIF = "";
        public String VCI = "";
        public String VPI = "";

    }

    //---------------------------------------------------------------------------

    public static class _SWITCH_REPORT_OUT {

        public String DATETIME = "";
        public String DEVICEID = "";
        public String PORTINFO = "";
        public String GETWAY = "3";
        public String IFINOCTETS = "0";
        public String IFOUTOCTETS = "0";
        public String IFINERRORS = "0";
        public String IFOUTERRORS = "0";
        public String IFINDISCARDS = "0";
        public String IFOUTDISCARDS = "0";
        public String IFINUCASTPKTS = "0";
        public String IFOUTUCASTPKTS = "0";
        public String IFINNUCASTPKTS = "0";
        public String IFOUTNUCASTPKTS = "0";
        public String IFINUNKNOWNPROTOS = "0";
        public String IFOUTQLEN = "0";
        public String IFINOCTETSBPS = "0";
        public String IFOUTOCTETSBPS = "0";
        public String IFINERRORSPPS = "0";
        public String IFOUTERRORSPPS = "0";
        public String IFINDISCARDSPPS = "0";
        public String IFOUTDISCARDSPPS = "0";
        public String IFINUCASTPKTSPPS = "0";
        public String IFOUTUCASTPKTSPPS = "0";
        public String IFINNUCASTPKTSPPS = "0";
        public String IFOUTNUCASTPKTSPPS = "0";
        public String IFINUNKNOWNPROTOSPPS = "0";
        public String IFOUTQLENPPS = "0";
        public String DELTATIME = "0";

        public String NODEID = "";
        public String ATMIF = "";
        public String VCI = "";
        public String VPI = "";

    }

    //--------------------------------------------------------------------------

    public static class atm {

        public String nodeid = "";
        public String NODEID = "";
        public String ATMIF = "";
        public String VPI = "";
        public String VCI = "";
        public String TXCELL = "";
        public String TXCELLDISCARD = "";
        public String RXCELL = "";
        public String RXCELLDISCARD = "";
        public String RXAAL5FRAMEERROR = "";
        public String UPDATETIME = "";
    }

}

class tb_SWITCH_REPORT_OUT {
    /*
     public static Connection conn = null;
     public static PreparedStatement pst = null;
     public static Hashtable lst_p2 = new Hashtable();*/
//----------------------------------------------------------------

    public static class _SWITCH_REPORT_OUT {

        public String DATETIME = "";
        public String DEVICEID = "";
        public String PORTINFO = "";
        public String GETWAY = "3";
        public String IFINOCTETS = "0";
        public String IFOUTOCTETS = "0";
        public String IFINERRORS = "0";
        public String IFOUTERRORS = "0";
        public String IFINDISCARDS = "0";
        public String IFOUTDISCARDS = "0";
        public String IFINUCASTPKTS = "0";
        public String IFOUTUCASTPKTS = "0";
        public String IFINNUCASTPKTS = "0";
        public String IFOUTNUCASTPKTS = "0";
        public String IFINUNKNOWNPROTOS = "0";
        public String IFOUTQLEN = "0";
        public String IFINOCTETSBPS = "0";
        public String IFOUTOCTETSBPS = "0";
        public String IFINERRORSPPS = "0";
        public String IFOUTERRORSPPS = "0";
        public String IFINDISCARDSPPS = "0";
        public String IFOUTDISCARDSPPS = "0";
        public String IFINUCASTPKTSPPS = "0";
        public String IFOUTUCASTPKTSPPS = "0";
        public String IFINNUCASTPKTSPPS = "0";
        public String IFOUTNUCASTPKTSPPS = "0";
        public String IFINUNKNOWNPROTOSPPS = "0";
        public String IFOUTQLENPPS = "0";
        public String DELTATIME = "0";

        public String NODEID = "";
        public String ATMIF = "";
        public String VCI = "";
        public String VPI = "";

    }
}

class tb_atmif_performance {

    public static class atm {

        public String nodeid = "";
        public String NODEID = "";
        public String ATMIF = "";
        public String VPI = "";
        public String VCI = "";
        public String TXCELL = "";
        public String TXCELLDISCARD = "";
        public String RXCELL = "";
        public String RXCELLDISCARD = "";
        public String RXAAL5FRAMEERROR = "";
        public String UPDATETIME = "";

    }
//---------------------------------------------------------------------

    public static class atmif_performance {

        public String DATETIME = "";
        public String DEVICEID = "";
        public String PORTINFO = "";
        public String GETWAY = "";
        public String IFINOCTETS = "0";
        public String IFOUTOCTETS = "0";
        public String IFINERRORS = "0";
        public String IFOUTERRORS = "0";
        public String IFINDISCARDS = "0";
        public String IFOUTDISCARDS = "0";
        public String IFINUCASTPKTS = "0";
        public String IFOUTUCASTPKTS = "0";
        public String IFINNUCASTPKTS = "0";
        public String IFOUTNUCASTPKTS = "0";
        public String IFINUNKNOWNPROTOS = "0";
        public String IFOUTQLEN = "0";
        public String IFINOCTETSBPS = "0";
        public String IFOUTOCTETSBPS = "0";
        public String IFINERRORSPPS = "0";
        public String IFOUTERRORSPPS = "0";
        public String IFINDISCARDSPPS = "0";
        public String IFOUTDISCARDSPPS = "0";
        public String IFINUCASTPKTSPPS = "0";
        public String IFOUTUCASTPKTSPPS = "0";
        public String IFINNUCASTPKTSPPS = "0";
        public String IFOUTNUCASTPKTSPPS = "0";
        public String IFINUNKNOWNPROTOSPPS = "0";
        public String IFOUTQLENPPS = "0";
        public String DELTATIME = "0";

        public String NODEID = "";
        public String ATMIF = "";
        public String VCI = "";
        public String VPI = "";

    }

}
