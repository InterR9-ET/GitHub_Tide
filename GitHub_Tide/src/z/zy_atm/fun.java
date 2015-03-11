package z.zy_atm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class fun {

    //----------------------oracle数据映射----------------------
    public static class oracle_path {

        public long path_id;//               NUMBER(18) default '0' not null,
    }

    //----------------------mysql数据映射----------------------
    public static class mysql_path {

        public String name;
        public int network_id;//           NUMBER(18) default '0' not null,
        public long path_id;//               NUMBER(18) default '0' not null,
        public int servicetype;//          NUMBER(18) default 0,
        public int connectstatus;//        NUMBER(18) default 0,
        public int bandwidth;//            NUMBER(18) default 0,
        public int isfree;//                NUMBER(18) default 0,
        public String type_str;//             VARCHAR2(500),
        public int acustomer_id;//         NUMBER(18) default 0,
        public int agroup_id;//            NUMBER(18) default 0,
        public int zgroup_id;//           NUMBER(18) default 0,
        public int zcustomer_id;//         NUMBER(18) default 0,
        public int slacontractid;//        NUMBER(18) default -1,
        public int parentid;//          NUMBER(18) default -1,
        public long serial;//                INTEGER default 0,
        public long ems_type;//              INTEGER default 0,
        public int aendvpi;//            NUMBER(18) default 0,
        public int aendvci;//             NUMBER(18) default 0,
        public int aenddlci;//            NUMBER(18) default 0,
        public int zendvpi;//              NUMBER(18) default 0,
        public int zendvci;//            NUMBER(18) default 0,
        public int zenddlci;//          NUMBER(18) default 0,
        public int pcr;//        NUMBER(18) default 0,
        public int mbs;//    NUMBER(18) default 0,
        public int cdvt;//             NUMBER(18) default 0,
        public int rsr;//          NUMBER(18) default 0,
        public String atmservicetype;//        VARCHAR2(500) default 0,
        public int aendnode;//            NUMBER(18) default 0,
        public int aendshelf;//            NUMBER(18) default 0,
        public int aendslot;//           NUMBER(18) default 0,
        public int aendport;//            NUMBER(18) default 0 not null,
        public int aendtimeslot;//         NUMBER(18) default 0,
        public int zendnode;//           NUMBER(18) default 0,
        public int zendshelf;//           NUMBER(18) default 0,
        public int zendslot;//           NUMBER(18) default 0,
        public int zendport;//            NUMBER(18) default 0,
        public int zendtimeslot;//         NUMBER(18) default 0,
        public int owner_id;//            NUMBER(18) default 0,
        public int ownergroup_id;//         NUMBER(18) default 0,
        public int ownerheadoffice_id;//    NUMBER(18) default 0,
        public String aendinput;//             VARCHAR2(500),
        public String zendinput;//             VARCHAR2(500),
        public String aendalcatel;//          VARCHAR2(300),
        public String zendalcatel;//           VARCHAR2(4000),
        public String aendlineno;//           VARCHAR2(300),
        public String zendlineno;//           VARCHAR2(300),
        public String manageno;//              VARCHAR2(300),
        public String pathlevel;//             VARCHAR2(100) default 'N',
        public int priority;//             NUMBER(18) default 0,
        public int recovery;//             NUMBER(18) default 0,
        public int ismultiple;//           NUMBER(18) default 0,
        public int subrate;//               NUMBER(18) default 0,
        public int rate;//                 NUMBER(18) default 0,
        public int speed;//                NUMBER(18) default 0,
        public int dataoctets;//            NUMBER(18) default 0,
        public int buffertime;//            NUMBER(18) default 0,
        public int hdlc;//             NUMBER(18) default 0,
        public int maxhdlc;//             NUMBER(18) default 0,
        public int tbwidth;//              NUMBER(18) default 0,
        public int bumping;//              NUMBER(18) default 0,
        public int longdistance;//          INTEGER default 0,
        public int filterid;//            NUMBER(18) default 0,
        public int scr;//                 NUMBER(18) default 0,
        public long isloop;//                INTEGER default '-1',
        public String code;//                 VARCHAR2(255),
        public String nmpathno;//             VARCHAR2(255),
        public int customeridinnm;//      NUMBER(18) default -1,
        public String vendor;//               VARCHAR2(250),
        public String aendname;//              VARCHAR2(250),
        public String zendname;//              VARCHAR2(250),
        public String datasource;//            VARCHAR2(50),
        public int nmsync;//               NUMBER(10) default -1,
        public String aendaddressabbr;//       VARCHAR2(255),
        public String aendcontactor;//         VARCHAR2(255),
        public String aendtel;//               VARCHAR2(255),
        public String zendaddressabbr;//       VARCHAR2(255),
        public String zendcontactor;//         VARCHAR2(255),
        public String zendtel;//               VARCHAR2(255),
        public int aendportinnm;//          NUMBER(20) default -1,
        public int zendportinnm;//          NUMBER(20) default -1,
        public int idinnm;//                NUMBER(20) default -1,------------------------
        public String description;//           VARCHAR2(255) default '',
        public String starttime;//             VARCHAR2(200),
        public String endtime;//               VARCHAR2(200),
        public String isimportance;//          VARCHAR2(10),
        public int aendpathid;//            NUMBER(20) default -1,
        public int zendpathid;//            NUMBER(20) default -1,
        public int backuppathid;//          NUMBER(20) default -1,
        public String remarksite;//            VARCHAR2(50),
        public String aatmif;//                VARCHAR2(50),
        public String zatmif;//                VARCHAR2(50),
        public int sync_result;//          NUMBER(20),
        public String sync_result_info;//      VARCHAR2(50),
        public String sync_time;//            VARCHAR2(50),
        public int sortid;//               NUMBER(20) default 0,
        public int main_circuit_terminal;//NUMBER default 0,
        public String path_alias;//            VARCHAR2(1000)
    }

}
/**
 ******************************************************************************* 同步path
 * @author Administrator
 */
