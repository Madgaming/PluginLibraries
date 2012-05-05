package net.zetaeta.libraries.commands.local;


/**
 * 
 * 
 * @author Zetaeta
 *
 */
@Deprecated
public interface LocalPermission {
    
    
    /**
     * @return The overall permission String represented by this LocalPermission
     */
    public String getPermission();
    
    /**
     * @return The specific subpermission for this LocalPermission.
     */
    public String getSubPermission();
    
    /**
     * @return Whether this is the master local permission.
     */
    public boolean isMasterPermission();
    
    /**
     * @return This LocalPermission's parent LocalPermission.
     */
    public LocalPermission getParent();
    
}
