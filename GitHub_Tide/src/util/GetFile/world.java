/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetFile;

import util.AbstractClass.files;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfFont;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

/**
 *
 * @author yangzhen
 */
public class world extends files {

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String read_file(String file_url, String str_content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write_file(String file_url, String str_content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Document world_add_null(Document document, int count) throws Exception {
        Paragraph p_null = new Paragraph(" ");
        for (int i = 0; i < count; i++) {
            document.add(p_null);
        }
        return document;
    }

    public Document world_add_image(Document document, String image_url, float width, float height, String left_center_right) throws Exception {
        File directory = new File("");// 设定为当前文件夹
        Image png1 = Image.getInstance(directory.getCanonicalPath().toString() + image_url);//  \\report\\image\\1.png
        // 设置图片的绝对大小，宽和高  
        png1.scaleAbsolute(width, height);
        // 设置图片居中显示  
        if (left_center_right.length() > 0) {
            if (left_center_right.equals("0") || left_center_right.toLowerCase().equals("left")) {
                png1.setAlignment(Image.LEFT);
            } else if (left_center_right.equals("1") || left_center_right.toLowerCase().equals("center")) {
                png1.setAlignment(Image.MIDDLE);
            } else if (left_center_right.equals("2") || left_center_right.toLowerCase().equals("right")) {
                png1.setAlignment(Image.RIGHT);
            }
        }
        document.add(png1);
        return document;
    }

    public Font world_get_font(int fontsize, String BOLD_ITALIC_NORMAL_BOLDITALIC, String en_color) throws Exception {
        Font font = new Font();
        //-------------Itext字体-------------//
        //BaseFont bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //-------------Itext字体-------------//

        //-------------本地系统字体----------//
        BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/Arial.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        //-------------本地系统字体----------//

        //设置
        if (BOLD_ITALIC_NORMAL_BOLDITALIC != null) {
            if (BOLD_ITALIC_NORMAL_BOLDITALIC.length() > 0) {
                if (BOLD_ITALIC_NORMAL_BOLDITALIC.toUpperCase().equals("NORMAL")) {
                    font = new Font(bf, fontsize, Font.NORMAL);
                } else if (BOLD_ITALIC_NORMAL_BOLDITALIC.toUpperCase().equals("BOLD")) {
                    font = new Font(bf, fontsize, Font.BOLD);
                } else if (BOLD_ITALIC_NORMAL_BOLDITALIC.toUpperCase().equals("ITALIC")) {
                    font = new Font(bf, fontsize, Font.ITALIC);
                } else if (BOLD_ITALIC_NORMAL_BOLDITALIC.toUpperCase().equals("BOLDITALIC")) {
                    font = new Font(bf, fontsize, Font.BOLDITALIC);
                }
            }
        } else {
            font = new Font(bf, fontsize, Font.NORMAL);
        }

        //设置颜色
        if (en_color.toLowerCase().equals("yellow")) {
            font.setColor(Color.yellow);
        } else if (en_color.toLowerCase().equals("black")) {
            font.setColor(Color.black);
        } else if (en_color.toLowerCase().equals("blue")) {
            font.setColor(Color.blue);
        } else if (en_color.toLowerCase().equals("red")) {
            font.setColor(Color.red);
        } else if (en_color.toLowerCase().equals("green")) {
            font.setColor(Color.green);
        } else if (en_color.toLowerCase().equals("gray")) {
            font.setColor(Color.gray);
        } else if (en_color.toLowerCase().equals("orange")) {
            font.setColor(Color.orange);
        } else if (en_color.toLowerCase().equals("pink")) {
            font.setColor(Color.pink);
        } else if (en_color.toLowerCase().equals("white")) {
            font.setColor(Color.white);
        } else if (en_color.toLowerCase().equals("magenta")) {
            font.setColor(Color.magenta);
        }
        return font;
    }

    public Document world_add_String(Document document, String str_content, Font font, String left_center_right) throws Exception {
        Paragraph p = new Paragraph(str_content, font);
        // 设置段落居中，其中1为居中对齐，2为右对齐，3为左对齐  
        if (left_center_right.length() > 0) {
            if (left_center_right.equals("1") || left_center_right.toLowerCase().equals("center")) {
                p.setAlignment(1);
            } else if (left_center_right.equals("2") || left_center_right.toLowerCase().equals("right")) {
                p.setAlignment(2);
            } else if (left_center_right.equals("3") || left_center_right.toLowerCase().equals("left")) {
                p.setAlignment(3);
            }
        }
        //向文件中加入段落
        document.add(p);
        return document;
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
