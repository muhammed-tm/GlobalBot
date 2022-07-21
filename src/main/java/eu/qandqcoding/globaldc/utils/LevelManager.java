package eu.qandqcoding.globaldc.utils;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bson.Document;

/*
    Created by Andre
    Project DCGlobalBot
*/
public class LevelManager {

     public int message = 10;

     public static int getXP(Guild guild, Member member) {
          Document document = getCollectionByGuild(guild).find(new Document("member", member.getId())).first();
          if (document != null) {
               return document.getInteger("xp") != null ? document.getInteger("xp") : 0;
          } else {
               return 0;
          }
     }

     public static void addXP(Guild guild, Member member, XpType type) {
          Document document = getCollectionByGuild(guild).find(new Document("member", member.getId())).first();
          if (document == null) {
               getCollectionByGuild(guild).insertOne(new Document("member", member.getId()).append("xp", type.getXp()));
          } else {
               getCollectionByGuild(guild).replaceOne(document, new Document("member", member.getId()).append("xp", getXP(guild, member) + type.getXp()));
          }
     }

     public static void removeXP(Guild guild, Member member, XpType type) {
          Document document = getCollectionByGuild(guild).find(new Document("member", member.getId())).first();
          if (document == null) {
               getCollectionByGuild(guild).insertOne(new Document("member", member.getId()).append("xp", type.getXp()));
          } else {
               getCollectionByGuild(guild).replaceOne(document, new Document("member", member.getId()).append("xp", getXP(guild, member) - type.getXp()));
          }
     }

     public static MongoCollection<Document> getCollectionByGuild(Guild guild) {
          return MongoDB.instance.database.getCollection(guild.getName() + " | " + guild.getId());
     }

     public static Role getRoleByXP(Guild guild, int xp) {
          MongoCollection<Document> collection = getCollectionByGuild(guild);
          Document find = new Document("xptoreach", xp);
          if (collection.find(find).first() != null) {
               if (collection.find(find).first().getInteger("xptoreach") == xp) {
                    String rolle = collection.find(find).first().getString("rolle");
                    if (rolle != null) {
                         return guild.getRoleById(rolle);
                    } else {
                         return null;
                    }
               }
          }
          return null;
     }
}

