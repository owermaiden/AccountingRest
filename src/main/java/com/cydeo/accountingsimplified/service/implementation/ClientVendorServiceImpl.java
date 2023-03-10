package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.exception.AccountingException;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.ClientVendorRepository;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.common.CommonService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl extends CommonService implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;

    public ClientVendorServiceImpl(SecurityService securityService, MapperUtil mapperUtil, ClientVendorRepository clientVendorRepository) {
        super(securityService, mapperUtil);
        this.clientVendorRepository = clientVendorRepository;
    }

    @Override
    public ClientVendorDto findClientVendorById(Long id) throws AccountingException {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(id);
        if (clientVendor == null){
            throw new AccountingException("Client/Vendor not found");
        }
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors() {
        return clientVendorRepository
                .findAllByCompany(getCompany()).stream()
                .sorted(Comparator.comparing(ClientVendor::getClientVendorType)
                .reversed()
                .thenComparing(ClientVendor::getClientVendorName))
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors(ClientVendorType clientVendorType) {
        return getAllClientVendors()
                .stream()
                .filter(dto -> dto.getClientVendorType().equals(clientVendorType))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception {
        clientVendorDto.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(clientVendor), new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(Long clientVendorId, ClientVendorDto clientVendorDto) throws ClassNotFoundException, CloneNotSupportedException, AccountingException {
        ClientVendor savedClientVendor = clientVendorRepository.findById(clientVendorId).orElseThrow(() -> new AccountingException("Client/Vendor not found") );
        clientVendorDto.getAddress().setId(savedClientVendor.getAddress().getId());     // otherwise it creates new address instead of updating existing one
        clientVendorDto.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(updatedClientVendor), new ClientVendorDto());
    }

    @Override
    public void delete(Long clientVendorId) throws AccountingException {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(clientVendorId);
        if (clientVendor == null){
            throw new AccountingException("Client/Vendor not found");
        }
        clientVendor.setIsDeleted(true);
        clientVendor.setClientVendorName(clientVendor.getClientVendorName() + "-" + clientVendor.getId());
        clientVendorRepository.save(clientVendor);
    }

    @Override
    public boolean companyNameExists(ClientVendorDto clientVendorDto) {
        ClientVendor existingClientVendor = clientVendorRepository.findByClientVendorNameAndCompany(clientVendorDto.getClientVendorName(), getCompany());
        if (existingClientVendor == null) {
            return false;
        }
        return !existingClientVendor.getId().equals(clientVendorDto.getId());
    }

}
