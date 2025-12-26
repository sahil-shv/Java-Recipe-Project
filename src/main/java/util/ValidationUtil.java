package util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation and sanitization
 */
public class ValidationUtil {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Name validation pattern (letters, spaces, hyphens, apostrophes)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s'-]{2,50}$");
    
    /**
     * Validate email format
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate name format
     * @param name Name to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }
    
    /**
     * Sanitize string input to prevent XSS
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        return input.trim()
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");
    }
    
    /**
     * Check if string is null or empty after trimming
     * @param str String to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate recipe title
     * @param title Recipe title to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRecipeTitle(String title) {
        return !isEmpty(title) && title.trim().length() >= 3 && title.trim().length() <= 200;
    }
    
    /**
     * Validate recipe ingredients
     * @param ingredients Recipe ingredients to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIngredients(String ingredients) {
        return !isEmpty(ingredients) && ingredients.trim().length() >= 10;
    }
    
    /**
     * Validate recipe description
     * @param description Recipe description to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDescription(String description) {
        return !isEmpty(description) && description.trim().length() >= 20;
    }
    
    /**
     * Validate comment content
     * @param comment Comment to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidComment(String comment) {
        return !isEmpty(comment) && comment.trim().length() >= 3 && comment.trim().length() <= 500;
    }
}