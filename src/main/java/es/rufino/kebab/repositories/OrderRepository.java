package es.rufino.kebab.repositories;

import es.rufino.kebab.models.Order;
import es.rufino.kebab.models.User;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rufino Serrano Cañas
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    List<Order> findByOrderedAtBetween(Date minimumDate, Date maximumDate);

    List<Order> findByUserEqualsAndOrderedAtBetween(User user, Date minimumDate, Date maximumDate);

}