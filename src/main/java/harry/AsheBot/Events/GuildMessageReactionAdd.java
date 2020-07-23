package harry.AsheBot.Events;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMessageReactionAdd extends ListenerAdapter {
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){
//        if(event.getReactionEmote().getName().equals("🎉") &&
//                !event.getMember().getUser().equals(event.getJDA().getSelfUser())){
//            if(event.getMember().getUser().equals(event.getChannel().retrieveMessageById(event.getMessageId()).complete())){
//                event.getChannel().retrieveMessageById(event.getMessageId()).complete().delete().queue();
//            }
//        }
        //System.out.println(event.getReactionEmote().getName());
        if(event.getReactionEmote().getName().equalsIgnoreCase("blobhyperthink")){
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("HMMMMMMMMMMMMMMMMMMMMMMMMM").queue();
        }
    }
}
