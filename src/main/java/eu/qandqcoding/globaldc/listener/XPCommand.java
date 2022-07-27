package eu.qandqcoding.globaldc.listener;

import eu.qandqcoding.globaldc.utils.EmbedGenerator;
import eu.qandqcoding.globaldc.utils.LevelManager;
import eu.qandqcoding.globaldc.utils.StreamUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class XPCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getMessage().getContentRaw().equalsIgnoreCase("-xp")) return;
        if (event.getGuild().getId().equals("806123072955875328")) {
            try {
                int xp = LevelManager.getXP(event.getGuild(), event.getMember());
                int nextXP = LevelManager.getNextRoleXP(event.getMember());
                BufferedImage image = ImageIO.read(new URL("https://i.ibb.co/Zm73NFV/Pics-Art-10-21-08-06-54.png"));
                BufferedImage logo = ImageIO.read(new URL(event.getMember().getUser().getAvatarUrl()));
                Graphics2D g2d = image.createGraphics();

                g2d.setFont(Font.decode("ARIAL"));

                g2d.drawImage(logo, 15, 15, null);
                g2d.drawString("" + event.getMember().getUser().getName(), 20, 180);
                if(LevelManager.getRoleFromMember(event.getMember()) != null) {
                    g2d.drawString("" + LevelManager.getRoleFromMember(event.getMember()).getName(), 450, 80);
                } else {
                    g2d.drawString("Level 0", 500, 80);
                }

                g2d.drawString("" + xp + "/" + nextXP, 200, 80);
                File tempFile = StreamUtils.tempFileFromImage(image, "memberImage", ".png");
                event.getMessage().getChannel().sendMessage("Levelstand").addFile(tempFile).queue();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {

            event.getChannel().sendMessageEmbeds(EmbedGenerator.error("Diese Funktion kommt in Zukunft").build());
        }
    }
}
