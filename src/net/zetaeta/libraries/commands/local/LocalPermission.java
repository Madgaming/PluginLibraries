package net.zetaeta.libraries.commands.local;


/**
 * 
 * 
 * @author Zetaeta
 *
 */
public class LocalPermission {
    
    protected String subPermission;
    protected LocalPermission parent;
    
    
    public LocalPermission(String permission, LocalPermission parent) {
        subPermission = permission;
        this.parent = parent;
    }
    
    
    public String getPermission() {
        return new StringBuilder().append(parent.getPermission()).append(".").append(subPermission).toString();
    }
    
    
    public String getParentPermission() {
        return parent.getPermission();
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
