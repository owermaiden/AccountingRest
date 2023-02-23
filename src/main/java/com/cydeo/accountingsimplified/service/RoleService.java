package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.RoleDto;
import com.cydeo.accountingsimplified.exception.AccountingException;

import java.util.List;

public interface RoleService {

    RoleDto findRoleById(Long id);
    List<RoleDto> getFilteredRolesForCurrentUser();

}
