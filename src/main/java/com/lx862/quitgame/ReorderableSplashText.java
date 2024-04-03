package com.lx862.quitgame;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class ReorderableSplashText {
    public String text;
    public LinkedList<SplashTextCharacter> chars;
    public ReorderableSplashText(String text) {
        this.text = text;
        chars = new LinkedList<>();
        for(int i = 0; i < text.length(); i++) {
            chars.add(new SplashTextCharacter(text.charAt(i)));
        }
    }

    public void reorder(SplashTextCharacter object, int idx) {
        int fromIndex = chars.indexOf(object);
        if(fromIndex == idx) return;
        if(idx < 0) return;

        boolean forward = idx > fromIndex;
        chars.remove(fromIndex);

        if(forward) {
            if(idx+1 > chars.size()) {
                chars.add(object);
            } else {
                chars.add(idx+1, object);
            }
        } else {
            chars.add(idx, object);
        }
    }

    public boolean startsWith(String str) {
        String formedStr = chars.stream().map(e -> String.valueOf(e.getChar())).collect(Collectors.joining());
        return formedStr.trim().toLowerCase().startsWith(str.toLowerCase());
    }
}