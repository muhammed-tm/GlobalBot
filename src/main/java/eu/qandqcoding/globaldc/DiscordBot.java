package eu.qandqcoding.globaldc;

import eu.qandqcoding.globaldc.listener.MessageReceived;
import eu.qandqcoding.globaldc.utils.MongoDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

/*
    Created by Andre
    Project DCGlobalBot
*/
public class DiscordBot {

     public static JDA jda;
     public static String token = "OTkzNjEyNDk2MzI3OTUwMzk2.Gf_LiQ.FV2sr9GRgGfWefDMzgOOlzPKf4Pjrh8lskLA5o";

     public static void main(String[] args) {
          buildJDA();
     }

     public static void buildJDA() {
          new MongoDB();
          try {
               jda = JDABuilder.createDefault(token).setRawEventsEnabled(true).build();
          } catch (LoginException ignored) {
          }
          jda.updateCommands().queue();
          jda.addEventListener(new MessageReceived());
          List<String> servers = new ArrayList<>();
          for (String guild : MongoDB.instance.collection.distinct("guild", String.class)) {
               servers.add(guild);
          }
          jda.getPresence().setPresence(Activity.listening(servers.size() + " Servern "), true);
     }
}
