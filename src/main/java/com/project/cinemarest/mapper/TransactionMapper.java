package com.project.cinemarest.mapper;

import com.project.cinemarest.entity.Payment;
import com.project.cinemarest.model.ClientInfo;
import com.project.cinemarest.entity.Transaction;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TransactionMapper {

    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "ticketId", source = "clientInfo.ticketId")
    @Mapping(target = "idMovie", source = "clientInfo.idMovie")
    @Mapping(target = "userId", source = "clientInfo.userId")
    Transaction mapTransaction(ClientInfo clientInfo, UUID transactionId);

    @Mapping(target = "userId", source = "clientInfo.userId")
    @Mapping(target = "ticketId", source = "clientInfo.ticketId")
    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "cardNumber", source = "clientInfo.cardNumber")
    @Mapping(target = "expirationDate", source = "clientInfo.expirationDate")
    @Mapping(target = "cvc", source = "clientInfo.cvc")
    @Mapping(target = "totalPrice", source = "clientInfo.totalPrice")
    @Mapping(target = "createdAt", expression = "java(getPaymentDate())")
    Payment mapPayment(ClientInfo clientInfo, UUID transactionId);

    default LocalDateTime getPaymentDate() {
        return LocalDateTime.now();
    }
}
