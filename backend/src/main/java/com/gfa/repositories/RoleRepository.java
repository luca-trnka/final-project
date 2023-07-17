package com.gfa.repositories;

import com.gfa.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleById(Long id);

}
