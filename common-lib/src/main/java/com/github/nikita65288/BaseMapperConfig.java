package com.github.nikita65288;

import com.github.nikita65288.base_entity.BaseEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface BaseMapperConfig {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
//    void ignoreBaseFields(@MappingTarget BaseEntity entity);
}
