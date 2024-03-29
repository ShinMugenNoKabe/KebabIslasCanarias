package es.rufino.kebab.services;

import es.rufino.kebab.models.Role;
import es.rufino.kebab.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role insert(Role newRole) {
        return roleRepository.save(newRole);
    }

}
