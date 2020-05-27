package tw.edu.ntub.imd.courtesyumbrella.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import tw.edu.ntub.imd.courtesyumbrella.TestApplication;
import tw.edu.ntub.imd.databaseconfig.Config;
import tw.edu.ntub.imd.databaseconfig.dao.UserDAO;
import tw.edu.ntub.imd.databaseconfig.entity.User;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@ContextConfiguration(classes = TestApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        Config.class
})
@DisplayName("UserDAO測試")
class UserDAOTest {
    @Autowired
    private UserDAO userDAO;

    @Test
    @DisplayName("測試insert")
    void testInsert() {
        User user = new User();
        user.setAccount("test insert");
        user.setPassword("test password");
        user.setBirthday(LocalDate.of(2020, 4, 2));
        user.setEmail("10646007@ntub.edu.tw");
        user.setModifyId(user.getAccount());
        Assertions.assertDoesNotThrow(() -> userDAO.saveAndFlush(user));
    }

    @Test
    @DisplayName("測試getById")
    void testGetById() {
        Optional<User> optionalUser = userDAO.findById("admin");
        Assertions.assertTrue(optionalUser.isPresent());
        User user = optionalUser.get();
        Assertions.assertAll(
                () -> Assertions.assertEquals("admin", user.getAccount()),
                () -> Assertions.assertEquals("$2a$10$hagQjQFpQTlChHKHzKpLT.0eabhsQNOOnJ/VJkH/e4PPRqoG55nsK", user.getPassword()),
                () -> Assertions.assertEquals(LocalDate.of(2020, 3, 28), user.getBirthday()),
                () -> Assertions.assertEquals("10646007@ntub.edu.tw", user.getEmail())
        );
        Optional<User> optionalNotInDb = userDAO.findById("not in db");
        Assertions.assertTrue(optionalNotInDb.isEmpty());
    }
}
