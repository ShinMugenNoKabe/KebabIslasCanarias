package es.rufino.kebab.services;

import es.rufino.kebab.exceptions.ResourceNotFoundException;
import es.rufino.kebab.models.Order;
import es.rufino.kebab.models.User;
import es.rufino.kebab.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order insert(Order newOrder) {
        return orderRepository.save(newOrder);
    }

    // TODO: Move to the OrderLine Service
//    public Order addLineaPedido(OrderLine lp, Order p) {
//        p.getOrderLines().add(lp);
//        return insert(p);
//    }

    public Order findById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The Order was not found"));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<Order> findBetweenDates(Date minimumDate, Date maximumDate) {
        return orderRepository.findByOrderedAtBetween(minimumDate, maximumDate);
    }

    public List<Order> findBetweenDates(User user, Date minimumDate, Date maximumDate) {
        return orderRepository.findByUserEqualsAndOrderedAtBetween(user, minimumDate, maximumDate);
    }
    
}
