package com.project.cinemarest.controller;

import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/cinema/tickets")
@RequiredArgsConstructor
public class TicketsControllerApi {

    private final TicketService ticketService;

    @PostMapping(value = "/ticket", produces = {"application/json"})
    public ResponseEntity<Void> postMovieTicket(@RequestBody ClientInfo clientInfo) {
        return ticketService.postMovieTicket(clientInfo);
    }
}
