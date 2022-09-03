package eu.qandqcoding.globaldc.listener;

import com.mongodb.client.MongoCollection;
import eu.qandqcoding.globaldc.utils.LevelManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.*;

public class VoiceChannelXP extends ListenerAdapter {

     public static HashMap<Member, Integer> memberTime = new HashMap<>();

     public static HashMap<Member, ExecutorService> memberExecutor = new HashMap<>();

     @Override
     public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

          MongoCollection<Document> collection = LevelManager.getCollectionByGuild(event.getGuild());
          if (collection.find(new Document("levelsystem", true)).first() != null) {
               memberTime.put(event.getMember(), 0);
               countMinutesInChannel(event.getMember());
          }
     }

     @Override
     public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
          MongoCollection<Document> collection = LevelManager.getCollectionByGuild(event.getGuild());
          if (collection.find(new Document("levelsystem", true)).first() != null) {
               memberExecutor.get(event.getMember()).shutdown();

               event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                    int xp = (memberTime.get(event.getMember()) * 5);
                    privateChannel.sendMessage("Du hast " + xp + " XP für " + memberTime.get(event.getMember()) + " Minuten im Voice erhalten.").queue();
                   LevelManager.addVoiceXP(event.getGuild(), event.getMember(), xp);
                    Role role = LevelManager.getRoleByXP(event.getGuild(), LevelManager.getXP(event.getGuild(), event.getMember()));
                    if (role != null) {
                         LevelManager.resetLevelRole(event.getMember());
                         event.getGuild().addRoleToMember(event.getMember(), role).queue();
                         EmbedBuilder levelUP = new EmbedBuilder();
                         levelUP.setTitle("LevelBot");
                         levelUP.setColor(Color.GREEN);
                         levelUP.setDescription("Du bist erfolgreich auf Level " + role.getName() + " hochgestuft worden.");
                         privateChannel.sendMessageEmbeds(levelUP.build()).queue();
                    }
               });

          }
     }

     public void countMinutesInChannel(Member member) {
          ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
          final Runnable actualTask = () -> {
               if (memberTime.containsKey(member)) {
                    int updateTime = memberTime.get(member);
                    updateTime += 1;
                    memberTime.put(member, updateTime);


               }
          };

          executorService.scheduleAtFixedRate(new Runnable() {
               private final ExecutorService executor = Executors.newSingleThreadExecutor();
               private Future<?> lastExecution;

               @Override
               public void run() {
                    if (lastExecution != null && !lastExecution.isDone()) {
                         return;
                    }
                    lastExecution = executor.submit(actualTask);
               }
          }, 1, 1, TimeUnit.MINUTES);

          memberExecutor.put(member, executorService);
     }
}
