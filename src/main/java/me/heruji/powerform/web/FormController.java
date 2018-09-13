package me.heruji.powerform.web;

import me.heruji.powerform.domain.ElementResult;
import me.heruji.powerform.domain.Form;
import me.heruji.powerform.domain.FormElement;
import me.heruji.powerform.domain.FormResult;
import me.heruji.powerform.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin({"http://demo.heruji.me", "http://localhost:3000"})
@RequestMapping("/form")
public class FormController {
    private final FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    /**
     * 创建表单
     *
     * @param formElements 表单元素
     * @return 表单
     */
    @PostMapping
    public Form createForm(@RequestBody List<FormElement> formElements) {
        return formService.createForm(formElements);
    }

    /**
     * 获取表单元素
     *
     * @param formId 表单id
     * @return 表单元素
     */
    @GetMapping("/{formId}/element")
    public List<FormElement> getFormElements(@PathVariable String formId) {
        return formService.getFormElements(formId);
    }

    /**
     * 创建表单反馈
     *
     * @param formId 表单id
     * @param elementResults 表单反馈
     */
    @PostMapping("/{formId}/result")
    public void createFormResult(@PathVariable String formId, @RequestBody Set<ElementResult> elementResults) {
        formService.createFormResult(formId, elementResults);
    }

    /**
     * 获取表单反馈
     *
     * @param formId 表单id
     * @return 表单反馈
     */
    @GetMapping("/{formId}/result")
    public List<FormResult> getFormResults(@PathVariable String formId) {
        return formService.getFormResults(formId);
    }

    /**
     * 以xlsx格式导出表单反馈
     *
     * @param formId 表单id
     */
    @GetMapping("/{formId}/result/xlsx")
    public void exportXlsx(@PathVariable String formId,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        String filename = formId + ".xlsx";
        String mimeType = request.getServletContext().getMimeType(filename);
        response.setContentType(mimeType);
        response.setHeader("content-disposition",
                "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

        ServletOutputStream out = response.getOutputStream();
        formService.exportXlsx(formId, out);
    }
}
