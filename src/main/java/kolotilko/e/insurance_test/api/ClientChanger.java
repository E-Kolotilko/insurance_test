package kolotilko.e.insurance_test.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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

@SuppressWarnings("serial")
public class ClientChanger extends HttpServlet  {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();
    
    /**
     * in: JSON with full client info (see client constructor) + "id":... (because we CHANGE!)
     * out: {"error":-1} if OK, else {"error": "Error message..."} 
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
        Client clientUpdateInfo = null;
        try {
            clientUpdateInfo = new Client(clientInfo);
            clientUpdateInfo.setId(clientInfo.getLong("id"));
        }
        catch (Exception e) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Wrong client format:"+e));
            return;
        }

        EntityManager em = DbWatcher.EM_FACTORY.createEntityManager();
        EntityTransaction et = null;
        String errorMsg = null;
        Client clientInDb = null;
        try {
            et = em.getTransaction();
            et.begin();
            clientInDb = em.find(Client.class, clientUpdateInfo.getId());
            if (clientInDb != null) {   
                clientInDb.setSurname(clientUpdateInfo.getSurname());
                clientInDb.setName(clientUpdateInfo.getName());
                clientInDb.setPatronymic(clientUpdateInfo.getPatronymic());
                clientInDb.setDateOfBirth(clientUpdateInfo.getDateOfBirth());
                clientInDb.setPassport_series(clientUpdateInfo.getPassport_series());
                clientInDb.setPassport_number(clientUpdateInfo.getPassport_number());
                em.persist(clientInDb);
            }            
            else {
                errorMsg = "Клиент не найден";
            }
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
            writer.writeJsonObj(response, Utils.makeSimpleGoodResponse());
        }
        else {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, errorMsg));
        }
    }
    
}
