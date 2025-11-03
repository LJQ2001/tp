package seedu.quotely.command;

import org.junit.jupiter.api.Test;
import seedu.quotely.data.CompanyName;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuoteList;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.exception.QuotelyException;
import seedu.quotely.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for CalculateTotalCommand that verifies the correct output is printed.
 */
public class CalculateTotalCommandTest {

    /**
     * calculate total needs to be modified to test total with hasTax
     */
    @Test
    public void execute_calculateTotalCommand_printsCorrectTotal() {
        // Create a dynamic stream to capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        try {
            // Redirect the standard output to our capture stream
            System.setOut(new PrintStream(outContent));

            // 1. Arrange: Set up the necessary objects
            Ui ui = Ui.getInstance();
            QuoteList quoteList = new QuoteList();
            CompanyName companyName = new CompanyName("Test Corp");
            QuotelyState state = QuotelyState.getInstance();

            // Create a quote with items to get a predictable total
            Quote quote = new Quote("Sample Quote", "John Doe");
            quote.addItem("Paint", 25.0, 2, 0); // 50.0
            quote.addItem("Brushes", 5.5, 4, 0);  // 22.0
            // Total = 72.0
            CalculateTotalCommand command = new CalculateTotalCommand(quote);

            // 2. Act: Execute the command
            command.execute(ui, quoteList, companyName, state);

            // 3. Assert: Check if the captured output matches the expected string
            String actualOutput = outContent.toString();
            String expectedPrefix = "Total cost of quote Sample Quote for John Doe: $";
            assertTrue(actualOutput.contains(expectedPrefix));
            String value = actualOutput.substring(expectedPrefix.length()).trim();
            assertTrue(value.contains("72.00"));
            assertFalse(command.isExit());

        } catch (QuotelyException e) {
            assert false : "Execution of calculate total command should not fail.";
        }
    }
}
