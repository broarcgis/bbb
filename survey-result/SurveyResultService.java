package com.klikpeta.suko.service;

import com.klikpeta.suko.common.Increment;
import com.klikpeta.suko.domain.*;
import com.klikpeta.suko.domain.enumeration.QuestionType;
import com.klikpeta.suko.repository.SurveyRepository;
import com.klikpeta.suko.repository.SurveyResultDtlRepository;
import com.klikpeta.suko.repository.SurveyResultRepository;
import com.klikpeta.suko.repository.TempTokoRepository;
import com.klikpeta.suko.repository.search.SurveyResultSearchRepository;
import com.klikpeta.suko.repository.search.TempTokoSearchRepository;
import com.klikpeta.suko.security.SecurityUtils;
import com.klikpeta.suko.service.dto.SurveyDTO;
import com.klikpeta.suko.service.dto.SurveyResultDTO;
import com.klikpeta.suko.service.dto.SurveyResultDtlDTO;
import com.klikpeta.suko.service.mapper.SurveyMapper;
import com.klikpeta.suko.service.mapper.SurveyResultDtlMapper;
import com.klikpeta.suko.service.mapper.SurveyResultMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing SurveyResult.
 */
@Service
@Transactional
public class SurveyResultService {

    private final Logger log = LoggerFactory.getLogger(SurveyResultService.class);

    @Inject
    private SurveyResultRepository surveyResultRepository;

    @Inject
    private TempTokoRepository tempTokoRepository;

    @Inject
    private SurveyRepository surveyRepository;

    @Inject
    private SurveyResultDtlRepository surveyResultDtlRepository;

    @Inject
    private SurveyResultMapper surveyResultMapper;

    @Inject
    private SurveyMapper surveyMapper;

    @Inject
    private SurveyResultDtlMapper surveyResultDtlMapper;

    @Inject
    private SurveyResultSearchRepository surveyResultSearchRepository;

    @Inject
    private TempTokoSearchRepository tempTokoSearchRepository;

    /**
     * Save a surveyResult.
     *
     * @param surveyResultDTO the entity to save
     * @return the persisted entity
     */
    public SurveyResultDTO save(SurveyResultDTO surveyResultDTO) {
        log.debug("Request to save SurveyResult : {}", surveyResultDTO);
        SurveyResult surveyResult = surveyResultMapper.surveyResultDTOToSurveyResult(surveyResultDTO);
        surveyResult = surveyResultRepository.save(surveyResult);
        SurveyResultDTO result = surveyResultMapper.surveyResultToSurveyResultDTO(surveyResult);
        surveyResultSearchRepository.save(surveyResult);
        return result;
    }

