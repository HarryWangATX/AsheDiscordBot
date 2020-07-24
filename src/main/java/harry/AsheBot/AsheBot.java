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
//        DBObject query = new BasicDBObject("lvl", 123);
//        DBCursor cursor = users.find(query);
//        DBObject one = cursor.one();
//        System.out.println((String)one.get("lvl"));
//        DBObject converted = convert(test);
//        System.out.println("adsfsdf");
//        users.insert(converted);
//        DBObject query = new BasicDBObject();
//        DBCursor cursor = users.find();
//        users.findAndRemove(query);
//        System.out.println(cursor.count());
        jda = new JDABuilder(AccountType.BOT).setToken("TOKEN").build();
        jda.getPresence().setStatus(OnlineStatus.ONLINE);
        jda.getPresence().setActivity(Activity.playing("WATCHING OVER ASHE!!"));
        jda.addEventListener(new allCommand());
        jda.addEventListener(new GuildMemberJoin());
        jda.addEventListener(new GuildMemberLeave());
        jda.addEventListener(new GuildMessageReactionAdd());
        jda.addEventListener(new XPSystem());
        //jda.addEventListener(new GuildMessageReceive());
    }
    public static DBObject convert(User user){
        //System.out.println("adsfsdf");
        return new BasicDBObject("AFK", user.getAfk()).append("XP", user.getXp()).append("memberID", user.getMemberID()).append("Timer", user.getTimer());
    }
}
