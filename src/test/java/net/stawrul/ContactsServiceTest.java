package net.stawrul;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
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
import net.stawrul.controllers.Controller;
import net.stawrul.services.ContactsService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ContactsServiceTest {

    @Mock
    EntityManager em;
    
    @Test
    public void whenAddingContactsToDBContacts_ReturningValueIsAdded() throws URISyntaxException {
        //Arrange  
        Contact contactI = new Contact();
        contactI.setAmount(1);
        Random generator = new Random();
        contactI.setPrice(generator.nextInt(1000));
        contactI.setColor("red");
       
     
        UriComponentsBuilder uriBuilder = Mockito.mock(UriComponentsBuilder.class, RETURNS_DEEP_STUBS);

        Mockito.when(
                uriBuilder.path(any()).buildAndExpand(contactI.getId()).toUri()
        ).thenReturn(
                new URI("/contacts")
        );
        
        ContactsService contactsService = new ContactsService(em);
        Controller controller = new Controller(contactsService);
        
        //Act
        controller.addContact(contactI, uriBuilder);

        //Assert
        //sprawdzamy czy to te same obiekty kontaktu (czy rzeczywiscie sie dodal)
        
        assertNotNull(controller.getContact(contactI.getId()));
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu aktualizacji soczewki:
        Mockito.verify(em, times(1)).persist(contactI);
    }
    
    @Test
     public void whenGettingTheContact_theContactWasNotAddedToDB() {
        //Arrange
        ContactsService contactsService = new ContactsService(em);
        Controller controller = new Controller(contactsService);
        
        Contact contactI = new Contact();

        Mockito.when(
            em.find(eq(Controller.class), any(UUID.class))
        ).thenReturn(
            controller
        );

        //Act
        controller.getContact(contactI.getId());
        assertNotSame(controller.getContact(contactI.getId()), contactI); // po lewej 404 NOT FOUND, po prawej nasz obiekt
        Mockito.verify(em, never()).persist(contactI);
        
        //Assert - exception expected
    }
    
    
    
}
