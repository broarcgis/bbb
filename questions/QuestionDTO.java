package com.klikpeta.suko.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.klikpeta.suko.domain.enumeration.QuestionType;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the Question entity.
 */
public class QuestionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String questionString;

    private Boolean isMandatory;

    private Integer seq;

    private QuestionType questionType;    
    
    private Long tenantId;
    
    private Long questionSetId;
    
    private List<AnswerOptionDTO> answerOptions = new ArrayList<>();

    public List<AnswerOptionDTO> getAnswerOptionDTOs() {
        return answerOptions;
    }

    public void setAnswerOptionDTOs(List<AnswerOptionDTO> answerOptions) {
        this.answerOptions = answerOptions;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestionString() {
        return questionString;
    }

    public void setQuestionString(String questionString) {
        this.questionString = questionString;
    }
    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getQuestionSetId() {
        return questionSetId;
    }

    public void setQuestionSetId(Long questionSetId) {
        this.questionSetId = questionSetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;

        if ( ! Objects.equals(id, questionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + id +
            ", questionString='" + questionString + "'" +
            ", isMandatory='" + isMandatory + "'" +
            ", seq='" + seq + "'" +
            ", questionType='" + questionType + "'" +
            '}';
    }
}
