package com.aod.aod.service.impl;

import com.aod.aod.entity.Transaction;
import com.aod.aod.entity.User;
import com.aod.aod.repository.TransactionRepository;
import com.aod.aod.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private static final String FILE = "/home/olumide/Videos/MyStatement.pdf";

    /**
     * retrieve the list of transaction within a date range
     * generate a pdf for the list of transaction
     * send pdf via mail
     */

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate)throws FileNotFoundException, DocumentException{
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start))
                .filter(transaction -> transaction.getCreatedAt().isEqual(end))
                .toList();

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("AOD Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("1 Alaguntan Street, Idi Ope GRA Ibadan"));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statementTitle = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statementTitle.setBorder(0);
        PdfPCell endDateInfo = new PdfPCell(new Phrase("End Date: " + endDate));
        endDateInfo.setBorder(0);
        PdfPCell customer = new PdfPCell(new Phrase("Customer: " + customerName));
        customer.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Address: " + user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date: "));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("Transaction Type"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("Transaction Amount"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("Status"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionList.forEach(transaction -> {
            transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(customer);
        statementInfo.addCell(endDateInfo);
        statementInfo.addCell(customerInfo);


        return transactionList;
    }

//    private void designStatement(List<Transaction> transactionList) throws FileNotFoundException, DocumentException {
//
//        Rectangle statementSize = new Rectangle(PageSize.A4);
//        Document document = new Document(statementSize);
//        log.info("setting size of document");
//        OutputStream outputStream = new FileOutputStream(FILE);
//        PdfWriter.getInstance(document, outputStream);
//        document.open();
//
//        PdfPTable bankInfoTable = new PdfPTable(1);
//        PdfPCell bankName = new PdfPCell(new Phrase("AOD Bank"));
//        bankName.setBorder(0);
//        bankName.setBackgroundColor(BaseColor.BLUE);
//        bankName.setPadding(20f);
//
//        PdfPCell bankAddress = new PdfPCell(new Phrase("1 Alaguntan Street, Idi Ope GRA Ibadan"));
//        bankAddress.setBorder(0);
//
//        bankInfoTable.addCell(bankName);
//        bankInfoTable.addCell(bankAddress);
//
//        PdfPTable statementInfo = new PdfPTable(2);
//
//    }
}
