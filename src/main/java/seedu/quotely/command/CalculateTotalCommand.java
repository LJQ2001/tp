package seedu.quotely.command;

import seedu.quotely.ui.Ui;
import seedu.quotely.data.CompanyName;
import seedu.quotely.data.QuoteList;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.data.Quote;
import seedu.quotely.exception.QuotelyException;
import seedu.quotely.util.LoggerConfig;

import java.util.logging.Logger;

public class CalculateTotalCommand extends Command {
    private static final String COMMAND_WORD = "total";
    private static final Logger logger = LoggerConfig.getLogger(CalculateTotalCommand.class);
    private Quote quote;

    public CalculateTotalCommand(Quote quote) {
        super(COMMAND_WORD);
        this.quote = quote;
    }

    /**
     * Calculate total needs to be modified for hasTax attribute
     * - example: if item is taxable apply the multiplier, then sum
     * 
     * @param ui
     * @param quoteList
     * @param companyName
     * @param state
     * @throws QuotelyException
     */
    @Override
    public void execute(Ui ui,
            QuoteList quoteList,
            CompanyName companyName,
            QuotelyState state) throws QuotelyException {

        logger.fine("Executing CalculateTotalCommand");
        ui.showMessage(
                String.format("Total cost of quote %s for %s: $%.2f", quote.getQuoteName(),
                        quote.getCustomerName(), quote.getQuoteTotal()));
    }
}
