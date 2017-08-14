package com.klikpeta.suko.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A DTO for the SurveyResult entity.
 */
public class SurveyResultDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private ZonedDateTime startSurvey;

    private ZonedDateTime endSurvey;


    private Long tenantId;
    
    private Long tokoId;
    
    private Long userId;
    
    private Long journeyPlanId;
    
    private Long surveyId;

    private List<SurveyResultDtlDTO> surveyResultDtls = new ArrayList<>();

    public List<SurveyResultDtlDTO> getSurveyResultDtls() {
        return surveyResultDtls;
    }

    public void setSurveyResultDtls(List<SurveyResultDtlDTO> surveyResultDtls) {
        this.surveyResultDtls = surveyResultDtls;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getStartSurvey() {
        return startSurvey;
    }

    public void setStartSurvey(ZonedDateTime startSurvey) {
        this.startSurvey = startSurvey;
    }
    public ZonedDateTime getEndSurvey() {
        return endSurvey;
    }

    public void setEndSurvey(ZonedDateTime endSurvey) {
        this.endSurvey = endSurvey;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getTokoId() {
        return tokoId;
    }

    public void setTokoId(Long tokoId) {
        this.tokoId = tokoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJourneyPlanId() {
        return journeyPlanId;
    }

    public void setJourneyPlanId(Long journeyPlanId) {
        this.journeyPlanId = journeyPlanId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SurveyResultDTO surveyResultDTO = (SurveyResultDTO) o;

        if ( ! Objects.equals(id, surveyResultDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SurveyResultDTO{" +
            "id=" + id +
            ", startSurvey='" + startSurvey + "'" +
            ", endSurvey='" + endSurvey + "'" +
            '}';
    }
}
