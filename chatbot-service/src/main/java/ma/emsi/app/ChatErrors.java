package ma.emsi.app;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
@RestControllerAdvice
public class ChatErrors {
 @ExceptionHandler(org.springframework.ai.retry.NonTransientAiException.class)
 ResponseEntity<ProblemDetail> provider(){return ResponseEntity.status(502).body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY,"AI provider rejected the request. Verify your own API key, model and quota."));}
}
