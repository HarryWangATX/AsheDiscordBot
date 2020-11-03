package harry.AsheBot.Events;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.Time;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class QuickMaths extends ListenerAdapter {
    private final EventWaiter waiter;
    public QuickMaths(EventWaiter eventWaiter){
        waiter = eventWaiter;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        DBObject query = new BasicDBObject("memberID", Objects.requireNonNull(event.getMember()).getId());
        DBCursor cursor = AsheBot.timers.find(query);
        if(cursor.count() == 0){
            AsheBot.addNewTimer(event.getMember().getId());
        }

        if(args[0].equalsIgnoreCase("~quick")) {

            if(!canQuick(event.getMember().getId())){
                event.getChannel().sendMessage("YOU CANT RUN THIS YET! COOLDOWN IS 10 SECS").queue();
            }
            else{
                setQuick(event.getMember().getId());
                int num1 = (int)(Math.random() * 10000);
                int num2 = (int)(Math.random() * 10000);
                int op = (int)(Math.random() * 3);



                int res;
                int reward = num1 + num2;
                int timeout = 10;
                if(op == 0){
                    event.getChannel().sendMessage("Okay, please calculate `" + num1 + " + " + num2 + "` in 10 seconds:").queue();
                    res = num1 + num2;
                }
                else if(op == 1){
                    event.getChannel().sendMessage("Okay, please calculate `" + num1 + " - " + num2 + "` in 10 seconds:").queue();
                    res = num1 - num2;
                }
                else{
                    event.getChannel().sendMessage("Okay, please calculate `" + num1 + " * " + num2 + "` in 12 seconds:").queue();
                    res = num1 * num2;
                    reward = res;
                    timeout = 12;
                }

                System.out.println(res);

                int bal = CurrencySystem.getBal(event.getMember().getId()) + (int)Math.ceil((double)Math.abs(reward)/4);

                int finalReward = reward;
                waiter.waitForEvent(MessageReceivedEvent.class,
                        e -> e.getAuthor().equals(event.getAuthor())
                                && e.getChannel().equals(event.getChannel())
                                && !e.getMessage().equals(event.getMessage()),
                        e -> {
                            int ans;
                            try {
                                ans = Integer.parseInt(e.getMessage().getContentRaw());
                                if(ans == res){
                                    event.getChannel().sendMessage("YAY! You are a true math wiz. I shall give you " + (int)Math.ceil((double)Math.abs(finalReward)/4) + " ASHII!").queue();
                                    CurrencySystem.updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), bal);
                                }
                                else{
                                    event.getChannel().sendMessage("lol, thats incorrect. You won nothing...").queue();
                                }
                            }
                            catch (Exception ee){
                                event.getChannel().sendMessage("dont try to break me :/").queue();
                            }

                        }, timeout, TimeUnit.SECONDS, () -> event.getChannel().sendMessage("Sorry, u took too long...").queue());
            }

        }
    }

    public boolean canQuick(String memberID){
        DBObject query = new BasicDBObject("memberID", memberID);
        DBCursor cursor = AsheBot.timers.find(query);
        return (int)cursor.one().get("quick") == 1;
    }

    public void setQuick(String memberID){
        DBObject query = new BasicDBObject("memberID", memberID);
        DBCursor cursor = AsheBot.timers.find(query);
        Timer temp = new Timer();
        temp.setMemberID(memberID);
        temp.setBet((int)cursor.one().get("bet"));
        temp.setQuick(0);
        AsheBot.timers.findAndModify(query, AsheBot.convertTimer(temp));
        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    public void run(){
                        temp.setBet((int)cursor.one().get("bet"));
                        temp.setQuick(1);
                        AsheBot.timers.findAndModify(query, AsheBot.convertTimer(temp));
                        //System.out.println("Modified");
                    }
                },
                10*1000
        );
    }
}
