package harry.AsheBot.Events;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import harry.AsheBot.AsheBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Time;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class XPSystem {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split(" ");



        DBObject query = new BasicDBObject("memberID", event.getAuthor());
        if(AsheBot.users.find(query) == null){
            User newUser = new User();
            newUser.setXp(0);
            newUser.setAfk(null);
            newUser.setMemberID(event.getMember().getId());
            AsheBot.users.insert(AsheBot.convert(newUser));
        }
        else{
            if(canGetXP(event.getMember())){
                assert event.getMember() != null;
                setXP(event.getMember());

            }
        }
        if(args[0].equalsIgnoreCase("~XP")){
            String xp = getXp(event.getMember())+"";
            event.getChannel().sendMessage("Here is ur XP: " + xp).queue();
        }
    }

    public int getXp(Member member){
        String id = member.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        return (int)cursor.one().get("XP");
    }

    public int getTimer(String memberID){
        String id = memberID;
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        return (int)cursor.one().get("timer");
    }

    public void setXP(Member member){
        String id = member.getId();
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        User user = (User)cursor.one().get("User");
        int newXp = user.getXp() + randXP();
        user.setXp(newXp);
        AsheBot.users.findAndModify(query, AsheBot.convert(user));
    }

    public void setTimer(String memberID, int time){
        String id = memberID;
        DBObject query = new BasicDBObject("memberID", id);
        DBCursor cursor = AsheBot.users.find(query);
        User user = (User)cursor.one().get("User");
        user.setTimer(time);
        AsheBot.users.findAndModify(query, AsheBot.convert(user));
    }

    public int randXP(){
        Random r = new Random();
        return r.nextInt(10);
    }

    public boolean canGetXP(Member member){
        return getTimer(member.getId()) == 0;
    }

    public void startTimer(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            DBCursor cursor = AsheBot.users.find();
            @Override
            public void run() {
                while (cursor.hasNext()){
                    DBObject next = cursor.next();
                    setTimer((String)next.get("memberID"), getTimer((String)next.get("memberID"))-1);
                }
            }
        };
        timer.schedule(task, 1000, 1000);
    }
}
