package pt.psoft.g1.psoftg1.shared.model;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class StringUtilsCustom {
    /**
     * @see <a href="https://owasp.org/www-project-java-html-sanitizer/">OWASP Java HTML Sanitizer</a>
     */
    private static final PolicyFactory sanitizer = Sanitizers.FORMATTING.and(Sanitizers.LINKS);

    /**
     * Returns {@code true} if every character is a letter (capitalized or not), or a number, or a white space.
     * <p>In this pattern:
     *
     *     <p>- '{@code \\p{L}}' matches any Unicode letter, including letters with diacritics (accents) in various languages.
     *     <p>- '{@code 0-9}' matches any digit from 0 to 9.
     *     <p>- '{@code \\s}' matches any whitespace character.
     * <p>Adapted from <a href="https://www.baeldung.com/java-check-string-contains-only-letters-numbers">baeldung.com</a>
     * @param str string to be compared
     */
    public static boolean isAlphanumeric(String str){
        String pattern = "^[\\p{L}0-9\\s'-]*$";
        return Pattern.matches(pattern, str);
    }

    public static boolean startsOrEndsInWhiteSpace(String str) {
        return !str.startsWith(" ") && !str.endsWith(" ");
    }

    /**
     * Sanitizes the provided HTML content to remove potentially harmful elements and attributes,
     * ensuring that only safe HTML is retained.
     * <p>This method is intended for use in sanitizing user-generated HTML content before persisting
     * it in a database or rendering it in a web application. By sanitizing the HTML content, the
     * risk of Cross-Site Scripting (XSS) attacks is mitigated, as potentially malicious code or
     * scripts are removed from the input.</p>
     * @param str The HTML content to be sanitized.
     * @return A sanitized version of the HTML content, with potentially harmful elements and attributes removed.
     * @see <a href="https://owasp.org/www-project-java-html-sanitizer/">OWASP Java HTML Sanitizer</a>
     * @see <a href="https://github.com/OWASP/java-html-sanitizer/">OWASP Java HTML Sanitizer GitHub</a>
     */
    public static String sanitizeHtml(String str){
        return sanitizer.sanitize(str);
    }

}
