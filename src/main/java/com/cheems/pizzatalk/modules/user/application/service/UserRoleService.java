package com.cheems.pizzatalk.modules.user.application.service;

import com.cheems.pizzatalk.modules.role.application.port.in.share.QueryRoleUseCase;
import com.cheems.pizzatalk.modules.role.domain.Role;
import com.cheems.pizzatalk.modules.user.application.port.in.share.UserRoleUseCase;
import com.cheems.pizzatalk.modules.user.application.port.out.UserPort;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRoleService implements UserRoleUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserRoleService.class);

    private final QueryRoleUseCase queryRoleUseCase;

    private final UserPort userPort;

    public UserRoleService(QueryRoleUseCase queryRoleUseCase, UserPort userPort) {
        this.queryRoleUseCase = queryRoleUseCase;
        this.userPort = userPort;
    }

    @Override
    public void saveRoleToUser(Long userId, Set<String> roleNames) {
        log.debug("Saving role: {} to user id: {}", roleNames, userId);
        List<Role> roles = queryRoleUseCase.findListByListName(new ArrayList<>(roleNames));

        Set<Long> requestSaveRoleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
        Set<Long> existsRoleIds = queryRoleUseCase.findListByUserId(userId).stream().map(Role::getId).collect(Collectors.toSet());

        Set<Long> inSaveDemandRoleIds = new HashSet<>(requestSaveRoleIds);
        inSaveDemandRoleIds.removeAll(existsRoleIds);

        Set<Long> inRemoveDemandRoleIds = new HashSet<>(existsRoleIds);
        inRemoveDemandRoleIds.removeAll(requestSaveRoleIds);

        userPort.removeRole(userId, inRemoveDemandRoleIds);
        userPort.saveRole(userId, inSaveDemandRoleIds);

        log.debug("Saved role: {} to user id: {}", roleNames, userId);
    }

    @Override
    public void removeAllRoleOfUser(Long userId) {
        log.debug("Removing all role of user id: {}", userId);
        userPort.removeAllRoleOfUser(userId);
        log.debug("Removed all role of user id: {}", userId);
    }
}
