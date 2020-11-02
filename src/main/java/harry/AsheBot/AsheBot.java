package harry.AsheBot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.*;
import harry.AsheBot.Events.*;
import harry.AsheBot.Commands.allCommand;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AsheBot {
    public static JDA jda;
    public static MongoClient mongoClient;
    public static String prefix = "~";
    public static DB database;
    public static DBCollection users;
    public static DBCollection timers;
    public static void main(String[] args) throws LoginException, UnknownHostException {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("AsheUsers");
        users = database.getCollection("User");
        timers = database.getCollection("Timer");

        //System.out.println(timers.count());

        init();

        EventWaiter eventWaiter = new EventWaiter();
        //addNew("707236302671577182");

        jda = JDABuilder.createDefault("BOTTOKEN").enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.watching("Over Ashe"));
        jda.addEventListener(eventWaiter);
        jda.addEventListener(new allCommand());
        jda.addEventListener(new GuildMemberJoin());
        jda.addEventListener(new GuildMemberLeave());
        jda.addEventListener(new GuildMessageReceive());
        jda.addEventListener(new GuildMessageReactionAdd());
        jda.addEventListener(new memberAFK());
        jda.addEventListener(new XPSystem());
        jda.addEventListener(new WarningSystem());
        jda.addEventListener(new CurrencySystem());
        jda.addEventListener(new Spin());
        jda.addEventListener(new GuildPrivateMessageReceived());
        jda.addEventListener(new bet());
        jda.addEventListener(new Guess(eventWaiter));
        //jda.addEventListener(new GuildMessageReceive());
    }

    public static DBObject convert(User user, Warn warns){
        return new BasicDBObject("AFK", user.getAfk()).append("XP", user.getXp()).append("memberID", user.getMemberID()).append("Timer", user.getTimer()).append("Bal", user.getBalance()).append("Warns", warns.getWarns()).append("curTimer", user.getCurTimer());
    }
    public static DBObject convertTimer(Timer timer){
        return new BasicDBObject("bet", timer.getBet()).append("quick", timer.getGuess()).append("memberID", timer.getMemberID());
    }

    public static void addNewTimer(String memberID){
        Timer newTimer = new Timer();
        newTimer.setBet(1);
        newTimer.setGuess(1);
        newTimer.setMemberID(memberID);
        AsheBot.timers.insert(AsheBot.convertTimer(newTimer));
    }

    public static void addNew(String memberID){
        User newUser = new User();
        Warn warn = new Warn(new ArrayList<>());
        newUser.setAfk("");
        newUser.setBalance(0);
        newUser.setMemberID(memberID);
        newUser.setXp(0);
        newUser.setTimer(1);
        newUser.setCurTimer(1);
        AsheBot.users.insert(AsheBot.convert(newUser, warn));
    }


    public static void init(){
        DBCursor all2 = users.find();

        while (all2.hasNext()){
            DBObject next = all2.next();

//            String memberID = (String)next.get("memberID");
//            DBObject temp3 = new BasicDBObject("memberID", memberID);
//            DBCursor temp2 = users.find(temp3);
//
//            if(temp2.count() > 1){
//                users.remove(next);
//                continue;
//            }

            Warn temp1 = new Warn((List<String >)next.get("Warns"));
            //Warn temp1 = new Warn(new ArrayList<>());
            User temp = new User();
            if(next.get("memberID").equals("643241518986952706")){
                temp.setBalance(2100000000);
            }
            else{
                temp.setBalance((int)next.get("Bal"));
            }
//            if(next.get("memberID").equals("707236302671577182")){
//                if((int)next.get("XP") == 0){
//                    users.remove(next);
//                    continue;
//                }
//            }

            temp.setTimer(1);
            temp.setCurTimer(1);
            temp.setAfk((String)next.get("AFK"));
            temp.setXp((int)next.get("XP"));
            temp.setMemberID((String)next.get("memberID"));
            users.findAndModify(next, convert(temp, temp1));
            System.out.println(next);
        }

        DBCursor all = timers.find();
        while (all.hasNext()){
            DBObject next = all.next();

            Timer temp = new Timer();
            temp.setMemberID((String)next.get("memberID"));
            temp.setGuess(1);
            temp.setBet(1);

            timers.findAndModify(next, convertTimer(temp));
        }
    }
}
