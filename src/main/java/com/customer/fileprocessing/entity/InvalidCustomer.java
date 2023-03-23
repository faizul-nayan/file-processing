package com.customer.fileprocessing.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "invalid_customers")
public class InvalidCustomer {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "input_data", nullable = false, columnDefinition = "TEXT")
    private String input;

    @Override
    public String toString() {
        return String.format("%s", input);
    }
}
