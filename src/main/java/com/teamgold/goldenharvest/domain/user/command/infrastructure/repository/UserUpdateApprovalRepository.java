package com.teamgold.goldenharvest.domain.user.command.infrastructure.repository;

import com.teamgold.goldenharvest.domain.user.command.domain.RequestStatus;
import com.teamgold.goldenharvest.domain.user.command.domain.User;
import com.teamgold.goldenharvest.domain.user.command.domain.UserUpdateApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserUpdateApprovalRepository extends JpaRepository<UserUpdateApproval, Long> {
    // 관리자가 대기 중인 요청만 모아볼 때 사용
    List<UserUpdateApproval> findByStatus(RequestStatus status);

    boolean existsByUserAndStatus(User user, RequestStatus status);
}