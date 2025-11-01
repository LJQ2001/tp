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
        DUPLICATE_QUOTE_NAME
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
            return "I'm sorry, but I don't know what that means.";
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
        default:
            return "An unknown error occurred.";
        }
    }
}
