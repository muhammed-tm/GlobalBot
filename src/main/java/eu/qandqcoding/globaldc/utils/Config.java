package eu.qandqcoding.globaldc.utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class Config {


     private static File APP_DIR = null;
     private static File DEFAULT_CONFIG = null;

     private static JSONObject defaults;

     private static File file;
     private static JSONObject json;

     private static boolean initialized;

     public static boolean isInitialized() {
          return initialized;
     }

     public static boolean init(File configFile, boolean fromGUI) { // don't crash after generation if fromGUI
          initialized = false;

          file = configFile;

          JSONObject read;
          try {
               read = new JSONObject(new JSONTokener(new FileReader(file)));
          } catch (Exception e) {
               // Failed to load config. Maybe it doesn't exist (already).
               // Create an empty one
               read = new JSONObject();
          }

          json = read;

          return true;
     }

     public static String get(String key) {
          return toValue(json.getString(key));
     }

     public static boolean getBoolean(String key) {
          return Boolean.parseBoolean(get(key));
     }

     public static void set(String key, String value) {
          setRaw(key, toValue(value));
     }

     public static void set(String key, int value) {
          set(key, String.valueOf(value));
     }

     private static void setRaw(String key, String value) {
          json.put(key, value);
     }

     private static boolean generate(ArrayList<String> toAdd) {
          for (String key : toAdd) {
               setRaw(key, defaults.getString(key));
          }

          return save();
     }

     public static boolean save() {
          try {
               json.write(new FileWriter(file), 2, 0).close();
               Logger.getGlobal().warning("Config saved");
               return true;
          } catch (Exception e) {
               e.printStackTrace();
               Logger.getGlobal().warning("Failed to save config file");
               return false;
          }
     }

     private static String toValue(String v) {
          try {
               return v.split(Values.CONFIG_COMMENT)[0].trim();
          } catch (Exception e) {
               //Logger.getGlobal().warning("Invalid config value: " +v);
               Logger.getGlobal().warning("Invalid config value"); // Do not print 'v' because it could contain sensitive data like the token
          }
          return "";
     }

     public static File getAppDir() {
          if (APP_DIR != null) {
               return APP_DIR;
          }

          switch (Utils.getOS()) {
               case Values.OS_LINUX:
                    APP_DIR = new File("/home/bots");
                    break;
               case Values.OS_WINDOWS:
                    APP_DIR = new File(System.getenv("APPDATA"), Values.BOT_NAME);
                    break;
               default:
                    APP_DIR = new File(System.getProperty("user.dir"), Values.BOT_NAME.toLowerCase());
                    break;
          }

          if (!APP_DIR.exists()) {
               if (!APP_DIR.mkdir()) {
               }
          }

          return APP_DIR;
     }

     public static File getDefaultConfig() {
          if (DEFAULT_CONFIG != null) {
               return DEFAULT_CONFIG;
          }

          DEFAULT_CONFIG = new File(getAppDir(), Values.DEFAULT_CONFIG_FILENAME);

          return DEFAULT_CONFIG;
     }

}