package kolotilko.e.insurance_test.api;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import kolotilko.e.insurance_test.routines.AwardCalculator;
import request_writer.SimpleWriter;
import routines.Utils;
import routines.UtilsWeb;


@SuppressWarnings("serial")
public class AwardCalculatorController extends HttpServlet  {
    protected static final Logger aLogger = LogManager.getLogger();
    static final UtilsWeb webUtils = new UtilsWeb();
    static final SimpleWriter writer = new SimpleWriter();
    static public final String INSURANCE_AMOUNT_NAME = "insuranceAmount";
    static public final String DAYS_NAME = "days";
    static public final String PROPERTY_TYPE_NAME = "propertyType";
    static public final String BUILD_YEAR_NAME = "builtYear";
    static public final String AREA_NAME = "area";
    static public final String OPTION_NAME = "award";
    
    /*
     * in: { "insuranceAmount":..., "days":..., "propertyType":"...", "builtYear":..., "area":... }
     * out: {"error":-1, "award":... } if OK or {"error":"..." } 
     */
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        
        JSONObject info = webUtils.getJsonFromGet(request);
        String errorMsg = null;
        double result = -1;
        try {
            double insuranceAmount = info.getDouble(INSURANCE_AMOUNT_NAME);
            int days = info.getInt(DAYS_NAME);
            String propertyType = info.getString(PROPERTY_TYPE_NAME);
            int builtYear = info.getInt(BUILD_YEAR_NAME);
            double area = info.getInt(AREA_NAME);
            result = AwardCalculator.calculateAward(insuranceAmount, days, propertyType, builtYear, area);
        }
        catch (IllegalArgumentException eIllegal) {
            errorMsg = eIllegal.getMessage();
        }
        catch (Exception e) {
            errorMsg = e.getMessage();
        }        
        
        if (!Double.isFinite(result)) {
            errorMsg = "Результат равен бесконечности или не является числом";
        }
        
        if (errorMsg == null) {    
            writer.writeJsonObj(response, Utils.makeSimpleGoodResponse(OPTION_NAME, result));
        }
        else {
            writer.writeJsonObj(response, Utils.makeSimpleErrorResponse(null, errorMsg));
        }
        
    }
    
        
}
