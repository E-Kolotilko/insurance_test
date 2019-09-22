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

import kolotilko.e.insurance_test.models.ContractShort;
import kolotilko.e.insurance_test.routines.DbWatcher;
import request_writer.SimpleWriter;
import routines.Utils;
import routines.UtilsWeb;

@SuppressWarnings("serial")
public class ContractShortLister  extends HttpServlet  {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();
    static public final String OPTION_NAME = "shortContracts";
    
    public static final String CONTRACT_NUMBER_JSON_KEY="contractNumber";
    public static final String CONTRACT_DATE_JSON_KEY="contractDate";
    public static final String FIO_JSON_KEY="FIO";
    public static final String AWARD_JSON_KEY="award";
    public static final String ACTIVE_FROM_JSON_KEY="activeFrom";
    public static final String ACTIVE_TO_JSON_KEY="activeTo";
    
    /*
     * out: {"error":-1, shortContracts:[{},{},{}] }
     * shortContracts = {"contractNumber" : ..., "contractDate":..., 
     *      "FIO":"...", "award":..., "activeFrom":"...", "activeTo":...}
     *      activeFrom, activeTo - check for presence
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
        EntityManager em = DbWatcher.EM_FACTORY.createEntityManager();
        TypedQuery<ContractShort> tq = em.createQuery("SELECT c FROM ContractShort c ", ContractShort.class); 

        String errorMsg = null;
        List<ContractShort> contractShortList = null;
        try {
            contractShortList =  tq.getResultList();
        }
        catch (NoResultException e) {
            contractShortList = new ArrayList<ContractShort>();
        }
        catch (Exception e) {
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
        
        if (errorMsg == null) {
            JSONArray result = new JSONArray();
            if (contractShortList != null) {
                for (ContractShort c : contractShortList) {
                    JSONObject contractJson = new JSONObject();
                    contractJson.put(CONTRACT_NUMBER_JSON_KEY, c.getContractNumber());
                    contractJson.put(CONTRACT_DATE_JSON_KEY, c.getContractDate().getTime());
                    contractJson.put(FIO_JSON_KEY, c.getFIO());
                    contractJson.put(AWARD_JSON_KEY, c.getAward());
                    if (c.getActiveFrom() != null) {
                        contractJson.put(ACTIVE_FROM_JSON_KEY, c.getActiveFrom().getTime());
                    }
                    if (c.getActiveTo() != null) {
                        contractJson.put(ACTIVE_TO_JSON_KEY, c.getActiveTo().getTime());
                    }
                    result.put(contractJson);
                }
            }
            writer.writeJsonObj(response, Utils.makeSimpleGoodResponse(OPTION_NAME, result));
        }
        else {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, errorMsg));
        }
    }
    
}
