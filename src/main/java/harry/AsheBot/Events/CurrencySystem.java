package harry.AsheBot.Events;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.regex.Pattern;

public class CurrencySystem extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equalsIgnoreCase("~bal")){
            EmbedBuilder bal = new EmbedBuilder();
            bal.setTitle(event.getMember().getEffectiveName()+ "'s \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2 Balance");
            //bal.setAuthor(event.getAuthor().getAvatarUrl());
            bal.setThumbnail(event.getGuild().getIconUrl());
            bal.setFooter("Enjoy!", event.getAuthor().getAvatarUrl());
            int balance = getBal(event.getMember());
            bal.setDescription("["+event.getMember().getAsMention()+"] has ***" + balance + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2***!");
            event.getChannel().sendMessage(bal.build()).queue();
        }

        if(args[0].equalsIgnoreCase("~beg")){
            if(!canRunCur(event.getMember().getId())){
                event.getChannel().sendMessage("YOU CANT RUN THIS COMMAND THIS FAST! THE WAIT TIME IS 8 HOURS").queue();
            }
            else{
                setCurTimer(event.getMember().getId());
                int rand = (int)(Math.random() * 500);
                event.getChannel().sendMessage("Ashe gave you " + rand + " Ashii").queue();
                int newBal = getBal(event.getMember()) + rand;
                updateBal(event.getMember(), XPSystem.getXp(event.getMember()), newBal);
            }

            
        }

        //start of transfer
        if(args[0].equalsIgnoreCase("~transfer")){
            if(args.length == 1){
                event.getChannel().sendMessage("Correct Syntax is: ~transfer {transfer amount} 50XP = 1\uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
            }
            else{
                int amount = Integer.parseInt(args[1]);
                int xp = XPSystem.getXp(event.getMember());
                if(amount*50 > xp){
                    event.getChannel().sendMessage("You don't have enough XP to transfer for " + amount + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
                }
                else{
                    int newXp = xp - amount*50;
                    int newBal = getBal(event.getMember()) + amount;
                    updateBal(event.getMember(), newXp, newBal);
                    EmbedBuilder updated = new EmbedBuilder();
                    updated.setTitle("Transfer Accepted!");
                    updated.setThumbnail(event.getGuild().getIconUrl());
                    updated.setDescription("Transferred Amount: " + "***" + (amount*50) + "*** XP for ***" + amount + "***\uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2");
                    updated.setFooter("Success!", event.getAuthor().getAvatarUrl());
                    event.getChannel().sendMessage(updated.build()).queue();
                }
            }
        }
        //end of transfer
        //start of shop orders
        if(args[0].equalsIgnoreCase("~buy")){
            Member mooN = event.getGuild().getMemberById("581673853537746944");
            event.getChannel().sendMessage("Check Your DM!").queue();
            mooN.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(event.getMember().getUser().getName() + " has purchased a something! Check google forms!").queue());
            event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("What item are you buying? (Enter a number on the shop)").queue());
        }

        if(args[0].equalsIgnoreCase("~give")){
            int amount = Integer.parseInt(args[2]);

            if(amount > getBal(event.getMember())){
                event.getChannel().sendMessage("YOU DONT HAVE ENOUGHT!").queue();
            }
            else{
                //Member to = event.getGuild().getMemberById()
            }
        }
    }

    public void updateBal(Member member, int newXp, int newBal){
        String id = member.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        User temp = new User();
        temp.setBalance(newBal);
        temp.setMemberID(id);
        temp.setAfk((String)cursor.one().get("AFK"));
        temp.setXp(newXp);
        temp.setTimer((int)cursor.one().get("Timer"));
        temp.setCurTimer((int)cursor.one().get("curTimer"));
        List<String> warns = (List<String>)cursor.one().get("Warns");
        Warn warn = new Warn(warns);
        AsheBot.users.findAndModify(query, AsheBot.convert(temp, warn));
    }
    public static int getBal(Member member){
        String id = member.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        return (int)cursor.one().get("Bal");
    }

    public int getCurTimer(String memberID){
        String id = memberID;
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        return (int)cursor.one().get("curTimer");
    }
    public void setCurTimer(String memberID){
        DBObject query = new BasicDBObject("memberID", memberID);
        DBCursor cursor = AsheBot.users.find(query);
        User temp = new User();
        temp.setBalance((int)cursor.one().get("Bal"));
        temp.setMemberID(memberID);
        temp.setAfk((String)cursor.one().get("AFK"));
        temp.setXp((int)cursor.one().get("XP"));
        temp.setTimer((int)cursor.one().get("Timer"));
        temp.setCurTimer(0);
        List<String> warns = (List<String>)cursor.one().get("Warns");
        Warn warn = new Warn(warns);
        AsheBot.users.findAndModify(query, AsheBot.convert(temp, warn));

        new java.util.Timer().schedule(
                new java.util.TimerTask(){
                    public void run(){
                        temp.setBalance((int)cursor.one().get("Bal"));
                        temp.setMemberID(memberID);
                        temp.setAfk((String)cursor.one().get("AFK"));
                        temp.setXp((int)cursor.one().get("XP"));
                        temp.setTimer((int)cursor.one().get("Timer"));
                        temp.setCurTimer(1);
                        List<String> warns = (List<String>)cursor.one().get("Warns");
                        Warn warn = new Warn(warns);
                        AsheBot.users.findAndModify(query, AsheBot.convert(temp, warn));
                        //System.out.println("Modified");
                    }
                },
                60*1000*60*10
        );
    }
    public boolean canRunCur(String memberID){
        return getCurTimer(memberID) == 1;
    }

}
