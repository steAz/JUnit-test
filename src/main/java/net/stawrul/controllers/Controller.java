package net.stawrul.controllers;

import net.stawrul.model.Contact;
import net.stawrul.services.ContactsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;


/**
 * Kontroler zawierający akcje związane z książkami w sklepie.
 *
 * Parametr "/contacts" w adnotacji @RequestMapping określa prefix dla adresów wszystkich akcji kontrolera.
 */
@RestController
@RequestMapping("/contacts")
public class Controller {
    
    public int ilosc = 0;

    //Komponent realizujący logikę biznesową operacji na książkach
    final ContactsService contactsService;

    //Instancja klasy ContactsService zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public Controller(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    /**
     * Pobieranie listy wszystkich książek.
     *
     * Żądanie:
     * GET /contacts
     *
     * @return lista książek
     */
    @GetMapping
    public List<Contact> listContacts() {
        return contactsService.findAll();
    }

    /**
     * Dodawanie nowej książki.
     *
     * Żądanie:
     * POST /contacts
     *
     * @param contact obiekt zawierający dane nowej książki, zostanie zbudowany na podstawie danych
             przesłanych w ciele żądania (automatyczne mapowanie z formatu JSON na obiekt
             klasy Contact)
     * @param uriBuilder pomocniczy obiekt do budowania adresu wskazującego na nowo dodaną książkę,
     *                   zostanie wstrzyknięty przez framework Spring
     *
     * @return odpowiedź HTTP dla klienta
     */
    @PostMapping
    public ResponseEntity<Void> addContact(@RequestBody Contact contact, UriComponentsBuilder uriBuilder) {

        if (contactsService.find(contact.getId()) == null) {
            //Identyfikator nie istnieje w bazie danych - nowa książka zostaje zapisana
            contactsService.save(contact);
            //Jeśli zapisywanie się powiodło zwracana jest odpowiedź 201 Created z nagłówkiem Location, który zawiera
            //adres nowo dodanej książki
            URI location = uriBuilder.path("/contacts/{id}").buildAndExpand(contact.getId()).toUri();
            return ResponseEntity.created(location).build();

        } else {
            //Identyfikator książki już istnieje w bazie danych. Żądanie POST służy do dodawania nowych elementów,
            //więc zwracana jest odpowiedź z kodem błędu 409 Conflict
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    /**
     * Pobieranie informacji o pojedynczej książce.
     *
     * Żądanie:
     * GET /contacts/{id}
     *
     * @param id identyfikator książki
     *
     * @return odpowiedź 200 zawierająca dane książki lub odpowiedź 404, jeśli książka o podanym identyfikatorze nie
     * istnieje w bazie danych
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContact(@PathVariable UUID id) {
        //wyszukanie książki w bazie danych
        Contact contact = contactsService.find(id);

        //W warstwie biznesowej brak książki o podanym id jest sygnalizowany wartością null. Jeśli książka nie została
        //znaleziona zwracana jest odpowiedź 404 Not Found. W przeciwnym razie klient otrzymuje odpowiedź 200 OK
        //zawierającą dane książki w domyślnym formacie JSON
        return contact != null ? ResponseEntity.ok(contact) : ResponseEntity.notFound().build();
    }

    /**
     * Aktualizacja danych książki.
     *
     * Żądanie:
     * PUT /contacts/{id}
     *
     * @param contact
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateContact(@RequestBody Contact contact) {
        if (contactsService.find(contact.getId()) != null) {
            //aktualizacja danych jest możliwa o ile książka o podanym id istnieje w bazie danych
            contactsService.save(contact);
            return ResponseEntity.ok().build();

        } else { 
            //nie odnaleziono książki o podanym id - odpowiedź 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

}
