package kolotilko.e.insurance_test.api;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import kolotilko.e.insurance_test.routines.AwardCalculator;
import request_writer.SimpleWriter;
import routines.Utils;
import routines.UtilsWeb;

@SuppressWarnings("serial")
public class PropertyTypeTeller   extends HttpServlet  {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();
    static public final String OPTION_NAME = "propertyTypes";
    
    /*
     * out: {"error":-1, propertyTypes:[...,...,...] }
     */
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String errorMsg = AwardCalculator.getErrorMsg();
        if (errorMsg == null) {
            List<String> types = AwardCalculator.getPropertyTypes();
            
            JSONArray result = new JSONArray();
            for (String s : types) {
                result.put(s);
            }
    
            writer.writeJsonObj(response, Utils.makeSimpleGoodResponse(OPTION_NAME, result));
        }
        else {
            writer.writeJsonObj(response, Utils.makeArrayErrorResponse(OPTION_NAME, errorMsg));
        }
    }
    
}
