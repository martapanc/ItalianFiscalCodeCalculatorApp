package com.pancaldim.fiscalcode_app;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pancaldim.fiscalcode_app.TestConstants.DAY_RANGE_ERROR;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_DOB_SHORT;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_FISCAL_CODE;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_TOWN;
import static com.pancaldim.fiscalcode_app.TestConstants.WRONG_FORMAT;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExtractFragmentEspressoTest {

    @ClassRule
    public static final ForceLocaleRule localeTestRule = new ForceLocaleRule(Locale.ITALY);

    @Rule
    public final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void assertDateIsExtractedSuccessfully() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(typeText(VALID_FISCAL_CODE))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.extract_data_button)).perform(click());
        onView(withId(R.id.ext_first_name_text)).check(matches(withText(containsString("MRT"))));
        onView(withId(R.id.ext_last_name_text)).check(matches(withText(containsString("PNC"))));
        onView(withId(R.id.ext_gender_text)).check(matches(withText(containsString("F"))));
        onView(withId(R.id.ext_dob_text)).check(matches(withText(containsString(VALID_DOB_SHORT))));
        onView(withId(R.id.ext_pob_text)).check(matches(withText(containsString(VALID_TOWN))));
    }

    @Test
    public void assertShowsEmptyValueErrorMessages() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.extract_data_button)).perform(click());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs("Valore richiesto");
    }

    @Test
    public void assertShowsWrongFormatErrorMessage() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(typeText("PNCMRT95L52E253"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);
    }

    @Test
    public void assertShowsTownNotFoundErrorMessage() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(typeText("PNCMRT95L52E000E"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenToastErrorMessageIs("Errore: il luogo di nascita non è stato trovato");
    }

    @Test
    public void assertShowsDayOutOfRangeError() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(typeText("PNCMRT95L72E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenToastErrorMessageIs(DAY_RANGE_ERROR);

        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(clearText(), typeText("PNCMRT95L00E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenToastErrorMessageIs(DAY_RANGE_ERROR);
    }

    @Test
    public void assertShowsInvalidDayError() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(typeText("PNCMRT95L5IE253R"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);

        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(clearText(), typeText("PNCMRT95L#1E253R"));
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);
    }

    @Test
    public void assertShowsInvalidDateError() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(typeText("PNCMRT95Z52E253R"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);

        onView(withId(R.id.ext_fiscalCodeInput_input)).perform(clearText(), typeText("PNCMRT95952E253R"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.extract_data_button)).perform(click());
        thenInputErrorMessageIs(WRONG_FORMAT);
    }

    public void thenToastErrorMessageIs(String errorMessage) {
        onView(withText(errorMessage))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    public void thenInputErrorMessageIs(String errorMessage) {
        onView(withId(R.id.ext_fiscalCodeInput_input)).check(matches(hasErrorText(errorMessage)));
    }
}
