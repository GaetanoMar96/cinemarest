package com.project.cinemarest.mapper;


import com.project.cinemarest.entity.Hall;
import com.project.cinemarest.model.Seat;
import java.sql.SQLException;
import java.util.Optional;
import org.mapstruct.Mapper;
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
            Integer[] array = (Integer[]) hall.getAvailableSeats().getArray();
            Seat seat = new Seat();
            seat.setBaseCost(hall.getBaseCost());
            seat.setAvailableSeats(array);
            return Optional.of(seat);
        } catch (SQLException exception) {
            logger.debug("Error while mapping model");
            return Optional.empty();
        }
    }
}
