package com.cleanup.todoc;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @author Gaëtan HERFRAY
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void addAndRemoveTask(){


            ActivityScenario<MainActivity> activity = activityRule.getScenario();


            // Obtenir la vue TextView lblNoTask
            //ViewInteraction lblNoTask = onView(withId(R.id.lbl_no_task)).check(matches(isDisplayed()));

            // Obtenir la vue RecyclerView listTasks
            //onView(withId(R.id.list_tasks)).check(matches(isDisplayed()));


            //TextView lblNoTask = activity.onActivity(activity -> )       (R.id.lbl_no_task);
            //RecyclerView listTasks = activity.findViewById(R.id.list_tasks);

            onView(withId(R.id.fab_add_task)).perform(click());
            onView(withId(R.id.txt_task_name)).perform(replaceText("Tâche example"));
            onView(withId(android.R.id.button1)).perform(click());

            // Check that lblTask is not displayed anymore
            onView(withId(R.id.lbl_no_task)).check(matches(not(isDisplayed())));
            // Check that recyclerView is displayed
            onView(withId(R.id.list_tasks)).check(matches(isDisplayed()));
            // Check that it contains one element only
            onView(withId(R.id.list_tasks)).check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (view instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) view;
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    assertThat(adapter.getItemCount(), equalTo(1));
                }
            }
        });

            onView(withId(R.id.img_delete)).perform(click());

            // Check that lblTask is displayed
            onView(withId(R.id.lbl_no_task)).check(matches(isDisplayed()));
            // Check that recyclerView is not displayed anymore
            onView(withId(R.id.list_tasks)).check(matches(not(isDisplayed())));
    }


    @Test
    public void sortTasks() throws InterruptedException {

        //ViewInteraction listTasks = onView(withId(R.id.list_tasks)).check(matches(isDisplayed()));
        //listTasks.perform(RecyclerViewActions.scrollToPosition(0));

        // Attendre que la RecyclerView soit affichée
        //onView(withId(R.id.list_tasks)).check(matches(isDisplayed())).perform(waitFor(5000)); // Attendre 5 secondes


        /*ViewInteraction recyclerView = onView(withId(R.id.list_tasks))
                .check(matches(isDisplayed()));

        recyclerView.check(new ViewAssertion() {
            @Override
            public void check(View view, NoMatchingViewException noViewFoundException) {
                if (view instanceof RecyclerView) {
                    RecyclerView.Adapter adapter = ((RecyclerView) view).getAdapter();
                    if (adapter != null) {
                        int count = adapter.getItemCount();
                        if (count > 0) {
                            for (int i = 1; i <= count; i++) {
                                onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.img_delete))
                                        .perform(click());
                            }
                        }
                    }
                }
            }
        });*/


        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));

    }

}
