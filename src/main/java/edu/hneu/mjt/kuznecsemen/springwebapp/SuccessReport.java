package edu.hneu.mjt.kuznecsemen.springwebapp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SuccessReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "student_name", nullable = false, length = 50)
    private String studentName;

    @Size(max = 50)
    @NotNull
    @Column(name = "student_patronymic", nullable = false, length = 50)
    private String studentPatronymic;

    @NotNull
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate = LocalDate.now();

    @NotNull
    @Column(name = "grade", nullable = false)
    private Float grade;

}