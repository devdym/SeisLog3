package main.general;

import main.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class PropertiesWorker {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    private static Properties prop = new Properties();
    public static String MySQLDB_IP;
    public static String USERNAME;
    public static String PASSWORD;
    public static String CONN_STRING;
    public static String VESSEL;
    public static String CurrentPROJECT;

    public static void storeDBPreferences(String dbIp, String dbUser, String dbPass){
        try(OutputStream output = new FileOutputStream("config.properties")) {
            prop.setProperty("db_ip", dbIp);
            prop.setProperty("db_user", dbUser);
            prop.setProperty("db_pass", dbPass);
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void ReadPreferences(){
        logger.warn("Read Prefe");
        try (InputStream input = new FileInputStream("config.properties")){
            prop.load(input);
            MySQLDB_IP = prop.getProperty("db_ip");
            USERNAME = prop.getProperty("db_user");
            PASSWORD = prop.getProperty("db_pass");
            CONN_STRING = "jdbc:mysql://"+MySQLDB_IP+"/Aurora";

            VESSEL = prop.getProperty("Vessel");
            CurrentPROJECT = prop.getProperty("CurrentProject");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void storeVesselPreferences(String vessel){
        try(OutputStream output = new FileOutputStream("config.properties")) {
            prop.setProperty("Vessel", vessel);
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void storeProjectPreferences(String project){
        try(OutputStream output = new FileOutputStream("config.properties")) {
            prop.setProperty("CurrentProject", project);
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveImportPath(String batt, String ins, String seal, String ten) {
        try(OutputStream output = new FileOutputStream("config.properties")) {
            prop.setProperty("BattPath", batt);
            prop.setProperty("TensionPath", ten);
            prop.setProperty("InsTestPath", ins);
            prop.setProperty("SealLogPath", seal);
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
