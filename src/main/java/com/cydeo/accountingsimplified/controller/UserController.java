package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.annotation.DefaultExceptionMessage;
import com.cydeo.accountingsimplified.dto.ResponseWrapper;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.exception.AccountingException;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @RolesAllowed({"ADMIN","USER"})
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    public ResponseEntity<ResponseWrapper> getUsers() throws Exception {
        List<UserDto> users = userService.getFilteredUsers();
        return ResponseEntity.ok(new ResponseWrapper("Users successfully retrieved",users, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getUserById(@PathVariable("id") Long id) throws AccountingException {
        UserDto user = userService.findUserById(id);
        return ResponseEntity.ok(new ResponseWrapper("User successfully retrieved",user, HttpStatus.OK));
    }

    @PostMapping
    @DefaultExceptionMessage(defaultMessage = "Something went wrong, try again!")
    public ResponseEntity<ResponseWrapper> create(@RequestBody UserDto userDto) throws AccountingException {

        UserDto user = userService.save(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User successfully created",user, HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> update(@PathVariable("id") Long id, @RequestBody UserDto userDto) throws AccountingException {
        userDto.setId(id);  // spring cannot set id since it is not seen in UI and we need to check if updated email is used by different user.
        UserDto user = userService.update(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("User successfully created",user, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseWrapper("User successfully deleted",HttpStatus.OK));
    }


}
