package ru.crbpavel.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.crbpavel.users.dto.CredentialsDto;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class UserServiceTest {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository, passwordEncoder, userMapper);
    }

    @Test
    public void signUpTwice_ThrowException() {
        CredentialsDto credentialsDto = new CredentialsDto("test", "test".toCharArray());
        var user = new ApiUser(1, "test", passwordEncoder.encode(CharBuffer.wrap(credentialsDto.getPassword())));

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByLogin(credentialsDto.getLogin())).thenReturn(Optional.of(user));

        assertThrows(UserException.class, () -> userService.signUp(credentialsDto));
    }

    @Test
    public void signInWithNewUser_ThrowException() {
        CredentialsDto credentialsDto = new CredentialsDto("test", "test".toCharArray());
        when(userRepository.findByLogin(credentialsDto.getLogin())).thenReturn(Optional.empty());
        assertThrows(UserException.class, () -> userService.signIn(credentialsDto));
    }

    @Test
    public void signInWithInvalidPassword_ThrowException() {
        CredentialsDto credentialsDto = new CredentialsDto("test", "test".toCharArray());
        CredentialsDto credentialsDto2 = new CredentialsDto("test", "tess".toCharArray());
        var user = new ApiUser(1, "test", passwordEncoder.encode(CharBuffer.wrap(credentialsDto.getPassword())));

        when(userRepository.findByLogin(credentialsDto.getLogin())).thenReturn(Optional.of(user));
        assertThrows(UserException.class, () -> userService.signIn(credentialsDto2));
    }
}