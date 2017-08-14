package com.klikpeta.suko.service;

import com.klikpeta.suko.domain.Tenant;
import com.klikpeta.suko.domain.User;
import com.klikpeta.suko.domain.UserGroup;
import com.klikpeta.suko.repository.UserGroupRepository;
import com.klikpeta.suko.repository.UserRepository;
import com.klikpeta.suko.repository.search.UserGroupSearchRepository;
import com.klikpeta.suko.repository.search.UserSearchRepository;
import com.klikpeta.suko.security.SecurityUtils;
import com.klikpeta.suko.service.dto.UserDTO;
import com.klikpeta.suko.service.dto.UserGroupDTO;
import com.klikpeta.suko.service.dto.UserGroupUserDTO;
import com.klikpeta.suko.service.exception.UserGroupNotExistException;
import com.klikpeta.suko.service.exception.UserNotExistException;
import com.klikpeta.suko.service.mapper.UserGroupMapper;
import com.klikpeta.suko.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing UserGroup.
 */
@Service
@Transactional
public class UserGroupService {

    private final Logger log = LoggerFactory.getLogger(UserGroupService.class);

    @Inject
    private UserGroupRepository userGroupRepository;

    @Inject
    private UserGroupMapper userGroupMapper;

    @Inject
    private UserGroupSearchRepository userGroupSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private UserService userService;

    /**
     * Save a userGroup.
     *
     * @param userGroupDTO the entity to save
     * @return the persisted entity
     */
    public UserGroupDTO save(UserGroupDTO userGroupDTO) {
        log.debug("Request to save UserGroup : {}", userGroupDTO);
        UserGroup userGroup = userGroupMapper.userGroupDTOToUserGroup(userGroupDTO);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        userGroup.setTenant(new Tenant(tenantId));
        userGroup = userGroupRepository.save(userGroup);
        UserGroupDTO result = userGroupMapper.userGroupToUserGroupDTO(userGroup);
        userGroupSearchRepository.save(userGroup);
        return result;
    }

    /**
     *  Get all the userGroups.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserGroups");
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        Page<UserGroup> result = userGroupRepository.findAllByTenantId(tenantId, pageable);
        return result.map(userGroup -> userGroupMapper.userGroupToUserGroupDTO(userGroup));
    }

    /**
     *  Get one userGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public UserGroupDTO findOne(Long id) {
        log.debug("Request to get UserGroup : {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        UserGroup userGroup = userGroupRepository.findOneByIdAndTenantId(id, tenantId);
        UserGroupDTO userGroupDTO = userGroupMapper.userGroupToUserGroupDTO(userGroup);
        return userGroupDTO;
    }

    /**
     *  Delete the  userGroup by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete UserGroup : {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        userGroupRepository.deleteByIdAndTenantId(id, tenantId);
        userGroupSearchRepository.delete(id);
    }

    /**
     * Search for the userGroup corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UserGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UserGroups for query {}", query);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        query = query + " AND tenant.id:"+tenantId;
        Page<UserGroup> result = userGroupSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(userGroup -> userGroupMapper.userGroupToUserGroupDTO(userGroup));
    }

    public Set<UserDTO> findAllUserInUserGroup(Long id) {
        log.debug("Request to find all users in UserGroup {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        UserGroup one = userGroupRepository.findOneByIdAndTenantId(id,tenantId);
        if(one == null) {
            throw new UserGroupNotExistException(id);
        }
        Set<User> users = one.getUsers();
        return users.stream().map(UserDTO::new).collect(Collectors.toSet());
    }

    public UserDTO removeUserFromGroup(Long id, String userLogin) {
        log.debug("Request to remove a user {} in UserGroup {}", userLogin, id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        UserGroup one = userGroupRepository.findOneByIdAndTenantId(id,tenantId);
        if(one == null) {
            throw new UserGroupNotExistException(id);
        }
        return one.getUsers().stream().filter(user -> {
            return userLogin.toLowerCase().equals(user.getLogin());
        }).map((User user) -> {
            one.getUsers().remove(user);
            user.getUserGroups().remove(one);
            userGroupRepository.save(one);
            userGroupSearchRepository.save(one);
            return user;
        }).findFirst().map(UserDTO::new).get();
    }

    public void addUserToGroup(Long id, String login) {
        log.debug("Request to add a user {} to UserGroup {}", login, id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        UserGroup one = userGroupRepository.findOneByIdAndTenantId(id,tenantId);
        if(one == null) {
            throw new UserGroupNotExistException(id);
        }
        userRepository.findOneByLoginAndTenantId(login, tenantId).ifPresent(user -> {
            one.getUsers().add(user);
            user.getUserGroups().add(one);
            userGroupRepository.save(one);
        });
    }

    public void addUsersToGroup(Long id, UserGroupUserDTO userGroupUserDTO) {
        log.debug("Request to add users {} to UserGroup {}", userGroupUserDTO.getUsers(),id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        UserGroup userGroup = userGroupRepository.findOneByIdAndTenantId(id, tenantId);
        if(userGroup == null) {
            throw new UserGroupNotExistException(id);
        }
        Set<User> users = new HashSet<>();
        userGroupUserDTO.getUsers().forEach(login -> {
            User usr = userRepository.findOneByLoginAndTenantId(login, tenantId).map(user -> {
                users.add(user);
                user.getUserGroups().add(userGroup);
                return user;
            }).orElseThrow(() -> new UserNotExistException(login));
            userGroup.getUsers().addAll(users);
        });
        userGroupRepository.save(userGroup);
        userGroupSearchRepository.save(userGroup);
    }

    public Page<UserDTO> findAllUserInUserGroup(Long id, Pageable pageable) {
        log.debug("Request to find all users in UserGroup {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        UserGroup one = userGroupRepository.findOneByIdAndTenantId(id,tenantId);
        if(one == null) {
            throw new UserGroupNotExistException(id);
        }
        Page<User> allByUserGroupIdAndTenantId = userRepository.findAllByUserGroupsIdAndTenantId(id, tenantId, pageable);
        return allByUserGroupIdAndTenantId.map(ManagedUserVM::new);
    }
}
