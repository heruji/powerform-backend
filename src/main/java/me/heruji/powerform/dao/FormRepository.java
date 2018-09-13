package me.heruji.powerform.dao;

import me.heruji.powerform.domain.Form;
import me.heruji.powerform.domain.FormElement;

import java.util.List;

public interface FormRepository {
    /**
     * 新添加表单
     *
     * @param form 表单
     */
    void add(Form form);

    /**
     * 查询表单项
     *
     * @param formId 表单id
     * @return 表单项
     */
    List<FormElement> getFormElementsByFormId(String formId);
}
