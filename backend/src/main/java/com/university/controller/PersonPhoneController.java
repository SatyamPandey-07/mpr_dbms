package com.university.controller;

import com.university.dto.ApiResponse;
import com.university.model.PersonPhone;
import com.university.service.PersonPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phones")
@CrossOrigin(origins = "*")
public class PersonPhoneController {

    @Autowired
    private PersonPhoneService personPhoneService;

    @GetMapping("/person/{personId}")
    public ApiResponse<List<PersonPhone>> getPhonesByPersonId(@PathVariable Integer personId) {
        return ApiResponse.success(personPhoneService.getPhonesByPersonId(personId), "Fetched phones successfully");
    }

    @PostMapping
    public ApiResponse<String> addPhone(@RequestBody PersonPhone personPhone) {
        personPhoneService.addPhone(personPhone);
        return ApiResponse.success(null, "Phone added successfully");
    }

    @PutMapping("/{personId}/{oldPhoneNumber}")
    public ApiResponse<String> updatePhone(@PathVariable Integer personId, 
                                           @PathVariable String oldPhoneNumber,
                                           @RequestBody PersonPhone personPhone) {
        personPhone.setPersonId(personId);
        personPhoneService.updatePhone(personPhone, oldPhoneNumber);
        return ApiResponse.success(null, "Phone updated successfully");
    }

    @DeleteMapping("/{personId}/{phoneNumber}")
    public ApiResponse<String> deletePhone(@PathVariable Integer personId, @PathVariable String phoneNumber) {
        personPhoneService.deletePhone(personId, phoneNumber);
        return ApiResponse.success(null, "Phone deleted successfully");
    }
}
