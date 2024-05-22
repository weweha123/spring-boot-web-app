package edu.hneu.mjt.kuznecsemen.springwebapp;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@AllArgsConstructor
public class SuccessController {

    private SuccessReportRepository repository;

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/reports")
    public String successReports(Model model) {
        model.addAttribute("successReports", repository.findAll());
        return "successReports";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/reports";
    }

    @GetMapping("/reports/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        var report = repository.findById(id);
        if (report.isEmpty()) {
            return "redirect:/reports";
        }
        model.addAttribute("report", report.get());
        return "successReport";
    }

    @GetMapping("/reports/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model) {
        var report = repository.findById(id);
        if (report.isEmpty()) {
            return "redirect:/reports";
        }
        model.addAttribute("report", report.get());
        return "updateSuccessReport";
    }

    @GetMapping("/reports/new")
    public String showAddForm(Model model) {
        model.addAttribute("newReport", new SuccessReport());
        return "addSuccesReport";
    }

    @PostMapping("/reports")
    public String addReport(SuccessReport successReport) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<SuccessReport>> violations = validator.validate(successReport);

        if (violations.isEmpty()) {
            repository.save(successReport);
        }

        return "redirect:/";
    }

    @PostMapping("/reports/{id}/edit")
    public String updateReport(@PathVariable("id") Integer id, SuccessReport successReport) {
        var report = repository.findById(id);
        if (report.isEmpty()) {
            return "redirect:/reports";
        }
        var existedReport = report.get();
        existedReport.setStudentName(successReport.getStudentName());
        existedReport.setStudentPatronymic(successReport.getStudentPatronymic());
        existedReport.setGrade(successReport.getGrade());

        repository.save(existedReport);
        return "redirect:/";
    }

    @PostMapping("/reports/{id}/delete")
    public String deleteReport(@PathVariable("id") Integer id) {
        repository.deleteById(id);
        return "redirect:/";
    }
}
