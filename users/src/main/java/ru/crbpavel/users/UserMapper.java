package ru.crbpavel.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.crbpavel.users.dto.CredentialsDto;
import ru.crbpavel.users.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.login", target = "login")
    @Mapping(source = "token", target = "token")
    UserDto toUserDto(ApiUser user, String token);

    @Mapping(source = "encodedPassword", target = "password")
    ApiUser toApiUser(CredentialsDto userDto, String encodedPassword);
}
