package org.metacortex.nbjcr.model;

import java.beans.IntrospectionException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.openide.util.Lookup;

/**
 *
 * @author kyjo
 */
public class SessionNode extends JCRNode {

    private String repoUrl = "";
    private String workspace = null;
    private boolean connected = false;

    public SessionNode(Session session) throws RepositoryException, IntrospectionException {
        super(session.getRootNode());
        setName(session.getRepository().getDescriptor(Repository.REP_NAME_DESC) + " / " + session.getWorkspace().getName());
        setIconBaseWithExtension(iconWorkspace);

        //Lookup.getDefault().lookup(null)

    }

    


}
