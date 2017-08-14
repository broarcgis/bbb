package com.klikpeta.suko.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.klikpeta.suko.repository.UserRepository;
import com.klikpeta.suko.service.dto.UserDTO;
import com.klikpeta.suko.service.dto.UserGroupUserDTO;
import com.klikpeta.suko.service.exception.UserGroupNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.klikpeta.suko.security.annotation.MustHaveTenant;
import com.klikpeta.suko.service.UserGroupService;
import com.klikpeta.suko.service.dto.UserGroupDTO;
import com.klikpeta.suko.web.rest.util.HeaderUtil;
import com.klikpeta.suko.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;

import static org.springframework.http.ResponseEntity.notFound;

/**
 * REST controller for managing UserGroup.
 */
@RestController
@RequestMapping("/api")
@MustHaveTenant
public class UserGroupResource {

    private final Logger log = LoggerFactory.getLogger(UserGroupResource.class);

    @Inject
    private UserGroupService userGroupService;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /user-groups : Create a new userGroup.
     *
     * @param userGroupDTO the userGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userGroupDTO, or with status 400 (Bad Request) if the userGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-groups")
    @Timed
    public ResponseEntity<UserGroupDTO> createUserGroup(@RequestBody UserGroupDTO userGroupDTO) throws URISyntaxException {
        log.debug("REST request to save UserGroup : {}", userGroupDTO);
        if (userGroupDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userGroup", "idexists", "A new userGroup cannot already have an ID")).body(null);
        }
        UserGroupDTO result = userGroupService.save(userGroupDTO);
        return ResponseEntity.created(new URI("/api/user-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userGroup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-groups : Updates an existing userGroup.
     *
     * @param userGroupDTO the userGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userGroupDTO,
     * or with status 400 (Bad Request) if the userGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the userGroupDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-groups")
    @Timed
    public ResponseEntity<UserGroupDTO> updateUserGroup(@RequestBody UserGroupDTO userGroupDTO) throws URISyntaxException {
        log.debug("REST request to update UserGroup : {}", userGroupDTO);
        if (userGroupDTO.getId() == null) {
            return createUserGroup(userGroupDTO);
        }
        UserGroupDTO result = userGroupService.save(userGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userGroup", userGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-groups : get all the userGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of userGroups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/user-groups")
    @Timed
    public ResponseEntity<List<UserGroupDTO>> getAllUserGroups(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of UserGroups");
        Page<UserGroupDTO> page = userGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/user-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /user-groups/:id : get the "id" userGroup.
     *
     * @param id the id of the userGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/user-groups/{id}")
    @Timed
    public ResponseEntity<UserGroupDTO> getUserGroup(@PathVariable Long id) {
        log.debug("REST request to get UserGroup : {}", id);
        UserGroupDTO userGroupDTO = userGroupService.findOne(id);
        return Optional.ofNullable(userGroupDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /user-groups/:id : delete the "id" userGroup.
     *
     * @param id the id of the userGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long id) {
        log.debug("REST request to delete UserGroup : {}", id);
        userGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userGroup", id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-groups?query=:query : search for the userGroup corresponding
     * to the query.
     *
     * @param query the query of the userGroup search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/user-groups")
    @Timed
    public ResponseEntity<List<UserGroupDTO>> searchUserGroups(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of UserGroups for query {}", query);
        Page<UserGroupDTO> page = userGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/user-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PutMapping("/user-groups/{id}/users")
    @Timed
    public ResponseEntity<Void> addUsersToAGroup(@PathVariable Long id, @RequestBody UserGroupUserDTO userGroupUserDTO) {
        log.debug("REST request to add users {} to a UserGroup : {}", userGroupUserDTO.getUsers(), id);
        UserGroupDTO one = userGroupService.findOne(id);
        if(one == null) {
            return notFound().build();
        }
        userGroupService.addUsersToGroup(id, userGroupUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userGroup", id.toString()))
            .body(null);
    }

    @DeleteMapping("/user-groups/{id}/users/{login}")
    @Timed
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable Long id, @PathVariable String login) {
        log.debug("REST request to remove user {} from UserGroup {}", login, id);
        UserGroupDTO one = userGroupService.findOne(id);
        if(one == null) {
            return notFound().build();
        }
        userGroupService.removeUserFromGroup(id,login);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityDeletionAlert("userGroup",id.toString() + "," + login))
            .body(null);
    }

    @GetMapping("/user-groups/{id}/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUserInUserGroup(@PathVariable Long id, @ApiParam Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all user in UserGroup {}", id);
        try {
            Page<UserDTO> allUserInUserGroup = userGroupService.findAllUserInUserGroup(id, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(allUserInUserGroup, "/api/user-groups/" + id.toString() + "/users");
            return new ResponseEntity<>(allUserInUserGroup.getContent(), headers, HttpStatus.OK);
        } catch (UserGroupNotExistException e) {
            return new ResponseEntity<List<UserDTO>>(HttpStatus.NOT_FOUND);
        }
    }
}
