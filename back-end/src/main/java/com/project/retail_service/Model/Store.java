package com.project.retail_service.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NamedEntityGraph(
        name = "Store.withInventoryAndProducts",
        attributeNodes = @NamedAttributeNode(value = "inventories", subgraph = "inventory-product"),
        subgraphs = @NamedSubgraph(
                name = "inventory-product",
                attributeNodes = @NamedAttributeNode("product")
        )
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @JsonIgnore
    @OneToMany(mappedBy = "store")
    private List<Inventory> inventories;
}

