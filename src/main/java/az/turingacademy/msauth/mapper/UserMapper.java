package az.turingacademy.msauth.mapper;

import az.turingacademy.msauth.dao.entity.UserEntity;
import az.turingacademy.msauth.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(UserEntity userEntity);

    UserEntity toEntity(UserDto userDto);

}
