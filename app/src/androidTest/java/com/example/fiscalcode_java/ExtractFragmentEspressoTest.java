package com.example.fiscalcode_java;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExtractFragmentEspressoTest {

    @ClassRule
    public static final ForceLocaleRule localeTestRule = new ForceLocaleRule(Locale.ITALY);
    public static final String WRONG_FORMAT = "Il codice fiscale inserito non è nel formato richiesto";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void assertDateIsExtractedSuccessfully() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.fiscalCodeInputEditText)).perform(typeText("PNCMRT95L52E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        onView(withId(R.id.first_name_text)).check(matches(withText(containsString("MRT"))));
        onView(withId(R.id.last_name_text)).check(matches(withText(containsString("PNC"))));
        onView(withId(R.id.gender_text)).check(matches(withText(containsString("F"))));
        onView(withId(R.id.dob_text)).check(matches(withText(containsString("12/07/95"))));
        onView(withId(R.id.pob_text)).check(matches(withText(containsString("Guastalla (RE)"))));
    }

    @Test
    public void assertShowsEmptyValueErrorMessages() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.extract_data_button)).perform(click());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs("Valore richiesto");
        System.out.println("");
    }

    @Test
    public void assertShowsWrongFormatErrorMessage() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.fiscalCodeInputEditText)).perform(typeText("PNCMRT95L52E253"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);
    }

    @Test
    public void assertShowsTownNotFoundErrorMessage() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.fiscalCodeInputEditText)).perform(typeText("PNCMRT95L52E000E"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenToastErrorMessageIs("Errore: il luogo di nascita non è stato trovato");
    }

    @Test
    public void assertShowsDayOutOfRangeError() {
        final String dayRangeError = "Errore: il giorno di nascita inserito è fuori dal range (maschi: 1-31; femmine: 41-71)";

        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.fiscalCodeInputEditText)).perform(typeText("PNCMRT95L72E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenToastErrorMessageIs(dayRangeError);

        onView(withId(R.id.fiscalCodeInputEditText)).perform(clearText(), typeText("PNCMRT95L00E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenToastErrorMessageIs(dayRangeError);
    }

    @Test
    public void assertShowsInvalidDayError() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.fiscalCodeInputEditText)).perform(typeText("PNCMRT95L5IE253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);

        onView(withId(R.id.fiscalCodeInputEditText)).perform(clearText(), typeText("PNCMRT95L#1E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);
    }

    @Test
    public void assertShowsInvalidDateError() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.fiscalCodeInputEditText)).perform(typeText("PNCMRT95Z52E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);

        onView(withId(R.id.fiscalCodeInputEditText)).perform(clearText(), typeText("PNCMRT95952E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);
    }

    public void thenToastErrorMessageIs(String errorMessage) {
        onView(withText(errorMessage))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    public void thenInputErrorMessageIs(String errorMessage) {
        onView(withId(R.id.fiscalCodeInputEditText)).check(matches(hasErrorText(errorMessage)));
    }
}
