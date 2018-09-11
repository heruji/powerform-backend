package me.heruji.powerform.dao;

import me.heruji.powerform.domain.FormResult;

import java.util.List;

public interface FormResultRepository {
    void add(String formId, FormResult formResult);

    List<FormResult> getFormResultByFormId(String formId);
}
