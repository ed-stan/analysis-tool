package com.analysis.tool.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author DingYulong
 * @Date 2024/11/21 21:20
 */
@Data
public class StepDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    private String type;

    private Map<String,Object> nodeParam;

    private List<Map<String,Object>> taskParam;

    private Map<String,Object>  outputParam;
}
