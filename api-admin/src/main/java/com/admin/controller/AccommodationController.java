package com.admin.controller;

import com.admin.dto.request.AccommodationReqDto;
import com.admin.dto.response.AccommodationResDto;
import com.admin.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @PostMapping("/register")
    public ResponseEntity<AccommodationResDto> registerAccommodation(@RequestBody AccommodationReqDto req){
        AccommodationResDto res = accommodationService.registerAccommodation(req);
        return ResponseEntity.ok(res);
    }
}
