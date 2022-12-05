package com.crmservice.crmservice.domain.usecases.updatecustomerphoto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateCustomerPhotoRequest(String currentUser, String customerId, MultipartFile photo) {
}
