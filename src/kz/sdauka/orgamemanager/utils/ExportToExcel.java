package kz.sdauka.orgamemanager.utils;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import kz.sdauka.orgamemanager.entity.Session;
import kz.sdauka.orgamemanager.entity.SessionDetails;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Dauletkhan on 23.01.2015.
 */
public class ExportToExcel {
    private static final Logger LOG = Logger.getLogger(ExportToExcel.class);

    public static File exportToExcel(Session session, List<SessionDetails> sessionDetailses) {
        File file = null;
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        try {
            file = new File("C:\\Windows\\Temp\\Report_" + dateFormat.format(session.getDay()) + session.getId() + ".xls");
            WritableWorkbook excel = Workbook.createWorkbook(file);
            WritableSheet sessions = excel.createSheet("Отчет", 0);
            WritableSheet sessionDetails = excel.createSheet("Подробности", 1);
            addCellsToSession(sessions, session);
            addCellsToSession(sessions, session);
            addCellsToSessionDetails(sessionDetails, sessionDetailses);
            expandColumn(sessions, 6);
            expandColumn(sessionDetails, 3);
            excel.write();
            excel.close();
        } catch (IOException | WriteException e) {
            LOG.error(e);
        }
        return file;
    }

    private static void expandColumn(WritableSheet sheet, int amountOfColumns) {
        int c = amountOfColumns;
        for (int x = 0; x < c; x++) {
            CellView cell = sheet.getColumnView(x);
            cell.setAutosize(true);
            sheet.setColumnView(x, cell);
        }
    }

    private static void addCellsToSession(WritableSheet sessions, Session session) throws WriteException {
        sessions.addCell(new Label(0, 1, String.valueOf(session.getId())));
        sessions.addCell(new Label(1, 1, session.getOperator()));
        sessions.addCell(new Label(2, 1, String.valueOf(session.getDay())));
        sessions.addCell(new Label(3, 1, String.valueOf(session.getStartTime())));
        sessions.addCell(new Label(4, 1, String.valueOf(session.getStopTime())));
        sessions.addCell(new Label(5, 1, String.valueOf(session.getCountStart())));
        sessions.addCell(new Label(6, 1, String.valueOf(session.getSum())));
        sessions.addCell(new Label(0, 0, "ID"));
        sessions.addCell(new Label(1, 0, "Оператор"));
        sessions.addCell(new Label(2, 0, "День"));
        sessions.addCell(new Label(3, 0, "Время начала сессии"));
        sessions.addCell(new Label(4, 0, "Время окончания сессии"));
        sessions.addCell(new Label(5, 0, "Количество запусков"));
        sessions.addCell(new Label(6, 0, "Сумма"));
    }

    private static void addCellsToSessionDetails(WritableSheet sessionDetails, List<SessionDetails> sessionDetailses) throws WriteException {
        sessionDetails.addCell(new Label(0, 0, "ID"));
        sessionDetails.addCell(new Label(1, 0, "Время начала сессии"));
        sessionDetails.addCell(new Label(2, 0, "Название игры"));
        for (int i = 0; i < sessionDetailses.size(); i++) {
            SessionDetails details = sessionDetailses.get(i);
            sessionDetails.addCell(new Label(0, i + 1, String.valueOf(details.getId())));
            sessionDetails.addCell(new Label(1, i + 1, String.valueOf(details.getStartTime())));
            sessionDetails.addCell(new Label(2, i + 1, details.getGameName()));
        }

    }
}
