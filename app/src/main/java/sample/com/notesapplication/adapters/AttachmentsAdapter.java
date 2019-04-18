package sample.com.notesapplication.adapters;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import sample.com.notesapplication.R;
import sample.com.notesapplication.activities.AttachmentActivity;
import sample.com.notesapplication.datamodel.Attachment;

public class AttachmentsAdapter extends PagerAdapter {
    AttachmentActivity attachmentActivity;
    ArrayList<Attachment> attachmentArrayList = new ArrayList<>();

    public AttachmentsAdapter(AttachmentActivity attachmentActivity) {
        this.attachmentActivity = attachmentActivity;
    }

    @Override
    public int getCount() {
        if (attachmentArrayList == null) {
            return 0;
        } else {
            return attachmentArrayList.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.attachment_item, container, false);
        Attachment attachment = attachmentArrayList.get(position);
        ImageView attachImage = view.findViewById(R.id.attach_image);
        attachImage.setImageBitmap(BitmapFactory.decodeFile(attachment.imagePath));
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public ArrayList<Attachment> getAttachmentArrayList() {

        return attachmentArrayList;
    }
}
