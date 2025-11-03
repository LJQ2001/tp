package seedu.quotely.exception;

public class QuotelyException extends Exception {
    public enum ErrorType {
        INVALID_COMMAND,
        WRONG_COMMAND_FORMAT,
        INVALID_NUMBER_FORMAT,
        FILE_ERROR,
        INVALID_TASK_NUMBER,
        EMPTY_DESCRIPTION,
        EMPTY_TASK_LIST,
        INVALID_DATE_TIME,
        DUPLICATE_TASK,
        NO_ACTIVE_QUOTE,
        QUOTE_NOT_FOUND,
        ITEM_NOT_FOUND,
        INVALID_STATE,
        EMPTY_COMMAND,
        DUPLICATE_QUOTE_NAME,
        INVALID_QUOTE_NAME,
        INVALID_ITEM_NUMBER,
        INVALID_ITEM_NAME,
        INVALID_ITEM_PRICE,
        INVALID_ITEM_QTY,
        INVALID_ITEM_TAX,
        INVALID_CUSTOMER_NAME,
        INVALID_COMPANY_NAME
    }

    private final ErrorType errorType;
    private String message;

    public QuotelyException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public QuotelyException(ErrorType errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    @Override
    public String getMessage() {
        switch (errorType) {
        case FILE_ERROR:
            return "There was an error loading the file.";
        case INVALID_COMMAND:
            return handleInvalidCommand();
        case WRONG_COMMAND_FORMAT:
            return "The command format is incorrect. \nHere is the correct format: \n" + message;
        case INVALID_TASK_NUMBER:
            return "The task number provided is invalid.";
        case EMPTY_DESCRIPTION:
            return "The description of a task cannot be empty.";
        case EMPTY_TASK_LIST:
            return "The task list is currently empty.";
        case INVALID_DATE_TIME:
            return "The date/time format is invalid. Please use YYYY-MM-DD HH:MM.";
        case DUPLICATE_TASK:
            return "This task already exists in your list.";
        case INVALID_NUMBER_FORMAT:
            return "The number format is invalid. Please enter a valid number.";
        case NO_ACTIVE_QUOTE:
            return "There is no active quote. Please navigate to a quote or specify one in the commands.";
        case QUOTE_NOT_FOUND:
            return "The specified quote (" + message +  ") was not found.";
        case ITEM_NOT_FOUND:
            return "The specified item was not found in the quote.";
        case INVALID_STATE:
            return "This command should be executed in Main Menu.";
        case EMPTY_COMMAND:
            return "The command cannot be empty. Please enter a valid command.";
        case DUPLICATE_QUOTE_NAME:
            return "A quote with this name already exists. Please use a different name.";
        case INVALID_QUOTE_NAME:
            return "The quote name provided is too long. Please try again with a shorter name.";
        case INVALID_ITEM_NUMBER:
            return "You have reached the maximum number of items for a single quote. Delete any existing quotes" +
                    "or try with a new quote.";
        case INVALID_ITEM_NAME:
            return "The item name provided is too long. Please try again with a shorter name.";
        case INVALID_ITEM_PRICE:
            return "The item price provided is higher than the maximum price allowed. Please try again with a smaller" +
                    " value.";
        case INVALID_ITEM_QTY:
            return "The item quantity provided is higher than the maximum quantity allowed. Please try again with a " +
                    "smaller quantity.";
        case INVALID_ITEM_TAX:
            return "The item tax provided is higher than the maximum tax rate allowed. Please try again with a " +
                    "lower tax rate.";
        case INVALID_CUSTOMER_NAME:
            return "The customer name provided is too long. Please try again with a shorter name.";
        case INVALID_COMPANY_NAME:
            return "The company name provided is too long. Please try again with a shorter name.";
        default:
            return "An unknown error occurred.";
        }
    }

    private String handleInvalidCommand() {
        return "I'm sorry, but I don't know what that means. \n\n" +
                "Here are valid commands you may give: \n" +
                "1) Register company name `register c/COMPANY_NAME`\n" +
                "2) Create a quote `quote n/QUOTE_NAME c/CUSTOMER_NAME\n" +
                "3) Delete a quote `unquote n/QUOTE_NAME`\n" +
                "4) Add an item `add i/ITEM_NAME {n/QUOTE_NAME} p/PRICE q/QUANTITY`\n" +
                "5) Delete an item `delete i/ITEM_NAME {n/QUOTE_NAME}`\n" +
                "6) Export a quote to pdf file `export {n/QUOTE_NAME} f/FILE_NAME`\n" +
                "7) Calculate the total of a quote `total {n/QUOTE_NAME}`\n" +
                "8) Navigate to a quote or main menu `nav n/QUOTE_NAME/ nav main`\n" +
                "9) Finish the Quote `finish`\n" +
                "10) Show all quotes `show`\n" +
                "11) Search for Quotes `search n/QUOTE_NAME`\n" +
                "12) Exit the program `exit`\n";
    }
}
