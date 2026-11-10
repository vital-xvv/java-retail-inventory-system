package com.project.retail_service.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name cannot be null")
    private String name;
    @Email(message = "Invalid email format")
    @NotBlank
    private String email;
    @NotNull(message = "Phone cannot be blank")
    private String phone;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<OrderDetails> orders;
}

