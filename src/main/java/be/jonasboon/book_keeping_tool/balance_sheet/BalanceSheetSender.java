package be.jonasboon.book_keeping_tool.balance_sheet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor
@Service
public class BalanceSheetSender {

    private final BalanceSheetSaver balanceSheetSaver;

    public byte[] downloadBalanceSheet() throws IOException {
        InputStream is = new FileInputStream(balanceSheetSaver.getFullFilePath().toFile());
        return StreamUtils.copyToByteArray(is) ;
    }
}
