package io.pivotal.pal.tracker.backlog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private final Map<Long, ProjectInfo> map;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
        this.map = new ConcurrentHashMap<>();
    }


    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        Long id = Long.valueOf(projectId);
        map.put(id, restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class));
        return map.get(id);
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        Long id = Long.valueOf(projectId);
        return map.get(id);
    }
}
