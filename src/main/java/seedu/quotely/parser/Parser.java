package seedu.quotely.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

import seedu.quotely.command.SearchQuoteCommand;
import seedu.quotely.exception.QuotelyException;
import seedu.quotely.command.Command;
import seedu.quotely.command.ExitCommand;
import seedu.quotely.command.AddQuoteCommand;
import seedu.quotely.command.RegisterCommand;
import seedu.quotely.command.DeleteQuoteCommand;
import seedu.quotely.command.ShowQuotesCommand;
import seedu.quotely.command.FinishQuoteCommand;
import seedu.quotely.command.AddItemCommand;
import seedu.quotely.command.DeleteItemCommand;
import seedu.quotely.command.CalculateTotalCommand;
import seedu.quotely.command.NavigateCommand;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuoteList;
import seedu.quotely.util.LoggerConfig;

public class Parser {
    private static final Logger logger = LoggerConfig.getLogger(Parser.class);

    private static final String ADD_QUOTE_COMMAND_PATTERN = "^n/(.+?)\\s+c/(.+)$";
    private static final String QUOTENAME_ARG_PATTERN = "^n/(.+)$";
    private static final String EXPORT_QUOTENAME_ARG_PATTERN = "^n/(.+?)(?=\\s+f/|$)";
    private static final String EXPORT_START_PATTERN = "^(n/|f/)";
    private static final String FILENAME_ARG_PATTERN = "f/(.+)$";
    private static final String REGISTER_COMMAND_PATTERN = "^c/(.+)$";
    private static final String ADD_ITEM_COMMAND_PATTERN
            = "^i/(.+?)(?:\\s+n/(.+?))?\\s+p/(.+?)\\s+q/(.+?)(?:\\s+t/(.+))?$";
    private static final String DELETE_ITEM_COMMAND_PATTERN = "^i/(.+?)(?:\s+n/(.+))?$";

    // command keywords
    private static final String ADD_QUOTE_COMMAND_KEYWORD = "quote";
    private static final String DELETE_QUOTE_COMMAND_KEYWORD = "unquote";
    private static final String SHOW_QUOTES_COMMAND_KEYWORD = "show";
    private static final String FINISH_QUOTE_COMMAND_KEYWORD = "finish";
    private static final String DELETE_ITEM_COMMAND_KEYWORD = "delete";
    private static final String EXPORT_QUOTE_COMMAND_KEYWORD = "export";
    private static final String ADD_ITEM_COMMAND_KEYWORD = "add";
    private static final String CALCULATE_TOTAL_COMMAND_KEYWORD = "total";
    private static final String NAVIGATE_COMMAND_KEYWORD = "nav";
    private static final String SEARCH_QUOTE_COMMAND_KEYWORD = "search";
    private static final String REGISTER_COMMAND_KEYWORD = "register";
    private static final String EXIT_COMMAND_KEYWORD = "exit";

    //Fixed Variable Declarations
    private static double MAX_PRICE = 9999.99;
    private static int MAX_QTY = 999;
    private static int MAX_ITEMS = 30;
    private static double MAX_TAX_RATE = 200.00;
    private static int MAX_ITEMNAME_LENGTH = 30;
    private static int MAX_QUOTENAME_LENGTH = 50;
    private static int MAX_COMPANYNAME_LENGTH = 46;
    private static int MAX_CUSTOMERNAME_LENGTH = 45;

    public static Command parse(String fullCommand, QuotelyState state, QuoteList quoteList)
            throws QuotelyException {

        // Precondition assertions
        assert state != null : "QuotelyState cannot be null";
        assert quoteList != null : "QuoteList cannot be null";
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new QuotelyException(QuotelyException.ErrorType.EMPTY_COMMAND);
        }

        logger.info("Parsing command: " + fullCommand);
        logger.fine("Current state - isInside quote: " + state.isInsideQuote() +
                " QuoteReference: "
                + (state.getQuoteReference() != null ? state.getQuoteReference().toString() : "null"));

        /*
         * edit parse method to allow command input depending on isInsideState
         * add exception handling in parser
         */
        fullCommand = fullCommand.trim();
        String command = fullCommand.split(" ")[0];
        logger.fine("Extracted command: '" + command + "'");

