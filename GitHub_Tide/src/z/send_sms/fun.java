/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.send_sms;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {

    public static Hashtable ini_telstr(int type) {
        Hashtable _data = new Hashtable();
        if (type == 1) {//移动
            _data.put("134", "134");
            _data.put("135", "135");
            _data.put("136", "136");
            _data.put("137", "137");
            _data.put("138", "138");
            _data.put("139", "139");
            _data.put("150", "150");
            _data.put("151", "151");
            _data.put("152", "152");
            _data.put("157", "157");
            _data.put("158", "158");
            _data.put("159", "159");
            _data.put("182", "182");
            _data.put("183", "183");
            _data.put("184", "184");
            _data.put("187", "187");
            _data.put("188", "188");
            _data.put("178", "178");// 4G
            _data.put("147", "147");// 上网卡
        } else if (type == 2) {//联通
            _data.put("130", "130");
            _data.put("131", "131");
            _data.put("132", "132");
            _data.put("155", "155");
            _data.put("156", "156");
            _data.put("185", "185");
            _data.put("186", "186");
            _data.put("176", "176");// 4G
            _data.put("145", "145");// 上网卡
        } else if (type == 3) {//电信
            _data.put("133", "133");
            _data.put("153", "153");
            _data.put("180", "180");
            _data.put("181", "181");
            _data.put("189", "189");
            _data.put("177", "177");// 4G
            _data.put("170", "170");// 虚拟运营商
            _data.put("1349", "1349");// 卫星通信
        } else if (type == 4) {//需要程序过滤的号码
            //_data.put("133", "133");
        }
        return _data;
    }

    public static String get_city_code(String _city_code) {
        String kk = "";
        if (_city_code.equals("600837440")) {
            kk = "3";
        }
        if (_city_code.equals("613562383")) {
            kk = "4";
        }
        if (_city_code.equals("605023125")) {
            kk = "15";
        }
        if (_city_code.equals("618820000")) {
            kk = "20";
        }
        if (_city_code.equals("618830000")) {
            kk = "26";
        }
        if (_city_code.equals("618840000")) {
            kk = "33";
        }
        if (_city_code.equals("618850000")) {
            kk = "39";
        }
        if (_city_code.equals("618800000")) {
            kk = "48";
        }
        if (_city_code.equals("618860000")) {
            kk = "60";
        }
        if (_city_code.equals("618870000")) {
            kk = "63";
        }
        if (_city_code.equals("618747820")) {
            kk = "69";
        }
        if (_city_code.equals("618810000")) {
            kk = "79";
        }
        if (_city_code.equals("618880000")) {
            kk = "84";
        }
        return kk;
    }

    public static String get_boss(String _tel, Hashtable _yidong, Hashtable _liantong, Hashtable _dianxin) {
        String _boss = "";
        _tel = _tel.substring(0, 3);
        if (_yidong.containsKey(_tel)) {
            _boss = "移动";
        }

        if (_dianxin.containsKey(_tel)) {
            _boss = "电信";
        }

        if (_liantong.containsKey(_tel)) {
            _boss = "联通";
        }
        return _boss;
    }

    public static List get_sms_boss(int type, List _msm_data, Hashtable headstr_yidong, Hashtable headstr_liantong, Hashtable headstr_dianxin, util.GetSql.csnms _csnms) {
        List _list = new ArrayList();

        //检测短信并分流数据   1电信    2联通移动
        for (int i = 0, m = _msm_data.size(); i < m; i++) {
            //声明实体并赋值
            z.allClass.sendsms_sendstr _sms = new z.allClass.sendsms_sendstr();
            _sms = (z.allClass.sendsms_sendstr) _msm_data.get(i);//加载数据
            _sms.CITYID = fun.get_city_code(_sms.CITYID);//转换城市代码
            //判断短信所属的运营商
            String _tel_boss = fun.get_boss(_sms.TEL, headstr_yidong, headstr_liantong, headstr_dianxin);
            if (_tel_boss.equals("电信") && type == 1) {
                // 如果是电信的短信  添加退订短语                    
                _sms.CONTENT = _sms.CONTENT.replace("如不想再收到此端口下发的短信，请回复00000", "");
                String send_mess = _sms.CONTENT + " 如不想再收到此端口下发的短信，请回复00000";
                if (!db.update_description(_csnms, send_mess, _sms.STR_ID.toString())) {
                    System.out.println("[动环短信]添加退订短语失败 ");
                }
                //写入电信短信数组
                _list.add(_sms);
            } else {
                //写入移动联通短信数组
                _list.add(_sms);
            }
        }

        return _list;
    }

}
