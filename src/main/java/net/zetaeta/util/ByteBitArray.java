package net.zetaeta.bukkit.util;

public class ByteBitArray implements BitArray<Byte> {
    private byte array;
    
    @Override
    public boolean get(int index) {
        if (index > 7) {
            throw new BitArrayIndexOutOfBoundsException("Index too large: " + index, this);
        }
        return Util.booleanValue(array & (1 << index));
    }

    @Override
    public void set(int index, boolean value) {
        if (index > 7) {
            throw new BitArrayIndexOutOfBoundsException("Index too large: " + index, this);
        }
        array &= ~(1 << index);
    }

    @Override
    public boolean getAndSet(int index, boolean value) {
        boolean b = get(index);
        set(index, value);
        return b;
    }
    
}
