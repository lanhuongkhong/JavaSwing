/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DoAn.GiaoDien;

/**
 *
 * @author PC
 */
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceNumberManager {
    private static final String FILE_NAME = "last_invoice_number.txt";
    private static final AtomicInteger currentInvoiceNumber = new AtomicInteger();

    static {
        // Load the last invoice number from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine();
            if (line != null) {
                currentInvoiceNumber.set(Integer.parseInt(line));
            } else {
                currentInvoiceNumber.set(1); // Start from 1 if the file is empty
            }
        } catch (IOException e) {
            currentInvoiceNumber.set(1); // Start from 1 if the file doesn't exist or is unreadable
        }
    }

    public static synchronized String getNextInvoiceNumber() {
        int nextNumber = currentInvoiceNumber.incrementAndGet();
        // Save the next invoice number to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(String.valueOf(nextNumber));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "HD" + String.format("%03d", nextNumber); // Format as "HD001", "HD002", etc.
    }
}