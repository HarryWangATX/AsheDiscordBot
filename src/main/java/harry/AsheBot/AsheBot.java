package harry.AsheBot;

import com.mongodb.*;
import harry.AsheBot.Events.*;
import harry.AsheBot.Commands.allCommand;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

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
    public static void main(String[] args) throws LoginException, UnknownHostException {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        database = mongoClient.getDB("AsheUsers");
        users = database.getCollection("User");
//       User test = new User();
//       test.setAfk("Hello");
//       test.setXp(3124123);
//       test.setMemberID("2314132412341");
//        //users.insert(convert(test));
//        DBObject query = new BasicDBObject("lvl", 0);
//        users.findAndRemove(query);
//        System.out.println(users.find().count());
        //DBObject query = new BasicDBObject("memberID", "623999567725330436");
        //DBCursor cursor = users.find(query);
        //DBObject one = cursor.one();
        //System.out.println((String)one.get("lvl"));
        //DBObject converted = convert(test);
        //System.out.println("adsfsdf");
        //users.insert(converted);
        //DBObject query = new BasicDBObject("AFK", "hello");
        //users.findAndRemove(query);
        init();
        jda = new JDABuilder(AccountType.BOT).setToken("BOT_TOKEN").build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.watching("Over Ashe"));
        jda.addEventListener(new allCommand());
        jda.addEventListener(new GuildMemberJoin());
        jda.addEventListener(new GuildMemberLeave());
        jda.addEventListener(new GuildMessageReceive());
        jda.addEventListener(new GuildMessageReactionAdd());
        jda.addEventListener(new memberAFK());
        jda.addEventListener(new XPSystem());
        jda.addEventListener(new WarningSystem());
        jda.addEventListener(new CurrencySystem());
        jda.addEventListener(new GuildPrivateMessageReceived());
        //jda.addEventListener(new GuildMessageReceive());
    }

    public static DBObject convert(User user, Warn warns){
        return new BasicDBObject("AFK", user.getAfk()).append("XP", user.getXp()).append("memberID", user.getMemberID()).append("Timer", user.getTimer()).append("Bal", user.getBalance()).append("Warns", warns.getWarns()).append("curTimer", user.getCurTimer());
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
        DBCursor all = users.find();
        while (all.hasNext()){
            DBObject next = all.next();
//            if(next.get("XP") == null){
//                users.findAndRemove(next);
//                continue;
//            }

            Warn temp1 = new Warn((List<String >)next.get("Warns"));
            //Warn temp1 = new Warn(new ArrayList<>());
            User temp = new User();
            temp.setBalance((int)next.get("Bal"));
            temp.setTimer(1);
            temp.setCurTimer(1);
            temp.setAfk((String)next.get("AFK"));
            temp.setXp((int)next.get("XP"));
            temp.setMemberID((String)next.get("memberID"));
            users.findAndModify(next, convert(temp, temp1));
            System.out.println(next);
        }
    }
}
