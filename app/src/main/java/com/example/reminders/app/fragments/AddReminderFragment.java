package com.example.reminders.app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.reminders.app.R;
import com.example.reminders.app.dal.ReminderNoteDal;
import com.example.reminders.app.dal.dto.ReminderNote;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.roboguice.shaded.goole.common.base.Function;
import org.roboguice.shaded.goole.common.collect.Lists;

public class AddReminderFragment extends RoboFragment {

    private ReminderNoteDal reminderNoteDal;
    private ArrayAdapter<String> arrayAdapter;
    private View rootView;
    private ListView listView;
    private List<String> comments;
    private Set<String> commentsSet;
    private EditText commentEditText;

    private AutoCompleteTextView autoCompleteTextView;
    private String acChosenNumber;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AddReminderFragment() {

    }

    public void init(Context applicationContext) {
        this.reminderNoteDal = new ReminderNoteDal(applicationContext);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initViews(inflater, container);
        init(getActivity().getApplicationContext());
        initSearchButton();
        initAddButton();
        initList();
        return rootView;
    }


    private void initViews(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        setCommentsVisibility(rootView, false);

        listView = ((ListView) rootView.findViewById(R.id.comments_list));
        commentEditText = ((EditText) rootView.findViewById(R.id.comment_text));
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.phone_editText);
    }

    private void initSearchButton() {
        Button searchButton = (Button) rootView.findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneComments(rootView);
                setCommentsVisibility(rootView, true);
            }
        });
    }

    private void initAddButton() {
        Button addButton = (Button) rootView.findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ReminderNote remindernote = new ReminderNote();
                remindernote.author = acChosenNumber;
                remindernote.note = commentEditText.getText().toString();
                reminderNoteDal.addItem(remindernote);

                commentEditText.setText("");
            }
        });
    }

    private void initList() {

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                comments.remove(pos);
                //listView.invalidate();
                arrayAdapter.notifyDataSetChanged();
                return true;
            }
        });


        comments = new ArrayList<>();
        commentsSet = new HashSet<>();
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        arrayAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                comments);

        listView.setAdapter(arrayAdapter);
    }

    private void refreshList(String comment) {
        if (!commentsSet.contains(comment)) {
            commentsSet.add(comment);
            comments.add(comment);
            //listView.invalidate();
            arrayAdapter.notifyDataSetChanged();
        }
    }

    private void setCommentsVisibility(View rootView, boolean isVisible) {
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.bottom_container);
        relativeLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void getPhoneComments(View rootView) {
        String author = ((EditText) rootView.findViewById(R.id.phone_editText)).getText().toString();
        ArrayList<ReminderNote> reminders = reminderNoteDal.getAllRegexItems(author);
        if (reminders != null) {

            //TODO:table instead of list
            List<String> stringReminders = Lists.transform(reminders, new Function<ReminderNote, String>() {
                public String apply(ReminderNote reminderNote) {
                    return reminderNote.author + " - " + reminderNote.note;
                }
            });

            comments.addAll(stringReminders);
            commentsSet.addAll(comments);
        }
        arrayAdapter.notifyDataSetChanged();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.comments_list) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.phone_list_contextual_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
//            case R.id.add:
//                // add stuff here
//                return true;
            case R.id.edit: {
                return true;
            }
            case R.id.delete: {
                String comment = comments.get((int) info.id);
                comments.remove(comment);
                commentsSet.remove(comment);
                listView.invalidate();

                return true;
            }
            default: {
                return super.onContextItemSelected(item);
            }
        }
    }
}
