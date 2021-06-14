package comodo2.templates.qf.qm;

import java.util.ArrayList;
import java.util.Objects;


/**
 * This class will be used to represent a model or state machine so that we can retrieve relative paths between elements
 * This is because QM expects a relative path to be passed as a target.
 */

public class QmTree {
    
    String nodeName;
    QmTree parent;
    ArrayList<QmTree> children;
    Integer depth;

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
        this.children.add(childNode);
        childNode.depth = this.depth + 1;
        return childNode;
    }

    /**
     * Return the relative path between source and target by going up to the calling element (this) and exploring the subtrees
     * Most likely, this will only be called from the root of the state machine
     * @return Relative path between source and target, as a String.
     */
    public String getRelativePath(QmTree source, QmTree target){
        String path_to_root = stringRepeat(source.depth, "../");

        return path_to_root + this.findDownwardsPath(target);
    }

    /**
     * Recursively searches through this isntance's children for an element.
     * @param el to search for
     * @return String path
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
                    System.out.println(this.children.indexOf(child));
                    return Integer.toString(this.children.indexOf(child)) + "/" + child.findDownwardsPath(el);
                }
            }
            return "";
        }
    }
    
    /**
     * Used to get a relative path up to root element
     * @param count Number of times to repeat
     * @param with String to be repeated
     * @return
     */
    public static String stringRepeat(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }
    
    public static void main(String[] args) {

        QmTree stateMachine = new QmTree("SM");

        QmTree init = stateMachine.addChild("initial");
        QmTree off = stateMachine.addChild("off");
        QmTree on = stateMachine.addChild("on");

        QmTree off_timeout = off.addChild("off_timeout");
        QmTree on_timeout = on.addChild("on_timeout");
        

        System.out.println(off.depth); 
        System.out.println(stateMachine.children); 

        System.out.println("Downwards: " + stateMachine.findDownwardsPath(on_timeout));
        System.out.println("Relative: " + stateMachine.getRelativePath(on_timeout, off));

        

    }    
    // other features ...
}
