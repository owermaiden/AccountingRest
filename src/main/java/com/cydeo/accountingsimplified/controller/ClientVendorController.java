package com.cydeo.accountingsimplified.controller;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.dto.ResponseWrapper;
import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.exception.AccountingException;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clientVendors")
public class ClientVendorController {

    private final ClientVendorService clientVendorService;
    private final InvoiceService invoiceService;

    public ClientVendorController(ClientVendorService clientVendorService, InvoiceService invoiceService) {
        this.clientVendorService = clientVendorService;
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper> getClientVendors() throws Exception {
        List<ClientVendorDto> clientVendors = clientVendorService.getAllClientVendors();
        return ResponseEntity.ok(new ResponseWrapper("Client/vendors are successfully retrieved",clientVendors, HttpStatus.OK));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper> getClientVendorById(@PathVariable("id") Long id) throws AccountingException {
        ClientVendorDto clientVendor = clientVendorService.findClientVendorById(id);
        return ResponseEntity.ok(new ResponseWrapper("Client/vendors are successfully retrieved",clientVendor, HttpStatus.OK));
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> createClientVendor(@RequestBody ClientVendorDto clientVendorDto) throws Exception {
        boolean isDuplicatedCompanyName = clientVendorService.companyNameExists(clientVendorDto);
        if (isDuplicatedCompanyName) {
            throw new AccountingException("Client/vendor with this name already exists");
        }
        ClientVendorDto clientVendor = clientVendorService.create(clientVendorDto);
        return ResponseEntity.ok(new ResponseWrapper("Client/vendor is successfully created",clientVendor, HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper> updateClientVendor(@PathVariable("id") Long id, @RequestBody ClientVendorDto clientVendorDto) throws AccountingException, ClassNotFoundException, CloneNotSupportedException {
        clientVendorDto.setId(id);
        boolean isDuplicatedCompanyName = clientVendorService.companyNameExists(clientVendorDto);
        if (isDuplicatedCompanyName) {
            throw new AccountingException("Client/vendor with this name already exists");
        }
        ClientVendorDto clientVendor = clientVendorService.update(id, clientVendorDto);
        return ResponseEntity.ok(new ResponseWrapper("Client/vendor is successfully updated",clientVendor, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable("id") Long id) throws AccountingException {
        if (invoiceService.checkIfInvoiceExist(id)){
            throw new AccountingException("Client/Vendor can not be deleted...");
        }
        clientVendorService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Client/vendor is successfully deleted",HttpStatus.OK));
    }


}
