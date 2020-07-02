package com.example.fiscalcode_java;

import android.view.View;
import android.widget.DatePicker;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.ViewPagerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class VerifyFragmentEspressoTest {

    @ClassRule
    public static final ForceLocaleRule localeTestRule = new ForceLocaleRule(Locale.ITALY);

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void assertFiscalCodeIsVerifiedCorrectly() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.extract_data_button)).perform(click());
        onView(withId(R.id.extract_fragment)).perform(swipeLeft());
        onView(withId(R.id.ver_fiscalCodeInput_input)).perform(typeText("PNCMRT95L52E253R"));
        onView(withId(R.id.ver_first_name_input)).perform(typeText("Marta"));
        onView(withId(R.id.ver_last_name_input)).perform(typeText("Pancaldi"));
        onView(withId(R.id.ver_femaleRadioButton)).perform(click());
        onView(withId(R.id.ver_dob_input)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(1995, 7, 12));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.ver_pob_input)).perform(typeText("Guastalla (RE)"));
        onView(withText("Guastalla (RE)")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.verify_button)).perform(click());
        onView(withId(R.id.ver_fiscalCodeOutput)).check(matches(withText(containsString("Il codice fiscale è corretto"))));
    }
}


