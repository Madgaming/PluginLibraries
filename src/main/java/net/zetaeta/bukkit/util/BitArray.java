package net.zetaeta.bukkit.util;

public interface BitArray<N extends Number> {
    public boolean get(int index);
    
    public void set(int index, boolean value);
    
    public boolean getAndSet(int index, boolean value);
}
