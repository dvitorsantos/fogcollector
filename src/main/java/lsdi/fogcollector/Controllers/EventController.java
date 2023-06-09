package lsdi.fogcollector.Controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class EventController {
    RestTemplate restTemplate = new RestTemplate();
    private final String workerUrl = System.getenv("WORKER_URL");

    @PostMapping("/event/{eventType}")
    public void sendEventToCloud(@RequestBody Map<String, Object> event, @PathVariable String eventType) {
        restTemplate.postForObject(workerUrl + "/event/" + eventType, event, Map.class);
    }
}
