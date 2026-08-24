package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserCountersEntity;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserCountersRepository;
import org.springframework.stereotype.Component;

@Component
public class UserCountersService {

    private final UserCountersRepository userCountersRepository;

    public UserCountersService(
            UserCountersRepository userCountersRepository
    ) {
        this.userCountersRepository = userCountersRepository;
    }

    public void incrementViewed(UserEntity currentUser) {

        UserCountersEntity counters = currentUser.getUserCountersEntity();
        if (counters == null) {
            counters = new UserCountersEntity();
            counters.setViewed(0L);
        }

        Long current = counters.getViewed();
        current++;
        counters.setViewed(current);

        userCountersRepository.save(counters);
    }

    public void incrementSendedOffers(UserEntity currentUser) {
        UserCountersEntity counters = currentUser.getUserCountersEntity();
        if (counters == null) {
            counters = new UserCountersEntity();
            counters.setSendedOffers(0L);
        }

        Long current = counters.getSendedOffers();
        current++;
        counters.setSendedOffers(current);

        userCountersRepository.save(counters);
    }

    public void incrementAskedQuestions(UserEntity currentUser) {
        UserCountersEntity counters = currentUser.getUserCountersEntity();
        if (counters == null) {
            counters = new UserCountersEntity();
            counters.setAskedQuestions(0L);
            counters.setUser(currentUser);
        }

        Long current = counters.getAskedQuestions();
        current++;
        counters.setAskedQuestions(current);

        userCountersRepository.save(counters);
    }

    public void incrementAcceptedOrders(UserEntity currentUser) {
        UserCountersEntity counters = currentUser.getUserCountersEntity();
        if (counters == null) {
            counters = new UserCountersEntity();
            counters.setAcceptedOrders(0L);
        }

        Long current = counters.getAcceptedOrders();
        current++;
        counters.setAcceptedOrders(current);

        userCountersRepository.save(counters);
    }

    public void incrementOrderInWorks(UserEntity currentUser) {
        UserCountersEntity counters = currentUser.getUserCountersEntity();
        if (counters == null) {
            counters = new UserCountersEntity();
            counters.setOrderInWorks(0L);
        }

        Long current = counters.getOrderInWorks();
        current++;
        counters.setOrderInWorks(current);

        userCountersRepository.save(counters);
    }

    public void actualizeAllCounters(UserEntity currentUser) {}
}