        String arguments = "";
        if (fullCommand.split(" ").length > 1) {
            arguments = fullCommand.split(" ", 2)[1].trim();
            logger.fine("Extracted arguments: '" + arguments + "'");
        }
        switch (command) {
        case REGISTER_COMMAND_KEYWORD:
            // available in all state
            return parseRegisterCommand(arguments);
        case ADD_QUOTE_COMMAND_KEYWORD:
            // available in all state
            return parseAddQuoteCommand(arguments, state);
        case DELETE_QUOTE_COMMAND_KEYWORD:
            // can use no quote name if inside a quote
            return parseDeleteQuoteCommand(arguments, state, quoteList);
        case SHOW_QUOTES_COMMAND_KEYWORD:
            // available in all state, for now?
            return new ShowQuotesCommand();
        case FINISH_QUOTE_COMMAND_KEYWORD:
            // inside quote only
            return parseFinishQuoteCommand(state);
        case DELETE_ITEM_COMMAND_KEYWORD:
            // can use without quote name if inside a quote
            return parseDeleteItemCommand(arguments, state, quoteList);
        case EXPORT_QUOTE_COMMAND_KEYWORD:
            // can use without quote name if inside a quote
            return parseExportCommand(arguments, state, quoteList);
        case ADD_ITEM_COMMAND_KEYWORD:
            // can use without quote name if inside a quote
            return parseAddItemCommand(arguments, state, quoteList);
        case CALCULATE_TOTAL_COMMAND_KEYWORD:
            // can use without quote name if inside a quote
            return parseCalculateTotalCommand(arguments, state, quoteList);
        case NAVIGATE_COMMAND_KEYWORD:
            // available in all states, but need to specify target location e.g. 'main' or quoteName
            return parseNavigateCommand(arguments, state, quoteList);
        case SEARCH_QUOTE_COMMAND_KEYWORD:
            // available in main menu only
            return parseSearchCommand(arguments, state);
        case EXIT_COMMAND_KEYWORD:
            // available in all state, for now
            return new ExitCommand();
        default:
            throw new QuotelyException(QuotelyException.ErrorType.INVALID_COMMAND);
        }
    }

    private static Command parseAddQuoteCommand(String arguments, QuotelyState state)
            throws QuotelyException {
        logger.fine("parseAddQuoteCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(ADD_QUOTE_COMMAND_PATTERN);
        Matcher m = p.matcher(arguments);

        if (m.find()) {
            String quoteName = m.group(1).trim();
            String customerName = m.group(2).trim();

            //parse quote name length
            if (quoteName.length() > MAX_QUOTENAME_LENGTH) {
                logger.warning("Invalid quote name length for add quote command: " + arguments);
                throw new QuotelyException(QuotelyException.ErrorType.INVALID_QUOTE_NAME);
            }

            //parse customer name length
            if (customerName.length() > MAX_CUSTOMERNAME_LENGTH) {
                logger.warning("Invalid customer name length for add quote command: " + arguments);
                throw new QuotelyException(QuotelyException.ErrorType.INVALID_CUSTOMER_NAME);
            }

            logger.info("Successfully parsed add quote command - Quote: '"
                    + quoteName + "', Customer: '" + customerName + "'");
            return new AddQuoteCommand(quoteName, customerName);
        } else {
            logger.warning("Invalid format for add quote command: " + arguments);
            throw new QuotelyException(
                    QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "quote n/QUOTE_NAME c/CUSTOMER_NAME");
        }
    }

    private static Command parseNavigateCommand(String arguments, QuotelyState state, QuoteList quoteList)
            throws QuotelyException {
        logger.fine("parseNavigateCommand called with arguments: " + arguments);

        String targetName = arguments.trim();
        if (targetName.equalsIgnoreCase("main")) {
            return new NavigateCommand();
        }

        Pattern p = Pattern.compile(QUOTENAME_ARG_PATTERN);
        Matcher m = p.matcher(arguments);

        String targetQuoteName = null;
        if (m.find()) {
            targetQuoteName = m.group(1).trim();
        } else {
            logger.warning("Failed to navigate to target with name: " + targetQuoteName);
            throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT, "nav main OR nav n/QUOTE_NAME");
        }

        try {
            Quote targetQuote = getQuoteFromStateAndName(targetQuoteName, state, quoteList);
            logger.info("Successfully parsed navigate command to target location" + targetQuote.getQuoteName());
            return new NavigateCommand(targetQuote);
        } catch (QuotelyException e) {
            logger.warning("Failed to navigate to target with name: " + targetQuoteName);
            if (targetQuoteName != null) {
                throw new QuotelyException(QuotelyException.ErrorType.QUOTE_NOT_FOUND, targetQuoteName);
            } else {
                throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "nav main OR nav n/QUOTE_NAME");
            }
        }
    }

    private static Command parseDeleteQuoteCommand(String arguments, QuotelyState state,
                                                   QuoteList quoteList) throws QuotelyException {
        logger.fine("parseDeleteQuoteCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(QUOTENAME_ARG_PATTERN);
        Matcher m = p.matcher(arguments);

        String quoteName = null;
        if (m.find()) {
            quoteName = m.group(1).trim();
        } else if (arguments.length() > 0) {
            // if arguments were provided but did not match pattern
            logger.warning("Invalid format for delete quote command: " + arguments);
            throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT, "unquote [n/QUOTE_NAME]");
        }
        try {
            Quote quote = getQuoteFromStateAndName(quoteName, state, quoteList);
            logger.info("Successfully parsed delete quote command for quote: " + quote.getQuoteName());
            return new DeleteQuoteCommand(quote);
        } catch (QuotelyException e) {
            logger.warning("Failed to find quote for deletion with name: " + quoteName);
            if (quoteName != null) {
                throw new QuotelyException(QuotelyException.ErrorType.QUOTE_NOT_FOUND, quoteName);
            } else {
                throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT, "unquote [n/QUOTE_NAME]");
            }
        }
    }

    private static Command parseExportCommand(String arguments, QuotelyState state,
                                              QuoteList quoteList) throws QuotelyException {
        logger.fine("parseExportCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(EXPORT_QUOTENAME_ARG_PATTERN);
        Matcher m = p.matcher(arguments);
        Pattern f = Pattern.compile(FILENAME_ARG_PATTERN);
        Matcher fileMatcher = f.matcher(arguments);
        Pattern startPattern = Pattern.compile(EXPORT_START_PATTERN);
        Matcher startMatcher = startPattern.matcher(arguments);
        if (arguments.length() > 0 && !startMatcher.find()) {
            logger.warning("Invalid format for export quote command: " + arguments);
            throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                "export [n/QUOTE_NAME] [f/FILENAME]");
        }

        String quoteName = null;
        if (m.find()) {
            quoteName = m.group(1).trim();
        }
        
        try {
            Quote quote = getQuoteFromStateAndName(quoteName, state, quoteList);
            String filename = quote.getQuoteName();
            if (fileMatcher.find() && fileMatcher.group(1).trim().length() > 0) {
                filename = fileMatcher.group(1).trim();
            }
            // remove any extension if user included it
            if (filename.contains(".")) {
                filename = filename.substring(0, filename.lastIndexOf('.'));
            }
            logger.info("Successfully parsed export quote command for quote: " + quote.getQuoteName());
            return new seedu.quotely.command.ExportQuoteCommand(quote, filename);
        } catch (QuotelyException e) {
            logger.warning("Failed to find quote for export with name: " + quoteName);
            if (quoteName != null) {
                throw new QuotelyException(QuotelyException.ErrorType.QUOTE_NOT_FOUND, quoteName);
            } else {
                throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "export [n/QUOTE_NAME] [f/FILENAME]");
            }
        }
    }

    private static Command parseRegisterCommand(String arguments) throws QuotelyException {
        logger.fine("parseRegisterCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(REGISTER_COMMAND_PATTERN);
        Matcher m = p.matcher(arguments);

        if (m.find()) {
            String name = m.group(1).trim();

            //parse company name
            if (name.length() > MAX_COMPANYNAME_LENGTH) {
                logger.warning("Invalid company name length for register command: " + arguments);
                throw new QuotelyException(QuotelyException.ErrorType.INVALID_COMPANY_NAME);
            }

            logger.info("Successfully parsed register command for company: " + name);
            return new RegisterCommand(name);
        } else {
            logger.warning("Invalid format for register command: " + arguments);
            throw new QuotelyException(
                    QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "register c/COMPANY_NAME");
        }
    }

    private static Command parseAddItemCommand(String arguments,
                                               QuotelyState state, QuoteList quoteList) throws QuotelyException {
        logger.fine("parseAddItemCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(ADD_ITEM_COMMAND_PATTERN);
        Matcher m = p.matcher(arguments);

        if (m.find()) {
            String itemName = m.group(1).trim();
            //parse item name length
            if (itemName.length() > MAX_ITEMNAME_LENGTH) {
                logger.warning("Invalid item name length for add item command: " + arguments);
                throw new QuotelyException(QuotelyException.ErrorType.INVALID_ITEM_NAME);
            }
            String quoteName = m.group(2) != null ? m.group(2).trim() : null;
            String priceStr = m.group(3).trim();
            String quantityStr = m.group(4).trim();
            String taxRateStr = m.group(5) != null ? m.group(5).trim() : null;

            logger.fine("Extracted - Item: '" + itemName + "', Quote: '" +
                    (quoteName != null ? quoteName : "<none>") + "', Price: '" +
                    priceStr + "', Quantity: '" + quantityStr + "'" + "', Tax: '" +
                    (taxRateStr != null ? taxRateStr : "<none>"));

            double price;
            int quantity;
            double taxRate = 0;
            Quote quote;

            // extract quote from quoteName or state
            try {
                quote = getQuoteFromStateAndName(quoteName, state, quoteList);

                //parse quote item count
                if (quote.getItems().size() > MAX_ITEMS) {
                    logger.warning("Invalid item count for quote for add item command: " + arguments);
                    throw new QuotelyException(QuotelyException.ErrorType.INVALID_ITEM_NUMBER);
                }

            } catch (QuotelyException e) {
                logger.warning("Failed to find quote name:" + quoteName + " for adding item");
                if (quoteName != null) {
                    throw new QuotelyException(QuotelyException.ErrorType.QUOTE_NOT_FOUND, quoteName);
                } else {
                    throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                            "add i/ITEM_NAME [n/QUOTE_NAME] p/PRICE q/QUANTITY [t/TAX_RATE]");
                }
            }

            // parse price
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    throw new QuotelyException(QuotelyException.ErrorType.INVALID_NUMBER_FORMAT);
                }
                if (price > MAX_PRICE) {
                    throw new QuotelyException(QuotelyException.ErrorType.INVALID_ITEM_PRICE);
                }
            } catch (NumberFormatException e) {
                logger.warning("Failed to parse price: " + e.getMessage());
                throw new QuotelyException(QuotelyException.ErrorType.INVALID_NUMBER_FORMAT);
            }

            // parse quantity
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    throw new QuotelyException(QuotelyException.ErrorType.INVALID_NUMBER_FORMAT);
                }
                if  (quantity > MAX_QTY) {
                    throw new QuotelyException(QuotelyException.ErrorType.INVALID_ITEM_QTY);
                }
            } catch (NumberFormatException e) {
                logger.warning("Failed to parse quantity: " + e.getMessage());
                throw new QuotelyException(QuotelyException.ErrorType.INVALID_NUMBER_FORMAT);
            }

            // parse tax rate if provided
            if (taxRateStr != null) {
                try {
                    taxRate = Double.parseDouble(taxRateStr);
                    if (taxRate < 0) {
                        throw new QuotelyException(QuotelyException.ErrorType.INVALID_NUMBER_FORMAT);
                    }
                    if (taxRate > MAX_TAX_RATE) {
                        throw new QuotelyException(QuotelyException.ErrorType.INVALID_ITEM_TAX);
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Failed to parse tax rate: " + e.getMessage());
                    throw new QuotelyException(QuotelyException.ErrorType.INVALID_NUMBER_FORMAT);
                }
            }

            logger.info("Successfully parsed add item command - Item: '" +
                    itemName + "' Price: " + price + " Quantity: " + quantity +
                    " Tax Rate: " + taxRate + " for quote: '" + quote.getQuoteName() + "'");

            return new AddItemCommand(itemName, quote, price, quantity, taxRate);
        } else {
            logger.warning("Invalid format for add item command: " + arguments);
            throw new QuotelyException(
                    QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "add i/ITEM_NAME [n/QUOTE_NAME] p/PRICE q/QUANTITY [t/TAX_RATE]");
        }
    }

    private static Command parseDeleteItemCommand(String arguments, QuotelyState state,
                                                  QuoteList quoteList) throws QuotelyException {
        logger.fine("parseDeleteItemCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(DELETE_ITEM_COMMAND_PATTERN);
        Matcher m = p.matcher(arguments);
        if (m.find()) {
            String itemName = m.group(1).trim();
            String quoteName = m.group(2) != null ? m.group(2).trim() : null;
            Quote quote;
            try {
                quote = getQuoteFromStateAndName(quoteName, state, quoteList);
            } catch (QuotelyException e) {
                logger.warning("Failed to find quote name:" + quoteName + " for deleting item");
                if (quoteName != null) {
                    throw new QuotelyException(QuotelyException.ErrorType.QUOTE_NOT_FOUND, quoteName);
                } else {
                    throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                            "delete i/ITEM_NAME [n/QUOTE_NAME]");
                }
            }
            if (!quote.hasItem(itemName)) {
                logger.warning("Item not found in quote - Item: '" + itemName +
                        "', Quote: '" + quote.getQuoteName() + "'");
                throw new QuotelyException(QuotelyException.ErrorType.ITEM_NOT_FOUND);
            }
            logger.info("Successfully parsed delete item command - Item: '" +
                    itemName + " for quote: '" + quote.getQuoteName() + "'");
            return new DeleteItemCommand(itemName, quote);
        } else {
            logger.warning("Invalid format for delete item command: " + arguments);
            throw new QuotelyException(
                    QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "delete i/ITEM_NAME [n/QUOTE_NAME]");
        }
    }

    private static Command parseCalculateTotalCommand(String arguments,
                                                      QuotelyState state, QuoteList quoteList) throws QuotelyException {
        logger.fine("parseCalculateTotalCommand called with arguments: " + arguments);
        Pattern p = Pattern.compile(QUOTENAME_ARG_PATTERN);
        Matcher m = p.matcher(arguments);
        String quoteName = null;
        if (m.find()) {
            quoteName = m.group(1).trim();
        }
        try {
            Quote quote = getQuoteFromStateAndName(quoteName, state, quoteList);
            return new CalculateTotalCommand(quote);
        } catch (QuotelyException e) {
            logger.warning("Invalid format for calculate total command: " + arguments);
            if (quoteName != null) {
                throw new QuotelyException(QuotelyException.ErrorType.QUOTE_NOT_FOUND, quoteName);
            } else {
                throw new QuotelyException(QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                        "total [n/QUOTE_NAME]");
            }
        }
    }

    private static Command parseFinishQuoteCommand(QuotelyState state)
            throws QuotelyException {
        logger.fine("parseFinishQuoteCommand called");
        if (state.isInsideQuote()) {
            logger.info("Successfully parsed finish quote command");
            return new FinishQuoteCommand();
        } else {
            logger.warning("Attempted to finish quote while not inside a quote");
            throw new QuotelyException(QuotelyException.ErrorType.NO_ACTIVE_QUOTE);
        }
    }

    private static Quote getQuoteFromStateAndName(String quoteName,
                                                  QuotelyState state, QuoteList quoteList) throws QuotelyException {
        // Precondition assertions
        assert state != null : "QuotelyState cannot be null";
        assert quoteList != null : "QuoteList cannot be null";

        logger.fine("getQuoteFromStateAndName called");

        if (quoteName == null && state.getQuoteReference() == null) {
            logger.warning("No quote name provided and no active quote in state");
            throw new QuotelyException(QuotelyException.ErrorType.NO_ACTIVE_QUOTE);
        } else if (quoteName != null) {
            logger.fine("Looking up quote by name: " + quoteName);
            return quoteList.getQuoteByName(quoteName);
        } else {
            logger.fine("Using current quote from state");
            return state.getQuoteReference();
        }
    }

    private static Command parseSearchCommand(String arguments, QuotelyState state) throws QuotelyException {
        logger.fine("parseSearchCommand called");
        String quoteName;

        Pattern p = Pattern.compile(QUOTENAME_ARG_PATTERN);
        Matcher m = p.matcher(arguments);

        if (m.find()) {
            quoteName = m.group(1).trim();
            logger.info("Successfully parsed search quote command");
            return new SearchQuoteCommand(quoteName);
        } else {
            logger.warning("Invalid format for search item command: " + arguments);
            throw new QuotelyException(
                    QuotelyException.ErrorType.WRONG_COMMAND_FORMAT,
                    "search n/QUOTE_NAME");
        }
    }
}
