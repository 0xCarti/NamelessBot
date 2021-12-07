package main.economy;

import net.dv8tion.jda.api.entities.Message;
import utilities.Utils;

import java.util.ArrayDeque;

public class RouletteWheel {
    public ArrayDeque<String> wheel = new ArrayDeque<>();

    public RouletteWheel() {
        initialize();
    }

    public String spin() {
        String tmp = wheel.remove();
        wheel.addLast(tmp);
        return tmp;
    }


    public String getLeft(){
        return wheel.getLast();
    }
    public String getMiddle(){
        return wheel.getFirst();
    }
    public String getRight(){
        String tmp = wheel.removeFirst();
        String tmp2 = wheel.getFirst();
        wheel.addFirst(tmp);
        return tmp2;
    }

    public void reset(){
        wheel.clear();
        initialize();
    }

    private void initialize(){
        wheel.add(":green_circle:");
        wheel.add(":red_circle:");
        wheel.add(":black_circle:");
        wheel.add(":red_circle:");
        wheel.add(":black_circle:");
        wheel.add(":red_circle:");
        wheel.add(":black_circle:");
        wheel.add(":red_circle:");
        wheel.add(":black_circle:");
        wheel.add(":red_circle:");
        wheel.add(":black_circle:");
        wheel.add(":red_circle:");
        wheel.add(":black_circle:");
    }

    public int getSpins(){
        return Utils.getRandomInt(wheel.size()-1);
    }
}
