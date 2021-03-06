package com.pancaldim.fiscalcode_app;

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
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.pancaldim.fiscalcode_app.TestConstants.INVALID_DOB;
import static com.pancaldim.fiscalcode_app.TestConstants.INVALID_INPUT;
import static com.pancaldim.fiscalcode_app.TestConstants.INVALID_LAST_NAME;
import static com.pancaldim.fiscalcode_app.TestConstants.INVALID_NAME;
import static com.pancaldim.fiscalcode_app.TestConstants.INVALID_TOWN;
import static com.pancaldim.fiscalcode_app.TestConstants.INVALID_YEAR;
import static com.pancaldim.fiscalcode_app.TestConstants.OK;
import static com.pancaldim.fiscalcode_app.TestConstants.REQUIRED_VALUE;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_DAY;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_FISCAL_CODE;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_LAST_NAME;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_MONTH;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_NAME;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_TOWN;
import static com.pancaldim.fiscalcode_app.TestConstants.VALID_YEAR;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ComputeFragmentEspressoTest {

    @ClassRule
    public static final ForceLocaleRule localeTestRule = new ForceLocaleRule(Locale.ITALY);

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void assertFiscalCodeIsComputedSuccessfully() {
        onView(withId(R.id.compute_fragment)).check(matches(isDisplayed()));
        typeValidInput();
        onView(withId(R.id.compute_button)).perform(click());
        onView(withId(R.id.com_fiscalCodeOutput)).check(matches(withText(containsString(VALID_FISCAL_CODE))));
    }

    @Test
    public void assertShowsEmptyValueErrorMessages() {
        onView(withId(R.id.compute_fragment)).check(matches(isDisplayed())).perform(closeSoftKeyboard());
        onView(withId(R.id.compute_button)).perform(click());
        checkAllFieldsHaveEmptyValueError();
    }

    @Test
    public void assertShowsInvalidInputErrorMessages() {
        onView(withId(R.id.com_first_name_input)).perform(typeText(INVALID_NAME));
        onView(withId(R.id.com_last_name_input)).perform(typeText(INVALID_LAST_NAME));
        onView(withId(R.id.com_dob_input)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(INVALID_YEAR, VALID_MONTH, VALID_DAY));
        onView(withText(OK)).perform(click());
        onView(withId(R.id.com_pob_input)).perform(typeText(INVALID_TOWN)).perform(closeSoftKeyboard());
        onView(withId(R.id.compute_button)).perform(click());

        onView(withId(R.id.com_first_name_input)).check(matches(hasErrorText(INVALID_INPUT)));
        onView(withId(R.id.com_last_name_input)).check(matches(hasErrorText(INVALID_INPUT)));
        onView(withId(R.id.com_dob_input)).check(matches(withText(INVALID_DOB)));
        onView(withId(R.id.com_pob_input)).check(matches(hasErrorText(INVALID_INPUT)));
    }

    @Test
    public void assertResetButtonResetsAllFields() {
        onView(withId(R.id.compute_reset_button)).perform(click());
        onView(withId(R.id.com_first_name_input)).check(matches(withText(isEmptyOrNullString())));

        typeValidInput();
        onView(withText(VALID_TOWN)).perform(click()).perform(closeSoftKeyboard());
        onView(withId(R.id.compute_reset_button)).perform(click());
        onView(withId(R.id.com_first_name_input)).check(matches(withText(isEmptyOrNullString())));

        onView(withId(R.id.compute_button)).perform(click());
        checkAllFieldsHaveEmptyValueError();
    }

    private void typeValidInput() {
        onView(withId(R.id.com_first_name_input)).perform(typeText(VALID_NAME));
        onView(withId(R.id.com_last_name_input)).perform(typeText(VALID_LAST_NAME));
        onView(withId(R.id.com_femaleRadioButton)).perform(click());
        onView(withId(R.id.com_dob_input)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(VALID_YEAR, VALID_MONTH, VALID_DAY));
        onView(withText(OK)).perform(click());
        onView(withId(R.id.com_pob_input)).perform(typeText(VALID_TOWN)).perform(closeSoftKeyboard());
    }

    private void checkAllFieldsHaveEmptyValueError() {
        onView(withId(R.id.com_first_name_input)).check(matches(hasErrorText(REQUIRED_VALUE)));
        onView(withId(R.id.com_last_name_input)).check(matches(hasErrorText(REQUIRED_VALUE)));
        onView(withId(R.id.com_femaleRadioButton)).check(matches(isNotChecked()));
        onView(withId(R.id.com_maleRadioButton)).check(matches(isNotChecked()));
        onView(withId(R.id.com_dob_input)).check(matches(withText(EMPTY)));
        onView(withId(R.id.com_pob_input)).check(matches(hasErrorText(REQUIRED_VALUE)));
    }
}
