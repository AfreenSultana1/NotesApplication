package sample.com.notesapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

public class Utils {

    public interface AlertDialogClickListener {
        String POSITIVE_BUTTON = "pos";
        String NEGATIVE_BUTTON = "neg";
        String NEUTRAL_BUTTON = "neu";

        void onDialogButtonClick(String whichButton);
    }

    public static void showAlert(Context context, String dialogTitle, String dialogBodyMessage,
                                 String positiveText,
                                 String negativeText,
                                 String neutralText, final AlertDialogClickListener alertDialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(dialogTitle)) {
            builder.setTitle(dialogTitle);
        }
        if (!TextUtils.isEmpty(dialogBodyMessage)) {
            builder.setTitle(dialogBodyMessage);
        }
        builder.setCancelable(false);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (alertDialogClickListener != null) {
                    alertDialogClickListener.onDialogButtonClick(AlertDialogClickListener.POSITIVE_BUTTON);
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
           if(alertDialogClickListener!=null){
               alertDialogClickListener.onDialogButtonClick(AlertDialogClickListener.NEGATIVE_BUTTON);
           }
           dialogInterface.dismiss();
            }
        });

        builder.setNeutralButton(neutralText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(alertDialogClickListener!=null){
                    alertDialogClickListener.onDialogButtonClick(AlertDialogClickListener.NEUTRAL_BUTTON);
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}
