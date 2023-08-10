package com.project.cinemarest.mapper;

import com.project.cinemarest.connector.jdbc.utils.JdbcQuery;
import com.project.cinemarest.model.ClientInfo;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ClientMapper {

    default void getClientParams(JdbcQuery queryBuilder, Long ticketId, ClientInfo clientInfo) {
        queryBuilder.setParameters(UUID.randomUUID(), clientInfo.getIdMovie(), ticketId);
    }
}
