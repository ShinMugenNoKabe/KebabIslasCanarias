package es.rufino.kebab.services;

import es.rufino.kebab.models.Privilege;
import es.rufino.kebab.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    public Privilege findByName(String name) {
        return privilegeRepository.findByName(name);
    }

    public Privilege insert(Privilege newPrivilege) {
        return privilegeRepository.save(newPrivilege);
    }

}
