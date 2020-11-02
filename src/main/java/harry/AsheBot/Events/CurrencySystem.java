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
            if(args.length > 1){
                EmbedBuilder bal = new EmbedBuilder();
                String otherID = "";
                for (int i = 0; i < args[1].length(); i++) {
                    char a = args[1].charAt(i);
                    if(a >= '0' && a <= '9'){
                        otherID += a+"";
                    }
                }
                Member other = event.getGuild().getMemberById(otherID);
                bal.setTitle(other.getEffectiveName()+ "'s \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2 Balance");
                //bal.setAuthor(event.getAuthor().getAvatarUrl());
                bal.setThumbnail(event.getGuild().getIconUrl());
                bal.setFooter("Enjoy!", event.getAuthor().getAvatarUrl());
                int balance = getBal(other.getId());
                bal.setDescription("["+other.getAsMention()+"] has ***" + balance + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2***!");
                event.getChannel().sendMessage(bal.build()).queue();
            }
            else{
                EmbedBuilder bal = new EmbedBuilder();
                bal.setTitle(event.getMember().getEffectiveName()+ "'s \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2 Balance");
                //bal.setAuthor(event.getAuthor().getAvatarUrl());
                bal.setThumbnail(event.getGuild().getIconUrl());
                bal.setFooter("Enjoy!", event.getAuthor().getAvatarUrl());
                int balance = getBal(event.getMember().getId());
                bal.setDescription("["+event.getMember().getAsMention()+"] has ***" + balance + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2***!");
                event.getChannel().sendMessage(bal.build()).queue();
            }

        }

        if(args[0].equalsIgnoreCase("~beg")){
            if(!canRunCur(event.getMember().getId())){
                event.getChannel().sendMessage("YOU CANT RUN THIS COMMAND THIS FAST! THE WAIT TIME IS 8 HOURS").queue();
            }
            else{
                setCurTimer(event.getMember().getId());
                int rand = (int)(Math.random() * 500);
                event.getChannel().sendMessage("Ashe gave you " + rand + " Ashii").queue();
                int newBal = getBal(event.getMember().getId()) + rand;
                updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), newBal);
            }

            
        }

        //start of transfer
        if(args[0].equalsIgnoreCase("~transfer")){
            if(args.length == 1){
                event.getChannel().sendMessage("Correct Syntax is: ~transfer {transfer amount} 5XP = 1\uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
            }
            else{
                int amount = Integer.parseInt(args[1]);
                int xp = XPSystem.getXp(event.getMember().getId());
                if(amount*5 > xp){
                    event.getChannel().sendMessage("You don't have enough XP to transfer for " + amount + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
                }
                else{
                    int newXp = xp - amount*5;
                    int newBal = getBal(event.getMember().getId()) + amount;
                    updateBal(event.getMember().getId(), newXp, newBal);
                    EmbedBuilder updated = new EmbedBuilder();
                    updated.setTitle("Transfer Accepted!");
                    updated.setThumbnail(event.getGuild().getIconUrl());
                    updated.setDescription("Transferred Amount: " + "***" + (amount*5) + "*** XP for ***" + amount + "***\uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2");
                    updated.setFooter("Success!", event.getAuthor().getAvatarUrl());
                    event.getChannel().sendMessage(updated.build()).queue();
                }
            }
        }
        //end of transfer
        //start of shop orders
        if(args[0].equalsIgnoreCase("~buy")){
            Member mooN = event.getGuild().getMemberById("581673853537746944");

            int cur = getBal(event.getMember().getId());
            event.getChannel().sendMessage("Check Your DM!").queue();
            String finalOrderID = randID();
            mooN.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(event.getMember().getUser().getName() + " has purchased a something! Check google forms! Order ID = " + finalOrderID + ". Their current balance is " + cur).queue());
            event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("What item are you buying? (Enter a number on the shop) Your orderID is " + finalOrderID).queue());
        }

        if(args[0].equalsIgnoreCase("~give")){
            int amount = Integer.parseInt(args[2]);

            if(amount > getBal(event.getMember().getId())){
                event.getChannel().sendMessage("YOU DONT HAVE ENOUGH!").queue();
            }
            else if(amount < 0){
                event.getChannel().sendMessage("STOP TRYNA BREAK ME").queue();
            }
            else{
                StringBuilder otherID = new StringBuilder();

                System.out.println(args[1]);

                for (int i = 0; i < args[1].length(); i++) {
                    char a = args[1].charAt(i);
                    if(a >= '0' && a <= '9'){
                        otherID.append(a);
                    }
                }

                System.out.println(otherID);

                DBObject query = new BasicDBObject("memberID", otherID.toString());
                DBCursor cursor = AsheBot.users.find(query);

                if(cursor.count() == 0){
                    AsheBot.addNew(otherID.toString());
                }
                //

                updateBal(otherID.toString(), XPSystem.getXp(otherID.toString()), getBal(otherID.toString())+amount);
                updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), getBal(event.getMember().getId())-amount);

                event.getChannel().sendMessage("Okay, you have given " + args[1] + " " + amount + "\uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2!").queue();
            }
        }
    }

    public static String randID(){
        String orderID = "";
        for (int i = 0; i < 16; i++) {
            int rand = (int)(Math.random() * 10);
            orderID += rand + "";
        }
        return orderID;
    }

    public static void updateBal(String member, int newXp, int newBal){
        String id = member;
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
    public static int getBal(String member){
        //System.out.println(member);
        String id = member;
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
