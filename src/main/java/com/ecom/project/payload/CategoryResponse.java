package com.ecom.project.payload;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@NotBlank
public class CategoryResponse {
    private List<CategoryDto> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long    totalElements;
    private Integer totalPage;
    private boolean lastPage;
}
