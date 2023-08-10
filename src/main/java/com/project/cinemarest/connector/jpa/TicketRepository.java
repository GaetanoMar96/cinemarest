package com.project.cinemarest.connector.jpa;


import com.project.cinemarest.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Modifying
    @Query("delete from Ticket t where t.ticketId = :ticketId")
    int deleteTicket(Long ticketId);
}
