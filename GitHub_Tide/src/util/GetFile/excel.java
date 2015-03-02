/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetFile;

import util.AbstractClass.files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author yangzhen
 */
public class excel extends files {

    @Override
    public void close(Object[] param) {
        if (param != null) {
            for (Object obj : param) {
                try {

                    if (obj instanceof Workbook) {
                        ((Workbook) obj).close();
                        obj = null;
                    }
                    if (obj instanceof WritableWorkbook) {
                        ((WritableWorkbook) obj).write();
                        ((WritableWorkbook) obj).close();
                        obj = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean create_folder(String folder_url) {
        boolean bs = false;
        try {
            File directory = new File("");// 设定为当前文件夹
            String[] mes_h = folder_url.toString().split("/");
            String _url = directory.getCanonicalPath().toString();
            for (int i = 0, len = mes_h.length; i < len; i++) {//循环创建文件夹
                String mes_h_l = mes_h[i].toString();
                if (mes_h_l.length() > 0) {
                    _url = _url + "/" + mes_h_l;
                    _url = _url.replace("\\", "/");
                    System.out.println(_url);
                    File _folder = new File(_url);
                    if (!_folder.exists()) {// 不存在   
                        if (!_folder.isFile()) {//是否是有效文件
                            if (!_folder.isDirectory()) {//是否是有效目录，为空也为无效
                                _folder.mkdir();//创建文件夹
                                System.out.println("==========>文件夹[" + _url + "]已创建.");
                                bs = true;
                            }
                        }
                    } else {
                        bs = true;
                        System.out.println("已存在");
                    }
                }
            }
        } catch (Exception e) {
            bs = false;
            System.out.println("创建文件夹异常：");
            System.out.println("==========>：" + e.getMessage().toString());
        }
        return bs;
    }

    @Override
    public boolean delete_folder(String folder_url) {
        boolean bs = false;
        try {

            File directory = new File("");// 设定为当前文件夹
            String _url = directory.getCanonicalPath().toString();
            _url = _url + folder_url;

            File file = new File(_url);

            if (file.exists()) {                    //判断文件是否存在
                if (file.isFile()) {                    //判断是否是文件
                    file.delete();                       //delete()方法 你应该知道 是删除的意思;
                } else if (file.isDirectory()) {              //否则如果它是一个目录
                    File files[] = file.listFiles();               //声明目录下所有的文件 files[];
                    for (int i = 0; i < files.length; i++) {            //遍历目录下所有的文件
                        this.delete_folder(folder_url + "/" + files[i].toString());             //把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
                bs = true;
            } else {
                System.out.println("所删除的文件夹不存在！" + '\n');
            }

        } catch (Exception e) {
            bs = false;
            System.out.println("删除文件夹异常：");
            System.out.println("==========>：" + e.getMessage().toString());
        }
        return bs;
    }

    @Override
    public boolean delete_file(String file_url) {
        boolean bs = false;
        try {

            File directory = new File("");// 设定为当前文件夹
            String _url = directory.getCanonicalPath().toString();
            _url = _url + file_url;

            File file = new File(_url);

            if (file.exists()) {                    //判断文件是否存在
                if (file.isFile()) {                    //判断是否是文件
                    file.delete();                       //delete()方法 你应该知道 是删除的意思;
                } else if (file.isDirectory()) {              //否则如果它是一个目录
                    File files[] = file.listFiles();               //声明目录下所有的文件 files[];
                    for (int i = 0; i < files.length; i++) {            //遍历目录下所有的文件
                        this.delete_folder(file_url + "/" + files[i].toString());             //把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
                bs = true;
            } else {
                System.out.println("所删除的文件不存在！" + '\n');
            }
            bs = true;
        } catch (Exception e) {
            bs = false;
            System.out.println("删除文件夹异常：");
            System.out.println("==========>：" + e.getMessage().toString());
        }
        return bs;
    }

    @Override
    public boolean create_file(String file_url) {
        boolean _bs = false;
        _bs = excel_create_file(file_url);
        return _bs;
    }

    @Override
    public String read_file(String file_url, String str_content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write_file(String file_url, String str_content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // read Workbook
    public Workbook excel_R_Workbook(String str_filename) {
        Workbook R_workbook = null;
        try {
            R_workbook = Workbook.getWorkbook(new File(str_filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R_workbook;
    }

    // read Sheet
    public Sheet excel_R_Sheet(Workbook R_workbook, int count) {
        Sheet R_sheet = null;
        R_sheet = R_workbook.getSheet(count);
        return R_sheet;
    }

    // read Cell
    public String excel_R_Cell(Sheet R_sheet, int x, int y) {
        String str_mes = null;
        Cell R_cell = null;
        R_cell = R_sheet.getCell(x, y);
        str_mes = R_cell.getContents().toString();
        return str_mes;
    }

    // write to Workbook
    public WritableWorkbook excel_W_Workbook(String str_filename) {
        WritableWorkbook W_workbook = null;
        try {
            //打开文件
            Workbook R_workbook = Workbook.getWorkbook(new File(str_filename));
            W_workbook = Workbook.createWorkbook(new File(str_filename), R_workbook);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return W_workbook;
    }

    // write to W_sheet  
    public WritableSheet excel_W_Sheet(WritableWorkbook W_workbook, int count) {
        WritableSheet W_sheet = null;
        W_sheet = W_workbook.getSheet(count);
        return W_sheet;
    }

    // write to W_cell
    public void excel_W_Cell(WritableSheet W_sheet, int x, int y, String str_mes) {
        Label lb = new Label(x, y, str_mes);
        try {
            W_sheet.addCell(lb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create excel�ĵ�
    private boolean excel_create_file(String file_url) {
        boolean _bs = false;
        WritableWorkbook W_workbook = null;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file_url);
            W_workbook = Workbook.createWorkbook(fos);
            WritableSheet ws = W_workbook.createSheet("sheet1", 0);  // ����һ��������
            W_workbook.write();
            W_workbook.close();
            _bs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return _bs;
    }

    @Override
    public boolean copy_file(String oldpath, String newpath) {
        boolean _bs = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldpath);
            if (oldfile.exists()) { //文件存在时  
                InputStream inStream = new FileInputStream(oldpath); //读入原文件   
                FileOutputStream fs = new FileOutputStream(newpath);
                byte[] buffer = new byte[2048];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, length);
                } //字节数 文件大小

                inStream.close();
                fs.flush();
                fs.close();

                _bs = true;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
        return _bs;
    }

}
