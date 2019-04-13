package hr.foi.myguide;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

/**
 * Created by DudasPC on 01.02.2018..
 */
@RunWith(AndroidJUnit4.class)
public class PrijavaUITest {

    @Rule
    public ActivityTestRule<Prijava> activityActivityTestRule = new ActivityTestRule<>(Prijava.class);

    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testEditText(){
        onView(withId(R.id.etUsernameLogin)).check(matches((isDisplayed())));
        onView(withId(R.id.etUsernameLogin)).perform(clearText(),typeText("matkantoc"));
        onView(withId(R.id.etPasswordLogin)).check(matches((isDisplayed())));
        onView(withId(R.id.etPasswordLogin)).perform(clearText(),typeText("matkantoc"));
    }

    @Test
    public void buttonIsEnabled() {
        onView(withId(R.id. btnLogin)).check(matches(isEnabled()));
    }

    @Test
    public void buttonIsDisplayed() {
        onView(withId(R.id. btnLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void buttonIsCompletelyDisplayed() {
        onView(withId(R.id. btnLogin)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void buttonIsNotSelectable() {
        onView(withId(R.id. btnLogin)).check(matches(not(isSelected())));
    }

    @Test
    public void buttonIsClickable() {
        onView(withId(R.id. btnLogin)).perform(scrollTo()).check(matches(isClickable()));
    }

    @Test
    public void buttonClicked() {
        onView(withId(R.id.etUsernameLogin)).check(matches((isDisplayed())));
        onView(withId(R.id.etUsernameLogin)).perform(clearText(),typeText("matkantoc"));
        onView(withId(R.id.etPasswordLogin)).check(matches((isDisplayed())));
        onView(withId(R.id.etPasswordLogin)).perform(clearText(),typeText("matkantoc"));
        onView(withId(R.id.btnLogin))
                .perform(scrollTo())
                .perform(longClick());

    }

    @Test
    public void isNetworkConnected() throws Exception {
        boolean actual = activityActivityTestRule.getActivity().isNetworkConnected();
        assertEquals(true, actual);
    }
}
