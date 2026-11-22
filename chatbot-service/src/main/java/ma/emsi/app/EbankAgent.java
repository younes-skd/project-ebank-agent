package ma.emsi.app;

import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.*;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class EbankAgent {
 private final ChatClient client;
 private final ConcurrentHashMap<String,Object> locks=new ConcurrentHashMap<>();
 public EbankAgent(ChatClient.Builder builder,ToolCallbackProvider tools,ChatMemory memory){
  client=builder.defaultSystem("Tu es un assistant pédagogique e-banking. Les données sont fictives. Réponds en français. Utilise les outils MCP pour les données courantes, n'invente jamais de soldes ou de clients. Tu disposes uniquement d'outils de lecture. Explique clairement si un service est indisponible.")
    .defaultToolCallbacks(tools).defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build()).build();
 }
 public String chat(String conversationId,String message){
   // Serialize a conversation so parallel messages cannot interleave its memory.
   synchronized(locks.computeIfAbsent(conversationId,k->new Object())){
     return client.prompt().user(message).advisors(a->a.param(ChatMemory.CONVERSATION_ID,conversationId)).call().content();
   }
 }
 @Bean static ChatMemory chatMemory(){return MessageWindowChatMemory.builder().maxMessages(20).build();}
}
