package me.heruji.powerform.service.impl;

import me.heruji.powerform.dao.FormRepository;
import me.heruji.powerform.dao.FormResultRepository;
import me.heruji.powerform.domain.*;
import me.heruji.powerform.service.FormService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FormServiceImpl implements FormService {
    private final FormRepository formRepository;
    private final FormResultRepository formResultRepository;

    @Autowired
    public FormServiceImpl(FormRepository formRepository, FormResultRepository formResultRepository) {
        this.formRepository = formRepository;
        this.formResultRepository = formResultRepository;
    }

    @Override
    public Form createForm(List<FormElement> formElements) {
        Form form = new Form();
        form.setId(UUID.randomUUID().toString());
        form.setCreateTime(new Date());
        form.setFormElements(formElements);
        formRepository.add(form);
        return form;
    }

    @Override
    public List<FormElement> getFormElements(String formId) {
        return formRepository.getFormElementsByFormId(formId);
    }

    @Override
    public void createFormResult(String formId, Set<ElementResult> elementResults) {
        FormResult formResult = new FormResult();
        formResult.setCreateTime(new Date());
        formResult.setElementResults(elementResults);
        formResultRepository.add(formId, formResult);
    }

    @Override
    public List<FormResult> getFormResults(String formId) {
        return formResultRepository.getFormResultByFormId(formId);
    }

    @Override
    public void exportXlsx(String formId, OutputStream out) throws IOException {
        // 获取表单项和表单反馈
        List<FormElement> formElements = formRepository.getFormElementsByFormId(formId);
        List<FormResult> formResults = formResultRepository.getFormResultByFormId(formId);

        // 去除表单项中的标题和分割线
        formElements = formElements.stream()
                .filter(formElement ->
                        !formElement.getType().equals("form") && !formElement.getType().equals("sep"))
                .collect(Collectors.toList());

        // 创建工作簿
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet();

        // 根据表单项创建表头
        Row header = sheet.createRow(0);
        for (int i = 0; i < formElements.size(); i++) {
            FormElement element = formElements.get(i);
            header.createCell(i).setCellValue(element.getTitle());
        }

        // 将每条反馈作为一行写入工作簿
        for (int i = 0; i < formResults.size(); i++) {
            Row row = sheet.createRow(i + 1);

            // 将一条反馈中的所有项目以map方式存储, key为elemKey, 方便之后读取
            Map<String, ElementResult> map = formResults.get(i).getElementResults().stream()
                    .collect(Collectors.toMap(ElementResult::getElemKey, elementResult -> elementResult));

            // 将一条反馈中的所有项目分别存入一行中的各个单元格
            for (int j = 0; j < formElements.size(); j++) {
                FormElement element = formElements.get(j);

                // 通过elemKey获取单元格对应的反馈项
                String elemKey = element.getElemKey();
                ElementResult elementResult = map.get(elemKey);

                // 若反馈项为文本框类型, 则将值直接写入单元格; 否则为单选或多选类型, 将选择的选项写入单元格
                if (element.getType().equals("text")) {
                    row.createCell(j).setCellValue(elementResult.getValue());
                } else {
                    // 将选项以map方式存储, 方便之后读取
                    Map<String, String> optionMap = element.getOptions().stream()
                            .collect(Collectors.toMap(Option::getId, Option::getTitle));

                    // 多个选项连接成一个字符串, 以逗号分隔
                    String value = elementResult.getOptionIds().stream()
                            .map(optionMap::get)
                            .collect(Collectors.joining(","));

                    row.createCell(j).setCellValue(value);
                }
            }
        }

        // 将工作簿写入输出流
        wb.write(out);
    }
}
