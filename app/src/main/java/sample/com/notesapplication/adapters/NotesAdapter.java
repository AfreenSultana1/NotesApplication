package sample.com.notesapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import sample.com.notesapplication.R;
import sample.com.notesapplication.activities.AttachmentActivity;
import sample.com.notesapplication.datamodel.Notes;

public class NotesAdapter extends BaseAdapter {
    Context context;
    ArrayList<Notes> notesArrayList = new ArrayList<>();

    OnCheckAttachment onCheckAttachment;

    public void setOnCheckAttachment(OnCheckAttachment onCheckAttachment) {
        this.onCheckAttachment = onCheckAttachment;
    }

    public NotesAdapter(ArrayList<Notes> notesArrayList) {
        this.notesArrayList = notesArrayList;
    }

    @Override
    public int getCount() {
        return notesArrayList.size();
    }

    @Override
    public Notes getItem(int i) {
        return notesArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_notes, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.updateData(getItem(position));
        return convertView;
    }


    public class ViewHolder {
        TextView dot;
        TextView timeStamp;
        TextView notesTextview;
        ImageView attachImageView;



        public ViewHolder(View view) {
            notesTextview = view.findViewById(R.id.notesText);
            dot = view.findViewById(R.id.dot);
            timeStamp = view.findViewById(R.id.timeStamp);
            attachImageView = view.findViewById(R.id.attach);

        }

        public void updateData(final Notes notes) {
            notesTextview.setText(notes.notes);

            timeStamp.setText(dateFormat(notes.timeStamp));

            if (onCheckAttachment.OnShowAttach(true)) {
                attachImageView.setVisibility(View.VISIBLE);
            }else{
                attachImageView.setVisibility(View.INVISIBLE);
            }


            attachImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "Attachment", Toast.LENGTH_SHORT).show();

                    Intent attachmentIntent = new Intent(context, AttachmentActivity.class);
                    context.startActivity(attachmentIntent);
                }
            });

        }


    }

    public interface OnCheckAttachment {
        boolean OnShowAttach(boolean isShow);
    }


    private String dateFormat(String dateStr) {
        try {

            dateStr = DateFormat.getDateTimeInstance().format(new Date());

            return dateStr;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";

    }
}
