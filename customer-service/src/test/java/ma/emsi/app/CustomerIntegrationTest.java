package ma.emsi.app;

import org.junit.jupiter.api.Test;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.test.web.servlet.MockMvc;
    import org.springframework.http.MediaType;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
    import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
    @SpringBootTest(properties={"spring.cloud.config.enabled=false","eureka.client.enabled=false"}) @AutoConfigureMockMvc
    class CustomerIntegrationTest {
     @Autowired MockMvc mvc; @Autowired ObjectMapper json;
     @Test void createReadUpdateDelete() throws Exception {
      String body="{\"name\": \"Test Client\", \"email\": \"test@example.com\"}";
      var res=mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andReturn();
      long id=json.readTree(res.getResponse().getContentAsString()).get("id").asLong();
      mvc.perform(get("/customers/"+id)).andExpect(jsonPath("$.name").value("Test Client"));
      mvc.perform(put("/customers/"+id).contentType(MediaType.APPLICATION_JSON).content(body.replace("Test Client","Updated"))).andExpect(jsonPath("$.name").value("Updated"));

      mvc.perform(delete("/customers/"+id)).andExpect(status().isNoContent());
      mvc.perform(get("/customers/"+id)).andExpect(status().isNotFound());
     }
     @Test void invalidRequestAndCors() throws Exception {
      mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
      mvc.perform(options("/customers").header("Origin","http://localhost:4200").header("Access-Control-Request-Method","GET")).andExpect(header().string("Access-Control-Allow-Origin","http://localhost:4200"));
     }
    }
