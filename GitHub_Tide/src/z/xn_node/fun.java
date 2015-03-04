/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import z.xn_port.*;
import z.send_sms.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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

    public static Hashtable keyHash = new Hashtable();
    public static Hashtable keyHash2 = new Hashtable();

    private static util.GetFile.excel _excel = new util.GetFile.excel();
    private static util.GetTools.tools _tools = new util.GetTools.tools();

    /**
     * @param fil-----------------------------------------------------文件名
     * @param log-----------------------------------------------------日志
     * @param _csnms--------------------------------------------------数据库连接
     * @return 
     */
    //--------------------------------------------------------------------------
    public static Runnable createTask(final String fil, final org.apache.log4j.Logger log, final util.GetSql.csnms _csnms) {
        return new Runnable() {
            public void run() {
                try {
                    List _list = new ArrayList();//明细内容
                    List list_in = new ArrayList();

                    List list_cpu_in = new ArrayList();
                    List list_mem_in = new ArrayList();
                    List list_tem_in = new ArrayList();

                    List list_cpu_up = new ArrayList();
                    List list_mem_up = new ArrayList();
                    List list_tem_up = new ArrayList();

                    //进行
                    log.info("开始处理文件:" + fil);

                    if (fil.indexOf("edu") != -1 || fil.indexOf("dcn") != -1) {
                        _list = NodeList_edu(fil);
                    } else {
                        _list = NodeList(fil);
                    }

                    if (_list.size() > 0) {
                        log.info("得到内容");
                        strclass.nodein _nodein_s = new strclass.nodein();
                        List _new = new ArrayList();
                        int ps = 0;
                        for (int i = 0, m = _list.size(); i < m; i++) {
                            ps = ps + 1;
                            if (ps == 5000) {
                                log.info("重新赋值：" + (i + 1) + "/" + m);
                                ps = 0;
                            }

                            strclass.node _node = new strclass.node();
                            _node = (strclass.node) _list.get(i);

                            _nodein_s = get_data3(_node);
                            Thread.sleep(5);

                            String kk = _nodein_s.LONGID.toString().trim();

                            if (kk.length() == 0) {
                                continue;
                            }
                            _new.add(_nodein_s);
                        }

                        System.out.println("size:" + _new.size());

                        if (_new.size() > 0) {
                            //JSHPFMOTHER_NODE  写入数据
                            db.ycl_ini(_new, _csnms, log);

                            //处理其它数据
                            for (int i = 0, m = _new.size(); i < m; i++) {
                                //System.out.println("处理进度：" + (i + 1) + "/" + m);
                                log.info("处理进度：" + (i + 1) + "/" + m);
                                _nodein_s = new strclass.nodein();
                                _nodein_s = (strclass.nodein) _new.get(i);
                                if (_nodein_s.NODEID.length() > 0) {
                                    //cpu  内存 温度
                                    retun_date(_nodein_s, list_cpu_in, list_mem_in, list_tem_in, list_cpu_up, list_mem_up, list_tem_up);
                                }
                            }
                        }

                        _new.clear();
                    }else{
                        log.info("没有数据");
                    }

                    if (list_cpu_in.size() > 0) {
                        db.ycl_ini_cpu("in", list_cpu_in,log,_csnms);
                    }

                    if (list_mem_in.size() > 0) {
                        db.ycl_ini_mem("in", list_mem_in,log,_csnms);
                    }

                    if (list_tem_in.size() > 0) {
                        db.ycl_ini_tem("in", list_tem_in,log,_csnms);
                    }

                    if (list_cpu_up.size() > 0) {
                        db.ycl_ini_cpu("up", list_cpu_up,log,_csnms);
                    }

                    if (list_mem_up.size() > 0) {
                        db.ycl_ini_mem("up", list_mem_up,log,_csnms);
                    }

                    if (list_tem_up.size() > 0) {
                        db.ycl_ini_tem("up", list_tem_up,log,_csnms);
                    }

                    _list.clear();
                    list_in.clear();

                    //删除文件
                    File ff = new File(fil);
                    //---------copy文件----------------//
                    String file_name = ff.getName();
                    File directory = new File("");// 设定为当前文件夹
                    if (fil.indexOf("nodeper") != -1 && fil.indexOf("dcn") != -1) {
                        file_name = file_name + "_dcn";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/node/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_dcn");//删除多余文件
                    } else if (fil.indexOf("nodeper") != -1 && fil.indexOf("huanbao") != -1) {
                        file_name = file_name + "_huanbao";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/node/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_huanbao");//删除多余文件
                    } else if (fil.indexOf("nodeper") != -1 && fil.indexOf("nanjing") != -1) {
                        file_name = file_name + "_nanjing";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/node/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_nanjing");//删除多余文件
                    } else if (fil.indexOf("nodeper") != -1 && fil.indexOf("edu") != -1) {
                        file_name = file_name + "_edu";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/node/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_edu");//删除多余文件
                    }
                    //-------------------------//
                    Thread.sleep(50);
                    ff.delete();
                    Thread.sleep(50);

                    log.info("删除文件:" + fil + _tools.systime_prase_string(""));

                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.info("多线程异常:" + ex.getMessage().toString());
                }
            }

            public void del_files(String str_type) {
                //str_type   _sz   _jyw   _lyg   _wx  _dcn   _nj
                File file = new File("file//xingneng/node/");
                File[] files = file.listFiles();
                List _list = new ArrayList();
                if (files != null) {
                    if (files.length > 0) {
                        // Arrays.sort(files);//排序
                        //按日期排序
                        Arrays.sort(files, new Comparator<File>() {
                            public int compare(File f1, File f2) {
                                long diff = f1.lastModified() - f2.lastModified();
                                if (diff > 0) {
                                    return 1;
                                } else if (diff == 0) {
                                    return 0;
                                } else {
                                    return -1;
                                }
                            }

                            public boolean equals(Object obj) {
                                return true;
                            }
                        });
                        for (int kk = 0; kk < files.length; kk++) {
                            if (!files[kk].isDirectory()) {
                                String str_mes = files[kk].toString();
                                if (str_mes.indexOf("bulk") != -1 && str_mes.indexOf(str_type) != -1) {
                                    _list.add(str_mes);
                                }
                            }
                        }
                    }
                }

                if (_list.size() > 10) {
                    for (int i = 0, m = _list.size(); i < m - 10; i++) {
                        String fil = _list.get(i).toString();
                        File ff = new File(fil);
                        ff.delete();
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {

                        }
                    }
                }
            }
        };
    }

    /**
     * @param filename----------------------------------------------------
     * @return 
     */
    //--------------------------------------------------------------------------
    public static List NodeList_edu(String filename) {
        List list = new ArrayList();
        try {
            FileReader fw = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fw);
            String str = null;
            int row = 1;
            String pms[] = null;

            Hashtable keyHash = new Hashtable();

            while ((str = reader.readLine()) != null) {
                str = str.replace("@|#\\#", "_");
                String[] pm = str.split("_");

                for (int i = 0; i < pm.length; i++) {
                    strclass.node _node = new strclass.node();
                    String mess = pm[i].toString();
                    mess = mess.replace("$||#", "_");
                    String[] pm2 = mess.split("_");
                    if (pm2.length > 0) {
                        try {
                            _node.str1 = pm2[0].toString();
                            _node.str2 = pm2[1].toString();
                            _node.str3 = pm2[2].toString();
                            _node.str4 = pm2[3].toString();

                            //---------过滤重复的---------//
                            String key = _node.str1 + "#" + _node.str2;
                            if (!keyHash.containsKey(key)) {
                                keyHash.put(key, key);
                            } else {
                                continue;
                            }

                            //System.out.println(_node.str1 + "#" + _node.str2 + "#" + _node.str3 + "#" + _node.str4 );
                            list.add(_node);
                        } catch (Exception e) {
                            System.out.println("###########" + str + "###########");
                        }
                    }

                }

            }
            reader.close();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //--------------------------------------------------------------------------
    public static List NodeList(String filename) {
        //处理node文件   pm    000000000000000009900701460001$1390706400$31.00$900
        List list = new ArrayList();
        try {
            FileReader fw = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fw);
            String str = null;
            int row = 1;
            String pms[] = null;

            Hashtable keyHash = new Hashtable();

            while ((str = reader.readLine()) != null) {
                strclass.node _node = new strclass.node();
                String[] pm = str.split("\\$");
                if (pm.length > 0) {
                    try {
                        _node.str1 = pm[0].toString();
                        _node.str2 = pm[1].toString();
                        _node.str3 = pm[2].toString();
                        _node.str4 = pm[3].toString();

                        //---------过滤重复的---------//
                        String key = _node.str1 + "#" + _node.str2;
                        if (!keyHash.containsKey(key)) {
                            keyHash.put(key, key);
                        } else {
                            continue;
                        }
                        list.add(_node);
                    } catch (Exception e) {
                        System.out.println("###########" + str + "###########");
                    }
                }
            }
            reader.close();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //--------------------------------------------------------------------------
    public static strclass.nodein get_data3(strclass.node _node) {
        //检查设备id是否存在      
        strclass.nodein _nodein = new strclass.nodein();
        if (_node.str1.length() > 0) {
            if (keyHash.containsKey(_node.str1)) {
                String str_value = keyHash.get(_node.str1).toString();
                boolean _bs2 = false;
                if (str_value.length() > 0) {
                    // _ID + "##" + _EXPRESSIONID + "##" + _DEVICE_ID + "##" + _NAME + "##" + _MES + "##" + _NODE_ID;
                    //  _ID + "##" + _EXPRESSIONID + "##" + _DEVICE_ID +"##" + _NAME + "##" + _MES + "##" + _NODE_ID;
                    String[] valuess = str_value.split("##");
                    _nodein.OBJNO = "1";
                    String _ID = valuess[0].toString();
                    _nodein.DEVICEID = valuess[2].toString();
                    String _NAME = valuess[3].toString();
                    String _MES = valuess[4].toString();
                    _nodein.NODEID = valuess[5].toString();

                    _nodein.TYPENAME = _MES + " " + _NAME;
                    //类型为 S9306的设备只保留LPU跟MPU的数据
                    if (_nodein.TYPENAME.indexOf("S93") != -1) {
                        if (_nodein.TYPENAME.indexOf("LPU") == -1 && _nodein.TYPENAME.indexOf("MPU") == -1) {
                            return _nodein;
                        }
                    }

                    if (_nodein.TYPENAME.indexOf("内存") != -1) {
                        String beginDate = _node.str2 + "000";
                        String sd = getDateTimeByMillisecond(beginDate);
                        _nodein.GETTIME = sd;
                        _nodein.VALUE = _node.str3.replace(".00", "").toString();
                        _nodein.TYPE = "1003";
                        _nodein.SIZES = "1";
                        _nodein.LONGID = _ID;
                        _bs2 = true;
                    } else if (_nodein.TYPENAME.indexOf("CPU") != -1) {
                        String beginDate = _node.str2 + "000";
                        String sd = getDateTimeByMillisecond(beginDate);
                        _nodein.GETTIME = sd;
                        _nodein.VALUE = _node.str3.replace(".00", "").toString();
                        _nodein.TYPE = "1002";
                        _nodein.SIZES = "1";
                        _nodein.LONGID = _ID;
                        _bs2 = true;
                    } else if (_nodein.TYPENAME.indexOf("温度") != -1) {
                        String beginDate = _node.str2 + "000";
                        String sd = getDateTimeByMillisecond(beginDate);
                        _nodein.GETTIME = sd;
                        _nodein.VALUE = _node.str3.replace(".00", "").toString();
                        _nodein.TYPE = "1006";
                        _nodein.SIZES = "1";
                        _nodein.LONGID = _ID;
                        _bs2 = true;
                    }
                }
                if (_bs2) {
                    return _nodein;
                }
            }
        }
        return _nodein;
    }

    //--------------------------------------------------------------------------
    public static boolean ishaslongid(String longid) {
        boolean _bs = false;
        if (keyHash2.containsKey(longid)) {
            _bs = true;
        }
        return _bs;
    }

    //--------------------------------------------------------------------------
    public static void retun_date(strclass.nodein _nodein, List list_cpu_in, List list_mem_in, List list_tem_in, List list_cpu_up, List list_mem_up, List list_tem_up) {

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

    /**
     * 毫秒转换为时间
     */
    //--------------------------------------------------------------------------
    public static String getDateTimeByMillisecond(String str) {
        Date date = new Date(Long.valueOf(str));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

}
