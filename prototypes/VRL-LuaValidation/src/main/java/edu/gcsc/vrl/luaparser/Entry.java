/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gcsc.vrl.luaparser;

/**
 * Named Lua table entry.
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public interface Entry {

    /**
     * @return the name
     */
    String getName();

    /**
     * Set parent group.
     *
     * @param parent parent to set
     */
    void setParent(Group parent);

    /**
     * Returns the group parent.
     *
     * @return group parent or {@code null} if no parent has been specified
     */
    Group getParent();

    /**
     * Returns the distance to the root group, i.e., the first group that does
     * not have a parent.
     *
     * @return distance to the root group as integer
     */
    default int distanceToRoot() {
        int dist = 0;
        Group parent = getParent();

        while (parent != null) {
            parent = parent.getParent();
            dist++;
        }

        return dist;
    }

}
