package com.naumoff.rnc.database.repository.users.history;

import com.naumoff.rnc.database.entities.users.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

}
