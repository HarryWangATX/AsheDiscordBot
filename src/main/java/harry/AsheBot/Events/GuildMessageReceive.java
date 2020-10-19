package harry.AsheBot.Events;

import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;


public class GuildMessageReceive extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
//        if(event.getMember().getIdLong() == 643241518986952706L){
//            event.getMessage().addReaction("🛒").queue();
//        }

        String mess = event.getMessage().getContentRaw();
        if(mess.contains("<@")){
            int count = 0;
            for (int i = 0; i < mess.length() - 1; i++) {
                if(mess.substring(i, i+2).equalsIgnoreCase("<@")){
                    count++;
                }
            }
            if (count >= 6){
                event.getChannel().sendMessage("HAHAHHAH, you raider! You will be banned in 3 seconds!").queue();
                new java.util.Timer().schedule(
                        new java.util.TimerTask(){
                            public void run(){
                                event.getMember().ban(1, "raiding").complete();
                                event.getChannel().sendMessage("BYE BYE").queue();
                            }
                        },
                        3*1000
                );
            }
        }
    }
}
