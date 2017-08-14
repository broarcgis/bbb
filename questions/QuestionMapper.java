package com.klikpeta.suko.service.mapper;

import com.klikpeta.suko.domain.*;
import com.klikpeta.suko.service.dto.QuestionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionMapper {

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "questionSet.id", target = "questionSetId")
    QuestionDTO questionToQuestionDTO(Question question);

    List<QuestionDTO> questionsToQuestionDTOs(List<Question> questions);

    @Mapping(source = "tenantId", target = "tenant")
    @Mapping(source = "questionSetId", target = "questionSet")
    Question questionDTOToQuestion(QuestionDTO questionDTO);

    List<Question> questionDTOsToQuestions(List<QuestionDTO> questionDTOs);

    default Tenant tenantFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }

    default QuestionSet questionSetFromId(Long id) {
        if (id == null) {
            return null;
        }
        QuestionSet questionSet = new QuestionSet();
        questionSet.setId(id);
        return questionSet;
    }
}
