package net.stawrul;

import java.util.Random;
import net.stawrul.model.Contact;
import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.OutOfStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

    @Mock
    EntityManager em;

    @Test(expected = OutOfStockException.class)
    public void whenOrderedContactNotAvailable_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Contact contact = new Contact();
        contact.setAmount(0); //nie ma produktu
        Random generator = new Random();
        contact.setPrice(generator.nextInt(1000));
        
        Order order = new Order();
        order.getContacts().add(contact);

        Mockito.when(em.find(Contact.class, contact.getId())).thenReturn(contact);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }
    
    @Test(expected = OutOfStockException.class)
     public void whenPriceSmallerThan50_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Contact contact = new Contact();
        contact.setAmount(1);
        contact.setPrice(40); // cena mniejsza od 50 dla testu
        
        Order order = new Order();
        order.getContacts().add(contact);

        Mockito.when(em.find(Contact.class, contact.getId())).thenReturn(contact);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test
    public void whenOrderedContactAvailable_placeOrderDecreasesAmountByOne() {
        //Arrange  
        Contact contact = new Contact();
        contact.setAmount(1);
        Random generator = new Random();
        contact.setPrice(generator.nextInt(1000));
        
        Order order = new Order();
        order.getContacts().add(contact);

        Mockito.when(em.find(Contact.class, contact.getId())).thenReturn(contact);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(0, (int)contact.getAmount());
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(1)).persist(order);
    }

    @Test
    public void whenGivenLowercaseString_toUpperReturnsUppercase() {

        //Arrange
        String lower = "abcdef";

        //Act
        String result = lower.toUpperCase();

        //Assert
        assertEquals("ABCDEF", result);
    }
}
