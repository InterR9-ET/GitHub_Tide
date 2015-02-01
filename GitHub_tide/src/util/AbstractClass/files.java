/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.AbstractClass;

import java.sql.Connection;

/**
 *
 * @author yangzhen
 */
public abstract class files {

    public abstract void close(Object[] param);

    public abstract boolean create_folder(String folder_url);

    public abstract boolean delete_folder(String folder_url);

    public abstract boolean delete_file(String file_url);

    public abstract boolean create_file(String file_url);

    public abstract String read_file(String file_url, String str_content);

    public abstract void write_file(String file_url, String str_content);

    public abstract boolean copy_file(String oldpath, String newpath);
    
    /*
        string str = "c:/123.txt";  
        string fileName=str.Substring(str.LastIndexOf('/')+1 );  
    */
    
    
    
}
