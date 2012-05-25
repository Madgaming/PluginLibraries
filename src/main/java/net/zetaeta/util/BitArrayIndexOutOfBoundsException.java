package net.zetaeta.bukkit.util;

public class BitArrayIndexOutOfBoundsException extends IndexOutOfBoundsException {
    public BitArrayIndexOutOfBoundsException(String message) {
        super(message);
    }
    
    public BitArrayIndexOutOfBoundsException(BitArray<?> array) {
        this(array.getClass().getName());
    }
    
    public BitArrayIndexOutOfBoundsException(String message, BitArray<?> array) {
        this(message + " in: " + array.getClass().getName());
    }
}
