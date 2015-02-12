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
public abstract class socket {

    public abstract void set_SOCKET_IP(String SOCKET_IP);

    public abstract void set_SOCKET_PORT(String SOCKET_PORT);

    public abstract String get_SOCKET_IP();

    public abstract String get_SOCKET_PORT();

    public abstract boolean ini(String SOCKET_NAME);

    public abstract boolean ini();

    public abstract boolean sendmessage(String message);

}
