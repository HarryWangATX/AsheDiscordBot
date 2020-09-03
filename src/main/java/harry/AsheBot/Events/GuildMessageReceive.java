package harry.AsheBot.Events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;


public class GuildMessageReceive extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String mess = event.getMessage().getContentRaw();
        if(mess.contains("<@")){
            int count = 0;
            for (int i = 0; i < mess.length() - 1; i++) {
                if(mess.substring(i, i+2).equalsIgnoreCase("<@")){
                    count++;
                }
            }
            if (count >= 7){
                event.getMember().ban(10, "Raiding").complete();
            }
        }
    }
}
