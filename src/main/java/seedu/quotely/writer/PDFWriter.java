package seedu.quotely.writer;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.util.List;
import java.awt.Color;

import seedu.quotely.data.CompanyName;
import seedu.quotely.data.Quote;
import seedu.quotely.data.Item;
import seedu.quotely.ui.Ui;

public class PDFWriter {
    private static PDFWriter writer = null;

    private PDFWriter() {
    }

    public static PDFWriter getInstance() {
        if (writer == null) {
            writer = new PDFWriter();
        }
        return writer;
    }

    public String getSecureFilename(String filename) {
        String sanitizedFilename = filename.
            replace("/", "_").
            replace("\\", "_").
            replace(".", "_").
            replace("..", "_");
        if (sanitizedFilename.length() > 255) {
            sanitizedFilename = sanitizedFilename.substring(0, 255);
        }
        return sanitizedFilename + ".pdf";
    }

    public void writeQuoteToPDF(Quote quote, CompanyName companyName, String filename) {
        List<Item> items = quote.getItems();
        Ui ui = Ui.getInstance();

        try {
            Document document = new Document(PageSize.A4, 50, 50, 50, 50);

            filename = getSecureFilename(filename);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(filename);
            } catch (Exception e) {
                ui.showMessage(e.getMessage());
                return;
            }
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();

            // Add quotation title
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("QUOTATION", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            // Add quotation info
            Font infoFont = new Font(Font.HELVETICA, 12);
            Paragraph invoiceInfo = new Paragraph(
                "Quotation Name: " + quote.getQuoteName() + "\n" + 
                "Company Name: " + companyName.getCompanyName() + "\n" + 
                "Customer Name: " + quote.getCustomerName() + "\n" +
                "Date: " + java.time.LocalDate.now().toString(), infoFont
            );
            document.add(invoiceInfo);

            document.add(Chunk.NEWLINE);

            // Table header
            Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            String[] headers = { "Description", "Unit Price", "Qty", "Taxed", "Amount" };
            int columnNumber = headers.length;

            // Add table for items
            PdfPTable table = new PdfPTable(columnNumber); // 5 columns
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 4, 2, 1, 2, 2 });

            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(new Color(230, 230, 230));
                table.addCell(cell);
            }
            double subtotal = 0.0;
            double totalTax = 0.0;

            // Table rows
            for (Item item : items) {
                table.addCell(new PdfPCell(new Phrase(item.getItemName())));

                PdfPCell priceCell = new PdfPCell(new Phrase(String.format("%.2f", item.getPrice())));
                priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(priceCell);

                PdfPCell qtyCell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity())));
                qtyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(qtyCell);

                PdfPCell taxCell = new PdfPCell(new Phrase(String.format("%.2f %%", item.getTaxRate())));
                taxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(taxCell);

                PdfPCell totalCell = new PdfPCell(new Phrase(String.format("%.2f",
                        item.getItemTotalTax() + item.getItemTotalPriceWithoutTax())));
                totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalCell);

                subtotal += item.getItemTotalPriceWithoutTax();
                totalTax += item.getItemTotalTax();
            }

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setColspan(columnNumber - 2);
            emptyCell.setBorder(Rectangle.NO_BORDER);

            // Add summary rows(subtotal, total tax, grand total)
            addSummaryRow(table, "Subtotal", subtotal, columnNumber);
            addSummaryRow(table, "Total Tax", totalTax, columnNumber);
            addSummaryRow(table, "Grand Total", subtotal + totalTax, columnNumber);
            
            document.add(table);

            document.close();
            writer.close();
            ui.showMessage("Exporting quote: " + quote.getQuoteName() + 
                " to " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addSummaryRow(PdfPTable table, String label, double amount, int columnSpan) {
        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setColspan(columnSpan - 2);
        emptyCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(emptyCell);

        PdfPCell labelCell = new PdfPCell(new Phrase(label, headFont));
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell amountCell = new PdfPCell(new Phrase(String.format("$ %.2f", amount), headFont));
        amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        amountCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(amountCell);
    }
}
