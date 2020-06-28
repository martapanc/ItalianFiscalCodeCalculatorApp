package com.example.fiscalcode_java;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ComputeFragmentEspressoTest {

    @ClassRule
    public static final ForceLocaleRule localeTestRule = new ForceLocaleRule(Locale.ITALY);

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testFiscalCodeIsComputedSuccessfully() {
        onView(withId(R.id.compute_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.first_name)).perform(typeText("Marta"));
        onView(withId(R.id.last_name)).perform(typeText("Pancaldi"));
        onView(withId(R.id.femaleRadioButton)).perform(click());
        onView(withId(R.id.dateOfBirth_editText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1995, 7, 12));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.pob_autocompleteTextView)).perform(typeText("Guastalla (RE)"));
        onView(withId(R.id.compute_button)).perform(click());
        onView(withId(R.id.fiscalCodeOutput)).check(matches(withText(containsString("PNCMRT95L52E253R"))));
    }

    @Test
    public void testShowsEmptyValueErrorMessages() {
        onView(withId(R.id.compute_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.compute_button)).perform(click());
        onView(withId(R.id.first_name)).check(matches(hasErrorText("Valore richiesto")));
        onView(withId(R.id.last_name)).check(matches(hasErrorText("Valore richiesto")));
        onView(withId(R.id.femaleRadioButton)).check(matches(isNotChecked()));
        onView(withId(R.id.maleRadioButton)).check(matches(isNotChecked()));
        onView(withId(R.id.dateOfBirth_editText)).check(matches(withText("")));
        onView(withId(R.id.pob_autocompleteTextView)).check(matches(hasErrorText("Valore richiesto")));
    }

    @Test
    public void testShowsInvalidInputErrorMessages() {
        onView(withId(R.id.first_name)).perform(typeText("Marta$"));
        onView(withId(R.id.last_name)).perform(typeText("Panca1di"));
        onView(withId(R.id.dateOfBirth_editText)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1899, 1, 1));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.pob_autocompleteTextView)).perform(typeText("Guastalla"));
        onView(withId(R.id.compute_button)).perform(click());

        onView(withId(R.id.first_name)).check(matches(hasErrorText("Il valore inserito è invalido")));
        onView(withId(R.id.last_name)).check(matches(hasErrorText("Il valore inserito è invalido")));
        onView(withId(R.id.dateOfBirth_editText)).check(matches(withText("01/01/1899")));
        onView(withId(R.id.pob_autocompleteTextView)).check(matches(hasErrorText("Il valore inserito è invalido")));
    }
}
