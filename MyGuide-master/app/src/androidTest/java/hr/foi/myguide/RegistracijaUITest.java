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
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by DudasPC on 01.02.2018..
 */
@RunWith(AndroidJUnit4.class)
public class RegistracijaUITest {
    @Rule
    public ActivityTestRule<Registracija> activityActivityTestRule = new ActivityTestRule<>(Registracija.class);

    @Before
    public void init(){
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

/*    @Test
    public void testTapGalleryButtonAndReturnCancel() {
        // Build a result to return when the activity is launched.
        Intent resultData = new Intent();
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultData);

        // Set up result stubbing when an intent sent to "choose photo" is seen.
        intending(toPackage("com.android.gallery")).respondWith(result);

        onView(withId(R.id.imageViewReg)).check(matches(withText("Gallery")));
        onView(withId(R.id.imageViewReg)).perform(longClick());
    }*/

    @Test
    public void testSetImage(){
        onView(withId(R.id.imageViewReg)).perform(longClick());
    }

    @Test
    public void TestEditText(){
        onView(withId(R.id.etFirstNameReg)).check(matches((isDisplayed())));
        onView(withId(R.id.etFirstNameReg)).perform(clearText(),typeText("Mateo"));
        onView(withId(R.id.etLastNameReg)).check(matches((isDisplayed())));
        onView(withId(R.id.etLastNameReg)).perform(clearText(),typeText("Kantoci"));
        onView(withId(R.id.etUsernameReg)).check(matches((isDisplayed())));
        onView(withId(R.id.etUsernameReg)).perform(clearText(),typeText("matkantoc"));
        onView(withId(R.id.etEmailReg)).check(matches((isDisplayed())));
        onView(withId(R.id.etEmailReg)).perform(clearText(),typeText("matkantoc@foi.hr"));
        onView(withId(R.id.etPasswordReg)).perform(scrollTo()).check(matches((isDisplayed())));
        onView(withId(R.id.etPasswordReg)).perform(scrollTo()).perform(clearText(),typeText("matkantocc"));
    }

    @Test
    public void buttonRB() {
        onView(withId(R.id.erbGuide))
                .perform(scrollTo())
                    .perform(longClick());

        onView(withId(R.id.erbGuide))
                .check(matches(isChecked()));

        onView(withId(R.id.erbTourist))
                .perform(scrollTo())
                .perform(longClick());

        onView(withId(R.id.erbTourist))
                .check(matches(isChecked()));
    }

    @Test
    public void buttonIsEnabled() {
        onView(withId(R.id. btnRegister)).check(matches(isEnabled()));
    }

    @Test
    public void buttonIsDisplayed() {
        onView(withId(R.id. btnRegister)).check(matches(isDisplayed()));
    }

    @Test
    public void buttonIsCompletelyDisplayed() {
        onView(withId(R.id. btnRegister)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void buttonIsNotSelectable() {
        onView(withId(R.id. btnRegister)).check(matches(not(isSelected())));
    }

    @Test
    public void buttonIsClickable() {
        onView(withId(R.id. btnRegister)).check(matches(isClickable()));
    }
}
