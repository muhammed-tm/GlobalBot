package eu.qandqcoding.globaldc.listener;

import com.mongodb.client.MongoCollection;
import eu.qandqcoding.globaldc.DiscordBot;
import eu.qandqcoding.globaldc.utils.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
    Created by Andre
    Project DCGlobalBot
*/
public class MessageReceived extends ListenerAdapter {

     @Override
     public void onGuildJoin(@NotNull GuildJoinEvent event) {
          List<String> servers = new ArrayList<>();
          for (String guild : MongoDB.instance.collection.distinct("guild", String.class)) {
               servers.add(guild);
          }
          DiscordBot.jda.getPresence().setPresence(Activity.listening(servers.size() + " Servern "), true);
          if (MongoDB.instance.database.getCollection(event.getGuild().getName() + " | " + event.getGuild().getId()) == null) {
               MongoDB.instance.database.createCollection(event.getGuild().getName() + " | " + event.getGuild().getId());
          }
          for (Member member : event.getGuild().getMembers()) {
               if (member.getUser().isBot() || member.getUser() == DiscordBot.jda.getSelfUser()) {

               } else if (member.hasPermission(Permission.ADMINISTRATOR) || member.isOwner()) {
                    member.getUser().openPrivateChannel().queue(privateChannel -> {
                         EmbedBuilder builder = new EmbedBuilder();
                         builder.setTitle("QandQCoding GlobalBot");
                         builder.setDescription("× Mit dem Global-Chat kannst du serverweit mit anderen Usern auf ganz Discord chatten. \n" +
                                 "Der Bot teilt Nachrichten aus diesem Kanal mit vielen anderen Servern, wodurch User nicht zwingend auf dem Server sein müssen, um am Chat teilzunehmen. \n" +
                                 "Der Grund warum alle deine Nachrichten in so einer Art \"Boxen\" (Embeds) gesetzt wird ist simpel: Der Chat wird von einem anderen Team moderiert und soll nicht langweilig für fremde Server aussehen. \uD83D\uDC40 \n Wie richte ich den Bot ein?");
                         builder.setColor(Color.WHITE);
                         builder.addField("Levelsystem (de)aktivieren", "-togglelevel", false);
                         builder.addField("LevelSystem Rolle festlegen", "-setlevel <Rollenid> <XP>", false);
                         builder.addField("GlobalChat setzen", "-setglobal (In dem Channel)", false);
                         privateChannel.sendMessageEmbeds(builder.build()).queue();
                    });
               }
          }
     }

