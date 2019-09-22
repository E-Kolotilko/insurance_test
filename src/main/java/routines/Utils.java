package routines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

public class Utils {
    private static final String ERROR_JSON_KEY = "error";
	static final Logger aLogger = LogManager.getLogger();
    private Utils() {} //hiding constructor
	
	static public String getFormattedDate(long time) {
		Calendar timeCalend = Calendar.getInstance();//not static field!!! Otherwise can have no concurrency!
		timeCalend.setTimeInMillis(time);
		return String.format("%02d.%02d.%04d", timeCalend.get(Calendar.DAY_OF_MONTH), 
				timeCalend.get(Calendar.MONTH)+1, timeCalend.get(Calendar.YEAR));
	}
        
	
    static public Map<String, String> getMapFromQuerryStr(String unparsed) {
        Map<String, String> result = new TreeMap<String, String>();
        //No params in request? It's ok
        if(unparsed == null || unparsed.length()==0) {
        	return result;
        }
        //Something is there. But is is well formed?
    	if (!unparsed.contains("=")) {
    		aLogger.error("String is null or has no '=' char");
    		return result; 
    	}
        String devider1 = "&";
        String devider2 = "=";
        String[] parts = unparsed.split(devider1);
        for (String aPart : parts) {
            String[] pair = aPart.split(devider2);
            if (pair.length!=2) {
                aLogger.error("Unable to properly devide request string: " + unparsed);
                continue;
            }
            try {
                if (pair[1]!= null) {
                    String decoded = URLDecoder.decode(pair[1], StandardCharsets.UTF_8.name());
                    result.put(pair[0], decoded);
                }
            } catch (Exception decodeExc) {
                aLogger.error("Decoding exception! Message: " + decodeExc.getMessage());
            }
        }
        return result;
    }

    
    static public String getDecodedString(BufferedReader in) throws IOException {
        return getDecodedString(in, null);
    }

    
    static public String getDecodedString(BufferedReader in, String encoding) throws IOException {
        if (encoding == null) {
            encoding = StandardCharsets.UTF_8.name();
        }
        StringBuilder dataSave = new StringBuilder();
        
        String readNow = null;
        while ((readNow = in.readLine()) != null)  {
            dataSave.append(readNow);
        }
        
        String queryString  =  dataSave.toString();
        if (queryString != null) {
            try {
                queryString = URLDecoder.decode(queryString, encoding);
                return queryString;
            }
            catch (Exception decodeExc) {
                aLogger.error("Decode exception! Message: "+decodeExc.getMessage());
            }
        }
        return null;
    }


    static public String getDecodedString(InputStream input, String encoding) {
        String queryString = null;
        if (encoding == null) {
            encoding = StandardCharsets.UTF_8.name();
        }
        try (BufferedReader in = new BufferedReader(new InputStreamReader(input,encoding)))
        {
            queryString = getDecodedString(in,encoding);
        }
        catch (Exception ioExc) {
            aLogger.error("IO exception! Message: "+ioExc.getMessage());
        }
        return queryString;
    }


    static public String getDecodedString(InputStream input) {
        return getDecodedString(input,null);
    }
        
    
    static public boolean isLong(String aString) {
        try {
            Long.parseLong(aString);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    static public String calendarToString(Calendar c) {
    	return String.format("%04d.%02d.%02d",  c.get(Calendar.YEAR),  c.get(Calendar.MONTH)+1,  c.get(Calendar.DAY_OF_MONTH));
    }
    
    
    static public JSONObject makeArrayErrorResponse(String option, String message) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, message);
        if (option != null) {
            result.put(option, new JSONArray());
        }
        return result;
    }
    
    static public JSONObject makeSimpleErrorResponse(String option, String message) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, message);
        if (option != null) {
            result.put(option, new JSONObject());
        }
        return result;
    }

    //makeSimpleGoodResponse could work with just Object, but I want some extra checking... (was usefull)
    static public JSONObject makeSimpleGoodResponse(String option, JSONObject userData) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, -1);
        if (option != null) {
            result.put(option, userData);
        }
        return result;
    }

    static public JSONObject makeSimpleGoodResponse(String option, JSONArray userData) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, -1);
        if (option != null) {
            result.put(option, userData);
        }
        return result;
    }

    static public JSONObject makeSimpleGoodResponse(String option, String userData) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, -1);
        if (option != null) {
            result.put(option, userData);
        }
        return result;
    }

    static public JSONObject makeSimpleGoodResponse(String option, double userData) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, -1);
        if (option != null) {
            result.put(option, userData);
        }
        return result;
    }

    static public JSONObject makeSimpleGoodResponse(String option, long userData) {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, -1);
        if (option != null) {
            result.put(option, userData);
        }
        return result;
    }
    
    static public JSONObject makeSimpleGoodResponse() {
        JSONObject result = new JSONObject();
        result.put(ERROR_JSON_KEY, -1);
        return result;
    }

}
