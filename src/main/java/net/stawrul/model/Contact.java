package net.stawrul.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * Klasa encyjna reprezentująca towar w sklepie (książkę).
 */
@Entity
@EqualsAndHashCode(of = "id")
@NamedQueries(value = {
        @NamedQuery(name = Contact.FIND_ALL, query = "SELECT b FROM Contact b")
})
public class Contact {
    public static final String FIND_ALL = "Contact.FIND_ALL";

    @Getter
    @Id
    UUID id = UUID.randomUUID();

    @Getter
    @Setter
    String color;

    @Getter
    @Setter
    Integer amount;
    
    @Getter
    @Setter
    Integer price;
        
    @Getter
    @Setter
    String name;
            
    @Getter
    @Setter
    Integer timeOfLife;
    
    @Getter
    @Setter
    Float power;
    
    
    public void setMyId(UUID id){
        this.id = id;
    }
}


