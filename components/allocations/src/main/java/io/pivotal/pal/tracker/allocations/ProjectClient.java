package io.pivotal.pal.tracker.allocations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String registrationServerEndpoint;
    private final Map<Long, ProjectInfo> map;

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations= restOperations;
        this.registrationServerEndpoint = registrationServerEndpoint;
        this.map = new ConcurrentHashMap<>();
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        Long id = Long.valueOf(projectId);
        map.put(id, restOperations.getForObject(registrationServerEndpoint + "/projects/" + projectId, ProjectInfo.class));
        return map.get(id);
    }

    public ProjectInfo getProjectFromCache(long projectId) {
        Long id = Long.valueOf(projectId);
        return map.get(id);
    }
}