    /**
     * Get all the surveyResults.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SurveyResultDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SurveyResults");
        Page<SurveyResult> result = surveyResultRepository.findAll(pageable);
        return result.map(surveyResult -> surveyResultMapper.surveyResultToSurveyResultDTO(surveyResult));
    }

    /**
     * Get one surveyResult by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SurveyResultDTO findOne(Long id) {
        log.debug("Request to get SurveyResult : {}", id);
        SurveyResult surveyResult = surveyResultRepository.findOne(id);
        SurveyResultDTO surveyResultDTO = surveyResultMapper.surveyResultToSurveyResultDTO(surveyResult);
        return surveyResultDTO;
    }

    /**
     * Delete the surveyResult by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete SurveyResult : {}", id);
        surveyResultRepository.delete(id);
        surveyResultSearchRepository.delete(id);
    }

    public void deletebysurveyId(Long id,Long answerid) {
        log.debug("Request to delete SurveyResult : {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        surveyResultRepository.deleteBySurveyIdAndIdAndTenantId(id,answerid,tenantId);
        surveyResultSearchRepository.delete(id);
    }

    /**
     * Search for the surveyResult corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SurveyResultDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SurveyResults for query {}", query);
        Page<SurveyResult> result = surveyResultSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(surveyResult -> surveyResultMapper.surveyResultToSurveyResultDTO(surveyResult));
    }

    public SurveyResultDTO saveCustom(Long surveyId,SurveyResultDTO surveyResultDTO) {
        log.debug("Request to save SurveyResult : {}", surveyResultDTO);
        surveyResultDTO.setSurveyId(surveyId);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        SurveyResult surveyResult = surveyResultMapper.surveyResultDTOToSurveyResult(surveyResultDTO);
        SurveyResult sr = surveyResultMapper.surveyResultDTOToSurveyResult(surveyResultDTO);
        sr.setTenant(tenant);
        sr = surveyResultRepository.save(sr);
        surveyResultDTO.setId(sr.getId());
        Optional.ofNullable(sr.getId()).ifPresent(surveyResultDtlRepository::deleteBySurveyResultId);
        for (SurveyResultDtlDTO srDTO : surveyResultDTO.getSurveyResultDtls()) {
            SurveyResultDtl srd = surveyResultDtlMapper.surveyResultDtlDTOToSurveyResultDtl(srDTO);
            srd.setSurveyResult(sr);
            srd.setTenant(tenant);
            srd = surveyResultDtlRepository.save(srd);
            srDTO.setId(srd.getId());
        }
        Survey survey = surveyRepository.findOneByIdAndTenantIdWithQuestionEager(sr.getSurvey().getId(), tenantId);
        if(survey.getQuestionSet().getIsNewToko()) {
            copySurveyResultToTempToko(sr, surveyResultDTO.getSurveyResultDtls(), tenant);
        }
        SurveyResultDTO result = surveyResultDTO;
        surveyResultSearchRepository.save(surveyResult);
        return result;
    }

    public SurveyDTO getAllAnswersBySurvey(Long id, Long questionid, List<Long> tokoIds) {
        log.debug("Request to get QuestionSet by Survey: {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        Survey survey = surveyRepository.findOneByIdAndTenantId(id, tenantId);
        if (survey == null) {
            return null;
        }
        List<SurveyResult> sr;
        if (questionid != null && tokoIds != null) {
            sr = surveyResultRepository.findBySurveyIdAndSurveyQuestionSetIdAndTokoIdInAndTenantId(id, questionid, tokoIds, tenantId);
        } else if (questionid != null && tokoIds == null) {
            sr = surveyResultRepository.findBySurveyIdAndSurveyQuestionSetIdAndTenantId(id, questionid, tenantId);
        } else if (questionid == null && tokoIds != null) {
            sr = surveyResultRepository.findBySurveyIdAndTokoIdInAndTenantId(id, tokoIds, tenantId);
        } else {
            sr = surveyResultRepository.findBySurveyIdAndTenantId(id, tenantId);
        }
        survey.setSurveyResults(sr);

        SurveyDTO surveyDTO = sr != null ? customConvertSurveyToSurveyDTO(survey) : new SurveyDTO();
        return surveyDTO;
    }

    public Survey getAllAnswersBySurveyAndTimePeriod(Long id, ZonedDateTime from, ZonedDateTime to) {
        log.debug("Request to get QuestionSet by Survey: {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        Survey survey = surveyRepository.findOneByIdAndTenantId(id, tenantId);
        if (survey == null) {
            return null;
        }
        List<SurveyResult>  sr = surveyResultRepository.findBySurveyIdAndEndSurveyBetweenAndTenantId(id, from ,to, tenantId);
        survey.setSurveyResults(sr);
        return survey;
    }

    @Transactional(readOnly = true)
    public SurveyResultDTO findOnebysurvey(Long id,Long answerid) {
        log.debug("Request to get SurveyResult : {}", id);
        Long tenantId = Long.valueOf(SecurityUtils.getCurrentUserTenant());
        SurveyResult surveyResult = surveyResultRepository.findOneByIdAndSurveyIdAndTenantId(answerid,id,tenantId);
        SurveyResultDTO surveyResultDTO = surveyResult!=null?customConvertSurveyResultToSurveyResultDTO(surveyResult):new SurveyResultDTO();
        return surveyResultDTO;
    }

    public SurveyDTO customConvertSurveyToSurveyDTO(Survey s) {
        SurveyDTO surveyResultDTO = surveyMapper.surveyToSurveyDTO(s);
        surveyResultDTO.setSurveyResults(surveyResultsToSurveyResultDTOs(s.getSurveyResults()));
        return surveyResultDTO;
    }

    public Survey customConvertSurveyToSurveyDTO(SurveyDTO s) {
        Survey surveyResult = surveyMapper.surveyDTOToSurvey(s);
        surveyResult.setSurveyResults(surveyResultMapper.surveyResultDTOsToSurveyResults(s.getSurveyResults()));
        return surveyResult;
    }

    public SurveyResultDTO customConvertSurveyResultToSurveyResultDTO(SurveyResult s) {
        SurveyResultDTO surveyResultDTO = surveyResultMapper.surveyResultToSurveyResultDTO(s);
        surveyResultDTO.setSurveyResultDtls(surveyResultDtlMapper.surveyResultDtlsToSurveyResultDtlDTOs(s.getSurveyResultDtls()));
        return surveyResultDTO;
    }

    public SurveyResult customConvertSurveyResultToSurveyResultDTO(SurveyResultDTO s) {
        SurveyResult surveyResult = surveyResultMapper.surveyResultDTOToSurveyResult(s);
        surveyResult.setSurveyResultDtls(surveyResultDtlMapper.surveyResultDtlDTOsToSurveyResultDtls(s.getSurveyResultDtls()));
        return surveyResult;
    }

    public List<SurveyResultDTO> surveyResultsToSurveyResultDTOs(List<SurveyResult> surveyResults) {
        if (surveyResults == null) {
            return null;
        }
        List<SurveyResultDTO> list = new ArrayList<SurveyResultDTO>();
        for (SurveyResult surveyResult : surveyResults) {
            SurveyResultDTO temp = surveyResultMapper.surveyResultToSurveyResultDTO(surveyResult);
            temp.setSurveyResultDtls(surveyResultDtlMapper.surveyResultDtlsToSurveyResultDtlDTOs(surveyResult.getSurveyResultDtls()));
            list.add(temp);
        }
        return list;
    }

    /**
     *
     * @param id
     * @param from
     * @param to
     * @return
     */
	public XSSFWorkbook createXSSFWorkbookSurveyResult(Long id, ZonedDateTime from, ZonedDateTime to) {
    	log.debug("Crafting excel file from survey result");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Survey survey = getAllAnswersBySurveyAndTimePeriod(id, from, to);
        List<SurveyResult> results = survey.getSurveyResults();
        if (results != null && results.size() > 0) {
        List<SurveyResultDtl> resultDtls = results.get(0).getSurveyResultDtls();

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Survey Result");

		CreationHelper helper = wb.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();

		XSSFCellStyle centerAlign = wb.createCellStyle();
		centerAlign.setAlignment(HorizontalAlignment.CENTER);
		centerAlign.setVerticalAlignment(VerticalAlignment.CENTER);

		XSSFRow row2 = sheet.createRow(2);
		row2.createCell(1).setCellValue("Result for Survey : "+survey.getTitle());
		row2 = sheet.createRow(3);
		row2.createCell(1).setCellValue("FROM: " + dateFormat.format(Date.from(from.toInstant())) + ", TO: " + dateFormat.format(Date.from(to.toInstant())));

		int totCell = 0;
		XSSFRow row7 = sheet.createRow(7);
		row7.createCell(totCell++).setCellValue("Start Survey");
		row7.getCell(totCell - 1).setCellStyle(centerAlign);
		row7.createCell(totCell++).setCellValue("End Survey");
		row7.getCell(totCell - 1).setCellStyle(centerAlign);
		row7.createCell(totCell++).setCellValue("Surveyor Name");
		row7.getCell(totCell - 1).setCellStyle(centerAlign);
		row7.createCell(totCell++).setCellValue("Outlet Code");
		row7.getCell(totCell - 1).setCellStyle(centerAlign);
		row7.createCell(totCell++).setCellValue("Outlet Name");
		row7.getCell(totCell - 1).setCellStyle(centerAlign);
		row7.createCell(totCell++).setCellValue("Outlet Address");
		row7.getCell(totCell - 1).setCellStyle(centerAlign);

		for(SurveyResultDtl dtl : resultDtls){
			row7.createCell(totCell++).setCellValue(dtl.getQuestionString());
			row7.getCell(totCell - 1).setCellStyle(centerAlign);

		}
		int rows = 8;

			for (SurveyResult result : results) {
				if (result == null)
					break;

				XSSFRow rowValue = sheet.createRow(rows++);

				final Increment i = new Increment(0);

				rowValue.createCell(i.incrementAfter()).setCellValue(dateTimeFormat.format(Date.from(result.getStartSurvey().toInstant())));
				rowValue.createCell(i.incrementAfter()).setCellValue(dateTimeFormat.format(Date.from(result.getEndSurvey().toInstant())));
				String firstName = result.getUser().getFirstName()!=null ? result.getUser().getFirstName() : "";
				String lastName = result.getUser().getLastName()!=null ? result.getUser().getLastName() : "";
				rowValue.createCell(i.incrementAfter()).setCellValue(firstName+" "+lastName);
				rowValue.createCell(i.incrementAfter()).setCellValue(result.getToko().getCode());
				rowValue.createCell(i.incrementAfter()).setCellValue(result.getToko().getName());
				rowValue.createCell(i.incrementAfter()).setCellValue(result.getToko().getAddress());

				for(SurveyResultDtl var : result.getSurveyResultDtls()){
					if(var.getQuestionType().equals(QuestionType.TEXT) ||
							var.getQuestionType().equals(QuestionType.CHECKBOX) ||
							var.getQuestionType().equals(QuestionType.DROPDOWN) ||
							var.getQuestionType().equals(QuestionType.MULTIPLECHOICE)) {
						if (var.getAnsText() != null)
							rowValue.createCell(i.incrementAfter()).setCellValue(var.getAnsText());
						else
							i.incrementAfter();
					} else
					if(var.getQuestionType().equals(QuestionType.NUMBER)) {
						if (var.getAnsDouble() != null && var.getAnsLong() == null)
							rowValue.createCell(i.incrementAfter()).setCellValue(var.getAnsDouble());
						else if (var.getAnsDouble() == null && var.getAnsLong() != null)
							rowValue.createCell(i.incrementAfter()).setCellValue(var.getAnsLong());
						else
							i.incrementAfter();
					} else
					if(var.getQuestionType().equals(QuestionType.DATE) ||
							var.getQuestionType().equals(QuestionType.TIME)) {
						if (var.getAnsDateTime() != null)
							rowValue.createCell(i.incrementAfter()).setCellValue(dateTimeFormat.format(Date.from(var.getAnsDateTime().toInstant())));
						else
							i.incrementAfter();
					} else
					if(var.getQuestionType().equals(QuestionType.IMAGE) ||
							var.getQuestionType().equals(QuestionType.SIGNATURE)) {
					if (var.getByteArray() != null && var.getByteArray().getBytes() != null) {
						// Adds a picture to the workbook
						int pictureIdx = wb.addPicture(var.getByteArray().getBytes(), Workbook.PICTURE_TYPE_JPEG);

						// Create an anchor that is attached to the worksheet
						ClientAnchor anchor = helper.createClientAnchor();
						// set top-left corner for the image
						anchor.setCol1(i.incrementAfter());
						anchor.setRow1(rows - 1);

						// Creates a picture
						Picture pict = drawing.createPicture(anchor, pictureIdx);
						// Reset the image to the original size
						pict.resize(1.0, 1.0);
						rowValue.createCell(i.incrementAfter()).setCellValue(var.getAnsDouble());
					} else
							i.incrementAfter();
					}
				}

			}

		for (int j = 0; j < totCell; j++) {
			sheet.autoSizeColumn(j, true);
		}
		return wb;
        }
        return null;
	}

