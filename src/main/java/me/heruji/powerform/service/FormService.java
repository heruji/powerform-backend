package me.heruji.powerform.service;

import me.heruji.powerform.domain.ElementResult;
import me.heruji.powerform.domain.Form;
import me.heruji.powerform.domain.FormElement;
import me.heruji.powerform.domain.FormResult;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;

public interface FormService {
    /**
     * 创建表单
     *
     * @param formElements 表单元素
     * @return 表单
     */
    Form createForm(List<FormElement> formElements);

    /**
     * 获取表单元素
     *
     * @param formId 表单id
     * @return 表单元素
     */
    List<FormElement> getFormElements(String formId);

    /**
     * 创建表单反馈
     *
     * @param formId 表单id
     * @param elementResults 表单反馈
     */
    void createFormResult(String formId, Set<ElementResult> elementResults);

    /**
     * 获取表单反馈
     *
     * @param formId 表单id
     * @return 表单反馈
     */
    List<FormResult> getFormResults(String formId);

    /**
     * 以xlsx格式导出表单反馈
     *
     * @param formId 表单id
     * @param out 输出流
     */
    void exportXlsx(String formId, OutputStream out) throws IOException;
}
