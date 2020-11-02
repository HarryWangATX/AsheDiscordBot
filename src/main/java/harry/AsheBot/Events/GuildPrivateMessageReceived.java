package harry.AsheBot.Events;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class GuildPrivateMessageReceived extends ListenerAdapter {
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if(args[0].charAt(0) >= '0' && args[0].charAt(0) <= '9'){
            int item = Integer.parseInt(args[0]);
            int bal = getBal(event.getAuthor());
            if(item == 1){
                int newB = bal - 1699;
                if(bal < 1699){
                    event.getChannel().sendMessage("Sigh, you don't have enough \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
                }
                else{
                    updateBal(event.getAuthor(), newB);
                    event.getChannel().sendMessage("Okay, your order has been processed. You now have: " + newB + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2 left").queue();
                    event.getChannel().sendMessage("Fill out this form to get your role!\nhttps://forms.gle/YWJhfotc1RxghbcU8\nMake sure you include your order id in the form or your order will not be processed.\nPlease give about 2 days for your role to be processed").queue();
                }
            }
            else if(item == 2){
                int newB = bal - 6900;
                if(bal < 6900){
                    event.getChannel().sendMessage("Sigh, you don't have enough \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
                }
                else{
                    updateBal(event.getAuthor(), newB);
                    event.getChannel().sendMessage("Okay, your order has been processed. You now have: " + newB + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2 \nDM mooN with your orderID and he will send you fuzzy doggo pictures soon!").queue();
                }
            }
            else{
                int newB = bal - 696969;
                if(bal < 696969){
                    event.getChannel().sendMessage("Sigh, you don't have enough \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
                }
                else{
                    updateBal(event.getAuthor(), newB);
                    event.getChannel().sendMessage("Okay, your order has been processed. You now have: " + newB + " \uD835\uDCD0\uD835\uDCFC\uD835\uDCF1\uD835\uDCF2\uD835\uDCF2").queue();
                    event.getChannel().sendMessage("Fill out this form to get house!\nhttps://forms.gle/jYw5ehE1LzhSwRJD9\nMake sure you include your order id in the form or your order will not be processed.\n").queue();
                    event.getChannel().sendMessage("Please allow for up to 2 days for your house to be created!").queue();
                }
            }
        }
    }
    public void updateBal(User member, int newBal){
        String id = member.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        harry.AsheBot.Events.User temp = new harry.AsheBot.Events.User();
        temp.setBalance(newBal);
        temp.setMemberID(id);
        temp.setAfk((String)cursor.one().get("AFK"));
        temp.setXp((int)cursor.one().get("XP"));
        temp.setTimer((int)cursor.one().get("Timer"));
        List<String> warns = (List<String>)cursor.one().get("Warns");
        Warn warn = new Warn(warns);
        AsheBot.users.findAndModify(query, AsheBot.convert(temp, warn));
    }
    public int getBal(User user){
        String id = user.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        return (int)cursor.one().get("Bal");
    }
}
