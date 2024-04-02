package com.lx862.quitgame;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class NewEpicSplashText {
    public String text;
    public LinkedList<CharacterRenderer> chars;
    public NewEpicSplashText(String text) {
        this.text = text;
        chars = new LinkedList<>();
        for(int i = 0; i < text.length(); i++) {
            chars.add(new CharacterRenderer(text.charAt(i)));
        }
    }

    public void reorder(CharacterRenderer object, int idx) {
        int fromIndex = chars.indexOf(object);
        if(fromIndex == idx) return;
        if(idx < 0) return;

        boolean forward = idx > fromIndex;
        if(forward) {
            if(idx+1 > chars.size()-1) return;
            chars.remove(fromIndex);
            chars.add(idx+1, object);
        } else {
            chars.remove(fromIndex);
            chars.add(idx, object);
        }
    }

    public boolean startsWith(String str) {
        String formedStr = chars.stream().map(e -> String.valueOf(e.getChar())).collect(Collectors.joining());
        return formedStr.trim().toLowerCase().startsWith(str.toLowerCase());
    }
}