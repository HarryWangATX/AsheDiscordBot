package harry.AsheBot.Events;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;



public class Guess extends ListenerAdapter {
    private final EventWaiter waiter;

    public Guess(EventWaiter eventWaiter){
        this.waiter = eventWaiter;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if(args[0].equalsIgnoreCase("~guess")){
            if(args.length != 2){
                event.getChannel().sendMessage("THE CORRECT SYNTAX IS `~guess {higher number}`").queue();
            }
            else{
                int upper = Integer.parseInt(args[1]);
                int rand = (int)(Math.random()*upper);
                if(upper < 0){
                    event.getChannel().sendMessage("bruH ur number is from 0 - a POSITIVE number").queue();
                }
                else{
                    System.out.println(rand);
                    event.getChannel().sendMessage("Okay select a number between 0 - " + upper).queue();

                    waiter.waitForEvent(MessageReceivedEvent.class,
                            e -> e.getAuthor().equals(event.getAuthor())
                                    && e.getChannel().equals(event.getChannel())
                                    && !e.getMessage().equals(event.getMessage()),
                            e -> {
                                int guess;
                                try {
                                    guess = Integer.parseInt(e.getMessage().getContentRaw());
                                    if(guess != rand){
                                        event.getChannel().sendMessage("Sorry, you had the wrong guess...").queue();
                                    }
                                    else{
                                        event.getChannel().sendMessage("YAY, U GOT " + upper + " ASHII").queue();
                                        int newbal = CurrencySystem.getBal(event.getMember().getId()) + upper;
                                        CurrencySystem.updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), newbal);
                                    }
                                }
                                catch (Exception a){
                                    event.getChannel().sendMessage("DONT TRY TO BREAK ME!").queue();
                                }

                            },
                            // if the user takes more than a minute, time out
                            15, TimeUnit.SECONDS, () -> event.getChannel().sendMessage("Sorry, you took too long.").queue());
                }

            }
        }
    }
}
