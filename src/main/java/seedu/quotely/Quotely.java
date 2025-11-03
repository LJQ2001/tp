package seedu.quotely;

import seedu.quotely.command.Command;
import seedu.quotely.data.CompanyName;
import seedu.quotely.data.QuoteList;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.exception.QuotelyException;
import seedu.quotely.parser.Parser;
import seedu.quotely.ui.Ui;
import seedu.quotely.util.LoggerConfig;

import seedu.quotely.storage.Storage;
import seedu.quotely.storage.JsonSerializer;
import seedu.quotely.storage.ApplicationData;
import java.io.IOException;

import java.util.logging.Logger;

public class Quotely {
    private static Logger logger;

    private static final String DEFAULT_STORAGE_DIRECTORY = "data";
    private static final String DEFAULT_STORAGE_FILENAME = "quotely.json";

    private Ui ui;
    private CompanyName companyName;
    private QuoteList quoteList;
    private QuotelyState state;

    // Fields for storage
    private Storage storage;
    private JsonSerializer serializer;

    /**
     * Constructor for Quotely
     * 1) create new ui instance
     * 2) create new CompanyName and set a default company name
     * 3) initialise storage and load existing data if any
     */
    public Quotely() {
        ui = Ui.getInstance();
        state = QuotelyState.getInstance();
        companyName = new CompanyName("Default");

        // Initialize storage and load data
        storage = new Storage(DEFAULT_STORAGE_DIRECTORY, DEFAULT_STORAGE_FILENAME);
        serializer = new JsonSerializer();

        loadDataFromFile();

    }

    /**
     * Loads the QuoteList and CompanyName from the file.
     * If the file is not found or is corrupted, initializes with new empty data.
     */
    private void loadDataFromFile() {
        try {
            String jsonData = storage.loadData();
            // Deserialize the wrapper object
            ApplicationData loadedData = serializer.deserialize(jsonData);

            assert loadedData != null : "Deserialization should not return null";

            // Set the class fields from the loaded data
            this.quoteList = loadedData.getQuoteList();
            this.companyName = loadedData.getCompanyName();

            logger.info("Successfully loaded data from " + storage.getDataFilePath());

        } catch (IOException e) {
            logger.warning("Failed to read from data file. " +
                    "Starting with new data. Error: " + e.getMessage());
            ui.showError("Could not load data file. Starting fresh.");
            // Initialize both fields on failure
            this.quoteList = new QuoteList();
            this.companyName = new CompanyName("Default");
        } catch (Exception e) { // Catches potential JSON syntax errors
            logger.severe("Data file is corrupted. Starting with new data. Error: " + e.getMessage());
            ui.showError("Data file appears to be corrupted. Starting fresh.");
            // Initialize both fields on failure
            this.quoteList = new QuoteList();
            this.companyName = new CompanyName("Default");
        }
    }

    /**
     * Saves the current QuoteList and CompanyName to the file specified in storage.
     */
    private void saveDataToFile() {
        assert quoteList != null : "Cannot save a null QuoteList";
        assert companyName != null : "Cannot save a null CompanyName";
        assert serializer != null : "Serializer must be initialized to save";

        try {
            // Wrap both objects in the container
            ApplicationData appData = new ApplicationData(quoteList, companyName);
            // Serialize the wrapper object
            String jsonData = serializer.serialize(appData);

            storage.saveData(jsonData);
            logger.info("Data saved successfully to " + storage.getDataFilePath());
        } catch (IOException e) {
            logger.severe("Failed to save data to file: " + e.getMessage());
            ui.showError("Error: Failed to save data to file.");
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        ui.showLine();
        while (!isExit) {
            try {
                logger.finer("Reading user inputs...");
                String fullCommand = ui.readCommand(state);
                ui.showLine();
                logger.finer("Read successful, parsing command: ...");
                // parser throws QuotelyException if parse invalid
                Command command = Parser.parse(fullCommand, state, quoteList);
                logger.finer("Parse successful, executing command...");
                // execute throws QuotelyException if data mutation fails
                command.execute(ui, quoteList, companyName, state);
                isExit = command.isExit();

                // Save data after every successful command that doesn't exit
                if (!isExit) {
                    saveDataToFile();
                }

            } catch (QuotelyException e) {
                ui.showError(e.getMessage());
                logger.severe(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    public static void main(String[] args) {
        // Initialize global logging configuration
        LoggerConfig.initializeGlobalLogging();

        logger = LoggerConfig.getLogger(Quotely.class);
        logger.info("Starting Quotely application");

        try {
            new Quotely().run();
            logger.info("Quotely application finished successfully");
        } catch (Exception e) {
            logger.severe("Quotely application crashed: " + e.getMessage());
            throw e;
        }
    }
}
