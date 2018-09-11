package me.heruji.powerform.dao;

import me.heruji.powerform.domain.Form;
import me.heruji.powerform.domain.FormElement;

import java.util.List;

public interface FormRepository {
    void add(Form form);

    List<FormElement> getFormElementsByFormId(String formId);
}
