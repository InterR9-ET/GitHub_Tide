/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.AbstractClass;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author yangzhen
 *
 */
public abstract class databases {

    public abstract void close(Object[] param);

    public abstract void set_db_name(String db_name);

    public abstract void set_db_driver(String db_driver);

    public abstract void set_db_url(String db_url);

    public abstract void set_db_user(String db_user);

    public abstract void set_db_pwd(String db_pwd);

    public abstract String get_db_name();

    public abstract String get_db_driver();

    public abstract String get_db_url();

    public abstract String get_db_user();

    public abstract String get_db_pwd();

    public abstract boolean load();

    public abstract boolean load_web();

    public abstract boolean open();

    public abstract boolean close();

    public abstract boolean rush();

    public abstract int execute(String sql, Object[] objs);

    public abstract int execute(List sql);

    public abstract int execute_listobj(String sql, List Objects);

    public abstract List<Map<Object, Object>> getdata(String sql, Object[] objs);

    public abstract boolean create_tablefile(String DBName, String TableName);

}
