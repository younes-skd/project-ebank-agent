package ma.emsi.app;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
@RestControllerAdvice
public class UpstreamErrors {
 @ExceptionHandler(feign.FeignException.class)
 ResponseEntity<ProblemDetail> upstream(feign.FeignException e){HttpStatus s=e.status()==404?HttpStatus.BAD_REQUEST:HttpStatus.SERVICE_UNAVAILABLE;return ResponseEntity.status(s).body(ProblemDetail.forStatusAndDetail(s,e.status()==404?"Unknown customer or product":"Dependent service unavailable"));}
}
