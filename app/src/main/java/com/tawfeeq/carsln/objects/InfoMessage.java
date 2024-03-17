package com.tawfeeq.carsln.objects;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.tawfeeq.carsln.activities.MainActivity;
import com.tawfeeq.carsln.R;
import com.tawfeeq.carsln.fragments.LogInFragment;

public class InfoMessage {

    private static InfoMessage instance;

    public InfoMessage() {
    }

    public static InfoMessage getInstance()
    {
        if (instance == null)
            instance = new InfoMessage();

        return instance;
    }

    public void LogoutMessage(Context context, FragmentTransaction ft) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are You Sure You Want to Logout");
        FireBaseServices fbs = FireBaseServices.getInstance();

        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                fbs.getAuth().signOut();
                fbs.setMarketList(null);
                GoToLogIn(context, ft);
                setNavigationBarGone(context);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void GoToLogIn(Context context, FragmentTransaction ft) {

        ft.replace(R.id.FrameLayoutMain, new LogInFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    private void setNavigationBarGone(Context context) {
        ((MainActivity) context).getBottomNavigationView().setVisibility(View.GONE);
    }

}
