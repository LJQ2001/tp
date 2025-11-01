# User Guide

## Table of Content

- [User Guide](#user-guide)
  - [Table of Content](#table-of-content)
  - [Introduction](#introduction)
  - [Quick Start](#quick-start)
  - [Features](#features)
    - [Register Company Name: `register`](#register-company-name-register)
    - [Creating a Quote: `quote`](#creating-a-quote-quote)
    - [Deleting a Quote: `unquote`](#deleting-a-quote-unquote)
    - [Adding an item: `add`](#adding-an-item-add)
    - [Delete an item `delete`](#delete-an-item-delete)
    - [Calculate the total `total`](#calculate-the-total-total)
    - [Export a quote: `export`](#export-a-quote-export)
    - [Finish the Quote `finish`](#finish-the-quote-finish)
    - [Navigate: `nav`](#navigate-nav)
    - [Searching for Quotes: `search`](#searching-for-quotes-search)
    - [Show all Quotes: `show`](#show-all-quotes-show)
    - [Exit `exit`](#exit-exit)
  - [FAQ](#faq)
  - [Command Summary](#command-summary)
  - [Coming soon](#coming-soon)

## Introduction

This user guide provides step-by-step instructions with examples on how to use Quotely, a CLI based application to help
you create quotes efficiently and cleanly.

V1.0 usage starts from the main menu, and enters the quotation menu when creating or editing a quote. The user may
return to the main menu by finishing or deleting the quote.

## Quick Start

1. Ensure that you have Java 17 or above installed.
2. Down the latest version of `Quotely`
   from [here](https://github.com/AY2526S1-CS2113-W10-1/tp/releases).
3. Copy quotely.jar to the folder you want to use as the home folder.
4. Open a command terminal, cd into the folder you put the jar file in, and use the java -jar quotely.jar command to
   run the application.

Refer to Features below to see more details on using the quotely application!

## Features

### Register Company Name: `register`

Register your company name. This is the name of your company billing the customer.

Command is available in both main menu and during quotation.

**Format:**

```
register c/COMPANY_NAME
```

* The `COMPANY_NAME` can be in a natural language format.

**Example:**

```
register c/NTU
register c/NUS
```

**Expected output:**

```
Registering company: NUS
```

### Creating a Quote: `quote`

Create a new quote for a customer.

Command is available only in the main menu (i.e. new quote cannot be created concurrently with editing another quote).

**Format:**

```
quote n/QUOTE_NAME c/CUSTOMER_NAME
```

* The `QUOTE_NAME` and `CUSTOMER_NAME` can be in a natural language format.
* This command can only be used in main menu.

**Example:**

```
quote n/007 c/NTU
quote n/new quote c/NUS
```

**Expected output:**

```
Adding quote: new quote for NUS
```

### Deleting a Quote: `unquote`

Delete Quote and all items inside for a given quote name.

Command is available in both main menu and during quotation.

* Deleting a quote from main menu requires user to specify quote name.
* Deleting a quote from within a quotation makes quote name optional.

**Format:**

```
unquote {n/QUOTE_NAME}
```

* The `QUOTE_NAME` can be in a natural language format.
* `{n/QUOTE_NAME}` - Quote name (optional when inside a quote; if specified, that quote will be deleted instead of the
  current one)

**Example:**

During quotation (deletes current quote):

```
unquote
```

In main menu or during quotation (deletes specified quote):

```
unquote n/007
unquote n/new quote
```

**Expected output:**

```
Deleting quote: new quote
```

### Adding an item: `add`

Add item to the quote with the unit cost and quantity.

Command is available in both main menu and during quotation.

* Adding item from main menu requires user to specify quote name.
* Adding item from within a quotation makes quote name optional.

**Format:**

```
add i/ITEM_NAME {n/QUOTE_NAME} p/PRICE q/QUANTITY {t/TAX_RATE}
```

* The `ITEM_NAME` and `QUOTE_NAME` can be in a natural language format.
* `{n/QUOTE_NAME}` - Quote name (optional when inside a quote; if specified, item will be added to that quote instead of
  the current one)
* The `PRICE` should be decimal and not negative(0 is allowed).
* The `QUANTITY` should be integer and postive.
* The `TAX_RATE` should be decimal and not negative.

**Example:**

During quotation (adds item to current quote):

```
add i/apple p/20.3 q/20
add i/book p/10.5 q/3
```

In main menu or during quotation (add item to specified quote):

```
add i/apple n/quote_1 p/20.3 q/20
add i/book n/quote_100 p/10.5 q/3
```

**Expected output:**

```
Adding book to quote quote_100 with price 10.50, quantity 3
```

### Delete an item `delete`

Deletes an item of matching name completely from the list of items in the quote, regardless of quantity.

Command is available in both main menu and during quotation.

* Deleting item from main menu requires user to specify quote name.
* Deleting item from within a quotation makes quote name optional.

**Format:**

```
delete i/ITEM_NAME {n/QUOTE_NAME}
```

* `{n/QUOTE_NAME}` - Quote name (optional when inside a quote; if specified, item will be deleted from that quote
  instead of the current one)

**Example:**

During quotation (deletes item to current quote):

```
delete i/apple
delete i/book
```

In main menu or during quotation (deletes item to specified quote):

```
delete i/apple n/quote_1
delete i/book n/quote_2
```

**Expected output:**

```
Deleting book from quote quote_100
```

### Calculate the total `total`

Calculate the total of specific quote, including GST.

Command is available in both main menu and during quotation.

* Calculating quote total from main menu requires user to specify quote name.
* Calculating quote total from within a quotation makes quote name optional.

**Format:**

```
total {n/QUOTE_NAME}
```

* `{n/QUOTE_NAME}` - Quote name (optional when inside a quote; if specified, total will be calculated for that quote
  instead of the current one)

**Example:**

During quotation (calculates total amount of current quote):

```
total
```

In main menu or during quotation (calculates total amount of specified quote):

```
total n/quote 1
```

**Expected output:**

```
Total cost of quote quote 1 for c: 1023.4
```

### Export a quote: `export`

Export a quote to PDF (invoice-style). The command formats the selected Quote and writes a PDF file into the current
working directory.

Key behaviours

- If run while editing a quote (inside quote), `export` with no `n/` argument exports the active quote.
- If run from the main menu, you must provide the quote name via `n/QUOTE_NAME`.
- Use `f/FILE_NAME` to explicitly set the output file name; otherwise the quote name is used.
- `.`, `\`, `..`, and `/` in the filename are replaced by `_` to prevent any insecure file 
paths.
- Filename is truncated to a maximum length of 255 characters to avoid file system errors.

**Format:**

```
export {n/QUOTE_NAME} {f/FILE_NAME}
```

Notes on filenames

- `FILE_NAME` may be provided with or without the `.pdf` extension. The application will ensure the final file uses the
  `.pdf` extension.

**Example:**

Export the active quote (inside quote):

```
export
```

Export a named quote from the main menu:

```
export n/quote_1
```

Export a named quote and save it as `Quote.pdf`:

```
export n/quote_1 f/Quote
```

### Finish the Quote `finish`

Finalise the current quote that the user is working on and exit to the main menu.

Command is available only during quotation.

**Example:**

```
finish
```

**Expected output:**

```
Finishing quote process.
```

### Navigate: `nav`

Navigate to other states.

Command is available in both main menu and during quotation.

* If in main menu, go to specific quote
* If quoting, go to main menu, or other quote

**Format:**

```
nav main
nav {n/QUOTE_NAME}
```

**Example:**

```
nav main
nav n/quote1
```

**Expected output**

The expected output shows navigation from quote1 to main menu then back to quote1.

```
quote1 > nav main
____________________________________________________________
Navigating to the main menu.
____________________________________________________________
main > nav n/quote1
____________________________________________________________
Navigating to quote: quote1
____________________________________________________________
quote1 > 
```

### Searching for Quotes: `search`

Searches for and displays all quotes whose names contain the provided search term.

Command is available **only in the main menu**.

**Format:**

```
search n/QUOTE_NAME
```

* The `QUOTE_NAME` is the text to search for within quote names.
* This command will display the full details for all quotes where the quote **contains** the `QUOTE_NAME`.
* This command cannot be used while you are inside a quote (editing a quote).
* Essentially similar to the `show` but instead of all Quotes it just shows the `QUOTE_NAME` quotes.

**Example:**

```
search n/NUS
```

**Expected output:**

```
_____________________________QUOTE______________________________
| Company name: Default                                        |
| Quote ID: 123                                                |
| Customer name: c                                             |
|--------------------------------------------------------------|
| Description                    | QTY |  Unit cost | Tax Rate |
|--------------------------------------------------------------|
| book                           |  10 | $    12.00 |   14.00 %|
|--------------------------------------------------------------|
|                                                              |
|                             Subtotal:                $120.00 |
|                             GST:                      $16.80 |
|                             Total:                   $136.80 |
|______________________________________________________________|

Successfully found quotes containing: NUS
```

### Show all Quotes: `show`

Show the current state of all quotes, with Subtotal, GST, and Total including GST.

Command is available in both main menu and during quotation.

**Example:**

```
show
```

**Expected output:**

```
_____________________________QUOTE______________________________
| Company name: Default                                        |
| Quote ID: 123                                                |
| Customer name: c                                             |
|--------------------------------------------------------------|
| Description                    | QTY |  Unit cost | Tax Rate |
|--------------------------------------------------------------|
| book                           |  10 | $    12.00 |   14.00 %|
|--------------------------------------------------------------|
|                                                              |
|                             Subtotal:                $120.00 |
|                             GST:                      $16.80 |
|                             Total:                   $136.80 |
|______________________________________________________________|
```

### Exit `exit`

Exit the program.

Command is available in both main menu and during quotation.

**Example:**

```
exit
```

**Expected output:**

```
Bye. Hope to see you again soon!
```

## FAQ

**Q**: Will I be able to access my past quote records after exiting the program?

**A**: Yes, from V2.0 onwards, every run saves a `quotely.json` file in a data subfolder. The data subfolder exists in
the same directory as the `quotely.jar` file.

**Q**: What happens if I accidentally deleted a quote/item? Can I retrieve that quote/item back?

**A**: Unfortunately, the app is not able to restore deleted quotes or items. Therefore, we suggest that you thoroughly
review
any deletion commands before running them as once executed, they are permanent.

**Q**: How do I transfer my data to another computer?

**A**: Feature is not available in V1.0, addition is planned for future versions.

## Command Summary

* Register company name `register c/COMPANY_NAME`
* Create a quote `quote n/QUOTE_NAME c/CUSTOMER_NAME`
* Delete a quote `unquote n/QUOTE_NAME`
* Add an item `add i/ITEM_NAME {n/QUOTE_NAME} p/PRICE q/QUANTITY`
* Delete an item `delete i/ITEM_NAME {n/QUOTE_NAME}`
* Export a quote to pdf file `export {n/QUOTE_NAME} f/FILE_NAME`
* Calculate the total of a quote `total {n/QUOTE_NAME}`
* Navigate to a quote or main menu `nav n/QUOTE_NAME/ nav main`
* Finish the Quote `finish`
* Show all quotes `show`
* Search for Quotes `search n/QUOTE_NAME`
* Exit the program `exit`

## Coming soon

We are working on exciting improvements to make Quotely more powerful and flexible for your business needs!

* Custom PDF Templates
    * Soon, you’ll be able to choose from multiple PDF layouts when exporting your quotes. This will allow you to choose
      the appropriate PDF template for any situation.
* Multi-Currency Support
    * Quotely will support different currencies. This means you can create quotes for clients anywhere in the world.
* Installment Calculator
    * Manage flexible billing for larger projects or long-term clients with ease. You will soon be able to break down
      totals into monthly or custom payments.

