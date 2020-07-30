package harry.AsheBot.Events;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;

public class WarningSystem extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");
        System.out.println(Arrays.toString(args));

        //start of method to display warnings
        if(args[0].equalsIgnoreCase("~WARNS") || args[0].equalsIgnoreCase("~WARN")){
            if (args.length == 1){
                event.getChannel().sendMessage("Bruh, mention the user to see their warns!").queue();
            }
            else {
                Member member;
                if(args[1].contains("!")){
                    member = event.getGuild().getMemberById(args[1].replace("<@!","").replace(">", ""));
                }
                else if(args[1].contains("&")){
                    member = event.getGuild().getMemberById(args[1].replace("<@&","").replace(">", ""));
                }
                else {
                    member = event.getGuild().getMemberById(args[1].replace("<@","").replace(">", ""));
                }
                EmbedBuilder warnEmbed = new EmbedBuilder();
                warnEmbed.setThumbnail(event.getGuild().getIconUrl());
                warnEmbed.setTitle(member.getEffectiveName() + "'s Warns");
                warnEmbed.setImage(member.getUser().getAvatarUrl());
                List<String> warns = getWarn(member);
                if(warns.size() == 0){
                    warnEmbed.setDescription("[" + member.getAsMention() + "] has NO warns. YAY!");
                }
                else{
                    String warnings = "";
                    for (int i = 0; i < warns.size(); i++) {
                        warnings += warns.get(i);
                        warnings += "\n";
                    }
                    warnEmbed.setDescription(warnings);
                }
                event.getChannel().sendMessage(warnEmbed.build()).queue();
            }
        }
        //end of display warnings


        //start of adding warnings
        if (args[0].equalsIgnoreCase("~addWarn")){
            Role req = event.getGuild().getRoleById("705444506933395496");
            if(!event.getMember().getRoles().contains(req)){
                event.getChannel().sendMessage("Sigh, you don't have perms to warn").queue();
            }
            else{
                if(args.length <= 2) {
                    event.getChannel().sendMessage("Bruh, mention the user to warn and put a reason. SMH").queue();
                }
                else{
                    if(!args[1].contains("@")){
                        event.getChannel().sendMessage("Bruh, mention the user first. Then put a reason. BOT").queue();
                    }
                    else{
                        Member member;
                        String reason = args[2];
                        if(args[1].contains("!")){
                            member = event.getGuild().getMemberById(args[1].replace("<@!","").replace(">", ""));
                        }
                        else if(args[1].contains("&")){
                            member = event.getGuild().getMemberById(args[1].replace("<@&","").replace(">", ""));
                        }
                        else {
                            member = event.getGuild().getMemberById(args[1].replace("<@","").replace(">", ""));
                        }
                        if(addWarn(member, reason, event.getMember())){
                            event.getChannel().sendMessage(member.getAsMention() + ", smh. You have over 5 warns. You will be banned in 5 secs!").queue();
                            event.getChannel().sendMessage("~ban " + member.getAsMention());
                        }
                        else{
                            event.getChannel().sendMessage(member.getAsMention() + ", smh. Don't get warned again. Ban at 5 warns.").queue();
                        }
                    }
                }
            }
        }
        //end of add warn
    }
    public List<String> getWarn(Member user){
        String id = user.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor member = AsheBot.users.find(query);
        if(member.count() < 1){
            AsheBot.addNew(user.getId());
        }
        List<String> res = (List<String>)member.one().get("Warns");
        return res;
    }
    public boolean addWarn(Member user, String reason, Member giver){
        String id = user.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor member = AsheBot.users.find(query);
        if(member.count() < 1){
            AsheBot.addNew(user.getId());
        }
        User temp = new User();
        temp.setMemberID(id);
        temp.setXp((int)member.one().get("XP"));
        temp.setTimer((int)member.one().get("Timer"));
        temp.setAfk((String)member.one().get("AFK"));
        List<String> warns = (List<String>)member.one().get("Warns");
        reason += " - Given by: " + giver.getEffectiveName();
        warns.add(reason);
        Warn warn = new Warn(warns);
        AsheBot.users.findAndModify(query, AsheBot.convert(temp, warn));
        if(warns.size() == 5){
            return true;
        }
        return false;
    }
}
