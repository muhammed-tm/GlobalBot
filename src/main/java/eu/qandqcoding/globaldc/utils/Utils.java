package eu.qandqcoding.globaldc.utils;

/*
    Created by Andre
    At 00:35 Uhr | 10. Apr.. 2022
    Project Discordbot
*/

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Utils {

     public static String lineImage = "https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png";
     private static String OS;

     public static String getOS() {
          if (OS != null) {
               return OS;
          }

          String tmp = System.getProperty("os.name").toLowerCase();
          if (tmp.equals(Values.OS_LINUX)) {
               return (OS = Values.OS_LINUX);
          } else if (tmp.startsWith(Values.OS_WINDOWS)) {
               return (OS = Values.OS_WINDOWS);
          } else {
               return (OS = Values.UNKNOWN_OS);
          }
     }

     public static boolean isUnknownOS() {
          return getOS().equals(Values.UNKNOWN_OS);
     }
}
