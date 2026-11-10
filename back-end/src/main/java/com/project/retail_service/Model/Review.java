package com.project.retail_service.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    private String id;
    @NotNull
    private Long customerId;
    @NotNull
    private Long productId;
    @NotNull
    private Long storeId;
    @NotNull
    private Integer rating;
    @NotBlank
    private String comment;
}
