package hr.domagoj.servicepilot.services.implementations;

import hr.domagoj.servicepilot.DTOs.UserDTO;
import hr.domagoj.servicepilot.entities.User;
import hr.domagoj.servicepilot.repos.RoleRepository;
import hr.domagoj.servicepilot.repos.UserRepository;
import hr.domagoj.servicepilot.services.interfaces.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getActive(),
                user.getRole().getName()
        );
    }
}
