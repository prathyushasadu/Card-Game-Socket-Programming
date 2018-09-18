import java.io.*;
import java.util.*;

public class Message implements Serializable{
    private static final long serialVersionUID = -2777777777777699L;
    ArrayList<String>  player = new ArrayList<String>();

    public Message(ArrayList<String> playerobj) {
	    player = playerobj;
    }
    public ArrayList<String>  getplayer() {
        return player;
    }

}

