package seedu.quotely.parser;

import org.junit.jupiter.api.Test;

import seedu.quotely.command.Command;
import seedu.quotely.data.CompanyName;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.data.QuoteList;
import seedu.quotely.exception.QuotelyException;
import seedu.quotely.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for item-related command parsing including add item and delete item
 * commands.
 */
public class ParserItemCommandTest {

    @Test
    public void parseAddItemCommand_validInputInsideQuote_returnAddItemCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("add i/Item1 p/10.0 q/2", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.AddItemCommand);
            Command command2 = Parser.parse("add i/Item2 p/9999.99 q/999 t/200", state, quoteList);
            assertTrue(command2 instanceof seedu.quotely.command.AddItemCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseAddItemCommand_validInputOutsideQuote_returnAddItemCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        try {
            Command command = Parser.parse("add i/Item 1 n/quote 1 p/10.0 q/2", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.AddItemCommand);
            Command command2 = Parser.parse("add i/Item2 n/quote 1 p/9999.99 q/999 t/200", state, quoteList);
            assertTrue(command2 instanceof seedu.quotely.command.AddItemCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseAddItemCommand_invalidInputInsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/Item1 p/invalidprice q/2", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/loooooooooooooooooooooooooooooooooongName p/1 q/2", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/!nv@l!#$%Name p/1 q/2", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add p/invalidprice q/2", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add p/10000.00 q/2", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/Item1 p/-20 q/2", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/Item1 p/2 q/2.5", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/Item1 p/2 q/1000", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/Item1 p/2 q/1 t/-1", state, quoteList);
        });
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add i/Item1 p/2 q/1 t/200.1", state, quoteList);
        });
    }

    @Test
    public void parseAddItemCommand_invalidItemCount_throwException() {
        Ui ui = Ui.getInstance();
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        CompanyName companyName = new CompanyName("Default");
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);

        try {
            for (int i = 0; i < 30; i++) {
                Command command = Parser.parse("add i/Item p/1.23 q/1 t/10.00", state, quoteList);
                command.execute(ui, quoteList, companyName, state);
            }
            assertThrows(QuotelyException.class, () -> {
                Command command = Parser.parse("add i/Item p/1.23 q/1 t/10.00", state, quoteList);
                command.execute(ui, quoteList, companyName, state);
            });
        } catch (QuotelyException e) {
            assert false : "Exception should not be thrown";
        }


    }

    @Test
    public void parseDeleteItemCommand_validInputInsideQuote_returnDeleteItemCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        q.addItem("Item1", 10.0, 4, 0);
        try {
            Command command = Parser.parse("delete i/Item1", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.DeleteItemCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseDeleteItemCommand_validInputOutsideQuote_returnDeleteItemCommand() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        q.addItem("Item1", 10.0, 4, 0);
        try {
            Command command = Parser.parse("delete i/Item1 n/quote 1", state, quoteList);
            assertTrue(command instanceof seedu.quotely.command.DeleteItemCommand);
        } catch (Exception e) {
            assert false : "Exception should not be thrown";
        }
    }

    @Test
    public void parseAddItemCommand_invalidQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add n/invalid quote i/Item1 p/10.0 q/2", state, quoteList);
        });
    }

    @Test
    public void parseAddItemCommand_invalidPriceOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add n/quote 1 i/Item1 p/-0.3 q/2", state, quoteList);
        });
    }

    @Test
    public void parseAddItemCommand_invalidQuantityOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("add n/quote 1 i/Item1 p/12.2 q/-10", state, quoteList);
        });
    }

    @Test
    public void parseDeleteItemCommand_noQuoteNameOutsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setOutsideQuote();
        quoteList.addQuote(q);
        q.addItem("Item1", 10.0, 4, 0);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("delete i/Item1", state, quoteList);
        });
    }

    @Test
    public void parseDeleteItemCommand_invalidItemInsideQuote_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        q.addItem("Item1", 10.0, 4, 0);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("delete i/invalid item", state, quoteList);
        });
    }

    @Test
    public void parseDeleteItemCommand_noArguments_throwException() {
        QuotelyState state = QuotelyState.getInstance();
        QuoteList quoteList = new QuoteList();
        Quote q = new Quote("quote 1", "customer 1");
        state.setInsideQuote(q);
        quoteList.addQuote(q);
        q.addItem("Item1", 10.0, 4, 0);
        assertThrows(QuotelyException.class, () -> {
            Parser.parse("delete", state, quoteList);
        });
    }
}
