package common;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import beans.LoginManager;


public class AuthenticationPhaseListener implements PhaseListener {
    
    /**
	 * 
	 */
	//private static final long serialVersionUID = -8257804439659199807L;
	/**
     * <p>The outcome to trigger navigation to the login page.</p>
     */
    private static final String USER_LOGIN_OUTCOME = "login";
       
    // ---------------------------------------------- Methods from PhaseListener

    /**
     * <p>Determines if the user is authenticated.  If not, direct the
     * user to the login view, otherwise all the user to continue to the
     * requested view.</p>
     *
     * <p>Implementation Note: We do this in the <code>afterPhase</code>
     * to make use of the <code>NavigationHandler</code>.</p>
     */
    public void afterPhase(PhaseEvent event) {
        FacesContext context = event.getFacesContext();
       
        if (userExists(context)) {
            // allow processing of the requested view
            return;
        } else {            
            // send the user to the login view
            if (requestingSecureView(context)) {
                context.responseComplete();              
                context.getApplication().
                        getNavigationHandler().handleNavigation(context, 
                                                                null, 
                                                                USER_LOGIN_OUTCOME);
            }
        }
    }

    /**
     * <p>This is a no-op.</p>
     */
    public void beforePhase(PhaseEvent event) {        
    }

    /**
     * @return <code>PhaseId.RESTORE_VIEW</code>
     */
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    // --------------------------------------------------------- Private Methods       
    
    /**
     * <p>Determine if the user has been authenticated by checking the session
     * for an existing <code>Wuser</code> object.</p>
     * 
     * @param context the <code>FacesContext</code> for the current request
     * @return <code>true</code> if the user has been authenticated, otherwise
     *  <code>false</code>
     */
    private boolean userExists(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();
        return (extContext.getSessionMap().containsKey(LoginManager.USER_SESSION_KEY));
    }
    
    /**
     * <p>Determines if the requested view is one of the login pages which will
     * allow the user to access them without being authenticated.</p>
     *
     * <p>Note, this implementation most likely will not work if the 
     * <code>FacesServlet</code> is suffix mapped.</p>
     *
     * @param context the <code>FacesContext</code> for the current request
     * @return <code>true</code> if the requested view is allowed to be accessed
     *  without being authenticated, otherwise <code>false</code>
     */
    private boolean requestingSecureView(FacesContext context) {
        ExternalContext extContext = context.getExternalContext();       
        String path = extContext.getRequestPathInfo();
        return (!"/login.xhtml".equals(path));              
    }
}