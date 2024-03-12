package com.cheems.pizzatalk.modules.userkey.adapter.database;

import com.cheems.pizzatalk.entities.mapper.UserKeyMapper;
import com.cheems.pizzatalk.modules.userkey.application.port.out.UserKeyPort;
import com.cheems.pizzatalk.modules.userkey.domain.UserKey;
import com.cheems.pizzatalk.repository.UserKeyRepository;
import org.springframework.stereotype.Component;

@Component
public class UserKeyAdapter implements UserKeyPort {

    private final UserKeyRepository userKeyRepository;

    private final UserKeyMapper userKeyMapper;

    public UserKeyAdapter(UserKeyRepository userKeyRepository, UserKeyMapper userKeyMapper) {
        this.userKeyRepository = userKeyRepository;
        this.userKeyMapper = userKeyMapper;
    }

    @Override
    public UserKey save(UserKey userKey) {
        return userKeyMapper.toDomain(userKeyRepository.save(userKeyMapper.toEntity(userKey)));
    }

    @Override
    public void deleteById(Long id) {
        userKeyRepository.deleteById(id);
    }
}
