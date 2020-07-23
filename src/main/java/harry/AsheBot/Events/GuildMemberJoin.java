package harry.AsheBot.Events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class GuildMemberJoin extends ListenerAdapter {
    String[] messages = {
            "[member] joined. You must construct additional pylons.",
            "Never gonna give [member] up. Never let [member] down!",
            "Hey! Listen! [member] has joined!",
            "Ha! [member] has joined! You activated my trap card!",
            "We've been expecting you, [member].",
            "It's dangerous to go alone, take [member]!",
            "Swoooosh. [member] just landed.",
            "Brace yourselves. [member] just joined the server.",
            "A wild [member] appeared."
    };
    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        Random rand = new Random();
        int number = rand.nextInt(messages.length);
        EmbedBuilder join = new EmbedBuilder();
        join.setColor(0x66d8ff);
        join.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));
        //System.out.println(event.getMember().getAsMention());
        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRolesByName("Testing", true).get(0)).queue();
    }
}
