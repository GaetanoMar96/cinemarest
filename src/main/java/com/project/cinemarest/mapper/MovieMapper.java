package com.project.cinemarest.mapper;


import com.project.cinemarest.entity.Hall;
import com.project.cinemarest.model.Seat;
import java.sql.SQLException;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants.ComponentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MovieMapper {

    Logger logger = LoggerFactory.getLogger(MovieMapper.class);

    default Optional<Seat> mapSeat(Hall hall) {
        try {
            String[] availableSeats = (String[]) hall.getAvailableSeats().getArray();
            Seat seat = map(hall, availableSeats);
            return Optional.of(seat);
        } catch (SQLException exception) {
            logger.debug("Error while mapping model");
            return Optional.empty();
        }
    }

    @Mapping(target = "hallName", source = "hall.hallName")
    @Mapping(target = "baseCost", source = "hall.baseCost")
    @Mapping(target = "availableSeats", source = "availableSeats")
    Seat map(Hall hall, String[] availableSeats);
}
