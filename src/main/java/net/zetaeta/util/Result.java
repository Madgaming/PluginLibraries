package net.zetaeta.bukkit.util;

public class Result<T> {
    private T data;
    
    public Result(T initial) {
        data = initial;
    }
    public Result() {
        data = null;
    }
    
    public T get() {
        return data;
    }
    
    public void set(T data) {
        this.data = data;
    }
}
