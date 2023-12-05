package hcmute.vn.springonetomany.Custom.Oauth2;

import hcmute.vn.springonetomany.Custom.CustomUserDetails;
import hcmute.vn.springonetomany.Entities.Role;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Enum.AuthProvider;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OAuthLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserService userService;
    @Autowired
    IRoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = customOAuth2User.getEmail();
        String clientName = customOAuth2User.getOauth2ClientName().toUpperCase();
        User existUser = userService.findUserByEmail(email);
        SimpleUrlAuthenticationSuccessHandler userSuccessHandler =
                new SimpleUrlAuthenticationSuccessHandler("/home");
        SimpleUrlAuthenticationSuccessHandler adminSuccessHandler =
                new SimpleUrlAuthenticationSuccessHandler("/admin");

        if (existUser == null) {
            User newUser = new User();
            // Lấy thông tin người dùng
            String lastName = customOAuth2User.getAttribute("family_name");
            String firstName = clientName.equals("GOOGLE") ? customOAuth2User.getAttribute("given_name") : customOAuth2User.getAttribute("name");
            String picture = customOAuth2User.getAttribute("picture");

            // Set thông tin người dùng
            newUser.setEmail(email);
            newUser.setAuthProvider(AuthProvider.valueOf(customOAuth2User.getOauth2ClientName().toUpperCase()));
            newUser.addRole(roleRepository.findByName("User"));
            newUser.setFirstName(firstName != null ? firstName : "");
            newUser.setLastName(lastName != null ? lastName : "");
            newUser.setPhotos(picture != null ? picture : "");

            userService.saveOauth2(newUser);
        }
        else {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (final GrantedAuthority grantedAuthority : authorities) {
                String authorityName = grantedAuthority.getAuthority();
                if (authorityName.equals("ADMIN")) {
                    boolean isAdmin = true;
                    // if the user is an ADMIN delegate to the adminSuccessHandler
                    adminSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                    return;
                }
            }
        }

        userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }
}
