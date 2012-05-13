package net.zetaeta.bukkit.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class UtilTest {

    @Test
    public void test() {
        String[] strings = {"hello", "world", "from", "zetaeta"};
        String[] reverseStrings = Util.reverse(strings);
        assertEquals(strings[0], reverseStrings[3]);
        System.out.println(Arrays.toString(strings));
        System.out.println(Arrays.toString(reverseStrings));
    }

}
