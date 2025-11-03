package seedu.quotely.storage;

import seedu.quotely.data.CompanyName;
import seedu.quotely.data.QuoteList;

/**
 * A container class to hold all application data that needs to be saved.
 * Used by JsonSerializer to save/load from a single JSON file.
 */
public class ApplicationData {

    private QuoteList quoteList;
    private CompanyName companyName;

    /**
     * No-arg constructor for Gson deserialization.
     * Initializes with empty/default data.
     */
    public ApplicationData() {
        this.quoteList = new QuoteList();
        this.companyName = new CompanyName("Default"); // Your app's default
    }

    /**
     * Constructor to wrap existing data for serialization.
     */
    public ApplicationData(QuoteList quoteList, CompanyName companyName) {
        this.quoteList = quoteList;
        this.companyName = companyName;
    }

    public QuoteList getQuoteList() {
        return quoteList;
    }

    public CompanyName getCompanyName() {
        return companyName;
    }
}



