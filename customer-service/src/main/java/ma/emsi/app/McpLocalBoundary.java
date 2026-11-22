package ma.emsi.app;

import org.springframework.stereotype.Component;
    import org.springframework.web.filter.OncePerRequestFilter;
    import jakarta.servlet.*;
    import jakarta.servlet.http.*;
    import java.io.IOException;
    @Component
    public class McpLocalBoundary extends OncePerRequestFilter {
      @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        if(req.getRequestURI().startsWith("/mcp")){
          String host=req.getServerName();
          if(req.getHeader("Origin")!=null || !(host.equals("localhost")||host.equals("127.0.0.1")||host.equals("[::1]"))){res.sendError(403,"MCP accepts local non-browser clients only");return;}
        }
        chain.doFilter(req,res);
      }
    }
