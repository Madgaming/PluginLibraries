package net.zetaeta.bukkit.configuration;

import static org.junit.Assert.*;

import net.zetaeta.bukkit.configuration.PluginConfiguration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.Test;

public class PluginConfigurationTest {

    String yaml = "#Hello, world\n" +
                  "  #Me too\n" +
                  "\n" +
                  "net:\n" +
                  "  #Oh hello there\n" +
                  "  zetaeta:\n" +
                  "    settlement: derp\n" +
                  "  minecraft:\n" +
                  "    #Server stuff\n" +
                  "    server: <import server>\n" +
                  "# A footer!\n" +
                  "    # HERPADERP\n" +
                  "#DERPAHERP\n";
    
    @Test
    public void test() throws InvalidConfigurationException {
        PluginConfiguration pConf = new PluginConfiguration();
        pConf.options().indent(2);
        pConf.loadFromString(yaml);
        System.out.println(pConf.getComments());
        System.out.println(pConf.getFooter());
        System.out.println(pConf.getHeader());
//        fail("Not yet implemented");
    }

}
