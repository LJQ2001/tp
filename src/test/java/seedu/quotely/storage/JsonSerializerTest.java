package seedu.quotely.storage;

import org.junit.jupiter.api.Test;
import seedu.quotely.command.AddItemCommand;
import seedu.quotely.command.DeleteItemCommand;
import seedu.quotely.command.DeleteQuoteCommand;
import seedu.quotely.data.CompanyName;
import seedu.quotely.data.Item;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuoteList;
import seedu.quotely.data.QuotelyState;
import seedu.quotely.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JsonSerializerTest {

    private final String referenceJsonString1 = "{\n" +
            "  \"quoteList\": {\n" +
            "    \"quotes\": []\n" +
            "  },\n" +
            "  \"companyName\": {\n" +
            "    \"companyName\": \"Default\"\n" +
            "  }\n" +
            "}";
    private final String referenceJsonString2 = "{\n" +
            "  \"quoteList\": {\n" +
            "    \"quotes\": [\n" +
            "      {\n" +
            "        \"quoteName\": \"1\",\n" +
            "        \"customerName\": \"1\",\n" +
            "        \"items\": []\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"companyName\": {\n" +
            "    \"companyName\": \"Default\"\n" +
            "  }\n" +
            "}";
    private final String referenceJsonString3 = "{\n" +
            "  \"quoteList\": {\n" +
            "    \"quotes\": [\n" +
            "      {\n" +
            "        \"quoteName\": \"1\",\n" +
            "        \"customerName\": \"1\",\n" +
            "        \"items\": [\n" +
            "          {\n" +
            "            \"itemName\": \"TestItem\",\n" +
            "            \"price\": 10.0,\n" +
            "            \"quantity\": 2,\n" +
            "            \"taxRate\": 0.0\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"companyName\": {\n" +
            "    \"companyName\": \"Default\"\n" +
            "  }\n" +
            "}";
    // New string to test a non-default company name
    private final String referenceJsonString4 = "{\n" +
            "  \"quoteList\": {\n" +
            "    \"quotes\": []\n" +
            "  },\n" +
            "  \"companyName\": {\n" +
            "    \"companyName\": \"My Test Company\"\n" +
            "  }\n" +
            "}";


    /**
     * Invalid input not tested as proper QuoteList is expected always
     */
    @Test
    public void serialize_validInput_success() {
        try {
            //setup test
            QuoteList quoteList = new QuoteList();
            JsonSerializer serializer = new JsonSerializer();
            Ui ui = Ui.getInstance();
            CompanyName companyName = new CompanyName("Default");
            QuotelyState state = QuotelyState.getInstance();

            //test 1: empty QuoteList, default CompanyName
            ApplicationData appData1 = new ApplicationData(quoteList, companyName);
            String testJson1 = serializer.serialize(appData1);
            assertEquals(referenceJsonString1, testJson1);

            //test 2: add 1 quote and test
            Quote quote = new Quote("1", "1");
            quoteList.addQuote(quote);
            ApplicationData appData2 = new ApplicationData(quoteList, companyName);
            String testJson2 = serializer.serialize(appData2);
            assertEquals(referenceJsonString2, testJson2);

            //test 3: add 1 item and test
            AddItemCommand addItemCommand = new AddItemCommand("TestItem", quote, 10.0, 2, 0);
            addItemCommand.execute(ui, quoteList, companyName, state);
            ApplicationData appData3 = new ApplicationData(quoteList, companyName);
            String testJson3 = serializer.serialize(appData3);
            assertEquals(referenceJsonString3, testJson3);

            //test 4: delete the item and assert that saved condition is identical to test 2
            DeleteItemCommand deleteItemCommand = new DeleteItemCommand("TestItem", quote);
            deleteItemCommand.execute(ui, quoteList, companyName, state);
            ApplicationData appData4 = new ApplicationData(quoteList, companyName); // Re-wrap
            String testJson4 = serializer.serialize(appData4); // Re-serialize
            assertEquals(referenceJsonString2, testJson4);

            //test 5: delete the quote and assert that the saved condition is identical to test 1
            DeleteQuoteCommand deleteQuoteCommand = new DeleteQuoteCommand(quote);
            deleteQuoteCommand.execute(ui, quoteList, companyName, state);
            ApplicationData appData5 = new ApplicationData(quoteList, companyName); // Re-wrap
            String testJson5 = serializer.serialize(appData5); // Re-serialize
            assertEquals(referenceJsonString1, testJson5);

            //test 6: change company name and test
            quoteList = new QuoteList(); // Reset quoteList to empty
            companyName.setCompanyName("My Test Company"); // Change the company name
            ApplicationData appData6 = new ApplicationData(quoteList, companyName);
            String testJson6 = serializer.serialize(appData6);
            assertEquals(referenceJsonString4, testJson6);

        } catch (Exception e) {
            assert false : "Execution should not fail.";
        }
    }

    @Test
    public void deserialize_validInput_success() {
        try {
            //setup test
            JsonSerializer serializer = new JsonSerializer();

            //test 1: deserialize empty QuoteList
            ApplicationData appData1 = serializer.deserialize(referenceJsonString1);
            QuoteList testQuoteList1 = appData1.getQuoteList();
            CompanyName testCompany1 = appData1.getCompanyName();
            assertEquals(0, testQuoteList1.getQuotes().size());
            assertEquals("Default", testCompany1.getCompanyName());

            //test 2: deserialize QuoteList with 1 quote
            ApplicationData appData2 = serializer.deserialize(referenceJsonString2);
            QuoteList testQuoteList2 = appData2.getQuoteList();
            assertEquals(1, testQuoteList2.getQuotes().size());
            assertEquals("1", testQuoteList2.getQuotes().get(0).getQuoteName());
            assertEquals("1", testQuoteList2.getQuotes().get(0).getCustomerName());
            assertEquals("Default", appData2.getCompanyName().getCompanyName());

            //test 3: deserialize QuoteList with item added
            ApplicationData appData3 = serializer.deserialize(referenceJsonString3);
            QuoteList testQuoteList3 = appData3.getQuoteList();
            assertEquals(1, testQuoteList3.getQuotes().size());
            Quote quote = testQuoteList3.getQuotes().get(0);
            Item item = quote.getItems().get(0);
            assertEquals("TestItem", item.getItemName());
            assertEquals(10.0, item.getPrice());
            assertEquals(2, item.getQuantity());
            assertFalse(item.hasTax());
            assertEquals("Default", appData3.getCompanyName().getCompanyName());

            //test 4: deserialize QuoteList with item removed and assert that saved condition is identical to test 2
            ApplicationData appData4 = serializer.deserialize(referenceJsonString4);
            QuoteList testQuoteList4 = appData4.getQuoteList();
            CompanyName testCompany4 = appData4.getCompanyName();
            assertEquals(0, testQuoteList4.getQuotes().size());
            assertEquals("My Test Company", testCompany4.getCompanyName());;

        } catch (Exception e) {
            assert false : "Execution should not fail.";
        }
    }
}