     @Override
     public void onMessageReceived(@NotNull MessageReceivedEvent event) {
          if (event.getAuthor().isBot()) return;
          if (event.getMessage().getContentRaw().startsWith("-setglobal")) {
               if (event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().getId().equals("367292204248727553")) {
                    List<String> servers = new ArrayList<>();
                    for (String guild : MongoDB.instance.collection.distinct("guild", String.class)) {
                         servers.add(guild);
                    }
                    DiscordBot.jda.getPresence().setPresence(Activity.listening(servers.size() + " Servern "), true);
                    if (MongoDB.instance.collection.find(new Document("guild", event.getGuild().getId())).first() == null) {
                         MongoDB.instance.collection.insertOne(new Document("guild", event.getGuild().getId()).append("globalchat", event.getChannel().asTextChannel().getId()));
                    } else {
                         String guildID = event.getGuild().getId();
                         MongoDB.instance.collection.replaceOne(new Document("guild", guildID), new Document("guild", guildID).append("globalchat", event.getChannel().asTextChannel().getId()));
                    }
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle("GlobalChat");
                    builder.setColor(Color.YELLOW);
                    builder.setDescription("Du hast erfolgreich " + event.getChannel().getAsMention() + " als GlobalChat gesetzt.");
                    Message message = event.getChannel().sendMessageEmbeds(builder.build()).complete();
                    message.delete().queueAfter(5, TimeUnit.SECONDS);

                    event.getMessage().delete().queue();
               }
               return;
          }
          MongoCollection<Document> collection = LevelManager.getCollectionByGuild(event.getGuild());
          if (event.getMessage().getContentRaw().startsWith("-togglelevel")) {
               if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    if (collection.find(new Document("levelsystem", true)).first() != null) {
                         collection.replaceOne(new Document("levelsystem", true), new Document("levelsystem", false));
                         event.getMessage().replyEmbeds(EmbedGenerator.success("LevelSystem wurde deaktiviert").setColor(Color.RED).build()).queue();
                    } else if (collection.find(new Document("levelsystem", false)).first() != null) {
                         collection.replaceOne(new Document("levelsystem", false), new Document("levelsystem", true));
                         event.getMessage().replyEmbeds(EmbedGenerator.success("LevelSystem wurde aktiviert").setColor(Color.GREEN).build()).queue();
                    } else {
                         collection.insertOne(new Document("levelsystem", true));
                         event.getMessage().replyEmbeds(EmbedGenerator.success("LevelSystem wurde aktiviert").setColor(Color.GREEN).build()).queue();
                    }
               }
          }
          if (event.getMessage().getContentRaw().startsWith("-setverify") && event.getMember().getId().equalsIgnoreCase("367292204248727553")) {
               String[] args = event.getMessage().getContentRaw().split(" ");
               if(args.length < 1) {
                    event.getMessage().delete().queue();
                    Config.set(event.getGuild().getId() + ".verified", "true");
                    Config.save();
               } else {
                    event.getMessage().delete().queue();
                    Config.set(args[1] + ".verified", "true");
                    Config.save();
               }
               return;
          }
          if (event.getMessage().getContentRaw().startsWith("-setlevel")) {
               if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    if (collection.find(new Document("levelsystem", true)).first() != null) {
                         String[] args = event.getMessage().getContentRaw().split(" ");
                         if (args.length == 3) {
                              int id = 1;
                              while (collection.find(new Document("id", id)).first() != null) {
                                   id += 1;
                              }
                              Document document = new Document("xptoreach", Integer.parseInt(args[2]));
                              if (collection.find(document).first() == null) {
                                   collection.insertOne(document.append("rolle", args[1]).append("id", id));
                              } else {
                                   collection.replaceOne(document, new Document("xp", Integer.parseInt(args[2])).append("rolle", args[1]));
                              }
                              event.getMessage().replyEmbeds(EmbedGenerator.success("Erfolgreich Rolle " + event.getGuild().getRoleById(args[1]).getName() + " mit XP " + args[2] + " gesetzt.").build()).queue();
                         } else {
                              event.getMessage().replyEmbeds(EmbedGenerator.error("Falsche Nutzung. Bitte nutze -setlevel <Rollenid> <XP>").build()).queue();
                         }
                    } else {
                         event.getMessage().replyEmbeds(EmbedGenerator.error("Das LevelSystem ist auf diesem Server ausgeschalten.").build()).queue();
                    }
               } else {
                    event.getMessage().replyEmbeds(EmbedGenerator.missingPermissions().build()).queue();
               }
               return;
          }
          Role role = null;
          if (collection.find(new Document("levelsystem", true)).first() != null) {
               LevelManager.addXP(event.getGuild(), event.getMember(), XpType.MESSAGE);
               if (LevelManager.getNextRoleXP(event.getMember()) == LevelManager.getXP(event.getGuild(), event.getMember())) {
                    role = LevelManager.getRoleByXP(event.getGuild(), LevelManager.getXP(event.getGuild(), event.getMember()));
               } else if (LevelManager.getNextRoleXP(event.getMember()) == LevelManager.getXP(event.getGuild(), event.getMember()) - 5) {
                    role = LevelManager.getRoleByXP(event.getGuild(), LevelManager.getXP(event.getGuild(), event.getMember()) - 5);
               }
               if (role != null) {
                    LevelManager.resetLevelRole(event.getMember());
                    event.getGuild().addRoleToMember(event.getMember().getUser(), role).queue();
                    EmbedBuilder levelUP = new EmbedBuilder();
                    levelUP.setTitle("LevelBot");
                    levelUP.setColor(Color.GREEN);
                    levelUP.setDescription("Du bist erfolgreich auf Level " + role.getName() + " hochgestuft worden.");
                    Message message = event.getChannel().sendMessageEmbeds(levelUP.build()).complete();
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
               }
          }

          Document document = new Document("guild", event.getGuild().getId());
          String globalistID = MongoDB.instance.collection.find(document).first().get("globalchat", String.class);
          if (event.getChannel().getId().equalsIgnoreCase(globalistID)) {
               if (event.getMember().getId().equalsIgnoreCase("367292204248727553") || event.getMember().getId().equalsIgnoreCase("567694483647627294") && event.getMessage().getContentRaw().startsWith("--gnews")) {
                    EmbedBuilder builder = new EmbedBuilder();
                    EmbedBuilder thumbnail = new EmbedBuilder();
                    builder.setImage("https://cdn.discordapp.com/attachments/711927299515088896/998302889107796048/banner_qandq.png");
                    builder.setDescription("GLOBALE NEWS");
                    builder.setDescription("**Neuigkeiten:**\n" + event.getMessage().getContentDisplay().replace("--gnews", ""));
                    builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
                    Button button = Button.link("https://discordapp.com/channels/" + event.getGuild().getId() + "/", "Discord Server");
                    for (String guildID : MongoDB.instance.collection.distinct("guild", String.class)) {
                         assert guildID != null;
                         Guild guild = DiscordBot.jda.getGuildCache().getElementById(guildID);
                         if (guild != null) {
                              TextChannel channel = guild.getTextChannelById(MongoDB.instance.collection.find(new Document("guild", guildID)).first().get("globalchat", String.class));
                              assert channel != null;
                              channel.sendMessageEmbeds(builder.build()).setActionRows(ActionRow.of(button)).queue();
                         }
                    }
                    event.getMessage().delete().queue();
               } else {
                    EmbedBuilder builder = new EmbedBuilder();
                    EmbedBuilder thumbnail = new EmbedBuilder();
                    builder.setDescription("Globale Nachricht");
                    builder.setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl());
                    builder.addField("User:", "**" + event.getMember().getUser().getAsTag() + "**", false);
                    builder.addField("Nachricht:", event.getMessage().getContentDisplay(), false);
                    if (Boolean.valueOf(Config.get(event.getGuild().getId() + ".verified"))) {
                         builder.setFooter(event.getGuild().getName() + " | Verifiziert ✅", event.getGuild().getIconUrl());
                    } else {
                         builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
                    }
                    String key = event.getGuild().getId() + ".invite";
                    String inviteLink = "";
                    try {
                         inviteLink = Config.get(key);
                    } catch(JSONException exception) {
                         Config.set(key, event.getChannel().asTextChannel().createInvite().setMaxUses(-1).setUnique(true).complete().getUrl());
                         Config.save();
                         inviteLink = Config.get(key);
                    }
                    Button button = Button.link(inviteLink, "Discord Server");
                    for (String guildID : MongoDB.instance.collection.distinct("guild", String.class)) {
                         assert guildID != null;
                         Guild guild = DiscordBot.jda.getGuildById(guildID);
                         if (guild != null) {
                              TextChannel channel = guild.getTextChannelById(MongoDB.instance.collection.find(new Document("guild", guildID)).first().get("globalchat", String.class));
                              assert channel != null;
                              channel.sendMessageEmbeds(builder.build()).setActionRows(ActionRow.of(button)).queue();
                         }
                    }
                    event.getMessage().delete().queue();
               }
          }
     }
}

