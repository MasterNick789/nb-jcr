package org.metacortex.nbjcr.model;

import java.beans.IntrospectionException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 *
 * @author Tomas Kyjovsky <tkyjovsk at redhat dot com>
 */
public class NodeNode extends JCRNode<Node> {

    public NodeNode(Node n) throws IntrospectionException, RepositoryException {
        super(n);
    }

}
