package es.rufino.kebab.services;

import es.rufino.kebab.models.Role;
import es.rufino.kebab.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role insert(Role newRole) {
        return roleRepository.save(newRole);
    }

}
