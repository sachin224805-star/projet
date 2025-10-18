package com.weconnect.controller;

import com.weconnect.model.Report;
import com.weconnect.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.weconnect.dto.ReportDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<?> all() {
        List<Report> reports = reportRepository.findAll();
        reports.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return ResponseEntity.ok(reports);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ReportDTO body, HttpServletRequest req) {
        String userId = (String) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("message", "Authentication required"));

        Report r = new Report();
        r.setSubject(body.subject);
        r.setSubjectLabel(body.subjectLabel);
        r.setTitle(body.title);
        r.setDetails(body.details);
        r.setLocation(body.location);
        r.setFileUrl(body.fileUrl);
        r.setUserId(userId);
        r.setCreatedAt(new Date());
        r.setUpdatedAt(new Date());

        reportRepository.save(r);
        return ResponseEntity.status(201).body(r);
    }

    @PatchMapping("/{id}/reply")
    public ResponseEntity<?> reply(@PathVariable String id, @RequestBody Map<String, String> body, HttpServletRequest req) {
        String role = (String) req.getAttribute("role");
        if (!"admin".equals(role)) return ResponseEntity.status(403).body(Map.of("message", "Admin access required"));

        var opt = reportRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("message", "Report not found"));

        Report report = opt.get();
        report.setReply(body.get("reply"));
        report.setStatus(body.getOrDefault("status", "In Progress"));
        report.setUpdatedAt(new Date());
        reportRepository.save(report);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/user")
    public ResponseEntity<?> myReports(HttpServletRequest req) {
        String userId = (String) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("message", "Authentication required"));
        var list = reportRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        var opt = reportRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.status(404).body(Map.of("message", "Report not found"));
        return ResponseEntity.ok(opt.get());
    }
}
