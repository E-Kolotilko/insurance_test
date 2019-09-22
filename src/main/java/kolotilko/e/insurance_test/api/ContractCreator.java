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
import kolotilko.e.insurance_test.models.Contract;
import kolotilko.e.insurance_test.routines.DbWatcher;
import request_writer.SimpleWriter;
import routines.Utils;
import routines.UtilsWeb;

@SuppressWarnings("serial")
public class ContractCreator extends HttpServlet {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();

    public static final String CLIENT_OPTION_NAME = "client";
    public static final String CONTRACT_OPTION_NAME = "contract";
    
    
    /**
     * in: {"client":{}, "contract":{}}
     * client - full client info + id (to check)
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
        //create new contract if possible
        JSONObject info = webUtils.getJsonFromPost(request);
        if (!info.has(CONTRACT_OPTION_NAME)) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Не найдены данные по контракту"));
        }
        if (!info.has(CLIENT_OPTION_NAME)) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Не найдены данные по клиенту"));
        }
        
        JSONObject contractInfo;
        Contract newContract;
        try {
            contractInfo = info.getJSONObject(CONTRACT_OPTION_NAME);
            newContract = new Contract(contractInfo);
        }
        catch (Exception e) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Wrong contract format:"+e));
            return;
        }
        
        JSONObject clientInfo;
        Client existingClient;
        long id;
        try {
            clientInfo = info.getJSONObject(CLIENT_OPTION_NAME);            
            if (!clientInfo.has(Client.ID_JSON_KEY)) {
                writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Client has no id!"));
                return;
            }
            existingClient = new Client(clientInfo);
            id = clientInfo.getLong(Client.ID_JSON_KEY);
        }
        catch (Exception e) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, "Wrong client format:"+e));
            return;
        }
        existingClient.setId(id);
        newContract.setClientId(id);

        EntityManager em = DbWatcher.EM_FACTORY.createEntityManager();                
        EntityTransaction et = null;
        Client clientInDb = null;
        String errorMsg = null;        
        try {
            et = em.getTransaction();
            et.begin();
            
            clientInDb = em.find(Client.class, id);
            if (clientInDb != null ) {   
                if (clientInDb.isDifferent(existingClient)) {
                  clientInDb.setSurname(existingClient.getSurname());
                  clientInDb.setName(existingClient.getName());
                  clientInDb.setPatronymic(existingClient.getPatronymic());
                  clientInDb.setDateOfBirth(existingClient.getDateOfBirth());
                  clientInDb.setPassport_series(existingClient.getPassport_series());
                  clientInDb.setPassport_number(existingClient.getPassport_number());
                  em.persist(clientInDb);
                }

                em.persist(newContract);       
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
