package com.project.cinemarest.controller.cinema;

import com.project.cinemarest.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/cinema/transactions")
@RequiredArgsConstructor
public class TransactionsControllerApi {

    private final TransactionsService transactionsService;

    @PutMapping(value = "/transaction/{userId}/{price}", produces = {"application/json"})
    public ResponseEntity<Void> postTransaction(@PathVariable("userId") String userId,
        @PathVariable("price") Double price) {
       transactionsService.increaseUserWallet(userId, price);
       return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
