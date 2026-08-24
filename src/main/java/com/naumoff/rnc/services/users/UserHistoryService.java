package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.entities.users.UserHistory;
import com.naumoff.rnc.database.repository.users.history.UserHistoryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserHistoryService {

    private UserHistoryRepository userHistoryRepository;

    public UserHistoryService(
            UserHistoryRepository userHistoryRepository
    ) {
        this.userHistoryRepository = userHistoryRepository;
    }

    public UserHistory createHistoryRecord(String title, String description, UserEntity currentUser) {
        UserHistory userHistory = createBaseUserHistory(title, description, currentUser);

        userHistoryRepository.save(userHistory);

        return userHistory;
    }

    public UserHistory createHistoryRecord(
            String title,
            String description,
            UserEntity currentUser,
            String objectType,
            Long objectId
    ) {
        UserHistory userHistory = createBaseUserHistory(title, description, currentUser);

        userHistory.setObjectType(objectType);
        userHistory.setObjectId(objectId);

        userHistoryRepository.save(userHistory);

        return userHistory;
    }

    /**
     * Create base user history
     *
     * @param title String title of history record
     * @param description description of history record
     * @param currentUser link to current auth user
     * @return userhistory entity
     */
    private UserHistory createBaseUserHistory(String title, String description, UserEntity currentUser) {
        UserHistory userHistory = new UserHistory();

        userHistory.setCreatedAt(LocalDateTime.now());
        userHistory.setUpdatedAt(LocalDateTime.now());
        userHistory.setTitle(title);
        userHistory.setDescription(description);
        userHistory.setUser(currentUser);

        return userHistory;
    }
}
