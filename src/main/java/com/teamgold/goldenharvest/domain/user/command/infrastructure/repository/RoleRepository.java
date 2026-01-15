package com.teamgold.goldenharvest.domain.user.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.user.command.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
