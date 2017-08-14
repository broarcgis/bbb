package com.klikpeta.suko.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A SurveyResult.
 */
@Entity
@Table(name = "survey_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "surveyresult")
public class SurveyResult extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "start_survey")
    private ZonedDateTime startSurvey;

    @Column(name = "end_survey")
    private ZonedDateTime endSurvey;

    @ManyToOne
    private Tenant tenant;

    @ManyToOne
    private Toko toko;

    @ManyToOne
    private User user;

    @ManyToOne
    private JourneyPlan journeyPlan;

    @ManyToOne
    @JsonIgnore
    private Survey survey;
    
    @OneToMany(mappedBy = "surveyResult",orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<SurveyResultDtl> surveyResultDtls = new ArrayList<>();

    public List<SurveyResultDtl> getSurveyResultDtls() {
        return surveyResultDtls;
    }

    public void setSurveyResultDtls(List<SurveyResultDtl> surveyResultDtls) {
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

    public SurveyResult startSurvey(ZonedDateTime startSurvey) {
        this.startSurvey = startSurvey;
        return this;
    }

    public void setStartSurvey(ZonedDateTime startSurvey) {
        this.startSurvey = startSurvey;
    }

    public ZonedDateTime getEndSurvey() {
        return endSurvey;
    }

    public SurveyResult endSurvey(ZonedDateTime endSurvey) {
        this.endSurvey = endSurvey;
        return this;
    }

    public void setEndSurvey(ZonedDateTime endSurvey) {
        this.endSurvey = endSurvey;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public SurveyResult tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Toko getToko() {
        return toko;
    }

    public SurveyResult toko(Toko toko) {
        this.toko = toko;
        return this;
    }

    public void setToko(Toko toko) {
        this.toko = toko;
    }

    public User getUser() {
        return user;
    }

    public SurveyResult user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JourneyPlan getJourneyPlan() {
        return journeyPlan;
    }

    public SurveyResult journeyPlan(JourneyPlan journeyPlan) {
        this.journeyPlan = journeyPlan;
        return this;
    }

    public void setJourneyPlan(JourneyPlan journeyPlan) {
        this.journeyPlan = journeyPlan;
    }

    public Survey getSurvey() {
        return survey;
    }

    public SurveyResult survey(Survey survey) {
        this.survey = survey;
        return this;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SurveyResult surveyResult = (SurveyResult) o;
        if (surveyResult.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, surveyResult.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SurveyResult{" +
            "id=" + id +
            ", startSurvey='" + startSurvey + "'" +
            ", endSurvey='" + endSurvey + "'" +
            '}';
    }
}