class path implements Runnable {

    private org.apache.log4j.Logger log;
    private util.GetSql.csnms _csnms;
    private util.GetSql.atm_mysql _mysql;

    /**
     * @param log the log to set
     */
    public void setLog(org.apache.log4j.Logger log) {
        this.log = log;
    }

    /**
     * @param _csnms the _csnms to set
     */
    public void setCsnms(util.GetSql.csnms _csnms) {
        this._csnms = _csnms;
    }

    /**
     * @param _mysql the _mysql to set
     */
    public void setMysql(util.GetSql.atm_mysql _mysql) {
        this._mysql = _mysql;
    }

    @Override
    public void run() {
        //取数据mysql的数据

        try {
            List mysql_path = db._pathdata(log, _csnms, _mysql);
            //判断是否取到数据
            if (mysql_path.size() > 0) {
                for (int i = 0, m = mysql_path.size(); i < m; i++) {

                    // 逐条取出所有信息
                    fun.mysql_path pathid_datal = (fun.mysql_path) mysql_path.get(i);
                    //***************添信息**************************
                    List insert = new ArrayList();
                    String name = pathid_datal.name;
                    int network = pathid_datal.network_id;
                    int aendport = pathid_datal.aendport;   //aEndPort
                    long path = pathid_datal.path_id;
                    insert.add(name);
                    insert.add(network);
                    insert.add(path);
                    insert.add(aendport);

                    //拿mysql的pathid到oracle里去匹配
                    boolean re = db._pathoracle(log, _csnms, _mysql, path);

                    //如果oracle里面有，更新数据
                    if (re) {
                        //根据pathid更新数据
                        db.update_path(_csnms, pathid_datal);
                        log.info("[path_存在数据]:已完成更新 path id :" + pathid_datal.path_id);
                    } //如果oracle里面没有，添加之后在更新
                    else {
                        //添加数据
                        db.intsert_oraclepath(_csnms, insert);
                        //更新数据
                        try {
                            int res = db.update_path(_csnms, pathid_datal);
                            if (res > 0) {
                                log.info("[path_存在数据]:已完成更新 path id :" + pathid_datal.path_id);
                            } else {
                                log.info("【更新失败】" + pathid_datal.path_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("[path_同步完成！！！]");
        } catch (IOException ex) {
            Logger.getLogger(path.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
/**
 * *****************************************************************************同步node
 * @author Administrator
 */
class node implements Runnable {

    private org.apache.log4j.Logger log;
    private util.GetSql.csnms _csnms;
    private util.GetSql.atm_mysql _mysql;

    /**
     * @param log the log to set
     */
    public void setLog(org.apache.log4j.Logger log) {
        this.log = log;
    }

    /**
     * @param _csnms the _csnms to set
     */
    public void setCsnms(util.GetSql.csnms _csnms) {
        this._csnms = _csnms;
    }

    /**
     * @param _mysql the _mysql to set
     */
    public void setMysql(util.GetSql.atm_mysql _mysql) {
        this._mysql = _mysql;
    }

    @Override
    public void run() {
        try {
            //------------------------------------------------------------------取数据
           List mysql_node = db._nodedata(log, _csnms, _mysql);
            if (mysql_node.size() > 0) {
                boolean re = false;
                for (int i = 0, m = mysql_node.size(); i < m; i++) {
                    strclass.node node_datal = (strclass.node) mysql_node.get(i);
                    re = db._nodeoracle(log, _csnms, _mysql, node_datal);
                    if (re) {
                        ////----------------------------------------------------根据pathid更新数据
                        db.update_node(_csnms, node_datal);
                        log.info("[node_存在数据]:已完成更新 path id :" + node_datal.node_id);
                    } else {
                        //------------------------------------------------------添加数据
                        int resu = db.intsert_oraclenode(_csnms, node_datal);
                        if (resu > 0) {
                            System.out.println("node_插入成功！！！");
                        }
                    }
                }
            }
            
            log.info("[node_同步完成！！！]");
        } catch (IOException ex) {
            Logger.getLogger(node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
/**
 * *****************************************************************************同步pathtrace
 * @author Administrator
 */
  class pathtrca implements Runnable {
        
    private org.apache.log4j.Logger log;
    private util.GetSql.csnms _csnms;
    private util.GetSql.atm_mysql _mysql;

    /**
     * @param log the log to set
     */
    public void setLog(org.apache.log4j.Logger log) {
        this.log = log;
    }

    /**
     * @param _csnms the _csnms to set
     */
    public void setCsnms(util.GetSql.csnms _csnms) {
        this._csnms = _csnms;
    }

    /**
     * @param _mysql the _mysql to set
     */
    public void setMysql(util.GetSql.atm_mysql _mysql) {
        this._mysql = _mysql;
    }
        
        @Override
        public void run() {
            //-----------------------------取数据----------------------------------
           List mysql_pathtrace = db._pathtrca(log, _csnms, _mysql);
            if (mysql_pathtrace.size() > 0) {
                boolean re = false;
                for (int i = 0, m = mysql_pathtrace.size(); i < m; i++) {
                    strclass.pathtrace pathtrace = (strclass.pathtrace) mysql_pathtrace.get(i);
                    //----------------------------------------------------------查找数据
                    re = db._pathtrcaoracle(log, _csnms, _mysql, pathtrace);
                    if (re) {
                        //------------------------------------------------------根据pathid更新数据
                        db.update_pathtrace(_csnms, pathtrace);
                        log.info("[pathtrace_存在数据]:已完成更新 path id :" + pathtrace.path_id);
                    } else {
                        //------------------------------------------------------添加数据
                        int resu = db.intsert_oraclepathtrca(_csnms, pathtrace);
                        if (resu > 0) {
                            System.out.println("pathtrace_插入成功！！！");
                        }
                    }
                }
            }
            log.info("[pathtrace同步完成！！！]");
        }
    
    }
    
    