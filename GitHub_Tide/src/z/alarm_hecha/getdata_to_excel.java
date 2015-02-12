/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.alarm_hecha;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author yangzhen
 */
public class getdata_to_excel extends Thread {

    private static GetFile.excel _excel = new GetFile.excel();

    public static class ALARM_MES {

        String STARTTIME = "";
        String UPDATETIME = "";
        String TYPE = "";
        String WGNAME = "";
        String DESCRIPTION = "";
        String FILENAME = "";
    }

    public void run(String message, String wg, int _day) {
        String key = "";
        if (message.length() > 0) {
            import_data2("file/alarm_log/log/" + _day + "/", message + "_" + _day + ".xls", key, message, wg);
        } else {
            import_data("file/alarm_log/", "data.xls", key);
        }
    }

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

    public static void insert(WritableSheet W_sheet, ALARM_MES _alarm, int count) {
        //写数据
        if (count == 1) {
            _excel.excel_W_Cell(W_sheet, 0, 0, "START_TIME");
            _excel.excel_W_Cell(W_sheet, 1, 0, "UPDATE_TIME");
            _excel.excel_W_Cell(W_sheet, 2, 0, "网管名称");
            _excel.excel_W_Cell(W_sheet, 3, 0, "告警状态");
            _excel.excel_W_Cell(W_sheet, 4, 0, "所属文件");
            _excel.excel_W_Cell(W_sheet, 5, 0, "高级内容");

        }

        //---------------------写数据-------------------------//
        _excel.excel_W_Cell(W_sheet, 0, count, _alarm.STARTTIME);
        _excel.excel_W_Cell(W_sheet, 1, count, _alarm.UPDATETIME);
        _excel.excel_W_Cell(W_sheet, 2, count, _alarm.WGNAME);
        if (_alarm.TYPE.equals("1")) {
            _excel.excel_W_Cell(W_sheet, 3, count, "恢复告警");
        } else if (_alarm.TYPE.equals("0")) {
            _excel.excel_W_Cell(W_sheet, 3, count, "活动告警");
        } else {
            _excel.excel_W_Cell(W_sheet, 3, count, "异常未能识别：[" + _alarm.TYPE + "]");
        }

        String _description = _alarm.DESCRIPTION.replace("\t", " ").replace("null", " ");

        _excel.excel_W_Cell(W_sheet, 4, count, _alarm.FILENAME.toString());
        _excel.excel_W_Cell(W_sheet, 5, count, _description.toString());
        //---------------------写数据-------------------------//
    }

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
                            ALARM_MES _ALARM_MES = new ALARM_MES();
                            while ((str = br.readLine()) != null) {
                                if (str.indexOf(">>收到告警") != -1) {

                                    str = str.toString().replace(" - --->>收到告警：", "");
                                    //System.out.println(str.toString());

                                    _ALARM_MES = new ALARM_MES();
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

    public static void import_data2(String _url, String _filename, String key, String message, String wg) {
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

                W_book = _excel.excel_W_Workbook(file_full_url);
                int maxRowCount = 50000;//设置最大的行数
                int sheet_count = 0;

                W_sheet_nj = get_sheet("合并", sheet_count, W_book);
                sheet_count = sheet_count + 1;

                int W_sheet_nj_rows = 0;

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
                            ALARM_MES _ALARM_MES = new ALARM_MES();
                            while ((str = br.readLine()) != null) {
                                if (str.indexOf(">>收到告警") != -1) {
                                    str = str.toString().replace(" - --->>收到告警：", "");
                                    //System.out.println(str.toString());

                                    if (message.length() > 0) {
                                        if (str.indexOf(message) == -1) {
                                            continue;
                                        }
                                    }

                                    _ALARM_MES = new ALARM_MES();
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

                                    W_sheet_nj_rows = W_sheet_nj_rows + 1;
                                    if (W_sheet_nj_rows > maxRowCount) {
                                        sheet_count = sheet_count + 1;
                                        W_sheet_nj = get_sheet("合并" + sheet_count, sheet_count, W_book);
                                        W_sheet_nj_rows = 0;
                                        W_sheet_nj_rows = W_sheet_nj_rows + 1;
                                        insert(W_sheet_nj, _ALARM_MES, W_sheet_nj_rows);
                                    } else {
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
}
