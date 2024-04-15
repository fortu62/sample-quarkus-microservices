package com.example.ms.services.department.controller;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.ms.services.department.model.Department;
import com.example.ms.services.department.repository.DepartmentRepository;

import com.example.ms.services.department.client.EmployeeClient;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;

@Path("/departments")
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @Inject
    DepartmentRepository repository;
    @Inject
    @RestClient
    EmployeeClient employeeClient;

    @Path("/")
    @POST
    public Department add(@Valid Department department) {
        LOGGER.info("Department add: {}", department);
        // TODO: add department to repository
        return repository.add(department);
    }

    @Path("/{id}")
    @GET
    public Department findById(@PathParam("id") Long id) {
        LOGGER.info("Department find: id={}", id);
        return repository.findById(id);
    }

    @GET
    public Set<Department> findAll() {
        LOGGER.info("Department find");
        return repository.findAll();
    }

    @Path("/organization/{organizationId}")
    @GET
    public Set<Department> findByOrganization(@PathParam("organizationId") Long organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        return repository.findByOrganization(organizationId);
    }

    @Path("/organization/{organizationId}/with-employees")
    @GET
    public Set<Department> findByOrganizationWithEmployees(@PathParam("organizationId") Long organizationId) {
        LOGGER.info("Department find: organizationId={}", organizationId);
        Set<Department> departments = repository.findByOrganization(organizationId);
        departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(d.getId())));
        return departments;
    }

}
