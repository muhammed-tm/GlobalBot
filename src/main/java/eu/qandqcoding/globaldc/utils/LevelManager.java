package eu.qandqcoding.globaldc.utils;

import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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

    public static void addVoiceXP(Guild guild, Member member, int time) {
        Document document = getCollectionByGuild(guild).find(new Document("member", member.getId())).first();
        if (document == null) {
            getCollectionByGuild(guild).insertOne(new Document("member", member.getId()).append("xp", time * 5));
        } else {
            getCollectionByGuild(guild).replaceOne(document, new Document("member", member.getId()).append("xp", getXP(guild, member) + (time * 5)));
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
            return guild.getRoleById(collection.find(new Document("xptoreach", xp)).first().getString("rolle"));
        } else return null;
    }

    public static void resetLevelRole(Member member) {
        if (member.getGuild().getId().equals("806123072955875328")) {
            //5
            Role _5 = member.getGuild().getRoleById("806451970582904852");
            if (member.getRoles().contains(_5)) {
                member.getGuild().removeRoleFromMember(member, _5).queue();
            }
            //25
            Role _25 = member.getGuild().getRoleById("998667218114052198");
            if (member.getRoles().contains(_25)) {
                member.getGuild().removeRoleFromMember(member, _25).queue();
            }
            //50
            Role _50 = member.getGuild().getRoleById("998668545883897888");
            if (member.getRoles().contains(_50)) {
                member.getGuild().removeRoleFromMember(member, _50).queue();
            }
            //75
            Role _75 = member.getGuild().getRoleById("998670278559285290");
            if (member.getRoles().contains(_75)) {
                member.getGuild().removeRoleFromMember(member, _75).queue();
            }
            //100
            Role _100 = member.getGuild().getRoleById("998670278559285290");
            if (member.getRoles().contains(_100)) {
                member.getGuild().removeRoleFromMember(member, _100).queue();
            }
            //125
            Role _125 = member.getGuild().getRoleById("998670627072397382");
            if (member.getRoles().contains(_125)) {
                member.getGuild().removeRoleFromMember(member, _125).queue();

            }
        }
    }

    public static int getNextRoleXP(Member member) {
        Role _5 = member.getGuild().getRoleById("806451970582904852");
        Role _25 = member.getGuild().getRoleById("998667218114052198");
        Role _50 = member.getGuild().getRoleById("998668545883897888");
        Role _75 = member.getGuild().getRoleById("998670278559285290");
        Role _100 = member.getGuild().getRoleById("998670278559285290");
        Role _125 = member.getGuild().getRoleById("998670627072397382");
        MongoCollection<Document> collection = getCollectionByGuild(member.getGuild());
        if (member.getRoles().contains(_5)) {
            return 25000;
        }
        //25
        if (member.getRoles().contains(_25)) {
            return 50000;
        }
        //50
        if (member.getRoles().contains(_50)) {
            return 75000;
        }
        //75
        if (member.getRoles().contains(_75)) {
            return 100000;
        }
        //100
        if (member.getRoles().contains(_100)) {
            return 125000;
        }
        //125
        if (member.getRoles().contains(_125)) {
            return Integer.MAX_VALUE;

        }
        return 500;
    }

    public static Role getRoleFromMember(Member member) {
        Role _5 = member.getGuild().getRoleById("806451970582904852");
        Role _25 = member.getGuild().getRoleById("998667218114052198");
        Role _50 = member.getGuild().getRoleById("998668545883897888");
        Role _75 = member.getGuild().getRoleById("998670278559285290");
        Role _100 = member.getGuild().getRoleById("998670278559285290");
        Role _125 = member.getGuild().getRoleById("998670627072397382");
        List<Role> roles = new ArrayList<>();
        roles.addAll(member.getRoles());
        if(roles.contains(_5)) {
            return _5;
        }
        if(roles.contains(_25)) {
            return _25;
        }
        if(roles.contains(_50)) {
            return _50;
        }
        if(roles.contains(_75)) {
            return _75;
        }
        if(roles.contains(_100)) {
            return _100;
        }
        if(roles.contains(_125)) {
            return _125;
        }
        return null;
    }
}

