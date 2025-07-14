package com.ecom.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Long categoryId;
    @NotBlank(message = "Category Name Should Not be Blank")
    @Size(min = 3 ,message = "Minium 3 character is required")
    private String categoryName;
}
