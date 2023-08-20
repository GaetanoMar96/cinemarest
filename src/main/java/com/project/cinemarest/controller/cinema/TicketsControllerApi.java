package com.project.cinemarest.controller.cinema;

import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.service.DeleteTicketService;
import com.project.cinemarest.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/cinema/tickets")
@RequiredArgsConstructor
public class TicketsControllerApi {

    private final TicketService ticketService;

    private final DeleteTicketService deleteTicketService;

    @PostMapping(value = "/ticket", produces = {"application/json"})
    public ResponseEntity<Void> postMovieTicket(@RequestBody ClientInfo clientInfo) {
        return ticketService.postMovieTicket(clientInfo);
    }

    @DeleteMapping(value = "/ticket/remove", produces = {"application/json"})
    public ResponseEntity<Void> deleteMovieTicket(@RequestBody ClientInfo clientInfo) {
        return deleteTicketService.deleteMovieTicket(clientInfo);
    }

    //TODO includere tutti i tipi di cambi di orario e di film da fare solo in seguito
}
