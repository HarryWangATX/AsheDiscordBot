package harry.AsheBot.Events;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.dv8tion.jda.api.entities.Member;

public class User {
    public int xp=0;
    public int balance=0;
    public int timer=0;
    public String afk = "";
    public String MemberID="";
    public User(){

    }
    public int getBalance() {
        return balance;
    }
    public int getXp() {
        return xp;
    }
    public String getAfk(){
        return afk;
    }
    public String getMemberID() {
        return MemberID;
    }
    public int getTimer(){
        return timer;
    }
    public void setMemberID(String id){
        MemberID = id;
    }
    public void setXp(int newX){
        xp = newX;
    }
    public void setAfk(String newA){
        afk = newA;
    }
    public void setTimer(int num){
        timer = num;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
}
