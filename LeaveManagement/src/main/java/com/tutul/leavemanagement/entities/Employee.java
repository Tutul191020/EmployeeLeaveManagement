package com.tutul.leavemanagement.entities;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Data
@Entity
@Table(name="Employee")

public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String department;
    private String mail;
    @Setter
    @Getter
    private long totalLeavesAvailable;


}
