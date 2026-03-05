package ru.itis.dis403.lab02.context_homework.component;

import org.springframework.stereotype.Component;
import ru.itis.dis403.lab02.context_homework.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserService {
    private Map<Integer, User> users = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public User getUserByName(String username) {
        return users.values().stream()
                .filter(o -> o.getName().equals(username))
                .findFirst()
                .orElseThrow();
    }

    public Map<String, String> getUserSettingsById(int id) {
        User user = users.get(id);
        Map<String, String> userSettings = new HashMap<>();
        if (user != null) {
            userSettings.put("preferLanguage", user.getPreferLanguage().toString());
            userSettings.put("isHideOnline", user.isHideOnline() ? "Да" : "Нет");
            userSettings.put("pageType", user.getPageType().toString());
        } else {
            throw new RuntimeException("User not found");
        }
        return userSettings;
    }
}
