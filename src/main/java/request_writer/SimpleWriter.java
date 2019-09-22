package request_writer;

import routines.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class SimpleWriter {
    static Logger aLogger = LogManager.getLogger();

    public void writeTextMessage(HttpServletResponse response, String aMessage) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
            PrintWriter out = response.getWriter();
            out.println(aMessage);
            out.flush();
            out.close();
        } catch (Exception eGen) {
            aLogger.error(eGen);
        }
    }

    public void writeHtmlMessage(HttpServletResponse response, String aMessage) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println(aMessage);
            out.flush();
            out.close();
        } catch (Exception eGen) {
            aLogger.error(eGen);
        }
    }

    public void writeJsonObj(HttpServletResponse response, JSONObject anObject) {
        writeStringAsJson(response, anObject.toString());
    }

    public void writeJsonEmpty(ServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Charset.defaultCharset().toString());
        try {
            PrintWriter outPut = response.getWriter();
            outPut.print("{}");
            outPut.flush();
            outPut.close();
        } catch (Exception eGen) {
            aLogger.error("Write to response exception in sendEmptyObject:" + eGen.getMessage());
        }
    }

    public void writeStringAsJson(HttpServletResponse response, String jsonConverted) {
        response.setContentType("application/json");
        response.setCharacterEncoding(Charset.defaultCharset().toString());
        try {
            PrintWriter outPut = response.getWriter();
            outPut.print(jsonConverted);
            outPut.flush();
            outPut.close();
        } catch (Exception eGen) {
            aLogger.error("Write in response exception in writeJsonObj:" + eGen.getMessage());
        }
    }

    public void writeHugeResponse(HttpServletResponse response,
                                  List<Map<String, String>> bigList, String option) {
        JSONArray out = new JSONArray();
        if (bigList != null) {
            for (Map<String, String> big : bigList) {
                JSONObject result = new JSONObject();
                for (Map.Entry<String, String> anEntry : big.entrySet()) {
                    result.put(anEntry.getKey(), anEntry.getValue());
                }
                out.put(result);
            }
        }
        writeJsonObj(response, Utils.makeSimpleGoodResponse(option, out));
    }

}
