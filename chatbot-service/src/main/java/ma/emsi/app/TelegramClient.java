package ma.emsi.app;

import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.DisposableBean;
import com.fasterxml.jackson.databind.*;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.*;
import org.slf4j.*;
@Component
public class TelegramClient implements DisposableBean {
 private static final Logger LOG=LoggerFactory.getLogger(TelegramClient.class);
 private final EbankAgent agent;private final ObjectMapper json;private final String token,allowed;
 private final ExecutorService executor=Executors.newSingleThreadExecutor();private volatile boolean running;
 private final HttpClient http=HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
 public TelegramClient(EbankAgent a,ObjectMapper j,@Value("${bots.telegram.token}") String t,@Value("${bots.telegram.allowed-chat-id}") String id){agent=a;json=j;token=t;allowed=id;}
 @EventListener(ApplicationReadyEvent.class) public void start(){if(token.isBlank()||allowed.isBlank())return;running=true;executor.submit(this::poll);}
 private JsonNode api(String method,Object payload)throws Exception{
  var req=HttpRequest.newBuilder(URI.create("https://api.telegram.org/bot"+token+"/"+method)).timeout(Duration.ofSeconds(40)).header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(json.writeValueAsString(payload))).build();
  var res=http.send(req,HttpResponse.BodyHandlers.ofString());if(res.statusCode()!=200)throw new IllegalStateException("Telegram API status "+res.statusCode());return json.readTree(res.body());
 }
 private void poll(){long offset=0;while(running){try{
  var updates=api("getUpdates",Map.of("offset",offset,"timeout",25,"allowed_updates",new String[]{"message"}));
  for(var update:updates.path("result")){offset=update.path("update_id").asLong()+1;var m=update.path("message");String chat=m.path("chat").path("id").asText();if(!chat.equals(allowed)||!m.has("text"))continue;
   String answer;try{answer=agent.chat("telegram:"+chat,m.get("text").asText());}catch(Exception ex){answer="Service temporairement indisponible. Vérifiez la configuration du serveur.";}
   for(int i=0;i<answer.length();i+=4000)api("sendMessage",Map.of("chat_id",chat,"text",answer.substring(i,Math.min(i+4000,answer.length()))));
  }
 }catch(InterruptedException ex){Thread.currentThread().interrupt();break;}catch(Exception ex){LOG.warn("Telegram request failed; retrying without logging credentials");try{Thread.sleep(5000);}catch(InterruptedException stop){Thread.currentThread().interrupt();break;}}}}
 @Override public void destroy(){running=false;executor.shutdownNow();}
}
