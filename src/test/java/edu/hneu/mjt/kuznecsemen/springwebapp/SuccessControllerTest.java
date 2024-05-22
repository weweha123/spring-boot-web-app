package edu.hneu.mjt.kuznecsemen.springwebapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(SuccessController.class)
public class SuccessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuccessReportRepository successReportRepository;

    @Test
    public void aboutTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/about"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("about"));
    }

    @Test
    public void successReportsTest() throws Exception {

        java.util.List<SuccessReport> reports = new java.util.ArrayList<>();
        SuccessReport report = new SuccessReport();
        report.setStudentName("John");
        report.setStudentPatronymic("Doe");
        report.setGrade(94f);
        reports.add(report);

        org.mockito.Mockito.when(successReportRepository.findAll()).thenReturn(reports);

        mockMvc.perform(MockMvcRequestBuilders.get("/reports"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("successReports"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("successReports"))
                .andExpect(MockMvcResultMatchers.model().attribute("successReports", reports));
    }

    @Test
    public void rootTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/reports"));
    }
    @Test
    public void show_withInvalidId_returnsRedirect() throws Exception {
        when(successReportRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/reports/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/reports"));
    }

    @Test
    public void show_withValidId_returnsSuccessReport() throws Exception {
        SuccessReport report = new SuccessReport();
        report.setStudentName("John");
        report.setStudentPatronymic("Doe");
        report.setGrade(94f);

        when(successReportRepository.findById(1)).thenReturn(Optional.of(report));

        mockMvc.perform(MockMvcRequestBuilders.get("/reports/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("successReport"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("report"))
                .andExpect(MockMvcResultMatchers.model().attribute("report", report));
    }

    @Test
    public void edit_withInvalidId_returnsRedirect() throws Exception {
        when(successReportRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/reports/1/edit"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/reports"));
    }

    @Test
    public void edit_withValidId_returnsUpdateSuccessReport() throws Exception {
        SuccessReport report = new SuccessReport();
        report.setStudentName("John");
        report.setStudentPatronymic("Doe");
        report.setGrade(94f);

        when(successReportRepository.findById(1)).thenReturn(Optional.of(report));

        mockMvc.perform(MockMvcRequestBuilders.get("/reports/1/edit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.view().name("updateSuccessReport"))
               .andExpect(MockMvcResultMatchers.model().attributeExists("report"))
               .andExpect(MockMvcResultMatchers.model().attribute("report", report));
   }

    @Test
    public void showAddForm_returnsAddSuccessReport() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reports/new"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("addSuccesReport"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("newReport"));
    }
   @Test
    public void addReport_withValidReport_returnsRedirect() throws Exception {
        SuccessReport report = new SuccessReport();
        report.setStudentName("John");
        report.setStudentPatronymic("Doe");
        report.setGrade(94f);

        when(successReportRepository.save(any(SuccessReport.class))).thenReturn(report);

        mockMvc.perform(MockMvcRequestBuilders.post("/reports")
                .flashAttr("successReport", report))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

   @Test
    public void addReport_withInvalidReport_returnsRedirectWithoutSaving() throws Exception {
        SuccessReport report = new SuccessReport();
        // Invalid report without required fields

        mockMvc.perform(MockMvcRequestBuilders.post("/reports")
                .flashAttr("successReport", report))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        verify(successReportRepository, times(0)).save(any(SuccessReport.class));
    }
    @Test
    public void updateReport_withInvalidId_returnsRedirect() throws Exception {
        SuccessReport report = new SuccessReport();
        report.setStudentName("John");
        report.setStudentPatronymic("Doe");
        report.setGrade(94f);

        when(successReportRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/reports/1/edit")
                .flashAttr("report", report))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/reports"));
    }

    @Test
    public void updateReport_withValidId_returnsRedirect() throws Exception {
        SuccessReport report = new SuccessReport();
        report.setStudentName("John");
        report.setStudentPatronymic("Doe");
        report.setGrade(94f);

        SuccessReport storedReport = new SuccessReport();
        storedReport.setStudentName("Jack");
        storedReport.setStudentPatronymic("Smith");
        storedReport.setGrade(85f);

        when(successReportRepository.findById(1)).thenReturn(Optional.of(storedReport));
        when(successReportRepository.save(any(SuccessReport.class))).thenReturn(storedReport);

        mockMvc.perform(MockMvcRequestBuilders.post("/reports/1/edit")
                .flashAttr("report", report))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }
    @Test
    public void deleteReport_withValidId_returnsRedirect() throws Exception {
        when(successReportRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/reports/1/delete"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        verify(successReportRepository, times(1)).deleteById(1);
    }
}
