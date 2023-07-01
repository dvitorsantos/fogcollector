package lsdi.fogcollector.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lsdi.fogcollector.Services.MqttService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
public class SubscribeController {
    MqttService mqttService = MqttService.getInstance();
    RestTemplate restTemplate = new RestTemplate();
    private String workerUrl = System.getenv("WORKER_URL");
    private String cloudCollectorUrl = System.getenv("CLOUDCOLLECTOR_URL");

    @PostMapping("/subscribe/{eventType}")
    public void subscribe(@PathVariable String eventType) {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("INFO: Subscribed to cdpo/FOG/event/" + eventType);
        mqttService.subscribe("cdpo/FOG/event/" + eventType, (t, message) -> {
            new Thread(() -> {
                try {
                    Map<String, Object> event = mapper.readValue(message.getPayload(), Map.class);
                    restTemplate.postForObject(workerUrl + "/event/" + eventType, event, Map.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }
}
