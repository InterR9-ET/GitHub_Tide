/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.xn_transperwbs;

import z.xn_transper.*;
import z.xn_port.*;
import z.send_sms.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import static z.xn_transperwbs.main.wbs_url;

/**
 *
 * @author yangzhen
 * @author 功能：
 * @author 描述：
 *
 */
public class fun {
    
    /**
     * @param PORTUUID-------------------------------------------------------
     * @param wbs_url ------------------------------------------------------- 
     */
//------------------------------------------------------------------------------
    public static void do_test(String PORTUUID, String wbs_url) {
        String str_mes = "";
        try {
            String _url = wbs_url;
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(_url));
            call.setOperationName("getCurrentPm");//WSDL里面描述的接口名称
            String _temp = PORTUUID;
            String _xmlDoc = (String) call.invoke(new Object[]{_temp});
            str_mes = _xmlDoc;
            System.out.println(str_mes);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("END");

    }

    /**
     *
     * @param _UUID-------------------------------------------NODEUUID的先关数据
     * @param log---------------------------------------------日志信息
     * @param _csnms------------------------------------------oracle数据库信息
     * @param wbs_url-----------------------------------------
     * @return
     */
    //---------------------------------------------------------------------------
    public static Runnable createTask(final strclass.UUID _UUID, final org.apache.log4j.Logger log, final util.GetSql.csnms _csnms, final String wbs_url) {
        return new Runnable() {
            public void run() {
                try {
                    if (_UUID.PORTUUID.length() > 0) {
                        int count = 0;
                        System.out.println(_UUID.PORTUUID.toString());
                        count = count + 1;
                        String mes = getWBSData(_UUID.PORTUUID, wbs_url);
                        if (mes.length() > 0) {
                            //解析数据入库
                            log.info("MES:" + mes);
                            db.jiexi(mes, _csnms, log);
                        } else {
                            log.info("无数据 :PORTUUID[" + _UUID.PORTUUID + "]");
                        }

                    }

                } catch (Exception ex) {
                    String mes = ex.getMessage().toString();
                    log.info("异常：" + mes);
                }
            }
        };
    }

    /**
     *
     * @param _uuid------------------------------------------------NODEUUID信息
     * @param wbs_url----------------------------------------------
     * @return
     */
    //---------------------------------------------------------------------------
    public static String getWBSData(String _uuid, String wbs_url) {
        String str_mes = "";
        try {
            String _url = wbs_url;
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(_url));
            call.setOperationName("getCurrentPm");//WSDL里面描述的接口名称

            String _temp = _uuid;
            System.out.println("_temp:" + _temp);

            String _xmlDoc = (String) call.invoke(new Object[]{_temp});
            str_mes = _xmlDoc;

            System.out.println(str_mes);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_mes;
    }

}
