package hr.foi.myguide;

import android.os.SystemClock;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hr.foi.webservice.ZahtjevZaTuru;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by DudasPC on 04.02.2018..
 */
@RunWith(AndroidJUnit4.class)
public class PocetnaStranicaUITest {
    @Rule
    public ActivityTestRule<PocetnaStranica> activityActivityTestRule = new ActivityTestRule<>(PocetnaStranica.class);

    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void buttonIsNotSelectable() {
        SystemClock.sleep(1000);
        onView(withId(R.id. recyclerView)).check(matches(not(isSelected())));
    }

    @Test
    public void buttonClicked() {
        SystemClock.sleep(1000);
        onView(withId(R.id.recyclerView)).check(matches((isDisplayed())));
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Varazdin")), longClick()));
    }

    @Test
    public void webService(){
        SystemClock.sleep(1000);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject dataJSON = jsonResponse.getJSONObject("data");
                    boolean success = dataJSON.getBoolean("success");
                    Integer idUserType = 1;
                    if (success) {
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ZahtjevZaTuru zahtjevZaTuru = new ZahtjevZaTuru(responseListener);
        RequestQueue queue = Volley.newRequestQueue(activityActivityTestRule.getActivity());
        queue.add(zahtjevZaTuru);
    }

}
