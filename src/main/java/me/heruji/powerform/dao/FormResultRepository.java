package me.heruji.powerform.dao;

import me.heruji.powerform.domain.FormResult;

import java.util.List;

public interface FormResultRepository {
    /**
     * 新增表单反馈
     *
     * @param formId 表单id
     * @param formResult 表单反馈
     */
    void add(String formId, FormResult formResult);


    /**
     * 获取表单反馈
     *
     * @param formId 表单id
     * @return 表单反馈
     */
    List<FormResult> getFormResultByFormId(String formId);
}
