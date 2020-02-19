package com.test.activiti.activiti.controller;

import com.test.activiti.common.RestServiceController;
import com.test.activiti.util.ToWeb;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布流程
 */
@RestController
public class ProcessEngineController{

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    IdentityService identityService;

    @RequestMapping("test")
    public Object test(@RequestParam String id){
        System.out.println("id:"+id);
//        ProcessDefinitionEntity processDefinitionEntity =repositoryService.createProcessDefinitionQuery().deploymentId(id);

//        int i = runtimeService.getProcessInstanceEvents(id).size();
//        System.out.println("i:"+i);
        return ToWeb.buildResult().refresh();
    }

    @RequestMapping("create")
    public Object create(){
//        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().singleResult();
        StartFormData startFormData = formService.getStartFormData("testprocess:2:5004");
        List<FormProperty> formProperties = startFormData.getFormProperties();
        ProcessDefinition pd = startFormData.getProcessDefinition();
        Map map = new HashMap();
        map.put("test","testValueTask");
        formService.submitStartFormData("testprocess:2:5004",map);//启动流程，提交表单
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById("testprocess:2:5004", map);
//        System.out.println(processInstance);
//        mapform.put("list", formProperties);
//        mapform.put("pd", pd);
        return ToWeb.buildResult().refresh();
    }

    @RequestMapping("run")
    public Object run(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("userProcess");
        System.out.println("pid:"+processInstance.getId()+",activitiId:"+processInstance.getActivityId());
//        System.out.println("taskService:"+taskService.createTaskQuery().taskAssignee("张三").list().size());
        return ToWeb.buildResult().refresh();
    }

    @RequestMapping("start")
    public Object start(){
        //流程启动时设置流程比那里，指定执行人
        Map<String, Object> variables=new HashMap<String, Object>();
        variables.put("userid", "李四");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process", variables);
        System.out.println("pid:"+processInstance.getId()+",activitiId:"+processInstance.getActivityId());
//        System.out.println("taskService:"+taskService.createTaskQuery().taskAssignee("张三").list().size());
        return ToWeb.buildResult().refresh();
    }

    @RequestMapping("queryTaskForm")
    public Object queryTaskForm(){
//        System.out.println("taskService:"+taskService.createTaskQuery().taskAssignee("李四").list().size());
//        if (taskService.createTaskQuery().taskAssignee("张三").list().size() != 0){
            //查询代理人的task任务
//            Task task = taskService.createTaskQuery().taskAssignee("张三").list().get(0);
            //查询候选组的task任务
//            Task task = taskService.createTaskQuery().taskCandidateGroup("user").singleResult();
            //查询候选人的task任务
            Task task = taskService.createTaskQuery().taskCandidateUser("张三").singleResult();
        //获取variable的值
//            Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
            //修改variable表的值
//            runtimeService.setVariable(task.getExecutionId(),"test","newTestValue");
//            TaskFormData taskFormData = formService.getTaskFormData(task.getId());//根据任务ID拿取表单数据
//            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            System.out.println("test");
//        }
        return ToWeb.buildResult().refresh();
    }

    @RequestMapping("query")
    public Object query(){
        System.out.println("taskService:"+taskService.createTaskQuery().taskAssignee("李四").list().size());
        if (taskService.createTaskQuery().taskAssignee("李四").list().size() != 0){
//            TaskFormData taskFormData = formService.getTaskFormData(taskService.createTaskQuery().taskAssignee("李四").list().get(0).getId());
//            System.out.println("taskFormData:"+taskService.createTaskQuery().taskAssignee("李四").list().get(0));
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(taskService.createTaskQuery().taskAssignee("李四").list().get(0).getProcessInstanceId()).singleResult();
            System.out.println(processInstance);
        }
        return ToWeb.buildResult().refresh();
    }

    @RequestMapping("completeTask")
    public Object completeTask(){
        Task task = taskService.createTaskQuery().taskCandidateGroup("user3").list().get(0);
        /*if (task != null) {
            System.out.println(task.getId());
            Map map = new HashMap();
            map.put("bool",true);
            taskService.complete(task.getId(), map);
        }*/
        if (task != null) {
            taskService.complete(task.getId());
        }
//        taskService.complete(taskService.createTaskQuery().taskAssignee("张三").list().get(0).getId());
        System.out.println("完成任务");
        return ToWeb.buildResult().refresh();
    }

}
