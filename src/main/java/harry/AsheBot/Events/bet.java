package harry.AsheBot.Events;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.List;
import java.util.Objects;

public class bet extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        DBObject query = new BasicDBObject("memberID", Objects.requireNonNull(event.getMember()).getId());
        DBCursor cursor = AsheBot.timers.find(query);
        if(cursor.count() == 0){
            AsheBot.addNewTimer(event.getMember().getId());
        }



        if(args[0].equalsIgnoreCase("~bet")){
            if(args.length != 2){
                event.getChannel().sendMessage("BRUH, ITS ~BET + AMOUNT, NOT WHAT U TYPED").queue();
            }
            else{
                int amount = Integer.parseInt(args[1]);
                if(amount < 0){
                    event.getChannel().sendMessage("BRUH U THOTS").queue();
                }
                else{
                    int bal = CurrencySystem.getBal(event.getMember().getId());
                    if(amount > 100000 || amount > bal){
                        event.getChannel().sendMessage("BRUH I CANT LET U BET THIS MUCH").queue();
                    }
                    else{
                        if(canBet(event.getMember().getId())){
                            setBet(event.getMember().getId());
                            int ashe = (int)(Math.random() * 12) + 1;
                            int them = (int)(Math.random() * 12) + 1;

                            double mult = (Math.random()*250)/100;
                            double won = Math.round(mult * amount);
                            boolean win = ashe < them;
                            String mess = (win) ? "YOU WON " + (int)won + " ASHII!!" : "YOU LOST EVERYTHING U BET. F";



                            EmbedBuilder bet = new EmbedBuilder();
                            bet.setTitle(event.getMember().getEffectiveName() +"'s Bet Against ASHEBOT");

                            bet.addField("You Rolled: ", "`"+them+"`", true);
                            bet.addField("ASHEBOT ROLLED", "`"+ashe+"`", true);
                            bet.addField("RESULT:", mess, false);
                            bet.setFooter("BET BET BET", event.getAuthor().getAvatarUrl());

                            if(win){
                                bet.setColor(Color.GREEN);
                            }
                            else{
                                bet.setColor(Color.RED);
                            }


                            if(win){
                                bal += won;
                            }
                            else{
                                bal -= amount;
                            }
                            CurrencySystem.updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), Math.max(bal, 0));
                            event.getChannel().sendMessage(bet.build()).queue();
                        }
                        else{
                            event.getChannel().sendMessage("BRUH, YOU CANT RUN THIS COMMAND THIS FAST!! 10SECOND COOLDOWN").queue();
                        }
                    }
                }


            }

        }

    }

    public boolean canBet(String memberID){
        DBObject query = new BasicDBObject("memberID", memberID);
        DBCursor cursor = AsheBot.timers.find(query);

        return (int)cursor.one().get("bet") == 1;
    }

    public void setBet(String memberID){
        DBObject query = new BasicDBObject("memberID", memberID);
        DBCursor cursor = AsheBot.timers.find(query);

        Timer temp = new Timer();
        temp.setBet(0);
        temp.setQuick((int)cursor.one().get("quick"));
        temp.setMemberID(memberID);

        AsheBot.timers.findAndModify(query, AsheBot.convertTimer(temp));

        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    public void run(){

                        temp.setQuick((int)cursor.one().get("quick"));
                        temp.setBet(1);

                        AsheBot.timers.findAndModify(query, AsheBot.convertTimer(temp));
                    }
                },
                10*1000
        );
    }
}
