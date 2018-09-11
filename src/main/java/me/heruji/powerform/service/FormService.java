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
    Form createForm(List<FormElement> formElements);

    List<FormElement> getFormElements(String formId);

    void createFormResult(String formId, Set<ElementResult> elementResults);

    List<FormResult> getFormResults(String formId);

    void exportXlsx(String formId, OutputStream out) throws IOException;
}
