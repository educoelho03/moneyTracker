package br.com.moneyTracker.service.reportTransaction;

import br.com.moneyTracker.dto.response.TransactionResponseDTO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TransactionExportToExcelService extends ReportAbstract {

    public void writeTableData(Object data){
        List<TransactionResponseDTO> transactions = (List<TransactionResponseDTO>) data;
        CellStyle style = getFontContentExcel();
        int startRow = 2;

        for (TransactionResponseDTO transactionResponseDTO : transactions) {
            Row row = sheet.createRow(startRow++);
            int columnCount = 0;
            createCell(row, columnCount++, transactionResponseDTO.name(), style);
            createCell(row, columnCount++, transactionResponseDTO.amount(), style);
            createCell(row, columnCount++, transactionResponseDTO.transactionType().toString(), style);
            createCell(row, columnCount++, transactionResponseDTO.transactionCategory().toString(), style);
            createCell(row, columnCount++, transactionResponseDTO.date().toString(), style);
        }
    }

    public void exportToExcel(HttpServletResponse response, Object data) throws IOException {
        newReportExcel();

        // response writer to excel
        response = initResponseforExcelReport(response, "UserExcel");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            // write sheet, title & header
            String[] headers = new String[]{"No", "Name", "Amount", "TransactionType", "TransactionCategory", "Data"};
            writeTableHeaderExcel("Sheet User", "Report User", headers);

            // write content row
            writeTableData(data);

            workbook.write(outputStream);
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }
}

