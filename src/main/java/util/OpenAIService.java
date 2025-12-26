package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Service class for OpenAI API integration
 */
public class OpenAIService {
    
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static String API_KEY;
    
    static {
        loadApiKey();
    }
    
    /**
     * Load OpenAI API key from system properties or properties file
     */
    private static void loadApiKey() {
        // First try to get from system property
        API_KEY = System.getProperty("openai.api.key");
        
        // If not found, try to load from properties file
        if (API_KEY == null) {
            Properties props = new Properties();
            try (InputStream input = OpenAIService.class.getClassLoader()
                    .getResourceAsStream("openai.properties")) {
                if (input != null) {
                    props.load(input);
                    API_KEY = props.getProperty("openai.api.key");
                }
            } catch (IOException e) {
                System.err.println("Warning: Could not load OpenAI API key from properties file");
            }
        }
        
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            System.err.println("Warning: OpenAI API key not configured. Recipe recommendations will not work.");
        }
    }
    
    /**
     * Generate recipe recommendation based on ingredients
     * @param ingredients Comma-separated list of ingredient names
     * @return Recipe recommendation or error message
     */
    public static String generateRecipeRecommendation(String ingredients) {
        if (API_KEY == null || API_KEY.trim().isEmpty()) {
            return "Recipe recommendation service is not configured. Please contact administrator.";
        }
        
        if (ingredients == null || ingredients.trim().isEmpty()) {
            return "Please provide at least one ingredient.";
        }
        
        try {
            String prompt = createPrompt(ingredients.trim());
            String requestBody = createRequestBody(prompt);
            
            return callOpenAIAPI(requestBody);
        } catch (Exception e) {
            System.err.println("Error generating recipe recommendation: " + e.getMessage());
            return "Sorry, we couldn't generate a recipe recommendation at this time. Please try again later.";
        }
    }
    
    /**
     * Create the prompt for OpenAI API
     * @param ingredients Ingredient names
     * @return Formatted prompt
     */
    private static String createPrompt(String ingredients) {
        return "Suggest a recipe that can be prepared using only the following ingredients: " + ingredients + 
               ". Provide: 1. Recipe name 2. Step-by-step cooking instructions 3. Helpful cooking tips";
    }
    
    /**
     * Create JSON request body for OpenAI API
     * @param prompt The prompt to send
     * @return JSON request body
     */
    private static String createRequestBody(String prompt) {
        return "{\n" +
               "  \"model\": \"gpt-3.5-turbo\",\n" +
               "  \"messages\": [\n" +
               "    {\n" +
               "      \"role\": \"user\",\n" +
               "      \"content\": \"" + escapeJsonString(prompt) + "\"\n" +
               "    }\n" +
               "  ],\n" +
               "  \"max_tokens\": 500,\n" +
               "  \"temperature\": 0.7\n" +
               "}";
    }
    
    /**
     * Call OpenAI API and get response
     * @param requestBody JSON request body
     * @return Recipe recommendation
     * @throws IOException if API call fails
     */
    private static String callOpenAIAPI(String requestBody) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(OPENAI_API_URL);
            
            // Set headers
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + API_KEY);
            
            // Set request body
            StringEntity entity = new StringEntity(requestBody, "UTF-8");
            httpPost.setEntity(entity);
            
            // Execute request
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                String responseString = EntityUtils.toString(responseEntity);
                
                // Check if request was successful
                if (response.getStatusLine().getStatusCode() == 200) {
                    return parseOpenAIResponse(responseString);
                } else {
                    System.err.println("OpenAI API error: " + response.getStatusLine().getStatusCode() + " - " + responseString);
                    return "Sorry, we couldn't generate a recipe recommendation at this time. Please try again later.";
                }
            }
        }
    }
    
    /**
     * Parse OpenAI API response and extract the recipe
     * @param responseJson JSON response from OpenAI
     * @return Recipe text
     */
    private static String parseOpenAIResponse(String responseJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseJson);
            
            JsonNode choicesNode = rootNode.get("choices");
            if (choicesNode != null && choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode firstChoice = choicesNode.get(0);
                JsonNode messageNode = firstChoice.get("message");
                if (messageNode != null) {
                    JsonNode contentNode = messageNode.get("content");
                    if (contentNode != null) {
                        return contentNode.asText().trim();
                    }
                }
            }
            
            return "Sorry, we couldn't generate a recipe recommendation. Please try again.";
        } catch (Exception e) {
            System.err.println("Error parsing OpenAI response: " + e.getMessage());
            return "Sorry, we couldn't process the recipe recommendation. Please try again.";
        }
    }
    
    /**
     * Escape special characters in JSON string
     * @param input Input string
     * @return Escaped string
     */
    private static String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    /**
     * Check if OpenAI service is configured
     * @return true if API key is available, false otherwise
     */
    public static boolean isConfigured() {
        return API_KEY != null && !API_KEY.trim().isEmpty();
    }
}