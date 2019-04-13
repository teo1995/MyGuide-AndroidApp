package hr.foi.myguide;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by DudasPC on 04.02.2018..
 */
public class PrijavaTest {
    @Test
    public void isNetworkConnected() throws Exception {
        Prijava prijava = mock(Prijava.class);
        when(prijava.isNetworkConnected()).thenReturn(false);
        assertEquals("Nema internetske veze",true,prijava.isNetworkConnected());
    }

}