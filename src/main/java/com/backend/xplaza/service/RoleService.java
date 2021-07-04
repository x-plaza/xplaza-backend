package com.backend.xplaza.service;

import com.backend.xplaza.model.Role;
import com.backend.xplaza.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepo;

    public void addRole(Role role) {
        roleRepo.save(role);
    }

    public void updateRole(Role role) {
        roleRepo.save(role);
    }

    public List<Role> listRoles() {
        return roleRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public String getRoleNameByID(long id) {
        return roleRepo.getName(id);
    }

    public void deleteRole(long id) {
        roleRepo.deleteById(id);
    }

    public Role listRole(long id) {
        return roleRepo.findRoleById(id);
    }

    public String getRoleNameByUserID(long id) {
        return roleRepo.getRoleNameByUserID(id);
    }
}
