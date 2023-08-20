package com.project.cinemarest.mapper;

import com.project.cinemarest.entity.Ticket;
import com.project.cinemarest.model.ClientInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TicketMapper {

    @Mapping(target = "ticketId", source = "clientInfo.ticketId")
    @Mapping(target = "idMovie", source = "clientInfo.idMovie")
    @Mapping(target = "cost", ignore = true)
    Ticket mapTicket(ClientInfo clientInfo);
}
