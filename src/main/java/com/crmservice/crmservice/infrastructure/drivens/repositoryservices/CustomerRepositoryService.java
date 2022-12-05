package com.crmservice.crmservice.infrastructure.drivens.repositoryservices;

import com.crmservice.crmservice.domain.entities.Customer;
import com.crmservice.crmservice.domain.interfaces.ICustomerRepositoryService;
import com.crmservice.crmservice.infrastructure.drivens.externalservices.s3.IAmazonS3Service;
import com.crmservice.crmservice.infrastructure.drivens.repositories.ICustomerRepository;
import com.crmservice.crmservice.infrastructure.drivens.repositorydtos.CustomerRepositoryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryService implements ICustomerRepositoryService {

    private final ICustomerRepository customerRepository;
    private final IAmazonS3Service amazonS3Service;

    public CustomerRepositoryService(ICustomerRepository customerRepository,
                                     IAmazonS3Service amazonS3Service) {
        this.customerRepository = customerRepository;
        this.amazonS3Service = amazonS3Service;
    }

    @Override
    public Optional<Customer> getCustomerById(String customerId) {
        return this.customerRepository.findById(customerId)
                .map(CustomerRepositoryDTO::toEntity);
    }

    @Override
    public Optional<Customer> getActiveCustomerById(String customerId) {
        return this.customerRepository.findById(customerId)
                .filter(CustomerRepositoryDTO::isNotDeleted)
                .map(CustomerRepositoryDTO::toEntity);
    }

    @Override
    public Customer saveCustomer(Customer newCustomer) {
        return this.customerRepository.save(CustomerRepositoryDTO.from(newCustomer)).toEntity();
    }

    @Override
    public List<Customer> getAllActiveCustomers() {
        return this.customerRepository.findAll()
                .stream()
                .filter(CustomerRepositoryDTO::isNotDeleted)
                .map(CustomerRepositoryDTO::toEntity)
                .toList();
    }

    @Override
    public String saveCustomerPhoto(MultipartFile photo) throws IOException {
        File photoFile = convertMultiPartToFile(photo);
        String photoFileName = generatePhotoFileName(photo);
        String photoUrl = this.amazonS3Service.uploadFileTos3bucket(photoFileName, photoFile);
        photoFile.delete();
        return photoUrl;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private String generatePhotoFileName(MultipartFile photo) {
        return Instant.now() + "-" + photo.getOriginalFilename().replace(" ", "_");
    }

}
