package com.project.cinemarest.mapper;

import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TransactionMapper {

    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "ticketId", source = "clientInfo.ticketId")
    @Mapping(target = "idMovie", source = "clientInfo.idMovie")
    @Mapping(target = "userId", source = "clientInfo.userId")
    Transaction mapTransaction(ClientInfo clientInfo);
}
