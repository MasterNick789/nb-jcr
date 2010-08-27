package org.metacortex.nbjcr.model;

import java.beans.IntrospectionException;
import javax.jcr.Item;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author kyjo
 */
public class JCRNode<T extends Item> extends BeanNode<T> implements Comparable<JCRNode> {

    public static String resPath = "org/metacortex/nbjcr/resources/";
    protected static String iconWorkspace = resPath + "desktop_16x16.png";
    protected static String iconRepo = resPath + "database-network_16x16.png";
    protected static String iconNode = resPath + "node_16x16.png";
    protected static String iconProp = resPath + "node-delete-child_16x16.png";
    protected static String iconPropMulti = resPath + "node-property-multi.png";
    protected static String iconFolder = resPath + "closedFolder_16x16.gif";
    protected static String iconFile = resPath + "file.png";
    protected static String iconFileText = resPath + "gnome-mime-text-xml_16x16.png";
    protected static String iconFileAudio = resPath + "gnome-mime-audio-x-voc_16x16.png";

    public JCRNode(T item) throws IntrospectionException, RepositoryException {
        super(item);
        setChildren(new Kids());


        if (item instanceof javax.jcr.Node) {
            javax.jcr.Node n = ((javax.jcr.Node) item);
            for (PropertyIterator pi = n.getProperties(); pi.hasNext();) {
                getChildren().add(new Node[]{new JCRNode(pi.nextProperty())});
            }
            for (NodeIterator ni = n.getNodes(); ni.hasNext();) {
                getChildren().add(new Node[]{new JCRNode(ni.nextNode())});
            }
            String primaryType = n.getPrimaryNodeType().getName();
            setName(getName() + " (" + primaryType + ")");

            if (primaryType.equals("nt:file")) {
                String mimeType = n.getNode("jcr:content").getProperty("jcr:mimeType").getValue().toString();
                if (mimeType.contains("text")) {
                    setIconBaseWithExtension(iconFileText);
                } else if (mimeType.contains("audio")) {
                    setIconBaseWithExtension(iconFileAudio);
                } else {
                    setIconBaseWithExtension(iconFile);
                }
            } else if (primaryType.equals("nt:folder")) {
                setIconBaseWithExtension(iconFolder);
            } else
            {
                setIconBaseWithExtension(iconNode);
            }
        } else if (item instanceof javax.jcr.Property) {
            javax.jcr.Property p = ((javax.jcr.Property) item);
            String val = "[ ";
            if (p.isMultiple()) {
                for (Value v : p.getValues()) {
                    val += v.toString() + " ";
                }
                val += "]";
                setIconBaseWithExtension(iconPropMulti);
            } else
            {
                val = " " + p.getValue().toString();
                setIconBaseWithExtension(iconProp);
            }
            setName(getName() + " = " + val);
        }


        if (!getJcrName().isEmpty()) {
            setName(getName() + " - " + getJcrName());
        }
    }

    public String getJcrName() {

        String name = "";
        try {
            if (getBean() instanceof javax.jcr.Node
                    && ((javax.jcr.Node) getBean()).hasProperty("jcr:name")) {
                name = ((javax.jcr.Node) getBean()).getProperty("jcr:name").getValue().toString();
            }
        } catch (ValueFormatException ex) {
            Exceptions.printStackTrace(ex);
        } catch (RepositoryException ex) {
            Exceptions.printStackTrace(ex);
        }

        return name;
    }

    @Override
    public String getHtmlDisplayName() {
        String hdName = getName();
        boolean isJcrName = false;
        boolean isJcrPrimaryType = false;
        try {
            isJcrName = getBean() instanceof javax.jcr.Property && getBean().getName().equals("jcr:name");
            isJcrPrimaryType = getBean() instanceof javax.jcr.Property && getBean().getName().equals("jcr:primaryType");
        } catch (RepositoryException ex) {
            Exceptions.printStackTrace(ex);
        }
        if (isJcrName) {
            hdName = "<b>" + hdName + "</b>";
        }
        if (isJcrPrimaryType) {
            hdName = "<i>" + hdName + "</i>";
        }
        return hdName;
    }

    public int compareTo(JCRNode t) {
        int comp = 0;
        if (t != null && getBean() != null) {
            if (getBean() instanceof javax.jcr.Property && t.getBean() instanceof javax.jcr.Node) {
                comp = 1;
            } else {
                comp = getName().compareTo(t.getName());
            }
        }
        return comp;
    }

    protected class Kids extends Children.SortedArray {
    }
}
