package com.fpt.report.utils;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ReportUtil {

    public static <T> byte[] generateReport(
            List<T> data,
            String jasperPath,
            Map<String, Object> parameters,
            String format
    ) throws JRException, IOException {
        JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperPath);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        if (parameters == null) {
            parameters = new HashMap<>();
        }

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            if ("PDF".equalsIgnoreCase(format)) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            } else if ("HTML".equalsIgnoreCase(format)) {
                HtmlExporter exporter = new HtmlExporter();

                // Set the input and output for the exporter
                // (input: JasperPrint, output: ByteArrayOutputStream)
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(byteArrayOutputStream));

                // Configuration for HTML export (optional)
                SimpleHtmlReportConfiguration reportConfig = new SimpleHtmlReportConfiguration();
                reportConfig.setWhitePageBackground(false);
                reportConfig.setRemoveEmptySpaceBetweenRows(true);
                exporter.setConfiguration(reportConfig);

                exporter.exportReport();
            } else if ("EXCEL".equalsIgnoreCase(format)) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));

                SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
                reportConfig.setRemoveEmptySpaceBetweenRows(true);
                reportConfig.setDetectCellType(true);
                reportConfig.setCollapseRowSpan(false);
                exporter.setConfiguration(reportConfig);

                exporter.exportReport();
            } else {
                throw new IllegalArgumentException("Unsupported format: " + format);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
}
