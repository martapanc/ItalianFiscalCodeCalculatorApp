package com.pancaldim.fiscalcode_app;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pancaldim.fiscalcode_app.TestConstants.CORRECT_FC;
import static com.pancaldim.fiscalcode_app.TestConstants.INCORRECT_FC;
import static com.pancaldim.fiscalcode_app.TestConstants.OK;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_DAY;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_FISCAL_CODE;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_LAST_NAME;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_MONTH;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_NAME;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_TOWN;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_YEAR;
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
        swipeToVerifyFragment();
        onView(withId(R.id.ver_fiscalCodeInput_input)).perform(typeText(VALID_FISCAL_CODE));
        onView(withId(R.id.ver_first_name_input)).perform(typeText(VALID_NAME));
        onView(withId(R.id.ver_last_name_input)).perform(typeText(VALID_LAST_NAME));
        onView(withId(R.id.ver_femaleRadioButton)).perform(click());
        onView(withId(R.id.ver_dob_input)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(VALID_YEAR, VALID_MONTH, VALID_DAY));
        onView(withText(OK)).perform(click());
        onView(withId(R.id.ver_pob_input)).perform(typeText(VALID_TOWN)).perform(closeSoftKeyboard());
        onView(withText(VALID_TOWN)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.verify_button)).perform(click());
        onView(withId(R.id.ver_fiscalCodeOutput)).check(matches(withText(containsString(CORRECT_FC))));
    }

    @Test
    public void assertFiscalCodeIsNotVerified() {
        swipeToVerifyFragment();
        onView(withId(R.id.ver_fiscalCodeInput_input)).perform(typeText(VALID_FISCAL_CODE));
        onView(withId(R.id.ver_first_name_input)).perform(typeText(VALID_NAME));
        onView(withId(R.id.ver_last_name_input)).perform(typeText(VALID_LAST_NAME));
        onView(withId(R.id.ver_maleRadioButton)).perform(click());
        onView(withId(R.id.ver_dob_input)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(VALID_YEAR, VALID_MONTH, VALID_DAY));
        onView(withText(OK)).perform(click());
        onView(withId(R.id.ver_pob_input)).perform(typeText(VALID_TOWN)).perform(closeSoftKeyboard());
        onView(withText(VALID_TOWN)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.verify_button)).perform(click());
        onView(withId(R.id.ver_fiscalCodeOutput)).check(matches(withText(containsString(INCORRECT_FC))));
    }

    public void swipeToVerifyFragment() {
        onView(withId(R.id.compute_fragment)).perform(swipeLeft());
        onView(withId(R.id.extract_data_button)).perform(click());
        onView(withId(R.id.extract_fragment)).perform(swipeLeft());
    }
}


