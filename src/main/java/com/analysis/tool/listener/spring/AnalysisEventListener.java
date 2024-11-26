package com.analysis.tool.listener.spring;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.analysis.tool.common.constant.CommonConstant;
import com.analysis.tool.common.constant.TaskConstant;
import com.analysis.tool.common.event.AnalysisEvent;
import com.analysis.tool.entity.dto.StepDTO;
import com.analysis.tool.plugin.AbstractPlugin;
import com.analysis.tool.util.ThreadIdGeneratorUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author DingYulong
 * @Date 2024/11/14 15:24
 */
@Component
public class AnalysisEventListener implements ApplicationListener<AnalysisEvent> {


    private static final Logger log = LoggerFactory.getLogger(AnalysisEventListener.class);
    @Value("${analysis.tool.param}")
    private String param;
    @Value("${analysis.tool.step}")
    private String step;
    @Value("${analysis.tool.script.path}")
    private String scriptPath;
    @Value("${analysis.tool.result}")
    private String result;

    @Resource
    private ApplicationContext context;

    private static final String NAME = "name";
    private static final String VALUE = "value";
    private static final String URL = "url";
    private static final String PATH = "path";
    private static final String TOKEN = "token";


    @Override
    public void onApplicationEvent(@NonNull AnalysisEvent event) {
        log.info("task {} start execute", ThreadIdGeneratorUtil.getThreadId());
        //读取执行规则
        try {
            JSONArray jsonArray = JSONArray.parseArray(step);
            Map<String, Object> runtimeParam = new HashMap<>();
            for (String s : param.split(CommonConstant.SEPARATOR)) {
                runtimeParam.put(s, event.getParam().getOrDefault(s, CommonConstant.EMPTY_STR));
            }
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                StepDTO stepDTO = JSONObject.parseObject(jsonObject.toJSONString(), StepDTO.class);
                String type = jsonObject.getString(TaskConstant.TYPE);
                Map<String, Object> nodeParam = stepDTO.getNodeParam();
                List<Map<String, Object>> taskParam = stepDTO.getTaskParam();
                if (TaskConstant.BEAN_TYPE.equals(type)) {
                    //执行插件
                    executeBean(runtimeParam, nodeParam, taskParam);
                } else if (TaskConstant.SCRIPT_TYPE.equals(type)) {
                    //执行脚本
                    executeScript(runtimeParam, nodeParam, taskParam, stepDTO);
                } else {
                    throw new RuntimeException("不支持的类型");
                }
            }
            //获取结果
            Map<String, Object> resultMap = new HashMap<>();
            for (String s : result.split(CommonConstant.SEPARATOR)) {
                resultMap.put(s, runtimeParam.get(s));
            }
            log.info("task execute over, result is :{}", resultMap);
        } catch (Exception e) {
            log.error("Error executing analysis event: {},taskId is {}", e.getMessage(), ThreadIdGeneratorUtil.getThreadId());
        }
    }


    /**
     * 执行插件
     *
     * @param runtimeParam 运行时参数
     * @param nodeParam    节点参数
     * @param taskParam    任务参数
     */
    private void executeBean(Map<String, Object> runtimeParam, Map<String, Object> nodeParam, List<Map<String, Object>> taskParam) {
        String pluginName = String.valueOf(nodeParam.get(NAME));
        AbstractPlugin abstractPlugin = (AbstractPlugin) context.getBean(pluginName);
        Map<String, Object> taskParamMap = new HashMap<>();
        taskParam.forEach(node -> {
            String name = String.valueOf(node.get(NAME));
            String value = String.valueOf(node.get(VALUE));
            taskParamMap.put(name, runtimeParam.get(value));
        });
        Map<String, Object> execute = abstractPlugin.execute(taskParamMap);
        //TODO 配置输出参数
        runtimeParam.putAll(execute);
    }


    /**
     * 执行脚本
     *
     * @param runtimeParam 运行时参数
     * @param nodeParam    节点参数
     * @param taskParam    任务参数
     * @param stepDTO      步骤对象
     */
    private void executeScript(Map<String, Object> runtimeParam, Map<String, Object> nodeParam, List<Map<String, Object>> taskParam, StepDTO stepDTO) {
        String url = String.valueOf(nodeParam.get(URL));
        String path = String.valueOf(nodeParam.get(PATH));
        String token = String.valueOf(nodeParam.get(TOKEN));
        url = String.format(url, token);
        //下载脚本
        String downloadCommand = "git clone " + url + " " + scriptPath;
        executeShellCommand(downloadCommand);
        //将 taskParam 的value先转为list再将所有元素用 分割后得到字符串
        String param = taskParam.stream().map(node -> String.valueOf(runtimeParam.get(String.valueOf(node.get(VALUE))))).collect(Collectors.joining(CommonConstant.SPACE_STR));
        //执行脚本
        String executeCommand = "sh " + scriptPath + path + " " + param;
        List<String> results = executeShellCommand(executeCommand);
        //将结果存储到outputParam
        Map<String, Object> outputParam = stepDTO.getOutputParam();
        outputParam.forEach((k, v) -> runtimeParam.put(k, results.get(Integer.parseInt(String.valueOf(v)))));
        //删除脚本
        String deleteCommand = "rm -rf " + scriptPath;
        executeShellCommand(deleteCommand);
    }


    /**
     * 执行 Shell 命令并返回控制台输出的每一行。
     *
     * @param command 命令字符串
     * @return 控制台输出的每一行作为一个列表元素
     */
    public static List<String> executeShellCommand(String command) {
        List<String> outputLines = new ArrayList<>();
        try {
            // 使用 Runtime.exec() 执行命令
            Process process = Runtime.getRuntime().exec(command);
            // 获取命令的输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            // 读取命令的输出
            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
            }
            // 等待命令执行完成
            process.waitFor();
            // 关闭输入流
            reader.close();
            errorReader.close();
        } catch (IOException | InterruptedException e) {
            log.error("Error executing shell command: {}，taskId is {}", e.getMessage(), ThreadIdGeneratorUtil.getThreadId());
            throw new RuntimeException("Error executing shell command");
        }
        return outputLines;
    }

}
