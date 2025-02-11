package com.unibuc.bookmyspace.service;

import com.unibuc.bookmyspace.entity.Role;
import com.unibuc.bookmyspace.entity.RoleName;
import com.unibuc.bookmyspace.exception.UserRoleNotFoundException;
import com.unibuc.bookmyspace.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new UserRoleNotFoundException();
        }
    }

    public Role getRoleByName(RoleName name) {
        Optional<Role> role = roleRepository.findByRoleName(name);
        if (role.isPresent()) {
            return role.get();
        } else {
            throw new UserRoleNotFoundException();
        }
    }

    public Role create(String name) {
        Role role = new Role();

        switch (name.toLowerCase()) {
            case "user":
                role.setRoleName(RoleName.USER);
                break;
            case "admin":
                role.setRoleName(RoleName.ADMIN);
                break;
        }
        return roleRepository.save(role);
    }

    public void delete(Long id) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findById(id);
        if (role.isPresent()) {
            roleRepository.deleteById(id);
        } else {
            throw new RoleNotFoundException();
        }
    }
}