/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.alarm_hecha;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {

    private static util.GetFile.excel _excel = new util.GetFile.excel();
    private static util.GetFile.txt _txt = new util.GetFile.txt();

    public static WritableSheet get_sheet(String sheet_name, int i, WritableWorkbook W_book) {
        WritableSheet W_sheet = null;
        try {
            W_book.createSheet(sheet_name, i);
            W_sheet = W_book.getSheet(sheet_name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return W_sheet;
    }

    /**
     *
     * @param _mes------------------------------------------查找的路由信息
     * @param _csnms----------------------------------------数据库
     * @param _zhizhen--------------------------------------数据库
     * @param _ess------------------------------------------数据库
     */
    public static void com_dake(strclass.ALARM_MES _mes, util.GetSql.csnms _csnms, util.GetSql.zhizhen _zhizhen, util.GetSql.ess _ess) {

        StringBuffer _str = new StringBuffer();
        _str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\r\n");
        _str.append("<告警核查>").append("\r\n");
        _str.append("<告警信息>").append("\r\n");
        if (_mes.SE_ALARM_ID.length() > 0) {
            _str.append(get_alarmm_xml(_mes, _csnms));
        }
        _str.append("</告警信息>").append("\r\n");

        _str.append("<大客路由信息>").append("\r\n");
        _str.append(get_dk_xml(_mes, _csnms));
        _str.append("</大客路由信息>").append("\r\n");

        _str.append("<ESS电路信息>").append("\r\n");
        _str.append(get_ess_xml(_mes, _ess));
        _str.append("</ESS电路信息>").append("\r\n");

        _str.append("<直真路由信息>").append("\r\n");
        _str.append(get_zhizhen_xml(_mes, _zhizhen));
        _str.append("</直真路由信息>").append("\r\n");

        _str.append("</告警核查>").append("\r\n");

        String file_path = "D:\\com_alarm.xml";
        boolean _bs = _txt.create_file(file_path);
        if (_bs) {
            _txt.write_file(file_path, _str.toString());
            try {
                java.awt.Desktop.getDesktop().open(new File(file_path));
            } catch (Exception ex) {
                System.out.println("调用默认程序打开程序失败:" + ex.getMessage());
            }
        }

    }

    private static String get_zhizhen_xml(strclass.ALARM_MES _alarm, util.GetSql.zhizhen _zhizhen) {
        StringBuffer _str = new StringBuffer();
        List _list_dk = db.get_zhizhen_pathtrace(_alarm, _zhizhen);
        int m_dk = _list_dk.size();
        if (m_dk > 0) {
            _str.append("<路由>").append("\r\n");
            for (int i = 0; i < m_dk; i++) {
                HashMap map = (HashMap) _list_dk.get(i);

                _str.append("<SERIA ");
                _str.append(" 序号=\"");
                if (map.get("SEQUENCE") != null) {
                    _str.append(str_rel(map.get("SEQUENCE").toString()));
                }
                _str.append("\"");

                _str.append(" 电路=\"");
                if (map.get("PATH_NAME") != null) {
                    _str.append(str_rel(map.get("PATH_NAME").toString()));
                }
                _str.append("\"");
                _str.append(" >");

                _str.append("<ANODE ");
                _str.append(" 编号=\"");
                if (map.get("ANODE_ID") != null) {
                    _str.append(str_rel(map.get("ANODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ANODE_NAME") != null) {
                    _str.append(str_rel(map.get("ANODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 类型=\"");
                if (map.get("ANODE_TYPE") != null) {
                    _str.append(str_rel(map.get("ANODE_TYPE").toString()));
                }
                _str.append("\"");

                _str.append(" 信息=\"");
                if (map.get("ANODE_MES") != null) {
                    _str.append(str_rel(map.get("ANODE_MES").toString()));
                }
                _str.append("\"");
                _str.append(" />");

                _str.append("<ZNODE ");
                _str.append(" 编号=\"");
                if (map.get("ZNODE_ID") != null) {
                    _str.append(str_rel(map.get("ZNODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ZNODE_NAME") != null) {
                    _str.append(str_rel(map.get("ZNODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 类型=\"");
                if (map.get("ZNODE_TYPE") != null) {
                    _str.append(str_rel(map.get("ZNODE_TYPE").toString()));
                }
                _str.append("\"");

                _str.append(" 信息=\"");
                if (map.get("ZNODE_MES") != null) {
                    _str.append(str_rel(map.get("ZNODE_MES").toString()));
                }
                _str.append("\"");
                _str.append(" />");

                _str.append("</SERIA>").append("\r\n");
            }
            _str.append("</路由>").append("\r\n");
        }
        return _str.toString();
    }

    private static String get_ess_xml(strclass.ALARM_MES _alarm, util.GetSql.ess _ess) {
        StringBuffer _str = new StringBuffer();
        List _list_dk = db.get_ess_path(_alarm, _ess);
        int m_dk = _list_dk.size();
        if (m_dk > 0) {
            _str.append("<电路>").append("\r\n");
            for (int i = 0; i < m_dk; i++) {
                HashMap map = (HashMap) _list_dk.get(i);

                _str.append("<PATH ");
                _str.append(" 编号=\"");
                if (map.get("CIRCD") != null) {
                    _str.append(str_rel(map.get("CIRCD").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("CIRNAME") != null) {
                    _str.append(str_rel(map.get("CIRNAME").toString()));
                }
                _str.append("\"");

                _str.append(" A端客户=\"");
                if (map.get("ACUSTOMID") != null) {
                    String name = "[" + map.get("ACUSTOMID").toString() + "]";
                    if (map.get("ANAME") != null) {
                        name += map.get("ANAME").toString();
                    }
                    _str.append(str_rel(name));
                }
                _str.append("\"");

                _str.append(" Z端客户=\"");
                if (map.get("ZCUSTOMID") != null) {
                    String name = "[" + map.get("ZCUSTOMID").toString() + "]";
                    if (map.get("ZNAME") != null) {
                        name += map.get("ZNAME").toString();
                    }
                    _str.append(str_rel(name));
                }
                _str.append("\"");

                _str.append(" />").append("\r\n");
            }
            _str.append("</电路>").append("\r\n");
        }
        return _str.toString();
    }
/**
 * 
 * @param _mes---------------------------------------------------要核查的路由信息
 * @param _csnms-------------------------------------------------数据库
 * @return 
 */
    private static String get_alarmm_xml(strclass.ALARM_MES _mes, util.GetSql.csnms _csnms) {

        //与大客核查路由信息
        int node_count = db.seach_has_node(_csnms, _mes);
        if (node_count == 0) {
            _mes.NODE_MES = "大客无此设备";
        } else {
            _mes.NODE_MES = "大客匹配正常";
        }

        //设备正常  核查slot
        if (node_count > 0) {
            int slot_count = db.seach_has_pathtrace_slot(_csnms, _mes.NODE_ID, _mes.NODE_SLOT);
            if (slot_count == 0) {
                _mes.SLOT_MES = "大客未匹配到SLOT";
            } else {
                _mes.SLOT_MES = "大客匹配正常";
                //设备正常  slot正常   核查port
                int port_count = db.seach_has_pathtrace_slot_port(_csnms, _mes.NODE_ID, _mes.NODE_SLOT, _mes.NODE_PORT);
                if (port_count == 0) {
                    _mes.PORT_MES = "大客未匹配到PORT";
                } else {
                    _mes.PORT_MES = "大客匹配正常";
                }
            }
        }

        StringBuffer _str = new StringBuffer();

        _str.append("<Alarm_ID>").append(str_rel(_mes.ALARM_ID)).append("</Alarm_ID>").append("\r\n");
        _str.append("<Alarm_TYPE>").append(str_rel(_mes.ALARM_TYPE)).append("</Alarm_TYPE>").append("\r\n");
        _str.append("<DESCRIPTION>").append(str_rel(_mes.DESCRIPTION)).append("</DESCRIPTION>").append("\r\n");
        _str.append("<ALARM_START>").append(str_rel(_mes.ALARM_START)).append("</ALARM_START>").append("\r\n");
        _str.append("<ALARM_UPDATE>").append(str_rel(_mes.ALARM_UPDATE)).append("</ALARM_UPDATE>").append("\r\n");

        _str.append("<Path>").append("\r\n");
        _str.append("<PATH_ID>").append(str_rel(_mes.PATH_ID)).append("</PATH_ID>").append("\r\n");
        _str.append("<PATH_NAME>").append(str_rel(_mes.PATH_NAME)).append("</PATH_NAME>").append("\r\n");
        _str.append("</Path>").append("\r\n");

        _str.append("<NODE>").append("\r\n");
        _str.append("<NODE_ID>").append(str_rel(_mes.NODE_ID)).append("</NODE_ID>").append("\r\n");
        _str.append("<NODE_NAME>").append(str_rel(_mes.NODE_NAME)).append("</NODE_NAME>").append("\r\n");
        _str.append("<SHORT_NAME>").append(str_rel(_mes.SHORT_NAME)).append("</SHORT_NAME>").append("\r\n");

        _str.append("<NODE_SLOT>").append(str_rel(_mes.NODE_SLOT)).append("</NODE_SLOT>").append("\r\n");
        _str.append("<NODE_PORT>").append(str_rel(_mes.NODE_PORT)).append("</NODE_PORT>").append("\r\n");
        _str.append("</NODE>").append("\r\n");

        _str.append("<NODE_MES>").append(str_rel(_mes.NODE_MES)).append("</NODE_MES>").append("\r\n");
        _str.append("<SLOT_MES>").append(str_rel(_mes.SLOT_MES)).append("</SLOT_MES>").append("\r\n");
        _str.append("<PORT_MES>").append(str_rel(_mes.PORT_MES)).append("</PORT_MES>").append("\r\n");

        return _str.toString();
    }

    private static String get_dk_xml(strclass.ALARM_MES _alarm, util.GetSql.csnms _csnms) {
        StringBuffer _str = new StringBuffer();
        List _list_dk = db.get_dk_pathtrace(_alarm, _csnms);
        int m_dk = _list_dk.size();
        if (m_dk > 0) {
            _str.append("<路由>").append("\r\n");
            for (int i = 0; i < m_dk; i++) {
                HashMap map = (HashMap) _list_dk.get(i);//-------遍历每条信息，放入hashmap

                _str.append("<SERIA ");
                _str.append(" 序号=\"");
                if (map.get("SERIAL_ID") != null) {
                    _str.append(str_rel(map.get("SERIAL_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 电路编号=\"");
                if (map.get("PATH_ID") != null) {
                    _str.append(str_rel(map.get("PATH_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 电路=\"");
                if (map.get("PATH_NAME") != null) {
                    _str.append(str_rel(map.get("PATH_NAME").toString()));
                }
                _str.append("\"");
                _str.append(" >");

                _str.append("<ANODE ");
                _str.append(" 编号=\"");
                if (map.get("ANODE_ID") != null) {
                    _str.append(str_rel(map.get("ANODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ANODE_NAME") != null) {
                    _str.append(str_rel(map.get("ANODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 简称=\"");
                if (map.get("ASHORT_NAME") != null) {
                    _str.append(str_rel(map.get("ASHORT_NAME").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" SHELF_ID=\"");
                 if (map.get("ASHELF_ID") != null) {
                 _str.append(str_rel(map.get("ASHELF_ID").toString())).append("\"");
                 }*/
                _str.append(" 板卡=\"");
                if (map.get("ASLOT_ID") != null) {
                    _str.append(str_rel(map.get("ASLOT_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 端口=\"");
                if (map.get("APORT_ID") != null) {
                    _str.append(str_rel(map.get("APORT_ID").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" 时隙=\"");
                 if (map.get("ATIMESLOT") != null) {
                 _str.append(str_rel(map.get("ATIMESLOT").toString()));
                 }
                 _str.append("\"");
                 */
                _str.append(" />");

                _str.append("<ZNODE ");
                _str.append(" 编号=\"");
                if (map.get("ZNODE_ID") != null) {
                    _str.append(str_rel(map.get("ZNODE_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 名称=\"");
                if (map.get("ZNODE_NAME") != null) {
                    _str.append(str_rel(map.get("ZNODE_NAME").toString()));
                }
                _str.append("\"");

                _str.append(" 简称=\"");
                if (map.get("ZSHORT_NAME") != null) {
                    _str.append(str_rel(map.get("ZSHORT_NAME").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" SHELF_ID=\"");
                 if (map.get("ZSHELF_ID") != null) {
                 _str.append(str_rel(map.get("ZSHELF_ID").toString()));
                 }
                 _str.append("\"");
                 */
                _str.append(" 板卡=\"");
                if (map.get("ZSLOT_ID") != null) {
                    _str.append(str_rel(map.get("ZSLOT_ID").toString()));
                }
                _str.append("\"");

                _str.append(" 端口=\"");
                if (map.get("ZPORT_ID") != null) {
                    _str.append(str_rel(map.get("ZPORT_ID").toString()));
                }
                _str.append("\"");

                /*
                 _str.append(" TIMESLOT=\"");
                 if (map.get("ZTIMESLOT") != null) {
                 _str.append(str_rel(map.get("ZTIMESLOT").toString()));
                 }
                 _str.append("\"");
                 */
                _str.append("/>");

                _str.append("</SERIA>").append("\r\n");
            }
            _str.append("</路由>").append("\r\n");
        }
        return _str.toString();
    }

    private static String str_rel(String str_con) {

        boolean _bs = false;

        if (str_con.indexOf(">") != -1) {
            _bs = true;
        } else if (str_con.indexOf("<") != -1) {
            _bs = true;
        } else if (str_con.indexOf("&") != -1) {
            _bs = true;
        } else if (str_con.indexOf("\"") != -1) {
            _bs = true;
        } else if (str_con.indexOf(",") != -1) {
            _bs = true;
        }

        if (_bs) {
            str_con = "<![CDATA[" + str_con + "]]>";
        }
        /*
         str_con = str_con.replace(">", "&gt;");
         str_con = str_con.replace("<", "&lt;");
         str_con = str_con.replace("&", "&amp;");
         str_con = str_con.replace("\"", "&quot;");
         str_con = str_con.replace("'", "&apos;");
         */
        return str_con;
    }

    /**
     * @param message---------------------------------------------查询的条件信息
     * @param wg--------------------------------------------------地区
     * @param _day -----------------------------------------------查询的日期
     */
    //---------------------------------筛选log日志----------------------------------
    public void run(String message, String wg, int _day) {
        String key = "";//--------------------------------------------------???
        if (message.length() > 0) {
            //判断是否有条件
            import_data2("file/alarmhecha/" + _day + "/", message + "_" + _day + ".xls", key, message, wg);
        } else {//---------------------------------------------------------？？？
            import_data("file/alarm_log/", "data.xls", key);
        }
    }

    //---------------------------------有筛选条件的筛选log日志----------------------------------
    /**
     * @param _url------------------------------------------log日志的路径
     * @param _filename-------------------------------------查出信息的存放的文件
     * @param key-------------------------------------------
     * @param message---------------------------------------筛选条件
     * @param wg -------------------------------------------地区
     */
    public static void import_data2(String _url, String _filename, String key, String message, String wg) {
        //信息写入的文件完整路径
        String file_full_url = _url + _filename;
        //创建Excel
        boolean _bs = _excel.create_file(file_full_url);
        if (_bs) {
            try {
                WritableWorkbook W_book = null;

                /*
                 南京      无锡       常州        淮安             
                 南通      泰州       宿迁        徐州
                 盐城      扬州       镇江        苏州
                 连云港
                 */
                WritableSheet W_sheet_nj = null;

                //打开文件
                W_book = _excel.excel_W_Workbook(file_full_url);
                int maxRowCount = 50000;//设置最大的行数
                int sheet_count = 0;

                //第一个工作表被命名为“合并”,从0开始，每个工作表限制5000行
                W_sheet_nj = get_sheet("合并", sheet_count, W_book);
                sheet_count = sheet_count + 1;

                //行数
                int W_sheet_nj_rows = 0;

                //读取数据文件
                File file = new File(_url);
                File directory = new File("");// 设定为当前文件夹
                //获取路径下面的文件
                File[] files = file.listFiles();
                String fil = "";

                for (int i = 0; i < files.length; i++) {
                    if (!files[i].isDirectory()) {                       //判断是否是一个目录
                        String _url2 = _url.replace("/", "\\");
                        //String _url3 = files[i].toString();
                        //System.out.println(_url2 + "##" + _url3);
                        String file_name = files[i].toString().replace(_url2, "");
                        if (!file_name.equals(_filename)) {             //将Excel文件去除
                            fil = files[i].toString();
                            //解析log文件                       
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fil), "GBK"));
                            String str = null;
                            String str1=null;
                            int count = 0;
                            System.out.println(fil + "####");
                            strclass.ALARM_MES2 _ALARM_MES = new strclass.ALARM_MES2();
                            //逐行读取
                            while ((str = br.readLine()) != null) {
                                if(str.startsWith("INFO")&& str.contains("RelayServer (RelayServer.java:87)")){
                                    StringBuilder str2 = new StringBuilder(str);
                                    str2.replace(25, 83, "");
                                    str2.replace(0, 6, "");
                                    str1=str2.toString();
                                }
                                if (str.indexOf(">>收到告警") != -1) {
                                    str = str.toString().replace(" - --->>收到告警：", "");
                                    //System.out.println(str.toString());
                                    //判断是否存在筛选条件
                                    if (message.length() > 0) {
                                        if (str.indexOf(message) == -1) {
                                            continue;
                                        }
                                    }

                                    _ALARM_MES = new strclass.ALARM_MES2();
                                    _ALARM_MES.CXyxsj=str1.toString();
                                    _ALARM_MES.DESCRIPTION = str;
                                    _ALARM_MES.FILENAME = fil;

                                    String[] mess = str.split("\t");
                                    if (mess.length > 0) {
                                        _ALARM_MES.STARTTIME = mess[12].toString();
                                        _ALARM_MES.UPDATETIME = mess[13].toString();
                                        _ALARM_MES.TYPE = mess[14].toString();
                                        _ALARM_MES.WGNAME = mess[2].toString();
                                    }
                                    String str_wg_name = mess[2].toString();
                                    if (wg.trim().toString().length() > 0) {
                                        if (str_wg_name.indexOf(wg) == -1) {
                                            continue;
                                        }
                                    }
                                    //插完一条信息，行数加一
                                    W_sheet_nj_rows = W_sheet_nj_rows + 1;
                                    //如果行数超过设置的数量，则自动加一页表
                                    if (W_sheet_nj_rows > maxRowCount) {
                                        sheet_count = sheet_count + 1;
                                        W_sheet_nj = get_sheet("合并" + sheet_count, sheet_count, W_book);
                                        W_sheet_nj_rows = 0;
                                        W_sheet_nj_rows = W_sheet_nj_rows + 1;
                                        //调用插入数据的方法
                                        insert(W_sheet_nj, _ALARM_MES, W_sheet_nj_rows);
                                    } else {//如果没有超过设置行数，直接插入
                                        insert(W_sheet_nj, _ALARM_MES, W_sheet_nj_rows);
                                    }
                                }
                            }
                            br.close();
                        }
                    }
                }

                Thread.sleep(2000);
                W_book.write();
                W_book.close();
                System.out.println("[" + file_full_url + "]文件生成成功");

                try {
                    java.awt.Desktop.getDesktop().open(new File(file_full_url));
                } catch (Exception ex) {
                    System.out.println("调用默认程序打开程序失败:" + ex.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {

            }
        } else {
            System.out.println("[" + file_full_url + "]创建失败");
        }
    }

    //---------------------------------没有筛选条件的筛选log日志----------------------------------
    public static void import_data(String _url, String _filename, String key) {
        String file_full_url = _url + _filename;

        boolean _bs = _excel.create_file(file_full_url);
        if (_bs) {
            try {
                WritableWorkbook W_book = null;

                /*
                 南京      无锡       常州        淮安             
                 南通      泰州       宿迁        徐州
                 盐城      扬州       镇江        苏州
                 连云港
                 */
                WritableSheet W_sheet_nj = null;
                WritableSheet W_sheet_wx = null;
                WritableSheet W_sheet_cz = null;
                WritableSheet W_sheet_ha = null;
                WritableSheet W_sheet_lyg = null;
                WritableSheet W_sheet_nt = null;
                WritableSheet W_sheet_tz = null;
                WritableSheet W_sheet_sq = null;
                WritableSheet W_sheet_xz = null;
                WritableSheet W_sheet_yc = null;
                WritableSheet W_sheet_yz = null;
                WritableSheet W_sheet_zj = null;
                WritableSheet W_sheet_sz = null;

                W_book = _excel.excel_W_Workbook(file_full_url);
                int maxRowCount = 50000;//设置最大的行数
                int sheet_count = 0;

                W_sheet_nj = get_sheet("南京", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_wx = get_sheet("无锡", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_cz = get_sheet("常州", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_ha = get_sheet("淮安", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_lyg = get_sheet("连云港", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_nt = get_sheet("南通", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_tz = get_sheet("泰州", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_sq = get_sheet("宿迁", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_xz = get_sheet("徐州", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_yc = get_sheet("盐城", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_yz = get_sheet("扬州", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_zj = get_sheet("镇江", sheet_count, W_book);
                sheet_count = sheet_count + 1;
                W_sheet_sz = get_sheet("苏州", sheet_count, W_book);

                int W_sheet_nj_rows = 0;
                int W_sheet_wx_rows = 0;
                int W_sheet_cz_rows = 0;
                int W_sheet_ha_rows = 0;
                int W_sheet_lyg_rows = 0;
                int W_sheet_nt_rows = 0;
                int W_sheet_tz_rows = 0;
                int W_sheet_sq_rows = 0;
                int W_sheet_xz_rows = 0;
                int W_sheet_yc_rows = 0;
                int W_sheet_yz_rows = 0;
                int W_sheet_zj_rows = 0;
                int W_sheet_sz_rows = 0;

                //读取数据文件
                File file = new File(_url);
                File directory = new File("");// 设定为当前文件夹
                File[] files = file.listFiles();
                String fil = "";

                for (int i = 0; i < files.length; i++) {
                    if (!files[i].isDirectory()) {
                        String _url2 = _url.replace("/", "\\");
                        String _url3 = files[i].toString();
                        //System.out.println(_url2 + "##" + _url3);
                        String file_name = files[i].toString().replace(_url2, "");
                        if (!file_name.equals(_filename)) {
                            fil = files[i].toString();
                            //解析log文件                       
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fil), "GBK"));
                            String str = null;
                            int count = 0;
                            System.out.println(fil + "####");
                            strclass.ALARM_MES2 _ALARM_MES = new strclass.ALARM_MES2();
                            while ((str = br.readLine()) != null) {
                                if (str.indexOf(">>收到告警") != -1) {

                                    str = str.toString().replace(" - --->>收到告警：", "");
                                    //System.out.println(str.toString());

                                    _ALARM_MES = new strclass.ALARM_MES2();
                                    _ALARM_MES.DESCRIPTION = str;
                                    _ALARM_MES.FILENAME = fil;

                                    String[] mess = str.split("\t");
                                    if (mess.length > 0) {
                                        _ALARM_MES.STARTTIME = mess[12].toString();
                                        _ALARM_MES.UPDATETIME = mess[13].toString();
                                        _ALARM_MES.TYPE = mess[14].toString();
                                        _ALARM_MES.WGNAME = mess[2].toString();
                                    }
                                    String str_wg_name = mess[2].toString();

                                    if (str_wg_name.indexOf("南京") != -1) {
                                        W_sheet_nj_rows = W_sheet_nj_rows + 1;
                                        if (W_sheet_nj_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_nj = get_sheet("南京" + sheet_count, sheet_count, W_book);
                                            W_sheet_nj_rows = 0;
                                            W_sheet_nj_rows = W_sheet_nj_rows + 1;
                                            insert(W_sheet_nj, _ALARM_MES, W_sheet_nj_rows);
                                        } else {
                                            insert(W_sheet_nj, _ALARM_MES, W_sheet_nj_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("无锡") != -1) {
                                        W_sheet_wx_rows = W_sheet_wx_rows + 1;
                                        if (W_sheet_wx_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_wx = get_sheet("无锡" + sheet_count, sheet_count, W_book);
                                            W_sheet_wx_rows = 0;
                                            W_sheet_wx_rows = W_sheet_wx_rows + 1;
                                            insert(W_sheet_wx, _ALARM_MES, W_sheet_wx_rows);
                                        } else {
                                            insert(W_sheet_wx, _ALARM_MES, W_sheet_wx_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("常州") != -1) {
                                        W_sheet_cz_rows = W_sheet_cz_rows + 1;
                                        if (W_sheet_cz_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_cz = get_sheet("常州" + sheet_count, sheet_count, W_book);
                                            W_sheet_cz_rows = 0;
                                            W_sheet_cz_rows = W_sheet_cz_rows + 1;
                                            insert(W_sheet_cz, _ALARM_MES, W_sheet_cz_rows);
                                        } else {
                                            insert(W_sheet_cz, _ALARM_MES, W_sheet_cz_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("淮安") != -1) {
                                        W_sheet_ha_rows = W_sheet_ha_rows + 1;
                                        if (W_sheet_ha_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_ha = get_sheet("淮安" + sheet_count, sheet_count, W_book);
                                            W_sheet_ha_rows = 0;
                                            W_sheet_ha_rows = W_sheet_ha_rows + 1;
                                            insert(W_sheet_ha, _ALARM_MES, W_sheet_ha_rows);
                                        } else {
                                            insert(W_sheet_ha, _ALARM_MES, W_sheet_ha_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("连云港") != -1) {
                                        W_sheet_lyg_rows = W_sheet_lyg_rows + 1;
                                        if (W_sheet_lyg_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_lyg = get_sheet("连云港" + sheet_count, sheet_count, W_book);
                                            W_sheet_lyg_rows = 0;
                                            W_sheet_lyg_rows = W_sheet_lyg_rows + 1;
                                            insert(W_sheet_lyg, _ALARM_MES, W_sheet_lyg_rows);
                                        } else {
                                            insert(W_sheet_lyg, _ALARM_MES, W_sheet_lyg_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("南通") != -1) {
                                        W_sheet_nt_rows = W_sheet_nt_rows + 1;
                                        if (W_sheet_nt_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_nt = get_sheet("南通" + sheet_count, sheet_count, W_book);
                                            W_sheet_nt_rows = 0;
                                            W_sheet_nt_rows = W_sheet_nt_rows + 1;
                                            insert(W_sheet_nt, _ALARM_MES, W_sheet_nt_rows);
                                        } else {
                                            insert(W_sheet_nt, _ALARM_MES, W_sheet_nt_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("泰州") != -1) {
                                        W_sheet_tz_rows = W_sheet_tz_rows + 1;
                                        if (W_sheet_tz_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_tz = get_sheet("泰州" + sheet_count, sheet_count, W_book);
                                            W_sheet_tz_rows = 0;
                                            W_sheet_tz_rows = W_sheet_tz_rows + 1;
                                            insert(W_sheet_tz, _ALARM_MES, W_sheet_tz_rows);
                                        } else {
                                            insert(W_sheet_tz, _ALARM_MES, W_sheet_tz_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("宿迁") != -1) {
                                        W_sheet_sq_rows = W_sheet_sq_rows + 1;
                                        if (W_sheet_sq_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_sq = get_sheet("宿迁" + sheet_count, sheet_count, W_book);
                                            W_sheet_sq_rows = 0;
                                            W_sheet_sq_rows = W_sheet_sq_rows + 1;
                                            insert(W_sheet_sq, _ALARM_MES, W_sheet_sq_rows);
                                        } else {
                                            insert(W_sheet_sq, _ALARM_MES, W_sheet_sq_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("徐州") != -1) {
                                        W_sheet_xz_rows = W_sheet_xz_rows + 1;
                                        if (W_sheet_xz_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_xz = get_sheet("徐州" + sheet_count, sheet_count, W_book);
                                            W_sheet_xz_rows = 0;
                                            W_sheet_xz_rows = W_sheet_xz_rows + 1;
                                            insert(W_sheet_xz, _ALARM_MES, W_sheet_xz_rows);
                                        } else {
                                            insert(W_sheet_xz, _ALARM_MES, W_sheet_xz_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("盐城") != -1) {
                                        W_sheet_yc_rows = W_sheet_yc_rows + 1;
                                        if (W_sheet_yc_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_yc = get_sheet("盐城" + sheet_count, sheet_count, W_book);
                                            W_sheet_yc_rows = 0;
                                            W_sheet_yc_rows = W_sheet_yc_rows + 1;
                                            insert(W_sheet_yc, _ALARM_MES, W_sheet_yc_rows);
                                        } else {
                                            insert(W_sheet_yc, _ALARM_MES, W_sheet_yc_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("扬州") != -1) {
                                        W_sheet_yz_rows = W_sheet_yz_rows + 1;
                                        if (W_sheet_yz_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_yz = get_sheet("扬州" + sheet_count, sheet_count, W_book);
                                            W_sheet_yz_rows = 0;
                                            W_sheet_yz_rows = W_sheet_yz_rows + 1;
                                            insert(W_sheet_yz, _ALARM_MES, W_sheet_yz_rows);
                                        } else {
                                            insert(W_sheet_yz, _ALARM_MES, W_sheet_yz_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("镇江") != -1) {
                                        W_sheet_zj_rows = W_sheet_zj_rows + 1;
                                        if (W_sheet_zj_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_zj = get_sheet("镇江" + sheet_count, sheet_count, W_book);
                                            W_sheet_zj_rows = 0;
                                            W_sheet_zj_rows = W_sheet_zj_rows + 1;
                                            insert(W_sheet_zj, _ALARM_MES, W_sheet_zj_rows);
                                        } else {
                                            insert(W_sheet_zj, _ALARM_MES, W_sheet_zj_rows);
                                        }
                                    }

                                    if (str_wg_name.indexOf("苏州") != -1) {
                                        W_sheet_sz_rows = W_sheet_sz_rows + 1;
                                        if (W_sheet_sz_rows > maxRowCount) {
                                            sheet_count = sheet_count + 1;
                                            W_sheet_sz = get_sheet("苏州" + sheet_count, sheet_count, W_book);
                                            W_sheet_sz_rows = 0;
                                            W_sheet_sz_rows = W_sheet_sz_rows + 1;
                                            insert(W_sheet_sz, _ALARM_MES, W_sheet_sz_rows);
                                        } else {
                                            insert(W_sheet_sz, _ALARM_MES, W_sheet_sz_rows);
                                        }
                                    }
                                }
                            }
                            br.close();
                        }
                    }
                }

                Thread.sleep(2000);
                W_book.write();
                W_book.close();
                System.out.println("[" + file_full_url + "]文件生成成功");

                try {
                    java.awt.Desktop.getDesktop().open(new File(file_full_url));
                } catch (Exception ex) {
                    System.out.println("调用默认程序打开程序失败:" + ex.getMessage());
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {

            }
        } else {
            System.out.println("[" + file_full_url + "]创建失败");
        }
    }

    /**
     * @param W_sheet----------------------------------------------表页数
     * @param _alarm-----------------------------------------------要插入的信息
     * @param count -----------------------------------------------信息插入的行数
     */
    //---------------------------------讲符合条件的信息写入Excel----------------------------------
    public static void insert(WritableSheet W_sheet, strclass.ALARM_MES2 _alarm, int count) {
        //写数据表头
        if (count == 1) {
            _excel.excel_W_Cell(W_sheet, 0, 0, "程序运行时间");
            _excel.excel_W_Cell(W_sheet, 1, 0, "START_TIME");
            _excel.excel_W_Cell(W_sheet, 2, 0, "UPDATE_TIME");
            _excel.excel_W_Cell(W_sheet, 3, 0, "网管名称");
            _excel.excel_W_Cell(W_sheet, 4, 0, "告警状态");
            _excel.excel_W_Cell(W_sheet, 5, 0, "所属文件");
            _excel.excel_W_Cell(W_sheet, 6, 0, "高级内容");

        }

        //---------------------写数据-------------------------//
        _excel.excel_W_Cell(W_sheet, 0, count, _alarm.CXyxsj);
        _excel.excel_W_Cell(W_sheet, 1, count, _alarm.STARTTIME);
        _excel.excel_W_Cell(W_sheet, 2, count, _alarm.UPDATETIME);
        _excel.excel_W_Cell(W_sheet, 3, count, _alarm.WGNAME);
        if (_alarm.TYPE.equals("1")) {
            _excel.excel_W_Cell(W_sheet, 4, count, "恢复告警");
        } else if (_alarm.TYPE.equals("0")) {
            _excel.excel_W_Cell(W_sheet, 4, count, "活动告警");
        } else {
            _excel.excel_W_Cell(W_sheet, 4, count, "异常未能识别：[" + _alarm.TYPE + "]");
        }

        String _description = _alarm.DESCRIPTION.replace("\t", " ").replace("null", " ");

        _excel.excel_W_Cell(W_sheet, 5, count, _alarm.FILENAME.toString());
        _excel.excel_W_Cell(W_sheet, 6, count, _description.toString());
        //---------------------写数据-------------------------//
    }
}
