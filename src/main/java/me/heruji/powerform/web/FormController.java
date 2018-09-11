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

    @PostMapping
    public Form createForm(@RequestBody List<FormElement> formElements) {
        return formService.createForm(formElements);
    }

    @GetMapping("/{formId}/element")
    public List<FormElement> getFormElements(@PathVariable String formId) {
        return formService.getFormElements(formId);
    }

    @PostMapping("/{formId}/result")
    public void createFormResult(@PathVariable String formId, @RequestBody Set<ElementResult> elementResults) {
        formService.createFormResult(formId, elementResults);
    }

    @GetMapping("/{formId}/result")
    public List<FormResult> getFormResults(@PathVariable String formId) {
        return formService.getFormResults(formId);
    }

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
