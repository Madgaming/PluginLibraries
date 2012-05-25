package net.zetaeta.bukkit.util;

public class Triple {
    public static final int NONE = 0;
    public static final int TRUE = 1;
    public static final int FALSE = -1;
    
    private int state;
    
    public boolean isTrue() {
        return state == TRUE;
    }
    
    public boolean isFalse() {
        return state == FALSE;
    }
    
    public boolean hasValue() {
        return Math.abs(state) == TRUE; 
    }
    
    public int get() {
        return state;
    }
    
    public void set(int state) {
        this.state = (state > 0 ? TRUE : (state < 0 ? FALSE : NONE));
    }
    
    public void set(boolean state) {
        this.state = state ? TRUE : FALSE;
    }
}
