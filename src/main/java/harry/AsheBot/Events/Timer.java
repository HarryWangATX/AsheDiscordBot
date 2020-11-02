package harry.AsheBot.Events;



public class Timer {
    private String memberID;
    private int guess = 0;
    private int bet = 0;

    public String getMemberID() {
        return memberID;
    }

    public int getGuess() {
        return guess;
    }

    public int getBet(){
        return bet;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void setGuess(int quick) {
        this.guess = quick;
    }

    public void setBet(int bet){
        this.bet = bet;
    }
}
