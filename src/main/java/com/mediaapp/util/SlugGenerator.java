package com.mediaapp.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Slug Generator Utility
 * Generates URL-friendly slugs from text
 */
@Component
public class SlugGenerator {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");

    /**
     * Generate a slug from the given text
     * 
     * @param text the text to convert to slug
     * @return URL-friendly slug
     */
    public String generateSlug(String text) {
        if (text == null || text.isBlank()) {
            return UUID.randomUUID().toString();
        }

        // Normalize to NFD (decomposed form) to separate base characters from diacritics
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Convert to lowercase
        String lowercase = normalized.toLowerCase(Locale.ROOT);

        // Replace whitespace with hyphens
        String hyphenated = WHITESPACE.matcher(lowercase).replaceAll("-");

        // Remove non-latin characters (keeps alphanumeric, hyphens, underscores)
        String latinOnly = NON_LATIN.matcher(hyphenated).replaceAll("");

        // Replace multiple consecutive hyphens with a single hyphen
        String cleaned = MULTIPLE_HYPHENS.matcher(latinOnly).replaceAll("-");

        // Remove leading and trailing hyphens
        String trimmed = cleaned.replaceAll("^-+|-+$", "");

        // If result is empty, generate UUID-based slug
        if (trimmed.isEmpty()) {
            return UUID.randomUUID().toString();
        }

        // Limit length to reasonable size (e.g., 100 characters)
        if (trimmed.length() > 100) {
            trimmed = trimmed.substring(0, 100);
            // Remove trailing hyphen if any
            trimmed = trimmed.replaceAll("-+$", "");
        }

        return trimmed;
    }

    /**
     * Generate a unique slug by appending a suffix
     * Used when the base slug already exists
     * 
     * @param baseSlug the base slug
     * @param suffix the suffix to append (usually an ID or counter)
     * @return unique slug
     */
    public String generateUniqueSlug(String baseSlug, Object suffix) {
        if (baseSlug == null || baseSlug.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return baseSlug + "-" + suffix;
    }

    /**
     * Generate slug from title with ID suffix
     * Common pattern: "my-awesome-post-123"
     * 
     * @param title the title
     * @param id the ID
     * @return slug with ID suffix
     */
    public String generateSlugWithId(String title, Long id) {
        String baseSlug = generateSlug(title);
        return generateUniqueSlug(baseSlug, id);
    }
}
