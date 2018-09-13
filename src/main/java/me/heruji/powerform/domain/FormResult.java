package me.heruji.powerform.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Set;

/**
 * 表单反馈
 */
public class FormResult {
    @JsonIgnore
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createTime;
    private Set<ElementResult> elementResults;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<ElementResult> getElementResults() {
        return elementResults;
    }

    public void setElementResults(Set<ElementResult> elementResults) {
        this.elementResults = elementResults;
    }
}
