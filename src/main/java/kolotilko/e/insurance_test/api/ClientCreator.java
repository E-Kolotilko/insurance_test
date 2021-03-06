package kolotilko.e.insurance_test.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import kolotilko.e.insurance_test.models.Client;
import kolotilko.e.insurance_test.routines.DbWatcher;
import request_writer.SimpleWriter;
import routines.Utils;
import routines.UtilsWeb;

/**
 * This class is supposed to try to save info about new client
 *
 */
@SuppressWarnings("serial")
public final class ClientCreator extends HttpServlet {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();
    public static final String OPTION_NAME = "id";

    /**
     * in: JSON with full client info (see client constructor)
     * out: {"error":-1, id:...} if OK, else {"error": "Error message..."} 
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            process(request, response);
        }
        catch (Exception e) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, e.getMessage()));
        }
    }
    
    void process(HttpServletRequest request, HttpServletResponse response) {
        //create new client if possible
        JSONObject clientInfo = webUtils.getJsonFromPost(request);
        Client client = null;
        try {
            client = new Client(clientInfo);
        }
        catch (Exception e) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Wrong client format:"+e));
            return;
        }
        
        //persist new client
        EntityManager em = DbWatcher.EM_FACTORY.createEntityManager();
        EntityTransaction et = null;
        String errorMsg = null;
        try {
            et = em.getTransaction();
            et.begin();
            em.persist(client);
            et.commit();
        }
        catch (Exception e) {
            aLogger.error(e);
            if (et!=null) {
                et.rollback();
                errorMsg = e.getMessage();
            }
            else {
                errorMsg = "Transaction was null. " + e.getMessage();
            }
        }
        finally {
            em.close();
        }
        
        if (errorMsg==null) {
            writer.writeJsonObj(response, Utils.makeSimpleGoodResponse(OPTION_NAME,client.getId()));
        }
        else {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, errorMsg));
        }
    }
    
}
