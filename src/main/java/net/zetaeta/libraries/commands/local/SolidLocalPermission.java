package net.zetaeta.libraries.commands.local;


/**
 * 
 * 
 * @author Zetaeta
 *
 */
public class SolidLocalPermission implements LocalPermission {
    
    protected String subPermission;
    protected LocalPermission parent;
    
    
    /**
     * Constructs a SolidLocalPermission with specified LocalPermission parent.
     * 
     * @param permission Specific subpermission for this LocalPermission.
     * @param parent Parent LocalPermission.
     */
    public SolidLocalPermission(String permission, LocalPermission parent) {
        subPermission = permission;
        this.parent = parent;
    }
    
    
    public String getPermission() {
        if (isMasterPermission()) {
            return subPermission;
        }
        return new StringBuilder().append(parent.getPermission()).append(".").append(subPermission).toString();
    }
    
    
    public String getSubPermission() {
        return subPermission;
    }
    
    public boolean isMasterPermission() {
        return false;
    }
    
    
    public LocalPermission getParent() {
        return parent;
    }
    
}
