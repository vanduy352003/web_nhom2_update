package hcmute.vn.springonetomany.Custom.Oauth2;

import hcmute.vn.springonetomany.Custom.CustomUser;
import hcmute.vn.springonetomany.Entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User, CustomUser {
    private OAuth2User oauth2User;
    private String oauth2ClientName;
    private Set<Role> roles;

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(oauth2User.getAuthorities());

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }
    @Override
    public String getEmail() {
        return oauth2User.<String>getAttribute("email");
    }

}
