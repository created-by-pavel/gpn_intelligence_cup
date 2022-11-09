package ru.crbpavel.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.crbpavel.users.dto.CredentialsDto;
import ru.crbpavel.users.dto.UserDto;

import javax.annotation.PostConstruct;
import java.nio.CharBuffer;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public UserDto signIn(CredentialsDto credentialsDto) {
        var user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "User not found"));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user, createToken(user));
        }

        throw new UserException(HttpStatus.BAD_REQUEST, "Invalid password");
    }

    public UserDto validateToken(String token) {
        String login = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Optional<ApiUser> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty()) {
            throw new UserException(HttpStatus.NOT_FOUND, "User not found");
        }

        ApiUser user = userOptional.get();
        return userMapper.toUserDto(user, createToken(user));
    }

    private String createToken(ApiUser user) {
        Claims claims = Jwts.claims().setSubject(user.getLogin());

        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public UserDto signUp(CredentialsDto credentialsDto) {
        var userOptional = userRepository.findByLogin(credentialsDto.getLogin());
        if (userOptional.isPresent()) {
            throw new UserException(HttpStatus.BAD_REQUEST, "User already in database");
        }

        var user = userMapper.toApiUser(
                credentialsDto, passwordEncoder.encode(CharBuffer.wrap(credentialsDto.getPassword())));
        userRepository.save(user);

        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .token(createToken(user))
                .build();
    }
}
