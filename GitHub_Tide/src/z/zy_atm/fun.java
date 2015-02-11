/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.zy_atm;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class fun {

    //-----------------------------调用添加信息的方法-------------------------------------------------
    public static void insert_csnms(util.GetSql.csnms _csnms,List _list) {

        //tb_SWITCH_REPORT_OUT does = new tb_SWITCH_REPORT_OUT();
        //_tb_in.insert_SWITCH_REPORT_OUTs(_list);
       db.tb_in_up(_csnms, _list);
    }
    
    
    public static class mysql_path {

        public String name ;
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
        public String aendinput ;//             VARCHAR2(500),
        public String zendinput;//             VARCHAR2(500),
        public String aendalcatel ;//          VARCHAR2(300),
        public String zendalcatel ;//           VARCHAR2(4000),
        public String aendlineno;//           VARCHAR2(300),
        public String zendlineno ;//           VARCHAR2(300),
        public String manageno ;//              VARCHAR2(300),
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
        public String code ;//                 VARCHAR2(255),
        public String nmpathno ;//             VARCHAR2(255),
        public int customeridinnm;//      NUMBER(18) default -1,
        public String vendor ;//               VARCHAR2(250),
        public String aendname ;//              VARCHAR2(250),
        public String zendname ;//              VARCHAR2(250),
        public String datasource ;//            VARCHAR2(50),
        public int nmsync;//               NUMBER(10) default -1,
        public String aendaddressabbr;//       VARCHAR2(255),
        public String aendcontactor ;//         VARCHAR2(255),
        public String aendtel ;//               VARCHAR2(255),
        public String zendaddressabbr;//       VARCHAR2(255),
        public String zendcontactor;//         VARCHAR2(255),
        public String zendtel ;//               VARCHAR2(255),
        public int aendportinnm;//          NUMBER(20) default -1,
        public int zendportinnm;//          NUMBER(20) default -1,
        public int idinnm;//                NUMBER(20) default -1,------------------------
        public String description ;//           VARCHAR2(255) default '',
        public String starttime ;//             VARCHAR2(200),
        public String endtime ;//               VARCHAR2(200),
        public String isimportance ;//          VARCHAR2(10),
        public int aendpathid;//            NUMBER(20) default -1,
        public int zendpathid;//            NUMBER(20) default -1,
        public int backuppathid;//          NUMBER(20) default -1,
        public String remarksite ;//            VARCHAR2(50),
        public String aatmif ;//                VARCHAR2(50),
        public String zatmif ;//                VARCHAR2(50),
        public int sync_result;//          NUMBER(20),
        public String sync_result_info ;//      VARCHAR2(50),
        public String sync_time ;//            VARCHAR2(50),
        public int sortid;//               NUMBER(20) default 0,
        public int main_circuit_terminal;//NUMBER default 0,
        public String path_alias ;//            VARCHAR2(1000)
    }
}
