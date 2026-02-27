package ru.itis.dis403.lab01.homework.component;

import ru.itis.dis403.lab01.homework.annotation.Component;
import ru.itis.dis403.lab01.homework.model.Language;
import ru.itis.dis403.lab01.homework.model.PageType;
import ru.itis.dis403.lab01.homework.model.User;

import java.util.List;
import java.util.Map;

@Component
public class Application {

    private UserService userService;

    public Application(UserService userService) {
        this.userService = userService;
    }

    public void run() {
        init();
    }

    private void init() {
        userService.save(new User( 1, "Иван Петров",
                "ivan.petrov@email.com",
                "+7 (901) 123-45-67",
                Language.RU,
                true,
                PageType.PUBLIC
        ));
        userService.save(new User(
                2,
                "John Smith",
                "john.smith@email.com",
                "+1 (555) 123-4567",
                Language.EN,
                false,
                PageType.PRIVATE
        ));

        userService.save(new User(
                3,
                "Jan Jansen",
                "jan.jansen@email.nl",
                "+31 (20) 123-4567",
                Language.DU,
                true,
                PageType.PUBLIC
        ));

        userService.save(new User(
                4,
                "Yuki Tanaka",
                "yuki.tanaka@email.jp",
                "+81 (90) 1234-5678",
                Language.JP,
                false,
                PageType.PRIVATE
        ));

        userService.save(new User(
                5,
                "Maria Garcia",
                "maria.garcia@email.es",
                "+34 (91) 123-4567",
                Language.SP,
                true,
                PageType.PUBLIC
        ));
    }

    public List<User> getUsers() {
        return userService.getAll();
    }

    public Map<String, String> getUserSettingsById(int id) {
        return userService.getUserSettingsById(id);
    }

    public User getUserById(int id) {
        return userService.getUserById(id);
    }
}
