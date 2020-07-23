package harry.AsheBot.Events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;


public class GuildMemberLeave extends ListenerAdapter {
    String[] messages = {
            "[member] left. FFFFFFFFFFF",
            "Hey! Listen! [member] has left!",
            "Rip! [member] has left!",
            "We've been expecting you, [member]. But not you leaving!",
            "It's dangerous to go alone, take [member]! Why did you leave!",
            "Swoooosh. [member] just left.",
            "Brace yourselves. [member] just left the server.",
            "A wild [member] left."
    };
    public void onGuildMemberJoin(GuildMemberLeaveEvent event) {
        Random rand = new Random();
        int number = rand.nextInt(messages.length);
        EmbedBuilder leave = new EmbedBuilder();
        leave.setColor(0xf48342);
        leave.setDescription(messages[number].replace("[member]", event.getMember().getAsMention()));

        event.getGuild().getDefaultChannel().sendMessage(leave.build()).queue();
    }
}
