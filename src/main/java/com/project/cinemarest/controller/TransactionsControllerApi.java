package com.project.cinemarest.controller;

import com.project.cinemarest.model.Transaction;
import com.project.cinemarest.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/cinema/transactions")
@RequiredArgsConstructor
public class TransactionsControllerApi {

    private final TransactionsService transactionsService;

    @PostMapping(value = "/transaction", produces = {"application/json"})
    public ResponseEntity<Void> postTransaction(@RequestBody Transaction transaction) {
       transactionsService.insertTransaction(transaction);
       return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //TODO increase user wallet with post call
}
