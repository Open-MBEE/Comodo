package comodo2.templates.qpc.qm;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class will be used to represent a model or state machine so that
 * relative paths between elements can be retrieved. This is because QM expects a
 * relative path to be passed as a target.
 */

public class QmTree {

    String nodeName;
    QmTree parent; // not used for now, could maybe remove it.
    ArrayList<QmTree> children;
    Integer depth;
    // QmTree rootNode;

    public QmTree(String nodeName) {
        this.nodeName = nodeName;
        this.children = new ArrayList<QmTree>();
        this.depth = 0;
    }

    public String toString() {
        return this.nodeName.toString();
    }

    public QmTree addChild(String child) {
        QmTree childNode = new QmTree(child);

        childNode.parent = this;
        childNode.depth = this.depth + 1;

        this.children.add(childNode);

        return childNode;
    }

    /**
     * Recursively checks within all of this instance's children for a node that has the name nodeName
     * @param nodeName name to match
     * @return Matched node, null if no match
     */
    public QmTree getNodeByName(String nodeName) {

        if (this.children.isEmpty()) {
            return null; // case where we have explored this entire subtree but not found
        }

        QmTree found_node = this.children.stream().filter(qmTree -> nodeName.equals(qmTree.nodeName)).findFirst().orElse(null);

        if (!Objects.equals(null, found_node)) {
            return found_node;
        } else {
            for (final QmTree child : this.children) {

                QmTree rec_found_node = child.getNodeByName(nodeName);

                if (!Objects.equals(rec_found_node, null)) {
                    return rec_found_node;
                }
            }

        }
        return null;
    }

    /**
     * Return the relative path between source and target by going up to the calling
     * element (this) and exploring the subtrees Most likely, this will only be
     * called from the root of the state machine
     * 
     * @return Relative path between source and target, as a String.
     */
    public String getRelativePath(QmTree source, QmTree target) {
        String path_to_root = stringRepeat(source.depth, "../");

        return path_to_root + this.findDownwardsPath(target);
    }

    /**
     * Recursively searches through this isntance's children for an element.
     * 
     * @param el target to search for
     * @return String path from this instance towards target
     */
    public String findDownwardsPath(QmTree el) {

        if (this.children.isEmpty()) {
            return ""; // case where we have explored this entire subtree but not found
        }

        if (this.children.contains(el)) {
            return Integer.toString(this.children.indexOf(el));
        } else {

            for (final QmTree child : this.children) {

                String down_path = child.findDownwardsPath(el);

                if (!Objects.equals("", down_path)) {
                    return Integer.toString(this.children.indexOf(child)) + "/" + child.findDownwardsPath(el);
                }
            }
            return "";
        }
    }

    /**
     * Used to get a relative path up to root element
     * 
     * @param count Number of times to repeat
     * @param with  String to be repeated
     * @return
     */
    public static String stringRepeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    // public static void main(String[] args) {

    //     QmTree stateMachine = new QmTree("SM");

    //     QmTree init = stateMachine.addChild("initial");
    //     QmTree off = stateMachine.addChild("off");
    //     QmTree on = stateMachine.addChild("on");

    //     QmTree off_timeout = off.addChild("off_timeout");
    //     QmTree on_timeout = on.addChild("on_timeout");

    //     QmTree off_eetimeout = off_timeout.addChild("offee_timeout");

    //     System.out.println(stateMachine.children);

    //     System.out.println("Downwards: " + stateMachine.findDownwardsPath(off_eetimeout));
    //     System.out.println("Relative: " + stateMachine.getRelativePath(on_timeout, on));

    //     System.out.println(stateMachine.findDownwardsPath(stateMachine.getNodeByName("on_timeout")));

    // }
}
