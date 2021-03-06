package me.heruji.powerform.dao.impl;

import me.heruji.powerform.dao.FormRepository;
import me.heruji.powerform.domain.Form;
import me.heruji.powerform.domain.FormElement;
import me.heruji.powerform.domain.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FormRepositoryImpl implements FormRepository {
    // 项form表插入记录
    private static final String INSERT_FORM = "INSERT INTO form VALUES (?, ?)";

    // 项form_element表插入记录
    private static final String INSERT_ELEM = "INSERT INTO form_element VALUES (NULL, ?, ?, ? ,?, ?, ?, ?, ?)";

    // 向option表插入记录
    private static final String INSERT_OPTION = "INSERT INTO `option` VALUES (?, ?, ?, ?)";

    // 根据表单id查询表单项
    private static final String SELECT_ELEMS_BY_FORM_ID =
            "SELECT * FROM form_element WHERE form_id = ? ORDER BY `order`";

    // 根据表单项id查询选项
    private static final String SELECT_OPTIONS_BY_ELEM_ID =
            "SELECT * FROM `option` WHERE elem_id = ? ORDER BY `order`";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FormRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Form form) {
        // 向form表插入记录
        jdbcTemplate.update(INSERT_FORM, form.getId(), form.getCreateTime());

        // 项form_element表插入记录
        List<FormElement> formElements = form.getFormElements();
        for (int i = 0; i < formElements.size(); i++) {
            FormElement element = formElements.get(i);
            jdbcTemplate.update(
                    INSERT_ELEM,
                    form.getId(),
                    i, // order
                    element.getElemKey(),
                    element.getTitle(),
                    element.getHint(),
                    element.getType(),
                    element.getMultiLine(),
                    element.getSingleSelect()
            );
            // 若有选项, 则向option表插入记录
            List<Option> options = element.getOptions();
            if (options != null) {
                // 获取表单项的id
                Long elementId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
                for (int j = 0; j < options.size(); j++) {
                    Option option = options.get(j);
                    jdbcTemplate.update(
                            INSERT_OPTION,
                            option.getId(),
                            elementId,
                            option.getTitle(),
                            j // order
                    );
                }
            }
        }
    }

    @Override
    public List<FormElement> getFormElementsByFormId(String formId) {
        // 根据表单id查询表单项
        List<FormElement> formElements = jdbcTemplate.query(SELECT_ELEMS_BY_FORM_ID, (rs, rowNum) -> {
            FormElement formElement = new FormElement();
            formElement.setId(rs.getLong("id"));
            formElement.setElemKey(rs.getString("elem_key"));
            formElement.setHint(rs.getString("hint"));
            formElement.setMultiLine(rs.getBoolean("multi_line"));
            formElement.setSingleSelect(rs.getBoolean("single_select"));
            formElement.setTitle(rs.getString("title"));
            formElement.setType(rs.getString("type"));
            return formElement;
        }, formId);

        // 根据表单项id查询选项
        for (FormElement element : formElements) {
            List<Option> options = jdbcTemplate.query(SELECT_OPTIONS_BY_ELEM_ID, (rs, rowNum) -> {
                Option option = new Option();
                option.setId(rs.getString("id"));
                option.setTitle(rs.getString("title"));
                return option;
            }, element.getId());
            element.setOptions(options);
        }

        return formElements;
    }
}
