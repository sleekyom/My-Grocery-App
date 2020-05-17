package com.app.mygrocery.Data;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mygrocery.Activities.ListActivity;
import com.app.mygrocery.Activities.MainActivity;
import com.app.mygrocery.Model.Grocery;
import com.app.mygrocery.UI.RecyclerViewAdapter;
import com.app.mygrocery.Util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by paulodichone on 4/7/17.
 */

public class DatabaseHandler {

    private Context ctx;

    public DatabaseHandler(Context context) {
        this.ctx = context;
    }

    public void addGrocery(Grocery grocery, final AlertDialog dialog, final View v) {

        final ProgressDialog Progressdialog = new ProgressDialog(ctx);
        Progressdialog.setMessage("Saving..");
        Progressdialog.setCancelable(false);
        Progressdialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        grocery.setId( db.collection("Items").getId());
        db.collection("Items")
                .add(grocery).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Progressdialog.dismiss();
                if (task.isSuccessful()) {

                    Snackbar.make(v, "Item Saved!", Snackbar.LENGTH_LONG).show();


                } else {

                    Snackbar.make(v, "Failed To  Save", Snackbar.LENGTH_LONG).show();
                }


                // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        //start a new activity
                        ctx.startActivity(new Intent(ctx, ListActivity.class));
                        getActivity().finish();
                    }
                }, 1200); //  1 second.

            }
        });


    }

    private Activity getActivity() {

        return (Activity) ctx;
    }

    public void getAllGroceries(final RecyclerView recyclerView) {

        final ProgressDialog Progressdialog = new ProgressDialog(ctx);
        Progressdialog.setMessage("Loading Items..");
        Progressdialog.setCancelable(false);
        Progressdialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Items")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                Progressdialog.dismiss();
                if (task.isSuccessful()) {

                    List<Grocery> list=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult())
                    {

                        Grocery obj=new Grocery();

                        obj.setDateItemAdded((Timestamp) document.get("dateItemAdded"));
                        obj.setId(document.getId());
                        obj.setQuantity(String.valueOf(document.get("quantity")));
                        obj.setName(String.valueOf(document.get("name")));
                        list.add(obj);
                    }

                    if(list.size()<1)
                    {
                        Toast.makeText(getActivity(),"No Items In Database",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), list));
                    }
                }
                else
                    {
                        Toast.makeText(getActivity(), "Failed To Get Results", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

    public void deleteGrocery(String id)
    {

        final ProgressDialog Progressdialog = new ProgressDialog(ctx);
        Progressdialog.setMessage("Deleting..");
        Progressdialog.setCancelable(false);
        Progressdialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Items").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(getActivity(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                }
                else
                {
                    Toast.makeText(getActivity(),"Failed To Delete",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void updateGrocery(Grocery grocery)
    {

        final ProgressDialog Progressdialog = new ProgressDialog(ctx);
        Progressdialog.setMessage("Updating");
        Progressdialog.setCancelable(false);
        Progressdialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference reference = db.collection("Items").document(grocery.getId());
        reference.update("name",grocery.getName());
        reference.update("quantity",grocery.getQuantity()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Toast.makeText(getActivity(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                }
                else
                {
                    Toast.makeText(getActivity(),"Failed To Updated",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
