package net.zetaeta.libraries.commands.local;

import net.zetaeta.libraries.Useless;

/**
 * Represents a local command instance, used to pass information to a command executor method.
 *
 * @author Zetaeta
 */
@Useless("May have facilities for parsing args in future.")
public interface LocalCommand {
    
    public String getName();
    
    public String[] getArgs();
    
}
