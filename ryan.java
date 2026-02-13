public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

import java.util.HashMap;

public class UserManager {
    private HashMap<String, User> users = new HashMap<>();

    public User register(String username) {
        if (users.containsKey(username)) {
            System.out.println("이미 존재하는 사용자입니다.");
            return null;
        }
        User user = new User(username);
        users.put(username, user);
        System.out.println("회원가입 완료!");
        return user;
    }
}
public interface Game {
    void start(User user);
    void showScores();
}


import java.util.*;

public class ScoreManager {
    private HashMap<String, List<Integer>> scoreMap = new HashMap<>();

    public void addScore(String gameName, int score) {
        scoreMap.putIfAbsent(gameName, new ArrayList<>());
        scoreMap.get(gameName).add(score);
    }

    public void showScores(String gameName) {
        List<Integer> scores = scoreMap.get(gameName);

        if (scores == null || scores.isEmpty()) {
            System.out.println("아직 점수가 없습니다.");
            return;
        }

        scores.sort(Collections.reverseOrder());

        System.out.println("=== " + gameName + " 점수 ===");
        for (int s : scores) {
            System.out.println(s);
        }
    }
}
