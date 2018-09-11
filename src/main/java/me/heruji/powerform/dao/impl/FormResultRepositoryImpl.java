package me.heruji.powerform.dao.impl;

import me.heruji.powerform.dao.FormResultRepository;
import me.heruji.powerform.domain.ElementResult;
import me.heruji.powerform.domain.FormResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class FormResultRepositoryImpl implements FormResultRepository {
    private static final String INSERT_FORM_RESULT = "INSERT INTO form_result VALUES (NULL, ?, ?)";
    private static final String INSERT_ELEM_RESULT = "INSERT INTO element_result VALUES (NULL, ?, ?, ?, ?)";
    private static final String SELECT_KEY = "SELECT LAST_INSERT_ID()";
    private static final String SELECT_FORM_RESULTS_BY_FORM_ID =
            "SELECT * FROM form_result WHERE form_id = ? ORDER BY create_time DESC";
    private static final String SELECT_ELEM_RESULTS_BY_FORM_RESULT_ID =
            "SELECT elem_key, group_concat(`value`) AS `value`, group_concat(option_id) AS `option_ids` " +
                    "FROM element_result WHERE form_result_id = ? GROUP BY elem_key";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FormResultRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(String formId, FormResult formResult) {
        jdbcTemplate.update(INSERT_FORM_RESULT, formResult.getCreateTime(), formId);
        Long formResultId = jdbcTemplate.queryForObject(SELECT_KEY, Long.class);
        Set<ElementResult> elementResults = formResult.getElementResults();
        for (ElementResult elementResult : elementResults) {
            Set<String> optionIds = elementResult.getOptionIds();
            if (optionIds == null) {
                jdbcTemplate.update(
                        INSERT_ELEM_RESULT,
                        formResultId,
                        elementResult.getElemKey(),
                        elementResult.getValue(),
                        null
                );
            } else {
                for (String optionId : optionIds) {
                    jdbcTemplate.update(
                            INSERT_ELEM_RESULT,
                            formResultId,
                            elementResult.getElemKey(),
                            null,
                            optionId
                    );
                }
            }
        }
    }

    @Override
    public List<FormResult> getFormResultByFormId(String formId) {
        List<FormResult> formResults = jdbcTemplate.query(SELECT_FORM_RESULTS_BY_FORM_ID, (rs, rowNum) -> {
            FormResult formResult = new FormResult();
            formResult.setId(rs.getLong("id"));
            formResult.setCreateTime(rs.getTimestamp("create_time"));
            return formResult;
        }, formId);

        for (FormResult formResult : formResults) {
            List<ElementResult> elementResults = jdbcTemplate.query(
                    SELECT_ELEM_RESULTS_BY_FORM_RESULT_ID,
                    (rs, rowNum) -> {
                        ElementResult elementResult = new ElementResult();
                        elementResult.setElemKey(rs.getString("elem_key"));
                        elementResult.setValue(rs.getString("value"));
                        String optionIdsString = rs.getString("option_ids");
                        if (optionIdsString != null) {
                            Set<String> optionIds = new HashSet<>();
                            Collections.addAll(optionIds, optionIdsString.split(","));
                            elementResult.setOptionIds(optionIds);
                        }
                        return elementResult;
                    },
                    formResult.getId()
            );
            formResult.setElementResults(new HashSet<>(elementResults));
        }

        return formResults;
    }
}
