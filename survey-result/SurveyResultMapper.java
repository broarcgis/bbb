package com.klikpeta.suko.service.mapper;

import com.klikpeta.suko.domain.*;
import com.klikpeta.suko.service.dto.SurveyResultDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SurveyResult and its DTO SurveyResultDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, TenantMapper.class, })
public interface SurveyResultMapper {

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "toko.id", target = "tokoId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "journeyPlan.id", target = "journeyPlanId")
    @Mapping(source = "survey.id", target = "surveyId")
    @Mapping(target = "surveyResultDtls", ignore = true)
    SurveyResultDTO surveyResultToSurveyResultDTO(SurveyResult surveyResult);

    List<SurveyResultDTO> surveyResultsToSurveyResultDTOs(List<SurveyResult> surveyResults);

    @Mapping(source = "tenantId", target = "tenant")
    @Mapping(source = "tokoId", target = "toko")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "journeyPlanId", target = "journeyPlan")
    @Mapping(source = "surveyId", target = "survey")
    @Mapping(target = "surveyResultDtls", ignore = true)
    SurveyResult surveyResultDTOToSurveyResult(SurveyResultDTO surveyResultDTO);

    List<SurveyResult> surveyResultDTOsToSurveyResults(List<SurveyResultDTO> surveyResultDTOs);

    default Toko tokoFromId(Long id) {
        if (id == null) {
            return null;
        }
        Toko toko = new Toko();
        toko.setId(id);
        return toko;
    }

    default JourneyPlan journeyPlanFromId(Long id) {
        if (id == null) {
            return null;
        }
        JourneyPlan journeyPlan = new JourneyPlan();
        journeyPlan.setId(id);
        return journeyPlan;
    }

    default Survey surveyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Survey survey = new Survey();
        survey.setId(id);
        return survey;
    }
}
