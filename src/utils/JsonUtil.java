package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
    
    public static void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        try {
            String json = objectMapper.writeValueAsString(data);
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        } catch (Exception e) {
            // Fallback to simple serialization if Jackson fails
            Logger.error("Error serializing JSON with Jackson, using fallback", e);
            String json = toJson(data);
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        }
    }
    
    public static void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        
        ErrorResponse error = new ErrorResponse(message);
        String json = "{\"error\":\"" + escapeJson(error.getError()) + "\",\"timestamp\":" + error.getTimestamp() + "}";
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
    
    private static String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Map) {
            return mapToJson((Map<?, ?>) obj);
        }
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        // For complex objects, use basic reflection or GSON/Jackson
        // For now, return simple toString
        return "\"" + escapeJson(obj.toString()) + "\"";
    }
    
    private static String mapToJson(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append("\"").append(escapeJson(entry.getKey().toString())).append("\":");
            sb.append(toJson(entry.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }
    
    private static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    public static class ErrorResponse {
        private String error;
        private long timestamp;
        
        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getError() {
            return error;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
}

