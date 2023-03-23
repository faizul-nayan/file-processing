package com.customer.fileprocessing.entity;

import com.customer.fileprocessing.validator.ValidEmail;
import com.customer.fileprocessing.validator.ValidPhone;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
@Builder
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "first_name", columnDefinition = "VARCHAR(255)")
    private String firstName;
    @Column(name = "last_name", columnDefinition = "VARCHAR(255)")
    private String lastName;
    @Column(name = "city", columnDefinition = "VARCHAR(255)")
    private String city;
    @Column(name = "street", columnDefinition = "VARCHAR(255)")
    private String street;
    @Column(name = "street_code", columnDefinition = "VARCHAR(255)")
    private String streetCode;
    @ValidPhone
    private String phoneNumber;
    @ValidEmail
    private String email;
    private String ipAddress;


    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s, %s, %s", firstName, lastName, city, street, streetCode, phoneNumber, email, ipAddress);
    }
}
