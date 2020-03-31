package tw.edu.ntub.imd.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.databaseconfig.dao.UserDAO;
import tw.edu.ntub.imd.databaseconfig.entity.User;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserDAO userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userDAO.getById(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new org.springframework.security.core.userdetails.User(
                    user.getAccount(),
                    user.getPassword(),
                    user.getEnable(),
                    true,
                    true,
                    true,
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRoleName()))
            );
        } else {
            throw new UsernameNotFoundException("請檢查帳號密碼是否錯誤");
        }
    }
}
