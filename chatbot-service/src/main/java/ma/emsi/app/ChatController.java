package ma.emsi.app;

import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.*;
import io.modelcontextprotocol.client.McpSyncClient;
@RestController @CrossOrigin(origins="http://localhost:4200")
public class ChatController {
 private final EbankAgent agent;private final List<McpSyncClient> clients;
 public ChatController(EbankAgent a,List<McpSyncClient> c){agent=a;clients=c;}
 public record Question(@NotBlank @Pattern(regexp="[a-zA-Z0-9:_-]{1,100}") String conversationId,@NotBlank @Size(max=4000) String message) {}
 @PostMapping("/chat") public Map<String,String> chat(@Valid @RequestBody Question q){return Map.of("conversationId",q.conversationId(),"answer",agent.chat(q.conversationId(),q.message()));}
 @GetMapping("/mcp-tools") public List<String> tools(){return clients.stream().flatMap(c->c.listTools().tools().stream()).map(t->t.name()).toList();}
}
