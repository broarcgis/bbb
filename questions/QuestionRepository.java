package com.klikpeta.suko.repository;

import com.klikpeta.suko.domain.Question;
import com.klikpeta.suko.domain.QuestionSet;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
public interface QuestionRepository extends JpaRepository<Question, Long> {

    void deleteByQuestionSetId(Long id);

    List<Question> findByIdAndQuestionSetId(Long id, Long qsId);

    List<Question> findByQuestionSetId(Long id);

    @Query(value = "select id from question where question_set_id=(?1) and id=(?2)", nativeQuery = true)
    List<BigInteger> getIdQuestionByIdAndQuestionSetId(Long id, Long idq);

    @Modifying
    @Query(value = "delete from Question q where q.id=(?1)")
    void deleteQuestionbyId(List<Long> id);

    void deleteByIdAndTenantId(List<Long> id, Long tenantId);

    Page<Question> findAllByQuestionSetIdAndTenantId(Long questionSetId, Long tenantId, Pageable pageable);
    
    List<Question> findByQuestionSetSurveysIdAndIdAndTenantId(Long surveyId,Long questionId,Long tenantId);
    
    Question findByQuestionSetSurveysIdAndTenantId(Long id,Long tenantId);    
    
    Question findByIdAndQuestionSetSurveysIdAndTenantId(Long questionId,Long id,Long tenantId);    
    
    Question findByIdAndQuestionSetSurveysIdAndQuestionSetSurveysTokosIdInAndTenantId(Long questionId,Long id,List<Long>tokoIds,Long tenantId);
    
    Question findByQuestionSetSurveysIdAndQuestionSetSurveysTokosIdInAndTenantId(Long id,List<Long>tokoIds,Long tenantId);
}
