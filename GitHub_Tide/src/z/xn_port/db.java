/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_port;

import java.io.File;
import java.util.*;
import org.apache.log4j.Logger;

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
    private static  util.GetTools.tools tools = new util.GetTools.tools();//声明工具类
    private static util.GetFile.excel _excel = new util.GetFile.excel();
 
    public static boolean ini(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        boolean _bs = false;
        if (_csnms.load()) {
            if (_csnms.open()) {
                _bs = true;
            } else {
                log.info("csnms打开出错");
            }
        } else {
            log.info("csnms加载出错");
        }
        return _bs;
    }
     /**
     * 
     */
    public static List<Map<Object, Object>> get_device_data(util.GetSql.csnms _csnms) {
        List<Map<Object, Object>> lst = new ArrayList<Map<Object, Object>>();
        String sql = "select distinct t.loopback_ip as IP,t.device_id as DEVICEID,n.node_id as NODEID from dcn_device t,node n where n.inter_net_addr=t.loopback_ip ";
        lst  = _csnms.getdata(sql, null);
        return lst;
    }
    /**
     * 传入文件名
     * 给文件分类，并处理，返回Runnable类型
     * @param fil
     * @return
     */
    static Runnable createTask( final org.apache.log4j.Logger log,final String fil,final util.GetSql.csnms _csnms,final Hashtable lst_p2) {
        return new Runnable() {
            public void run() {
                try {
                    List list = new ArrayList();
                    try {
                    	/**
                    	 * 文件分类，分开执行
                    	 */
                        if (fil.indexOf("cus_ftth_flux") != -1) {
                        	//如果包含cus_ftth_flux，则调用PortList_ftth方法，用list接收结果集
                            list = fun.PortList_ftth(log,fil,lst_p2);
                        } else if (fil.indexOf("wuxi") != -1) {
                        	//如果包含wuxi，则调用PortList_wx方法，用list接收结果集
                            System.out.println(fil+"               *********************************");
                            list =fun.PortList_wx(log,fil,lst_p2);
                            System.out.println(list.size()+"               *********************************");
                        } else {
                        	//最后一种情况，调用PortList方法，用list接收结果集
                            list = fun.PortList(log,fil,lst_p2);
                        }
                    } catch (Exception e) {
                        System.out.println("异常#######" + fil);
                    }

                    //进行预处理
                    log.info("开始处理文件:" + fil);
                    //得到list信息，调用jiexi3_ycl方法，添加到数据库
                    System.out.println(_csnms+"0000000000000000000000000000000000000000000000");
                    
                    System.out.println(list.size()+"               0000000000000000000000000000000000000000000000");
                    jiexi3_ycl( _csnms,list);//-----------------------------------------------
                    list.clear();

                    //删除文件
                    File ff = new File(fil);

                    //---------copy文件----------------//
                    String file_name = ff.getName();
                    File directory = new File("");// 设定为当前文件夹
                    if (fil.indexOf("portper/onu") != -1 && fil.indexOf("wuxi") != -1) {
                        file_name = file_name + "_onuwuxi";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_onuwuxi");//删除多余文件
                    } else if (fil.indexOf("portper/onu") != -1 && fil.indexOf("suzhou") != -1) {
                        file_name = file_name + "_onusuzhou";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_onusuzhou");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("xuzhou") != -1) {
                        file_name = file_name + "_xuzhou";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_xuzhou");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("nanjing") != -1) {
                        file_name = file_name + "_nanjing";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_nanjing");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("huanbao") != -1) {
                        file_name = file_name + "_huanbao";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_huanbao");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("edu") != -1) {
                        file_name = file_name + "_edu";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_edu");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("dcn") != -1) {
                        file_name = file_name + "_dcn";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_dcn");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("changzhou") != -1) {
                        file_name = file_name + "_changzhou";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_changzhou");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("wuxi") != -1) {
                        file_name = file_name + "_wuxi";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_wuxi");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("nantong") != -1) {
                        file_name = file_name + "_nantong";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_nantong");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("suzhou") != -1) {
                        file_name = file_name + "_suzhou";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_suzhou");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("edu") != -1) {
                        file_name = file_name + "_edu";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_edu");//删除多余文件
                    } else if (fil.indexOf("portper/onu") == -1 && fil.indexOf("lianyungang") != -1) {
                        file_name = file_name + "_lianyungang";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_lianyungang");//删除多余文件
                    } else {
                        file_name = file_name + "_default";
                        String _url = directory.getCanonicalPath().toString() + "/file/xingneng/liuliang/" + file_name;
                        // System.out.println(fil+"\r\n"+_url);
                        boolean _bs_c = _excel.copy_file(fil, _url);
                        if (_bs_c) {
                            log.info("Copy成功：" + _url);
                        }
                        del_files("_default");//删除多余文件
                    }
                    //-------------------------//

                    ff.delete();
                    log.info("删除文件:" + fil);
                    list.clear();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    String mes = ex.getMessage().toString();
                    log.info("异常：" + mes);
                }
            }

            public void del_files(String str_type) {
                //str_type   _sz   _jyw   _lyg   _wx  _dcn   _nj
                File file = new File("file//xingneng/liuliang/");
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

            /**
             * 获取信息，并添加到数据库
             * @param _list
             */
            public void jiexi3_ycl(util.GetSql.csnms _csnms,List _list) {
                int rows = 0;
                Hashtable keyHash = new Hashtable();
                List _list_obj = new ArrayList();
                for (int i = 0, m = _list.size(); i < m; i++) {
                    System.out.println("进度：" + (i + 1) + "/" + m);
                    fun.port2 _port = new fun.port2();
                    //_port = new strings.portper.port2();
                    rows = rows + 1;

                    try {
                        _port = (fun.port2) _list.get(i);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        continue;
                    }                 

                    if (_port.DATETIME == null || _port.DATETIME.length() == 0) {
                        continue;
                    }
                    
                    String key = _port.DATETIME + "_" + _port.DEVICEID + "_" + _port.PORTINFO+"_"+_port.GETWAY.toString();
                    //System.out.println(key);

                    
                    Object[] objs = new Object[]{
                        tools.string_parse_long(_port.DATETIME.toString()),
                        _port.DEVICEID.toString(),
                        _port.PORTINFO.toString(),
                        Long.parseLong(_port.GETWAY.toString()),
                        Long.parseLong(_port.IFINOCTETS.toString()),
                        Long.parseLong(_port.IFOUTOCTETS.toString()),
                        Long.parseLong(_port.IFINERRORS.toString()),
                        Long.parseLong(_port.IFOUTERRORS.toString()),
                        Long.parseLong(_port.IFINDISCARDS.toString()),
                        Long.parseLong(_port.IFOUTDISCARDS.toString()),
                        Long.parseLong(_port.IFINUCASTPKTS.toString()),
                        Long.parseLong(_port.IFOUTUCASTPKTS.toString()),
                        Long.parseLong(_port.IFINNUCASTPKTS.toString()),
                        Long.parseLong(_port.IFOUTNUCASTPKTS.toString()),
                        Long.parseLong(_port.IFINUNKNOWNPROTOS.toString()),
                        Long.parseLong(_port.IFOUTQLEN.toString()),
                        Long.parseLong(_port.IFINOCTETSBPS.toString()),
                        Long.parseLong(_port.IFOUTOCTETSBPS.toString()),
                        Long.parseLong(_port.IFINERRORSPPS.toString()),
                        Long.parseLong(_port.IFOUTERRORSPPS.toString()),
                        Long.parseLong(_port.IFINDISCARDSPPS.toString()),
                        Long.parseLong(_port.IFOUTDISCARDSPPS.toString()),
                        Long.parseLong(_port.IFINUCASTPKTSPPS.toString()),
                        Long.parseLong(_port.IFOUTUCASTPKTSPPS.toString()),
                        Long.parseLong(_port.IFINNUCASTPKTSPPS.toString()),
                        Long.parseLong(_port.IFOUTNUCASTPKTSPPS.toString()),
                        Long.parseLong(_port.IFINUNKNOWNPROTOSPPS.toString()),
                        Long.parseLong(_port.IFOUTQLENPPS.toString()),
                        Long.parseLong(_port.DELTATIME.toString())
                    };

                    _list_obj.add(objs);
                }

                if (_list_obj.size() > 0) {
                    String str_sql = "";
                    try {
                        str_sql = "insert into switch_report_out2("
                                + "DATETIME,"
                                + "DEVICEID,"
                                + "PORTINFO,"
                                + "GETWAY,"
                                + "IFINOCTETS,"
                                + "IFOUTOCTETS,"
                                + "IFINERRORS,"
                                + "IFOUTERRORS,"
                                + "IFINDISCARDS,"
                                + "IFOUTDISCARDS,"
                                + "IFINUCASTPKTS,"
                                + "IFOUTUCASTPKTS,"
                                + "IFINNUCASTPKTS,"
                                + "IFOUTNUCASTPKTS,"
                                + "IFINUNKNOWNPROTOS,"
                                + "IFOUTQLEN,"
                                + "IFINOCTETSBPS,"
                                + "IFOUTOCTETSBPS,"
                                + "IFINERRORSPPS,"
                                + "IFOUTERRORSPPS,"
                                + "IFINDISCARDSPPS,"
                                + "IFOUTDISCARDSPPS,"
                                + "IFINUCASTPKTSPPS,"
                                + "IFOUTUCASTPKTSPPS,"
                                + "IFINNUCASTPKTSPPS,"
                                + "IFOUTNUCASTPKTSPPS,"
                                + "IFINUNKNOWNPROTOSPPS,"
                                + "IFOUTQLENPPS,"
                                + "DELTATIME"
                                + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        _csnms.execute_listobj(str_sql, _list_obj);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        log.info("提交数据异常:" + e2.getMessage().toString() + "起始行：" + rows + " SQL:\r\n" + str_sql);
                    }
                }
                keyHash.clear();
            }
        };
    }
}
