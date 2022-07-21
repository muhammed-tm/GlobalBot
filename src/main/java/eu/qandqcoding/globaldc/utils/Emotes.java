package eu.qandqcoding.globaldc.utils;

import eu.qandqcoding.globaldc.DiscordBot;
import net.dv8tion.jda.api.entities.emoji.Emoji;

import java.util.HashMap;
import java.util.Map;

/*
    Created by Andre
    Project DCGloablBot
*/
public class Emotes {

     // Emotes for characters
     private static final Map<String, String> characterEmotes = new HashMap<>();
     // Ping emotes
     public static String pingGood = "<:PingGood:803930413612531782>";
     public static String pingOk = "<:PingOk:803930412979585055>";
     public static String pingBad = "<:PingBad:803930413126647829>";

     // System data emotes
     public static String jda = "<:JDA:803930227511918622>";
     public static String java = "<:Java:803930227239682068>";

     // Status emotes
     public static String statusOnline = "<:StatusOnline:803930226409078795>";
     public static String statusIdle = "<:StatusIdle:803930226392039424>";
     public static String statusDND = "<:StatusDND:803930226395578398>";
     public static String statusStreaming = "<:StatusStreaming:803930226416418826>";
     public static String statusOffline = "<:StatusOffline:803930226337120258>";

     // Embeds emotes
     public static String success = "<:white_check_mark:983019507658412052>";
     public static String warning = "<:warning:998670915900547133>";
     public static String error = "<:no_entry:983023093662515210>";

     static {
          characterEmotes.put("a", "\uD83C\uDDE6");
          characterEmotes.put("b", "\uD83C\uDDE7");
          characterEmotes.put("c", "\uD83C\uDDE8");
          characterEmotes.put("d", "\uD83C\uDDE9");
          characterEmotes.put("e", "\uD83C\uDDEA");
          characterEmotes.put("f", "\uD83C\uDDEB");
          characterEmotes.put("g", "\uD83C\uDDEC");
          characterEmotes.put("h", "\uD83C\uDDED");
          characterEmotes.put("i", "\uD83C\uDDEE");
          characterEmotes.put("j", "\uD83C\uDDEF");
          characterEmotes.put("k", "\uD83C\uDDF0");
          characterEmotes.put("l", "\uD83C\uDDF1");
          characterEmotes.put("m", "\uD83C\uDDF2");
          characterEmotes.put("n", "\uD83C\uDDF3");
          characterEmotes.put("o", "\uD83C\uDDF4");
          characterEmotes.put("p", "\uD83C\uDDF5");
          characterEmotes.put("q", "\uD83C\uDDF6");
          characterEmotes.put("r", "\uD83C\uDDF7");
          characterEmotes.put("s", "\uD83C\uDDF8");
          characterEmotes.put("t", "\uD83C\uDDF9");
          characterEmotes.put("u", "\uD83C\uDDFA");
          characterEmotes.put("v", "\uD83C\uDDFB");
          characterEmotes.put("w", "\uD83C\uDDFC");
          characterEmotes.put("x", "\uD83C\uDDFD");
          characterEmotes.put("y", "\uD83C\uDDFE");
          characterEmotes.put("z", "\uD83C\uDDFF");
          characterEmotes.put("0", "\u0030");
          characterEmotes.put("1", "\u0031");
          characterEmotes.put("2", "\u0032");
          characterEmotes.put("3", "\u0033");
          characterEmotes.put("4", "\u0034");
          characterEmotes.put("5", "\u0035");
          characterEmotes.put("6", "\u0036");
          characterEmotes.put("7", "\u0037");
          characterEmotes.put("8", "\u0038");
          characterEmotes.put("9", "\u0039");
          characterEmotes.put("?", "\u2754");
          characterEmotes.put("!", "\u2755");
          characterEmotes.put(" ", "\u25AB");
          characterEmotes.put("up", "\u2B06");
          characterEmotes.put("down", "\u2B07");
          characterEmotes.put("left", "\u2B05");
          characterEmotes.put("right", "\u27A1");
     }

     // To add custom emotes as reaction, it should be formatted like name:id
     public static String getReactionEmote(String emote) {
          return emote.replace("<:", "").replace(">", "");
     }

     public static Emoji getEmoji(String emote) {
          emote = emote.replace("<:", "").replace(">", "");
          String[] data = emote.split(":");
          Emoji getEmote = DiscordBot.jda.getEmojiById(data[1]);
          if (getEmote == null) {
               return null;
          }
          return Emoji.fromFormatted(String.valueOf(getEmote));
     }

     public static String getEmoteForCharacter(String character) {
          if (characterEmotes.containsKey(character)) {
               return characterEmotes.get(character);
          }
          return " ";
     }

}
