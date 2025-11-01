package seedu.quotely.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.quotely.command.Command;
import seedu.quotely.command.FinishQuoteCommand;
import seedu.quotely.command.NavigateCommand;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.data.QuoteList;
import seedu.quotely.exception.QuotelyException;

/**
 * Tests for quote-related command parsing including add quote, delete quote,
 * and finish quote commands.
 */
public class ParserQuoteCommandTest {

    @Test
    public void parseFinishQuoteCommand_insideQuote_returnFinishQuoteCommand() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        Quote q = new Quote("quote name", "customer name");
        state.setInsideQuote(q);

        try {
            Command command = Parser.parse("finish", state, quoteList);
            assertTrue(command instanceof FinishQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseFinishQuoteCommand_outsideQuote_throwException() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("finish", state, quoteList);
        });
    }

    @Test
    public void parseAddQuoteCommand_validInput_returnAddQuoteCommand() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        try {
            Command command = Parser.parse("quote n/Quote Name c/Customer Name", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.AddQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseAddQuoteCommand_noCustomerName_throwException() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("quote n/Quote Name", state, quoteList);
        });
    }

    @Test
    public void parseAddQuoteCommand_noQuoteName_throwException() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("quote c/Customer Name", state, quoteList);
        });
    }

    @Test
    public void parseAddQuoteCommand_insideQuote_throwException() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        Quote q = new Quote("quote name", "customer name");
        state.setInsideQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("quote n/Quote Name c/Customer Name", state, quoteList);
        });
    }

    @Test
    public void parseDeleteQuoteCommand_validInputOutsideQuote_returnDeleteQuoteCommand() {
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("unquote n/quote 1", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.DeleteQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseDeleteQuoteCommand_validInputInsideQuote_returnDeleteQuoteCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("unquote", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.DeleteQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseDeleteQuoteCommand_invalidQuoteName_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        QuoteList quoteList = new QuoteList();
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("unquote n/invalid quote", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("unquote", state, quoteList);
        });
    }

    @Test
    public void parseUnquoteCommand_validInputInsideQuote_returnUnquoteCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("unquote", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.DeleteQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseUnquoteCommand_validInputOutsideQuote_returnUnquoteCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("unquote n/quote 1", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.DeleteQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseUnquoteCommand_invalidQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("unquote n/invalid quote", state, quoteList);
        });
    }

    @Test
    public void parseUnquoteCommand_noQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("unquote", state, quoteList);
        });
    }

    @Test
    public void parseNavCommand_validQuoteName_returnNavCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("nav n/quote 1", state, quoteList);
            assertTrue(command instanceof NavigateCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseNavCommand_leadingSpace_returnNavCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("    nav   n/quote 1", state, quoteList);
            assertTrue(command instanceof NavigateCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseNavCommand_spaceBeforeQuote_returnNavCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("    nav   n/     quote 1", state, quoteList);
            assertTrue(command instanceof NavigateCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseNavCommand_validMainInput_returnNavCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("nav main", state, quoteList);
            assertTrue(command instanceof NavigateCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseNavCommand_invalidQuoteName_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("nav n/no quote", state, quoteList);
        });
    }

    @Test
    public void parseNavCommand_noQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("nav", state, quoteList);
        });
    }

    @Test
    public void parserExportCommand_validQuoteName_returnExportCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("export n/quote 1", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.ExportQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parserExportCommand_validInputInsideQuote_returnExportCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("export", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.ExportQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parserExportCommand_noQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("export", state, quoteList);
        });
    }

    @Test
    public void parserExportCommand_invalidQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("export n/invalid quote", state, quoteList);
        });
    }
}
