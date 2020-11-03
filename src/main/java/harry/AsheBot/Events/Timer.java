package harry.AsheBot.Events;



public class Timer {
    private String memberID;
    private int bet = 0;
    private int quick = 0;

    public String getMemberID() {
        return memberID;
    }

    public int getQuick() {
        return quick;
    }

    public int getBet(){
        return bet;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void setBet(int bet){
        this.bet = bet;
    }

    public void setQuick(int quick) {
        this.quick = quick;
    }
}
