package eu.qandqcoding.globaldc.listener;

import eu.qandqcoding.globaldc.utils.EmbedGenerator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ClearCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if(!event.getMessage().getContentRaw().contains("-clear")) return;
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            String[] args = event.getMessage().getContentRaw().split(" ");
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("-clear")) {
                    int lines = Integer.parseInt(args[1]);
                    if (lines < 100) {
                        for (Message message : event.getChannel().getHistory().retrievePast(lines + 1).complete()) {
                            message.delete().queue();
                        }
                    } else {
                        event.getChannel().sendMessageEmbeds(EmbedGenerator.error("Du kannst nur bis zu 100 Nachrichten löschen.").build()).queue();
                    }
                }
            } else {
                event.getChannel().sendMessageEmbeds(EmbedGenerator.error("Bitte nutze -clear <Zeilen>").build()).queue();
            }
        }
    }
}
