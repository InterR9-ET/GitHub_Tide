/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_port;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {
       /**
     * 获取下载好的信息
     *
     * @param file1 文件路径
     * @return
     */
    public List huoqwuwenjian(File file1) {
        //获取下载下来的总目录
        List File_list = new ArrayList();
        File[] files1 = file1.listFiles();
        //遍历子目录的文件，并存放在File_list中
        if (files1 != null) {
            if (files1.length > 0) {
                Arrays.sort(files1);//文件名排序
                for (File files11 : files1) {
                    //isDirectory()判断是不是目录，是返回true
                    if (!files11.isDirectory()) {
                        //判断文件名是否存在bulk   (下载下来的文件已bulk开头)
                        if (files11.toString().contains("bulk")) {
                            String fil = files11.toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }
        return File_list;
    }

    //文件数量大于0
    public void pand(org.apache.log4j.Logger log,List File_list,util.GetThread.thread _thread,util.GetSql.csnms _csnms,Hashtable lst_p2) {
        if (File_list.size() > 0) {
            log.info("执行多线程");
            for (int i = 0, leng = File_list.size(); i < leng; i++) {
                //获取文件名
                String file_name = File_list.get(i).toString();
                //判断文件是否是包含flux，不包含返回-1，包含，返回位置数
                if (file_name.contains("flux")) {
                    //判断文件是否是包含bulk，不包含返回-1，包含，返回位置数
                    if (file_name.indexOf("bulk") != -1) {
                        String fil = file_name;
                        System.out.println(fil + "////////////////////////////////////////////////////////");
                        //-------------添加多线程任务----------------//传入日志，文件名，数据库信息
                        _thread.execute(db.createTask(log, fil, _csnms,lst_p2));
                        System.out.println("添加任务：" + i);
                        //-------------添加多线程任务----------------//
                    }
                }
            }
        }
    }

    /**
     * 处理文件名包含cus_ftth_flux的数据 读取字段，并存放在list中
     *
     * @param filename
     * @return
     */
    public static List PortList_ftth(org.apache.log4j.Logger log,String filename,Hashtable lst_p2) {
        List list = new ArrayList();
        try {
            //log.info("################"+filename);
            FileReader fw = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fw);
            String str = null;
            fun.port2 _port = new fun.port2();

            String[] pm = null;
            while ((str = reader.readLine()) != null) {
                _port = new fun.port2();
                pm = null;

                if (str.length() > 0) {
                    str = str.replace("$||#", "###");
                    str = str.replace("$", "");
                    pm = str.split("###");
                    str = null;
                    //log.info("################"+pm);
                    if (pm.length > 0) {

                        /**
                         * 0 collecttime numeric 采集时间 1 device_id varchar 设备ID 2
                         * port_desc varchar 端口描述 （同cus_specialuser表
                         * assess_port_descr） 3 ontid varchar ontid 4 username
                         * varchar 用户 5 inoctets numeric 流入字节数 6 in_turned
                         * numeric 是否翻转 7 outoctets numeric 流出字节数 8 out_turned
                         * numeric 是否翻转 9 ifinoctetsbps numeric 流入速率 10
                         * ifoutoctetsbps numeric 流出速率
                         */
                        if (pm[0] != null) {
                            _port.DATETIME = pm[0].toString();
                        }
                        _port.DEVICEID = pm[1].toString();
                        //找不到device_id则跳转下一循环
                        if (!lst_p2.containsKey(_port.DEVICEID)) {
                            //log.info("deviceid_id不存在:" + key);
                            continue;
                        }

                        if (pm[2] != null) {
                            _port.PORTINFO = pm[2].toString();
                        }

                        if (pm[3] != null) {
                            _port.GETWAY = pm[3].toString();
                        }

                        if (pm[5] != null) {
                            _port.IFINOCTETS = pm[5].toString();
                        }

                        if (pm[7] != null) {
                            _port.IFOUTOCTETS = pm[7].toString();

                        }

                        if (pm[9] != null) {
                            _port.IFINOCTETSBPS = pm[9].toString();
                        }

                        if (pm[10] != null) {
                            _port.IFOUTOCTETSBPS = pm[10].toString();
                        }

                        list.add(_port);
                    } else {
                        break;
                    }

                }
            }
            pm = null;
            reader.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(filename);
            e.printStackTrace();
            log.info("出现异常：" + e.getMessage().toString() + "#" + filename);
        }
        return list;
    }

    /**
     * 处理文件名包含wuxi的数据 读取字段，并存放在list中
     *
     * @param filename
     * @return
     */
    public static List PortList_wx(org.apache.log4j.Logger log, String filename,Hashtable lst_p2) {
        List list = new ArrayList();
        try {
            //log.info("################"+filename);  
            
            FileReader fw = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fw);
            String str = null;
            fun.port2 _port = new fun.port2();
            while ((str = reader.readLine()) != null) {
                if (str.length() > 0) {
                    str = str.replace("@|#\\#", "###");
                    String[] pms = str.split("###");
                    str = null;
                    for (int i = 0, m = pms.length; i < m; i++) {
                        _port = new fun.port2();
                        String _mes = pms[i].toString();
                        if (_mes != null) {
                            if (_mes.length() > 0) {
                                _mes = _mes.replace("$||#", "###");
                                String[] pm2 = _mes.split("###");
                                if (pm2.length > 0) {

                                    if (pm2[0] != null) {
                                        _port.DATETIME = pm2[0].toString();
                                    }

                                    if (pm2[1] != null) {
                                        _port.DEVICEID = pm2[1].toString();
                                        //找不到device_id则跳转下一循环
                                        if (!lst_p2.containsKey(_port.DEVICEID)) {
                                            //log.info("deviceid_id不存在:" + _port.DEVICEID);
                                            continue;
                                        } else {
                                            // log.info("deviceid_id存在:" + key);
                                        }
                                    }

                                    if (pm2[2] != null) {
                                        _port.PORTINFO = pm2[2].toString().replace("'", "");
                                    }

                                    if (pm2[3] != null) {
                                        _port.GETWAY = pm2[3].toString();
                                    }

                                    if (pm2[4] != null) {
                                        String _data_s = pm2[4].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINOCTETS = _data_s;
                                        }
                                    }

                                    if (pm2[5] != null) {
                                        String _data_s = pm2[5].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTOCTETS = _data_s;
                                        }
                                    }

                                    if (pm2[6] != null) {
                                        String _data_s = pm2[6].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINERRORS = _data_s;
                                        }
                                    }

                                    if (pm2[7] != null) {
                                        String _data_s = pm2[7].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTERRORS = _data_s;
                                        }
                                    }

                                    if (pm2[8] != null) {
                                        String _data_s = pm2[8].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINDISCARDS = _data_s;
                                        }
                                    }

                                    if (pm2[9] != null) {
                                        String _data_s = pm2[9].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTDISCARDS = _data_s;
                                        }
                                    }

                                    if (pm2[10] != null) {
                                        String _data_s = pm2[10].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINUCASTPKTS = _data_s;
                                        }
                                    }

                                    if (pm2[11] != null) {
                                        String _data_s = pm2[11].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTUCASTPKTS = _data_s;
                                        }
                                    }

                                    if (pm2[12] != null) {
                                        String _data_s = pm2[12].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINNUCASTPKTS = _data_s;
                                        }
                                    }

                                    if (pm2[13] != null) {
                                        String _data_s = pm2[13].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTNUCASTPKTS = _data_s;
                                        }
                                    }

                                    if (pm2[14] != null) {
                                        String _data_s = pm2[14].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINUNKNOWNPROTOS = _data_s;
                                        }
                                    }

                                    if (pm2[15] != null) {
                                        String _data_s = pm2[15].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTQLEN = _data_s;
                                        }
                                    }

                                    if (pm2[16] != null) {
                                        String _data_s = pm2[16].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINOCTETSBPS = _data_s;
                                        }
                                    }

                                    if (pm2[17] != null) {
                                        String _data_s = pm2[17].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTOCTETSBPS = _data_s;
                                        }
                                    }

                                    if (pm2[18] != null) {
                                        String _data_s = pm2[18].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINERRORSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[19] != null) {
                                        String _data_s = pm2[19].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTERRORSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[20] != null) {
                                        String _data_s = pm2[20].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINDISCARDSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[21] != null) {
                                        String _data_s = pm2[21].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTDISCARDSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[22] != null) {
                                        String _data_s = pm2[22].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINUCASTPKTSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[23] != null) {
                                        String _data_s = pm2[23].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTUCASTPKTSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[24] != null) {
                                        String _data_s = pm2[24].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINNUCASTPKTSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[25] != null) {
                                        String _data_s = pm2[25].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTNUCASTPKTSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[26] != null) {
                                        String _data_s = pm2[26].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFINUNKNOWNPROTOSPPS = _data_s;
                                        }
                                    }

                                    if (pm2[27] != null) {
                                        String _data_s = pm2[27].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.IFOUTQLENPPS = _data_s;
                                        }
                                    }

                                    if (pm2[28] != null) {
                                        String _data_s = pm2[28].toString().trim();
                                        if (_data_s.length() > 0) {
                                            _port.DELTATIME = _data_s;
                                        }
                                    }
                                    list.add(_port);
                                }
                                pm2 = null;
                            }
                        }
                    }

                    //log.info("################"+pm);
                }
            }
            reader.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(filename);
            e.printStackTrace();
            log.info("出现异常：" + e.getMessage().toString() + "#" + filename);
        }
        return list;
    }

    /**
     * 最后一种情况
     *
     * @param filename
     * @return
     */
    public static List PortList(org.apache.log4j.Logger log,String filename,Hashtable lst_p2) {
        List list = new ArrayList();
        try {
            FileReader fw = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fw);
            String str = null;
            fun.port2 _port = new fun.port2();

            String[] pm = null;
            while ((str = reader.readLine()) != null && str.trim().length() > 0) {
                _port = new fun.port2();
                pm = null;

                if (str.length() > 0) {
                    try {
                        pm = str.split("\\$");
                        str = null;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info(str);
                    }
                    if (pm.length > 0) {

                        if (pm[0] != null) {
                            _port.DATETIME = pm[0].toString();
                        }

                        if (pm[1] != null) {
                            _port.DEVICEID = pm[1].toString();

                            //找不到device_id则跳转下一循环
                            if (!lst_p2.containsKey(_port.DEVICEID)) {
                                //log.info("deviceid_id不存在:" + _port.DEVICEID);
                                continue;
                            } else {
                                //对 onu的数据进行过滤
                                if (_port.DEVICEID.equals("191054")
                                        || _port.DEVICEID.equals("2486722")
                                        || _port.DEVICEID.equals("41066900")
                                        || _port.DEVICEID.equals("128690")
                                        || _port.DEVICEID.equals("128654")
                                        || _port.DEVICEID.equals("41066937")
                                        || _port.DEVICEID.equals("41962504")
                                        || _port.DEVICEID.equals("47353458")
                                        || _port.DEVICEID.equals("2458653")) {
                                    continue;
                                }
                            }
                        }

                        if (pm[2] != null) {
                            _port.PORTINFO = pm[2].toString().replace("'", "");
                        }

                        if (pm[3] != null) {
                            _port.GETWAY = pm[3].toString();
                        }

                        if (pm[4] != null) {
                            String _data_s = pm[4].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINOCTETS = _data_s;
                            }
                        }

                        if (pm[5] != null) {
                            String _data_s = pm[5].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTOCTETS = _data_s;
                            }
                        }

                        if (pm[6] != null) {
                            String _data_s = pm[6].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINERRORS = _data_s;
                            }
                        }

                        if (pm[7] != null) {
                            String _data_s = pm[7].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTERRORS = _data_s;
                            }
                        }

                        if (pm[8] != null) {
                            String _data_s = pm[8].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINDISCARDS = _data_s;
                            }
                        }

                        if (pm[9] != null) {
                            String _data_s = pm[9].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTDISCARDS = _data_s;
                            }
                        }

                        if (pm[10] != null) {
                            String _data_s = pm[10].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINUCASTPKTS = _data_s;
                            }
                        }

                        if (pm[11] != null) {
                            String _data_s = pm[11].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTUCASTPKTS = _data_s;
                            }
                        }

                        if (pm[12] != null) {
                            String _data_s = pm[12].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINNUCASTPKTS = _data_s;
                            }
                        }

                        if (pm[13] != null) {
                            String _data_s = pm[13].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTNUCASTPKTS = _data_s;
                            }
                        }

                        if (pm[14] != null) {
                            String _data_s = pm[14].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINUNKNOWNPROTOS = _data_s;
                            }
                        }

                        if (pm[15] != null) {
                            String _data_s = pm[15].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTQLEN = _data_s;
                            }
                        }

                        if (pm[16] != null) {
                            String _data_s = pm[16].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINOCTETSBPS = _data_s;
                            }
                        }

                        if (pm[17] != null) {
                            String _data_s = pm[17].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTOCTETSBPS = _data_s;
                            }
                        }

                        if (pm[18] != null) {
                            String _data_s = pm[18].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINERRORSPPS = _data_s;
                            }
                        }

                        if (pm[19] != null) {
                            String _data_s = pm[19].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTERRORSPPS = _data_s;
                            }
                        }

                        if (pm[20] != null) {
                            String _data_s = pm[20].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINDISCARDSPPS = _data_s;
                            }
                        }

                        if (pm[21] != null) {
                            String _data_s = pm[21].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTDISCARDSPPS = _data_s;
                            }
                        }

                        if (pm[22] != null) {
                            String _data_s = pm[22].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINUCASTPKTSPPS = _data_s;
                            }
                        }

                        if (pm[23] != null) {
                            String _data_s = pm[23].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTUCASTPKTSPPS = _data_s;
                            }
                        }

                        if (pm[24] != null) {
                            String _data_s = pm[24].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINNUCASTPKTSPPS = _data_s;
                            }
                        }

                        if (pm[25] != null) {
                            String _data_s = pm[25].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTNUCASTPKTSPPS = _data_s;
                            }
                        }

                        if (pm[26] != null) {
                            String _data_s = pm[26].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFINUNKNOWNPROTOSPPS = _data_s;
                            }
                        }

                        if (pm[27] != null) {
                            String _data_s = pm[27].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.IFOUTQLENPPS = _data_s;
                            }
                        }

                        if (pm[28] != null) {
                            String _data_s = pm[28].toString().trim();
                            if (_data_s.length() > 0) {
                                _port.DELTATIME = _data_s;
                            }
                        }
                        list.add(_port);
                    }
                    pm = null;
                }
            }
            pm = null;
            reader.close();
            fw.close();
        } catch (Exception e) {
            System.out.println(filename);
            e.printStackTrace();
            log.info("出现异常：" + e.getMessage().toString() + "#" + filename);
        }
        return list;
    }

    /**
     *
     */
    public static class port {

        public String str1 = "";
        public String str2 = "";
        public String str3 = "";
        public String str4 = "";
        public String str5 = "";
        public String str6 = "";
        public String str7 = "";
        public String str8 = "";
        public String str9 = "";
        public String str10 = "";
        public String str11 = "";
        public String str12 = "";
        public String str13 = "";
        public String str14 = "";
        public String str15 = "";
        public String str16 = "";
        public String str17 = "";
        public String str18 = "";
        public String str19 = "";
        public String str20 = "";
        public String str21 = "";
        public String str22 = "";
        public String str23 = "";
        public String str24 = "";
        public String str25 = "";
        public String str26 = "";
        public String str27 = "";
        public String str28 = "";
        public String str29 = "";

    }

    public static class port2 {

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

    }

}
