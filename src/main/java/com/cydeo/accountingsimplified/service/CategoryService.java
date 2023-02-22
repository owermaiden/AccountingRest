package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CategoryDto;
import com.cydeo.accountingsimplified.exception.AccountingException;


import java.util.List;

public interface CategoryService {

    CategoryDto findCategoryById(Long categoryId) throws AccountingException;

    List<CategoryDto> getAllCategories() throws Exception;

    CategoryDto create(CategoryDto categoryDto) throws Exception;

    CategoryDto update(Long categoryId, CategoryDto categoryDto) throws AccountingException;

    void delete(Long categoryId) throws AccountingException;
    boolean isCategoryDescriptionExist(CategoryDto categoryDto);

}
