package seedu.quotely.storage;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;


import seedu.quotely.util.LoggerConfig;

/**
 * Serializes and deserializes the ApplicationData to/from JSON format.
 */
public class JsonSerializer {
    private static final Logger logger = LoggerConfig.getLogger(JsonSerializer.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Converts an ApplicationData object into a JSON string.
     */
    public String serialize(ApplicationData appData) {
        assert appData != null : "Cannot serialize null ApplicationData";
        return gson.toJson(appData);
    }

    /**
     * Converts a JSON string back into an ApplicationData object.
     * Defensive: catches parse errors, returns new ApplicationData on failure,
     * and calls validate() on the loaded QuoteList.
     */
    public ApplicationData deserialize(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ApplicationData();
        }

        try {
            ApplicationData appData = gson.fromJson(json, ApplicationData.class);
            if (appData == null) {
                return new ApplicationData();
            }

            // Validate the loaded QuoteList (as in your original code)
            if (appData.getQuoteList() != null) {
                appData.getQuoteList().validate();
            } else {
                // Handle case where quotelist might be null in a corrupt file
                logger.warning("Loaded data contained a null QuoteList. Initializing a new one.");
                // This scenario shouldn't happen with the current ApplicationData constructor,
                // but it's safe to check.
            }

            return appData;
        } catch (JsonParseException e) {
            logger.warning("Failed to parse ApplicationData from JSON: " + e.getMessage());
            return new ApplicationData();
        }
    }
}

