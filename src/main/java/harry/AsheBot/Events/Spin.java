package harry.AsheBot.Events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Scanner;

public class Spin extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");


        if(args[0].equalsIgnoreCase("~spin")){

            Member mooN = event.getGuild().getMemberById("581673853537746944");

            int bal = CurrencySystem.getBal(event.getMember().getId());
            if(bal < 150){
                event.getChannel().sendMessage("YOUR TOO BROKE!! U NEED AT LEAST 150ASHII IN UR WALLET TO SPIN").queue();
            }
            else{

                CurrencySystem.updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), bal - 150);


                String prize;
                int rand3 = (int)(Math.random() * 10001);
                if(rand3 <= 8500){
                    prize = "Negative 130 Ashii";
                }
                else if(rand3 <= 9500){
                    prize = "30 Ashii";
                }
                else if(rand3 <= 9920){
                    prize = "Role";
                }
                else if(rand3 < 9999){
                    prize = "FUZZY DOGGO PICS!";
                }
                else{
                    prize = "Private Channel";
                }


                event.getChannel().sendMessage("Your prize is: ").queue();

//                for (int i = 0; i < rand2; i++) {
//                    String cur = prizes[rand++];
//                }S

                event.getChannel().sendMessage(prize+"!!!!!!!").queue();

                if(prize.equalsIgnoreCase("Negative 130 Ashii")){
                    CurrencySystem.updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), CurrencySystem.getBal(event.getMember().getId())+20);
                }
                else if(prize.equalsIgnoreCase("30 Ashii")){
                    CurrencySystem.updateBal(event.getMember().getId(), XPSystem.getXp(event.getMember().getId()), CurrencySystem.getBal(event.getMember().getId())+180);
                }
                else if(prize.equalsIgnoreCase("Role")){
                    event.getChannel().sendMessage("Okay, mooN will DM you soon to get your role configured!").queue();
                    mooN.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(event.getMember().getUser().getName() + " has won a role. DM him to get it configured.").queue());
                }
                else if(prize.equalsIgnoreCase("FUZZY DOGGO PICS!")){
                    event.getChannel().sendMessage("Okay, mooN will DM you your FUZZY PICS SOON.").queue();
                    mooN.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(event.getMember().getUser().getName() + " has won a fuzzy pic. DM him the FUZZY PICS.").queue());
                }
                else{
                    event.getChannel().sendMessage("Okay, mooN will DM you soon to get your role channel configured!").queue();
                    mooN.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(event.getMember().getUser().getName() + " has won a PRIVATE FRICKING CHANNEL. DM him to get it configured.").queue());
                }
            }


        }
    }
}
