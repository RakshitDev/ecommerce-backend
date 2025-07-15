package com.ecom.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank(message = "ProductName cannot be blank")
    @Size(min = 4,message = "Name should be Atleast 4 chracter")
    private String productName;
    @Size(min = 5,max = 60,message = "Description should be least with 5 character")
    private String description;
    private String image;
    private double price;
    private double discount;
    private String specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
