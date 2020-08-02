package comodo2.utils;

import java.util.TreeSet;

@SuppressWarnings("all")
public class Actions {
  /**
   * Return the name of the Action classes.
   * E.g. "ActionsStd.Init" -> "ActionsStd"
   */
  public TreeSet<String> getClassNames(final TreeSet<String> actionNames) {
    TreeSet<String> names = new TreeSet<String>();
    for (final String a : actionNames) {
      names.add(a.substring(0, a.indexOf(".")));
    }
    return names;
  }
  
  /**
   * Return the name of the Action methods of a given Action class.
   * E.g. "ActionsStd.Init" -> "Init"
   */
  public TreeSet<String> getMethodNames(final TreeSet<String> actionNames, final String className) {
    TreeSet<String> names = new TreeSet<String>();
    for (final String a : actionNames) {
      {
        String prefix = (className + ".");
        boolean _startsWith = a.startsWith(prefix);
        if (_startsWith) {
          names.add(a.substring(prefix.length(), a.length()));
        }
      }
    }
    return names;
  }
  
  public TreeSet<String> getMergeClassNames(final TreeSet<String> classNames1, final TreeSet<String> classNames2) {
    TreeSet<String> names = new TreeSet<String>();
    names.addAll(classNames1);
    names.addAll(classNames2);
    return names;
  }
}
