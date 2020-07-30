package harry.AsheBot.Events;


import java.util.List;

public class Warn {
    private final List<String> warns;
    public Warn(List<String> warns){
        this.warns = warns;
    }

    public List<String> getWarns() {
        return warns;
    }
}
