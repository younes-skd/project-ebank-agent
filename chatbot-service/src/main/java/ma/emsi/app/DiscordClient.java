package ma.emsi.app;

import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.DisposableBean;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import java.util.concurrent.*;
@Component
public class DiscordClient extends ListenerAdapter implements DisposableBean {
 private final EbankAgent agent;private final String token,allowed;private JDA jda;
 private final ExecutorService executor=Executors.newSingleThreadExecutor();
 public DiscordClient(EbankAgent a,@Value("${bots.discord.token}") String t,@Value("${bots.discord.allowed-user-id}") String id){agent=a;token=t;allowed=id;}
 @EventListener(ApplicationReadyEvent.class) public void start(){if(token.isBlank()||allowed.isBlank())return;jda=JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).addEventListeners(this).build();}
 @Override public void onMessageReceived(MessageReceivedEvent e){
  if(e.getAuthor().isBot()||!e.getAuthor().getId().equals(allowed))return;
  String text=e.getMessage().getContentRaw();if(!text.startsWith("!bank "))return;
  executor.submit(()->{try{String answer=agent.chat("discord:"+e.getAuthor().getId()+":"+e.getChannel().getId(),text.substring(6));for(int i=0;i<answer.length();i+=1900)e.getChannel().sendMessage(answer.substring(i,Math.min(i+1900,answer.length()))).queue();}catch(Exception ex){e.getChannel().sendMessage("Service temporairement indisponible.").queue();}});
 }
 @Override public void destroy(){if(jda!=null)jda.shutdown();executor.shutdownNow();}
}
