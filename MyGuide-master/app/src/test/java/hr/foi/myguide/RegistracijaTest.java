package hr.foi.myguide;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


/**
 * Created by DudasPC on 02.02.2018..
 */
public class RegistracijaTest {

    @Test
    public void checkEmail() throws Exception {
        Registracija registracija = mock(Registracija.class);
        System.out.println("Početak");
        String email = "josdudas@foi.hr";
        when(registracija.checkEmail(email)).thenReturn(true);
        String email2 = "josdudas";
        when(registracija.checkEmail(email2)).thenReturn(false);
        assertEquals("Neispravan email",true, registracija.checkEmail(email));
        assertEquals("Ispravan email", false, registracija.checkEmail(email2));

    }
}