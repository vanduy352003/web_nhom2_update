package hcmute.vn.springonetomany.Custom.Oauth2;

import hcmute.vn.springonetomany.Entities.Role;
import hcmute.vn.springonetomany.Entities.User;
import hcmute.vn.springonetomany.Enum.AuthProvider;
import hcmute.vn.springonetomany.Repository.IRoleRepository;
import hcmute.vn.springonetomany.Repository.IUserRepository;
import hcmute.vn.springonetomany.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private IUserRepository userRepo;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        User user = userRepo.findByEmail(email);

        Set<Role> roles;
        if (user != null) {
            roles = user.getRoles();
        } else {
            roles = new HashSet<>();
            roles.add(new Role("USER"));
        }
        return new CustomOAuth2User(oAuth2User, clientName, roles);
    }
}
