package seedu.quotely.ui;

import seedu.quotely.data.CompanyName;
import seedu.quotely.data.Item;
import seedu.quotely.data.Quote;
import seedu.quotely.data.QuotelyState;

import java.util.Scanner;

public class Ui {
    private static Ui ui = null;
    private Scanner scanner;

    private Ui() {
        scanner = new Scanner(System.in);
    }

    public static Ui getInstance() {
        if (ui == null) {
            ui = new Ui();
        }
        return ui;
    }

    public void showWelcome() {
        String logo = " ____  _     ____ _____ _____ _    ___  _\n" + //
                "/  _ \\/ \\ /\\/  _ Y__ __Y  __// \\   \\  \\//\n" + //
                "| / \\|| | ||| / \\| / \\ |  \\  | |    \\  / \n" + //
                "| \\_\\|| \\_/|| \\_/| | | |  /_ | |_/\\ / /  \n" + //
                "\\____\\\\____/\\____/ \\_/ \\____\\\\____//_/   \n";
        System.out.println("Hello from\n" + logo);
        System.out.println("What can I do for you?");
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showExitMessage() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public String readCommand(QuotelyState state) {
        if (state.isInsideQuote()) {
            System.out.print(state.getQuoteReference().toString() + " > ");
        } else {
            System.out.print("main > ");
        }
        return scanner.nextLine();
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void promptCompanyName() {
        System.out.println("Please enter your company name: ");
        // to be updated to prompt user if companyName is default String
    }

    private void showInfoLine(StringBuilder stringBuilder, int boxInner, String title) {
        stringBuilder.append(String.format("| %-" + boxInner + "s |%n", title));
    }

    private void showInfoLines(StringBuilder stringBuilder, int boxInner, CompanyName companyName, Quote q) {
        showInfoLine(stringBuilder, boxInner, "Company name: " + companyName.getCompanyName());
        showInfoLine(stringBuilder, boxInner, "Quote ID: " + q.getQuoteName());
        showInfoLine(stringBuilder, boxInner, "Customer name: " + q.getCustomerName());
    }

    private void showTableBody(StringBuilder stringBuilder, int wDesc, int wQty, int wUnit, int wTax, Quote q) {
        final String emptyRowFmt = "| %-" + 
                wDesc + "s | %" + 
                wQty + "s | %" + 
                wUnit + "s | %" + 
                wTax + "s |%n";
        final String itemFmt = "| %-" +
                wDesc + "s | %" +
                wQty + "d | $%" +
                (wUnit - 1) + ".2f | %" +
                (wTax - 1) + ".2f %%|%n";

        if (q.getItems().isEmpty()) {
            stringBuilder.append(String.format(emptyRowFmt, "(no items)", "-", "-", "-"));
        } else {
            for (Item it : q.getItems()) {
                String name = it.getItemName();
                if (name.length() > wDesc) {
                    name = name.substring(0, wDesc);
                }
                stringBuilder.append(String.format(itemFmt, name, it.getQuantity(), it.getPrice(), it.getTaxRate()));
            }
        }
    }

    /**
     * Formats amount to 2 decimal places.
     * @param amount the amount to format
     * @return the formatted amount as a string with 2 decimal places
     */
    private String formatAmount(Double amount) {
        return String.format("%.2f", amount);
    }

    private void showSummaryLines(StringBuilder stringBuilder, int indent, int labelW, int amtW, Quote q) {
        String summaryFmt = "| %" + indent + "s%-" + labelW + "s %-" + amtW + "s |%n";
        stringBuilder.append(String.format(summaryFmt, "", 
            "Subtotal:", "$" + formatAmount(q.getQuoteTotalPriceWithoutTax())));
        stringBuilder.append(String.format(summaryFmt, "", 
            "GST:", "$" + formatAmount(q.getQuoteTotalTax())));
        stringBuilder.append(String.format(summaryFmt, "", 
            "Total:", "$" + formatAmount(q.getQuoteTotal())));
    }

    public void showQuote(CompanyName companyName, Quote q) {
        // ===== choose ONE inner width for the whole box =====
        final int boxInner = 60; // chars between the two side pipes

        // items table columns (these MUST sum to boxInner - 8)
        final int wQty = 3;
        final int wUnit = 10;
        final int wTax = 8;
        final int wDesc = boxInner - wQty - wUnit - wTax - 9; // 9 accounts for " | ", " | "

        // totals block (left margin + label + space + amount = boxInner)
        final int amtW = 15; // width for "$xx.xx" (right aligned)
        final int indent = 34; // left margin you want before totals
        final int labelW = boxInner - indent - amtW - 1; // -1 for the single space before amount

        // reusable strings
        final String dash = "-".repeat(boxInner + 2);
        final String und = "_".repeat(boxInner + 2);

        StringBuilder stringBuilder = new StringBuilder();

        // ===== header (all rows exactly boxInner wide) =====
        String title = "QUOTE";
        int pad = Math.max(0, boxInner+4 - title.length());
        int left = pad / 2;
        int right = pad - left;
        stringBuilder.append(String.format("%s%n", "_".repeat(left) + title + "_".repeat(right)));

        showInfoLines(stringBuilder, boxInner, companyName, q);
        stringBuilder.append(String.format("|%s|%n", dash));

        // ===== table header =====
        final String headerFmt = "| %-" + wDesc + "s | %" + wQty + "s | %" + wUnit + "s | %" + wTax + "s |%n";
        String headerLine = String.format(headerFmt, "Description", "QTY", "Unit cost", "Tax Rate");
        stringBuilder.append(headerLine);
        stringBuilder.append(String.format("|%s|%n", dash));

        // ===== table body =====
        showTableBody(stringBuilder, wDesc, wQty, wUnit, wTax, q);
        stringBuilder.append(String.format("|%s|%n", dash));
        stringBuilder.append(String.format("| %-" + boxInner + "s |%n", "")); // blank spacer row
        
        // ===== summary lines (subtotal, tax, total) =====
        showSummaryLines(stringBuilder, indent, labelW, amtW, q);
        stringBuilder.append(String.format("|%s|%n", und));

        showMessage(stringBuilder.toString());
    }
}
