package sample.com.notesapplication.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import sample.com.notesapplication.R;
import sample.com.notesapplication.adapters.NotesAdapter;
import sample.com.notesapplication.datamodel.Notes;
import sample.com.notesapplication.offline.NDataHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, NotesAdapter.OnCheckAttachment {
    public NDataHelper nDataHelper;
    ListView listView;
    FloatingActionButton floatingActionButton;
    ArrayList<Notes> notesArrayList = new ArrayList<>();
    NotesAdapter notesAdapter;
    private final static String TAG = MainActivity.class.getName();
    boolean checkAttach = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view_notes);
        floatingActionButton = findViewById(R.id.notes_fab);

        nDataHelper = new NDataHelper(this);

        notesArrayList.addAll(nDataHelper.getAllNotes());
        notesAdapter = new NotesAdapter(notesArrayList);
        listView.setAdapter(notesAdapter);

        listView.setOnItemClickListener(this);

        floatingActionButton.setOnClickListener(this);
        notesAdapter.setOnCheckAttachment(this);

    }


    private void createNote(String note) {
        long insertNote = nDataHelper.insertNote(note);
        Notes notes = nDataHelper.getNotes(insertNote);
        if (notes != null) {
            notesArrayList.add(notes);
            notesAdapter.notifyDataSetChanged();
        }
    }

    private void updateNotes(String notes, int position) {
        Notes n = notesArrayList.get(position);
        n.setNotes(notes);
        notesArrayList.set(position, n);
        notesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        floatAlert(false, null, -1);
    }

    private void floatAlert(final boolean shouldUpdate, Notes notes, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_newnote, null);

        final EditText newNotes = view.findViewById(R.id.notes_dialog_text);
        CheckBox checkBox = view.findViewById(R.id.notes_checkbox);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle(shouldUpdate ? "Edit Note" : "New Note");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setPositiveButton(shouldUpdate ? "update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String insertNotes = newNotes.getText().toString();
                nDataHelper.insertNote(insertNotes);


                if (shouldUpdate && insertNotes != null) {
                    updateNotes(insertNotes, position);
                } else {
                    createNote(insertNotes);
                }

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                  OnShowAttach(checkAttach);

                } else {
                    OnShowAttach(checkAttach);
                }
            }
        });


        builder.create().show();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence charSequence[] = new String[]{"Edit", "Delete"};
        builder.setTitle("Choose Option");
        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case 0:
                        floatAlert(true, notesArrayList.get(position), position);
                        break;
                    case 1:
                        deleteNotes(position);
                        break;
                }
            }
        });
        builder.show();
    }

    private void deleteNotes(int position) {
        nDataHelper.deleteNotes(notesArrayList.get(position));
        notesArrayList.remove(position);
        notesAdapter.notifyDataSetChanged();
    }




    @Override
    public boolean OnShowAttach(boolean isShow) {
        this.checkAttach=isShow;
        return checkAttach;
    }
}
