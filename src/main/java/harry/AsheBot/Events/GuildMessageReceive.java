package harry.AsheBot.Events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;


public class GuildMessageReceive extends ListenerAdapter {
    HashMap<Member, Integer> playerXp = new HashMap<>();
    HashMap<Member, Integer> playerTimer = new HashMap<>();
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){


    }
    private void randXp(Member member){

    }
}
