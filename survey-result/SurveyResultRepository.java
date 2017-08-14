package com.klikpeta.suko.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.klikpeta.suko.domain.SurveyResult;

/**
 * Spring Data JPA repository for the SurveyResult entity.
 */
public interface SurveyResultRepository extends JpaRepository<SurveyResult,Long> {

    @Query("select surveyResult from SurveyResult surveyResult where surveyResult.user.login = ?#{principal.username}")
    List<SurveyResult> findByUserIsCurrentUser();
    
    List<SurveyResult> findBySurveyIdAndTenantId(Long surveyId,Long tenantId);
    
    List<SurveyResult> findBySurveyIdAndEndSurveyBetweenAndTenantId(Long surveyId, ZonedDateTime from, ZonedDateTime to, Long tenantId);
    
    List<SurveyResult> findBySurveyIdAndSurveyQuestionSetIdAndTenantId(Long surveyId,Long questionId,Long tenantId);
    
    List<SurveyResult> findBySurveyIdAndSurveyQuestionSetIdAndTokoIdInAndTenantId(Long surveyId,Long questionId,List<Long>tokoIds,Long tenantId);
    
    List<SurveyResult> findBySurveyIdAndTokoIdInAndTenantId(Long id,List<Long>tokoIds,Long tenantId);
    
    SurveyResult findOneByIdAndSurveyIdAndTenantId(Long id,Long surveyId,Long tenantId);
    
    void deleteBySurveyIdAndIdAndTenantId(Long id,Long answerid,Long tenantid);
}
