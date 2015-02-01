/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetFtp;

/**
 *
 * @author zhen
 *
 */
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;
import sun.net.ftp.FtpClient;
import util.AbstractClass.ftp;

/**
 * ftp上传，下载
 *
 * @author why 2009-07-30
 *
 */
public class ping_dcn extends ftp {

    private String FTP_IP = "132.228.166.233";
    private String FTP_USERNAME = "taisitong";
    private String FTP_PWD = "Telecom@123";
    private int FTP_PORT = -1;

    private FtpClient FTP_CLIENT = null;
    private String FTP_PATH = "";
    private OutputStream os = null;
    private FileInputStream is = null;

    @Override
    public void set_FTP_IP(String FTP_IP) {
        this.FTP_IP = FTP_IP;
    }

    @Override
    public void set_FTP_USERNAME(String FTP_USERNAME) {
        this.FTP_USERNAME = FTP_USERNAME;
    }

    @Override
    public void set_FTP_PWD(String FTP_PWD) {
        this.FTP_PWD = FTP_PWD;
    }

    @Override
    public void set_FTP_PORT(int FTP_PORT) {
        this.FTP_PORT = FTP_PORT;
    }

    @Override
    public String get_FTP_IP() {
        return FTP_IP.toString();
    }

    @Override
    public String get_FTP_USERNAME() {
        return FTP_USERNAME.toString();
    }

    @Override
    public String get_FTP_PWD() {
        return FTP_PWD.toString();
    }

    @Override
    public int get_FTP_PORT() {
        return FTP_PORT;
    }

    @Override
    public boolean load() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean open() {
        boolean _bs = false;
        FTP_CLIENT = new FtpClient();
        try {
            if (this.FTP_PORT != -1) {
                FTP_CLIENT.openServer(this.FTP_IP, this.FTP_PORT);
            } else {
                FTP_CLIENT.openServer(this.FTP_IP);
            }
            FTP_CLIENT.login(this.FTP_USERNAME, this.FTP_PWD);
            if (this.FTP_PATH.length() != 0) {
                FTP_CLIENT.cd(this.FTP_PATH);// path是ftp服务下主目录的子目录
            }
            FTP_CLIENT.binary();// 用2进制上传、下载
            System.out.println("FTP 已登录到\"" + FTP_CLIENT.pwd() + "\"目录");
            _bs = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean close() {
        boolean _bs = false;
        try {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
            if (FTP_CLIENT != null) {
                FTP_CLIENT.closeServer();
            }
            _bs = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean rush() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isDirExist(String full_dir) {
        boolean _bs = false;
        String pwd = "";
        try {
            pwd = FTP_CLIENT.pwd();
            FTP_CLIENT.cd(full_dir);
            FTP_CLIENT.cd(pwd);
            _bs = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean EXECMD(String cmd) {
        boolean _bs = false;
        try {
            FTP_CLIENT.sendServer(cmd);
            System.out.println(cmd);
            _bs = true;
            //int reply = ftpClient.readServerResponse();
            //System.out.println(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return _bs;
    }

    @Override
    public boolean createDir(String full_dir) {
        try {
            FTP_CLIENT.ascii();
            StringTokenizer s = new StringTokenizer(full_dir, "/"); //sign
            s.countTokens();
            String pathName = "";//ftpClient.pwd();
            while (s.hasMoreElements()) {
                pathName = pathName + "/" + (String) s.nextElement();
                if (this.isDirExist(pathName)) {
                    continue;
                }
                try {
                    FTP_CLIENT.sendServer("MKD " + pathName + "\r\n");
                } catch (Exception e) {
                    e = null;
                    return false;
                }
                FTP_CLIENT.readServerResponse();
            }
            FTP_CLIENT.binary();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    @Override
    public List getDirFileList(String full_dir) {
        List list = new ArrayList();
        DataInputStream dis;
        try {
            //System.out.println(full_dir);
            dis = new DataInputStream(FTP_CLIENT.nameList(full_dir));
            String filename = "";
            while ((filename = dis.readLine()) != null) {
                String sfilename = new String(filename.getBytes("ISO-8859-1"), "utf-8");
                list.add(sfilename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean downloadFile(String full_filedir, String full_filedir_new) {
        boolean _bs = false;
        long result = 0;
        TelnetInputStream is = null;
        FileOutputStream os = null;
        try {
            is = FTP_CLIENT.get(full_filedir);
            java.io.File outfile = new java.io.File(full_filedir_new);
            os = new FileOutputStream(outfile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = is.read(bytes)) != -1) {
                os.write(bytes, 0, c);
                result = result + c;
            }
            _bs = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _bs;
    }

}
