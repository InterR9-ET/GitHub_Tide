/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.pro;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {
    private static util.GetThread.thread _thread = new util.GetThread.thread(6);
    private static util.GetTools.getPro _pro = new util.GetTools.getPro();
    private static util.GetSql.donghuan_mysql _donghuan_mysql = new util.GetSql.donghuan_mysql();

    public static String str_sms = "";
    public static String str_file_url = "";
    public static List people_list = new ArrayList();

    public static void jc_doing(org.apache.log4j.Logger log, util.GetSql.csnms _csnms) {
        str_sms = "";
        try {
            // 初始化
            db.ini(log, _csnms);
            // 加载启动文件
            List sh_file_list = load_sh_file();
            // --------------------------添加任务------------------------//
            _thread.execute(task_15_min(sh_file_list, log));// 15分钟轮询任务
            // --------------------------添加任务------------------------//
        } catch (Exception e) {
            e.printStackTrace();
            log.info("线程执行:" + e.getMessage());
        } finally {
            // ------------------等待线程运行---------------//
            _thread.waitFinish(); // 等待所有任务执行完毕
            _thread.closePool(); // 关闭线程池
            // ------------------等待线程运行---------------//
        }
    }

    //-----------------------------加载文件---------------------------------------------
    public static List load_sh_file() {
        // 加载文件
        List sh_file_list = new ArrayList();
        File file1 = new File(".");// 取当前文件夹
        try {
            String url1 = file1.getCanonicalPath(); // 得到的是C:\test
            String url2 = file1.getAbsolutePath(); // 得到的是C:\test\.
            String url3 = file1.getPath(); // 得到的是.
            str_file_url = url1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        File[] files1 = file1.listFiles();
        if (files1 != null) {
            if (files1.length > 0) {
                Arrays.sort(files1);// 排序
                for (int kk = 0; kk < files1.length; kk++) {
                    if (!files1[kk].isDirectory()) {
                        String fil = files1[kk].toString();
                        if (fil.indexOf(".sh") != -1) {
                            sh_file_list.add(fil);
                            // System.out.println(fil);
                        }
                    }
                }
            }
        }

        return sh_file_list;
    }

    //----------------------------任执行务---------------------------------------
    private static Runnable task_15_min(final List sh_file_list, final org.apache.log4j.Logger log) {
        return new Runnable() {
            public void run() {

                while (true) {
                    int _time = 15;
                    log.info("######" + _time + "分钟任务begin");
                    // 检查atm性能采集
                    try {
                        log.info("--------检查atm性能采集");
                        jc_atm(sh_file_list, log);
                        log.info("--------完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("检查atm性能采集:ERROR:" + e.getMessage());
                    }

                    // 检测PING性能文件
                    try {
                        log.info("--------检测PING性能文件");
                        jc_ping_file(sh_file_list, log);//++++ ++++ +++
                        log.info("--------完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("检测PING性能文件:ERROR:" + e.getMessage());
                    }

                    // 检测PORT性能文件
                    try {
                        log.info("--------检测PORT性能文件");
                        jc_port_file(sh_file_list, log);
                        log.info("--------完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("检测PORT性能文件:ERROR:" + e.getMessage());
                    }

                    // 检测NODE性能文件
                    try {
                        log.info("--------检测NODE性能文件");
                        jc_node_file(sh_file_list, log);
                        log.info("--------完成");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("检测NODE性能文件:ERROR:" + e.getMessage());
                    }

                    try {
                        log.info("######" + _time + "分钟任务end");
                        Thread.sleep(1000 * 60 * _time);// 延时
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    //----------------------------ping---------------------------------------
    public static void jc_ping_file(List sh_file_list, org.apache.log4j.Logger log) {
        // 总文件
        List File_list = new ArrayList();

        // 加载文件
        File file1 = new File("pingper/");
        File[] files1 = file1.listFiles();
        if (files1 != null) {
            if (files1.length > 0) {
                Arrays.sort(files1);// 排序
                for (int kk = 0; kk < files1.length; kk++) {
                    if (!files1[kk].isDirectory()) {
                        if (files1[kk].toString().indexOf("ping_data") != -1) {
                            String fil = files1[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        File file2 = new File("pingper/dcn/");
        File[] files2 = file2.listFiles();
        if (files2 != null) {
            if (files2.length > 0) {
                Arrays.sort(files2);// 排序
                for (int kk = 0; kk < files2.length; kk++) {
                    if (!files2[kk].isDirectory()) {
                        if (files2[kk].toString().indexOf("ping_data") != -1) {
                            String fil = files2[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        File file3 = new File("pingper/wx/");
        File[] files3 = file3.listFiles();
        if (files3 != null) {
            if (files3.length > 0) {
                Arrays.sort(files3);// 排序
                for (int kk = 0; kk < files3.length; kk++) {
                    if (!files3[kk].isDirectory()) {
                        if (files3[kk].toString().indexOf("ping_data") != -1) {
                            String fil = files3[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        File file4 = new File("pingper/lyg/");
        File[] files4 = file4.listFiles();
        if (files4 != null) {
            if (files4.length > 0) {
                Arrays.sort(files4);// 排序
                for (int kk = 0; kk < files4.length; kk++) {
                    if (!files4[kk].isDirectory()) {
                        if (files4[kk].toString().indexOf("ping_data") != -1) {
                            String fil = files4[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        File file5 = new File("pingper/jyw/");
        File[] files5 = file5.listFiles();
        if (files5 != null) {
            if (files5.length > 0) {
                Arrays.sort(files5);// 排序
                for (int kk = 0; kk < files5.length; kk++) {
                    if (!files5[kk].isDirectory()) {
                        if (files5[kk].toString().indexOf("ping_data") != -1) {
                            String fil = files5[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        File file6 = new File("pingper/sz/");
        File[] files6 = file6.listFiles();
        if (files6 != null) {
            if (files6.length > 0) {
                Arrays.sort(files6);// 排序
                for (int kk = 0; kk < files6.length; kk++) {
                    if (!files6[kk].isDirectory()) {
                        if (files6[kk].toString().indexOf("ping_data") != -1) {
                            String fil = files6[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        if (File_list.size() > 30) {
            if (File_list.size() > 50) {
                if (str_sms.trim().length() > 0) {
                    str_sms = str_sms + "," + "[PINGPER]中文件积压";
                    log.info(str_sms);
                } else {
                    str_sms = str_sms + "[PINGPER]中文件积压";
                    log.info(str_sms);
                }
            }
            // 重启进程
            if (return_sh_file("ping.sh", sh_file_list, log)) {
                String _url = str_file_url + "/" + "ping.sh" + " &";
                log.info("重启进程，run :" + _url);
                _pro.Pro_Start(_url);
            }
        }

    }

    //----------------------------判断启动文件名是否正确---------------------------------------
    public static boolean return_sh_file(String str_file, List sh_file_list, org.apache.log4j.Logger log) {
        boolean _bs = false;
        for (int i = 0, m = sh_file_list.size(); i < m; i++) {
            String _file1 = sh_file_list.get(i).toString();
            if (_file1.indexOf(str_file) > -1) {
                _bs = true;
                break;
            }
        }

        if (!_bs) {
            log.info("找不到[" + str_file + "]文件");
        }

        return _bs;
    }

    //----------------------------porter---------------------------------------
    public static void jc_port_file(List sh_file_list, org.apache.log4j.Logger log) {
        // 总文件
        List File_list = new ArrayList();

        // 加载文件
        File file1 = new File("portper/");
        File[] files1 = file1.listFiles();
        if (files1 != null) {
            if (files1.length > 0) {
                Arrays.sort(files1);// 排序
                for (int kk = 0; kk < files1.length; kk++) {
                    if (!files1[kk].isDirectory()) {
                        if (files1[kk].toString().indexOf("flux_data") != -1) {
                            String fil = files1[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        System.out.println(File_list.size());
        log.info("[porter总文件数：]"+File_list.size());
        if (File_list.size() > 50) {
            if (File_list.size() > 60) {
                if (str_sms.trim().length() > 0) {
                    str_sms = str_sms + "," + "[PORTPER]中文件积压";
                } else {
                    str_sms = str_sms + "[PORTPER]中文件积压";
                }
            }
            // 重启进程
            if (return_sh_file("run_xn_port.sh", sh_file_list, log)) {
                String _url = str_file_url + "/" + "run_xn_port.sh" + " &";
                log.info("run :" + _url);
                _pro.Pro_Start(_url);
            }
        }

    }

    //----------------------------node---------------------------------------
    public static void jc_node_file(List sh_file_list, org.apache.log4j.Logger log) {
        // 总文件
        List File_list = new ArrayList();

        // 加载文件
        File file1 = new File("nodeper/");
        File[] files1 = file1.listFiles();
        if (files1 != null) {
            if (files1.length > 0) {
                Arrays.sort(files1);// 排序
                for (int kk = 0; kk < files1.length; kk++) {
                    if (!files1[kk].isDirectory()) {
                        if (files1[kk].toString().indexOf("pm_raw") != -1) {
                            String fil = files1[kk].toString();
                            File_list.add(fil);
                        }
                    }
                }
            }
        }

        if (File_list.size() > 20) {
            if (File_list.size() > 40) {
                if (str_sms.trim().length() > 0) {
                    str_sms = str_sms + "," + "[NODEPER]中文件积压";
                } else {
                    str_sms = str_sms + "[NODEPER]中文件积压";
                }
            }
            // 重启进程
            if (return_sh_file("node.sh", sh_file_list, log)) {
                String _url = str_file_url + "/" + "node.sh" + " &";
                log.info("run :" + _url);
                _pro.Pro_Start(_url);
            }
        }

    }

    //----------------------------atm---------------------------------------
    public static void jc_atm(List sh_file_list, org.apache.log4j.Logger log) {

        String str_sql = "select  count(1) as MAX_COUNT from atmif_performance  s ";

        List _list = new ArrayList();
        try {
            _list = _donghuan_mysql.getdata(str_sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (_list.size() > 0) {
            if (_list.get(0) != null) {
                HashMap map = (HashMap) _list.get(0);
                String _count = map.get("MAX_COUNT").toString();
                if (_count.length() > 0) {
                    long l = Long.parseLong(_count);
                    if (l > 500) {

                        if (l > 2000) {
                            if (str_sms.trim().length() > 0) {
                                str_sms = str_sms + "," + "[atm]性能数据积压";
                            } else {
                                str_sms = str_sms + "[atm]性能数据积压";
                            }
                        }

                        // 重启进程
                        if (return_sh_file("atm.sh", sh_file_list, log)) {
                            String _url = str_file_url + "/" + "atm.sh" + " &";
                            log.info("run :" + _url);
                            _pro.Pro_Start(_url);
                        }
                    }
                }
            }
        }

    }

}
