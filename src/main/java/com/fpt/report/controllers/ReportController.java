package com.fpt.report.controllers;

import com.fpt.report.models.Student;
import com.fpt.report.services.StudentService;
import com.fpt.report.utils.ReportUtil;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final StudentService studentService;

    @GetMapping("/student/{rollNumber}")
    public ResponseEntity<byte[]> generateStudentReport(
            @PathVariable(name = "rollNumber") Integer rollNumber,
            @RequestParam(defaultValue = "PDF") String format) throws IOException {
        try {
            // Fetch student with the given roll number
            List<Student> students = null;
            if (rollNumber != null) {
                students = List.of(studentService.getStudentById(rollNumber));
            } else {
                students = new ArrayList<>();
                students.add(new Student());
            }

            // Path to the compiled .jasper file
            String jasperPath = new File("src/main/resources/reports/StudentScoreCard.jasper").getAbsolutePath();

            // Parameters to be passed to the report
            HashMap<String, Object> parameters = new HashMap<>();
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(students.getFirst().getScoreCards());
            parameters.put("TABLE_DATA_SOURCE", dataSource);

            // Generate report with the selected format
            byte[] reportData = ReportUtil.generateReport(students, jasperPath, parameters, format);

            // Return the report data with the appropriate content type
            if ("PDF".equalsIgnoreCase(format)) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/pdf")
                        .header("Content-Disposition", "attachment; filename=student-report.pdf")
                        .body(reportData);
            } else if ("HTML".equalsIgnoreCase(format)) {
                return ResponseEntity.ok()
                        .header("Content-Type", "text/html; charset=UTF-8")
                        .body(reportData);
            } else if ("EXCEL".equalsIgnoreCase(format) || "XLSX".equalsIgnoreCase(format)) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        .header("Content-Disposition", "attachment; filename=student-report.xlsx")
                        .body(reportData);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

        } catch (JRException e) {
            throw new RuntimeException("Error generating report", e);
        }
    }
}
