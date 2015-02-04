/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_ping;

import java.io.*;
import java.util.*;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {
    
    private static util.GetFile.excel _excel = new util.GetFile.excel();
       /**
     * 获取下载好的信息
     *返回List文件名
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
    //------------------------------------------------------------------------------
    /**
     * 
     * @param log--------------日志
     * @param File_list--------文件名
     * @param _csnms-----------数据连接
     */
    public void chulwj(org.apache.log4j.Logger log ,List File_list,util.GetSql.csnms _csnms){
        String bfpath="/file/xingneng/ping/";
        try{
            List list_in = new ArrayList();
            //文件数量大于0
            if (File_list.size() > 0) {
                ping _ping = new ping();
                for (Object File_list1 : File_list) {
                    _csnms.rush();
                    String fil = File_list1.toString(); //取出文件名
                    List list = PingList(fil);                  //传入文件名，调用筛选信息的方法，得到想要字段的信息
                    //处理数据
                    db DB=new db();                              //调用方法，将信息写入数据库
                    if (DB.ycl(_csnms,list,log)) {
                        //删除文件
                        File ff = new File(fil);                  //string将文件名转换成File
                        //---------copy文件----------------//
                        String file_name = ff.getName();
                        File directory = new File("");            // 设定为当前文件夹
                        if (fil.contains("pingper/sz/")) {        //文件名包含指定的信息
                            file_name = file_name + "_sz";        //修改文件名称
                            String _url = directory.getCanonicalPath() + bfpath + file_name;//得到文件路径
                            // System.out.println(fil+"\r\n"+_url);
                            boolean _bs_c = _excel.copy_file(fil, _url);        //拷贝文件
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            del_files("_sz");//删除多余文件
                        } else if (fil.contains("pingper/jyw/")) {
                            file_name = file_name + "_jyw";
                            String _url = directory.getCanonicalPath() + bfpath + file_name;
                            // System.out.println(fil+"\r\n"+_url);
                            boolean _bs_c = _excel.copy_file(fil, _url);
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            del_files("_jyw");//删除多余文件
                        } else if (fil.indexOf("pingper/lyg/") != -1) {
                            file_name = file_name + "_lyg";
                            String _url = directory.getCanonicalPath() + bfpath + file_name;
                            // System.out.println(fil+"\r\n"+_url);
                            boolean _bs_c = _excel.copy_file(fil, _url);
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            del_files("_lyg");//删除多余文件
                        } else if (fil.indexOf("pingper/wx/") != -1) {
                            file_name = file_name + "_wx";
                            String _url = directory.getCanonicalPath() + bfpath + file_name;
                            // System.out.println(fil+"\r\n"+_url);
                            boolean _bs_c = _excel.copy_file(fil, _url);
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            del_files("_wx");//删除多余文件
                        } else if (fil.indexOf("pingper/dcn/") != -1) {
                            file_name = file_name + "_dcn";
                            String _url = directory.getCanonicalPath() + bfpath + file_name;
                            // System.out.println(fil+"\r\n"+_url);
                            boolean _bs_c = _excel.copy_file(fil, _url);
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            del_files("_dcn");//删除多余文件
                        } else {
                            file_name = file_name + "_nj";
                            String _url = directory.getCanonicalPath() + bfpath + file_name;
                            // System.out.println(fil+"\r\n"+_url);
                            boolean _bs_c = _excel.copy_file(fil, _url);
                            if (_bs_c) {
                                log.info("Copy成功：" + _url);
                            }
                            del_files("_nj");//删除多余文件
                        }
                        //-------------------------//
                        ff.delete();
                        Thread.sleep(50);
                        log.info("删除表:" + fil);
                    }
                    //释放资源
                    list.clear();
                    list_in.clear();
                }
            }
            log.info("运行结束");
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------
    /**
     * 删除多余文件
     * @param str_type 
     */
    public void del_files(String str_type) {
        //str_type   _sz   _jyw   _lyg   _wx  _dcn   _nj
        File file = new File("file//xingneng/ping/");
        File[] files = file.listFiles();
        List _list = new ArrayList();
        if (files != null) {
            if (files.length > 0) {
                // Arrays.sort(files);//排序
                Arrays.sort(files, new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        long diff = f1.lastModified() - f2.lastModified(); //lastModified():最后一次被修改的时间的 long 值
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
                        if (str_mes.indexOf("ping_data") != -1 && str_mes.indexOf(str_type) != -1) {
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
                // System.out.println("\r\n删除文件：\r\n" + ff + "\r\n");
                ff.delete();
                try {
                    Thread.sleep(50);
                } catch (Exception e) {

                }
            }
        }
    }
    
    //------------------------------传入文件名，读取文件信息，返回得到字段的信息--------------------------------------------
    public static List PingList(String filename) {
        //处理ping文件  58.213.239.80   58.213.14.1  58.213.14.4  1.000000  1390705234
        List list = new ArrayList();
        try {
            FileReader fw = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fw);
            String str = null;
            int row = 1;
            String pms[] = null;
            ping _ping = new ping();
            while ((str = reader.readLine()) != null) {
                _ping = new ping();
                str = str.replace("   ", "  ");//-------------------------------意义何在？
                str = str.replace("  ", "$");
                String[] pm = str.split("\\$");
                //取出想要的字段
                if (pm.length > 0) {
                    try {
                        _ping.str1 = pm[0].toString();
                    } catch (Exception e) {

                    }

                    try {
                        _ping.str2 = pm[1].toString();
                    } catch (Exception e) {

                    }

                    try {
                        _ping.str3 = pm[2].toString();
                    } catch (Exception e) {
                    }
                    try {
                        _ping.str4 = pm[3].toString();
                    } catch (Exception e) {

                    }
                    try {
                        _ping.str5 = pm[4].toString();
                    } catch (Exception e) {
                    }
                }
                list.add(_ping);
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
    
    //--------------------------------------------------------------------------------------------------
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
                node _node = new node();
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
    
     //-------------------------------------------------------------------------
    public static class ping {

        public String str1 = "";
        public String str2 = "";
        public String str3 = "";
        public String str4 = "";
        public String str5 = "";

    }
    
    //--------------------------------------------------------------------------
    public static class node {
        public String str1 = "";
        public String str2 = "";
        public String str3 = "";
        public String str4 = "";
    }
    
    
    
    
}
