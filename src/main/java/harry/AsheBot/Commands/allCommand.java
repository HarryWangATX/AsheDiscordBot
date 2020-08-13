package harry.AsheBot.Commands;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import harry.AsheBot.Events.User;
import harry.AsheBot.Events.XPSystem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.Field;
import java.text.Format;
import java.util.List;

public class allCommand extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        //System.out.println(args[0].equalsIgnoreCase("~mute"));
        //System.out.println(Arrays.toString(args));
        //info command
        if (args[0].equalsIgnoreCase(AsheBot.prefix + "info")) {
            EmbedBuilder hello = new EmbedBuilder();
            hello.setTitle("Ashe Server Info");
            hello.addField("Purpose: ", "GAMING!", false);
            hello.setDescription("This server is mainly a surviv.io server. We also play krunker, VALORANT. Feel free to give suggestions and follow the rules occasionally!");
            hello.setColor(0xf45642);
            hello.setFooter("From", event.getMember().getUser().getAvatarUrl());
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(hello.build()).queue();
            hello.clear();
        }
        //end of info command
        //clear command
        if (args[0].equalsIgnoreCase(AsheBot.prefix + "clear")) {
            if (args.length < 2) {
                EmbedBuilder usage = new EmbedBuilder();
                usage.setColor(0xff3923);
                usage.setTitle("🔴 Command Usage");
                usage.setDescription("~clear [# of messages]");
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage(usage.build()).queue();
            } else {
                try {
                    List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1]) + 1).complete();
                    event.getChannel().deleteMessages(messages).queue();
                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0x22ff2a);
                    success.setTitle("✅ Successfully deleted " + args[1] + " messages.");
                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessage(success.build()).queue();
                } catch (IllegalArgumentException e) {
                    if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle("🔴 Too Many Messages");
                        error.setDescription("You can only delete 1 - 99 at a time");
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage(error.build()).queue();
                    } else {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle("🔴 Messages are too old");
                        error.setDescription("Messages older than 2 weeks cannot be deleted");
                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage(error.build()).queue();
                    }
                }

            }
        }
        //end of clear command
        //unmute command
        if (args[0].equalsIgnoreCase("~unmute")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("Enter the correct syntax. ~unmute + '@User'").queue();
            } else {
                Member member = event.getGuild().getMemberById(args[1].replace("<@!", "").replace(">", ""));
                Role role = event.getGuild().getRoleById("707362535090814997");
                assert member != null;
                if (member.getRoles().contains(role)) {
                    assert role != null;
                    event.getGuild().removeRoleFromMember(member, role).complete();
                    event.getChannel().sendMessage("Unmuted " + args[1] + ".").queue();
                } else {
                    event.getChannel().sendMessage("Bruh, you can't unmute someone that is already unmuted. smh").queue();
                }
            }

        }
        //end of unmute command
        //muting command
        if (args[0].equalsIgnoreCase("~mute")) {

            Role role = event.getGuild().getRoleById("707362535090814997");
            if (args.length == 2) {
                Member member = event.getGuild().getMemberById(args[1].replace("<@!", "").replace(">", ""));
                assert member != null;
                //System.out.println(member.getAsMention());
                if (!member.getRoles().contains(role)) {

                    assert role != null;
                    try {
                        event.getGuild().addRoleToMember(member, role).complete();
                        event.getChannel().sendMessage("Muted " + args[1] + ".").queue();
                    } catch (HierarchyException e) {
                        //System.out.println(e.toString());
                        event.getChannel().sendMessage("Bruh, you don't have permission to mute people higher or with equal role than yourself nor do you even have permissions to MUTE LMAO. BOT!").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Bruh, they are already muted. SMFH!").queue();
                }
            } else if (args.length == 3) {
                int minutes = Integer.parseInt(args[2]);
                Member member = event.getGuild().getMemberById(args[1].replace("<@!", "").replace(">", ""));
                assert member != null;
                if (!member.getRoles().contains(role)) {

                    assert role != null;
                    try {
                        event.getGuild().addRoleToMember(member, role).complete();
                        event.getChannel().sendMessage("Muted " + args[1] + " for " + args[2] + " minutes.").queue();
                        //event.getGuild().removeRoleFromMember(member, role).completeAfter(minutes, TimeUnit.MINUTES);
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    public void run() {
                                        if (member.getRoles().contains(role)) {
                                            event.getGuild().removeRoleFromMember(member, role).complete();
                                            event.getChannel().sendMessage(args[1] + " you are now unmuted! Start Talking!").queue();
                                        }
                                    }
                                },
                                minutes * 60 * 1000
                        );
                    } catch (HierarchyException e) {
                        //System.out.println(e.toString());
                        event.getChannel().sendMessage("Bruh, you don't have permission to mute people higher or with equal role than yourself nor do you even have permissions to MUTE LMAO. BOT!").queue();
                    }

                } else {
                    event.getChannel().sendMessage("Bruh, they are already muted. SMFH!").queue();
                }
            } else {
                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessage("Incorrect Syntax, BOT! Make sure to mention and add time limit!").queue();
            }
        }
        //end of mute command
        //kick command
        if (args[0].equalsIgnoreCase("~kick")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("Bruh mention someone to kick!").queue();
            } else {
                Member member;
                try {
                    member = event.getGuild().getMemberById(args[1].replace("<@!", "").replace(">", ""));
                    assert member != null;
                    event.getGuild().kick(member).complete();
                    event.getChannel().sendMessage("Okay " + args[1] + " has been kicked!").queue();
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage("Bruh mention someone in this channel!").queue();
                }
            }
        }
        //end of kick command
        //ban command
        if (args[0].equalsIgnoreCase("~ban")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("Bruh mention someone to ban!").queue();
            } else {
                Member member;
                try {
                    member = event.getGuild().getMemberById(args[1].replace("<@!", "").replace(">", ""));
                    assert member != null;
                    event.getGuild().ban(member, 0).complete();
                    event.getChannel().sendMessage("Okay " + args[1] + " has been banned!").queue();
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage("Bruh mention someone in this channel!").queue();
                }
            }
        }
        //end of ban command
        //start of ping ABHI
        if(args[0].equalsIgnoreCase("~pingABHI")){
            Role ashe = event.getGuild().getRolesByName("smallest of pps", true).get(0);
            if(event.getMember().getRoles().contains(ashe)){
                event.getChannel().sendMessage("Initializing ABHI PING").queue();
                Member ABHI = event.getGuild().getMemberById("623999567725330436");
                for (int i = 0; i < 25; i++) {
                    event.getChannel().sendMessage(ABHI.getAsMention()).queue();
                }
                event.getChannel().sendMessage("ABHI!!!!!!!!").queue();
            }
            else{
                event.getChannel().sendMessage("You don't have perms to ping ABHI. REEEEEEEEEEEEEEEEE!").queue();
            }
        }
        //end of ping ABHI
        //start of shop command
        if(args[0].equalsIgnoreCase("~shop")){
            event.getChannel().deleteMessageById(event.getChannel().getLatestMessageId()).queue();
            EmbedBuilder shop = new EmbedBuilder();
            shop.setTitle("Welcome To Ashe Shop!");
            shop.setThumbnail(event.getGuild().getIconUrl());
            shop.setDescription("Item 1: **Special Role**\n\tCost: ***500 \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2***\nItem 2: **Fuzzy Doggo Pics**\n\tCost: ***6900 \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2***\nItem 3: **Private Channel**\n\tCost: ***696969 Ashii***\n\n\nTo Buy head over to Shop-Commands");
            shop.setFooter("Get Grinding! Earn \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2 By Chatting! Use ~transfer to exchange your XP for \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2!");
            event.getChannel().sendMessage(shop.build()).queue();

        }
    }
}
