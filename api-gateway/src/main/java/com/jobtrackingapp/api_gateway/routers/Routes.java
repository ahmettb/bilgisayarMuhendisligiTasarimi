package com.jobtrackingapp.api_gateway.routers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes {


    List<String> marketingUrl = new ArrayList<>();


    List<String> softwareUrl = new ArrayList<>();

    List<String> humanServiceUrl = new ArrayList<>();


    public Map<String, List<String>> roleEndpoints = new HashMap<>();

    public Routes() {
        marketingUrl.add("/api/campaign/update");
        marketingUrl.add("/api/campaign/update-status");
        marketingUrl.add("/api/campaign/save");
        marketingUrl.add("api/campaign/get");
        marketingUrl.add("api/campaign/get-all");
        marketingUrl.add("api/campaign/delete");
        marketingUrl.add("/api/campaign/add-permission");


        softwareUrl.add("/api/task/update");
        softwareUrl.add("/api/task/update-status");
        softwareUrl.add("/api/task/create");
        softwareUrl.add("/api/task/get");
        softwareUrl.add("/api/task/delete");
        softwareUrl.add("/api/task/get-by-software-id");
        softwareUrl.add("/api/task/add-permission");
        softwareUrl.add("/api/task/get-permission");

        humanServiceUrl.add("/api/user/save");
        humanServiceUrl.add("/api/user/update");
        humanServiceUrl.add("/api/user/accept-permission");
        humanServiceUrl.add("/api/user/get-permission");
        humanServiceUrl.add("/api/user/create-permission");
        humanServiceUrl.add("/api/user/delete");
        humanServiceUrl.add("/api/user/get");
        humanServiceUrl.add("/api/user/get-user-entity");
        humanServiceUrl.add("/api/user/role");


        //humanServiceUrl.add("/api/report");
        //  softwareUrl.add("/api/user-management");
        roleEndpoints.put("MARKETING", marketingUrl);
        roleEndpoints.put("SOFTWARE", softwareUrl);
        roleEndpoints.put("HUMAN_RESOURCE", humanServiceUrl);
    }

    public Map<String, List<String>> getRoleEndpoints() {
        return roleEndpoints;

    }
}