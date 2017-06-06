package net.stawrul.services;

import net.stawrul.model.Contact;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na książkach.
 */
@Service
public class ContactsService extends EntityService<Contact> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public ContactsService(EntityManager em) {

        //Contact.class - klasa encyjna, na której będą wykonywane operacje
        //Contact::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Contact.class, Contact::getId);
    }

    /**
     * Pobranie wszystkich książek z bazy danych.
     *
     * @return lista książek
     */
    public List<Contact> findAll() {
        //pobranie listy wszystkich książek za pomocą zapytania nazwanego (ang. named query)
        //zapytanie jest zdefiniowane w klasie Contact
        return em.createNamedQuery(Contact.FIND_ALL, Contact.class).getResultList();
    }

}
