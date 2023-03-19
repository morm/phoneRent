package com.morm.phone.rent.manager.controller.rest;

import com.morm.phone.rent.manager.dto.PhoneRentItem;
import com.morm.phone.rent.manager.dto.response.PhonesRentResponse;
import com.morm.phone.rent.manager.exception.PhoneNotRentedException;
import com.morm.phone.rent.manager.service.PhonesRentService;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PhonesController {

  private static final Logger logger = LoggerFactory.getLogger(PhonesController.class);
  private final PhonesRentService phonesRentService;

  public PhonesController(PhonesRentService phonesRentService) {
    this.phonesRentService = phonesRentService;
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping("/rented_phones")
  public ResponseEntity<PhonesRentResponse> getRentedPhones(Principal principal) {

    PhonesRentResponse res = new PhonesRentResponse();
    res.setPhonesRent(phonesRentService.getRentedPhones().stream()
        .map(phoneRent -> {
          PhoneRentItem item = new PhoneRentItem();
          Optional.ofNullable(phoneRent.getPhone()).ifPresent(phone -> {
            item.setPhoneId(phone.getId());
            item.setPhoneName(phone.getName());
          });

    Optional.ofNullable(phoneRent.getEmployee())
      .ifPresent(employee -> {
        item.setEmployeeId(employee.getId());
        item.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        // We allow to edit only if the phone is booked by the current user
        item.setIsEditable(principal.getName().equals(employee.getUsername()));
      });

      item.setBookedAt(phoneRent.getBookedAt());
      item.setIsAvailable(phoneRent.getIsAvailable());

      return item;
    }).collect(Collectors.toList()));

    return ResponseEntity.ok(res);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @PutMapping("/rented_phones/{phoneId}")
  public ResponseEntity<?> releasePhone(@RequestParam Boolean book, Principal principal,
      @PathVariable Integer phoneId) {

    try {
      if (Boolean.TRUE.equals(book)) {
        phonesRentService.bookPhone(principal.getName(), phoneId);
      } else {
        phonesRentService.releasePhone(principal.getName(), phoneId);
      }
    } catch (PhoneNotRentedException e) {
      logger.error("PhonesController error: tried to act on phone rented by another user", e);
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    return ResponseEntity.ok().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Exception e) {
    logger.error("PhonesController error", e);
    return ResponseEntity.internalServerError().body("Internal server error");
  }
}
