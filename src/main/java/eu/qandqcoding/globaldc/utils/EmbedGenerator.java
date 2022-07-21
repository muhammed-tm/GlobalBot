package eu.qandqcoding.globaldc.utils;

import eu.qandqcoding.globaldc.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

/*
    Created by Andre
    Project DCGloablBot
*/
public class EmbedGenerator {
     public static EmbedBuilder justDescription(String message) {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setColor(Color.cyan);
          embedBuilder.setDescription(message);
          return embedBuilder;
     }

     public static EmbedBuilder unknownError() {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setAuthor("Unknown Error", null, DiscordBot.jda.getSelfUser().getEffectiveAvatarUrl());
          embedBuilder.setColor(new Color(191, 61, 39));
          embedBuilder.setDescription(Emotes.error + " An error occured when executing the command, please contact the bot developers [here](https://discord.gg/aKHn5KtRbE) or try again later!");
          return embedBuilder;
     }

     public static EmbedBuilder missingPermissions() {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setAuthor("Permissions Error", null, DiscordBot.jda.getSelfUser().getEffectiveAvatarUrl());
          embedBuilder.setColor(new Color(191, 61, 39));
          embedBuilder.setDescription(Emotes.error + " You don't have enough permissions to execute this command!");
          return embedBuilder;
     }

     public static EmbedBuilder error(String message) {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setAuthor("Error", null, DiscordBot.jda.getSelfUser().getEffectiveAvatarUrl());
          embedBuilder.setColor(new Color(191, 61, 39));
          embedBuilder.setDescription(Emotes.error + " " + message);
          return embedBuilder;
     }

     public static EmbedBuilder info(String message) {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setAuthor("Information", null, DiscordBot.jda.getSelfUser().getEffectiveAvatarUrl());
          embedBuilder.setColor(new Color(32, 102, 148));
          embedBuilder.setDescription(Emotes.getEmoteForCharacter("i") + " " + message);
          return embedBuilder;
     }

     public static EmbedBuilder warning(String message) {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setAuthor("Warning", null, DiscordBot.jda.getSelfUser().getEffectiveAvatarUrl());
          embedBuilder.setColor(new Color(255, 122, 0));
          embedBuilder.setDescription(Emotes.warning + " " + message);
          return embedBuilder;
     }

     public static EmbedBuilder success(String message) {
          EmbedBuilder embedBuilder = new EmbedBuilder();
          embedBuilder.setAuthor("Success", null, DiscordBot.jda.getSelfUser().getEffectiveAvatarUrl());
          embedBuilder.setColor(new Color(88, 170, 137));
          embedBuilder.setDescription(Emotes.success + " " + message);
          return embedBuilder;
     }
}
