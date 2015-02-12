/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.AbstractClass;

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

/**
 * ftp上传，下载
 *
 * @author why 2009-07-30
 *
 */
public abstract class ftp {

    public abstract void set_FTP_IP(String FTP_IP);

    public abstract void set_FTP_USERNAME(String FTP_USERNAME);

    public abstract void set_FTP_PWD(String FTP_PWD);

    public abstract void set_FTP_PORT(int FTP_PORT);

    public abstract String get_FTP_IP();

    public abstract String get_FTP_USERNAME();

    public abstract String get_FTP_PWD();

    public abstract int get_FTP_PORT();

    public abstract boolean ini();

    public abstract boolean ini(String ftp_name);

    public abstract boolean close();

    public abstract boolean rush();

    public abstract boolean EXECMD(String cmd);

    public abstract boolean isDirExist(String full_dir);

    public abstract boolean createDir(String full_dir);

    public abstract List getDirFileList(String full_dir);

    public abstract boolean downloadFile(String full_filedir, String full_filedir_new);

    /**
     * FTP远程命令列表 USER PORT RETR ALLO DELE SITE XMKD CDUP FEAT PASS PASV STOR
     * REST CWD STAT RMD XCUP OPTS ACCT TYPE APPE RNFR XCWD HELP XRMD STOU AUTH
     * REIN STRU SMNT RNTO LIST NOOP PWD SIZE PBSZ QUIT MODE SYST ABOR NLST MKD
     * XPWD MDTM PROT
     * 在服务器上执行命令,如果用sendServer来执行远程命令(不能执行本地FTP命令)的话，所有FTP命令都要加上\r\n
     * ftpclient.sendServer("XMKD /test/bb\r\n"); //执行服务器上的FTP命令
     * ftpclient.readServerResponse一定要在sendServer后调用
     * nameList("/test")获取指目录下的文件列表 XMKD建立目录，当目录存在的情况下再次创建目录时报错 XRMD删除目录
     * DELE删除文件
     */
}
