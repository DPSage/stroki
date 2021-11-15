package ru.example.test.project.storki.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.example.test.project.storki.exception.AppException;
import ru.example.test.project.storki.model.user.RoleRq;
import ru.example.test.project.storki.model.user.RoleRs;
import ru.example.test.project.storki.repo.RoleRepo;
import ru.example.test.project.storki.service.api.RoleService;
import ru.example.test.project.storki.utils.AdapterMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static ru.example.test.project.storki.utils.AdapterMapper.convertToRoleRs;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    @Transactional
    public RoleRs saveRole(RoleRq roleRq) throws AppException {
        log.info("RoleServiceImpl.saveRole(), Role: {}", roleRq);

        try {
            RoleRq role = roleRepo.findByName(roleRq.getName());

            if (role != null) {
                log.error("Role with this name is exist!");
                return convertToRoleRs(role);
            }

            roleRepo.save(roleRq);

            log.info("Saved Role: {}, successful", roleRq);
            return convertToRoleRs(roleRq);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AppException(e.getMessage(), e);
        }
    }

    @Override
    public List<RoleRs> getRoles() {
        log.info("Get all Roles");
        return roleRepo.findAll()
                .stream()
                .map(AdapterMapper::convertToRoleRs)
                .collect(Collectors.toList());
    }

}
