@Query(value = "select ao.* from answer_option ao \n"
            + "left join question q on ao.question_id=q.id\n"
            + "left join question_set qs on q.question_set_id=qs.id\n"
            + "left join survey s on qs.id=s.question_set_id\n"
            + "left join tenant t on ao.tenant_id =t.id\n"
            + "where s.id=(?1) and ao.id=(?2) and ao.tenant_id=(?3)", nativeQuery = true)
    AnswerOption findOneByIdAndTenantId(Long surveyId,Long answerId,Long tenantId);