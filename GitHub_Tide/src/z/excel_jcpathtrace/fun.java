/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.excel_jcpathtrace;

import z.xn_port.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
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

    /**
     * 获取下载好的信息
     *
     * @param file1 文件路径
     * @return
     */
    private static util.GetFile.excel _excel = new util.GetFile.excel();
    private static WritableWorkbook W_book = null;
    private static WritableSheet W_sheet = null;

    public static boolean copy_file(String fileurl1, String fileurl2) {
        boolean bs = false;
        File directory = new File("");// 设定为当前文件夹
        try {
            String _url1 = directory.getCanonicalPath().toString() + "/file/excel/VPN和ATM电路核查表/" + fileurl1;
            String _url2 = directory.getCanonicalPath().toString() + "/file/excel/VPN和ATM电路核查表/" + fileurl2;
            boolean _bs_c = _excel.copy_file(_url1, _url2);
            if (_bs_c) {
                bs = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bs;
    }

    public static boolean loadfile(String fileurl, String sheetname) {
        boolean bs = false;
        try {
            W_book = _excel.excel_W_Workbook(fileurl);
            W_sheet = W_book.getSheet(sheetname);
            bs = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bs;
    }

    public static boolean jc_data(util.GetSql.csnms _csnms) {
        boolean bs = false;
        try {

            int rows = W_sheet.getRows();
            int columns = W_sheet.getColumns();

            for (int i = 1; i < rows; i++) {
                System.out.println("读取数据：" + (i) + "/" + (rows - 1));
                String pathname1 = get_value(0, i).trim().toString().trim();
                String pathname2 = get_value(13, i).trim().toString().trim();
                System.out.println(pathname1 + "#" + pathname2);

                if (!pathname1.equals(pathname2)) {
                    set_value(25, i, "A列N列名称不一致");
                }

                if (pathname1.length() > 0) {
                    List _list = db.haspath(_csnms, pathname1);
                    if (_list.size() > 0) {

                        //判断pathtrace
                        boolean _bs3 = false;

                        for (int i2 = 0, m = _list.size(); i2 < m; i2++) {
                            HashMap map = (HashMap) _list.get(i2);
                            String PATHID = "";
                            if (map.get("PATHID") != null) {
                                PATHID = map.get("PATHID").toString();

                                List _list3 = db.haspathtrace(_csnms, PATHID);
                                if (_list3.size() > 0) {
                                    _bs3 = true;
                                }
                            }
                        }

                        if (_bs3) {
                            set_value(23, i, "A列电路代号,有路由数据");
                        } else {
                            set_value(23, i, "A列电路代号,无路由数据");
                        }

                    } else {
                        set_value(23, i, "A列电路代号,No Path");
                    }

                    List _list2 = db.haspath(_csnms, pathname2);
                    if (_list2.size() > 0) {

                        //判断pathtrace
                        boolean _bs3 = false;
                        for (int i2 = 0, m = _list2.size(); i2 < m; i2++) {
                            HashMap map = (HashMap) _list2.get(i2);
                            String PATHID = "";
                            if (map.get("PATHID") != null) {
                                PATHID = map.get("PATHID").toString();

                                List _list3 = db.haspathtrace(_csnms, PATHID);
                                if (_list3.size() > 0) {
                                    _bs3 = true;
                                }

                            }
                        }

                        if (_bs3) {
                            set_value(24, i, "N列本地专线号,有路由数据");
                        } else {
                            set_value(24, i, "N列本地专线号,无路由数据");
                        }
                    } else {
                        set_value(24, i, "N列本地专线号,No path");
                    }
                }

            }

            W_book.write();
            W_book.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bs;
    }

    private static String get_value(int lie, int hang) {
        String _mes = "";
        try {
            _mes = _excel.excel_R_Cell(W_sheet, lie, hang).trim().toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return _mes;
    }

    private static void set_value(int lie, int hang, String mes) {
        try {
            _excel.excel_W_Cell(W_sheet, lie, hang, mes);   // (W_sheet, lie, hang);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
