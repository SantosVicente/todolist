package br.com.santosvicente.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.santosvicente.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

  @Autowired
  private IUserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    var header = request.getHeader("Authorization");

    var token = header.substring("Basic".length()).trim();

    var decoded = new String(Base64.getDecoder().decode(token));

    var username = decoded.split(":")[0];
    var password = decoded.split(":")[1];

    var userExists = this.userRepository.findByUsername(username);

    if (userExists == null) {
      response.sendError(401);
      return;
    } else {
      var passwordHashed = userExists.getPassword();

      var passwordIsCorrect = BCrypt.verifyer().verify(password.toCharArray(),
          passwordHashed);

      if (passwordIsCorrect.verified) {
        filterChain.doFilter(request, response);
      } else {
        response.sendError(401);
        return;
      }
    }
  }
}
