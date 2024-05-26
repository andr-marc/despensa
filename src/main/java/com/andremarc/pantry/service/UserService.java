package com.andremarc.pantry.service;

import com.andremarc.pantry.DTO.UserDTO;
import com.andremarc.pantry.entity.User;
import com.andremarc.pantry.model.ApiResponse;
import com.andremarc.pantry.model.PaginatedData;
import com.andremarc.pantry.model.Pagination;
import com.andremarc.pantry.repository.UserRepository;
import com.andremarc.pantry.security.jwt.JwtResponse;
import com.andremarc.pantry.security.jwt.JwtUtils;
import com.andremarc.pantry.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public ApiResponse<User> saveUser(User user){
        ApiResponse<User> response = new ApiResponse<>();
        user.setAccessDuration("3600");
        User saved = repository.save(user);
        return response.of(HttpStatus.CREATED, "Usuário salvo com sucesso!", saved);
    }

    public ApiResponse<User> getUserById(UUID id){
        ApiResponse<User> userApiResponse = new ApiResponse<>();
        repository.findById(id).ifPresentOrElse(
                it ->
                        userApiResponse.of(HttpStatus.OK, "Usuário encontrado!",it),
                () ->
                        userApiResponse.of(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID informado")
        );

        return userApiResponse;
    }







    public ApiResponse<PaginatedData<User>> listUsers(Pageable pageable){
        ApiResponse<PaginatedData<User>> userApiResponse = new ApiResponse<>();

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "name")

        );

        Page<User> users = repository.findAll(pageable);
        final Pagination pagination = Pagination.from(users, pageable);

        return userApiResponse.of(HttpStatus.OK, new PaginatedData<>(users.getContent(), pagination));

    }

    public ApiResponse<User> updateUser(User user){
        ApiResponse<User> userApiResponse = new ApiResponse<>();

        repository.findById(user.getId()).ifPresentOrElse(
                it -> {
                    user.setPassword(it.getPassword());
                    userApiResponse.of(HttpStatus.OK, "Usuário atualizado com sucesso!", repository.save(user));
                },
                () ->
                        userApiResponse.of(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID informado")
        );

        return userApiResponse;
    }


    public ApiResponse<User> resetUserPassword(User user){
        ApiResponse<User> userApiResponse = new ApiResponse<>();

        repository.findById(user.getId()).ifPresentOrElse(
                it -> {
                    user.setPassword(passwordEncoder.encode(user.getUsername()));
                    userApiResponse.of(HttpStatus.OK, "Senha resetada com sucesso!", repository.save(user));
                },
                () ->
                        userApiResponse.of(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID informado")
        );

        return userApiResponse;
    }


    public ApiResponse<User> updateUserPassword(UserDTO user){
        ApiResponse<User> userApiResponse = new ApiResponse<>();

        repository.findUserByUsername(user.username).ifPresentOrElse(
                it -> {
                    if(!verificationPasswordUpdate(user, it).isEmpty()) {
                        userApiResponse.of(HttpStatus.BAD_REQUEST, verificationPasswordUpdate(user, it));
                        return;
                    }
                    it.setPassword(passwordEncoder.encode(user.newPassword));
                    userApiResponse.of(
                            HttpStatus.OK, "Senha atualizada com sucesso!", repository.save(it));
                },
                () -> userApiResponse.of(HttpStatus.NOT_FOUND, "Usuário não encontrado com o ID informado")
        );

        return userApiResponse;
    }

    public ApiResponse<JwtResponse> login(User loginRequest) {
        ApiResponse<JwtResponse> userApiResponse = new ApiResponse<>();
        Optional<User> user = repository.findUserByUsername(loginRequest.getUsername());

        if (user.isEmpty())
            return userApiResponse.of(HttpStatus.BAD_REQUEST, "Não foi possível efetuar o login");

//        if(!user.get().getActive())
//            return userApiResponse.of(HttpStatus.BAD_REQUEST, MsgSystem.errUserInactive());

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return userApiResponse.of(HttpStatus.OK, tokenResponse(authentication, user.get()));
        } catch (Exception e) {
            return userApiResponse.of(HttpStatus.BAD_REQUEST, "Não foi possível efetuar o login");
        }

    }

    public ApiResponse<User> getUserLogged() {
        ApiResponse<User> response = new ApiResponse<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList().contains("ROLE_ANONYMOUS")){
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            response.of(HttpStatus.OK, "Usuário logado encontrado com sucesso", repository.findUserByUsername(userDetails.getUsername()).get());
            return response;
        }
        return response.of(HttpStatus.NOT_FOUND, "Usuário não está logado");
    }


    public String verificationPasswordUpdate(UserDTO user, User it) {
        if (user.username.isEmpty())
            return "Usuário deve ser informado";
        if (user.previousPassword.isEmpty())
            return "Senha antiga deve ser informada";
        if (user.newPassword.isEmpty())
            return "Nova senha deve ser informada";
        if(!passwordEncoder.matches(user.previousPassword, it.getPassword()))
            return "Senha atual inválida.";
        if(passwordEncoder.matches(user.newPassword, it.getPassword()))
            return "Nova senha deve ser diferente da atual.";
        if(passwordEncoder.matches(user.newPassword, user.newPasswordConfirm))
            return "Senha de confirmação não correspondem com a nova senha";

        return isValidPassword(user.newPassword);
    }

    private JwtResponse tokenResponse(Authentication authentication, User user) {
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                user.getName(),
                user.getPermission()
        );
    }

    public static String isValidPassword(String password){
        long passwordLength = password.chars().count();
        if(passwordLength < 8 || passwordLength > 12)
            return "Senha deve possuir no mínimo 8 e no máximo 12 caracteres";
        Boolean hasNumber = false;
        Boolean hasSpecial = false;
        Boolean hasUpperCase = false;
        Boolean hasLowerCase = false;
        for(int i = 0; i < passwordLength; i++){
            char ch = password.charAt(i);
            if(Character.isDigit(ch))
                hasNumber = Character.isDigit(ch);
            if(Character.isUpperCase(ch))
                hasUpperCase = Character.isUpperCase(ch);
            if(Character.isLowerCase(ch))
                hasLowerCase = Character.isLowerCase(ch);
            if(ch == '!' || ch == '@' || ch == '#' || ch == '$' || ch == '&' ||
                    ch == '¨' || ch == '*' || ch == '(' || ch == ')' || ch == '/' ||
                    ch == '-' || ch == '_' || ch == '=' || ch == '+' || ch == '?' ||
                    ch == '{' || ch == '}' || ch == '[' || ch == ']' || ch == '`' ||
                    ch == '´' || ch == '~' || ch == '^' || ch == ';' || ch == ':' ||
                    ch == '.' || ch == ',' || ch == '|' || ch == '<' || ch == '>' ||
                    ch == 'º' || ch == 'ª' || ch == '\'' || ch == '\\' || ch == '\"'
            )
                hasSpecial = true;
        }
        if(!hasNumber)
            return "Senha digitada não possui número";
        if(!hasSpecial)
            return "Senha digitada não possui caracter especial";
        if(!hasUpperCase)
            return "Senha digitada não possui letra maiúscula";
        if(!hasLowerCase)
            return "Senha digitada não possui letra minúscula";

        return "";
    }

}
