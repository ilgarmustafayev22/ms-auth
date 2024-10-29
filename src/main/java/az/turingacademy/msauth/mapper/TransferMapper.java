package az.turingacademy.msauth.mapper;

import az.turingacademy.msauth.dao.entity.TransferEntity;
import az.turingacademy.msauth.model.dto.TransferDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TransferMapper {

    TransferEntity toEntity(TransferDto transferDto);

}
