package com.klikpeta.suko.repository;

import com.klikpeta.suko.domain.UserGroup;
import java.math.BigInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@SuppressWarnings("unused")
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {
    
    @Query(value = "select id from user_group where tenant_id=(?1) and id=(?2)", nativeQuery = true)
    List<BigInteger> getIdUserGroupsFromTenantAndLogin(Long id, Long groupId);
    
    @Modifying
    @Query(value = "update UserGroup u set u.tenant.id=null where u.id=(?1)")
    void deleteGroupFromTenantAndDelete(List<Long> id);

	void deleteByIdAndTenantId(Long id, Long tenantId);

	UserGroup findOneByIdAndTenantId(Long id, Long tenantId);

	Page<UserGroup> findAllByTenantId(Long tenantId, Pageable pageable);

}
