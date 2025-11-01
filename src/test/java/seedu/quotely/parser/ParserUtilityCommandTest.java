package seedu.quotely.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.quotely.command.Command;
import seedu.quotely.command.SearchQuoteCommand;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.data.QuoteList;
import seedu.quotely.exception.QuotelyException;

/**
 * Tests for utility command parsing including total, register, and show
 * commands.
 */
public class ParserUtilityCommandTest {

    @Test
    public void parseTotalQuoteCommand_insideQuote_returnTotalCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote name", "customer name");
        quoteList.addQuote(q);
        state.setInsideQuote(q);
        try {
            Command command = Parser.parse("total", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.CalculateTotalCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseTotalQuoteCommand_validInputOutsideQuote_returnTotalCommand() {
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote name", "customer name");
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("total n/quote name", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.CalculateTotalCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseTotalQuoteCommand_invalidQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote name", "customer name");
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("total n/invalid quote name", state, quoteList);
        });
    }

    @Test
    public void parseTotalQuoteCommand_noQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote name", "customer name");
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("total", state, quoteList);
        });
    }

    @Test
    public void parseRegisterCommand_validInput_returnRegisterCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        try {
            Command command = Parser.parse("register c/Customer Name", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.RegisterCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseRegisterCommand_invalidInput_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("register invalidinput", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("register", state, quoteList);
        });
    }

    @Test
    public void parseShowQuotesCommand_validInput_returnShowQuotesCommand() {
        try {
            QuoteList quoteList = new QuoteList();
            QuotelyState state = QuotelyState.getInstance();
            Command command = Parser.parse("show", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.ShowQuotesCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseSearchCommand_validInput_returnSearchQuoteCommand() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        try {
            Command command = Parser.parse("search n/quote1", state, quoteList);
            assertTrue(command instanceof SearchQuoteCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown for valid input.";
        }
    }

    @Test
    public void parseSearchCommand_invalidInput_throwException() {
        QuoteList quoteList = new QuoteList();
        QuotelyState state = QuotelyState.getInstance();
        state.setOutsideQuote();
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("search quote1", state, quoteList);
        });
    }
}
