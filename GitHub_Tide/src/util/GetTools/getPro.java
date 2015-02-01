/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetTools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author yangzhen
 */
public class getPro {

    //---------------------------------------------�������begin--------------------------------------------//
    public  BufferedReader Pro_Read() {
        BufferedReader br = null;
        Process process = null;
        br = null;
        try {
            String[] command = {"/bin/sh", "-c", "ps -ef |grep java"};
            process = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }

    public  ArrayList Pro_Search(String str_pro_name, BufferedReader br) {

        ArrayList list = new ArrayList();
        //
        Pro_Read();
        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\\s{1,}", " ");
                String[] line_mes = line.split(" ");

                try {
                    String str_pro_name1 = line.toString();
                    String[] str_count = str_pro_name1.split(str_pro_name);
                    if (str_count.length > 1) {
                        String str_pro_pid = line_mes[1].toString();	//PID
                        String str_pro_ppid = line_mes[2].toString();	//PPID
                        String str_pro_stime = line_mes[4].toString();	//STIME																											
                        list.add(str_pro_name + "|" + str_pro_pid + "|" + str_pro_ppid + "|" + str_pro_stime);
                        System.out.println("��̣�" + str_pro_name + "|" + str_pro_pid + "|" + str_pro_ppid + "|" + str_pro_stime);
                    }
                } catch (Exception e) {

                }
            }

        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return list;
    }

    public  void Pro_Start(String command) {
        try {

            Process process = Runtime.getRuntime().exec(command);
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "Error");
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "Output");
            errorGobbler.start();
            outputGobbler.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public  boolean Pro_Killer(String str_pro_name, int time_for_second) {
        boolean kk = false;
        Process process = null;
        try {
            String command = "ps -ef|grep " + str_pro_name + "|grep -v grep|cut -c 9-15|xargs kill -9";
            Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
            System.out.println(command);
            kk = true;
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }

        return kk;
    }

    public  class StreamGobbler extends Thread {

        InputStream is;
        String type;

        public StreamGobbler(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        public void run() {
            try {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (type.equals("Error")) {
                        // System.out.println("Error   :" + line);  
                    } else {
                        // System.out.println("Debug:" + line);  
                    }
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }
    //---------------------------------------------end--------------------------------------------//

}
