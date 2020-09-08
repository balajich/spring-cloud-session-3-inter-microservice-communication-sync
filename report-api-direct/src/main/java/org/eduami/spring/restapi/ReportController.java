package org.eduami.spring.restapi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eduami.spring.restapi.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ReportController {
    private Log logger = LogFactory.getLog(ReportController.class);

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/report/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployeeDetails(@PathVariable int employeeId) {
        logger.info(String.format("Getting Complete Details of Employee with id %s", employeeId));
        //Get employee name from employee-api
        Employee responseEmployeeNameDetails = restTemplate.getForEntity("http://employee-api/employee/" + employeeId, Employee.class).getBody();
        //Get employee salary from payroll-api
        Employee responseEmployeePayDetails = restTemplate.getForEntity("http://payroll-api/payroll/" + employeeId, Employee.class).getBody();
        return new Employee(responseEmployeeNameDetails.getId(), responseEmployeeNameDetails.getName(), responseEmployeePayDetails.getSalary());
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
