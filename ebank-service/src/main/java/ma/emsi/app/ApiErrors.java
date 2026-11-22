package ma.emsi.app;

import org.springframework.http.*;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    @RestControllerAdvice
    public class ApiErrors {
      @ExceptionHandler(IllegalArgumentException.class)
      ResponseEntity<ProblemDetail> invalid(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,e.getMessage()));
      }
      @ExceptionHandler(MethodArgumentNotValidException.class)
      ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,"Invalid request: "+e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+" "+x.getDefaultMessage()).toList()));
      }
    }
