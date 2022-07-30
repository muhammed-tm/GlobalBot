package eu.qandqcoding.globaldc;

import eu.qandqcoding.globaldc.listener.ClearCommand;
import eu.qandqcoding.globaldc.listener.MessageReceived;
import eu.qandqcoding.globaldc.listener.VoiceChannelXP;
import eu.qandqcoding.globaldc.listener.XPCommand;
import eu.qandqcoding.globaldc.utils.MongoDB;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/*
    Created by Andre
    Project DCGlobalBot
*/
public class DiscordBot extends ListenerAdapter {

    public static JDA jda;
    public static String token = "OTkzNjEyNDk2MzI3OTUwMzk2.G2KGwS.BrhE7tpbsQIte47RMFf356jBlI0DxZgfVd-lqc";

    public static void main(String[] args) {
        buildJDA();
    }

    public static void buildJDA() {
        new MongoDB();
        try {
            jda = JDABuilder.createDefault(token, Arrays.asList(GatewayIntent.values())).setRawEventsEnabled(true).build();
        } catch (LoginException ignored) {
        }
        jda.updateCommands().queue();
        jda.addEventListener(new MessageReceived());
        jda.addEventListener(new VoiceChannelXP());
        jda.addEventListener(new ClearCommand());
        jda.addEventListener(new DiscordBot());
        jda.addEventListener(new XPCommand());

    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<String> servers = new ArrayList<>();
        for (String guild : MongoDB.instance.collection.distinct("guild", String.class)) {
            servers.add(guild);
        }
        int members = 0;
        for (String server : servers) {
            members += jda.getGuildById(server).getMemberCount();
        }
        jda.getPresence().setPresence(Activity.listening(members + " Members "), true);
        startTimerForActivity();
    }

    int count = 0;
    public void startTimerForActivity() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        final Runnable actualTask = () -> {
            if(count > 1) count = 0;
            List<String> servers = new ArrayList<>();
            for (String guild : MongoDB.instance.collection.distinct("guild", String.class)) {
                servers.add(guild);
            }
            int members = 0;
            for (String server : servers) {
                members += jda.getGuildById(server).getMemberCount();
            }
            switch (count) {
                case 0 -> jda.getPresence().setPresence(Activity.listening(members + " Members "), true);
                case 1 -> jda.getPresence().setPresence(Activity.listening(servers.size() + " Servern "), true);
            }
            count++;
        };

        executorService.scheduleWithFixedDelay(new Runnable() {
            private final ExecutorService executor = Executors.newSingleThreadExecutor();
            private Future<?> lastExecution;

            @Override
            public void run() {
                if (lastExecution != null && !lastExecution.isDone()) {
                    return;
                }
                lastExecution = executor.submit(actualTask);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}
