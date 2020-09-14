package com.noelrmrz.pokedex;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.intent.Checks;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.noelrmrz.pokedex.ui.detail.DetailFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DetailFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void init(){
        DetailFragment fragment = new DetailFragment();
        testRule.getActivity()
                .getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_container, fragment).commit();;
    }

    @Test
    public void testSecondFragment() {
        onView(withId(R.id.favorite_fab)).perform(click());
        onView(withId(R.id.favorite_fab)).check(matches(withTextColor(R.color.fire)));
    }

    public static Matcher<View> withTextColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, EditText>(EditText.class) {
            @Override
            public boolean matchesSafely(EditText warning) {
                return color == warning.getCurrentTextColor();
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: ");
            }
        };
    }
}
