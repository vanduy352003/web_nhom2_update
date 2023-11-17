package hcmute.vn.springonetomany.Custom;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SimpleUrlAuthenticationSuccessHandler userSuccessHandler =
                new SimpleUrlAuthenticationSuccessHandler("/home");
        SimpleUrlAuthenticationSuccessHandler adminSuccessHandler =
                new SimpleUrlAuthenticationSuccessHandler("/admin");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority grantedAuthority : authorities) {
            String authorityName = grantedAuthority.getAuthority();
            if (authorityName.equals("ADMIN")) {
                // if the user is an ADMIN delegate to the adminSuccessHandler
                adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                return;
            }
        }
        // if the user is not an admin delegate to the userSuccessHandler
        userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
