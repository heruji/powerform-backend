package me.heruji.powerform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElementResult {
    private String elemKey;
    private String value;
    private Set<String> optionIds;

    public String getElemKey() {
        return elemKey;
    }

    public void setElemKey(String elemKey) {
        this.elemKey = elemKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<String> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(Set<String> optionIds) {
        this.optionIds = optionIds;
    }
}