    /**
     *
     * @param surveyResult
     */
    public void copySurveyResultToTempToko(SurveyResult surveyResult, List<SurveyResultDtlDTO> surveyResultDtlDTOs, Tenant tenant) {
        log.debug("Copy SurveyResult to TempToko : {}", surveyResult);
        TempToko tempToko = new TempToko().tenant(tenant).surveyResult(surveyResult);
        for (SurveyResultDtlDTO dtl : surveyResultDtlDTOs) {
            if(dtl.getQuestionString().equals("Code")) {
                tempToko.setCode(dtl.getAnsText());
            } else if(dtl.getQuestionString().equals("Name")) {
                tempToko.setName(dtl.getAnsText());
            } else if(dtl.getQuestionString().equals("Address")) {
                tempToko.setAddress(dtl.getAnsText());
            } else if(dtl.getQuestionString().equals("Longitude")) {
                tempToko.setLongitude(dtl.getAnsDouble());
            } else if(dtl.getQuestionString().equals("Latitude")) {
                tempToko.setLatitude(dtl.getAnsDouble());
            } else if(dtl.getQuestionString().equals("Owner Name")) {
                tempToko.setOwnerName(dtl.getAnsText());
            } else if(dtl.getQuestionString().equals("Phone Number")) {
                tempToko.setPhoneNumber(dtl.getAnsText());
            }
        }
        tempTokoRepository.save(tempToko);
        tempTokoSearchRepository.save(tempToko);
    }
}
