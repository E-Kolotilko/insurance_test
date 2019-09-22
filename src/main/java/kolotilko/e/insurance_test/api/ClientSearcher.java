package kolotilko.e.insurance_test.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import kolotilko.e.insurance_test.models.Client;
import kolotilko.e.insurance_test.routines.DbWatcher;
import request_writer.SimpleWriter;
import routines.Utils;
import routines.UtilsWeb;

@SuppressWarnings("serial")
public class ClientSearcher extends HttpServlet  {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();
    static public final String SURNAME_VAR_NAME = "SUR";
    static public final String NAME_VAR_NAME = "NAME";
    static public final String PATR_VAR_NAME = "PATR";
    static public final String QUERY_START = "SELECT c FROM Client c ";
    
    static public final String OPTION_NAME = "clients";

    /**
   Try to get data based on some (or no) info 
    * in: {"surname":"...", "name":"...", "patr":"..."}
    surname, name, patr - optional
    * out {"error":-1, "clients":[{client1_data},{client2_data},...] }
    client1_data:
    { "surname":"...", "name":"...", "patr":"...", "DoB":long, "passSeries":"...", "passNum":"..."}
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            process(request, response);
        }
        catch (Exception e) {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, e.getMessage()));
        }
    }

    void process(HttpServletRequest request, HttpServletResponse response) {      
        JSONObject clientInfo = webUtils.getJsonFromGet(request);
        String whereString = getWhereString(clientInfo);
        String query;
        if (whereString != null) {
            query = QUERY_START+whereString;
        }
        else {
            query = QUERY_START;
        }

        EntityManager em = DbWatcher.EM_FACTORY.createEntityManager();
        TypedQuery<Client> tq = em.createQuery(query, Client.class); 
        ///
        if (clientInfo.has(Client.SURNAME_JSON_KEY)) {
            tq.setParameter(SURNAME_VAR_NAME, "%"+clientInfo.get(Client.SURNAME_JSON_KEY)+"%");
        }        
        if (clientInfo.has(Client.NAME_JSON_KEY)) {
            tq.setParameter(NAME_VAR_NAME, "%"+clientInfo.get(Client.NAME_JSON_KEY)+"%");
        }
        if (clientInfo.has(Client.PATRONYMIC_JSON_KEY)) {
            tq.setParameter(PATR_VAR_NAME, "%"+clientInfo.get(Client.PATRONYMIC_JSON_KEY)+"%");
        }
        ///
        String errorMsg = null;
        List<Client> clientList = null;
        try {
            clientList = tq.getResultList();
        }
        catch (NoResultException e) {
            clientList = new ArrayList<Client>();
        }
        catch (Exception e) {
            errorMsg = e.getMessage();
        }
        
        if (errorMsg == null) {
            JSONArray result = new JSONArray();
            if (clientList != null) {
                for (Client c : clientList) {
                    if (c!=null) {
                        result.put(c.toJsonObject());
                    }
                }
            }
            writer.writeJsonObj(response, Utils.makeSimpleGoodResponse(OPTION_NAME, result));
        }
        else {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, errorMsg));
        }
    }
    
    
    String getWhereString(JSONObject clientInfo) {
        //no info -> search no where condition
        if (clientInfo.length() == 0) {
            return null;
        }
        
        boolean first = true;
        StringBuilder builder = new StringBuilder(" WHERE ");
        if (clientInfo.has(Client.SURNAME_JSON_KEY)) {
            first = false;
            builder.append("c.surname");
            builder.append(" LIKE :");
            builder.append(SURNAME_VAR_NAME);
        }        

        if (clientInfo.has(Client.NAME_JSON_KEY)) {
            if (first) {
                first = false;
            }
            else {
                builder.append(" AND ");
            }
            builder.append("c.name");
            builder.append(" LIKE :");
            builder.append(NAME_VAR_NAME);
        }

        if (clientInfo.has(Client.PATRONYMIC_JSON_KEY)) {
            if (first) {
                first = false;
            }
            else {
                builder.append(" AND ");
            }
            builder.append("c.patronymic");
            builder.append(" LIKE :");
            builder.append(PATR_VAR_NAME);
        }
        
        //if first was changed to false -> at least 1 filtering option was found
        if (first == false) {            
            return builder.toString();
        }
        else {
            return null;
        }
    }
    
}
