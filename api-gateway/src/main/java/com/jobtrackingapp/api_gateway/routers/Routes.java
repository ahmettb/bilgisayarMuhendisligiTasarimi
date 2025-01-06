package com.jobtrackingapp.api_gateway.routers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes {


    List<String> marketingUrl = new ArrayList<>();


    List<String> softwareUrl = new ArrayList<>();

    List<String> humanServiceUrl = new ArrayList<>();

    List<String> adminServiceUrl = new ArrayList<>();

    public Map<String, List<String>> roleEndpoints = new HashMap<>();

    public Routes() {
        marketingUrl.add("/api/campaign/update");
        marketingUrl.add("/api/campaign/update-status");
        marketingUrl.add("/api/campaign/save");
        marketingUrl.add("/api/campaign/get");
        marketingUrl.add("/api/campaign/get-all");
        marketingUrl.add("/api/campaign/delete");
        marketingUrl.add("/api/marketing/permission/add-permission");
        marketingUrl.add("/api/marketing/permission/get-permission");
        marketingUrl.add("/api/marketing/permission/delete-permission");

        adminServiceUrl.add("/api/campaign/update");
        adminServiceUrl.add("/api/campaign/update-status");
        adminServiceUrl.add("/api/campaign/save");
        adminServiceUrl.add("/api/campaign/get");
        adminServiceUrl.add("/api/campaign/get-all");
        adminServiceUrl.add("/api/campaign/delete");
        adminServiceUrl.add("/api/marketing/permission/add-permission");
        adminServiceUrl.add("/api/marketing/permission/get-permission");
        adminServiceUrl.add("/api/marketing/permission/delete-permission");

        softwareUrl.add("/api/task/update");
        softwareUrl.add("/api/task/update-status");
        softwareUrl.add("/api/task/create");
        softwareUrl.add("/api/task/get");
        softwareUrl.add("/api/task/delete");
        softwareUrl.add("/api/task/get-by-software-id");
        softwareUrl.add("/api/software/permission/add-permission");
        softwareUrl.add("/api/software/permission/delete-permission");

        softwareUrl.add("/api/comment/add");
        softwareUrl.add("/api/comment/delete");
        softwareUrl.add("/api/comment/update");


        softwareUrl.add("/api/software/permission/get-permission");
        softwareUrl.add("/api/task/get-task-status-analysis");
        softwareUrl.add("/api/task/get-tasks-by-user-and-date");
        softwareUrl.add("/api/task/get-task-completion-rate");
        softwareUrl.add("/api/task/get-task-by-status");

        adminServiceUrl.add("/api/comment/add");
        adminServiceUrl.add("/api/comment/delete");
        adminServiceUrl.add("/api/comment/update");



        adminServiceUrl.add("/api/task/update");
        adminServiceUrl.add("/api/task/update-status");
        adminServiceUrl.add("/api/task/create");
        adminServiceUrl.add("/api/task/get");
        adminServiceUrl.add("/api/task/delete");
        adminServiceUrl.add("/api/task/get-by-software-id");
        adminServiceUrl.add("/api/task/add-permission");
        adminServiceUrl.add("/api/task/get-permission");
        adminServiceUrl.add("/api/task/get-task-status-analysis");
        adminServiceUrl.add("/api/task/get-tasks-by-user-and-date");
        adminServiceUrl.add("/api/task/get-task-completion-rate");
        adminServiceUrl.add("/api/task/get-task-by-status");


        humanServiceUrl.add("/api/user/save");
        humanServiceUrl.add("/api/user/update");
        humanServiceUrl.add("/api/hr/permission/accept-permission");
        humanServiceUrl.add("/api/hr/permission/get-permission");
        humanServiceUrl.add("/api/hr/permission/create-permission");
        humanServiceUrl.add("/api/hr/permission/reject-permission");
        humanServiceUrl.add("/api/hr/permission/delete-permission");

        humanServiceUrl.add("/api/user/delete");
        humanServiceUrl.add("/api/user/get");
        humanServiceUrl.add("/api/user/get-user-entity");
        humanServiceUrl.add("/api/user/role");


        adminServiceUrl.add("/api/user/save");
        adminServiceUrl.add("/api/user/update");
        adminServiceUrl.add("/api/user/delete");
        adminServiceUrl.add("/api/user/get");
        adminServiceUrl.add("/api/user/get-user-entity");
        adminServiceUrl.add("/api/user/role");

        adminServiceUrl.add("/api/hr/permission/accept-permission");
        adminServiceUrl.add("/api/hr/permission/get-permission");
        adminServiceUrl.add("/api/hr/permission/create-permission");
        adminServiceUrl.add("/api/hr/permission/reject-permission");


        adminServiceUrl.add("/api/admin/task");
        adminServiceUrl.add("/api/admin/task/completion-rate");
        adminServiceUrl.add("/api/admin/task/status-change");
        adminServiceUrl.add("/api/admin/task/status-analysis");
        adminServiceUrl.add("/api/admin/task/get-by-status");
        adminServiceUrl.add("/api/admin/task/get-by-status");
        adminServiceUrl.add("/api/admin/add-user");
        adminServiceUrl.add("/api/admin/save/campaign");
        adminServiceUrl.add("/api/admin/get/campaign");
        adminServiceUrl.add("/api/admin/get-all/campaigns");

        adminServiceUrl.add("/api/admin/add/permission");
        adminServiceUrl.add("/api/admin/permission/approve");
        adminServiceUrl.add("/api/admin/permission/reject");
        adminServiceUrl.add("/api/admin/permissions/user");
        adminServiceUrl.add("/api/admin/permission/reject");
        adminServiceUrl.add("/api/admin/permissions/status");
        adminServiceUrl.add("/api/admin/get-all/permissions");


        adminServiceUrl.add("/api/admin/add-user");
        adminServiceUrl.add("/api/admin/update-user");
        adminServiceUrl.add("/api/admin/delete-user");
        adminServiceUrl.add("/api/admin/get-user");
        adminServiceUrl.add("/api/admin/get-user-entity");
        adminServiceUrl.add("/api/admin/users-by-role");




        roleEndpoints.put("MARKETING", marketingUrl);
        roleEndpoints.put("SOFTWARE", softwareUrl);
        roleEndpoints.put("HUMAN_RESOURCE", humanServiceUrl);
        roleEndpoints.put("ADMIN",adminServiceUrl);


    }

    public Map<String, List<String>> getRoleEndpoints() {
        return roleEndpoints;

    }
}